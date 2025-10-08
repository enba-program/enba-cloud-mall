package com.enba.cloud.common.interceptor;

import com.enba.cloud.common.constants.TraceConstants;
import com.enba.cloud.common.utils.TraceIdGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

// 拦截器，用于设置traceId
public class TraceIdInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    // 1. 从请求头获取traceId
    String traceId = request.getHeader(TraceConstants.TRACE_ID_HEADER);

    // 2. 如果没有则生成新的traceId
    if (traceId == null || traceId.trim().isEmpty()) {
      traceId = TraceIdGenerator.generate();
    }
    request.setAttribute(TraceConstants.TRACE_ID_HEADER, traceId);

    // 3. 设置到MDC和响应头
    MDC.put(TraceConstants.MDC_TRACE_ID_KEY, traceId);
    response.setHeader(TraceConstants.TRACE_ID_HEADER, traceId);

    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    // 清除MDC中的traceId，避免线程复用导致的问题
    MDC.remove(TraceConstants.MDC_TRACE_ID_KEY);
  }
}
