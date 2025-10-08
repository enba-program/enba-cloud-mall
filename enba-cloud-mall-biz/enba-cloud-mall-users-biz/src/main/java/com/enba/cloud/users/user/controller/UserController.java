package com.enba.cloud.users.user.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.users.api.client.UserClient;
import com.enba.cloud.users.api.resp.PersonalCenterResp;
import com.enba.cloud.users.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户表
 *
 * @author 恩爸编程
 * @since 2025-05-27
 */
@Api(tags = "用户管理")
@RestController
public class UserController implements UserClient {

  @Resource private IUserService userService;

  @Override
  @ApiOperation("个人中心-用户详情")
  public Result<PersonalCenterResp> personalCenter(@CurrentUser Long userId) {
    return Result.success(userService.personalCenter(userId));
  }
}
