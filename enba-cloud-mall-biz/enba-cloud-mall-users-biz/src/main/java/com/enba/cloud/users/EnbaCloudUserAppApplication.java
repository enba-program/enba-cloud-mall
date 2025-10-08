package com.enba.cloud.users;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan({"com.enba.cloud.users.**.mapper"})
@EnableDiscoveryClient
@SpringBootApplication
public class EnbaCloudUserAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnbaCloudUserAppApplication.class, args);
  }
}
