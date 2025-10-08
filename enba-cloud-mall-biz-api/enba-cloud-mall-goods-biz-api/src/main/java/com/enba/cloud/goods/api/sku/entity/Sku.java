package com.enba.cloud.goods.api.sku.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * SKU表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_sku")
@ApiModel(value = "Sku对象", description = "SKU表")
@Data
public class Sku implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("spu的id")
  private Long spuId;

  @ApiModelProperty("sku编码")
  private String skuCode;

  @ApiModelProperty("sku名称")
  private String skuName;

  @ApiModelProperty("sku主图地址")
  private String skuPicUrl;

  @ApiModelProperty("最小购买量")
  private Integer moq;

  @ApiModelProperty("库存")
  private Integer inventory;

  @ApiModelProperty("单价")
  private BigDecimal skuPrice;

  @ApiModelProperty(
      "规格 eg：[{\"valueId\":111,\"attrId\":222,\"valueName\":\"参数值名称\",\"attrName\":\"参数名称\"}]")
  private String skuSpecEnums;

  @ApiModelProperty("规格MD5,方便编辑操作")
  private String skuSpecMd5;

  @ApiModelProperty("sku描述")
  private String skuDesc;

  @ApiModelProperty("是否生效（1生效中 0失效）")
  private Integer status;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;
}
