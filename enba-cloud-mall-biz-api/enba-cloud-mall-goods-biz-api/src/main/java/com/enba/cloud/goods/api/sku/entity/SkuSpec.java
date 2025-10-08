package com.enba.cloud.goods.api.sku.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_sku_spec")
@ApiModel(value = "SkuSpec对象", description = "")
@Data
public class SkuSpec implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("spu的id")
  private Long spuId;

  @ApiModelProperty("属性名称")
  private String specName;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;
}
