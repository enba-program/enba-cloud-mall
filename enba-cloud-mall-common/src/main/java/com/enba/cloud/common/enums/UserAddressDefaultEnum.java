package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum UserAddressDefaultEnum {
  // 是否默认地址(0-否,1-是)
  NO(0, "否"),
  YES(1, "是");

  private final Integer status;
  private final String name;

  UserAddressDefaultEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }

  public static UserAddressDefaultEnum getByStatus(Integer status) {
    for (UserAddressDefaultEnum value : UserAddressDefaultEnum.values()) {
      if (value.status.equals(status)) {
        return value;
      }
    }
    return null;
  }
}
