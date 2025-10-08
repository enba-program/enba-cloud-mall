package com.enba.cloud.users.api.client;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.users.api.factory.UserAddressClientFallbackFactory;
import com.enba.cloud.users.api.req.SetDefaultAddressReq;
import com.enba.cloud.users.api.req.UserAddressReq;
import com.enba.cloud.users.api.resp.UserAddressResp;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    contextId = "UserAddressClient",
    name = ServiceAppConstants.USER_SERVICE_NAME,
    fallbackFactory = UserAddressClientFallbackFactory.class)
@ResponseBody
public interface UserAddressClient {

  @PostMapping("/api/address/add")
  @ApiOperation("新增收货地址")
  Result<Boolean> addAddress(
      @RequestBody @Validated UserAddressReq req, @RequestParam(name = "userId") Long userId);

  @PutMapping("/api/address/update")
  @ApiOperation("编辑收货地址")
  Result<Boolean> updateAddress(
      @RequestBody @Validated UserAddressReq req, @RequestParam(name = "userId") Long userId);

  @DeleteMapping("/api/address/delete/{id}")
  @ApiOperation("删除收货地址")
  Result<Boolean> deleteAddress(
      @PathVariable("id") Long id, @RequestParam(name = "userId") Long userId);

  @GetMapping("/api/address/{id}")
  @ApiOperation("查看收货地址详情")
  Result<UserAddressResp> getAddress(
      @PathVariable("id") Long id, @RequestParam(name = "userId") Long userId);

  @PutMapping("/api/address/default")
  @ApiOperation("设置默认收货地址&取消默认收货地址")
  Result<Boolean> setDefaultAddress(
      @RequestBody SetDefaultAddressReq req, @RequestParam(name = "userId") Long userId);

  @GetMapping("/api/address/list")
  @ApiOperation("获取用户所有收货地址")
  Result<List<UserAddressResp>> getUserAddresses(@RequestParam(name = "userId") Long userId);
}
