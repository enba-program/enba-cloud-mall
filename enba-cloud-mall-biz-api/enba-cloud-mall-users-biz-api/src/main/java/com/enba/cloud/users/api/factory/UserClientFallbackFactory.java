package com.enba.cloud.users.api.factory;

import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.users.api.client.UserClient;
import com.enba.cloud.users.api.resp.PersonalCenterResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

  private static final Logger log = LoggerFactory.getLogger(UserClientFallbackFactory.class);

  @Override
  public UserClient create(Throwable cause) {
    return new UserClient() {
      @Override
      public Result<PersonalCenterResp> personalCenter(Long userId) {
        log.error("UserClient.personalCenter调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UserClient personalCenter error");
      }
    };
  }
}
