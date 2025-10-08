package com.enba.cloud.shopping.api.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateQuantityRequest {

  @ApiModelProperty("购物车项id")
  @NotNull(message = "购物车项id不能为空")
  private Long itemId;

  @ApiModelProperty("数量")
  @NotNull(message = "数量不能为空")
  private Integer quantity;
}
