package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderShippingStatusEnum {
  // 物流状态(0-未发货,1-已发货,2-已收货)
  WAIT_SHIPPING(0, "未发货"),
  SHIPPED(1, "已发货"),
  RECEIVED(2, "已收货");

  private final Integer shippingStatus;
  private final String name;

  private OrderShippingStatusEnum(Integer shippingStatus, String name) {
    this.shippingStatus = shippingStatus;
    this.name = name;
  }

  public static OrderShippingStatusEnum getByShippingStatus(Integer shippingStatus) {
    for (OrderShippingStatusEnum orderShippingStatusEnum : OrderShippingStatusEnum.values()) {
      if (orderShippingStatusEnum.getShippingStatus().equals(shippingStatus)) {
        return orderShippingStatusEnum;
      }
    }
    return null;
  }
}
