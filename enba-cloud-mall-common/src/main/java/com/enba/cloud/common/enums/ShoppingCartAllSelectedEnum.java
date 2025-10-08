package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum ShoppingCartAllSelectedEnum {
  // 是否全选(0-否,1-是)
  NO_SELECTED(0, "否"),
  YES_SELECTED(1, "是");

  private final Integer selected;
  private final String name;

  public static ShoppingCartAllSelectedEnum getBySelected(Integer selected) {
    for (ShoppingCartAllSelectedEnum value : values()) {
      if (value.selected.equals(selected)) {
        return value;
      }
    }
    return null;
  }

  ShoppingCartAllSelectedEnum(Integer selected, String name) {
    this.selected = selected;
    this.name = name;
  }
}
