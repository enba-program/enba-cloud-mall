package com.enba.cloud.orders.api.ordercomment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单评价表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_order_comment")
@ApiModel(value = "OrderComment对象", description = "订单评价表")
@Data
public class OrderComment implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("评价ID")
  @TableId("comment_id")
  private Long commentId;

  @ApiModelProperty("订单ID")
  private Long orderId;

  @ApiModelProperty("订单商品ID")
  private Long orderItemId;

  @ApiModelProperty("评价用户ID")
  private Long userId;

  @ApiModelProperty("商品ID")
  private Long productId;

  @ApiModelProperty("商品SKU ID")
  private Long productSkuId;

  @ApiModelProperty("评分(1-5)")
  private Integer rating;

  @ApiModelProperty("评价内容")
  private String commentContent;

  @ApiModelProperty("评价图片(JSON数组)")
  private String commentImages;

  @ApiModelProperty("是否匿名(0-否,1-是)")
  private Integer isAnonymous;

  @ApiModelProperty("商家回复")
  private String replyContent;

  @ApiModelProperty("回复时间")
  private LocalDateTime replyTime;

  /**
   * @see com.enba.mallapi.enums.OrderCommentPassEnum
   */
  @ApiModelProperty("是否审核通过(0-待审核,1-通过,2-拒绝)")
  private Integer isPass;

  @ApiModelProperty("评价时间")
  private LocalDateTime createTime;
}
