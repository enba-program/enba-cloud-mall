package com.enba.cloud.shopping.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.shopping.api.client.ShoppingClient;
import com.enba.cloud.shopping.api.req.AddToCartRequest;
import com.enba.cloud.shopping.api.req.DeleteCartItemsRequest;
import com.enba.cloud.shopping.api.req.SelectAllRequest;
import com.enba.cloud.shopping.api.req.UpdateQuantityRequest;
import com.enba.cloud.shopping.api.resp.ShoppingCartResp.ShoppingCartItemResp;
import com.enba.cloud.shopping.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "购物车管理")
@RestController
public class ShoppingCartController implements ShoppingClient {

  private final ShoppingCartService shoppingCartService;

  public ShoppingCartController(ShoppingCartService shoppingCartService) {
    this.shoppingCartService = shoppingCartService;
  }

  /** 获取购物车列表 */
  @Override
  @ApiOperation(value = "获取购物车列表")
  public Result<List<ShoppingCartItemResp>> getCartList(@CurrentUser Long userId) {
    return Result.success(shoppingCartService.getCartList(userId));
  }

  /** 添加商品到购物车 */
  @Override
  @ApiOperation(value = "添加商品到购物车")
  @DistributedLock
  public Result<Boolean> addToCart(
      @RequestBody @Validated AddToCartRequest request, @CurrentUser Long userId) {
    return Result.success(
        shoppingCartService.addToCart(
            userId,
            request.getSpuId(),
            request.getSkuId(),
            request.getQuantity(),
            request.getSkuSpecEnums()));
  }

  /** 更新购物车商品数量 */
  @Override
  @ApiOperation(value = "更新购物车商品数量")
  @DistributedLock
  public Result<Boolean> updateCartItemQuantity(
      @RequestBody @Validated UpdateQuantityRequest request, @CurrentUser Long userId) {
    return Result.success(
        shoppingCartService.updateCartItemQuantity(
            userId, request.getItemId(), request.getQuantity()));
  }

  /** 删除购物车商品 */
  @Override
  @ApiOperation(value = "删除购物车商品")
  @DistributedLock
  public Result<Boolean> deleteCartItems(
      @RequestBody @Validated DeleteCartItemsRequest request, @CurrentUser Long userId) {
    return Result.success(shoppingCartService.deleteCartItems(userId, request.getItemIds()));
  }

  /** 全选/取消全选购物车商品 */
  @Override
  @ApiOperation(value = "全选/取消全选购物车商品")
  public Result<Boolean> selectOrUnselectAll(
      @RequestBody @Validated SelectAllRequest request, @CurrentUser Long userId) {
    return Result.success(shoppingCartService.selectOrUnselectAll(userId, request.isSelected()));
  }
}
