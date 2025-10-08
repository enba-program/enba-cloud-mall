package com.enba.cloud.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan({"com.enba.cloud.goods.**.mapper"})
@SpringBootApplication
@EnableDiscoveryClient
public class EnbaCloudGoodsAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnbaCloudGoodsAppApplication.class, args);
  }
}
