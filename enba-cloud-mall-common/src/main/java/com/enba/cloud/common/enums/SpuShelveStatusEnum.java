package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum SpuShelveStatusEnum {
  OFF_SHELVE(0, "已下架"),
  ON_SHELVE(1, "已上架"),
  WAIT_SHELVE(2, "待上架");

  private final Integer shelveStatus;
  private final String name;

  public static SpuShelveStatusEnum getByShelveStatus(Integer shelveStatus) {
    for (SpuShelveStatusEnum statusEnum : values()) {
      if (statusEnum.getShelveStatus().equals(shelveStatus)) {
        return statusEnum;
      }
    }
    return null;
  }

  private SpuShelveStatusEnum(Integer shelveStatus, String name) {
    this.shelveStatus = shelveStatus;
    this.name = name;
  }
}
