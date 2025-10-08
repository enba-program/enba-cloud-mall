package com.enba.cloud.goods.brand.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.boot.core.exception.BizException;
import com.enba.cloud.goods.api.brand.client.BrandClient;
import com.enba.cloud.goods.api.brand.entity.Brand;
import com.enba.cloud.goods.api.brand.req.BrandReq;
import com.enba.cloud.goods.api.brand.req.BrandUpdateStatusReq;
import com.enba.cloud.goods.brand.service.IBrandService;
import io.swagger.annotations.Api;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 品牌
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "品牌管理")
@RestController
@Slf4j
public class BrandController implements BrandClient {

  @Resource private IBrandService brandService;

  // 获取所有品牌
  @Override
  public Result<List<Brand>> findAll() {
    log.info("获取所有品牌");
    return Result.success(brandService.list());
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Brand> findOne(@PathVariable("id") Long id) {
    return Result.success(brandService.getById(id));
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @Override
  public Result<Page<Brand>> findPage(
      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
    return Result.success(brandService.page(new Page<>(pageNum, pageSize), queryWrapper));
  }

  /**
   * 新增和更新接口
   *
   * @param req brand
   * @return result
   */
  @Override
  public Result<Boolean> saveOrUpdate(@RequestBody @Validated BrandReq req) {
    Long id = req.getId();

    // 检查唯一性
    if (id != null) {
      // 更新
      Brand unit = brandService.getById(id);
      if (unit == null) {
        BizException.throwEx("单位不存在");
      }

      // 按照名称
      Brand brand =
          brandService.getOne(new QueryWrapper<Brand>().eq("brand_name", req.getBrandName()));
      if (brand != null && !brand.getId().equals(id)) {
        BizException.throwEx("品牌名称已存在");
      }
    } else {
      // 新增
      if (brandService.exists(new QueryWrapper<Brand>().eq("brand_name", req.getBrandName()))) {
        return Result.err(StatusEnum.ERR.getCode(), "品牌名称已存在");
      }
    }

    Brand brand = new Brand();
    BeanUtil.copyProperties(req, brand);

    return Result.success(brandService.saveOrUpdate(brand));
  }

  /**
   * 启用或禁用品牌
   *
   * @param req req
   * @return result
   */
  @Override
  public Result<Boolean> updateStatus(@RequestBody @Validated BrandUpdateStatusReq req) {
    // 检查是否存在
    Brand brand = brandService.getById(req.getId());
    if (brand == null) {
      return Result.err(StatusEnum.ERR.getCode(), "品牌不存在");
    }

    brand.setStatus(req.getStatus());

    return Result.success(brandService.updateById(brand));
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Boolean> delete(@PathVariable("id") Long id) {
    return Result.success(brandService.removeById(id));
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @Override
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return Result.success(brandService.removeByIds(ids));
  }
}
