package com.enba.cloud.orders.api.order.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderCommentOrderReq {

  @ApiModelProperty("订单ID")
  @NotNull(message = "订单ID不能为空")
  private Long orderId;

  @ApiModelProperty("评价")
  @NotBlank(message = "评价不能为空")
  private String commentContent;

  @ApiModelProperty("评分")
  @NotNull(message = "评分不能为空")
  private Integer rating;

  @ApiModelProperty("图片")
  private String commentImages;

  @ApiModelProperty("是否匿名")
  @NotNull(message = "是否匿名不能为空")
  private Boolean isAnonymous;
}
