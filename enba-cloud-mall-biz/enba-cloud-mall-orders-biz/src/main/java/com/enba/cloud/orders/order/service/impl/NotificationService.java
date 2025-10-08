package com.enba.cloud.orders.order.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationService {

  /**
   * 发送通知给用户
   *
   * @param orderId 订单ID
   * @param userId 用户ID
   * @param message 通知内容
   */
  public void notifyUser(Long orderId, Long userId, String message) {

    log.info("订单{}已{}，发送通知给用户{}", orderId, message, userId);
    // TODO 发送通知给用户
    // TODO 添加通知记录
  }
}
