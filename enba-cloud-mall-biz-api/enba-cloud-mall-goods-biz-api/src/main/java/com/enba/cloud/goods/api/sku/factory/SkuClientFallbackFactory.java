package com.enba.cloud.goods.api.sku.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.goods.api.sku.client.SkuClient;
import com.enba.cloud.goods.api.sku.entity.Sku;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SkuClientFallbackFactory implements FallbackFactory<SkuClient> {

  private static final Logger log = LoggerFactory.getLogger(SkuClientFallbackFactory.class);

  @Override
  public SkuClient create(Throwable cause) {
    return new SkuClient() {
      @Override
      public Result<List<Sku>> findBySpuId(Long spuId) {
        log.error("SkuClient findBySpuId 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SkuClient findBySpuId error");
      }

      @Override
      public Result<List<Sku>> batchBySkuId(Set<Long> skuIdSet) {
        log.error("SkuClient batchBySkuId 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SkuClient batchBySkuId error");
      }

      @Override
      public Result<Sku> findOne(Long id) {
        log.error("SkuClient findOne 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SkuClient findOne error");
      }

      @Override
      public Result<Page<Sku>> findPage(Integer pageNum, Integer pageSize) {
        log.error("SkuClient findPage 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SkuClient findPage error");
      }

      @Override
      public Result<Boolean> save(Sku sku) {
        log.error("SkuClient save 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SkuClient save error");
      }

      @Override
      public Result<Boolean> delete(Long id) {
        log.error("SkuClient delete 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SkuClient delete error");
      }

      @Override
      public Result<Boolean> deleteBatch(List<Long> ids) {
        log.error("SkuClient deleteBatch 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SkuClient deleteBatch error");
      }
    };
  }
}
