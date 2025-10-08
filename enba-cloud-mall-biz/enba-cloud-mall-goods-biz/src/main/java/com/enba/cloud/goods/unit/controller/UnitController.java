package com.enba.cloud.goods.unit.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.boot.core.exception.BizException;
import com.enba.cloud.goods.api.unit.client.UnitClient;
import com.enba.cloud.goods.api.unit.entity.Unit;
import com.enba.cloud.goods.api.unit.req.UnitReq;
import com.enba.cloud.goods.api.unit.req.UnitUpdateStatusReq;
import com.enba.cloud.goods.unit.service.IUnitService;
import io.swagger.annotations.Api;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 单位
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "单位管理")
@RestController
public class UnitController implements UnitClient {

  @Resource private IUnitService unitService;

  /**
   * 启用或禁用
   *
   * @param req req
   * @return result
   */
  @Override
  public Result<Boolean> updateStatus(@RequestBody @Validated UnitUpdateStatusReq req) {
    Unit unit = unitService.getById(req.getId());
    if (unit == null) {
      BizException.throwEx("单位不存在");
    }

    unit.setStatus(req.getStatus());

    return Result.success(unitService.updateById(unit));
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Unit> findOne(@PathVariable Long id) {
    return Result.success(unitService.getById(id));
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @Override
  public Result<Page<Unit>> findPage(
      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
    return Result.success(unitService.page(new Page<>(pageNum, pageSize), queryWrapper));
  }

  /**
   * 新增和更新接口
   *
   * @param req unit
   * @return result
   */
  @Override
  public Result<Boolean> saveOrUpdate(@RequestBody @Validated UnitReq req) {
    Long id = req.getId();

    // 检查唯一性
    if (id != null) {
      // 更新
      Unit unit = unitService.getById(id);
      if (unit == null) {
        BizException.throwEx("单位不存在");
      }

      // 按照名称
      Unit unitName =
          unitService.getOne(new QueryWrapper<Unit>().eq("unit_name", req.getUnitName()));
      if (unitName != null && !unitName.getId().equals(id)) {
        BizException.throwEx("单位名称已存在");
      }
    } else {
      // 新增
      if (unitService.exists(new QueryWrapper<Unit>().eq("unit_name", req.getUnitName()))) {
        return Result.err(StatusEnum.ERR.getCode(), "单位名称已存在");
      }
    }

    Unit unit = new Unit();
    BeanUtil.copyProperties(req, unit);

    return Result.success(unitService.saveOrUpdate(unit));
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Boolean> delete(@PathVariable Long id) {
    return Result.success(unitService.removeById(id));
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @Override
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return Result.success(unitService.removeByIds(ids));
  }
}
