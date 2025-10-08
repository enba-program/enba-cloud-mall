package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderRefundStatusEnum {
  ORDER_REFUND_STATUS_WAIT_REFUND(0, "待处理"),
  ORDER_REFUND_STATUS_REFUNDING(1, "退款中"),
  ORDER_REFUND_STATUS_REFUND_SUCCESS(2, "退款成功"),
  ORDER_REFUND_STATUS_REFUND_FAIL(3, "退款失败");

  private final Integer status;
  private final String name;

  OrderRefundStatusEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }

  public static OrderRefundStatusEnum getByStatus(Integer status) {
    for (OrderRefundStatusEnum value : OrderRefundStatusEnum.values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }
}
