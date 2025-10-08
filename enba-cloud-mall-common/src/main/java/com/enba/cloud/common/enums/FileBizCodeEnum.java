package com.enba.cloud.common.enums;

import lombok.Getter;

/** 文件上传场景枚举 */
@Getter
public enum FileBizCodeEnum {
  PRODUCT_SPU_MAIN("spu_main_pic", "spu主图", "enba-mall"),
  PRODUCT_SPU_DETAIL("spu_detail_pic", "spu详情图", "enba-mall"),
  PRODUCT_SKU_MAIN("sku_main_pic", "sku主图", "enba-mall"),
  PRODUCT_SKU_DETAIL("sku_detail_pic", "sku轮播图", "enba-mall"),
  ;

  // 业务场景code
  private final String bizCode;

  // 业务场景名称
  private final String bizName;

  // 存储桶名称
  private final String bucketName;

  FileBizCodeEnum(String bizCode, String bizName, String bucketName) {
    this.bizCode = bizCode;
    this.bizName = bizName;
    this.bucketName = bucketName;
  }

  public static FileBizCodeEnum getByBizCode(String bizCode) {
    for (FileBizCodeEnum value : FileBizCodeEnum.values()) {
      if (value.getBizCode().equals(bizCode)) {
        return value;
      }
    }
    return null;
  }
}
