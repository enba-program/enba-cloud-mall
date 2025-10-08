package com.enba.cloud.goods.api.unit.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.goods.api.unit.entity.Unit;
import com.enba.cloud.goods.api.unit.factory.UnitFallbackFactory;
import com.enba.cloud.goods.api.unit.req.UnitReq;
import com.enba.cloud.goods.api.unit.req.UnitUpdateStatusReq;
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
    contextId = "UnitClient",
    name = ServiceAppConstants.GOODS_SERVICE_NAME,
    fallbackFactory = UnitFallbackFactory.class)
@ResponseBody
public interface UnitClient {

  /**
   * 启用或禁用
   *
   * @param req req
   * @return result
   */
  @PostMapping("/api/unit/status")
  Result<Boolean> updateStatus(@RequestBody @Validated UnitUpdateStatusReq req);

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/api/unit/{id}")
  Result<Unit> findOne(@PathVariable("id") Long id);

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/api/unit/page")
  Result<Page<Unit>> findPage(
      @RequestParam(name = "pageNum") Integer pageNum,
      @RequestParam(name = "pageSize") Integer pageSize);

  /**
   * 新增和更新接口
   *
   * @param req unit
   * @return result
   */
  @PostMapping("/api/unit")
  Result<Boolean> saveOrUpdate(@RequestBody @Validated UnitReq req);

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/api/unit/{id}")
  Result<Boolean> delete(@PathVariable("id") Long id);

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/api/unit/del/batch")
  Result<Boolean> deleteBatch(@RequestBody List<Long> ids);
}
