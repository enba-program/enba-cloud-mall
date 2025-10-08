package com.enba.cloud.goods.api.sku.factory;

import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.goods.api.sku.client.MallSearchClient;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MallSearchFallbackFactory implements FallbackFactory<MallSearchClient> {

  private static final Logger log = LoggerFactory.getLogger(MallSearchFallbackFactory.class);

  @Override
  public MallSearchClient create(Throwable cause) {
    return new MallSearchClient() {
      @Override
      public Result<?> searchGoods(SearchGoodsReq req) {
        log.error("MallSearchClient searchGoods 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "MallSearchClient searchGoods error");
      }
    };
  }
}
