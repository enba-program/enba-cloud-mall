package com.enba.cloud.goods.api.category.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.goods.api.category.entity.Category;
import com.enba.cloud.goods.api.category.factory.CategoryFallbackFactory;
import com.enba.cloud.goods.api.category.req.CategoryReq;
import com.enba.cloud.goods.api.category.req.CategoryUpdateStatusReq;
import com.enba.cloud.goods.api.category.resp.CategoryResp;
import io.swagger.annotations.ApiOperation;
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
    contextId = "CategoryClient",
    name = ServiceAppConstants.GOODS_SERVICE_NAME,
    fallbackFactory = CategoryFallbackFactory.class)
@ResponseBody
public interface CategoryClient {

  /**
   * 获取分类树
   *
   * @return 分类树列表
   */
  @GetMapping(" /api/category/tree")
  @ApiOperation("获取分类树")
  Result<List<CategoryResp>> getCategoryTree();

  /**
   * 获取指定分类的所有子节点树
   *
   * @param categoryId 分类ID
   * @return 子节点树列表
   */
  @GetMapping("/api/category/{categoryId}/children-tree")
  @ApiOperation("获取指定分类下的所有子节点树")
  Result<List<CategoryResp>> getCategoryChildrenTree(@PathVariable("categoryId") Long categoryId);

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/api/category/{id}")
  Result<Category> findOne(@PathVariable("id") Long id);

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/api/category/page")
  Result<Page<Category>> findPage(
      @RequestParam(name = "pageNum") Integer pageNum,
      @RequestParam(name = "pageSize") Integer pageSize);

  /**
   * 新增和更新接口
   *
   * @param req category
   * @return result
   */
  @PostMapping("/api/category")
  Result<Boolean> saveOrUpdate(@RequestBody @Validated CategoryReq req);

  /**
   * 启用或禁用分类
   *
   * @param req req
   * @return result
   */
  @PostMapping("/api/category/status")
  Result<Boolean> updateStatus(@RequestBody @Validated CategoryUpdateStatusReq req);

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/api/category/{id}")
  Result<Boolean> delete(@PathVariable("id") Long id);

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/api/category/del/batch")
  Result<Boolean> deleteBatch(@RequestBody List<Long> ids);
}
