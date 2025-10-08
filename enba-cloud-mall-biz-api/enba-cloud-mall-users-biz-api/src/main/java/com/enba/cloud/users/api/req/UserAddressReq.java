package com.enba.cloud.users.api.req;

import cn.hutool.core.lang.RegexPool;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserAddressReq {
  private Long id;

  // 收货人
  @ApiModelProperty("收货人")
  @NotBlank(message = "收货人不能为空")
  private String receiverName;

  // 手机号码
  @ApiModelProperty("手机号码")
  @NotBlank(message = "手机号码不能为空")
  @Pattern(regexp = RegexPool.MOBILE, message = "⼿机号格式错误")
  private String phone;

  // 省份
  @ApiModelProperty("省份")
  @NotBlank(message = "省份不能为空")
  private String province;

  // 城市
  @ApiModelProperty("城市")
  @NotBlank(message = "城市不能为空")
  private String city;

  // 区域
  @ApiModelProperty("区域")
  @NotBlank(message = "区域不能为空")
  private String district;

  // 乡镇
  @ApiModelProperty("乡镇")
  private String town;

  // 详细地址
  @ApiModelProperty("详细地址")
  @NotBlank(message = "详细地址不能为空")
  private String addressDetail;

  // 邮编
  @ApiModelProperty("邮编")
  private String postalCode;

  // 是否默认
  @TableField("is_default")
  @ApiModelProperty("是否默认地址(0-否,1-是)")
  @NotNull(message = "是否默认不能为空")
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
}
