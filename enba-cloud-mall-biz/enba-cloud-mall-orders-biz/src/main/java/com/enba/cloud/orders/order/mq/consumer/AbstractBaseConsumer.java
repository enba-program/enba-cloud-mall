package com.enba.cloud.orders.order.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.enba.cloud.common.mq.payload.CreateOrderSuccessPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;

@Slf4j
public abstract class AbstractBaseConsumer implements RocketMQListener<MessageExt> {

  @Override
  public void onMessage(MessageExt messageExt) {
    // 获取消息key
    String messageKey = messageExt.getKeys();
    // 获取消息体（字节数组转字符串）
    String messageBody = new String(messageExt.getBody());
    log.info("接收到消息，messageKey: {}, messageBody: {}", messageKey, messageBody);

    // 处理消息逻辑
    if (canHandle(messageExt)) {
      handleMessage(JSONObject.parseObject(messageBody, CreateOrderSuccessPayload.class));
    } else {
      //  忽略此消息，消息已被处理
      log.info("忽略此消息，消息已被处理，messageKey: {}, messageBody: {}", messageKey, messageBody);
    }
  }

  /**
   * 判断是否可以处理此消息
   *
   * @param messageExt 消息
   * @return 是否可以处理此消息 true：可以处理此消息,调用方法handleMessage false：忽略此消息，消息已被处理
   */
  protected abstract boolean canHandle(MessageExt messageExt);

  /**
   * 处理消息逻辑
   *
   * @param createOrderSuccessPayload 创建订单成功消息体
   */
  protected abstract void handleMessage(CreateOrderSuccessPayload createOrderSuccessPayload);
}
