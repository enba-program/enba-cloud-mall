package com.enba.cloud.goods.api.spu.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.DistributedLock;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.goods.api.spu.factory.SpuFallbackFactory;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest;
import com.enba.cloud.goods.api.spu.req.SpuFindPageReq;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse;
import com.enba.cloud.goods.api.spu.resp.SpuPageResp;
import com.enba.cloud.goods.api.spu.resp.SpuResp;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    contextId = "SpuClient",
    name = ServiceAppConstants.GOODS_SERVICE_NAME,
    fallbackFactory = SpuFallbackFactory.class)
@ResponseBody
public interface SpuClient {

  @PostMapping("/api/spu/products")
  @ApiOperation("创建商品")
  @DistributedLock
  Result<Long> createProduct(@RequestBody @Validated ProductCreateRequest request);

  @PostMapping("/api/spu/products/{spuId}/submit")
  @ApiOperation("提交审核")
  @DistributedLock
  Result<Boolean> submitAudit(@PathVariable("spuId") Long spuId);

  @PostMapping("/api/spu/products/{spuId}/audit")
  @ApiOperation("审核通过或失败")
  @DistributedLock
  Result<Boolean> audit(
      @PathVariable("spuId") Long spuId, @RequestParam("auditStatus") Integer auditStatus);

  @PostMapping("/api/spu/products/{spuId}/shelve")
  @ApiOperation("商品上下架")
  @DistributedLock
  Result<Boolean> shelve(
      @PathVariable("spuId") Long spuId, @RequestParam("shelveStatus") Integer shelveStatus);

  @GetMapping("/api/spu/products/{spuId}")
  @ApiOperation("获取商品详情")
  Result<ProductDetailResponse> getProductDetail(@PathVariable("spuId") Long spuId);

  @GetMapping("/api/spu/products/list-by-category/{categoryId}")
  @ApiOperation("获取三级分类下的SPU列表")
  Result<List<SpuResp>> findByCategoryId(@PathVariable("categoryId") Long categoryId);

  @PostMapping("/api/spu/products/page")
  @ApiOperation("分页查询SPU列表")
  Result<Page<SpuPageResp>> findPage(@RequestBody @Validated SpuFindPageReq pageReq);
}
