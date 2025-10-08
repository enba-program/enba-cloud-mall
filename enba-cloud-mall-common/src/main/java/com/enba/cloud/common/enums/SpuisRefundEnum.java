package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum SpuisRefundEnum {
  NO_REFUND(0, "不可退换"),
  REFUND(1, "可退换"),
  ;

  private final Integer refund;
  private final String name;

  private SpuisRefundEnum(Integer refund, String name) {
    this.refund = refund;
    this.name = name;
  }

  public static SpuisRefundEnum getByRefund(Integer refund) {
    for (SpuisRefundEnum value : values()) {
      if (value.refund.equals(refund)) {
        return value;
      }
    }
    return null;
  }
}
