package com.enba.cloud.orders.api.order.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemReq {
  @ApiModelProperty("商品id")
  @NotNull(message = "商品id不能为空")
  private Long productId;

  @ApiModelProperty("商品sku id")
  @NotNull(message = "商品sku id不能为空")
  private Long productSkuId;

  @ApiModelProperty("商品数量")
  @NotNull(message = "商品数量不能为空")
  @Min(value = 1, message = "商品数量不能小于1")
  private Integer productQuantity;
}
