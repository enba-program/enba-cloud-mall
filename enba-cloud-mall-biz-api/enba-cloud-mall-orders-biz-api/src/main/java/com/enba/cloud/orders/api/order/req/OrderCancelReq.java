package com.enba.cloud.orders.api.order.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderCancelReq {

  @ApiModelProperty("订单ID")
  @NotNull(message = "订单ID不能为空")
  private Long orderId;

  @ApiModelProperty("取消原因")
  @NotBlank(message = "取消原因不能为空")
  private String cancelReason;
}
