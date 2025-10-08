package com.enba.cloud.shopping.api.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddToCartRequest {

  @ApiModelProperty("商品spuid")
  @NotNull(message = "商品spuid不能为空")
  private Long spuId;

  @ApiModelProperty("sku id")
  @NotNull(message = "sku id不能为空")
  private Long skuId;

  @ApiModelProperty("数量")
  @NotNull(message = "数量不能为空")
  @Positive(message = "数量必须大于0")
  private Integer quantity;

  @ApiModelProperty(
      "规格 eg：[{\"valueId\":111,\"attrId\":222,\"valueName\":\"参数值名称\",\"attrName\":\"参数名称\"}]")
  @NotBlank(message = "规格不能为空")
  private String skuSpecEnums;
}
