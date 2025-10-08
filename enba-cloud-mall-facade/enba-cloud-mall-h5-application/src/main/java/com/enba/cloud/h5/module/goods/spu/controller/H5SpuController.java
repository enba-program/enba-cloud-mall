package com.enba.cloud.h5.module.goods.spu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.goods.api.spu.client.SpuClient;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest;
import com.enba.cloud.goods.api.spu.req.SpuFindPageReq;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse;
import com.enba.cloud.goods.api.spu.resp.SpuPageResp;
import com.enba.cloud.goods.api.spu.resp.SpuResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SPU表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/spu")
public class H5SpuController {

  @Autowired private SpuClient spuClient;

  @PostMapping("/products")
  @ApiOperation("创建商品")
  @DistributedLock
  public Result<Long> createProduct(@RequestBody @Validated ProductCreateRequest request) {
    return spuClient.createProduct(request);
  }

  @PostMapping("/products/{spuId}/submit")
  @ApiOperation("提交审核")
  @DistributedLock
  public Result<Boolean> submitAudit(@PathVariable Long spuId) {
    return spuClient.submitAudit(spuId);
  }

  @PostMapping("/products/{spuId}/audit")
  @ApiOperation("审核通过或失败")
  @DistributedLock
  public Result<Boolean> audit(@PathVariable Long spuId, @RequestParam Integer auditStatus) {
    return spuClient.audit(spuId, auditStatus);
  }

  @PostMapping("/products/{spuId}/shelve")
  @ApiOperation("商品上下架")
  @DistributedLock
  public Result<Boolean> shelve(@PathVariable Long spuId, @RequestParam Integer shelveStatus) {
    return spuClient.shelve(spuId, shelveStatus);
  }

  @GetMapping("/products/{spuId}")
  @ApiOperation("获取商品详情")
  public Result<ProductDetailResponse> getProductDetail(@PathVariable Long spuId) {
    return spuClient.getProductDetail(spuId);
  }

  @GetMapping("/products/list-by-category/{categoryId}")
  @ApiOperation("获取三级分类下的SPU列表")
  public Result<List<SpuResp>> findByCategoryId(@PathVariable Long categoryId) {
    return spuClient.findByCategoryId(categoryId);
  }

  @PostMapping("/products/page")
  @ApiOperation("分页查询SPU列表")
  public Result<Page<SpuPageResp>> findPage(@RequestBody @Validated SpuFindPageReq pageReq) {
    return spuClient.findPage(pageReq);
  }
}
