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
 * 订单支付表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_order_payment")
@ApiModel(value = "OrderPayment对象", description = "订单支付表")
@Data
public class OrderPayment implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("支付记录ID")
  @TableId("payment_id")
  private Long paymentId;

  @ApiModelProperty("订单ID")
  private Long orderId;

  @ApiModelProperty("支付流水号，微信，支付等三方编号")
  private String paymentNo;

  @ApiModelProperty("支付方式(0-未知,1-支付宝,2-微信)")
  private Integer paymentMethod;

  @ApiModelProperty("支付金额")
  private BigDecimal paymentAmount;

  @ApiModelProperty("支付状态(0-未支付,1-支付成功,2-支付失败)")
  private Integer paymentStatus;

  @ApiModelProperty("支付时间")
  private LocalDateTime paymentTime;

  @ApiModelProperty("支付主题")
  private String subject;

  @ApiModelProperty("支付描述")
  private String body;

  @ApiModelProperty("支付额外信息")
  private String extraInfo;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;
}
