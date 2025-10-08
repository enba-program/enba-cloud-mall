package com.enba.cloud.h5.module.users.user.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.users.api.client.AuthClient;
import com.enba.cloud.users.api.req.LoginReq;
import com.enba.cloud.users.api.req.RegisteReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "用户认证接口")
public class H5AuthController {

  @Autowired private AuthClient authClient;

  @PostMapping("/register")
  @ApiOperation("用户注册")
  public Result<String> register(@Valid @RequestBody RegisteReq user) {
    return authClient.register(user);
  }

  @PostMapping("/login")
  @ApiOperation("用户登录")
  public Result<Map<String, ?>> login(@Valid @RequestBody LoginReq loginInfo) {
    return authClient.login(loginInfo);
  }

  @PostMapping("/logout")
  @ApiOperation("用户登出")
  public Result<String> logout() {
    // 真正的无状态,退出功能由客户端控制,结合redis可以实现更高级的功能
    return authClient.logout();
  }
}
