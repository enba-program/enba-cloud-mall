package com.enba.cloud.goods.api.unit.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.goods.api.unit.client.UnitClient;
import com.enba.cloud.goods.api.unit.entity.Unit;
import com.enba.cloud.goods.api.unit.req.UnitReq;
import com.enba.cloud.goods.api.unit.req.UnitUpdateStatusReq;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UnitFallbackFactory implements FallbackFactory<UnitClient> {

  private static final Logger log = LoggerFactory.getLogger(UnitFallbackFactory.class);

  @Override
  public UnitClient create(Throwable cause) {
    return new UnitClient() {
      @Override
      public Result<Boolean> updateStatus(UnitUpdateStatusReq req) {
        log.error("UnitClient updateStatus 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UnitClient updateStatus error");
      }

      @Override
      public Result<Unit> findOne(Long id) {
        log.error("UnitClient findOne 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UnitClient findOne error");
      }

      @Override
      public Result<Page<Unit>> findPage(Integer pageNum, Integer pageSize) {
        log.error("UnitClient findPage 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UnitClient findPage error");
      }

      @Override
      public Result<Boolean> saveOrUpdate(UnitReq req) {
        log.error("UnitClient saveOrUpdate 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UnitClient saveOrUpdate error");
      }

      @Override
      public Result<Boolean> delete(Long id) {
        log.error("UnitClient delete 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UnitClient delete error");
      }

      @Override
      public Result<Boolean> deleteBatch(List<Long> ids) {
        log.error("UnitClient deleteBatch 调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "UnitClient deleteBatch error");
      }
    };
  }
}
