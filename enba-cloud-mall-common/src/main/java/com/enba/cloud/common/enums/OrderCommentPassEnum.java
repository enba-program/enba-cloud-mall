package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderCommentPassEnum {
  // 是否审核通过(0-待审核,1-通过,2-拒绝)

  WAIT_AUDIT(0, "待审核"),
  PASS(1, "通过"),
  REFUSE(2, "拒绝");

  private final Integer code;
  private final String message;

  OrderCommentPassEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public static String getMessageByCode(Integer code) {
    for (OrderCommentPassEnum value : OrderCommentPassEnum.values()) {
      if (value.getCode().equals(code)) {
        return value.getMessage();
      }
    }
    return null;
  }
}
