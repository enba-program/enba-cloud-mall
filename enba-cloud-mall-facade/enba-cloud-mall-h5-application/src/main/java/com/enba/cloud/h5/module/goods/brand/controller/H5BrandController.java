package com.enba.cloud.h5.module.goods.brand.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.goods.api.brand.client.BrandClient;
import com.enba.cloud.goods.api.brand.entity.Brand;
import com.enba.cloud.goods.api.brand.req.BrandReq;
import com.enba.cloud.goods.api.brand.req.BrandUpdateStatusReq;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 品牌
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "品牌管理")
@RestController
@RequestMapping("/api/brand")
@Slf4j
public class H5BrandController {

  @Autowired private BrandClient brandClient;

  // 获取所有品牌
  @GetMapping
  public Result<List<Brand>> findAll() {
    log.info("获取所有品牌");
    return brandClient.findAll();
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/{id}")
  public Result<Brand> findOne(@PathVariable Long id) {
    return brandClient.findOne(id);
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/page")
  public Result<Page<Brand>> findPage(
      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    return brandClient.findPage(pageNum, pageSize);
  }

  /**
   * 新增和更新接口
   *
   * @param req brand
   * @return result
   */
  @PostMapping
  public Result<Boolean> saveOrUpdate(@RequestBody @Validated BrandReq req) {
    return brandClient.saveOrUpdate(req);
  }

  /**
   * 启用或禁用品牌
   *
   * @param req req
   * @return result
   */
  @PostMapping("/status")
  public Result<Boolean> updateStatus(@RequestBody @Validated BrandUpdateStatusReq req) {
    return brandClient.updateStatus(req);
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/{id}")
  public Result<Boolean> delete(@PathVariable Long id) {
    return brandClient.delete(id);
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/del/batch")
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return brandClient.deleteBatch(ids);
  }
}
