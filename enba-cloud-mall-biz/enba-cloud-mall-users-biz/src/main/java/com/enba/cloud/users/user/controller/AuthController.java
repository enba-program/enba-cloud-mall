package com.enba.cloud.users.user.controller;

import com.enba.boot.core.base.Result;
import com.enba.boot.jwt.JwtHelper;
import com.enba.boot.redis.RedisOperator;
import com.enba.cloud.common.constants.TokenConst;
import com.enba.cloud.users.api.client.AuthClient;
import com.enba.cloud.users.api.entity.User;
import com.enba.cloud.users.api.req.LoginReq;
import com.enba.cloud.users.api.req.RegisteReq;
import com.enba.cloud.users.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "用户认证接口")
public class AuthController implements AuthClient {

  private final IUserService userService;
  private final JwtHelper jwtHelper;
  private final RedisOperator redisOperator;

  public AuthController(
      IUserService userService, JwtHelper jwtHelper, RedisOperator redisOperator) {
    this.userService = userService;
    this.jwtHelper = jwtHelper;
    this.redisOperator = redisOperator;
  }

  @Override
  @ApiOperation("用户注册")
  public Result<String> register(@Valid @RequestBody RegisteReq user) {
    boolean success = userService.register(user);
    if (success) {
      return Result.success("注册成功");
    } else {
      return Result.err(500, "用户名/手机号/邮箱已存在");
    }
  }

  @Override
  @ApiOperation("用户登录")
  public Result<Map<String, ?>> login(@Valid @RequestBody LoginReq loginInfo) {
    String username = loginInfo.getUsername();
    String password = loginInfo.getPassword();

    if (username == null || password == null) {
      return Result.err(500, "用户名和密码不能为空");
    }

    User user = userService.login(username, password);
    if (user != null) {
      // 返回用户信息(不包含敏感信息)
      Map<String, Object> result = new HashMap<>();
      result.put(TokenConst.TOKEN_USER_ID, user.getUserId());
      result.put(TokenConst.TOKEN_USER_NAME, user.getUsername());
      result.put(TokenConst.TOKEN_USER_PHONE, user.getMobile());
      result.put(TokenConst.TOKEN_USER_EMAIL, user.getEmail());
      result.put(TokenConst.TOKEN_USER_NICK_NAME, user.getNickname());
      result.put(TokenConst.TOKEN_USER_AVATAR, user.getAvatar());

      result.put(TokenConst.TOKEN_NAME, jwtHelper.createToken(result));

      // 缓存用户信息
      redisOperator.set(
          TokenConst.getH5TokenKey(String.valueOf(user.getUserId())),
          result,
          TokenConst.LOGIN_H5_TOKEN_TIMEOUT);

      return Result.success(result);
    } else {
      return Result.err(500, "用户名或密码错误");
    }
  }

  @ApiOperation("用户登出")
  public Result<String> logout() {
    // 真正的无状态,退出功能由客户端控制,结合redis可以实现更高级的功能
    return Result.success("登出成功");
  }
}
