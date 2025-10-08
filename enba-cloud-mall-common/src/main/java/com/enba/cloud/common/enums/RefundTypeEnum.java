package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum RefundTypeEnum {
  // 退款类型(0-仅退款,1-退货退款)
  ONLY_REFUND(0, "仅退款"),
  RETURN_REFUND(1, "退货退款");

  private final Integer refundType;
  private final String name;

  RefundTypeEnum(Integer refundType, String name) {
    this.refundType = refundType;
    this.name = name;
  }

  public static RefundTypeEnum getByRefundType(Integer refundType) {
    for (RefundTypeEnum refundTypeEnum : RefundTypeEnum.values()) {
      if (refundTypeEnum.getRefundType().equals(refundType)) {
        return refundTypeEnum;
      }
    }
    return null;
  }
}
