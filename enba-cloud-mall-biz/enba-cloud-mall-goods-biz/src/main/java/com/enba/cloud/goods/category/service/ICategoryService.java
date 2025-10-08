package com.enba.cloud.goods.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enba.cloud.goods.api.category.entity.Category;
import com.enba.cloud.goods.api.category.resp.CategoryResp;
import java.util.List;

/**
 * 类目 服务类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
public interface ICategoryService extends IService<Category> {
  /**
   * 获取分类树
   *
   * @return 分类树列表
   */
  List<CategoryResp> getCategoryTree();

  /**
   * 获取指定分类的所有子节点树
   *
   * @param categoryId 分类ID
   * @return 子节点树列表
   */
  List<CategoryResp> getCategoryChildrenTree(Long categoryId);
}
