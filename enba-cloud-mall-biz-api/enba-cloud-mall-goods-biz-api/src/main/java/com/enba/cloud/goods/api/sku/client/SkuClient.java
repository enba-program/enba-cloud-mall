package com.enba.cloud.goods.api.sku.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.goods.api.sku.entity.Sku;
import com.enba.cloud.goods.api.sku.factory.SkuClientFallbackFactory;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    contextId = "SkuClient1",
    name = ServiceAppConstants.GOODS_SERVICE_NAME,
    fallbackFactory = SkuClientFallbackFactory.class)
@ResponseBody
public interface SkuClient {

  @GetMapping("/api/sku/list-by-spu/{spuId}")
  @ApiOperation("获取指定spu下所有sku")
  Result<List<Sku>> findBySpuId(@PathVariable("spuId") Long spuId);

  @PostMapping("/api/sku/batch-by-skuid")
  @ApiOperation("批量获取sku")
  Result<List<Sku>> batchBySkuId(@RequestBody Set<Long> skuIdSet);

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/api/sku/{id}")
  Result<Sku> findOne(@PathVariable("id") Long id);

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/api/sku/page")
  Result<Page<Sku>> findPage(
      @RequestParam(name = "pageNum") Integer pageNum,
      @RequestParam(name = "pageSize") Integer pageSize);

  /**
   * 新增和更新接口
   *
   * @param sku sku
   * @return result
   */
  @PostMapping("/api/sku")
  Result<Boolean> save(@RequestBody Sku sku);

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/api/sku/{id}")
  Result<Boolean> delete(@PathVariable("id") Long id);

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/api/sku/del/batch")
  Result<Boolean> deleteBatch(@RequestBody List<Long> ids);
}
