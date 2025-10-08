package com.enba.cloud.orders.api.order.req;

import io.swagger.annotations.ApiModelProperty;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderPayReq {

  @ApiModelProperty("订单编号")
  @NotBlank(message = "订单编号不能为空")
  private String orderNo;

  @ApiModelProperty("支付方式(0-未知,1-支付宝,2-微信)")
  @NotNull(message = "支付方式不能为空")
  private Integer paymentMethod;

  HttpServletResponse httpResponse;
}
