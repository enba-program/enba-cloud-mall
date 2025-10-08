package com.enba.cloud.shopping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan({"com.enba.cloud.shopping.**.mapper"})
@EnableDiscoveryClient
@SpringBootApplication
public class EnbaCloudShoppingAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnbaCloudShoppingAppApplication.class, args);
  }
}
