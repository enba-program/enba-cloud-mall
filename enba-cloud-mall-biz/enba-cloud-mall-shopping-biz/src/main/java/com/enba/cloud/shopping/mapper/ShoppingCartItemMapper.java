package com.enba.cloud.shopping.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enba.cloud.shopping.api.entity.ShoppingCartItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShoppingCartItemMapper extends BaseMapper<ShoppingCartItem> {
  /** 根据购物车ID查询所有商品项 */
  List<ShoppingCartItem> selectByCartId(@Param("cartId") Long cartId);

  /** 根据用户ID查询购物车项(包含购物车主表信息) */
  List<ShoppingCartItem> selectByUserIdWithCart(@Param("userId") Long userId);

  /** 批量插入购物车项 */
  int batchInsert(@Param("items") List<ShoppingCartItem> items);

  /** 更新购物车项选中状态 */
  int updateItemSelected(@Param("itemId") Long itemId, @Param("isSelected") Integer isSelected);

  /** 更新购物车项数量 */
  int updateItemQuantity(@Param("itemId") Long itemId, @Param("quantity") Integer quantity);

  /** 删除购物车项 */
  int deleteByItemIds(@Param("itemIds") List<Long> itemIds);
}
