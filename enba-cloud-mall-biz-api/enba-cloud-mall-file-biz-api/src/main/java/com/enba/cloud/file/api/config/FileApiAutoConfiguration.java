package com.enba.cloud.file.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.enba.cloud.file.api")
@EnableFeignClients(basePackages = "com.enba.cloud.file.api.client")
public class FileApiAutoConfiguration {
  public FileApiAutoConfiguration() {
    System.out.println("FileApiAutoConfiguration");
  }
}
