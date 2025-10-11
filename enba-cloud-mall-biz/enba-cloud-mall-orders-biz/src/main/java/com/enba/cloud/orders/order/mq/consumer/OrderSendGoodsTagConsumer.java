package com.enba.cloud.orders.order.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.enba.cloud.common.mq.MqConsts;
import com.enba.cloud.common.mq.payload.CreateOrderSuccessPayload;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(
    topic = MqConsts.ORDER_TOPIC,
    selectorType = SelectorType.TAG, // 指定按 Tag 过滤
    selectorExpression = MqConsts.ORDER_SEND_GOODS_TAG, // 只消费带有指定tag的消息
    consumerGroup = "OrderSendGoodsTagConsumer-group")
public class OrderSendGoodsTagConsumer implements RocketMQListener<String> {

  /** 订单发货 */
  @Override
  public void onMessage(String message) {
    // 处理消息逻辑
    CreateOrderSuccessPayload createOrderSuccessPayload =
        JSONObject.parseObject(message, CreateOrderSuccessPayload.class);

    // 订单号
    String orderNo = createOrderSuccessPayload.getOrderNo();
  }
}
