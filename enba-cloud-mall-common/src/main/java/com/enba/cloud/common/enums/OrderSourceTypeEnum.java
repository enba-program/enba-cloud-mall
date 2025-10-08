package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderSourceTypeEnum {
  // 订单来源(0-PC,1-H5,2-APP,3-小程序)
  PC(0, "PC"),
  H5(1, "H5"),
  APP(2, "APP"),
  MINI_PROGRAM(3, "小程序");

  private final Integer sourceType;
  private final String name;

  OrderSourceTypeEnum(Integer sourceType, String name) {
    this.sourceType = sourceType;
    this.name = name;
  }

  public static OrderSourceTypeEnum getBySourceType(Integer sourceType) {
    for (OrderSourceTypeEnum value : OrderSourceTypeEnum.values()) {
      if (value.sourceType.equals(sourceType)) {
        return value;
      }
    }

    return null;
  }
}
