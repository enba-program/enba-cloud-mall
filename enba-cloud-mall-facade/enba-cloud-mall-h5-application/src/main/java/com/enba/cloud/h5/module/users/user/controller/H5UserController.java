package com.enba.cloud.h5.module.users.user.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.users.api.client.UserClient;
import com.enba.cloud.users.api.resp.PersonalCenterResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户表
 *
 * @author 恩爸编程
 * @since 2025-05-27
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class H5UserController {

  @Resource private UserClient userClient;

  @ApiOperation("个人中心-用户详情")
  @GetMapping("/personal-center")
  public Result<PersonalCenterResp> personalCenter(@CurrentUser Long userId) {
    return userClient.personalCenter(userId);
  }
}
