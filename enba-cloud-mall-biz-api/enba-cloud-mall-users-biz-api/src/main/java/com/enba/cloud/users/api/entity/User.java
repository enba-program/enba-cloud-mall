package com.enba.cloud.users.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户表
 *
 * @author 恩爸编程
 * @since 2025-05-27
 */
@TableName("t_user")
@ApiModel(value = "User对象", description = "用户表")
@Data
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("用户ID")
  @TableId("user_id")
  private Long userId;

  @ApiModelProperty("用户名")
  private String username;

  @ApiModelProperty("手机号")
  private String mobile;

  @ApiModelProperty("邮箱")
  private String email;

  @ApiModelProperty("密码(加密存储)")
  private String password;

  @ApiModelProperty("密码盐值")
  private String salt;

  @ApiModelProperty("真实姓名")
  private String realName;

  @ApiModelProperty("昵称")
  private String nickname;

  @ApiModelProperty("头像URL")
  private String avatar;

  @ApiModelProperty("性别(0-未知,1-男,2-女)")
  private Integer gender;

  @ApiModelProperty("生日")
  private LocalDate birthday;

  @ApiModelProperty("注册时间")
  private LocalDateTime registerTime;

  @ApiModelProperty("最后登录时间")
  private LocalDateTime lastLoginTime;

  @ApiModelProperty("最后登录IP")
  private String lastLoginIp;

  @ApiModelProperty("状态(0-禁用,1-正常)")
  private Integer status;

  @ApiModelProperty("是否删除(0-否,1-是)")
  private Integer deleted;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty("更新时间")
  private LocalDateTime updateTime;
}
