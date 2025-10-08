package com.enba.cloud.goods.spu.tool;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.cloud.common.enums.SpuShelveStatusEnum;
import com.enba.cloud.goods.sku.doc.EsGoodsDocument;
import com.enba.cloud.goods.sku.doc.EsGoodsDocument.EsSkuListDoc;
import com.enba.cloud.goods.api.spu.bo.EsGoodsBo;
import com.enba.cloud.goods.api.spu.entity.Spu;
import com.enba.cloud.goods.spu.mapper.SpuMapper;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/** 商品同步工具类 mysql==>es */
@Component
@Slf4j
public class ProductSynchTool {

  private final ElasticsearchRestTemplate elasticsearchRestTemplate;
  private final SpuMapper spuMapper;

  public ProductSynchTool(
      ElasticsearchRestTemplate elasticsearchRestTemplate, SpuMapper spuMapper) {
    this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    this.spuMapper = spuMapper;
  }

  // 清空索引数据
  public void clearIndexData() {
    log.info(">>>>>>>>>开始清空索引goods数据...");
    // 构建一个匹配所有文档的查询
    NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).build();

    elasticsearchRestTemplate.delete(searchQuery, EsGoodsDocument.class);
    log.info(">>>>>>>>>清空索引goods数据完成");
  }

  // 同步指定SPU 商品到 es
  public void synchSpu(Long spuId) {
    log.info(">>>>>>>>>开始同步数据{}", spuId);
    Spu spu = spuMapper.selectById(spuId);

    List<EsGoodsBo> pageSynchAllList = spuMapper.getPageSynchAll(Lists.newArrayList(spuId), 0, 10);

    EsGoodsDocument esGoodsDocument = convertToEsDocument(spu, pageSynchAllList);

    bulkIndexToEs(Lists.newArrayList(esGoodsDocument));
    log.info(">>>>>>>>>开始同步数据{}结束", spuId);
  }

  // 商品数据分页全量同步
  public void synchAll() {
    // 商品数据分页全量同步
    log.info(">>>>>>>>>开始同步数据...");

    doSynchAll();

    log.info(">>>>>>>>>同步完成");
  }

  private void doSynchAll() {
    int pageSize = 100;
    int currentPage = 1;
    boolean hasMore = true;

    while (hasMore) {
      // 分页查询 MySQL 数据
      Page<Spu> page = new Page<>(currentPage, pageSize);
      IPage<Spu> SpuPage =
          spuMapper.selectPage(
              page,
              Wrappers.<Spu>lambdaQuery()
                  .eq(Spu::getShelveStatus, SpuShelveStatusEnum.ON_SHELVE.getShelveStatus()));
      List<Spu> spuList = SpuPage.getRecords();

      if (CollectionUtils.isEmpty(spuList)) {
        hasMore = false; // 没有更多数据，结束同步
        log.info("同步完成，总共同步了 {} 页数据。", currentPage - 1);
      } else {
        // 单位，品牌，类目，sku
        List<EsGoodsBo> pageSynchAllList =
            spuMapper.getPageSynchAll(
                spuList.stream().map(Spu::getId).collect(Collectors.toList()),
                currentPage - 1,
                pageSize);

        // 转换为 ES 文档对象
        List<EsGoodsDocument> esDocuments =
            spuList.stream()
                .map(e -> convertToEsDocument(e, pageSynchAllList))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 批量写入 ES
        bulkIndexToEs(esDocuments);

        log.info("已同步第 {} 页数据，共 {} 条。", currentPage, spuList.size());
        currentPage++;
      }
    }
  }

  /** 将 MySQL 的 spu 对象转换为 ES 的 GoodsEsDocument 对象 */
  private EsGoodsDocument convertToEsDocument(Spu spu, List<EsGoodsBo> pageSynchAllList) {
    // 根据spuid分组
    Map<Long, List<EsGoodsBo>> spuMap =
        pageSynchAllList.stream().collect(Collectors.groupingBy(EsGoodsBo::getSpuId));

    if (!spuMap.containsKey(spu.getId())) {
      return null;
    }

    EsGoodsDocument esDoc = new EsGoodsDocument();
    esDoc.setEsId(spu.getId());
    esDoc.setCreateTime(spu.getCreateTime());
    esDoc.setSpuId(spu.getId());
    esDoc.setSpuCode(spu.getSpuCode());
    esDoc.setSpuName(spu.getSpuName());
    esDoc.setSpuDesc(spu.getSpuDesc());
    esDoc.setHeaderPicUrl(spu.getHeaderPicUrl());
    esDoc.setDefaultSkuId(spu.getDefaultSkuId());
    esDoc.setIsSpu(spu.getIsSpu());
    esDoc.setIsRefund(spu.getIsRefund());
    esDoc.setIsPost(spu.getIsPost());
    esDoc.setInstallList(spu.getInstallList());
    esDoc.setSpecPackage(spu.getSpecPackage());
    esDoc.setPostSaleGuarantee(spu.getPostSaleGuarantee());
    esDoc.setShelveStatus(spu.getShelveStatus());

    List<EsGoodsBo> esGoodsBoList = spuMap.get(spu.getId());
    // spu下无sku的话   不同步至es
    if (CollectionUtils.isEmpty(esGoodsBoList)) {
      return null;
    }

    EsGoodsBo esGoodsBo = esGoodsBoList.get(0);
    // 单位
    esDoc.setUnitId(esGoodsBo.getUnitId());
    esDoc.setUnitName(esGoodsBo.getUnitName());
    // 品牌
    esDoc.setBrandId(esGoodsBo.getBrandId());
    esDoc.setBrandName(esGoodsBo.getBrandName());
    esDoc.setBrandImage(esGoodsBo.getBrandImage());
    // 类目
    esDoc.setCategoryId(esGoodsBo.getCategoryId());
    esDoc.setCategoryName(esGoodsBo.getCategoryName());
    esDoc.setCategoryCode(esGoodsBo.getCategoryCode());
    esDoc.setCategoryIconUrl(esGoodsBo.getCategoryIconUrl());
    // sku集合
    List<EsSkuListDoc> skuList =
        esGoodsBoList.stream()
            .map(
                e -> {
                  EsSkuListDoc esSkuListDoc = new EsSkuListDoc();
                  esSkuListDoc.setSkuCode(e.getSkuCode());
                  esSkuListDoc.setSkuName(e.getSkuName());
                  esSkuListDoc.setMoq(e.getMoq());
                  esSkuListDoc.setInventory(e.getInventory());
                  esSkuListDoc.setSkuPrice(e.getSkuPrice());
                  esSkuListDoc.setSkuSpecEnums(e.getSkuSpecEnums());
                  esSkuListDoc.setSkuDesc(e.getSkuDesc());
                  esSkuListDoc.setStatus(e.getStatus());
                  return esSkuListDoc;
                })
            .collect(Collectors.toList());
    esDoc.setSkuList(skuList);

    // 组装搜索关键字
    String searchKey =
        spu.getSpuName()
            + "#("
            + spu.getSpuCode()
            + ")#("
            + skuList.stream().map(EsSkuListDoc::getSkuName).collect(Collectors.joining(","))
            + ")#("
            + skuList.stream().map(EsSkuListDoc::getSkuCode).collect(Collectors.joining(","))
            + ")";

    esDoc.setSearchKeyword1(searchKey);
    esDoc.setSearchKeyword2(searchKey);

    return esDoc;
  }

  /** 批量将文档写入 ES */
  private void bulkIndexToEs(List<EsGoodsDocument> documents) {
    if (documents == null || documents.isEmpty()) {
      return;
    }

    try {
      // TODO 构建批量写入请求
      List<IndexQuery> queriesList =
          documents.stream()
              .map(
                  esDoc ->
                      new IndexQueryBuilder()
                          .withId(esDoc.getSpuId() + "") // 设置文档 ID
                          .withObject(esDoc) // 设置文档源数据
                          .build())
              .collect(Collectors.toList());

      elasticsearchRestTemplate.bulkIndex(queriesList, EsGoodsDocument.class);
    } catch (Exception e) {
      log.error("ES 批量写入异常:{} ", e.getMessage(), e);

      // TODO 异常告警
    }
  }
}
