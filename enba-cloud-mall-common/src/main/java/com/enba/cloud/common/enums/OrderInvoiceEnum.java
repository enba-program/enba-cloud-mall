package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderInvoiceEnum {
  // 发票类型(0-不开发票,1-普通发票,2-增值税发票)
  NO_INVOICE(0, "不开发票"),
  NORMAL_INVOICE(1, "普通发票"),
  VAT_INVOICE(2, "增值税发票");

  private final Integer invoiceType;
  private final String name;

  public static OrderInvoiceEnum getByInvoiceType(Integer invoiceType) {
    for (OrderInvoiceEnum invoiceEnum : values()) {
      if (invoiceEnum.getInvoiceType().equals(invoiceType)) {
        return invoiceEnum;
      }
    }
    return null;
  }

  OrderInvoiceEnum(Integer invoiceType, String name) {
    this.invoiceType = invoiceType;
    this.name = name;
  }
}
