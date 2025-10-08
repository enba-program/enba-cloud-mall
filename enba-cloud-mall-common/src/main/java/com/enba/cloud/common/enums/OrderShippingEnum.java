package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderShippingEnum {
  // 物流状态(0-未发货,1-已发货,2-运输中,3-已签收,4-退货中,5-已退货)
  WAIT_SHIP(0, "未发货"),
  SHIPPED(1, "已发货"),
  TRANSPORT(2, "运输中"),
  SIGNED(3, "已签收"),
  RETURNING(4, "退货中"),
  RETURNED(5, "已退货");

  private final Integer status;
  private final String name;

  public static OrderShippingEnum getByStatus(Integer status) {
    for (OrderShippingEnum value : values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }

  OrderShippingEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }
}
