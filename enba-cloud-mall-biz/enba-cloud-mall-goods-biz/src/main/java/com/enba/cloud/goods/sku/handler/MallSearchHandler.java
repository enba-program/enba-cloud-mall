package com.enba.cloud.goods.sku.handler;


import com.enba.cloud.common.utils.PageDataResp;
import com.enba.cloud.goods.api.sku.req.SearchGoodsReq;
import java.util.HashMap;
import java.util.Map;

public interface MallSearchHandler {

  Map<MallSearchEnum, MallSearchHandler> handlerMap = new HashMap<>();

  static MallSearchHandler getHandler(MallSearchEnum key) {
    return handlerMap.get(key);
  }

  /**
   * 处理器标识
   *
   * @return 处理器
   */
  MallSearchEnum handlerKey();

  /**
   * 商品搜索
   *
   * @param req 请求参数
   * @return 搜索结果
   */
  PageDataResp searchGoods(SearchGoodsReq req);

  /** 搜索处理器枚举 */
  enum MallSearchEnum {
    ES,
    MYSQL
  }
}
