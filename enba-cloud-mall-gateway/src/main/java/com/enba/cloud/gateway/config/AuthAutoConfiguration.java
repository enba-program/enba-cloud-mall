package com.enba.cloud.gateway.config;

import com.enba.cloud.gateway.filter.strategy.SystemH5AuthStrategy;
import com.enba.cloud.gateway.filter.strategy.SystemMiniAuthStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 认证自动配置类，注册所有的认证策略 */
@Configuration
public class AuthAutoConfiguration {

  @Bean
  public SystemH5AuthStrategy systemAAuthStrategy() {
    return new SystemH5AuthStrategy();
  }

  @Bean
  public SystemMiniAuthStrategy systemBAuthStrategy() {
    return new SystemMiniAuthStrategy();
  }

  // 当添加新系统时，只需在这里添加对应的策略Bean
  // @Bean
  // public SystemCAuthStrategy systemCAuthStrategy() {
  //     return new SystemCAuthStrategy();
  // }
}
