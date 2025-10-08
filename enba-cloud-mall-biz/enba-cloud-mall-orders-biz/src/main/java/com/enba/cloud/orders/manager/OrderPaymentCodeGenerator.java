package com.enba.cloud.orders.manager;

import com.enba.cloud.common.utils.CustomCodeGenerator;
import lombok.extern.slf4j.Slf4j;

/** 支付单号生成器 */
@Slf4j
public class OrderPaymentCodeGenerator {

  private static final String DEFAULT_PAYMENT_CODE_PREFIX = "P";

  private static final String DEFAULT_PAYMENT_CODE_TIME_PATTERN = "yyyyMMddHHmmss";

  private static final int DEFAULT_PAYMENT_CODE_RANDOM_DIGITS = 3;

  public static String generateCode() {
    return CustomCodeGenerator.generateCode(
        DEFAULT_PAYMENT_CODE_PREFIX,
        DEFAULT_PAYMENT_CODE_TIME_PATTERN,
        DEFAULT_PAYMENT_CODE_RANDOM_DIGITS);
  }
}
