package com.enba.cloud.goods.api.category.resp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import lombok.Data;

@Data
public class CategoryResp {
  private Long id;
  private Long parentId;
  private String categoryCode;
  private String categoryPath;
  private Integer categoryLevel;
  private String categoryName;
  private String categoryIconUrl;
  private Integer status;
  private List<CategoryResp> children;

  public CategoryResp() {
    this.children = new ArrayList<>();
  }

  public CategoryResp(
      Long id,
      Long parentId,
      String categoryCode,
      String categoryPath,
      Integer categoryLevel,
      String categoryName,
      String categoryIconUrl,
      Integer status) {
    this.id = id;
    this.parentId = parentId;
    this.categoryCode = categoryCode;
    this.categoryPath = categoryPath;
    this.categoryLevel = categoryLevel;
    this.categoryName = categoryName;
    this.categoryIconUrl = categoryIconUrl;
    this.status = status;
    this.children = new ArrayList<>();
  }

  /**
   * 获取分类树中所有子类并拍平返回集合
   *
   * @param root 分类树的根节点
   * @return 拍平后的所有子类集合（包含根节点）
   */
  public static List<CategoryResp> flattenCategoryTree(CategoryResp root) {
    List<CategoryResp> result = new ArrayList<>();
    flattenCategoryTreeHelper(root, result);
    return result;
  }

  /**
   * 递归辅助方法，用于拍平分类树
   *
   * @param node 当前节点
   * @param result 结果集合
   */
  private static void flattenCategoryTreeHelper(CategoryResp node, List<CategoryResp> result) {
    if (node == null) {
      return;
    }

    // 添加当前节点到结果集
    result.add(node);

    // 递归处理子节点
    if (node.getChildren() != null) {
      for (CategoryResp child : node.getChildren()) {
        flattenCategoryTreeHelper(child, result);
      }
    }
  }

  /**
   * 获取分类树中所有子类并拍平返回集合
   *
   * @param root 分类树的根节点
   * @param func 函数式接口，用于获取每个节点的值
   * @param <T> 返回值的类型
   * @return 拍平后的所有子类集合（包含根节点）
   */
  public static <T> List<T> flattenCategoryTreeFunc(
      CategoryResp root, Function<CategoryResp, T> func) {
    List<T> result = new ArrayList<>();

    flattenCategoryTreeHelperFunc(root, result, func);
    return result;
  }

  /**
   * 递归辅助方法，用于拍平分类树
   *
   * @param node 当前节点
   * @param result 结果集合
   * @param func 函数式接口，用于获取每个节点的值
   * @param <T> 节点值的类型
   */
  private static <T> void flattenCategoryTreeHelperFunc(
      CategoryResp node, List<T> result, Function<CategoryResp, T> func) {
    if (node == null) {
      return;
    }

    // 添加当前节点到结果集
    result.add(func.apply(node));

    // 递归处理子节点
    if (node.getChildren() != null) {
      for (CategoryResp child : node.getChildren()) {
        flattenCategoryTreeHelperFunc(child, result, func);
      }
    }
  }

  public static void main(String[] args) {
    // 创建分类树
    CategoryResp root = new CategoryResp();
    root.setId(1L);
    root.setCategoryName("Root");

    CategoryResp child1 = new CategoryResp();
    child1.setId(2L);
    child1.setCategoryName("Child 1");

    CategoryResp child2 = new CategoryResp();
    child2.setId(3L);
    child2.setCategoryName("Child 2");

    CategoryResp grandChild = new CategoryResp();
    grandChild.setId(4L);
    grandChild.setCategoryName("Grand Child");

    // 构建树结构
    root.setChildren(Arrays.asList(child1, child2));
    child1.setChildren(Arrays.asList(grandChild));

    // 拍平分类树
    List<CategoryResp> flattened = CategoryResp.flattenCategoryTree(root);

    // 输出结果
    flattened.forEach(node -> System.out.print(node.getCategoryName() + ","));

    System.out.println(
        CategoryResp.flattenCategoryTreeFunc(
            root,
            (node) -> {
              return node.getId();
            }));
  }
}
