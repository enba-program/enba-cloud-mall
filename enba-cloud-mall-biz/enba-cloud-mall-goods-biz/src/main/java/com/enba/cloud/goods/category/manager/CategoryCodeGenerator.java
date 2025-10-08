package com.enba.cloud.goods.category.manager;

import com.enba.cloud.common.utils.CustomCodeGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CategoryCodeGenerator {

  public static final String DEFAULT_CATEGORY_CODE_PREFIX = "C";

  public static final String DEFAULT_CATEGORY_CODE_TIME_PATTERN = "yyyyMMddHHmmss";

  public static final int DEFAULT_CATEGORY_CODE_RANDOM_DIGITS = 3;

  /**
   * 生成类目编码
   *
   * @return
   */
  public static String generateCode() {
    return CustomCodeGenerator.generateCode(
        DEFAULT_CATEGORY_CODE_PREFIX,
        DEFAULT_CATEGORY_CODE_TIME_PATTERN,
        DEFAULT_CATEGORY_CODE_RANDOM_DIGITS);
  }
}
