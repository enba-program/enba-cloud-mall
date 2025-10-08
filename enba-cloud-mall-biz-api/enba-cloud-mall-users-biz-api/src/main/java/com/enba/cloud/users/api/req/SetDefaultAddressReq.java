package com.enba.cloud.users.api.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SetDefaultAddressReq {

  @ApiModelProperty("收货地址ID")
  @NotNull(message = "收货地址ID不能为空")
  private Long id;

  // 是否默认地址(0-否,1-是)
  @ApiModelProperty("是否默认地址(0-否,1-是)")
  @NotNull(message = "是否默认不能为空")
  private Integer isDefault;
}
