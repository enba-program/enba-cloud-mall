package com.enba.cloud.users.api.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserAddressResp {
  private Long id;

  // 收货人
  @ApiModelProperty("收货人")
  private String receiverName;

  // 手机号码
  @ApiModelProperty("手机号码")
  private String phone;

  // 省份
  @ApiModelProperty("省份")
  private String province;

  // 城市
  @ApiModelProperty("城市")
  private String city;

  // 区域
  @ApiModelProperty("区域")
  private String district;

  // 乡镇
  @ApiModelProperty("乡镇")
  private String town;

  // 详细地址
  @ApiModelProperty("详细地址")
  private String addressDetail;

  // 邮编
  @ApiModelProperty("邮编")
  private String postalCode;

  // 是否默认
  @ApiModelProperty("是否默认地址(0-否,1-是)")
  private Integer isDefault;

  // 标签
  @ApiModelProperty("标签")
  private String tag;

  // 经度
  @ApiModelProperty("经度")
  private BigDecimal latitude;

  // 纬度
  @ApiModelProperty("纬度")
  private BigDecimal longitude;

  // 创建时间
  @ApiModelProperty("创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime createTime;

  // 更新时间
  @ApiModelProperty("更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime updateTime;
}
