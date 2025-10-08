package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum SkuStatusEnum {
  // 是否生效（1生效中 0失效）
  ON_SALE(1, "生效中"),
  OFF_SALE(0, "失效"),
  ;

  private final Integer status;
  private final String name;

  public static SkuStatusEnum getByStatus(Integer status) {
    for (SkuStatusEnum value : values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }

  private SkuStatusEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }
}
