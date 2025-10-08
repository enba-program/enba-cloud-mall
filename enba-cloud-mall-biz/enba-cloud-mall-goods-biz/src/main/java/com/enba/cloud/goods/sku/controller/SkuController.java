package com.enba.cloud.goods.sku.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.goods.api.sku.client.SkuClient;
import com.enba.cloud.goods.api.sku.entity.Sku;
import com.enba.cloud.goods.sku.service.ISkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * SKU表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "SKU管理")
@RestController
public class SkuController implements SkuClient {

  @Resource private ISkuService skuService;

  @Override
  @ApiOperation("获取指定spu下所有sku")
  public Result<List<Sku>> findBySpuId(@PathVariable Long spuId) {
    return Result.success(skuService.findBySpuId(spuId));
  }

  @Override
  @ApiOperation("批量获取sku")
  public Result<List<Sku>> batchBySkuId(Set<Long> skuIdSet) {
    return Result.success(skuService.batchBySkuId(skuIdSet));
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Sku> findOne(@PathVariable Long id) {
    return Result.success(skuService.getById(id));
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @Override
  public Result<Page<Sku>> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
    return Result.success(skuService.page(new Page<>(pageNum, pageSize), queryWrapper));
  }

  /**
   * 新增和更新接口
   *
   * @param sku sku
   * @return result
   */
  @Override
  public Result<Boolean> save(@RequestBody Sku sku) {
    return Result.success(skuService.saveOrUpdate(sku));
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Boolean> delete(@PathVariable Long id) {
    return Result.success(skuService.removeById(id));
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @Override
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return Result.success(skuService.removeByIds(ids));
  }
}
