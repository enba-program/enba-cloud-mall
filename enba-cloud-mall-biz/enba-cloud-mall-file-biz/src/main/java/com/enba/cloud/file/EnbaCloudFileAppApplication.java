package com.enba.cloud.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan({"com.enba.cloud.file.mapper"})
@SpringBootApplication
@EnableDiscoveryClient
public class EnbaCloudFileAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnbaCloudFileAppApplication.class, args);
  }
}
