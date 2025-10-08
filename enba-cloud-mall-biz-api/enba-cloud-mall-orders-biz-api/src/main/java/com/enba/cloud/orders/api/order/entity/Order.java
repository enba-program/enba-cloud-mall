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
 * 订单主表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_order")
@ApiModel(value = "Order对象", description = "订单主表")
@Data
public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("订单ID")
  @TableId("order_id")
  private Long orderId;

  @ApiModelProperty("订单编号")
  private String orderNo;

  @ApiModelProperty("用户ID")
  private Long userId;

  @ApiModelProperty("订单状态(0-待付款,1-已付款,2-已发货,3-已完成,4-已取消,5-已关闭,6-退款中,7-已退款)")
  private Integer orderStatus;

  @ApiModelProperty("支付状态(0-未支付,1-已支付,2-支付失败)")
  private Integer payStatus;

  @ApiModelProperty("物流状态(0-未发货,1-已发货,2-已收货)")
  private Integer shippingStatus;

  @ApiModelProperty("订单总金额")
  private BigDecimal totalAmount;

  @ApiModelProperty("实际支付金额")
  private BigDecimal payAmount;

  @ApiModelProperty("优惠金额")
  private BigDecimal discountAmount;

  @ApiModelProperty("运费")
  private BigDecimal shippingFee;

  @ApiModelProperty("配送方式(0-快递,1-自提)")
  private Integer shippingMethod;

  @ApiModelProperty("物流单号")
  private String shippingNo;

  @ApiModelProperty("物流公司")
  private String shippingCompany;

  @ApiModelProperty("收货人姓名")
  private String receiverName;

  @ApiModelProperty("收货人电话")
  private String receiverPhone;

  @ApiModelProperty("省")
  private String receiverProvince;

  @ApiModelProperty("市")
  private String receiverCity;

  @ApiModelProperty("区")
  private String receiverDistrict;

  @ApiModelProperty("详细地址")
  private String receiverAddress;

  @ApiModelProperty("买家留言")
  private String buyerMessage;

  @ApiModelProperty("买家备注")
  private String buyerRemark;

  @ApiModelProperty("卖家备注")
  private String sellerRemark;

  @ApiModelProperty("订单来源(0-PC,1-H5,2-APP,3-小程序)")
  private Integer sourceType;

  @ApiModelProperty("使用的优惠券ID")
  private Long couponId;

  @ApiModelProperty("优惠券金额")
  private BigDecimal couponAmount;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty("支付时间")
  private LocalDateTime payTime;

  @ApiModelProperty("发货时间")
  private LocalDateTime shippingTime;

  @ApiModelProperty("收货时间")
  private LocalDateTime receiveTime;

  @ApiModelProperty("取消时间")
  private LocalDateTime cancelTime;

  @ApiModelProperty("取消原因")
  private String cancelReason;

  @ApiModelProperty("关闭时间")
  private LocalDateTime closeTime;

  @ApiModelProperty("退款时间")
  private LocalDateTime refundTime;

  @ApiModelProperty("乐观锁版本号")
  private Integer version;

  @ApiModelProperty("删除标志(0-未删除,1-已删除)")
  private Integer deleted;
}
