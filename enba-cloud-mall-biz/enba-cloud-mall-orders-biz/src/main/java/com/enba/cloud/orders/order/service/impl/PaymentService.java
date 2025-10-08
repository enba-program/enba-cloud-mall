package com.enba.cloud.orders.order.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.enba.boot.mq.MqHelper;
import com.enba.boot.payment.base.PayProductBase;
import com.enba.boot.payment.base.RefundProductBase;
import com.enba.boot.payment.wechatpay.WechatPayHelper;
import com.enba.cloud.common.enums.OrderLogActionEnum;
import com.enba.cloud.common.enums.OrderPayStatusEnum;
import com.enba.cloud.common.enums.OrderShippingStatusEnum;
import com.enba.cloud.common.enums.OrderStatusEnum;
import com.enba.cloud.common.mq.MqConsts;
import com.enba.cloud.common.mq.payload.CreateOrderSuccessPayload;
import com.enba.cloud.orders.api.order.entity.Order;
import com.enba.cloud.orders.api.order.entity.OrderPayment;
import com.enba.cloud.orders.api.order.req.OrderRefundReq;
import com.enba.cloud.orders.api.order.resp.CallbackResult;
import com.enba.cloud.orders.manager.OrderLogManager;
import com.enba.cloud.orders.order.mapper.OrderMapper;
import com.enba.cloud.orders.order.mapper.OrderPaymentMapper;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction.TradeStateEnum;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class PaymentService {

  @Autowired(required = false)
  private WechatPayHelper wechatPayHelper;

  @Autowired private OrderMapper orderMapper;
  @Autowired private OrderPaymentMapper orderPaymentMapper;
  @Autowired private OrderLogManager orderLogManager;
  @Autowired private MqHelper mqHelper;

  /**
   * 生成微信支付二维码
   *
   * @param httpResponse 响应
   * @param outTradeNo 订单号
   * @param totalAmount 金额
   * @param subject 标题
   * @throws IOException 抛出IO异常
   */
  public void payNativeQrCode(
      HttpServletResponse httpResponse, String outTradeNo, BigDecimal totalAmount, String subject)
      throws IOException {
    PayProductBase payProductBase = new PayProductBase();
    payProductBase.setNotifyUrl("http://ik4c3s.natappfree.cc/api/order/pay-callback");
    payProductBase.setOutTradeNo(outTradeNo);
    payProductBase.setTotalAmount(totalAmount.toString());
    payProductBase.setSubject(subject);

    // 调起微信支付
    PrepayResponse prepay = wechatPayHelper.payNative(payProductBase);
    QrCodeUtil.generate(prepay.getCodeUrl(), 256, 256, "", httpResponse.getOutputStream());
  }

  public String test() {
    return "test";
  }

  /**
   * 微信支付回调
   *
   * @param request 请求
   * @return 回调结果
   * @throws IOException 抛出IO异常
   */
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<CallbackResult> notifyCallback(HttpServletRequest request)
      throws IOException {
    return wechatPayHelper.notifyCallback(
        request,
        (transaction) -> {
          try {
            // 根据回调结果自定义业务实现，这里简单打印
            log.info("微信支付成功回调：{}", JSON.toJSONString(transaction));
            String outTradeNo = transaction.getOutTradeNo();

            Order order =
                orderMapper.selectOne(
                    Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outTradeNo));
            if (order == null) {
              log.error("支付回调，订单不存在：{}", outTradeNo);
              return null;
            }

            OrderPayment payment =
                orderPaymentMapper.selectOne(
                    Wrappers.<OrderPayment>lambdaQuery()
                        .eq(OrderPayment::getOrderId, order.getOrderId()));
            if (payment == null) {
              log.error("支付回调，订单支付记录不存在：{}", outTradeNo);
              return null;
            }

            if (TradeStateEnum.SUCCESS.equals(transaction.getTradeState())) {
              // 订单支付成功，更新订单状态等
              order.setPayTime(LocalDateTime.parse(transaction.getSuccessTime()));
              order.setPayStatus(OrderPayStatusEnum.PAID.getStatus());
              order.setOrderStatus(OrderStatusEnum.PAID.getStatus());
              order.setShippingStatus(OrderShippingStatusEnum.WAIT_SHIPPING.getShippingStatus());

              payment.setPaymentStatus(OrderPayStatusEnum.PAID.getStatus());
            } else {
              // 支付失败
              log.info("微信支付失败回调：{}", JSON.toJSONString(transaction));
              order.setPayStatus(OrderPayStatusEnum.PAY_FAIL.getStatus());
              // 订单关闭
              order.setOrderStatus(OrderStatusEnum.CLOSED.getStatus());
              order.setCloseTime(LocalDateTime.now());

              payment.setPaymentStatus(OrderPayStatusEnum.PAY_FAIL.getStatus());
            }
            // 更新订单
            orderMapper.updateById(order);

            // 更新订单支付记录
            payment.setPaymentNo(transaction.getTransactionId());
            payment.setPaymentTime(LocalDateTime.parse(transaction.getSuccessTime()));
            orderPaymentMapper.updateById(payment);

            // 操作日志
            orderLogManager.addOrderLog(
                order.getOrderId(), OrderLogActionEnum.USER_PAY_ORDER, "用户支付订单");

            // 如果支付成功，发送消息（提醒发货，发放积分，发放优惠劵等）
            mqHelper.sendMessageByTopic(
                MqConsts.ORDER_TOPIC + ":" + MqConsts.ORDER_PAY_SUCCESS_TAG,
                new CreateOrderSuccessPayload().setOrderNo(outTradeNo).serialize(),
                this::test);
          } catch (Exception ex) {
            log.error("微信支付回调异常：{}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CallbackResult.err());
          }

          return TradeStateEnum.SUCCESS.equals(transaction.getTradeState())
              ? ResponseEntity.status(HttpStatus.OK).body(CallbackResult.ok())
              : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CallbackResult.err());
        });
  }

  private ResponseEntity<CallbackResult> apply(Transaction transaction) {
    // 根据回调结果自定义业务实现，这里简单打印
    log.info("微信支付成功回调：{}", JSON.toJSONString(transaction));
    if (TradeStateEnum.SUCCESS.equals(transaction.getTradeState())) {
      // 订单支付成功，更新订单状态
      String outTradeNo = transaction.getOutTradeNo();

      Order order =
          orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outTradeNo));
    } else if (TradeStateEnum.CLOSED.equals(transaction.getTradeState())) {
      // 订单已关闭，更新订单状态
    }

    return TradeStateEnum.SUCCESS.equals(transaction.getTradeState())
        ? ResponseEntity.status(HttpStatus.OK).body(CallbackResult.ok())
        : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CallbackResult.err());
  }

  public boolean pay(String orderNo, String paymentNo, BigDecimal payAmount) {
    return false;
  }

  public boolean refund(OrderRefundReq req, Order order) {
    RefundProductBase refundProductBase = new RefundProductBase();
    refundProductBase.setOutRefundNo("T" + order.getOrderNo());
    refundProductBase.setOutTradeNo(order.getOrderNo());
    refundProductBase.setRefundAmount(order.getPayAmount().toString());
    refundProductBase.setRefundReason(req.getRefundReason());

    // 申请退款
    log.info("申请退款: {}", refundProductBase);
    wechatPayHelper.refund(refundProductBase);

    return true;
  }
}
