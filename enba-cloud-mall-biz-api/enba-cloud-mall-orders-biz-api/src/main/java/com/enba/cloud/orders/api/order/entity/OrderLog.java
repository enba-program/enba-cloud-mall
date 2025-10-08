package com.enba.cloud.orders.api.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单操作日志表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_order_log")
@ApiModel(value = "OrderLog对象", description = "订单操作日志表")
@Data
public class OrderLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("日志ID")
  @TableId(value = "log_id", type = IdType.AUTO)
  private Long logId;

  @ApiModelProperty("订单ID")
  private Long orderId;

  @ApiModelProperty("操作人ID")
  private Long userId;

  /**
   * @see com.enba.mallapi.enums.UserTypeEnum
   */
  @ApiModelProperty("操作人类型(0-系统,1-买家,2-卖家,3-客服)")
  private Integer userType;

  /**
   * @see com.enba.mallapi.enums.OrderLogActionEnum
   */
  @ApiModelProperty("订单操作动作(0-用户下单,1-用户取消,2-用户支付,3-仓库发货,4-用户确认收货,5-系统关闭,6-用户申请退款,7-同意退款,8-拒绝退款，)")
  private Integer action;

  @ApiModelProperty("操作备注")
  private String actionNote;

  @ApiModelProperty("操作IP")
  private String ipAddress;

  @ApiModelProperty("操作时间")
  private LocalDateTime createTime;
}
