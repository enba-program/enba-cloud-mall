package com.enba.cloud.shopping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.cloud.goods.api.sku.client.SkuClient;
import com.enba.cloud.goods.api.sku.entity.Sku;
import com.enba.cloud.shopping.api.entity.ShoppingCart;
import com.enba.cloud.shopping.api.entity.ShoppingCartItem;
import com.enba.cloud.shopping.mapper.ShoppingCartItemMapper;
import com.enba.cloud.shopping.mapper.ShoppingCartMapper;
import com.enba.cloud.shopping.api.resp.ShoppingCartResp.ShoppingCartItemResp;
import com.enba.cloud.shopping.service.ShoppingCartService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService {

  private final ShoppingCartMapper shoppingCartMapper;
  private final ShoppingCartItemMapper shoppingCartItemMapper;
  private final SkuClient skuClient;

  @Override
  public List<ShoppingCartItemResp> getCartList(Long userId) {
    // 查询用户的购物车(如果不存在则创建)
    ShoppingCart cart = getOrCreateUserCart(userId);

    // 查询购物车项
    List<ShoppingCartItem> cartItems = shoppingCartItemMapper.selectByCartId(cart.getCartId());

    // 计算总价
    BigDecimal totalPrice = calculateTotalPrice(cartItems);

    // 更新购物车主表信息
    cart.setTotalPrice(totalPrice);
    updateById(cart);

    return BeanUtil.copyToList(cartItems, ShoppingCartItemResp.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean addToCart(
      Long userId, Long productId, Long skuId, Integer quantity, String specs) {
    // 查询用户的购物车(如果不存在则创建)
    ShoppingCart cart = getOrCreateUserCart(userId);

    // 检查商品是否已在购物车中
    ShoppingCartItem existingItem =
        shoppingCartItemMapper.selectOne(
            Wrappers.<ShoppingCartItem>lambdaQuery()
                .eq(ShoppingCartItem::getCartId, cart.getCartId())
                .eq(ShoppingCartItem::getProductId, productId)
                .eq(ShoppingCartItem::getSkuId, skuId));

    if (existingItem != null) {
      // 如果已存在，更新数量
      int newQuantity = existingItem.getQuantity() + quantity;
      return shoppingCartItemMapper.updateItemQuantity(existingItem.getItemId(), newQuantity) > 0;
    } else {
      // 如果不存在，创建新项
      ShoppingCartItem newItem = new ShoppingCartItem();
      newItem.setCartId(cart.getCartId());
      newItem.setProductId(productId);
      newItem.setSkuId(skuId);
      newItem.setQuantity(quantity);
      newItem.setIsSelected(1); // 默认选中

      Sku sku = skuClient.findOne(skuId).getData();

      newItem.setPrice(sku.getSkuPrice());
      newItem.setOriginPrice(sku.getSkuPrice());
      newItem.setSpecs(sku.getSkuSpecEnums());
      newItem.setProductName(sku.getSkuName());
      newItem.setProductImage(sku.getSkuPicUrl());

      return shoppingCartItemMapper.insert(newItem) > 0;
    }
  }

  @Override
  @Transactional
  public Boolean updateCartItemQuantity(Long userId, Long itemId, Integer quantity) {
    if (quantity <= 0) {
      // 数量小于等于0，删除该商品
      deleteCartItem(userId, itemId);
      return true;
    }

    return shoppingCartItemMapper.updateItemQuantity(itemId, quantity) > 0;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean deleteCartItems(Long userId, List<Long> itemIds) {
    // 查询购物车ID
    ShoppingCart cart = getOrCreateUserCart(userId);

    // 确保只删除该购物车的商品
    return shoppingCartItemMapper.delete(
            Wrappers.<ShoppingCartItem>lambdaQuery()
                .in(ShoppingCartItem::getItemId, itemIds)
                .eq(ShoppingCartItem::getCartId, cart.getCartId()))
        > 0;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean selectOrUnselectAll(Long userId, boolean selected) {
    // 查询购物车ID
    ShoppingCart cart = getOrCreateUserCart(userId);

    // 更新所有商品的选中状态
    return shoppingCartItemMapper.update(
            null,
            Wrappers.<ShoppingCartItem>lambdaUpdate()
                .set(ShoppingCartItem::getIsSelected, selected ? 1 : 0)
                .eq(ShoppingCartItem::getCartId, cart.getCartId()))
        > 0;
  }

  @Override
  public BigDecimal calculateTotalPrice(List<ShoppingCartItem> cartItems) {
    if (cartItems == null || cartItems.isEmpty()) {
      return BigDecimal.ZERO;
    }

    return cartItems.stream()
        .filter(item -> item.getIsSelected() == 1) // 只计算选中的商品
        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  // 辅助方法：获取或创建用户购物车
  private ShoppingCart getOrCreateUserCart(Long userId) {
    QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_id", userId);

    ShoppingCart cart = shoppingCartMapper.selectOne(queryWrapper);

    if (cart == null) {
      cart = new ShoppingCart();
      cart.setUserId(userId);
      cart.setIsAllSelected(0); // 默认不全选
      cart.setTotalPrice(BigDecimal.ZERO);
      cart.setCreatedAt(new Date());
      cart.setUpdatedAt(new Date());

      shoppingCartMapper.insert(cart);
    }

    return cart;
  }

  private void deleteCartItem(Long userId, Long itemId) {
    // 查询购物车ID
    ShoppingCart cart = getOrCreateUserCart(userId);

    QueryWrapper<ShoppingCartItem> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("item_id", itemId).eq("cart_id", cart.getCartId());

    shoppingCartItemMapper.delete(queryWrapper);
  }
}
