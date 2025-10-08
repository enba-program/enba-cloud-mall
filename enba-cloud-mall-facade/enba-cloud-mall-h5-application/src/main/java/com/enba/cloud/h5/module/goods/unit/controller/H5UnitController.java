package com.enba.cloud.h5.module.goods.unit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.goods.api.unit.client.UnitClient;
import com.enba.cloud.goods.api.unit.entity.Unit;
import com.enba.cloud.goods.api.unit.req.UnitReq;
import com.enba.cloud.goods.api.unit.req.UnitUpdateStatusReq;
import io.swagger.annotations.Api;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 单位
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "单位管理")
@RestController
@RequestMapping("/api/unit")
public class H5UnitController {

  @Resource private UnitClient unitClient;

  /**
   * 启用或禁用
   *
   * @param req req
   * @return result
   */
  @PostMapping("/status")
  public Result<Boolean> updateStatus(@RequestBody @Validated UnitUpdateStatusReq req) {
    return unitClient.updateStatus(req);
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/{id}")
  public Result<Unit> findOne(@PathVariable Long id) {
    return unitClient.findOne(id);
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/page")
  public Result<Page<Unit>> findPage(
      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    return unitClient.findPage(pageNum, pageSize);
  }

  /**
   * 新增和更新接口
   *
   * @param req unit
   * @return result
   */
  @PostMapping
  public Result<Boolean> saveOrUpdate(@RequestBody @Validated UnitReq req) {
    return unitClient.saveOrUpdate(req);
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/{id}")
  public Result<Boolean> delete(@PathVariable Long id) {
    return unitClient.delete(id);
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/del/batch")
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return unitClient.deleteBatch(ids);
  }
}
