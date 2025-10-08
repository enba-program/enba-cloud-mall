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
 * 订单退款表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_order_refund")
@ApiModel(value = "OrderRefund对象", description = "订单退款表")
@Data
public class OrderRefund implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("退款记录ID")
  @TableId("refund_id")
  private Long refundId;

  @ApiModelProperty("订单ID")
  private Long orderId;

  @ApiModelProperty("订单商品ID(如果是部分退款)")
  private Long orderItemId;

  @ApiModelProperty("退款单号")
  private String refundNo;

  @ApiModelProperty("退款金额")
  private BigDecimal refundAmount;

  @ApiModelProperty("退款原因")
  private String refundReason;

  @ApiModelProperty("退款类型(0-仅退款,1-退货退款)")
  private Integer refundType;

  @ApiModelProperty("退款状态(0-待处理,1-退款中,2-退款成功,3-退款失败)")
  private Integer refundStatus;

  @ApiModelProperty("退款方式")
  private String refundMethod;

  @ApiModelProperty("退款账户")
  private String refundAccount;

  @ApiModelProperty("退款原因编码")
  private String refundReasonCode;

  @ApiModelProperty("退款原因描述")
  private String refundReasonDesc;

  @ApiModelProperty("凭证图片")
  private String proofPics;

  @ApiModelProperty("卖家备注")
  private String sellerRemark;

  @ApiModelProperty("申请时间")
  private LocalDateTime createTime;

  @ApiModelProperty("处理时间")
  private LocalDateTime handleTime;

  @ApiModelProperty("完成时间")
  private LocalDateTime completeTime;
}
