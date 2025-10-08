package com.enba.cloud.orders.api.order.client;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.orders.api.order.factory.OrderFallbackFactory;
import com.enba.cloud.orders.api.order.req.OrderCancelReq;
import com.enba.cloud.orders.api.order.req.OrderCommentOrderReq;
import com.enba.cloud.orders.api.order.req.OrderCreateReq;
import com.enba.cloud.orders.api.order.req.OrderPayReq;
import com.enba.cloud.orders.api.order.req.OrderRefundReq;
import com.enba.cloud.orders.api.order.resp.CallbackResult;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    contextId = "OrderClient",
    name = ServiceAppConstants.ORDER_SERVICE_NAME,
    fallbackFactory = OrderFallbackFactory.class)
@ResponseBody
public interface OrderClient {
  /*
   购物车 → 点击“去结算,（下单）” → 确认收货地址（修改为“公司地址”）→ 选择“满199减20”优惠券 → 点击“提交订单，(支付)” → 进入支付页 → 点击“去支付” → 完成付款
  */

  @PostMapping("/api/order/create")
  @ApiOperation("去结算,（下单）")
  @DistributedLock(tips = "已提交订单，请勿重复操作")
  Result<String> createOrder(
      @RequestBody @Validated OrderCreateReq orderCreateReq,
      @RequestParam(name = "userId") Long userId);

  @PostMapping("/api/order/pay")
  @ApiOperation("提交订单，(去支付)")
  @DistributedLock(tips = "正在支付，请勿重复操作")
  void payOrder(OrderPayReq req) throws IOException;

  @PostMapping("/api/order/pay-callback")
  @ApiOperation("支付回调")
  ResponseEntity<CallbackResult> payCallback(HttpServletRequest request) throws IOException;

  @PostMapping("/api/order/cancel")
  @ApiOperation("取消订单")
  @DistributedLock(tips = "取消订单中，请勿重复操作")
  Result<Boolean> cancelOrder(
      @RequestBody @Validated OrderCancelReq req, @RequestParam(name = "userId") Long userId);

  @PostMapping("/api/order/close")
  @ApiOperation("关闭订单")
  @DistributedLock(tips = "关闭订单中，请勿重复操作")
  Result<Boolean> closeOrder(
      @RequestParam(name = "orderId") Long orderId, @RequestParam(name = "userId") Long userId);

  @PostMapping("/api/order/refund")
  @ApiOperation("用户申请退款")
  @DistributedLock(tips = "退款中，请勿重复操作")
  Result<Boolean> refundOrder(
      @RequestBody OrderRefundReq req, @RequestParam(name = "userId") Long userId);

  @PostMapping("/api/order/receive")
  @ApiOperation("用户确认收货")
  @DistributedLock(tips = "收货中，请勿重复操作")
  Result<Boolean> receiveOrder(
      @RequestParam(name = "orderId") @NotNull(message = "订单ID不能为空") Long orderId,
      @RequestParam(name = "userId") Long userId);

  @PostMapping("/api/order/comment")
  @ApiOperation("评价")
  @DistributedLock(tips = "评价中，请勿重复操作")
  Result<Boolean> commentOrder(
      @RequestBody @Validated OrderCommentOrderReq req, @RequestParam(name = "userId") Long userId);
}
