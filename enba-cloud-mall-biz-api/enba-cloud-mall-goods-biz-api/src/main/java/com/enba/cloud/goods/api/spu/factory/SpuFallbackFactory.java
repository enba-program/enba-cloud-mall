package com.enba.cloud.goods.api.spu.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.goods.api.spu.client.SpuClient;
import com.enba.cloud.goods.api.spu.req.ProductCreateRequest;
import com.enba.cloud.goods.api.spu.req.SpuFindPageReq;
import com.enba.cloud.goods.api.spu.resp.ProductDetailResponse;
import com.enba.cloud.goods.api.spu.resp.SpuPageResp;
import com.enba.cloud.goods.api.spu.resp.SpuResp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SpuFallbackFactory implements FallbackFactory<SpuClient> {

  private static final Logger log = LoggerFactory.getLogger(SpuFallbackFactory.class);

  @Override
  public SpuClient create(Throwable cause) {
    return new SpuClient() {
      @Override
      public Result<Long> createProduct(ProductCreateRequest request) {
        log.error("SpuClient createProduct 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SpuClient createProduct error");
      }

      @Override
      public Result<Boolean> submitAudit(Long spuId) {
        log.error("SpuClient submitAudit 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SpuClient submitAudit error");
      }

      @Override
      public Result<Boolean> audit(Long spuId, Integer auditStatus) {
        log.error("SpuClient audit 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SpuClient audit error");
      }

      @Override
      public Result<Boolean> shelve(Long spuId, Integer shelveStatus) {
        log.error("SpuClient shelve 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SpuClient shelve error");
      }

      @Override
      public Result<ProductDetailResponse> getProductDetail(Long spuId) {
        log.error("SpuClient getProductDetail 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SpuClient getProductDetail error");
      }

      @Override
      public Result<List<SpuResp>> findByCategoryId(Long categoryId) {
        log.error("SpuClient findByCategoryId 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SpuClient findByCategoryId error");
      }

      @Override
      public Result<Page<SpuPageResp>> findPage(SpuFindPageReq pageReq) {
        log.error("SpuClient findPage 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "SpuClient findPage error");
      }
    };
  }
}
