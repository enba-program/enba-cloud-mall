package com.enba.cloud.goods.sku.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.goods.api.sku.client.MallSearchClient;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import com.enba.cloud.goods.sku.handler.MallSearchHandler;
import com.enba.cloud.goods.sku.handler.MallSearchHandler.MallSearchEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商品搜索")
@RestController
public class MallSearchController implements MallSearchClient {

  @Value("${mall-sku.handler-key}")
  private MallSearchEnum handlerKey;

  @Override
  @ApiOperation("商品搜索")
  public Result<?> searchGoods(@RequestBody SearchGoodsReq req) {
    return Result.success(MallSearchHandler.getHandler(handlerKey).searchGoods(req));
  }
}
