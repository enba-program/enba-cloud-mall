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
    selectorExpression = MqConsts.ORDER_CLOSE_TAG, // 只消费带有指定tag的消息
    consumerGroup = "OrderCloseTagConsumer-group")
@Slf4j
public class OrderCloseTagConsumer extends AbstractBaseConsumer {

  /** 关闭订单 */
  @Override
  protected boolean canHandle(MessageExt messageExt) {
    return false;
  }

  @Override
  protected void handleMessage(CreateOrderSuccessPayload createOrderSuccessPayload) {

  }
}
