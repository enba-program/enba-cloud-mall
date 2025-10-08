package com.enba.cloud.orders.api.order.req;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderCreateReq {

  @ApiModelProperty("收货人姓名")
  @NotBlank(message = "收货人姓名不能为空")
  private String receiverName;

  @ApiModelProperty("收货人电话")
  @NotBlank(message = "收货人电话不能为空")
  private String receiverPhone;

  @ApiModelProperty("省")
  @NotBlank(message = "省不能为空")
  private String receiverProvince;

  @ApiModelProperty("市")
  @NotBlank(message = "市不能为空")
  private String receiverCity;

  @ApiModelProperty("区")
  @NotBlank(message = "区不能为空")
  private String receiverDistrict;

  @ApiModelProperty("详细地址")
  @NotBlank(message = "详细地址不能为空")
  private String receiverDetailAddress;

  @ApiModelProperty("买家留言")
  private String buyerMessage;

  @ApiModelProperty("买家备注")
  private String buyerRemark;

  @ApiModelProperty("配送方式")
  @NotNull(message = "配送方式不能为空")
  private Integer shippingMethod;

  /**
   * @see com.enba.mallapi.enums.OrderPayMethodEnum
   */
  @ApiModelProperty("订单来源")
  private Integer sourceType;

  @ApiModelProperty("订单项")
  @NotNull(message = "订单项不能为空")
  @Size(min = 1, message = "订单项不能为空")
  private List<OrderItemReq> orderItems;

  /**
   * @see com.enba.mallapi.enums.OrderPayMethodEnum
   */
  @ApiModelProperty("支付方式(0-未知,1-支付宝,2-微信)")
  @NotNull(message = "支付方式不能为空")
  private Integer paymentMethod;

  @ApiModelProperty("优惠券ID")
  private Long couponId;
}
