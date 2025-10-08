package com.enba.cloud.gateway.controller;

import com.enba.boot.core.base.Result;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HealthController {

  @Value("${spring.profiles.active}")
  private String environment;

  @Value("${spring.application.name}")
  private String name;

  /** 请求次数 */
  private AtomicInteger ai = new AtomicInteger(0);

  @GetMapping("/index")
  public Result<Map<String, Object>> index() {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("message", name + " ready!");
    resultMap.put("environment", environment);
    resultMap.put("count", ai.incrementAndGet());
    return Result.success(resultMap);
  }
}
