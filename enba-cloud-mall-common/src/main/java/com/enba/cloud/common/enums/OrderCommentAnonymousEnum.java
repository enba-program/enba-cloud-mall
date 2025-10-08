package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum OrderCommentAnonymousEnum {
  // 是否匿名(0-否,1-是)
  NO_ANONYMOUS(0, "否"),
  YES_ANONYMOUS(1, "是");

  private final Integer anonymous;
  private final String name;

  public static OrderCommentAnonymousEnum getByAnonymous(Integer anonymous) {
    for (OrderCommentAnonymousEnum orderCommentAnonymousEnum : OrderCommentAnonymousEnum.values()) {
      if (orderCommentAnonymousEnum.getAnonymous().equals(anonymous)) {
        return orderCommentAnonymousEnum;
      }
    }
    return null;
  }

  OrderCommentAnonymousEnum(Integer anonymous, String name) {
    this.anonymous = anonymous;
    this.name = name;
  }
}
