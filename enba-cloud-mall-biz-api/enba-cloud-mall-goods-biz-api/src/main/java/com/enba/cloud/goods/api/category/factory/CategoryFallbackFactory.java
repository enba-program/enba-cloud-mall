package com.enba.cloud.goods.api.category.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.goods.api.category.client.CategoryClient;
import com.enba.cloud.goods.api.category.entity.Category;
import com.enba.cloud.goods.api.category.req.CategoryReq;
import com.enba.cloud.goods.api.category.req.CategoryUpdateStatusReq;
import com.enba.cloud.goods.api.category.resp.CategoryResp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CategoryFallbackFactory implements FallbackFactory<CategoryClient> {

  private static final Logger log = LoggerFactory.getLogger(CategoryFallbackFactory.class);

  @Override
  public CategoryClient create(Throwable cause) {
    return new CategoryClient() {
      @Override
      public Result<List<CategoryResp>> getCategoryTree() {
        log.error("CategoryClient getCategoryTree 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient getCategoryTree error");
      }

      @Override
      public Result<List<CategoryResp>> getCategoryChildrenTree(Long categoryId) {
        log.error("CategoryClient getCategoryChildrenTree 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient getCategoryChildrenTree error");
      }

      @Override
      public Result<Category> findOne(Long id) {
        log.error("CategoryClient findOne 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient findOne error");
      }

      @Override
      public Result<Page<Category>> findPage(Integer pageNum, Integer pageSize) {
        log.error("CategoryClient findPage 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient findPage error");
      }

      @Override
      public Result<Boolean> saveOrUpdate(CategoryReq req) {
        log.error("CategoryClient saveOrUpdate 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient saveOrUpdate error");
      }

      @Override
      public Result<Boolean> updateStatus(CategoryUpdateStatusReq req) {
        log.error("CategoryClient updateStatus 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient updateStatus error");
      }

      @Override
      public Result<Boolean> delete(Long id) {
        log.error("CategoryClient delete 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient delete error");
      }

      @Override
      public Result<Boolean> deleteBatch(List<Long> ids) {
        log.error("CategoryClient deleteBatch 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "CategoryClient deleteBatch error");
      }
    };
  }
}
