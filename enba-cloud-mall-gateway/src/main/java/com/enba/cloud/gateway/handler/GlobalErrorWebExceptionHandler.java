package com.enba.cloud.gateway.handler;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Cloud Gateway 全局异常处理器
 */
@Component
@Order(-1) // 优先级高于默认的ErrorWebExceptionHandler
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final ApplicationContext applicationContext;

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                         WebProperties webProperties,
                                         ApplicationContext applicationContext,
                                         ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
        this.setMessageReaders(codecConfigurer.getReaders());
        this.applicationContext = applicationContext;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        // 所有异常都通过这个路由处理
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 渲染错误响应
     */
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        // 获取错误属性
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        
        // 提取状态码和异常信息
        HttpStatus status = getHttpStatus(errorAttributes);
        Throwable error = getError(request);
        
        // 构建统一响应体
        Map<String, Object> responseBody = buildResponseBody(status, error, errorAttributes);
        
        // 返回JSON格式的错误响应
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBody));
    }

    /**
     * 构建统一响应体
     */
    private Map<String, Object> buildResponseBody(HttpStatus status, Throwable error, Map<String, Object> errorAttributes) {
        Map<String, Object> body = new HashMap<>(4);
        
        // 基础响应信息
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", status.value());
        body.put("code", getErrorCode(status, error));
        body.put("message", getErrorMessage(status, error));
        
        // 开发环境可以添加详细信息
        if (isDevEnv()) {
            body.put("details", errorAttributes.get("message"));
            body.put("path", errorAttributes.get("path"));
            body.put("exception", error.getClass().getName());
        }
        
        return body;
    }

    /**
     * 获取HTTP状态码
     */
    private HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        Integer statusCode = (Integer) errorAttributes.get("status");
        return statusCode != null ? HttpStatus.valueOf(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 获取错误代码
     */
    private String getErrorCode(HttpStatus status, Throwable error) {
        // 可以根据不同异常类型返回不同的错误代码
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            return "GATEWAY_NOT_FOUND";
        } else if (error instanceof org.springframework.web.server.ResponseStatusException) {
            return "GATEWAY_RESPONSE_ERROR";
        } else if (error instanceof java.net.ConnectException) {
            return "SERVICE_CONNECT_FAILED";
        } else if (error instanceof java.util.concurrent.TimeoutException) {
            return "SERVICE_TIMEOUT";
        }
        return "GATEWAY_ERROR_" + status.value();
    }

    /**
     * 获取错误信息
     */
    private String getErrorMessage(HttpStatus status, Throwable error) {
        // 自定义不同异常的提示信息
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            return "请求的服务不存在";
        } else if (error instanceof java.net.ConnectException) {
            return "服务连接失败，请稍后再试";
        } else if (error instanceof java.util.concurrent.TimeoutException) {
            return "服务响应超时";
        }
        return status.getReasonPhrase();
    }

    /**
     * 判断是否为开发环境
     */
    private boolean isDevEnv() {
        // 实际项目中可以通过环境变量或配置获取
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("dev".equals(profile) || "development".equals(profile)) {
                return true;
            }
        }
        return false;
    }
}
