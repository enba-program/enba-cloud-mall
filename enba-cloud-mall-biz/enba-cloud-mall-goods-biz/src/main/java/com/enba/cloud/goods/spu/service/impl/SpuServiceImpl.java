package com.enba.cloud.goods.spu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.boot.core.context.SecurityContextHolder;
import com.enba.boot.core.exception.BizException;
import com.enba.cloud.common.enums.SpuAuditStatusEnum;
import com.enba.cloud.common.enums.SpuIsSpuEnum;
import com.enba.cloud.common.enums.SpuShelveStatusEnum;
import com.enba.cloud.common.utils.Md5Util;
import com.enba.cloud.common.utils.PageUtil;
import com.enba.cloud.file.api.client.FileCommonClient;
import com.enba.cloud.goods.api.sku.entity.Sku;
import com.enba.cloud.goods.api.sku.entity.SkuSpec;
import com.enba.cloud.goods.api.sku.entity.SkuSpecEnum;
import com.enba.cloud.goods.sku.manager.SkuCodeGenerator;
import com.enba.cloud.goods.sku.mapper.SkuMapper;
import com.enba.cloud.goods.sku.mapper.SkuSpecEnumMapper;
import com.enba.cloud.goods.sku.mapper.SkuSpecMapper;
import com.enba.cloud.goods.api.spu.bo.SpecNameValueBo;
import com.enba.cloud.goods.api.spu.entity.Spu;
import com.enba.cloud.goods.spu.manager.SpuCodeGenerator;
import com.enba.cloud.goods.spu.mapper.SpuMapper;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest.SkuInfo;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest.SpuInfoRequest;
import com.enba.cloud.goods.api.spu.req.SpuFindPageReq;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse.SkuInfoResp;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse.SpecValue;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse.SpuInfoResp;
import com.enba.cloud.goods.api.spu.resp.SpuPageResp;
import com.enba.cloud.goods.api.spu.resp.SpuResp;
import com.enba.cloud.goods.spu.service.ISpuService;
import com.enba.cloud.goods.spu.tool.ProductSynchTool;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.elasticsearch.common.util.set.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * SPU表 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements ISpuService {
  private final SpuMapper spuMapper;
  private final SkuMapper skuMapper;
  private final SkuSpecMapper skuSpecMapper;
  private final SkuSpecEnumMapper skuSpecEnumMapper;
  private final ProductSynchTool productSynchTool;
  private final FileCommonClient fileCommonClient;

  public SpuServiceImpl(
      SpuMapper spuMapper,
      SkuMapper skuMapper,
      SkuSpecMapper skuSpecMapper,
      SkuSpecEnumMapper skuSpecEnumMapper,
      ProductSynchTool productSynchTool,
      FileCommonClient fileCommonClient) {
    this.spuMapper = spuMapper;
    this.skuMapper = skuMapper;
    this.skuSpecMapper = skuSpecMapper;
    this.skuSpecEnumMapper = skuSpecEnumMapper;
    this.productSynchTool = productSynchTool;
    this.fileCommonClient = fileCommonClient;
  }

  @Transactional(rollbackFor = Exception.class)
  public Long createProduct(ProductCreateRequest request) {
    // 0. 参数校验
    request.checkSpectNameRepeat();

    // 1. 创建SPU记录
    Spu spu = insertSpu(request);

    // 2.添加文件业务ID （spu主图,spu详情图）
    updateFileBizId(request, spu);

    Long spuId = spu.getId();

    // 3. 处理规格
    insertSpecEnum(request, spuId);

    // 4. 创建SKU记录
    insertSku(request, spuId, spu);

    return spuId;
  }

  @Override
  public Boolean submitAudit(Long spuId) {
    if (!Objects.equals(
        SpuAuditStatusEnum.DRAFT.getAuditStatus(), spuMapper.selectById(spuId).getAuditStatus())) {
      BizException.throwEx("仅支持草稿状态提交审核");
    }

    Spu spu = new Spu();
    spu.setId(spuId);
    spu.setAuditStatus(SpuAuditStatusEnum.WAIT_AUDIT.getAuditStatus());
    return spuMapper.updateById(spu) > 0;
  }

  /**
   * 创建SPU
   *
   * @param request request
   * @return spuId
   */
  private Spu insertSpu(ProductCreateRequest request) {
    Spu spu = new Spu();
    // 填充属性
    fillApuAttributes(request, spu);
    spuMapper.insert(spu);
    return spu;
  }

  /**
   * 处理规格
   *
   * @param request request
   * @param spuId spuId
   */
  private void insertSpecEnum(ProductCreateRequest request, Long spuId) {
    if (Objects.equals(request.getSpuInfo().getIsSpu(), SpuIsSpuEnum.MULTIPLE.getIsSpu())) {
      // 多规格商品，保存规格名称
      for (ProductCreateRequest.SpecInfo specInfo : request.getSpecList()) {
        SkuSpec spec = new SkuSpec();
        spec.setSpuId(spuId);
        spec.setSpecName(specInfo.getSpecName());
        skuSpecMapper.insert(spec);

        Long specId = spec.getId();

        // 保存规格值
        for (ProductCreateRequest.SpecValue specValue : specInfo.getSpecValueList()) {
          SkuSpecEnum specEnum = new SkuSpecEnum();
          specEnum.setSpecId(specId);
          specEnum.setSpecValue(specValue.getSpecValue());
          skuSpecEnumMapper.insert(specEnum);
        }
      }
    }
  }

  /**
   * 创建SKU
   *
   * @param request request
   * @param spuId spuId
   * @param spu spu
   */
  private void insertSku(ProductCreateRequest request, Long spuId, Spu spu) {
    for (int i = 0; i < request.getSkuList().size(); i++) {
      SkuInfo skuInfo = request.getSkuList().get(i);

      Sku sku = new Sku();
      // 设置SKU字段...
      fillSKuAttributes(spuId, sku, skuInfo);

      skuMapper.insert(sku);

      if (i == 0) {
        // spu设置默认展示sku
        spu.setDefaultSkuId(sku.getId());
        spuMapper.updateById(spu);
      }

      // 添加文件业务ID （sku主图）
      fileCommonClient.updateFileBizId(Sets.newHashSet(skuInfo.getSkuPicId()), sku.getId());
    }
  }

  /**
   * 添加文件业务ID
   *
   * @param request request
   * @param spu spu
   */
  private void updateFileBizId(ProductCreateRequest request, Spu spu) {
    Set<Long> idSet = Sets.newHashSet();
    // 主图
    Long headerPicId = request.getSpuInfo().getHeaderPicId();
    // 详情图
    Set<Long> spuDetailPicIdSet = request.getSpuInfo().getSpuDetailPicIdSet();
    idSet.add(headerPicId);
    idSet.addAll(spuDetailPicIdSet);
    fileCommonClient.updateFileBizId(idSet, spu.getId());
  }

  /**
   * 填充属性
   *
   * @param spuId spuId
   * @param sku sku对象
   * @param skuInfo skuInfo对象
   */
  private void fillSKuAttributes(Long spuId, Sku sku, SkuInfo skuInfo) {
    sku.setSpuId(spuId);
    sku.setSkuCode(SkuCodeGenerator.generateCode());
    sku.setSkuName(skuInfo.getSkuName());
    sku.setSkuPicUrl(skuInfo.getSkuPicUrl());
    sku.setMoq(skuInfo.getMoq());
    sku.setInventory(skuInfo.getInventory());
    sku.setSkuPrice(skuInfo.getSkuPrice());
    sku.setSkuDesc(skuInfo.getSkuDesc());
    sku.setStatus(skuInfo.getStatus());

    String skuSpecEnums = skuInfo.getSkuSpecEnums();
    if (StringUtils.hasText(skuSpecEnums)) {
      // 根据 attrName，valueName 获取attrId，valueId
      List<SpecNameValueBo> specNameValueBoList =
          JSON.parseArray(skuInfo.getSkuSpecEnums(), SpecNameValueBo.class);

      for (SpecNameValueBo bo : specNameValueBoList) {
        SkuSpec spec =
            skuSpecMapper.selectOne(
                new LambdaQueryWrapper<SkuSpec>()
                    .eq(SkuSpec::getSpuId, spuId)
                    .eq(SkuSpec::getSpecName, bo.getAttrName()));
        // 获取attrId
        bo.setAttrId(spec.getId());

        SkuSpecEnum specEnum =
            skuSpecEnumMapper.selectOne(
                new LambdaQueryWrapper<SkuSpecEnum>()
                    .eq(SkuSpecEnum::getSpecId, bo.getAttrId())
                    .eq(SkuSpecEnum::getSpecValue, bo.getValueName()));
        // 获取valueId
        bo.setValueId(specEnum.getId());
      }

      sku.setSkuSpecEnums(JSON.toJSONString(specNameValueBoList));
      sku.setSkuSpecMd5(Md5Util.MD5Encode(skuInfo.getSkuSpecEnums()));
    }
  }

  /**
   * 填充属性
   *
   * @param request 请求参数
   * @param spu spu对象
   */
  private void fillApuAttributes(ProductCreateRequest request, Spu spu) {
    SpuInfoRequest spuInfo = request.getSpuInfo();
    spu.setUnitId(spuInfo.getUnitId());
    spu.setBrandId(spuInfo.getBrandId());
    spu.setCategoryId(spuInfo.getCategoryId());
    spu.setSpuCode(SpuCodeGenerator.generateCode());
    spu.setSpuName(spuInfo.getSpuName());
    spu.setSpuDesc(spuInfo.getSpuDesc());
    spu.setHeaderPicUrl(spuInfo.getHeaderPicUrl());
    spu.setIsSpu(spuInfo.getIsSpu());
    spu.setIsRefund(spuInfo.getIsRefund());
    spu.setIsPost(spuInfo.getIsPost());
    spu.setInstallList(spuInfo.getInstallList());
    spu.setSpecPackage(spuInfo.getSpecPackage());
    spu.setPostSaleGuarantee(spuInfo.getPostSaleGuarantee());
    spu.setShelveStatus(SpuShelveStatusEnum.WAIT_SHELVE.getShelveStatus());
    spu.setAuditStatus(SpuAuditStatusEnum.DRAFT.getAuditStatus());
  }

  public ProductDetailResponse getProductDetail(Long spuId) {
    // 1. 查询SPU信息
    Spu spu = spuMapper.selectById(spuId);
    if (spu == null) {
      BizException.throwEx("商品不存在");
    }

    ProductDetailResponse response = new ProductDetailResponse();
    // 设置SPU字段...
    fillSpuResponseAttributes(response, spu);

    // 2. 查询SKU列表
    List<Sku> skuList = skuMapper.selectList(Wrappers.<Sku>lambdaQuery().eq(Sku::getSpuId, spuId));
    List<SkuInfoResp> skuInfoResps = new ArrayList<>();
    for (Sku sku : skuList) {
      SkuInfoResp skuInfoResp = new SkuInfoResp();
      // 设置SKU字段...
      fillSKuResponseAttributes(skuInfoResp, sku);

      skuInfoResps.add(skuInfoResp);
    }
    response.setSkuList(skuInfoResps);

    // 3. 查询规格列表
    if (Objects.equals(spu.getIsSpu(), SpuIsSpuEnum.MULTIPLE.getIsSpu())) {
      // 查询参数名
      List<SkuSpec> specList =
          skuSpecMapper.selectList(Wrappers.<SkuSpec>lambdaQuery().eq(SkuSpec::getSpuId, spuId));

      List<ProductDetailResponse.SpecDetail> specDetails = new ArrayList<>();
      for (SkuSpec spec : specList) {
        ProductDetailResponse.SpecDetail specDetail = new ProductDetailResponse.SpecDetail();
        specDetail.setId(spec.getId());
        specDetail.setSpecName(spec.getSpecName());

        // 查询参数值
        List<SkuSpecEnum> enumList =
            skuSpecEnumMapper.selectList(
                Wrappers.<SkuSpecEnum>lambdaQuery().eq(SkuSpecEnum::getSpecId, spec.getId()));

        List<SpecValue> specValues = new ArrayList<>();
        for (SkuSpecEnum specEnum : enumList) {
          SpecValue specValue = new SpecValue();
          specValue.setId(specEnum.getId());
          specValue.setSpecId(specEnum.getSpecId());
          specValue.setSpecValue(specEnum.getSpecValue());
          specValue.setSpecName(spec.getSpecName());
          specValues.add(specValue);
        }
        specDetail.setValues(specValues);

        specDetails.add(specDetail);
      }

      // 参数列表
      response.setSpecList(specDetails);
    }

    return response;
  }

  /**
   * 填充属性
   *
   * @param skuInfoResp skuDetail对象
   * @param sku sku对象
   */
  private void fillSKuResponseAttributes(SkuInfoResp skuInfoResp, Sku sku) {
    skuInfoResp.setId(sku.getId());
    skuInfoResp.setSkuCode(sku.getSkuCode());
    skuInfoResp.setSkuName(sku.getSkuName());
    skuInfoResp.setSkuPicUrl(sku.getSkuPicUrl());
    skuInfoResp.setMoq(sku.getMoq());
    skuInfoResp.setInventory(sku.getInventory());
    skuInfoResp.setSkuPrice(sku.getSkuPrice());
    skuInfoResp.setSkuSpecEnums(sku.getSkuSpecEnums());
    skuInfoResp.setSkuSpecMd5(sku.getSkuSpecMd5());
    skuInfoResp.setSkuDesc(sku.getSkuDesc());
    skuInfoResp.setStatus(sku.getStatus());
    skuInfoResp.setCreateTime(sku.getCreateTime());
  }

  /**
   * 填充属性
   *
   * @param response 响应参数
   * @param spu spu对象
   */
  private void fillSpuResponseAttributes(ProductDetailResponse response, Spu spu) {
    SpuInfoResp spuInfo = new SpuInfoResp();
    spuInfo.setId(spu.getId());
    spuInfo.setUnitId(spu.getUnitId());
    spuInfo.setBrandId(spu.getBrandId());
    spuInfo.setCategoryId(spu.getCategoryId());
    spuInfo.setSpuCode(spu.getSpuCode());
    spuInfo.setSpuName(spu.getSpuName());
    spuInfo.setSpuDesc(spu.getSpuDesc());
    spuInfo.setHeaderPicUrl(spu.getHeaderPicUrl());
    spuInfo.setDefaultSkuId(spu.getDefaultSkuId());
    spuInfo.setIsSpu(spu.getIsSpu());
    spuInfo.setIsRefund(spu.getIsRefund());
    spuInfo.setIsPost(spu.getIsPost());
    spuInfo.setInstallList(spu.getInstallList());
    spuInfo.setSpecPackage(spu.getSpecPackage());
    spuInfo.setPostSaleGuarantee(spu.getPostSaleGuarantee());
    spuInfo.setShelveStatus(spu.getShelveStatus());
    spuInfo.setAuditStatus(spu.getAuditStatus());
    spuInfo.setCreateTime(spu.getCreateTime());

    response.setSpuInfo(spuInfo);
  }

  @Override
  public List<SpuResp> findByCategoryId(Long categoryId) {
    List<Spu> spuList =
        spuMapper.selectList(Wrappers.<Spu>lambdaQuery().eq(Spu::getCategoryId, categoryId));

    return BeanUtil.copyToList(spuList, SpuResp.class);
  }

  @Override
  public Page<SpuPageResp> findPage(SpuFindPageReq pageReq) {
    // 获取查询条件
    LambdaQueryWrapper<Spu> queryWrapper = getQueryWrapper(pageReq);

    Page<SpuPageResp> page =
        PageUtil.getPage(
            () ->
                spuMapper.selectPage(
                    new Page<>(pageReq.getPageNum(), pageReq.getPageSize()), queryWrapper),
            SpuPageResp.class);

    // 审核状态判断
    canAudit(page);

    return page;
  }

  /**
   * 审核状态判断 设置canAudit属性
   *
   * @param spuPageRespPage spuPageRespPage
   */
  private void canAudit(Page<SpuPageResp> spuPageRespPage) {
    Long userId = SecurityContextHolder.getUserId();

    // 遍历赋值 canAudit属性
    spuPageRespPage
        .getRecords()
        .forEach(
            spuPageResp -> {
              List<Long> userIdList = getSpuAuditUserIdList(spuPageResp.getId());
              spuPageResp.setCanAudit(userIdList.contains(0L) || userIdList.contains(userId));
            });
  }

  /**
   * 获取审核人ID列表，仅支持spu所属分类管理员操作审核
   *
   * @param spuId spuId
   * @return 审核人ID列表
   */
  private List<Long> getSpuAuditUserIdList(Long spuId) {
    // spu所属分类 根据分类获取分类管理员ID列表

    return new ArrayList();
  }

  @Override
  public Boolean audit(Long spuId, Integer auditStatus) {
    Spu spu = spuMapper.selectById(spuId);

    // 仅支持待审核状态操作
    if (!Objects.equals(SpuAuditStatusEnum.WAIT_AUDIT.getAuditStatus(), spu.getAuditStatus())) {
      BizException.throwEx("仅支持待审核状态操作");
    }

    if (!SpuAuditStatusEnum.AUDIT_PASS.getAuditStatus().equals(auditStatus)
        && !SpuAuditStatusEnum.AUDIT_FAIL.getAuditStatus().equals(auditStatus)) {
      BizException.throwEx("仅支持审核通过或审核拒绝操作");
    }

    return spuMapper.update(
            null,
            Wrappers.<Spu>lambdaUpdate()
                .set(Spu::getAuditStatus, auditStatus)
                .eq(Spu::getId, spuId))
        > 0;
  }

  @Override
  public Boolean shelve(Long spuId, Integer shelveStatus) {
    Spu spu = spuMapper.selectById(spuId);

    if (!Objects.equals(SpuAuditStatusEnum.AUDIT_PASS.getAuditStatus(), spu.getAuditStatus())) {
      BizException.throwEx("仅支持审核通过状态操作");
    }

    if (Objects.equals(SpuShelveStatusEnum.WAIT_SHELVE.getShelveStatus(), spu.getShelveStatus())) {
      // spu为待上架状态 只允许操作上架
      if (!Objects.equals(SpuShelveStatusEnum.ON_SHELVE.getShelveStatus(), shelveStatus)) {
        BizException.throwEx("待上架商品仅支持上架操作");
      }
    }

    if (Objects.equals(SpuShelveStatusEnum.OFF_SHELVE.getShelveStatus(), spu.getShelveStatus())) {
      // spu为待上架状态 只允许操作上架
      if (!Objects.equals(SpuShelveStatusEnum.ON_SHELVE.getShelveStatus(), shelveStatus)) {
        BizException.throwEx("已下架商品仅支持上架操作");
      }
    }

    if (Objects.equals(SpuShelveStatusEnum.ON_SHELVE.getShelveStatus(), spu.getShelveStatus())) {
      // spu为待上架状态 只允许操作上架
      if (!Objects.equals(SpuShelveStatusEnum.OFF_SHELVE.getShelveStatus(), shelveStatus)) {
        BizException.throwEx("已上架商品仅支持下架操作");
      }
    }

    // 更新
    spuMapper.update(
        null,
        Wrappers.<Spu>lambdaUpdate().set(Spu::getShelveStatus, shelveStatus).eq(Spu::getId, spuId));

    // 同步es
    productSynchTool.synchSpu(spuId);

    return Boolean.TRUE;
  }

  /**
   * 获取查询条件
   *
   * @param pageReq 查询参数
   * @return 查询条件
   */
  private static  LambdaQueryWrapper<Spu> getQueryWrapper(SpuFindPageReq pageReq) {
    LambdaQueryWrapper<Spu> queryWrapper = Wrappers.lambdaQuery();

    // 如果不为空 则添加搜索条件
    if (StringUtils.hasText(pageReq.getSpuName())) {
      queryWrapper.like(Spu::getSpuName, pageReq.getSpuName());
    }
    if (StringUtils.hasText(pageReq.getSpuCode())) {
      queryWrapper.eq(Spu::getSpuCode, pageReq.getSpuCode());
    }
    if (pageReq.getBrandId() != null) {
      queryWrapper.eq(Spu::getBrandId, pageReq.getBrandId());
    }
    if (pageReq.getUnitId() != null) {
      queryWrapper.eq(Spu::getUnitId, pageReq.getUnitId());
    }
    if (pageReq.getCategoryId() != null) {
      queryWrapper.eq(Spu::getCategoryId, pageReq.getCategoryId());
    }
    if (pageReq.getIsSpu() != null) {
      queryWrapper.eq(Spu::getIsSpu, pageReq.getIsSpu());
    }
    if (pageReq.getIsRefund() != null) {
      queryWrapper.eq(Spu::getIsRefund, pageReq.getIsRefund());
    }
    if (pageReq.getIsPost() != null) {
      queryWrapper.eq(Spu::getIsPost, pageReq.getIsPost());
    }
    if (pageReq.getShelveStatus() != null) {
      queryWrapper.eq(Spu::getShelveStatus, pageReq.getShelveStatus());
    }
    if (pageReq.getAuditStatus() != null) {
      queryWrapper.eq(Spu::getAuditStatus, pageReq.getAuditStatus());
    }
    queryWrapper.orderByDesc(Spu::getCreateTime);
    return queryWrapper;
  }
}
