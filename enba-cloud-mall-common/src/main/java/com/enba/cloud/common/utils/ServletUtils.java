package com.enba.cloud.common.utils;

import com.alibaba.fastjson.JSON;
import com.enba.boot.core.base.Result;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

public class ServletUtils {

  /**
   * 设置webflux模型响应
   *
   * @param response ServerHttpResponse
   * @param value 响应内容
   * @return Mono<Void>
   */
  public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value) {
    return webFluxResponseWriter(response, HttpStatus.OK, value, 500);
  }

  /**
   * 设置webflux模型响应
   *
   * @param response ServerHttpResponse
   * @param code 响应状态码
   * @param value 响应内容
   * @return Mono<Void>
   */
  public static Mono<Void> webFluxResponseWriter(
      ServerHttpResponse response, Object value, int code) {
    return webFluxResponseWriter(response, HttpStatus.UNAUTHORIZED, value, code);
  }

  /**
   * 设置webflux模型响应
   *
   * @param response ServerHttpResponse
   * @param status http状态码
   * @param code 响应状态码
   * @param value 响应内容
   * @return Mono<Void>
   */
  public static Mono<Void> webFluxResponseWriter(
      ServerHttpResponse response, HttpStatus status, Object value, int code) {
    return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
  }

  /**
   * 设置webflux模型响应
   *
   * @param response ServerHttpResponse
   * @param contentType content-type
   * @param status http状态码
   * @param code 响应状态码
   * @param value 响应内容
   * @return Mono<Void>
   */
  public static Mono<Void> webFluxResponseWriter(
      ServerHttpResponse response, String contentType, HttpStatus status, Object value, int code) {
    response.setStatusCode(status);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);

    DataBuffer dataBuffer =
        response.bufferFactory().wrap(JSON.toJSONBytes(Result.err(code, value.toString())));
    return response.writeWith(Mono.just(dataBuffer));
  }

  public static String getHeader(HttpServletRequest request, String name) {
    String value = request.getHeader(name);
    if (StringUtils.isEmpty(value)) {
      return "";
    }
    return urlDecode(value);
  }

  /**
   * 内容解码
   *
   * @param str 内容
   * @return 解码后的内容
   */
  public static String urlDecode(String str) {
    try {
      return URLDecoder.decode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return "";
    }
  }
}
