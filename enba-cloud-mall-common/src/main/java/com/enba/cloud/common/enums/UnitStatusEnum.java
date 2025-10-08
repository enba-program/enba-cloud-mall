package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum UnitStatusEnum {
  // 状态 1启用 0禁用
  ENABLE(1, "启用"),
  DISABLE(0, "禁用");

  private final Integer status;
  private final String name;

  public static UnitStatusEnum getByStatus(Integer status) {
    for (UnitStatusEnum value : values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }

  private UnitStatusEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }
}
