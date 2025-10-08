package com.enba.cloud.orders.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.enba.cloud.orders.api.order.entity.Order;
import com.enba.cloud.orders.api.order.req.OrderCancelReq;
import com.enba.cloud.orders.api.order.req.OrderCommentOrderReq;
import com.enba.cloud.orders.api.order.req.OrderCreateReq;
import com.enba.cloud.orders.api.order.req.OrderPayReq;
import com.enba.cloud.orders.api.order.req.OrderRefundReq;
import com.enba.cloud.orders.api.order.req.OrderSendGoodsReq;
import com.enba.cloud.orders.api.order.resp.CallbackResult;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

/**
 * 订单主表 服务类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
public interface IOrderService extends IService<Order> {

  /**
   * 创建订单
   *
   * @param orderCreateReq 创建订单参数
   * @param userId 用户ID
   * @return 订单编号
   */
  String createOrder(OrderCreateReq orderCreateReq, Long userId);

  /**
   * 支付订单
   *
   * @param req 支付参数
   */
  void payOrder(HttpServletResponse httpResponse, OrderPayReq req) throws IOException;

  /**
   * 取消订单
   *
   * @param req 取消订单参数
   * @param userId 用户ID
   */
  void cancelOrder(OrderCancelReq req, Long userId);

  /**
   * 关闭订单
   *
   * @param orderId 订单ID
   * @param userId 用户ID
   */
  void closeOrder(Long orderId, Long userId);

  /**
   * 用户申请退款
   *
   * @param req 订单
   * @param userId 用户ID
   */
  void refundOrder(OrderRefundReq req, Long userId);

  /**
   * 发货订单
   *
   * @param req 订单
   */
  void sendGoods(OrderSendGoodsReq req);

  /**
   * 收货订单
   *
   * @param orderId 订单ID
   * @param userId 用户ID
   */
  void receiveOrder(Long orderId, Long userId);

  /**
   * 评价订单
   *
   * @param req 订单
   * @param userId 用户ID
   */
  void commentOrder(OrderCommentOrderReq req, Long userId);

  /**
   * 支付回调
   *
   * @param request 请求
   * @return 回调结果
   */
  ResponseEntity<CallbackResult> payCallback(HttpServletRequest request) throws IOException;
}
