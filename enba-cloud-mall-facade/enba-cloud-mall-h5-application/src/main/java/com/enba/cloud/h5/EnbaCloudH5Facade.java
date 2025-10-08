package com.enba.cloud.h5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EnbaCloudH5Facade {

  public static void main(String[] args) {
    SpringApplication.run(EnbaCloudH5Facade.class, args);
  }
}
