package com.enba.cloud.gateway.config.properties;

import com.alibaba.fastjson2.JSON;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreWhiteProperties implements InitializingBean {

  private List<String> whites = new ArrayList<>();

  public List<String> getWhites() {
    return whites;
  }

  public void setWhites(List<String> whites) {
    this.whites = whites;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("【系统初始化】加载了{}个白名单地址=>{}", whites.size(), JSON.toJSONString(whites));
  }
}
