package com.enba.cloud.shopping.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.enba.cloud.shopping.api.entity.ShoppingCart;
import com.enba.cloud.shopping.api.entity.ShoppingCartItem;
import com.enba.cloud.shopping.api.resp.ShoppingCartResp.ShoppingCartItemResp;
import java.math.BigDecimal;
import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

  /** 获取用户购物车列表 */
  List<ShoppingCartItemResp> getCartList(Long userId);

  /** 添加商品到购物车 */
  Boolean addToCart(Long userId, Long productId, Long skuId, Integer quantity, String specs);

  /** 更新购物车商品数量 */
  Boolean updateCartItemQuantity(Long userId, Long itemId, Integer quantity);

  /** 删除购物车商品 */
  Boolean deleteCartItems(Long userId, List<Long> itemIds);

  /** 全选/取消全选购物车商品 */
  Boolean selectOrUnselectAll(Long userId, boolean selected);

  /** 计算购物车总价 */
  BigDecimal calculateTotalPrice(List<ShoppingCartItem> cartItems);
}
