package com.enba.cloud.goods.spu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.enba.cloud.goods.api.spu.entity.Spu;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest;
import com.enba.cloud.goods.api.spu.req.SpuFindPageReq;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse;
import com.enba.cloud.goods.api.spu.resp.SpuPageResp;
import com.enba.cloud.goods.api.spu.resp.SpuResp;
import java.util.List;

/**
 * SPU表 服务类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
public interface ISpuService extends IService<Spu> {

  /**
   * 创建商品
   *
   * @param request 请求参数
   * @return 商品spuID
   */
  Long createProduct(ProductCreateRequest request);

  /**
   * 提交审核
   *
   * @param spuId 商品ID
   */
  Boolean submitAudit(Long spuId);

  /**
   * 获取商品详情
   *
   * @param spuId 商品ID
   * @return 商品详情
   */
  ProductDetailResponse getProductDetail(Long spuId);

  /**
   * 根据三级分类ID获取SPU列表
   *
   * @param categoryId 分类ID
   * @return SPU列表
   */
  List<SpuResp> findByCategoryId(Long categoryId);

  /**
   * 分页查询SPU列表
   *
   * @param pageReq 分页参数
   * @return SPU列表
   */
  Page<SpuPageResp> findPage(SpuFindPageReq pageReq);

  /**
   * 审核通过或失败
   *
   * @param spuId 商品ID
   * @param auditStatus 审核状态
   */
  Boolean audit(Long spuId, Integer auditStatus);

  /**
   * 商品上下架
   *
   * @param spuId 商品ID
   * @param shelveStatus 上下架状态
   */
  Boolean shelve(Long spuId, Integer shelveStatus);
}
