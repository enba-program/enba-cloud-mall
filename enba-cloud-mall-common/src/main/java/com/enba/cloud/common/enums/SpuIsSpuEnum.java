package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum SpuIsSpuEnum {
  SINGLE(0, "单规格"),
  MULTIPLE(1, "多规格");

  private SpuIsSpuEnum(Integer isSpu, String name) {
    this.isSpu = isSpu;
    this.name = name;
  }

  private final Integer isSpu;
  private final String name;

  public static SpuIsSpuEnum getByIsSpu(Integer isSpu) {
    for (SpuIsSpuEnum spuIsSpuEnum : values()) {
      if (spuIsSpuEnum.getIsSpu().equals(isSpu)) {
        return spuIsSpuEnum;
      }
    }
    return null;
  }
}
