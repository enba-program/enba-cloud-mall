package com.enba.cloud.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD}) // 支持方法参数和对象属性
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {}
