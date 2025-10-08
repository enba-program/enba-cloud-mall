package com.enba.cloud.goods.spu.manager;

import com.enba.cloud.common.utils.CustomCodeGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpuCodeGenerator {

  private static final String DEFAULT_SPU_CODE_PREFIX = "SP";

  private static final String DEFAULT_SPU_CODE_TIME_PATTERN = "yyyyMMddHHmmss";

  private static final int DEFAULT_SPU_CODE_RANDOM_DIGITS = 3;

  public static String generateCode() {
    return CustomCodeGenerator.generateCode(
        DEFAULT_SPU_CODE_PREFIX, DEFAULT_SPU_CODE_TIME_PATTERN, DEFAULT_SPU_CODE_RANDOM_DIGITS);
  }
}
