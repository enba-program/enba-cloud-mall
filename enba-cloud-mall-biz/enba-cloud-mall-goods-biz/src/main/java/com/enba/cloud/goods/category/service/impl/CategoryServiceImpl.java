package com.enba.cloud.goods.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.cloud.goods.api.category.entity.Category;
import com.enba.cloud.goods.category.mapper.CategoryMapper;
import com.enba.cloud.goods.api.category.resp.CategoryResp;
import com.enba.cloud.goods.category.service.ICategoryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * 类目 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements ICategoryService {
  @Override
  public List<CategoryResp> getCategoryTree() {
    // 查询所有未删除且启用的分类
    QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .eq("deleted", 0)
        .eq("status", 1)
        .orderByAsc("category_level")
        .orderByAsc("parent_id")
        .orderByAsc("id");

    List<Category> allCategories = this.list(queryWrapper);

    // 构建分类树
    return buildCategoryTree(allCategories);
  }

  /**
   * 构建分类树
   *
   * @param allCategories 所有分类列表
   * @return 分类树
   */
  private List<CategoryResp> buildCategoryTree(List<Category> allCategories) {
    // 先将所有分类转换为CategoryResp并放入map
    Map<Long, CategoryResp> categoryMap = new HashMap<>();
    for (Category category : allCategories) {
      categoryMap.put(category.getId(), convertToResp(category));
    }

    // 构建树结构
    List<CategoryResp> roots = new ArrayList<>();

    for (Category category : allCategories) {
      CategoryResp categoryResp = categoryMap.get(category.getId());

      if (category.getParentId() == null || category.getParentId() == 0) {
        // 顶级分类
        roots.add(categoryResp);
      } else {
        // 非顶级分类，找到父分类并添加到children
        CategoryResp parent = categoryMap.get(category.getParentId());
        if (parent != null) {
          parent.getChildren().add(categoryResp);
        }
      }
    }

    return roots;
  }

  /**
   * 将Category转换为CategoryResp
   *
   * @param category 分类实体
   * @return 分类响应DTO
   */
  private CategoryResp convertToResp(Category category) {
    return new CategoryResp(
        category.getId(),
        category.getParentId(),
        category.getCategoryCode(),
        category.getCategoryPath(),
        category.getCategoryLevel(),
        category.getCategoryName(),
        category.getCategoryIconUrl(),
        category.getStatus());
  }

  @Override
  public List<CategoryResp> getCategoryChildrenTree(Long categoryId) {
    // 查询所有未删除且启用的分类
    QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .eq("deleted", 0)
        .eq("status", 1)
        .orderByAsc("category_level")
        .orderByAsc("parent_id")
        .orderByAsc("id");

    List<Category> allCategories = this.list(queryWrapper);

    // 构建分类ID到CategoryResp的映射
    Map<Long, CategoryResp> categoryMap = new HashMap<>();
    for (Category category : allCategories) {
      categoryMap.put(category.getId(), convertToResp(category));
    }

    // 找到指定分类及其所有子分类
    List<CategoryResp> result = new ArrayList<>();
    findChildrenTree(categoryId, allCategories, categoryMap, result);

    return result;
  }

  /**
   * 递归查找子分类树
   *
   * @param parentId 父分类ID
   * @param allCategories 所有分类列表
   * @param categoryMap 分类映射
   * @param result 结果列表
   */
  private void findChildrenTree(
      Long parentId,
      List<Category> allCategories,
      Map<Long, CategoryResp> categoryMap,
      List<CategoryResp> result) {
    for (Category category : allCategories) {
      if (category.getParentId() != null && category.getParentId().equals(parentId)) {
        CategoryResp categoryResp = categoryMap.get(category.getId());
        // 递归查找子分类
        findChildrenTree(category.getId(), allCategories, categoryMap, categoryResp.getChildren());
        result.add(categoryResp);
      }
    }
  }
}
