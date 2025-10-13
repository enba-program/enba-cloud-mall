package com.enba.cloud.orders.order.mq.consumer;

import com.enba.cloud.common.mq.MqConsts;
import com.enba.cloud.common.mq.payload.CreateOrderSuccessPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(
    topic = MqConsts.ORDER_TOPIC,
    selectorType = SelectorType.TAG, // 指定按 Tag 过滤
    selectorExpression = MqConsts.ORDER_RECEIVE_TAG, // 只消费带有指定tag的消息
    consumerGroup = "OrderReceiveTagConsumer-group")
@Slf4j
public class OrderReceiveTagConsumer extends AbstractBaseConsumer {

  /** 订单收货 */
  @Override
  protected boolean canHandle(MessageExt messageExt) {
    return false;
  }

  @Override
  protected void handleMessage(CreateOrderSuccessPayload createOrderSuccessPayload) {}
}
