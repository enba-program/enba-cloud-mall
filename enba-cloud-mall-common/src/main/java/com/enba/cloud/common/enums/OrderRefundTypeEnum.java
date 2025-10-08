package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderRefundTypeEnum {
  ORDER_REFUND_TYPE_ONLY_REFUND(0, "仅退款"),
  ORDER_REFUND_TYPE_RETURN_GOODS(1, "退货退款");

  private final Integer type;
  private final String name;

  OrderRefundTypeEnum(Integer type, String name) {
    this.type = type;
    this.name = name;
  }

  public static OrderRefundTypeEnum getByType(Integer type) {
    for (OrderRefundTypeEnum value : OrderRefundTypeEnum.values()) {
      if (value.getType().equals(type)) {
        return value;
      }
    }
    return null;
  }
}
