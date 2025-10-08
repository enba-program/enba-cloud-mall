package com.enba.cloud.gateway.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "login.check")
public class LoginCheckProperties {
  // 是否开启跳过登录校验的开关
  private boolean skipCheck;

  public boolean isSkipCheck() {
    return skipCheck;
  }

  public void setSkipCheck(boolean skipCheck) {
    this.skipCheck = skipCheck;
  }
}
