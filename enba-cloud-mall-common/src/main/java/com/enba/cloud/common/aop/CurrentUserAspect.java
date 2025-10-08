package com.enba.cloud.common.aop;

import com.enba.boot.core.context.SecurityContextHolder;
import com.enba.cloud.common.annotation.CurrentUser;
import java.lang.reflect.Parameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CurrentUserAspect {

  @Around(
      "@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.PostMapping)")
  public Object resolveRequestHeaderParam(ProceedingJoinPoint joinPoint) throws Throwable {
    // 获取方法参数
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Parameter[] parameters = signature.getMethod().getParameters();

    // 遍历参数，检查是否有 @RequestHeaderParam 注解
    Object[] args = joinPoint.getArgs();
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];

      if (parameter.isAnnotationPresent(CurrentUser.class)
          && parameter.getType().equals(Long.class)) {

        // 替换方法参数
        args[i] = SecurityContextHolder.getUserId();
      }
    }

    return joinPoint.proceed(args);
  }
}
