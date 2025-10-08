package com.enba.cloud.common.utils;

import java.util.UUID;

public class TraceIdGenerator {

  /** 生成自定义traceId 格式: 32位UUID（去除横线） */
  public static String generate() {
    // 可根据业务需求修改生成规则
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /** 生成带业务前缀的traceId 格式: 业务标识 + 时间戳 + 随机数 */
  public static String generateWithPrefix(String prefix) {
    return prefix + "-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000000);
  }
}
