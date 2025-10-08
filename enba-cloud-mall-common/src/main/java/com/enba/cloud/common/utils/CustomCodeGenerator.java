package com.enba.cloud.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/** 自定义编码生成器工具类 */
public class CustomCodeGenerator {

  /**
   * 生成自定义编码
   *
   * @param prefix 编码前缀（如业务类型：ORDER、USER等）
   * @param timePattern 时间格式，如 "yyyyMMddHHmmssSSS"
   * @param randomDigits 随机数位数，如 3 表示生成 3 位随机数
   * @return 生成的编码字符串
   */
  public static String generateCode(String prefix, String timePattern, int randomDigits) {
    if (prefix == null || prefix.isEmpty()) {
      throw new IllegalArgumentException("前缀不能为空");
    }
    if (timePattern == null || timePattern.isEmpty()) {
      throw new IllegalArgumentException("时间格式不能为空");
    }
    if (randomDigits < 0 || randomDigits > 6) {
      throw new IllegalArgumentException("随机数位数应在 0 到 6 之间");
    }

    // 1. 获取当前时间并格式化
    SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
    String timePart = sdf.format(new Date());

    // 2. 生成随机数部分
    String randomPart = generateRandomDigits(randomDigits);

    // 3. 拼接编码
    return prefix + timePart + randomPart;
  }

  /**
   * 生成指定位数的随机数字字符串
   *
   * @param digits 随机数位数
   * @return 随机数字字符串
   */
  private static String generateRandomDigits(int digits) {
    if (digits == 0) {
      return "";
    }
    Random random = new Random();
    int max = (int) Math.pow(10, digits) - 1; // 如 digits=3，max=999
    int min = (int) Math.pow(10, digits - 1); // 如 digits=3，min=100
    if (digits == 1) {
      min = 0; // 如果是1位，允许0-9
    }
    int randomNumber = random.nextInt(max - min + 1) + min;
    return String.format("%0" + digits + "d", randomNumber);
  }

  // ====================== 示例用法 ======================

  public static void main(String[] args) {
    // 示例1：订单编码，前缀ORDER，时间精确到毫秒，随机3位
    String orderCode = generateCode("ORDER", "yyyyMMddHHmmssSSS", 3);
    System.out.println("订单编码: " + orderCode);

    // 示例2：用户编码，前缀USER，时间精确到秒，随机2位
    String userCode = generateCode("USER", "yyyyMMddHHmmss", 2);
    System.out.println("用户编码: " + userCode);

    // 示例3：日志编码，前缀LOG，时间精确到分钟，无随机数
    String logCode = generateCode("LOG", "yyyyMMddHHmm", 0);
    System.out.println("日志编码: " + logCode);
  }
}
