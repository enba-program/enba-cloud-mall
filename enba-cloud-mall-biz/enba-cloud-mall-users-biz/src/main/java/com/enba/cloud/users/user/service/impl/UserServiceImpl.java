package com.enba.cloud.users.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.boot.core.exception.BizException;
import com.enba.cloud.common.utils.IpUtil;
import com.enba.cloud.users.api.entity.User;
import com.enba.cloud.users.api.req.RegisteReq;
import com.enba.cloud.users.api.resp.PersonalCenterResp;
import com.enba.cloud.users.user.mapper.UserMapper;
import com.enba.cloud.users.user.service.IUserService;
import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户表 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
  // 密码盐长度
  private static final int SALT_LENGTH = 8;

  private final UserMapper userMapper;

  public UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public boolean register(RegisteReq req) {
    // 检查用户名/手机号/邮箱是否已存在
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    if (req.getUsername() != null && !req.getUsername().isEmpty()) {
      queryWrapper.eq("username", req.getUsername());
    }
    if (req.getMobile() != null && !req.getMobile().isEmpty()) {
      queryWrapper.or().eq("mobile", req.getMobile());
    }
    if (req.getEmail() != null && !req.getEmail().isEmpty()) {
      queryWrapper.or().eq("email", req.getEmail());
    }

    if (userMapper.selectCount(queryWrapper) > 0) {
      return false; // 已存在
    }

    User user = new User();
    user.setUsername(req.getUsername());
    user.setMobile(req.getMobile());
    user.setEmail(req.getEmail());

    // 设置默认状态
    user.setStatus(1);
    user.setRegisterTime(LocalDateTime.now());
    user.setUpdateTime(LocalDateTime.now());

    // 生成盐值并加密密码
    String salt = generateSalt();
    user.setSalt(salt);
    user.setPassword(encryptPassword(req.getPassword(), salt));

    return userMapper.insert(user) > 0;
  }

  @Override
  public User login(String username, String password) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();

    // 根据用户名/手机号/邮箱查询用户
    if (username.matches("^1[3-9]\\d{9}$")) { // 手机号格式
      queryWrapper.eq("mobile", username);
    } else if (username.contains("@")) { // 邮箱格式
      queryWrapper.eq("email", username);
    } else { // 用户名
      queryWrapper.eq("username", username);
    }

    User user = userMapper.selectOne(queryWrapper);

    if (user == null || user.getStatus() != 1) {
      return null; // 用户不存在或被禁用
    }

    // 验证密码
    String encryptPassword = encryptPassword(password, user.getSalt());
    if (!encryptPassword.equals(user.getPassword())) {
      BizException.throwEx("密码错误");
    }

    // 更新最后登录时间
    user.setLastLoginTime(LocalDateTime.now());
    // 获取客户端IP
    user.setLastLoginIp(getClientIp());
    userMapper.updateById(user);

    // 返回时不返回密码和盐值
    user.setPassword(null);
    user.setSalt(null);
    return user;
  }

  /** 生成随机盐值 */
  private static String generateSalt() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuilder salt = new StringBuilder();
    for (int i = 0; i < SALT_LENGTH; i++) {
      salt.append(chars.charAt(random.nextInt(chars.length())));
    }
    return salt.toString();
  }

  public static void main(String[] args) {
    System.out.println(generateSalt());
  }

  /** 加密密码 */
  private String encryptPassword(String password, String salt) {
    return DigestUtils.md5DigestAsHex((password + salt).getBytes());
  }

  /** 获取客户端IP */
  private String getClientIp() {
    return IpUtil.getClientIp();
  }

  @Override
  public PersonalCenterResp personalCenter(Long userId) {
    User user = userMapper.selectById(userId);
    if (user == null) return null;

    return new PersonalCenterResp()
        .setUserId(user.getUserId())
        .setMobile(user.getMobile())
        .setEmail(user.getEmail())
        .setRealName(user.getRealName())
        .setNickName(user.getNickname())
        .setAvatar(user.getAvatar())
        .setGender(user.getGender())
        .setBirthday(user.getBirthday())
        .setRegisterTime(user.getRegisterTime());
  }
}
