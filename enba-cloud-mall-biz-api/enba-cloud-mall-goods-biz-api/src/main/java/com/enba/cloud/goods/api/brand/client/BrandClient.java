package com.enba.cloud.goods.api.brand.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.goods.api.brand.entity.Brand;
import com.enba.cloud.goods.api.brand.factory.BrandFallbackFactory;
import com.enba.cloud.goods.api.brand.req.BrandReq;
import com.enba.cloud.goods.api.brand.req.BrandUpdateStatusReq;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    contextId = "BrandClient",
    name = ServiceAppConstants.GOODS_SERVICE_NAME,
    fallbackFactory = BrandFallbackFactory.class)
@ResponseBody
public interface BrandClient {

  // 获取所有品牌
  @GetMapping("/api/brand")
  Result<List<Brand>> findAll();

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/api/brand/{id}")
  Result<Brand> findOne(@PathVariable("id") Long id);

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/api/brand/page")
  Result<Page<Brand>> findPage(
      @RequestParam(name = "pageNum") Integer pageNum,
      @RequestParam(name = "pageSize") Integer pageSize);

  /**
   * 新增和更新接口
   *
   * @param req brand
   * @return result
   */
  @PostMapping("/api/brand")
  Result<Boolean> saveOrUpdate(@RequestBody @Validated BrandReq req);

  /**
   * 启用或禁用品牌
   *
   * @param req req
   * @return result
   */
  @PostMapping("/api/brand/status")
  Result<Boolean> updateStatus(@RequestBody @Validated BrandUpdateStatusReq req);

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/api/brand/{id}")
  Result<Boolean> delete(@PathVariable("id") Long id);

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/api/brand/del/batch")
  Result<Boolean> deleteBatch(@RequestBody List<Long> ids);
}
