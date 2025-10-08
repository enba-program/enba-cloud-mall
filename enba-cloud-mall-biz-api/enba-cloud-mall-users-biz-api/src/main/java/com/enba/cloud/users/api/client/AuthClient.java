package com.enba.cloud.users.api.client;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.users.api.factory.AuthClientFallbackFactory;
import com.enba.cloud.users.api.req.LoginReq;
import com.enba.cloud.users.api.req.RegisteReq;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    contextId = "AuthClient1",
    name = ServiceAppConstants.USER_SERVICE_NAME,
    fallbackFactory = AuthClientFallbackFactory.class)
@ResponseBody
public interface AuthClient {

  @PostMapping("/api/auth/register")
  @ApiOperation("用户注册")
  Result<String> register(@Valid @RequestBody RegisteReq user);

  @PostMapping("/api/auth/login")
  @ApiOperation("用户登录")
  Result<Map<String, ?>> login(@Valid @RequestBody LoginReq loginInfo);

  @PostMapping("/api/auth/logout")
  @ApiOperation("用户登出")
  Result<String> logout();
}
