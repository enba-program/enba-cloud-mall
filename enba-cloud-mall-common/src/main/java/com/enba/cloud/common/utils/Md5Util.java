package com.enba.cloud.common.utils;

import java.security.MessageDigest;

public class Md5Util {

  public static void main(String[] args) {
    System.out.println(MD5Encode("123456"));
  }

  public static String MD5Encode(String origin) {
    return MD5Encode(origin, "UTF-8");
  }

  public static String MD5Encode(String origin, String charsetname) {
    String resultString = null;
    try {
      resultString = new String(origin);
      MessageDigest md = MessageDigest.getInstance("MD5");
      if (charsetname == null || "".equals(charsetname)) {
        resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
      } else {
        resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
      }
    } catch (Exception exception) {
    }
    return resultString;
  }

  /**
   * 字节转16进制字符串
   *
   * @param digest
   * @return
   */
  private static String byteArrayToHexString(byte[] digest) {
    return byteToHexString(digest);
  }

  // 字节转字符串
  private static String byteToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b & 0xff)); // 转为两位十六进制，确保前导零不丢失
    }
    return sb.toString();
  }
}
