package com.enba.cloud.shopping.api.factory;

import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.shopping.api.client.ShoppingClient;
import com.enba.cloud.shopping.api.req.AddToCartRequest;
import com.enba.cloud.shopping.api.req.DeleteCartItemsRequest;
import com.enba.cloud.shopping.api.req.SelectAllRequest;
import com.enba.cloud.shopping.api.req.UpdateQuantityRequest;
import com.enba.cloud.shopping.api.resp.ShoppingCartResp.ShoppingCartItemResp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ShoppingFallbackFactory implements FallbackFactory<ShoppingClient> {

  private static final Logger log = LoggerFactory.getLogger(ShoppingFallbackFactory.class);

  @Override
  public ShoppingClient create(Throwable cause) {
    return new ShoppingClient() {
      @Override
      public Result<List<ShoppingCartItemResp>> getCartList(Long userId) {
        log.error("ShoppingClient getCartList 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "ShoppingClient getCartList error");
      }

      @Override
      public Result<Boolean> addToCart(AddToCartRequest request, Long userId) {
        log.error("ShoppingClient addToCart 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "ShoppingClient addToCart error");
      }

      @Override
      public Result<Boolean> updateCartItemQuantity(UpdateQuantityRequest request, Long userId) {
        log.error("ShoppingClient updateCartItemQuantity 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "ShoppingClient updateCartItemQuantity error");
      }

      @Override
      public Result<Boolean> deleteCartItems(DeleteCartItemsRequest request, Long userId) {
        log.error("ShoppingClient deleteCartItems 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "ShoppingClient deleteCartItems error");
      }

      @Override
      public Result<Boolean> selectOrUnselectAll(SelectAllRequest request, Long userId) {
        log.error("ShoppingClient selectOrUnselectAll 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "ShoppingClient selectOrUnselectAll error");
      }
    };
  }
}
