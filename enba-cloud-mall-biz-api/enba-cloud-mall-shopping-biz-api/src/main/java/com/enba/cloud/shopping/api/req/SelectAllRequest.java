package com.enba.cloud.shopping.api.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SelectAllRequest {

  @ApiModelProperty("是否全选")
  @NotNull(message = "是否全选不能为空")
  private boolean isSelected;
}
