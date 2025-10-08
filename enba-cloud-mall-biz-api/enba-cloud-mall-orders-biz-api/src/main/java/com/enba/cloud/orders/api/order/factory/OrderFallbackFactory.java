package com.enba.cloud.orders.api.order.factory;

import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.orders.api.order.client.OrderClient;
import com.enba.cloud.orders.api.order.req.OrderCancelReq;
import com.enba.cloud.orders.api.order.req.OrderCommentOrderReq;
import com.enba.cloud.orders.api.order.req.OrderCreateReq;
import com.enba.cloud.orders.api.order.req.OrderPayReq;
import com.enba.cloud.orders.api.order.req.OrderRefundReq;
import com.enba.cloud.orders.api.order.resp.CallbackResult;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderFallbackFactory implements FallbackFactory<OrderClient> {

  private static final Logger log = LoggerFactory.getLogger(OrderFallbackFactory.class);

  @Override
  public OrderClient create(Throwable cause) {
    return new OrderClient() {
      @Override
      public Result<String> createOrder(OrderCreateReq orderCreateReq, Long userId) {
        log.error("OrderClient createOrder 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "OrderClient createOrder error");
      }

      @Override
      public void payOrder(OrderPayReq req) {
        log.error("OrderClient payOrder 调用异常: {} ", cause.getMessage(), cause);
      }

      @Override
      public ResponseEntity<CallbackResult> payCallback(HttpServletRequest request)
          throws IOException {
        log.error("OrderClient payCallback 调用异常: {} ", cause.getMessage(), cause);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CallbackResult.err());
      }

      @Override
      public Result<Boolean> cancelOrder(OrderCancelReq req, Long userId) {
        log.error("OrderClient cancelOrder 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "OrderClient cancelOrder error");
      }

      @Override
      public Result<Boolean> closeOrder(Long orderId, Long userId) {
        log.error("OrderClient closeOrder 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "OrderClient closeOrder error");
      }

      @Override
      public Result<Boolean> refundOrder(OrderRefundReq req, Long userId) {
        log.error("OrderClient refundOrder 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "OrderClient refundOrder error");
      }

      @Override
      public Result<Boolean> receiveOrder(Long orderId, Long userId) {
        log.error("OrderClient receiveOrder 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "OrderClient receiveOrder error");
      }

      @Override
      public Result<Boolean> commentOrder(OrderCommentOrderReq req, Long userId) {
        log.error("OrderClient commentOrder 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "OrderClient commentOrder error");
      }
    };
  }
}
