package com.enba.cloud.users.api.factory;

import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.users.api.client.AuthClient;
import com.enba.cloud.users.api.req.LoginReq;
import com.enba.cloud.users.api.req.RegisteReq;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthClientFallbackFactory implements FallbackFactory<AuthClient> {

  private static final Logger log = LoggerFactory.getLogger(AuthClientFallbackFactory.class);

  @Override
  public AuthClient create(Throwable cause) {
    return new AuthClient() {
      @Override
      public Result<String> register(RegisteReq user) {
        log.error("AuthClient.register调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "AuthClient register error");
      }

      @Override
      public Result<Map<String, ?>> login(LoginReq loginInfo) {
        log.error("AuthClient.login调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "AuthClient login error");
      }

      @Override
      public Result<String> logout() {
        log.error("AuthClient.logout调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "AuthClient logout error");
      }
    };
  }
}
