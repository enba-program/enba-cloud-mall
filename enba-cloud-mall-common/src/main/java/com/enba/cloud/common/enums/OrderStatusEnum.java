package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
  // 订单状态(0-待付款,1-已付款,2-已发货,3-已完成,4-已取消,5-已关闭,6-退款中,7-已退款)

  WAIT_PAYMENT(0, "待付款", "unpaid"),
  PAID(1, "已付款", "unshipped"),
  SHIPPED(2, "已发货", "shipped"),
  COMPLETED(3, "已完成", "completed"),
  CANCELLED(4, "已取消", "cancelled"),
  CLOSED(5, "已关闭", "closed"),
  REFUNDING(6, "退款中", "refunding"),
  REFUNDED(7, "已退款", "refunded");

  private final Integer status;
  private final String name;
  private final String enName;

  OrderStatusEnum(Integer status, String name, String enName) {
    this.status = status;
    this.name = name;
    this.enName = enName;
  }

  public static OrderStatusEnum getByStatus(Integer status) {
    for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
      if (orderStatusEnum.getStatus().equals(status)) {
        return orderStatusEnum;
      }
    }
    return null;
  }

  public static OrderStatusEnum getByEnName(String enName) {
    for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
      if (orderStatusEnum.getEnName().equals(enName)) {
        return orderStatusEnum;
      }
    }
    return null;
  }
}
