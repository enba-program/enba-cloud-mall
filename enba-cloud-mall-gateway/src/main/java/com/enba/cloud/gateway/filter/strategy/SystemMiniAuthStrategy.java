package com.enba.cloud.gateway.filter.strategy;

import com.enba.cloud.common.constants.TokenConst;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** 系统facade-h5-application的认证策略实现 */
public class SystemMiniAuthStrategy implements AuthStrategy {

  // 系统B的标识
  private static final String SYSTEM_CODE = "system-mini";

  // 系统B的白名单路径
  private static final String[] WHITELIST_PATHS = {"/api/b/open/**", "/api/b/auth"};

  @Override
  public String getSystemCode() {
    return SYSTEM_CODE;
  }

  @Override
  public String acquireAoken(ServerWebExchange exchange) {
    return exchange.getRequest().getHeaders().getFirst(TokenConst.TOKEN_HEADER);
  }

  @Override
  public Mono<Void> authenticate(ServerWebExchange exchange) {
    // 系统B从X-Token头获取token
    String token = acquireAoken(exchange);

    // 验证token
    if (token == null || !validateToken(token)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    // 系统B将用户信息放在X-B-User和X-B-Role头中
    String userInfo = parseUserInfoFromToken(token);
    String role = parseRoleFromToken(token);

    exchange.getRequest().mutate().header("X-B-User", userInfo).header("X-B-Role", role).build();

    return Mono.empty();
  }

  @Override
  public boolean isInWhitelist(ServerWebExchange exchange) {
    String path = exchange.getRequest().getPath().value();
    for (String whitelistPath : WHITELIST_PATHS) {
      if (pathMatches(path, whitelistPath)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void setMockUserInfo(ServerWebExchange exchange) {
    // 系统B的模拟用户信息
    exchange
        .getRequest()
        .mutate()
        .header("X-B-User", "mock-b-user-456")
        .header("X-B-Role", "admin")
        .build();
  }

  // 验证token的有效性
  private boolean validateToken(String token) {
    // 系统B的真实token验证逻辑
    return true;
  }

  // 从token中解析用户信息
  private String parseUserInfoFromToken(String token) {
    // 系统B的真实解析逻辑
    return "system-b-user-2";
  }

  // 从token中解析角色
  private String parseRoleFromToken(String token) {
    // 系统B的真实解析逻辑
    return "user";
  }
}
