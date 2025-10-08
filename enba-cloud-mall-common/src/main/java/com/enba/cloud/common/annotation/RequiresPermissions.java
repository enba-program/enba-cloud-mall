package com.enba.cloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequiresPermissions {

  /** 权限码 */
  String[] value() default {};

  /** 验证模式：AND | OR，默认AND */
  Logical logical() default Logical.AND;

  enum Logical {
    /** 必须满足所有权限 */
    AND,

    /** 满足任意权限 */
    OR
  }
}
