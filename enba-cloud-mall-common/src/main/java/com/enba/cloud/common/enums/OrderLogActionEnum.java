package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderLogActionEnum {
  // 订单操作动作(0-用户下单,1-用户取消,2-用户支付,3-仓库发货,4-用户确认收货,5-系统关闭,6-用户申请退款,7-同意退款,8-拒绝退款，)

  USER_CREATE_ORDER(0, "用户下单"),
  USER_CANCEL_ORDER(1, "用户取消"),
  USER_PAY_ORDER(2, "用户支付"),
  WAREHOUSE_SEND_GOODS(3, "仓库发货"),
  USER_CONFIRM_RECEIVE_ORDER(4, "用户确认收货"),
  SYSTEM_CLOSE_ORDER(5, "系统关闭"),
  USER_APPLY_REFUND(6, "用户申请退款"),
  SYSTEM_AGREE_REFUND(7, "同意退款"),
  SYSTEM_REFUSE_REFUND(8, "拒绝退款"),
  ;

  private final Integer status;
  private final String name;

  OrderLogActionEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }

  public static OrderLogActionEnum getByStatus(Integer status) {
    for (OrderLogActionEnum orderStatusEnum : OrderLogActionEnum.values()) {
      if (orderStatusEnum.getStatus().equals(status)) {
        return orderStatusEnum;
      }
    }
    return null;
  }
}
