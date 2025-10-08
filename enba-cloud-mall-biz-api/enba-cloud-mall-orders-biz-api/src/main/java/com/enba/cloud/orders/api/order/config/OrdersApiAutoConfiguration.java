package com.enba.cloud.orders.api.order.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.enba.cloud.orders.api")
@EnableFeignClients(basePackages = "com.enba.cloud.orders.api.order.client")
public class OrdersApiAutoConfiguration {}
