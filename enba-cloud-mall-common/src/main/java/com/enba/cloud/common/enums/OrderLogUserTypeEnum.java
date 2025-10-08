package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderLogUserTypeEnum {
  SYSTEM(0, "系统"),
  USER(1, "买家"),
  SELLER(2, "卖家"),
  CUSTOMER_SERVICE(3, "客服"),
  ;

  private final Integer status;
  private final String name;

  OrderLogUserTypeEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }

  public static OrderLogUserTypeEnum getByStatus(Integer status) {
    for (OrderLogUserTypeEnum value : OrderLogUserTypeEnum.values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }
}
