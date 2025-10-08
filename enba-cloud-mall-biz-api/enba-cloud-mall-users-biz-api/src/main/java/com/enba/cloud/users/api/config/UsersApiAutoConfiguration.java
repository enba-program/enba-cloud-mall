package com.enba.cloud.users.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.enba.cloud.users.api")
@EnableFeignClients(basePackages = "com.enba.cloud.users.api.client")
public class UsersApiAutoConfiguration {}
