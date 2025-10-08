package com.enba.cloud.users.api.req;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty("用户名")
  @NotBlank(message = "用户名不能为空")
  private String username;

  @ApiModelProperty("密码(加密存储)")
  @NotBlank(message = "密码不能为空")
  private String password;
}
