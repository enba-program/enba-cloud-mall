package com.enba.cloud.orders.order.service.impl;

import com.enba.boot.mq.MqHelper;
import com.enba.cloud.common.mq.MqConsts;
import com.enba.cloud.common.mq.payload.CreateOrderSuccessPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationService {

  @Autowired private MqHelper mqHelper;

  /**
   * 发送通知给用户
   *
   * @param orderId 订单ID
   * @param userId 用户ID
   * @param tag 通知内容
   */
  public void notifyUser(Long orderId, Long userId, String tag) {

    log.info("订单:{},tag:{}，发送通知给用户{}", orderId, tag, userId);
    // TODO 发送通知给用户
    // TODO 添加通知记录

    mqHelper.sendMessageByTopic(
        MqConsts.ORDER_TOPIC + ":" + tag,
        new CreateOrderSuccessPayload().setOrderNo(String.valueOf(orderId)).serialize(),
        String.valueOf(orderId),
        () -> "success");
  }
}
