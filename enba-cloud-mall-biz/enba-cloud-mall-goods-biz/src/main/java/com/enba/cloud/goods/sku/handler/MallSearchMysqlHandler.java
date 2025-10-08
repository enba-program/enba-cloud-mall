package com.enba.cloud.goods.sku.handler;


import com.enba.cloud.common.utils.PageDataResp;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import org.springframework.stereotype.Component;

@Component
public class MallSearchMysqlHandler extends AbstractMallSearchHandler {

  @Override
  public MallSearchEnum handlerKey() {
    return MallSearchEnum.MYSQL;
  }

  @Override
  public PageDataResp searchGoods(SearchGoodsReq req) {
    // TODO 待实现
    return null;
  }
}
