package com.enba.cloud.orders.api.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单商品表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_order_item")
@ApiModel(value = "OrderItem对象", description = "订单商品表")
@Data
public class OrderItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("订单商品ID")
  @TableId("item_id")
  private Long itemId;

  @ApiModelProperty("订单ID")
  private Long orderId;

  @ApiModelProperty("商品ID")
  private Long productId;

  @ApiModelProperty("商品SKU ID")
  private Long productSkuId;

  @ApiModelProperty("商品名称")
  private String productName;

  @ApiModelProperty("商品SKU名称")
  private String productSkuName;

  @ApiModelProperty("商品图片")
  private String productPic;

  @ApiModelProperty("商品单价")
  private BigDecimal productPrice;

  @ApiModelProperty("购买数量")
  private Integer productQuantity;

  @ApiModelProperty("商品小计")
  private BigDecimal productTotalPrice;

  @ApiModelProperty("商品规格JSON")
  private String productSpecJson;

  @ApiModelProperty("促销信息")
  private String promotionInfo;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;
}
