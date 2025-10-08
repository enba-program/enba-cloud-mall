package com.enba.cloud.common.config;

import com.enba.cloud.common.interceptor.ServletHeaderInterceptor;
import com.enba.cloud.common.interceptor.TraceIdInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer, InitializingBean {
  /** 不需要拦截地址 */
  public static final String[] excludeUrls = {"/login", "/logout", "/refresh"};

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 添加请求头拦截器
    registry
        .addInterceptor(getHeaderInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns(excludeUrls)
        .order(-10);

    // 添加TraceId拦截器
    registry.addInterceptor(getTraceIdInterceptor()).addPathPatterns("/**").order(-20);
  }

  /** 自定义请求头拦截器 */
  public ServletHeaderInterceptor getHeaderInterceptor() {
    return new ServletHeaderInterceptor();
  }

  public TraceIdInterceptor getTraceIdInterceptor() {
    return new TraceIdInterceptor();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("初始化拦截器HeaderInterceptor完成");
  }
}
