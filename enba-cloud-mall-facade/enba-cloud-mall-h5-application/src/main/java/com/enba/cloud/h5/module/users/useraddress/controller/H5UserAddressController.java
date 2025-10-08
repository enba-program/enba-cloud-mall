package com.enba.cloud.h5.module.users.useraddress.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.users.api.client.UserAddressClient;
import com.enba.cloud.users.api.req.SetDefaultAddressReq;
import com.enba.cloud.users.api.req.UserAddressReq;
import com.enba.cloud.users.api.resp.UserAddressResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@Api(tags = "用户收货地址管理")
public class H5UserAddressController {

  @Autowired private UserAddressClient userAddressClient;

  @PostMapping("/add")
  @ApiOperation("新增收货地址")
  public Result<Boolean> addAddress(
      @RequestBody @Validated UserAddressReq req, @CurrentUser Long userId) {
    return userAddressClient.addAddress(req, userId);
  }

  @PutMapping("/update")
  @ApiOperation("编辑收货地址")
  public Result<Boolean> updateAddress(
      @RequestBody @Validated UserAddressReq req, @CurrentUser Long userId) {

    return userAddressClient.updateAddress(req, userId);
  }

  @DeleteMapping("/delete/{id}")
  @ApiOperation("删除收货地址")
  public Result<Boolean> deleteAddress(@PathVariable Long id, @CurrentUser Long userId) {

    return userAddressClient.deleteAddress(id, userId);
  }

  @GetMapping("/{id}")
  @ApiOperation("查看收货地址详情")
  public Result<UserAddressResp> getAddress(@PathVariable Long id, @CurrentUser Long userId) {

    return userAddressClient.getAddress(id, userId);
  }

  @PutMapping("/default")
  @ApiOperation("设置默认收货地址&取消默认收货地址")
  public Result<Boolean> setDefaultAddress(
      @RequestBody SetDefaultAddressReq req, @CurrentUser Long userId) {

    return userAddressClient.setDefaultAddress(req, userId);
  }

  @GetMapping("/list")
  @ApiOperation("获取用户所有收货地址")
  public Result<List<UserAddressResp>> getUserAddresses(@CurrentUser Long userId) {
    return userAddressClient.getUserAddresses(userId);
  }
}
