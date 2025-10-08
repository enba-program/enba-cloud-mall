package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderShippingMethodEnum {
  EXPRESS(0, "快递"),
  SELF_PICKUP(1, "自提");

  private final Integer shippingMethod;
  private final String name;

  OrderShippingMethodEnum(Integer shippingMethod, String name) {
    this.shippingMethod = shippingMethod;
    this.name = name;
  }

  public static OrderShippingMethodEnum getByShippingMethod(Integer shippingMethod) {
    for (OrderShippingMethodEnum value : OrderShippingMethodEnum.values()) {
      if (value.shippingMethod.equals(shippingMethod)) {
        return value;
      }
    }
    return null;
  }
}
