package com.enba.cloud.orders.order.service.impl;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IntegralService {

  /**
   * 发放积分
   *
   * @param userId 用户ID
   * @param totalAmount 订单总金额
   */
  public void sendIntegral(Long userId, Long orderId, BigDecimal totalAmount) {
    log.info("发放积分: userId={}, orderId={}, totalAmount={}", userId, orderId, totalAmount);
    log.info("添加积分记录: userId={}, orderId={}, totalAmount={}", userId, orderId, totalAmount);
  }
}
