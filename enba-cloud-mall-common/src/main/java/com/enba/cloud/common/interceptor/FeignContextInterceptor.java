package com.enba.cloud.common.interceptor;

import com.enba.cloud.common.constants.TokenConst;
import com.enba.cloud.common.constants.TraceConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** OpenFeign拦截器，用于在服务间调用时传递上下文用户信息 */
@Component
public class FeignContextInterceptor implements RequestInterceptor {

  /** 拦截Feign请求，添加上下文用户信息到请求头 */
  @Override
  public void apply(RequestTemplate template) {
    // 获取当前请求的上下文
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();

      // 传递所有请求头，也可以只传递需要的特定头信息
      Enumeration<String> headerNames = request.getHeaderNames();
      if (headerNames != null) {
        while (headerNames.hasMoreElements()) {
          String name = headerNames.nextElement();
          String value = request.getHeader(name);

          // 这里可以添加过滤条件，只传递需要的头信息
          // 例如传递认证信息、用户ID等关键信息
          if (isNeedTransferHeader(name)) {
            template.header(name, value);
          }
        }
      }

      template.header(
          TraceConstants.TRACE_ID_HEADER,
          (String) request.getAttribute(TraceConstants.TRACE_ID_HEADER));
    } else {
      // 处理没有请求上下文的情况，例如定时任务调用
      handleNoRequestContext(template);
    }
  }

  /** 判断是否需要传递的请求头 */
  private boolean isNeedTransferHeader(String headerName) {
    // 可以根据实际需求自定义需要传递的头信息
    // 例如：认证信息、用户ID、租户ID等
    return TokenConst.TOKEN_USER_ID.equalsIgnoreCase(headerName)
        || TokenConst.TOKEN_USER_NAME.equalsIgnoreCase(headerName);
  }

  /** 处理没有请求上下文的情况 */
  private void handleNoRequestContext(RequestTemplate template) {
    // 可以从其他上下文（如ThreadLocal）中获取用户信息
    // 例如：从自定义的上下文工具类中获取
    template.header(TokenConst.TOKEN_USER_ID, TokenConst.SYSTEM_USER_ID);
    template.header(TokenConst.TOKEN_USER_NAME, TokenConst.SYSTEM_USER_NAME);
  }
}
