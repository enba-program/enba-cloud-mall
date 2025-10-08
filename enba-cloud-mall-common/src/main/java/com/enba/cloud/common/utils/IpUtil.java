package com.enba.cloud.common.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class IpUtil {

  /** 从请求中获取客户端IP地址 */
  public static String getClientIp() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    String xForwardedFor = request.getHeader("X-Forwarded-For");
    String clientIp;

    if (xForwardedFor != null
        && !xForwardedFor.isEmpty()
        && !"unknown".equalsIgnoreCase(xForwardedFor)) {
      // 如果有 X-Forwarded-For 头部，则取第一个 IP
      clientIp = xForwardedFor.split(",")[0].trim();
    } else {
      // 否则使用 getRemoteAddr 获取直接连接的客户端 IP
      clientIp = request.getRemoteAddr();
    }

    return clientIp;
  }
}
