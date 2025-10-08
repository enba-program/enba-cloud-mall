package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum InvoiceStatusEnum {
  // 发票状态(0-未开票,1-已开票)
  WAIT_INVOICE(0, "未开票"),
  INVOICED(1, "已开票");
  private final Integer status;
  private final String name;

  InvoiceStatusEnum(Integer status, String name) {
    this.status = status;
    this.name = name;
  }

  public static InvoiceStatusEnum getByStatus(Integer status) {
    for (InvoiceStatusEnum invoiceStatusEnum : InvoiceStatusEnum.values()) {
      if (invoiceStatusEnum.getStatus().equals(status)) {
        return invoiceStatusEnum;
      }
    }

    return null;
  }
}
