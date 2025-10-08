package com.enba.cloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

  // 锁的key
  String key() default "";

  // 锁过期时间
  long expireTime() default 30; // 默认锁过期时间30秒

  // 提示信息
  String tips() default "重复提交";
}
