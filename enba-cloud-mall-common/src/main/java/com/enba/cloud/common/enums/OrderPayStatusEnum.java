package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderPayStatusEnum {
  // 支付状态(0-未支付,1-已支付,2-支付失败)
  UNPAID(0, "未支付"),
  PAID(1, "已支付"),
  PAY_FAIL(2, "支付失败");

  private final Integer status;
  private final String name;

  private OrderPayStatusEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }

  public static OrderPayStatusEnum getByStatus(Integer status) {
    for (OrderPayStatusEnum orderPayStatusEnum : OrderPayStatusEnum.values()) {
      if (orderPayStatusEnum.getStatus().equals(status)) {
        return orderPayStatusEnum;
      }
    }
    return null;
  }
}
