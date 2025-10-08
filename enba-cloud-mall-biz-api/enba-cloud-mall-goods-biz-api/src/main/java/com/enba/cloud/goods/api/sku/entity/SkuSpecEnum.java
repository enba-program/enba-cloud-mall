package com.enba.cloud.goods.api.sku.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_sku_spec_enum")
@ApiModel(value = "SkuSpecEnum对象", description = "")
@Data
public class SkuSpecEnum implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("属性id")
  private Long specId;

  @ApiModelProperty("属性值")
  private String specValue;
}
