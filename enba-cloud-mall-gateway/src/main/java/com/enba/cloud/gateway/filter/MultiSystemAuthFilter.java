package com.enba.cloud.gateway.filter;

import com.enba.cloud.common.constants.TraceConstants;
import com.enba.cloud.common.utils.TraceIdGenerator;
import com.enba.cloud.gateway.config.properties.LoginCheckProperties;
import com.enba.cloud.gateway.filter.strategy.AuthStrategy;
import com.enba.cloud.gateway.filter.strategy.AuthStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

/** 多系统认证全局过滤器 */
@Configuration
@Slf4j
public class MultiSystemAuthFilter {

  // 认证策略工厂
  private final AuthStrategyFactory strategyFactory;
  // 登录检查配置
  private final LoginCheckProperties loginCheckProperties;

  public MultiSystemAuthFilter(
      AuthStrategyFactory strategyFactory, LoginCheckProperties loginCheckProperties) {
    this.strategyFactory = strategyFactory;
    this.loginCheckProperties = loginCheckProperties;
  }

  @Bean
  @Order(-200) // 优先级，数值越小优先级越高
  public GlobalFilter authGlobalFilter() {
    return (exchange, chain) -> {
      String traceId = TraceIdGenerator.generate();
      // 创建TraceId
      MDC.put(TraceConstants.MDC_TRACE_ID_KEY, traceId);

      // 1. 获取系统标识（从请求头中获取，也可以从路径等其他地方获取）
      String systemCode = exchange.getRequest().getHeaders().getFirst("X-System-Code");

      // 2. 验证系统标识是否存在
      if (systemCode == null || systemCode.trim().isEmpty()) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        return exchange.getResponse().setComplete();
      }

      // 3. 获取对应的认证策略
      AuthStrategy strategy = strategyFactory.getStrategy(systemCode);
      if (strategy == null) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        return exchange.getResponse().setComplete();
      }

      // 4. 检查是否在白名单中，如果是则直接放行
      if (strategy.isInWhitelist(exchange)) {
        return chain.filter(exchange);
      }

      // 5. 如果开关开启，跳过校验并设置mock的用户信息
      if (loginCheckProperties.isSkipCheck()) {
        strategy.setMockUserInfo(exchange);
        return chain
            .filter(exchange)
            .doFinally(signalType -> MDC.remove(TraceConstants.MDC_TRACE_ID_KEY));
      }

      // 6. 执行真实的登录校验逻辑
      return strategy
          .authenticate(exchange)
          .then(chain.filter(exchange))
          .doFinally(signalType -> MDC.remove(TraceConstants.MDC_TRACE_ID_KEY));
    };
  }
}
