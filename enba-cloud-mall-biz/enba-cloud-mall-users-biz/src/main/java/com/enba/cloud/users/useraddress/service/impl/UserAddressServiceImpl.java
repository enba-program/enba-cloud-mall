package com.enba.cloud.users.useraddress.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.boot.core.exception.BizException;
import com.enba.cloud.common.enums.UserAddressDefaultEnum;
import com.enba.cloud.users.api.entity.UserAddress;
import com.enba.cloud.users.api.req.SetDefaultAddressReq;
import com.enba.cloud.users.api.req.UserAddressReq;
import com.enba.cloud.users.api.resp.UserAddressResp;
import com.enba.cloud.users.useraddress.mapper.UserAddressMapper;
import com.enba.cloud.users.useraddress.service.UserAddressService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService {

  @Transactional(rollbackFor = Exception.class)
  @Override
  public Boolean addAddress(UserAddressReq req, Long userId) {
    List<UserAddress> addressesList = getUserAllAddresses(userId);

    if (Objects.isNull(addressesList)) {
      // 头一次添加地址，默认设置为默认地址
      req.setIsDefault(UserAddressDefaultEnum.YES.getStatus());
    } else {
      if (UserAddressDefaultEnum.YES.getStatus().equals(req.getIsDefault())) {
        addressesList.forEach(
            e -> {
              // 全部设置为非默认
              setDefaultAddress(
                  userId,
                  new SetDefaultAddressReq()
                      .setId(e.getId())
                      .setIsDefault(UserAddressDefaultEnum.NO.getStatus()));
            });
      }
    }

    // 复制属性
    UserAddress address = new UserAddress();
    BeanUtils.copyProperties(req, address);

    // 设置用户ID
    address.setId(null);
    address.setUserId(userId);

    // do add
    return save(address);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean setDefaultAddress(Long userId, SetDefaultAddressReq req) {
    // 校验地址是否存在
    UserAddress userAddress = checkExists(req.getId());

    // 校验用户ID
    checkUserId(userId, userAddress);

    // 1. 将该用户所有地址设为非默认
    UpdateWrapper<UserAddress> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("user_id", userId).set("is_default", 0);
    this.update(updateWrapper);

    // 2. 将指定地址设为默认
    UserAddress address = new UserAddress();
    address.setId(req.getId());
    address.setIsDefault(req.getIsDefault());
    return this.updateById(address);
  }

  @Override
  public List<UserAddress> getUserAllAddresses(Long userId) {
    QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
    wrapper.eq("user_id", userId).eq("deleted", 0).orderByDesc("is_default"); // 默认地址排在前面
    return this.list(wrapper);
  }

  // ============

  @Transactional(rollbackFor = Exception.class)
  public Boolean updateAddress(UserAddressReq req, Long userId) {
    // 校验地址是否存在
    UserAddress address = checkExists(req.getId());

    // 校验用户ID
    checkUserId(userId, address);

    // 复制属性
    BeanUtils.copyProperties(req, address);
    address.setUserId(userId);

    // 检查是否设置为了默认地址，如果设置了默认地址，则将该用户所有地址设为非默认
    if (UserAddressDefaultEnum.YES.getStatus().equals(req.getIsDefault())) {
      List<UserAddress> addressesList = getUserAllAddresses(userId);
      addressesList.forEach(
          e ->
              setDefaultAddress(
                  userId,
                  new SetDefaultAddressReq()
                      .setId(e.getId())
                      .setIsDefault(UserAddressDefaultEnum.NO.getStatus())));
    }

    if (UserAddressDefaultEnum.NO.getStatus().equals(req.getIsDefault())) {
      // 检查下是否存在默认地址，必须存在一条默认地址
      List<UserAddress> addressesList = getUserAllAddresses(userId);
      if (addressesList.stream()
          .filter(e -> !Objects.equals(e.getId(), req.getId()))
          .noneMatch(e -> UserAddressDefaultEnum.YES.getStatus().equals(e.getIsDefault()))) {
        BizException.throwEx("请设置一条默认地址");
      }
    }

    // do update
    return updateById(address);
  }

  public Boolean deleteAddress(Long id, Long userId) {
    // 校验地址是否存在
    UserAddress userAddress = checkExists(id);

    // 校验用户ID
    checkUserId(userId, userAddress);

    // do delete
    return removeById(id);
  }

  public UserAddressResp getAddress(Long id, Long userId) {
    // 校验地址是否存在
    UserAddress userAddress = checkExists(id);

    // 校验用户ID
    checkUserId(userId, userAddress);

    return BeanUtil.copyProperties(userAddress, UserAddressResp.class);
  }

  /**
   * 校验地址是否存在
   *
   * @param id 地址ID
   * @return 地址
   */
  private UserAddress checkExists(Long id) {
    Objects.requireNonNull(id, "地址ID不能为空");

    UserAddress address = getById(id);
    if (address == null) {
      BizException.throwEx("地址不存在");
    }

    return address;
  }

  /**
   * 校验用户ID
   *
   * @param userId 上下文用户ID
   * @param address 收货地址
   */
  private void checkUserId(Long userId, UserAddress address) {
    if (!userId.equals(address.getUserId())) {
      BizException.throwEx("无权限操作");
    }
  }
}
