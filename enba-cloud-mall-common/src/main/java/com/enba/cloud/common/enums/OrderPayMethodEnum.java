package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderPayMethodEnum {
  ALIPAY(1, "支付宝"),
  WECHAT(2, "微信"),
  UNKNOWN(0, "未知");

  private final Integer method;
  private final String name;

  public static OrderPayMethodEnum getByMethod(Integer method) {
    for (OrderPayMethodEnum orderPayMethodEnum : values()) {
      if (orderPayMethodEnum.getMethod().equals(method)) {
        return orderPayMethodEnum;
      }
    }
    return UNKNOWN;
  }

  OrderPayMethodEnum(Integer method, String name) {
    this.method = method;
    this.name = name;
  }
}
