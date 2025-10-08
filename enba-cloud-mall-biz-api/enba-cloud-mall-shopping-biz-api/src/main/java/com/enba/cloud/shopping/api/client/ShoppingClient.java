package com.enba.cloud.shopping.api.client;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.shopping.api.factory.ShoppingFallbackFactory;
import com.enba.cloud.shopping.api.req.AddToCartRequest;
import com.enba.cloud.shopping.api.req.DeleteCartItemsRequest;
import com.enba.cloud.shopping.api.req.SelectAllRequest;
import com.enba.cloud.shopping.api.req.UpdateQuantityRequest;
import com.enba.cloud.shopping.api.resp.ShoppingCartResp.ShoppingCartItemResp;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    contextId = "ShoppingClient",
    name = ServiceAppConstants.SHOPPING_SERVICE_NAME,
    fallbackFactory = ShoppingFallbackFactory.class)
@ResponseBody
public interface ShoppingClient {
  /** 获取购物车列表 */
  @GetMapping("/api/cart/list")
  @ApiOperation(value = "获取购物车列表")
  Result<List<ShoppingCartItemResp>> getCartList(@RequestParam(name = "userId") Long userId);

  /** 添加商品到购物车 */
  @PostMapping("/api/cart/add")
  @ApiOperation(value = "添加商品到购物车")
  @DistributedLock
  Result<Boolean> addToCart(
      @RequestBody @Validated AddToCartRequest request, @RequestParam(name = "userId") Long userId);

  /** 更新购物车商品数量 */
  @PutMapping("/api/cart/update-quantity")
  @ApiOperation(value = "更新购物车商品数量")
  @DistributedLock
  Result<Boolean> updateCartItemQuantity(
      @RequestBody @Validated UpdateQuantityRequest request, @RequestParam(name = "userId") Long userId);

  /** 删除购物车商品 */
  @DeleteMapping("/api/cart/delete")
  @ApiOperation(value = "删除购物车商品")
  @DistributedLock
  Result<Boolean> deleteCartItems(
      @RequestBody @Validated DeleteCartItemsRequest request, @RequestParam(name = "userId") Long userId);

  /** 全选/取消全选购物车商品 */
  @PutMapping("/api/cart/select-all")
  @ApiOperation(value = "全选/取消全选购物车商品")
  Result<Boolean> selectOrUnselectAll(
      @RequestBody @Validated SelectAllRequest request, @RequestParam(name = "userId") Long userId);
}
