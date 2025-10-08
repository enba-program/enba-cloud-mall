package com.enba.cloud.goods.api.brand.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 品牌
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_brand")
@ApiModel(value = "Brand对象", description = "品牌")
@Data
public class Brand implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("品牌名称")
  private String brandName;

  @ApiModelProperty("品牌图片地址")
  private String brandImage;

  @ApiModelProperty("状态 1启用 0禁用")
  private Integer status;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty("0未删除 1删除")
  private int deleted;
}
