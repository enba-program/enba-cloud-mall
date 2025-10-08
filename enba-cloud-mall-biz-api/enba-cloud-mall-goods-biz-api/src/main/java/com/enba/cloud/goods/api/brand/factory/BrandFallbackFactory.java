package com.enba.cloud.goods.api.brand.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.goods.api.brand.client.BrandClient;
import com.enba.cloud.goods.api.brand.entity.Brand;
import com.enba.cloud.goods.api.brand.req.BrandReq;
import com.enba.cloud.goods.api.brand.req.BrandUpdateStatusReq;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BrandFallbackFactory implements FallbackFactory<BrandClient> {

  private static final Logger log = LoggerFactory.getLogger(BrandFallbackFactory.class);

  @Override
  public BrandClient create(Throwable cause) {
    return new BrandClient() {
      @Override
      public Result<List<Brand>> findAll() {
        log.error("BrandClient findAll 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "BrandClient findAll error");
      }

      @Override
      public Result<Brand> findOne(Long id) {
        log.error("BrandClient findOne 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "BrandClient findOne error");
      }

      @Override
      public Result<Page<Brand>> findPage(Integer pageNum, Integer pageSize) {
        log.error("BrandClient findPage 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "BrandClient findPage error");
      }

      @Override
      public Result<Boolean> saveOrUpdate(BrandReq req) {
        log.error("BrandClient saveOrUpdate 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "BrandClient saveOrUpdate error");
      }

      @Override
      public Result<Boolean> updateStatus(BrandUpdateStatusReq req) {
        log.error("BrandClient updateStatus 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "BrandClient updateStatus error");
      }

      @Override
      public Result<Boolean> delete(Long id) {
        log.error("BrandClient delete 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "BrandClient delete error");
      }

      @Override
      public Result<Boolean> deleteBatch(List<Long> ids) {
        log.error("BrandClient deleteBatch 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "BrandClient deleteBatch error");
      }
    };
  }
}
