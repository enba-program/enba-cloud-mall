package com.enba.cloud.shopping.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@TableName("t_shopping_cart")
public class ShoppingCart {
  @TableId(type = IdType.AUTO)
  private Long cartId;

  private Long userId;

  @TableField("is_all_selected")
  private Integer isAllSelected; // 0-否,1-是

  @TableField("total_price")
  private BigDecimal totalPrice;

  @TableField("updated_at")
  private Date updatedAt;

  @TableField("created_at")
  private Date createdAt;
}
