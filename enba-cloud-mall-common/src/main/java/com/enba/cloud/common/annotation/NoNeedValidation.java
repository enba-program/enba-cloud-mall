package com.enba.cloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 有此注解，接口方法不需要校验token */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NoNeedValidation {}
