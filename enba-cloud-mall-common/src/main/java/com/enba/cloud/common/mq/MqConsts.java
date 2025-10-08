package com.enba.cloud.common.mq;

// 消息相关常量
public interface MqConsts {

  // 订单类型（不同消息类型对应不同的主题topic）
  String ORDER_TOPIC = "order-topic";

  // 订单类型-用户下单 tag
  String ORDER_CREATE_TAG = "order-create-tag";

  // 订单类型-支付成功 tag
  String ORDER_PAY_SUCCESS_TAG = "order-pay-success-tag";
}
