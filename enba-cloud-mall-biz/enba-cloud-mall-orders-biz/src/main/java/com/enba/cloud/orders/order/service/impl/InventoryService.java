package com.enba.cloud.orders.order.service.impl;


import com.enba.cloud.orders.api.order.req.OrderItemReq;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InventoryService {

  public void lockStock(List<OrderItemReq> orderItems) {}

  public void unlockStock(List<OrderItemReq> orderItems) {}
}
