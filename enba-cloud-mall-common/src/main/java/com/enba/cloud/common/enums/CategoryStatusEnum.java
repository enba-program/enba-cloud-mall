package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum CategoryStatusEnum {
  // 状态 1启用 0禁用
  ENABLE(1, "启用"),
  DISABLE(0, "禁用");

  private final Integer status;
  private final String name;

  public static CategoryStatusEnum getByStatus(Integer status) {
    for (CategoryStatusEnum value : values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }

  private CategoryStatusEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }
}
