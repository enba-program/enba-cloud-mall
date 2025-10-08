package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum SpuAuditStatusEnum {
  WAIT_AUDIT(0, "待审核"),
  AUDIT_PASS(1, "审核通过"),
  AUDIT_FAIL(2, "审核不通过"),
  DRAFT(3, "草稿"),
  ;

  private final Integer auditStatus;
  private final String name;

  public static SpuAuditStatusEnum getByAuditStatus(Integer auditStatus) {
    for (SpuAuditStatusEnum value : values()) {
      if (value.auditStatus.equals(auditStatus)) {
        return value;
      }
    }
    return null;
  }

  private SpuAuditStatusEnum(Integer auditStatus, String name) {
    this.auditStatus = auditStatus;
    this.name = name;
  }
}
