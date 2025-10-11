package com.enba.cloud.common.mq;

// 消息相关常量
public interface MqConsts {

  // 订单类型（不同消息类型对应不同的主题topic）
  String ORDER_TOPIC = "order-topic";

  // 订单类型-用户下单 tag
  String ORDER_CREATE_TAG = "order-create-tag";

  // 订单类型-支付成功 tag
  String ORDER_PAY_SUCCESS_TAG = "order-pay-success-tag";

  // 取消订单
  String ORDER_CANCEL_TAG = "order-cancel-tag";

  // 关闭订单
  String ORDER_CLOSE_TAG = "order-close-tag";

  // 用户申请退款
  String ORDER_REFUND_TAG = "order-refund-tag";

  // 订单发货
  String ORDER_SEND_GOODS_TAG = "order-send-goods-tag";

  // 订单收货
  String ORDER_RECEIVE_TAG = "order-receive-tag";

  // 订单评价
  String ORDER_COMMENT_TAG = "order-comment-tag";
}
