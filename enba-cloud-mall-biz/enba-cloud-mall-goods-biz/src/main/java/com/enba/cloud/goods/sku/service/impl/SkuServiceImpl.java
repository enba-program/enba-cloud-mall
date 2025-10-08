package com.enba.cloud.goods.sku.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.cloud.goods.api.sku.entity.Sku;
import com.enba.cloud.goods.sku.mapper.SkuMapper;
import com.enba.cloud.goods.sku.service.ISkuService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SKU表 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

  @Autowired private SkuMapper skuMapper;

  @Override
  public List<Sku> findBySpuId(Long spuId) {
    QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("spu_id", spuId);

    return skuMapper.selectList(queryWrapper);
  }

  @Override
  public List<Sku> batchBySkuId(Set<Long> skuIdSet) {
    return skuMapper.selectList(Wrappers.<Sku>lambdaQuery().in(Sku::getId, skuIdSet));
  }
}
