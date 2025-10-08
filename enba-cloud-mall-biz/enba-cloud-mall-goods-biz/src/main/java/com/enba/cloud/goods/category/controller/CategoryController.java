package com.enba.cloud.goods.category.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.boot.core.exception.BizException;
import com.enba.cloud.goods.api.category.client.CategoryClient;
import com.enba.cloud.goods.api.category.entity.Category;
import com.enba.cloud.goods.api.category.req.CategoryReq;
import com.enba.cloud.goods.api.category.req.CategoryUpdateStatusReq;
import com.enba.cloud.goods.api.category.resp.CategoryResp;
import com.enba.cloud.goods.category.manager.CategoryCodeGenerator;
import com.enba.cloud.goods.category.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 类目
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "类目管理")
@RestController
public class CategoryController implements CategoryClient {

  @Resource private ICategoryService categoryService;

  /**
   * 获取分类树
   *
   * @return 分类树列表
   */
  @Override
  @ApiOperation("获取分类树")
  public Result<List<CategoryResp>> getCategoryTree() {
    return Result.success(categoryService.getCategoryTree());
  }

  /**
   * 获取指定分类的所有子节点树
   *
   * @param categoryId 分类ID
   * @return 子节点树列表
   */
  @Override
  @ApiOperation("获取指定分类下的所有子节点树")
  public Result<List<CategoryResp>> getCategoryChildrenTree(@PathVariable Long categoryId) {
    return Result.success(categoryService.getCategoryChildrenTree(categoryId));
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Category> findOne(@PathVariable Long id) {
    return Result.success(categoryService.getById(id));
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @Override
  public Result<Page<Category>> findPage(
      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
    return Result.success(categoryService.page(new Page<>(pageNum, pageSize), queryWrapper));
  }

  /**
   * 新增和更新接口
   *
   * @param req category
   * @return result
   */
  @Override
  public Result<Boolean> saveOrUpdate(@RequestBody @Validated CategoryReq req) {
    Category category = new Category();
    BeanUtil.copyProperties(req, category);

    Long id = req.getId();

    // 检查唯一性
    if (id != null) {
      // 更新
      Category categoryDb = categoryService.getById(id);
      if (categoryDb == null) {
        BizException.throwEx("分类不存在");
      }

      // 名称验重
      Category categoryDb2 =
          categoryService.getOne(
              new QueryWrapper<Category>().eq("category_name", req.getCategoryName()));
      if (categoryDb2 != null && !categoryDb2.getId().equals(id)) {
        BizException.throwEx("分类名称已存在");
      }

      category.setParentId(null);
      category.setCategoryCode(null);
      category.setCategoryPath(null);
      category.setCategoryLevel(null);
    } else {
      // 新增
      Category one =
          categoryService.getOne(
              Wrappers.<Category>lambdaQuery()
                  .eq(Category::getCategoryName, req.getCategoryName()));
      if (one != null) {
        return Result.err(StatusEnum.ERR.getCode(), "分类名称已存在");
      }

      // 父级分类
      Long parentId = req.getParentId();
      if (parentId == 0L) {
        category.setCategoryPath("0");
        category.setCategoryLevel(1);
      } else {
        Category parent = categoryService.getById(req.getParentId());
        if (parent == null) {
          return Result.err(StatusEnum.ERR.getCode(), "父级分类不存在");
        }

        category.setCategoryPath("0");
        category.setCategoryLevel(parent.getCategoryLevel() + 1);
      }

      // 生成编码
      category.setCategoryCode(CategoryCodeGenerator.generateCode());
    }

    return Result.success(categoryService.saveOrUpdate(category));
  }

  /**
   * 启用或禁用分类
   *
   * @param req req
   * @return result
   */
  @Override
  public Result<Boolean> updateStatus(@RequestBody @Validated CategoryUpdateStatusReq req) {
    // 检查存在
    Category categoryDb = categoryService.getById(req.getId());
    if (categoryDb == null) {
      return Result.err(StatusEnum.ERR.getCode(), "分类不存在");
    }

    categoryDb.setStatus(req.getStatus());
    return Result.success(categoryService.updateById(categoryDb));
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @Override
  public Result<Boolean> delete(@PathVariable Long id) {
    return Result.success(categoryService.removeById(id));
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @Override
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return Result.success(categoryService.removeByIds(ids));
  }
}
