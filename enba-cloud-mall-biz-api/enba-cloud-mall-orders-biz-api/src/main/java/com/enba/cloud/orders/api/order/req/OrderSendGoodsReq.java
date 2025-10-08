package com.enba.cloud.orders.api.order.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderSendGoodsReq {

  @ApiModelProperty(value = "订单ID")
  @NotNull(message = "订单ID不能为空")
  private Long orderId;

  @ApiModelProperty(value = "商家备注")
  private String sellerRemark;

  @ApiModelProperty(value = "物流单号")
  @NotBlank(message = "物流单号不能为空")
  private String shippingNo;

  @ApiModelProperty(value = "物流公司")
  @NotBlank(message = "物流公司不能为空")
  private String shippingCompany;
}
