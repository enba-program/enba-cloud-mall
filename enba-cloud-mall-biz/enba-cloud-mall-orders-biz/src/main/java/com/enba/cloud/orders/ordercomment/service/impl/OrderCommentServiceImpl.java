package com.enba.cloud.orders.ordercomment.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.cloud.orders.api.ordercomment.entity.OrderComment;
import com.enba.cloud.orders.ordercomment.mapper.OrderCommentMapper;
import com.enba.cloud.orders.ordercomment.service.IOrderCommentService;
import org.springframework.stereotype.Service;

/**
 * 订单评价表 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Service
public class OrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment>
    implements IOrderCommentService {}
