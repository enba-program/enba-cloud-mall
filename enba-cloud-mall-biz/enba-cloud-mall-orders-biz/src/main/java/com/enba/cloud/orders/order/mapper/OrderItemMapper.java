package com.enba.cloud.orders.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enba.cloud.orders.api.order.entity.OrderItem;
import java.util.List;

/**
 * 订单商品表 Mapper 接口
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

  void insertBatchSomeColumn(List<OrderItem> orderItems);
}
