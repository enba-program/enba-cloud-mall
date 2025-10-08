package com.enba.cloud.users.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_user_address")
public class UserAddress {
  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  // 用户ID
  private Long userId;

  // 收货人
  private String receiverName;

  // 手机号码
  private String phone;

  // 省份
  private String province;

  // 城市
  private String city;

  // 区域
  private String district;

  // 乡镇
  private String town;

  // 详细地址
  private String addressDetail;

  // 邮编
  private String postalCode;

  // 是否默认
  @TableField("is_default")
  private Integer isDefault;

  // 标签
  private String tag;

  // 经度
  private BigDecimal latitude;

  // 纬度
  private BigDecimal longitude;

  // 是否删除  0-正常 1-删除
  private Integer deleted;

  // 创建时间
  private LocalDateTime createTime;

  // 更新时间
  private LocalDateTime updateTime;
}
