package com.enba.cloud.users.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enba.cloud.users.api.entity.User;
import com.enba.cloud.users.api.req.RegisteReq;
import com.enba.cloud.users.api.resp.PersonalCenterResp;

/**
 * 用户表 服务类
 *
 * @author 恩爸编程
 * @since 2025-05-27
 */
public interface IUserService extends IService<User> {
  /**
   * 用户注册
   *
   * @param user 用户信息
   * @return 注册结果
   */
  boolean register(RegisteReq user);

  /**
   * 用户登录
   *
   * @param username 用户名/手机号/邮箱
   * @param password 密码
   * @return 登录结果
   */
  User login(String username, String password);

  /**
   * 个人中心
   *
   * @param userId 用户ID
   * @return 个人中心信息
   */
  PersonalCenterResp personalCenter(Long userId);
}
