package com.enba.cloud.h5.module.goods.category.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.goods.api.category.client.CategoryClient;
import com.enba.cloud.goods.api.category.entity.Category;
import com.enba.cloud.goods.api.category.req.CategoryReq;
import com.enba.cloud.goods.api.category.req.CategoryUpdateStatusReq;
import com.enba.cloud.goods.api.category.resp.CategoryResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类目
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Api(tags = "类目管理")
@RestController
@RequestMapping("/api/category")
public class H5CategoryController {

  @Autowired private CategoryClient categoryClient;

  /**
   * 获取分类树
   *
   * @return 分类树列表
   */
  @GetMapping("/tree")
  @ApiOperation("获取分类树")
  public Result<List<CategoryResp>> getCategoryTree() {
    return categoryClient.getCategoryTree();
  }

  /**
   * 获取指定分类的所有子节点树
   *
   * @param categoryId 分类ID
   * @return 子节点树列表
   */
  @GetMapping("/{categoryId}/children-tree")
  @ApiOperation("获取指定分类下的所有子节点树")
  public Result<List<CategoryResp>> getCategoryChildrenTree(@PathVariable Long categoryId) {
    return categoryClient.getCategoryChildrenTree(categoryId);
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/{id}")
  public Result<Category> findOne(@PathVariable Long id) {
    return categoryClient.findOne(id);
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/page")
  public Result<Page<Category>> findPage(
      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    return categoryClient.findPage(pageNum, pageSize);
  }

  /**
   * 新增和更新接口
   *
   * @param req category
   * @return result
   */
  @PostMapping
  public Result<Boolean> saveOrUpdate(@RequestBody @Validated CategoryReq req) {
    return categoryClient.saveOrUpdate(req);
  }

  /**
   * 启用或禁用分类
   *
   * @param req req
   * @return result
   */
  @PostMapping("/status")
  public Result<Boolean> updateStatus(@RequestBody @Validated CategoryUpdateStatusReq req) {
    return categoryClient.updateStatus(req);
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/{id}")
  public Result<Boolean> delete(@PathVariable Long id) {
    return categoryClient.delete(id);
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/del/batch")
  public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    return categoryClient.deleteBatch(ids);
  }
}
