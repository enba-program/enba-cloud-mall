package com.enba.cloud.orders.manager;

import com.enba.boot.core.context.SecurityContextHolder;
import com.enba.cloud.common.enums.OrderLogActionEnum;
import com.enba.cloud.common.enums.OrderLogUserTypeEnum;
import com.enba.cloud.common.utils.IpUtil;
import com.enba.cloud.orders.api.order.entity.OrderLog;
import com.enba.cloud.orders.order.mapper.OrderLogMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderLogManager {

  private final OrderLogMapper orderLogMapper;

  private OrderLogManager(OrderLogMapper orderLogMapper) {
    this.orderLogMapper = orderLogMapper;
  }

  /**
   * 添加操作日志
   *
   * @param orderId 订单ID
   * @param action 操作动作
   * @param actionNote 操作备注
   */
  public void addOrderLog(Long orderId, OrderLogActionEnum action, String actionNote) {
    OrderLog orderLog = new OrderLog();
    orderLog.setOrderId(orderId);
    orderLog.setUserId(SecurityContextHolder.getUserId());

    // FIXME 操作人员类型 先写死
    orderLog.setUserType(OrderLogUserTypeEnum.USER.getStatus());

    orderLog.setAction(action.getStatus());
    orderLog.setActionNote(actionNote);
    orderLog.setIpAddress(IpUtil.getClientIp());

    orderLogMapper.insert(orderLog);
  }
}
