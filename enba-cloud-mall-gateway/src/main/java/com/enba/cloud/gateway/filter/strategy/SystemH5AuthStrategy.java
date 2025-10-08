package com.enba.cloud.gateway.filter.strategy;

import com.alibaba.fastjson.JSON;
import com.enba.boot.jwt.JwtHelper;
import com.enba.boot.redis.RedisOperator;
import com.enba.cloud.common.constants.TokenConst;
import com.enba.cloud.common.constants.TraceConstants;
import com.enba.cloud.common.utils.ServletUtils;
import com.enba.cloud.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** 系统facade-h5-application的认证策略实现 */
@Slf4j
public class SystemH5AuthStrategy implements AuthStrategy {

  // 系统A的标识
  private static final String SYSTEM_CODE = "system-h5";

  @Autowired private IgnoreWhiteProperties ignoreWhite;
  @Autowired private RedisOperator redisOperator;
  @Autowired private JwtHelper jwtHelper;
  @Autowired private IgnoreWhiteProperties ignoreWhiteProperties;

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
    // 系统A从Authorization头获取token
    String token = acquireAoken(exchange);

    // 验证token
    if (token == null) {
      return unauthorizedResponse(exchange, "令牌不能为空");
    }

    Claims claims = validateToken(token);
    if (Objects.isNull(claims)) {
      return unauthorizedResponse(exchange, "令牌非法");
    }

    // 系统A将用户信息放在请求头中
    String userId = parseUserIdFromToken(claims);
    String username = parseUsernameFromToken(claims);

    exchange
        .getRequest()
        .mutate()
        .header(TokenConst.TOKEN_USER_ID, userId)
        .header(TokenConst.TOKEN_USER_NAME, username)
        .header(TraceConstants.TRACE_ID_HEADER, MDC.get(TraceConstants.MDC_TRACE_ID_KEY))
        .build();

    log.info("开始进行系统认证,用户信息: {}", JSON.toJSONString(claims));
    return Mono.empty();
  }

  private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
    return ServletUtils.webFluxResponseWriter(
        exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED.value());
  }

  @Override
  public boolean isInWhitelist(ServerWebExchange exchange) {
    String path = exchange.getRequest().getPath().value();
    for (String whitelistPath : ignoreWhiteProperties.getWhites()) {
      if (pathMatches(path, whitelistPath)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void setMockUserInfo(ServerWebExchange exchange) {
    // 系统A的模拟用户信息
    String traceId = MDC.get(TraceConstants.MDC_TRACE_ID_KEY);
    exchange
        .getRequest()
        .mutate()
        .header(TokenConst.TOKEN_USER_ID, "mock-a-user-123")
        .header(TokenConst.TOKEN_USER_NAME, "mock-system-a-user")
        .header(TraceConstants.TRACE_ID_HEADER, traceId)
        .build();
  }

  // 验证token的有效性
  private Claims validateToken(String token) {
    // 系统A的真实token验证逻辑
    Claims claims;
    try {
      claims = jwtHelper.parseToken(token);
    } catch (Exception ex) {
      log.error("token解析异常:{}", ex.getMessage(), ex);
      return null;
    }
    return claims;
  }

  // 从token中解析用户ID
  private String parseUserIdFromToken(Claims claims) {
    // 系统A的真实解析逻辑
    return String.valueOf(claims.get(TokenConst.TOKEN_USER_ID));
  }

  // 从token中解析用户名
  private String parseUsernameFromToken(Claims claims) {
    // 系统A的真实解析逻辑
    return String.valueOf(claims.get(TokenConst.TOKEN_USER_NAME));
  }

  // 路径匹配逻辑

}
