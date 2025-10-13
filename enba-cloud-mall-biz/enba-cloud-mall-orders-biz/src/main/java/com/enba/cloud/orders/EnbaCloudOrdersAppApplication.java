package com.enba.cloud.orders;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan({"com.enba.cloud.orders.**.mapper"})
@EnableDiscoveryClient
@SpringBootApplication
public class EnbaCloudOrdersAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnbaCloudOrdersAppApplication.class, args);
  }
}
