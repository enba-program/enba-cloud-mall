package com.enba.cloud.goods.sku.handler;

public abstract class AbstractMallSearchHandler implements MallSearchHandler {

  public AbstractMallSearchHandler() {
    handlerMap.put(handlerKey(), this);
  }
}
