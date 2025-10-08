package com.enba.cloud.shopping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enba.cloud.shopping.api.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {}
