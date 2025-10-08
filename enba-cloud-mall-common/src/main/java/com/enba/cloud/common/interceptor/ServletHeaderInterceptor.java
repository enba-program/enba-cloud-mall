package com.enba.cloud.common.interceptor;

import com.enba.boot.core.context.SecurityContextHolder;
import com.enba.cloud.common.utils.ServletUtils;
import com.enba.cloud.common.constants.TokenConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/** 自定义请求头拦截器，将Header数据封装到线程变量中方便获取 */
public class ServletHeaderInterceptor implements AsyncHandlerInterceptor {
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }

    SecurityContextHolder.setKeyValue(
        TokenConst.TOKEN_USER_ID, ServletUtils.getHeader(request, TokenConst.TOKEN_USER_ID));
    SecurityContextHolder.setKeyValue(
        TokenConst.TOKEN_USER_NAME, ServletUtils.getHeader(request, TokenConst.TOKEN_USER_NAME));

    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    SecurityContextHolder.remove();
  }
}
