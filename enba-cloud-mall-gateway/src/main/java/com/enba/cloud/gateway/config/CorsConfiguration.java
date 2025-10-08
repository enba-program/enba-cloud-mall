package com.enba.cloud.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfiguration {

  private static final Logger log = LoggerFactory.getLogger(CorsConfiguration.class);

  @Value("${security.cors.allowed-origins}")
  String[] allowedOrigins;

  @Bean
  public CorsWebFilter corsWebFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    org.springframework.web.cors.CorsConfiguration corsConfiguration =
        new org.springframework.web.cors.CorsConfiguration();
    // 允许哪种请求头跨域
    corsConfiguration.addAllowedHeader("*");
    // 允许哪种方法类型跨域 get post delete put
    corsConfiguration.addAllowedMethod("OPTIONS");
    corsConfiguration.addAllowedMethod("HEAD");
    corsConfiguration.addAllowedMethod("GET");
    corsConfiguration.addAllowedMethod("PUT");
    corsConfiguration.addAllowedMethod("POST");
    corsConfiguration.addAllowedMethod("DELETE");
    corsConfiguration.addAllowedMethod("PATCH");
    // 允许哪些请求源跨域
    for (String globalUrl : allowedOrigins) {
      log.info("允许跨域--" + globalUrl);
      corsConfiguration.addAllowedOrigin(globalUrl);
    }
    // 是否携带cookie跨域
    corsConfiguration.setAllowCredentials(false);

    // 允许跨域的路径
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsWebFilter(source);
  }
}
