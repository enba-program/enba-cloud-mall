package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum RefundStatusEnum {
  // 退款状态(0-待处理,1-退款中,2-退款成功,3-退款失败)
  WAIT_REFUND(0, "待处理"),
  REFUNDING(1, "退款中"),
  REFUNDED(2, "退款成功"),
  REFUND_FAIL(3, "退款失败");

  private final Integer status;
  private final String name;

  RefundStatusEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }

  public static RefundStatusEnum getByStatus(Integer status) {
    for (RefundStatusEnum value : values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }
}
