package com.enba.cloud.h5.module.goods.sku.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.goods.api.sku.client.SkuClient;
import com.enba.cloud.goods.api.sku.entity.Sku;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SKU表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "SKU管理")
@RestController
@RequestMapping("/api/sku")
public class H5SkuController {

  @Autowired private SkuClient skuClient;

  @GetMapping("/list-by-spu/{spuId}")
  @ApiOperation("获取指定spu下所有sku")
  public Result<List<Sku>> findBySpuId(@PathVariable Long spuId) {
    return skuClient.findBySpuId(spuId);
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/{id}")
  public Result<Sku> findOne(@PathVariable Long id) {
    return skuClient.findOne(id);
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/page")
  public Result<Page<Sku>> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    return skuClient.findPage(pageNum, pageSize);
  }

  /**
   * 新增和更新接口
   *
   * @param sku sku
   * @return result
   */
  @PostMapping
  public Result<Boolean> save(@RequestBody Sku sku) {
    return skuClient.save(sku);
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/{id}")
  public Result<Boolean> delete(@PathVariable Long id) {
    return skuClient.delete(id);
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/del/batch")
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return skuClient.deleteBatch(ids);
  }
}
