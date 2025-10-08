package com.enba.cloud.goods.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.enba.cloud.goods.api")
@EnableFeignClients(basePackages = "com.enba.cloud.goods.api")
public class GoodsApiAutoConfiguration {}
