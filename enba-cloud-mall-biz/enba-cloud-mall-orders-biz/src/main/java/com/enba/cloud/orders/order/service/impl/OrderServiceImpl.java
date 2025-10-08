package com.enba.cloud.orders.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.boot.core.exception.BizException;
import com.enba.boot.mq.MqHelper;
import com.enba.cloud.common.enums.OrderLogActionEnum;
import com.enba.cloud.common.enums.OrderPayStatusEnum;
import com.enba.cloud.common.enums.OrderRefundStatusEnum;
import com.enba.cloud.common.enums.OrderRefundTypeEnum;
import com.enba.cloud.common.enums.OrderShippingEnum;
import com.enba.cloud.common.enums.OrderShippingMethodEnum;
import com.enba.cloud.common.enums.OrderShippingStatusEnum;
import com.enba.cloud.common.enums.OrderSourceTypeEnum;
import com.enba.cloud.common.enums.OrderStatusEnum;
import com.enba.cloud.goods.api.sku.client.SkuClient;
import com.enba.cloud.goods.api.sku.entity.Sku;
import com.enba.cloud.orders.api.order.entity.Order;
import com.enba.cloud.orders.api.order.entity.OrderItem;
import com.enba.cloud.orders.api.order.entity.OrderPayment;
import com.enba.cloud.orders.api.order.entity.OrderRefund;
import com.enba.cloud.orders.api.order.req.OrderCancelReq;
import com.enba.cloud.orders.api.order.req.OrderCommentOrderReq;
import com.enba.cloud.orders.api.order.req.OrderCreateReq;
import com.enba.cloud.orders.api.order.req.OrderItemReq;
import com.enba.cloud.orders.api.order.req.OrderPayReq;
import com.enba.cloud.orders.api.order.req.OrderRefundReq;
import com.enba.cloud.orders.api.order.req.OrderSendGoodsReq;
import com.enba.cloud.orders.api.order.resp.CallbackResult;
import com.enba.cloud.orders.api.ordercomment.entity.OrderComment;
import com.enba.cloud.orders.manager.OrderCodeGenerator;
import com.enba.cloud.orders.manager.OrderLogManager;
import com.enba.cloud.orders.manager.OrderPaymentCodeGenerator;
import com.enba.cloud.orders.manager.OrderRefundCodeGenerator;
import com.enba.cloud.orders.order.mapper.OrderItemMapper;
import com.enba.cloud.orders.order.mapper.OrderMapper;
import com.enba.cloud.orders.order.mapper.OrderPaymentMapper;
import com.enba.cloud.orders.order.mapper.OrderRefundMapper;
import com.enba.cloud.orders.order.service.IOrderService;
import com.enba.cloud.orders.ordercomment.mapper.OrderCommentMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单主表 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;
  private final OrderPaymentMapper orderPaymentMapper;
  private final InventoryService inventoryService; // 库存服务（需实现）
  private final PaymentService paymentService; // 支付服务（需实现）
  private final NotificationService notificationService; // 通知服务（需实现）
  private final IntegralService integralService; // 积分服务（需实现）
  private final SkuClient skuClient;
  private final OrderCommentMapper orderCommentMapper;
  private final OrderRefundMapper orderRefundMapper;
  private final OrderLogManager orderLogManager;
  private final MqHelper mqHelper;

  /** 创建订单 */
  @Transactional(rollbackFor = Exception.class)
  public String createOrder(OrderCreateReq orderCreateReq, Long userId) {
    // 1. 参数校验
    validateOrder(orderCreateReq);

    List<OrderItemReq> orderItems = orderCreateReq.getOrderItems();
    List<Sku> skuList =
        skuClient
            .batchBySkuId(
                orderItems.stream().map(OrderItemReq::getProductSkuId).collect(Collectors.toSet()))
            .getData();

    if (skuList.size() != orderItems.size()) {
      BizException.throwEx("非法操作,商品SKU不存在");
    }

    Map<Long, Sku> skuIdMap = skuList.stream().collect(Collectors.toMap(Sku::getId, v -> v));

    // 2. 生成订单号
    String orderNo = OrderCodeGenerator.generateCode();

    // 3. 计算价格（总价、优惠、运费）
    BigDecimal totalAmount = calculateTotalAmount(orderCreateReq, skuIdMap);
    BigDecimal discountAmount = calculateDiscount(orderCreateReq);
    BigDecimal shippingFee = calculateShippingFee(orderCreateReq);
    BigDecimal payAmount = totalAmount.subtract(discountAmount).add(shippingFee);

    // 4. 创建订单主表记录
    Order order = new Order();
    //  订单编号, 用户ID
    order.setOrderNo(orderNo);
    order.setUserId(userId);
    // 【订单状态：待付款】【支付状态：未支付】【发货状态：未发货】
    order.setOrderStatus(OrderStatusEnum.WAIT_PAYMENT.getStatus());
    order.setPayStatus(OrderPayStatusEnum.UNPAID.getStatus());
    order.setShippingStatus(OrderShippingEnum.WAIT_SHIP.getStatus());
    // 订单价格
    order.setTotalAmount(totalAmount);
    order.setPayAmount(payAmount);
    order.setDiscountAmount(discountAmount);
    order.setShippingFee(shippingFee);
    // 先固定 快递邮寄
    order.setShippingMethod(OrderShippingMethodEnum.EXPRESS.getShippingMethod());
    // 订单收货人信息
    order.setReceiverName(orderCreateReq.getReceiverName());
    order.setReceiverPhone(orderCreateReq.getReceiverPhone());
    order.setReceiverProvince(orderCreateReq.getReceiverProvince());
    order.setReceiverCity(orderCreateReq.getReceiverCity());
    order.setReceiverDistrict(orderCreateReq.getReceiverDistrict());
    order.setReceiverAddress(orderCreateReq.getReceiverDetailAddress());
    order.setBuyerMessage(orderCreateReq.getBuyerMessage());
    order.setBuyerRemark(orderCreateReq.getBuyerRemark());
    //  订单来源 先固定 H5
    order.setSourceType(OrderSourceTypeEnum.H5.getSourceType());
    // 优惠券ID, 优惠券金额    先固定
    order.setCouponId(111111L);
    order.setCouponAmount(null);
    orderMapper.insert(order);

    // 订单明细
    List<OrderItem> orderItemList = Lists.newArrayList();
    for (OrderItemReq orderItemReq : orderItems) {
      OrderItem orderItem = new OrderItem();
      orderItem.setOrderId(order.getOrderId());
      orderItem.setProductId(orderItemReq.getProductId());
      orderItem.setProductSkuId(orderItemReq.getProductSkuId());
      Sku sku = skuIdMap.get(orderItemReq.getProductSkuId());
      Objects.requireNonNull(sku, "商品SKU不存在");
      orderItem.setProductName(sku.getSkuName());
      orderItem.setProductSkuName(sku.getSkuName());
      orderItem.setProductPic(sku.getSkuPicUrl());
      orderItem.setProductPrice(sku.getSkuPrice());
      orderItem.setProductQuantity(orderItemReq.getProductQuantity());
      orderItem.setProductTotalPrice(
          sku.getSkuPrice().multiply(BigDecimal.valueOf(orderItemReq.getProductQuantity())));
      orderItem.setProductSpecJson(sku.getSkuSpecEnums());
      orderItem.setPromotionInfo("");
      orderItemList.add(orderItem);
    }
    // 5. 创建订单商品明细
    orderItemMapper.insert(orderItemList);

    // 6. 预扣减库存（分布式锁或乐观锁）
    inventoryService.lockStock(orderCreateReq.getOrderItems());

    // 7. 操作日志
    orderLogManager.addOrderLog(order.getOrderId(), OrderLogActionEnum.USER_CREATE_ORDER, "用户下单");
    return orderNo;
  }

  /** 计算运费 */
  private BigDecimal calculateShippingFee(OrderCreateReq orderCreateReq) {
    BigDecimal shippingFee = null;
    try {
      // 计算运费（模拟）
      shippingFee = new BigDecimal("10.00");
    } catch (Exception e) {
      log.error("计算运费失败：{}", JSON.toJSONString(orderCreateReq), e);
      BizException.throwEx("计算运费失败");
    }
    return shippingFee;
  }

  /** 优惠金额 */
  private BigDecimal calculateDiscount(OrderCreateReq orderCreateReq) {
    BigDecimal discountAmount = null;
    try {
      // 优惠金额（模拟）
      discountAmount = new BigDecimal("5.00");
    } catch (Exception e) {
      log.error("计算优惠金额失败{}", JSON.toJSONString(orderCreateReq), e);
      BizException.throwEx("计算优惠金额失败");
    }
    return discountAmount;
  }

  /** 计算总价 */
  private BigDecimal calculateTotalAmount(OrderCreateReq orderCreateReq, Map<Long, Sku> skuIdMap) {
    return orderCreateReq.getOrderItems().stream()
        .map(
            item -> {
              Sku sku = skuIdMap.get(item.getProductSkuId());
              return sku.getSkuPrice().multiply(BigDecimal.valueOf(item.getProductQuantity()));
            })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /** 参数校验 */
  private void validateOrder(OrderCreateReq orderCreateReq) {
    if (orderCreateReq.getOrderItems().isEmpty()) {
      BizException.throwEx("订单商品不能为空");
    }
    for (OrderItemReq item : orderCreateReq.getOrderItems()) {
      if (item.getProductId() == null || item.getProductId() <= 0) {
        BizException.throwEx("商品ID不能为空");
      }
      if (item.getProductSkuId() == null || item.getProductSkuId() <= 0) {
        BizException.throwEx("商品SKUID不能为空");
      }
      if (item.getProductQuantity() == null || item.getProductQuantity() <= 0) {
        BizException.throwEx("商品数量不能为空");
      }
    }
  }

  /** 支付订单 */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void payOrder(HttpServletResponse httpResponse, OrderPayReq req) throws IOException {
    String orderNo = req.getOrderNo();
    Integer paymentMethod = req.getPaymentMethod();

    Order order =
        orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, orderNo));
    if (order == null) {
      BizException.throwEx("订单不存在");
    }

    if (!Objects.equals(order.getPayStatus(), OrderStatusEnum.WAIT_PAYMENT.getStatus())) {
      BizException.throwEx("订单" + orderNo + "非待付款状态");
    }

    // 获取订单明细
    List<OrderItem> orderItemList =
        orderItemMapper.selectList(
            Wrappers.<OrderItem>lambdaQuery().eq(OrderItem::getOrderId, order.getOrderId()));

    String subject = orderItemList.stream().map(OrderItem::getProductSkuName).findFirst().get();
    // 调用支付网关（模拟）
    paymentService.payNativeQrCode(httpResponse, orderNo, order.getPayAmount(), subject);

    // 7. 创建支付记录（异步或同步）
    OrderPayment payment = new OrderPayment();
    payment.setOrderId(order.getOrderId());
    payment.setPaymentNo(OrderPaymentCodeGenerator.generateCode());
    payment.setPaymentMethod(paymentMethod);
    payment.setPaymentAmount(order.getPayAmount());
    payment.setPaymentStatus(OrderPayStatusEnum.UNPAID.getStatus());
    payment.setSubject(subject);
    payment.setBody("支付描述");
    payment.setExtraInfo("支付额外信息");
    orderPaymentMapper.insert(payment);

    // TODO 发送延迟队列消息，超时（30分钟）未支付则取消订单

  }

  @Override
  public ResponseEntity<CallbackResult> payCallback(HttpServletRequest request) throws IOException {
    return paymentService.notifyCallback(request);
  }

  // 其他方法：取消订单、关闭订单、退款等...

  /** 取消订单 */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void cancelOrder(OrderCancelReq req, Long userId) {
    Order order = orderMapper.selectById(req.getOrderId());
    if (order == null) {
      BizException.throwEx("订单不存在");
    }
    if (!OrderStatusEnum.WAIT_PAYMENT.getStatus().equals(order.getOrderStatus())) {
      BizException.throwEx("仅支持待付款订单取消");
    }
    if (!order.getUserId().equals(userId)) {
      BizException.throwEx("非法操作,订单不属于当前用户");
    }

    // 恢复库存
    // TODO inventoryService.unlockStock(order.getItems());

    // 更新订单状态 已取消
    order.setOrderStatus(OrderStatusEnum.CANCELLED.getStatus());
    order.setCancelTime(LocalDateTime.now());
    order.setCancelReason(req.getCancelReason());
    orderMapper.updateById(order);

    notificationService.notifyUser(req.getOrderId(), userId, "取消订单");

    // 添加操作日志
    log.info(String.format("用户%s取消订单: %s,原因%s", userId, order.getOrderNo(), req.getCancelReason()));
    orderLogManager.addOrderLog(order.getOrderId(), OrderLogActionEnum.USER_CANCEL_ORDER, "用户取消订单");
  }

  /** 关闭订单 */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void closeOrder(Long orderId, Long userId) {
    Order order = orderMapper.selectById(orderId);
    if (order == null) {
      BizException.throwEx("订单不存在");
    }
    if (!OrderStatusEnum.WAIT_PAYMENT.getStatus().equals(order.getOrderStatus())
        && !OrderStatusEnum.PAID.getStatus().equals(order.getOrderStatus())) {
      BizException.throwEx("仅支持待付款,已付款订单关闭");
    }

    order.setOrderStatus(OrderStatusEnum.CLOSED.getStatus());
    order.setCloseTime(LocalDateTime.now());
    orderMapper.updateById(order);
    log.info("订单已关闭: {}", orderId);

    // 发送通知
    notificationService.notifyUser(orderId, order.getUserId(), "关闭订单");

    if (OrderStatusEnum.PAID.getStatus().equals(order.getOrderStatus())) {
      // 已付款订单 自动退款
      log.info("订单已付款,自动退款: {}", orderId);

      OrderRefundReq req = new OrderRefundReq();
      req.setOrderId(orderId);
      req.setRefundReason("订单关闭");
      paymentService.refund(req, order);

      // 记录 订单退款表
      OrderRefund orderRefund = new OrderRefund();
      orderRefund.setOrderId(orderId);
      orderRefund.setRefundNo(OrderRefundCodeGenerator.generateCode());
      orderRefund.setRefundAmount(order.getPayAmount());
      orderRefund.setRefundReason("订单关闭");
      orderRefund.setRefundType(OrderRefundTypeEnum.ORDER_REFUND_TYPE_ONLY_REFUND.getType());
      orderRefund.setRefundStatus(OrderRefundStatusEnum.ORDER_REFUND_STATUS_REFUNDING.getStatus());
      orderRefundMapper.insert(orderRefund);
    }

    // 恢复库存
    // TODO inventoryService.unlockStock(order.getItems());

    // 添加操作日志
    orderLogManager.addOrderLog(
        order.getOrderId(), OrderLogActionEnum.SYSTEM_CLOSE_ORDER, "系统关闭订单");
  }

  /** 用户申请退款 */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void refundOrder(OrderRefundReq req, Long userId) {
    Long orderId = req.getOrderId();

    Order order = orderMapper.selectById(orderId);
    if (order == null) {
      BizException.throwEx("订单不存在");
    }
    if (!OrderStatusEnum.PAID.getStatus().equals(order.getOrderStatus())
        && !OrderStatusEnum.SHIPPED.getStatus().equals(order.getOrderStatus())) {
      BizException.throwEx("仅支持已付款,已发货订单申请退款");
    }
    if (!order.getUserId().equals(userId)) {
      BizException.throwEx("非法操作,订单不属于当前用户");
    }

    order.setOrderStatus(OrderStatusEnum.REFUNDING.getStatus());
    order.setCancelReason(req.getRefundReason());
    orderMapper.updateById(order);
    log.info("订单已退款: {}", orderId);

    // 恢复库存
    notificationService.notifyUser(orderId, userId, "用户申请退款");

    if (order.getOrderStatus().equals(OrderStatusEnum.PAID.getStatus())) {
      // 用户申请退款，订单状态为已付款时，直接发起退款
      paymentService.refund(req, order);

      // 记录 订单退款表
      OrderRefund orderRefund = new OrderRefund();
      orderRefund.setOrderId(orderId);
      orderRefund.setRefundNo(OrderRefundCodeGenerator.generateCode());
      orderRefund.setRefundAmount(order.getPayAmount());
      orderRefund.setRefundReason(req.getRefundReason());
      orderRefund.setRefundType(OrderRefundTypeEnum.ORDER_REFUND_TYPE_ONLY_REFUND.getType());
      orderRefund.setRefundStatus(OrderRefundStatusEnum.ORDER_REFUND_STATUS_REFUNDING.getStatus());
      orderRefund.setRefundReasonDesc("用户申请退款");
      orderRefundMapper.insert(orderRefund);
    }

    // 如果订单状态是已发货状态，需要买家退货卖家确认收货之后才发起退款

    // 添加操作日志
    orderLogManager.addOrderLog(order.getOrderId(), OrderLogActionEnum.USER_APPLY_REFUND, "用户申请退款");
  }

  /** 发货订单 */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void sendGoods(OrderSendGoodsReq req) {
    Long orderId = req.getOrderId();

    Order order = orderMapper.selectById(orderId);
    if (order == null) {
      BizException.throwEx("订单不存在");
    }
    if (!OrderStatusEnum.PAID.getStatus().equals(order.getOrderStatus())) {
      BizException.throwEx("仅支持已付款订单发货");
    }

    order.setOrderStatus(OrderStatusEnum.SHIPPED.getStatus());
    order.setShippingStatus(OrderShippingStatusEnum.SHIPPED.getShippingStatus());
    order.setShippingNo(req.getShippingNo());
    order.setShippingCompany(req.getShippingCompany());
    order.setSellerRemark(req.getSellerRemark());
    order.setShippingTime(LocalDateTime.now());
    orderMapper.updateById(order);

    log.info("订单已发货: {}", orderId);

    // 发送通知
    notificationService.notifyUser(orderId, order.getUserId(), "订单发货");

    // 添加操作日志
    orderLogManager.addOrderLog(order.getOrderId(), OrderLogActionEnum.WAREHOUSE_SEND_GOODS, "发货");
  }

  /** 收货订单 */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void receiveOrder(Long orderId, Long userId) {
    Order order = orderMapper.selectById(orderId);

    if (order == null) {
      BizException.throwEx("订单不存在");
    }
    if (!OrderStatusEnum.SHIPPED.getStatus().equals(order.getOrderStatus())) {
      BizException.throwEx("仅支持已发货订单确认收货");
    }
    if (!order.getUserId().equals(userId)) {
      BizException.throwEx("非法操作,订单不属于当前用户");
    }

    order.setOrderStatus(OrderStatusEnum.COMPLETED.getStatus());
    order.setReceiveTime(LocalDateTime.now());
    orderMapper.updateById(order);

    log.info("订单已收货: {}", orderId);

    // 发送通知
    notificationService.notifyUser(orderId, order.getUserId(), "订单收货");

    if (OrderStatusEnum.COMPLETED.getStatus().equals(order.getOrderStatus())) {
      // 发送积分
      integralService.sendIntegral(order.getUserId(), order.getOrderId(), order.getTotalAmount());
    }

    // 添加操作日志
    orderLogManager.addOrderLog(
        order.getOrderId(), OrderLogActionEnum.USER_CONFIRM_RECEIVE_ORDER, "用户确认收货");
  }

  /** 评价订单 */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void commentOrder(OrderCommentOrderReq req, Long userId) {
    Long orderId = req.getOrderId();

    Order order = orderMapper.selectById(orderId);

    if (order == null) {
      BizException.throwEx("订单不存在");
    }
    if (!OrderStatusEnum.COMPLETED.getStatus().equals(order.getOrderStatus())) {
      BizException.throwEx("仅支持已完成订单评价");
    }
    if (!order.getUserId().equals(userId)) {
      BizException.throwEx("非法操作,订单不属于当前用户");
    }

    OrderComment orderComment = new OrderComment();
    orderComment.setUserId(userId);
    orderComment.setOrderId(req.getOrderId());
    orderComment.setRating(req.getRating());
    orderComment.setCommentContent(req.getCommentContent());
    orderComment.setCommentImages(req.getCommentImages());
    orderComment.setIsAnonymous(req.getIsAnonymous() ? 1 : 0);
    orderCommentMapper.insert(orderComment);
    log.info("订单已评价: {}", orderId);

    notificationService.notifyUser(null, order.getUserId(), "订单已评价");
  }
}
