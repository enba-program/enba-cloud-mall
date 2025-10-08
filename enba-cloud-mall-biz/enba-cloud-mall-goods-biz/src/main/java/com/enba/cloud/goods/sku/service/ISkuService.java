package com.enba.cloud.goods.sku.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enba.cloud.goods.api.sku.entity.Sku;
import java.util.List;
import java.util.Set;

/**
 * SKU表 服务类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
public interface ISkuService extends IService<Sku> {

  /**
   * 通过spuId查询sku
   *
   * @param spuId spuId
   * @return List<Sku>
   */
  List<Sku> findBySpuId(Long spuId);

  /**
   * 批量查询sku
   *
   * @param skuIdSet skuIdSet
   * @return List<Sku>
   */
  List<Sku> batchBySkuId(Set<Long> skuIdSet);
}
