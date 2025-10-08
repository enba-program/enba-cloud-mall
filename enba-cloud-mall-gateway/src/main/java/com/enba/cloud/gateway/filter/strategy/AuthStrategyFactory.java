package com.enba.cloud.gateway.filter.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/** 认证策略工厂，根据系统标识获取对应的认证策略 */
@Component
public class AuthStrategyFactory {

  private final Map<String, AuthStrategy> strategyMap;

  // 构造函数注入所有的认证策略实现
  public AuthStrategyFactory(List<AuthStrategy> authStrategies) {
    strategyMap = new HashMap<>();
    for (AuthStrategy strategy : authStrategies) {
      strategyMap.put(strategy.getSystemCode(), strategy);
    }
  }

  /**
   * 根据系统标识获取对应的认证策略
   *
   * @param systemCode 系统标识
   * @return 对应的认证策略，如果没有找到返回null
   */
  public AuthStrategy getStrategy(String systemCode) {
    if (systemCode == null) {
      return null;
    }
    return strategyMap.get(systemCode);
  }
}
