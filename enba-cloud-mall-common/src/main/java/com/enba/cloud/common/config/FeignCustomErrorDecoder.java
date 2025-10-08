package com.enba.cloud.common.config;

import com.enba.boot.core.base.Result;
import feign.Response;
import feign.codec.ErrorDecoder;
import javax.naming.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class FeignCustomErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    if (response.status() == 503) {
      // 服务不可用特殊处理
      return new ServiceUnavailableException("服务暂不可用，请稍后再试");
    }

    return new RuntimeException("openfeign err");

    // 其他错误处理...
    //        try {
    //            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
    //            ErrorResponse errorResponse = new ObjectMapper().readValue(body,
    // ErrorResponse.class);
    //            return new BusinessException(errorResponse.getCode(), errorResponse.getMessage());
    //        } catch (IOException e) {
    //            return FeignException.errorStatus(methodKey, response);
    //        }
  }
}
