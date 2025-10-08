package com.enba.cloud.goods.spu.tool;

import com.alibaba.fastjson.JSON;
import com.enba.cloud.common.enums.SpuShelveStatusEnum;
import com.enba.cloud.common.utils.PageDataResp;
import com.enba.cloud.goods.sku.doc.EsGoodsDocument;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

/** 商品搜索工具类 */
@Component
@Slf4j
public class ProductSearchTool {

  private final RestHighLevelClient restHighLevelClient;

  String fieldName = "searchKeyword2";

  public ProductSearchTool(RestHighLevelClient restHighLevelClient) {
    this.restHighLevelClient = restHighLevelClient;
  }

  /**
   * 分页查询商品列表，支持排序
   *
   * @return 分页结果
   */
  public PageDataResp searchProducts(SearchGoodsReq req) {
    SearchRequest request = new SearchRequest("goods_doc");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.trackTotalHits(true);
    request.source(searchSourceBuilder);

    // 查询条件
    BoolQueryBuilder boolQueryBuilder = assembleBoolQueryBuilder(req);

    // 排序
    searchSourceBuilder.sort(new FieldSortBuilder("createTime").order(SortOrder.DESC));

    // 分页
    searchSourceBuilder.from((req.getPageNum() - 1) * req.getPageSize());
    searchSourceBuilder.size(req.getPageSize());

    // 为此请求设置搜索查询
    searchSourceBuilder.query(boolQueryBuilder);
    log.info("searchProducts  searchSourceBuilder：{}", searchSourceBuilder);

    SearchResponse response;
    try {
      response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    org.elasticsearch.search.SearchHit[] hits = response.getHits().getHits();
    long count = Objects.requireNonNull(response.getHits().getTotalHits()).value;
    log.info("searchProducts hits = {}条,{}", count, response);

    List<EsGoodsDocument> list = new ArrayList();
    for (org.elasticsearch.search.SearchHit hit : hits) {
      float score = hit.getScore();
      log.info("_score : {}", score);

      String source = hit.getSourceAsString();
      EsGoodsDocument entity = JSON.parseObject(source, EsGoodsDocument.class);
      list.add(entity);
    }

    PageDataResp pageDataResp = new PageDataResp();
    pageDataResp.setDataList(list);
    pageDataResp.setTotal(count);
    pageDataResp.setPageNum(req.getPageNum());
    pageDataResp.setPageSize(req.getPageSize());
    return pageDataResp;
  }

  /** 组装BoolQueryBuilder */
  private BoolQueryBuilder assembleBoolQueryBuilder(SearchGoodsReq req) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    // 只查上架的
    boolQueryBuilder.must(
        QueryBuilders.termQuery("shelveStatus", SpuShelveStatusEnum.ON_SHELVE.getShelveStatus()));

    // 品牌
    if (null != req.getBrandId()) {
      boolQueryBuilder.must(QueryBuilders.termQuery("brandId", req.getBrandId()));
    }

    // 关键字
    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(fieldName, req.getKeyword()));
    return boolQueryBuilder;
  }
}
