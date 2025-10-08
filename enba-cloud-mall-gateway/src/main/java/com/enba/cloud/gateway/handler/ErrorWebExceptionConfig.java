package com.enba.cloud.gateway.handler;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/** 异常处理器配置类 */
@Configuration
@EnableConfigurationProperties({WebProperties.class})
public class ErrorWebExceptionConfig implements WebFluxConfigurer {

  /** 注册自定义异常处理器 */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler(
      org.springframework.boot.web.reactive.error.ErrorAttributes errorAttributes,
      WebProperties webProperties,
      ApplicationContext applicationContext,
      ServerCodecConfigurer codecConfigurer) {
    return new GlobalErrorWebExceptionHandler(
        errorAttributes, webProperties, applicationContext, codecConfigurer);
  }
}
