package com.enba.cloud.shopping.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.enba.cloud.shopping.api")
@EnableFeignClients(basePackages = "com.enba.cloud.shopping.api.client")
public class ShoppingApiAutoConfiguration {}
