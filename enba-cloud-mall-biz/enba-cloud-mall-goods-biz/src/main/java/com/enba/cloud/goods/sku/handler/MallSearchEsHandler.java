package com.enba.cloud.goods.sku.handler;


import com.enba.cloud.common.utils.PageDataResp;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import com.enba.cloud.goods.spu.tool.ProductSearchTool;
import org.springframework.stereotype.Component;

@Component
public class MallSearchEsHandler extends AbstractMallSearchHandler {

  private final ProductSearchTool productSearchTool;

  public MallSearchEsHandler(ProductSearchTool productSearchTool) {
    this.productSearchTool = productSearchTool;
  }

  @Override
  public MallSearchEnum handlerKey() {
    return MallSearchEnum.ES;
  }

  @Override
  public PageDataResp searchGoods(SearchGoodsReq req) {
    // TODO 待实现
    req.checkSearchField();

    return productSearchTool.searchProducts(req);
  }
}
