package com.enba.cloud.orders.manager;

import com.enba.cloud.common.utils.CustomCodeGenerator;
import lombok.extern.slf4j.Slf4j;

/** 订单退款编号生成器 */
@Slf4j
public class OrderRefundCodeGenerator {

  private static final String DEFAULT_ORDER_REFUND_CODE_PREFIX = "T";

  private static final String DEFAULT_ORDER_REFUND_CODE_TIME_PATTERN = "yyyyMMddHHmmss";

  private static final int DEFAULT_ORDER_REFUND_CODE_RANDOM_DIGITS = 3;

  public static String generateCode() {
    return CustomCodeGenerator.generateCode(
        DEFAULT_ORDER_REFUND_CODE_PREFIX,
        DEFAULT_ORDER_REFUND_CODE_TIME_PATTERN,
        DEFAULT_ORDER_REFUND_CODE_RANDOM_DIGITS);
  }
}
