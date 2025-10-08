package com.enba.cloud.users.api.factory;

import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.users.api.client.UserAddressClient;
import com.enba.cloud.users.api.req.SetDefaultAddressReq;
import com.enba.cloud.users.api.req.UserAddressReq;
import com.enba.cloud.users.api.resp.UserAddressResp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAddressClientFallbackFactory implements FallbackFactory<UserAddressClient> {

  private static final Logger log = LoggerFactory.getLogger(UserAddressClientFallbackFactory.class);

  @Override
  public UserAddressClient create(Throwable cause) {
    return new UserAddressClient() {
      @Override
      public Result<Boolean> addAddress(UserAddressReq req, Long userId) {
        log.error("UserAddressClient.addAddress 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UserAddressClient addAddress error");
      }

      @Override
      public Result<Boolean> updateAddress(UserAddressReq req, Long userId) {
        log.error("UserAddressClient.updateAddress 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UserAddressClient updateAddress error");
      }

      @Override
      public Result<Boolean> deleteAddress(Long id, Long userId) {
        log.error("UserAddressClient.deleteAddress 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UserAddressClient deleteAddress error");
      }

      @Override
      public Result<UserAddressResp> getAddress(Long id, Long userId) {
        log.error("UserAddressClient.getAddress 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UserAddressClient getAddress error");
      }

      @Override
      public Result<Boolean> setDefaultAddress(SetDefaultAddressReq req, Long userId) {
        log.error("UserAddressClient.setDefaultAddress 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UserAddressClient setDefaultAddress error");
      }

      @Override
      public Result<List<UserAddressResp>> getUserAddresses(Long userId) {
        log.error("UserAddressClient.getUserAddresses 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UserAddressClient getUserAddresses error");
      }
    };
  }
}
