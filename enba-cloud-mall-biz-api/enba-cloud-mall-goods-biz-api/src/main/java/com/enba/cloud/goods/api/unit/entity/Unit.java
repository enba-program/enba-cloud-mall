package com.enba.cloud.goods.api.unit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 单位
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_unit")
@ApiModel(value = "Unit对象", description = "单位")
@Data
public class Unit implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("单位名称")
  private String unitName;

  @ApiModelProperty("状态 1启用 0禁用")
  private Integer status;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty("0未删除 1删除")
  private Integer deleted;
}
