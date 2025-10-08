package com.enba.cloud.users.useraddress.controller;

import cn.hutool.core.bean.BeanUtil;
import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.users.api.client.UserAddressClient;
import com.enba.cloud.users.api.entity.UserAddress;
import com.enba.cloud.users.api.req.SetDefaultAddressReq;
import com.enba.cloud.users.api.req.UserAddressReq;
import com.enba.cloud.users.api.resp.UserAddressResp;
import com.enba.cloud.users.useraddress.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "用户收货地址管理")
public class UserAddressController implements UserAddressClient {

  private final UserAddressService userAddressService;

  public UserAddressController(UserAddressService userAddressService) {
    this.userAddressService = userAddressService;
  }

  @Override
  @ApiOperation("新增收货地址")
  public Result<Boolean> addAddress(
      @RequestBody @Validated UserAddressReq req, @CurrentUser Long userId) {

    return Result.success(userAddressService.addAddress(req, userId));
  }

  @Override
  @ApiOperation("编辑收货地址")
  public Result<Boolean> updateAddress(
      @RequestBody @Validated UserAddressReq req, @CurrentUser Long userId) {

    return Result.success(userAddressService.updateAddress(req, userId));
  }

  @Override
  @ApiOperation("删除收货地址")
  public Result<Boolean> deleteAddress(@PathVariable Long id, @CurrentUser Long userId) {

    return Result.success(userAddressService.deleteAddress(id, userId));
  }

  @Override
  @ApiOperation("查看收货地址详情")
  public Result<UserAddressResp> getAddress(@PathVariable Long id, @CurrentUser Long userId) {

    return Result.success(userAddressService.getAddress(id, userId));
  }

  @Override
  @ApiOperation("设置默认收货地址&取消默认收货地址")
  public Result<Boolean> setDefaultAddress(
      @RequestBody SetDefaultAddressReq req, @CurrentUser Long userId) {

    return Result.success(userAddressService.setDefaultAddress(userId, req));
  }

  @Override
  @ApiOperation("获取用户所有收货地址")
  public Result<List<UserAddressResp>> getUserAddresses(@CurrentUser Long userId) {
    List<UserAddress> addressesList = userAddressService.getUserAllAddresses(userId);

    if (Objects.isNull(addressesList)) {
      return Result.success();
    }

    return Result.success(
        addressesList.stream()
            .map(e -> BeanUtil.copyProperties(e, UserAddressResp.class))
            .collect(Collectors.toList()));
  }
}
