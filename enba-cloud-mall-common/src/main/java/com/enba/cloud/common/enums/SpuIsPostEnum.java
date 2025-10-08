package com.enba.cloud.common.enums;

import lombok.Getter;

@Getter
public enum SpuIsPostEnum {
  NO_POST(0, "不包邮"),
  POST(1, "包邮"),
  ;

  private final Integer isPost;
  private final String name;

  public static SpuIsPostEnum getByIsPost(Integer isPost) {
    for (SpuIsPostEnum value : SpuIsPostEnum.values()) {
      if (value.isPost.equals(isPost)) {
        return value;
      }
    }
    return null;
  }

  private SpuIsPostEnum(Integer isPost, String name) {
    this.isPost = isPost;
    this.name = name;
  }
}
