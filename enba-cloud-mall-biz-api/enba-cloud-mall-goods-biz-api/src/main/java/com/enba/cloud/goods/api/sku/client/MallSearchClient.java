package com.enba.cloud.goods.api.sku.client;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.goods.api.sku.factory.MallSearchFallbackFactory;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    contextId = "MallSearchClient",
    name = ServiceAppConstants.GOODS_SERVICE_NAME,
    fallbackFactory = MallSearchFallbackFactory.class)
@ResponseBody
public interface MallSearchClient {

  @ApiOperation("商品搜索")
  @RequestMapping(value = "/api/sku/search/list", method = RequestMethod.POST)
  Result<?> searchGoods(@RequestBody SearchGoodsReq req);
}
