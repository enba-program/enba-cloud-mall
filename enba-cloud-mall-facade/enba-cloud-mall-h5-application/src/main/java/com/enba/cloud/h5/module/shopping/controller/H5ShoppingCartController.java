package com.enba.cloud.h5.module.shopping.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.shopping.api.client.ShoppingClient;
import com.enba.cloud.shopping.api.req.AddToCartRequest;
import com.enba.cloud.shopping.api.req.DeleteCartItemsRequest;
import com.enba.cloud.shopping.api.req.SelectAllRequest;
import com.enba.cloud.shopping.api.req.UpdateQuantityRequest;
import com.enba.cloud.shopping.api.resp.ShoppingCartResp.ShoppingCartItemResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "购物车管理")
@RestController
@RequestMapping("/api/cart")
public class H5ShoppingCartController {

  @Autowired private ShoppingClient shoppingClient;

  /** 获取购物车列表 */
  @GetMapping("/list")
  @ApiOperation(value = "获取购物车列表")
  public Result<List<ShoppingCartItemResp>> getCartList(@CurrentUser Long userId) {
    return shoppingClient.getCartList(userId);
  }

  /** 添加商品到购物车 */
  @PostMapping("/add")
  @ApiOperation(value = "添加商品到购物车")
  @DistributedLock
  public Result<Boolean> addToCart(
      @RequestBody @Validated AddToCartRequest request, @CurrentUser Long userId) {
    return shoppingClient.addToCart(request, userId);
  }

  /** 更新购物车商品数量 */
  @PutMapping("/update-quantity")
  @ApiOperation(value = "更新购物车商品数量")
  @DistributedLock
  public Result<Boolean> updateCartItemQuantity(
      @RequestBody @Validated UpdateQuantityRequest request, @CurrentUser Long userId) {
    return shoppingClient.updateCartItemQuantity(request, userId);
  }

  /** 删除购物车商品 */
  @DeleteMapping("/delete")
  @ApiOperation(value = "删除购物车商品")
  @DistributedLock
  public Result<Boolean> deleteCartItems(
      @RequestBody @Validated DeleteCartItemsRequest request, @CurrentUser Long userId) {
    return shoppingClient.deleteCartItems(request, userId);
  }

  /** 全选/取消全选购物车商品 */
  @PutMapping("/select-all")
  @ApiOperation(value = "全选/取消全选购物车商品")
  public Result<Boolean> selectOrUnselectAll(
      @RequestBody @Validated SelectAllRequest request, @CurrentUser Long userId) {
    return shoppingClient.selectOrUnselectAll(request, userId);
  }
}
