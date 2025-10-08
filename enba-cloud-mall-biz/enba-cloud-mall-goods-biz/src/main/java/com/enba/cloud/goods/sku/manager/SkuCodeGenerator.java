package com.enba.cloud.goods.sku.manager;


import com.enba.cloud.common.utils.CustomCodeGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkuCodeGenerator {

  private static final String DEFAULT_SKU_CODE_PREFIX = "SK";

  private static final String DEFAULT_SKU_CODE_TIME_PATTERN = "yyyyMMddHHmmss";

  private static final int DEFAULT_SKU_CODE_RANDOM_DIGITS = 3;

  public static String generateCode() {
    return CustomCodeGenerator.generateCode(
        DEFAULT_SKU_CODE_PREFIX, DEFAULT_SKU_CODE_TIME_PATTERN, DEFAULT_SKU_CODE_RANDOM_DIGITS);
  }
}
