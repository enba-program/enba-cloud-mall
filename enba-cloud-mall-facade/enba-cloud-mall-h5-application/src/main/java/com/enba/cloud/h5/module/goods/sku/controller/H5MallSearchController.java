package com.enba.cloud.h5.module.goods.sku.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.goods.api.sku.client.MallSearchClient;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商品搜索")
@RestController
@RequestMapping("/api/sku/search")
public class H5MallSearchController {

  @Autowired private MallSearchClient mallSearchClient;

  @ApiOperation("商品搜索")
  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public Result<?> searchGoods(@RequestBody SearchGoodsReq req) {
    return mallSearchClient.searchGoods(req);
  }
}
