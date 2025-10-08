package com.enba.cloud.gateway.filter.strategy;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** 认证策略接口，每个系统实现自己的认证逻辑 */
public interface AuthStrategy {

  /** 获取系统标识，用于匹配对应的策略 */
  String getSystemCode();

  /**
   * 获取token
   *
   * @param exchange 服务器交换对象
   * @return token
   */
  String acquireAoken(ServerWebExchange exchange);

  /**
   * 执行认证逻辑
   *
   * @param exchange 服务器交换对象
   * @return 认证结果，如果认证成功返回Mono.empty()，否则返回错误响应
   */
  Mono<Void> authenticate(ServerWebExchange exchange);

  /**
   * 检查请求是否在白名单中
   *
   * @param exchange 服务器交换对象
   * @return 如果在白名单中返回true，否则返回false
   */
  boolean isInWhitelist(ServerWebExchange exchange);

  /**
   * 设置模拟用户信息（当跳过认证时使用）
   *
   * @param exchange 服务器交换对象
   */
  void setMockUserInfo(ServerWebExchange exchange);

  /**
   * 路径匹配
   *
   * @param path 请求路径
   * @param pattern 匹配模式
   * @return 如果匹配返回true，否则返回false
   */
  default boolean pathMatches(String path, String pattern) {
    // 简单的路径匹配实现，实际可以使用Spring的PathMatcher
    return path.startsWith(pattern.replace("**", ""));
  }
}
