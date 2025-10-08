package com.enba.cloud.shopping.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@TableName("t_shopping_cart_item")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartItem {
  @TableId(type = IdType.AUTO)
  private Long itemId;

  private Long cartId;

  private Long productId;

  private Long skuId;

  private Integer quantity;

  @TableField("is_selected")
  private Integer isSelected; // 0-否,1-是

  private BigDecimal price;

  @TableField("origin_price")
  private BigDecimal originPrice;

  private String specs;

  @TableField("product_name")
  private String productName;

  @TableField("product_image")
  private String productImage;

  @TableField("updated_at")
  private Date updatedAt;

  @TableField("created_at")
  private Date createdAt;
}
