package com.enba.cloud.shopping.api.resp;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract class ShoppingCartResp {

  @Data
  public static class ShoppingCartItemResp {

    @ApiModelProperty(value = "购物车商品ID")
    private Long itemId;

    @ApiModelProperty(value = "购物车ID")
    private Long cartId;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "SKU ID")
    private Long skuId;

    @ApiModelProperty(value = "商品数量")
    private Integer quantity;

    @ApiModelProperty(value = "是否选中")
    private Integer isSelected;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品原价")
    private BigDecimal originPrice;

    @ApiModelProperty(value = "商品规格")
    private String specs;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String productImage;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;
  }

  @Data
  public static class ShoppingCart {

    @ApiModelProperty(value = "购物车ID")
    private Long cartId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "是否全选")
    private Integer isAllSelected;

    @ApiModelProperty(value = "购物车总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;
  }
}
