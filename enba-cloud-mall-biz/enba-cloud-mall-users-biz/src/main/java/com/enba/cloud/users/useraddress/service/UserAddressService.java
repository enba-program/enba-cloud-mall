package com.enba.cloud.users.useraddress.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enba.cloud.users.api.entity.UserAddress;
import com.enba.cloud.users.api.req.SetDefaultAddressReq;
import com.enba.cloud.users.api.req.UserAddressReq;
import com.enba.cloud.users.api.resp.UserAddressResp;
import java.util.List;

public interface UserAddressService extends IService<UserAddress> {

  /**
   * 新增地址
   *
   * @param req 地址信息
   * @param userId 用户ID
   * @return 操作结果
   */
  Boolean addAddress(UserAddressReq req, Long userId);

  /**
   * 修改地址
   *
   * @param req 地址信息
   * @param userId 用户ID
   * @return 操作结果
   */
  Boolean updateAddress(UserAddressReq req, Long userId);

  /**
   * 删除地址
   *
   * @param id 地址ID
   * @param userId 用户ID
   * @return 操作结果
   */
  Boolean deleteAddress(Long id, Long userId);

  /**
   * 获取地址详情
   *
   * @param id 地址ID
   * @param userId 用户ID
   * @return 地址详情
   */
  UserAddressResp getAddress(Long id, Long userId);

  /**
   * 设置默认地址
   *
   * @param userId 用户ID
   * @param req 地址ID
   * @return 操作结果
   */
  boolean setDefaultAddress(Long userId, SetDefaultAddressReq req);

  /**
   * 获取用户所有地址(包含默认标记)
   *
   * @param userId 用户ID
   * @return 地址列表
   */
  List<UserAddress> getUserAllAddresses(Long userId);
}
