package com.enba.cloud.users.api.resp;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PersonalCenterResp {

  @ApiModelProperty("用户ID")
  private Long userId;

  @ApiModelProperty("手机号码")
  private String mobile;

  @ApiModelProperty("邮箱")
  private String email;

  @ApiModelProperty("真实姓名")
  private String realName;

  @ApiModelProperty("昵称")
  private String nickName;

  @ApiModelProperty("头像")
  private String avatar;

  @ApiModelProperty("性别(0-未知,1-男,2-女)")
  private Integer gender;

  @ApiModelProperty("生日")
  private LocalDate birthday;

  @ApiModelProperty("注册时间")
  private LocalDateTime registerTime;
}
