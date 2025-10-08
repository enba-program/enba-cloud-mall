package com.enba.cloud.goods.spu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.goods.api.spu.client.SpuClient;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest;
import com.enba.cloud.goods.api.spu.req.SpuFindPageReq;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse;
import com.enba.cloud.goods.api.spu.resp.SpuPageResp;
import com.enba.cloud.goods.api.spu.resp.SpuResp;
import com.enba.cloud.goods.spu.service.ISpuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * SPU表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "商品管理")
@RestController
public class SpuController implements SpuClient {

  @Resource private ISpuService spuService;

  @Override
  @ApiOperation("创建商品")
  @DistributedLock
  public Result<Long> createProduct(@RequestBody @Validated ProductCreateRequest request) {
    return Result.success(spuService.createProduct(request));
  }

  @Override
  @ApiOperation("提交审核")
  @DistributedLock
  public Result<Boolean> submitAudit(@PathVariable Long spuId) {
    return Result.success(spuService.submitAudit(spuId));
  }

  @Override
  @ApiOperation("审核通过或失败")
  @DistributedLock
  public Result<Boolean> audit(@PathVariable Long spuId, @RequestParam Integer auditStatus) {
    return Result.success(spuService.audit(spuId, auditStatus));
  }

  @Override
  @ApiOperation("商品上下架")
  @DistributedLock
  public Result<Boolean> shelve(@PathVariable Long spuId, @RequestParam Integer shelveStatus) {
    return Result.success(spuService.shelve(spuId, shelveStatus));
  }

  @Override
  @ApiOperation("获取商品详情")
  public Result<ProductDetailResponse> getProductDetail(@PathVariable Long spuId) {
    return Result.success(spuService.getProductDetail(spuId));
  }

  @Override
  @ApiOperation("获取三级分类下的SPU列表")
  public Result<List<SpuResp>> findByCategoryId(@PathVariable Long categoryId) {
    return Result.success(spuService.findByCategoryId(categoryId));
  }

  @Override
  @ApiOperation("分页查询SPU列表")
  public Result<Page<SpuPageResp>> findPage(@RequestBody @Validated SpuFindPageReq pageReq) {
    return Result.success(spuService.findPage(pageReq));
  }
}
