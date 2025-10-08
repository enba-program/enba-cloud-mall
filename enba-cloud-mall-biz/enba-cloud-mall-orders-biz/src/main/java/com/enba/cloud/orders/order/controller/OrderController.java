package com.enba.cloud.orders.order.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.orders.api.order.client.OrderClient;
import com.enba.cloud.orders.api.order.req.OrderCancelReq;
import com.enba.cloud.orders.api.order.req.OrderCommentOrderReq;
import com.enba.cloud.orders.api.order.req.OrderCreateReq;
import com.enba.cloud.orders.api.order.req.OrderPayReq;
import com.enba.cloud.orders.api.order.req.OrderRefundReq;
import com.enba.cloud.orders.api.order.resp.CallbackResult;
import com.enba.cloud.orders.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单主表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "订单管理")
@RestController
public class OrderController implements OrderClient {

  @Resource private IOrderService orderService;

  /*
   购物车 → 点击“去结算,（下单）” → 确认收货地址（修改为“公司地址”）→ 选择“满199减20”优惠券 → 点击“提交订单，(支付)” → 进入支付页 → 点击“去支付” → 完成付款
  */
  @Override
  @ApiOperation("去结算,（下单）")
  @DistributedLock(tips = "已提交订单，请勿重复操作")
  public Result<String> createOrder(
      @RequestBody @Validated OrderCreateReq orderCreateReq, @CurrentUser Long userId) {
    String orderNo = orderService.createOrder(orderCreateReq, userId);
    return Result.success(orderNo);
  }

  @Override
  @ApiOperation("提交订单，(去支付)")
  @DistributedLock(tips = "正在支付，请勿重复操作")
  public void payOrder(@RequestBody OrderPayReq req) throws IOException {
    orderService.payOrder(req.getHttpResponse(), req);
  }

  @Override
  @ApiOperation("支付回调")
  public ResponseEntity<CallbackResult> payCallback(HttpServletRequest request) throws IOException {
    return orderService.payCallback(request);
  }

  @Override
  @ApiOperation("取消订单")
  @DistributedLock(tips = "取消订单中，请勿重复操作")
  public Result<Boolean> cancelOrder(
      @RequestBody @Validated OrderCancelReq req, @CurrentUser Long userId) {
    orderService.cancelOrder(req, userId);
    return Result.success(true);
  }

  @Override
  @ApiOperation("关闭订单")
  @DistributedLock(tips = "关闭订单中，请勿重复操作")
  public Result<Boolean> closeOrder(@RequestParam Long orderId, @CurrentUser Long userId) {
    orderService.closeOrder(orderId, userId);
    return Result.success(true);
  }

  @Override
  @ApiOperation("用户申请退款")
  @DistributedLock(tips = "退款中，请勿重复操作")
  public Result<Boolean> refundOrder(@RequestBody OrderRefundReq req, @CurrentUser Long userId) {
    orderService.refundOrder(req, userId);
    return Result.success(true);
  }

  @Override
  @ApiOperation("用户确认收货")
  @DistributedLock(tips = "收货中，请勿重复操作")
  public Result<Boolean> receiveOrder(
      @RequestParam @NotNull(message = "订单ID不能为空") Long orderId, @CurrentUser Long userId) {
    orderService.receiveOrder(orderId, userId);
    return Result.success(true);
  }

  @Override
  @ApiOperation("评价")
  @DistributedLock(tips = "评价中，请勿重复操作")
  public Result<Boolean> commentOrder(
      @RequestBody @Validated OrderCommentOrderReq req, @CurrentUser Long userId) {
    orderService.commentOrder(req, userId);
    return Result.success(true);
  }
}
