package com.enba.cloud.goods.api.category.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 类目
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_category")
@ApiModel(value = "Category对象", description = "类目")
@Data
public class Category implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("父级品类ID")
  private Long parentId;

  @ApiModelProperty("品类编码")
  private String categoryCode;

  @ApiModelProperty("品类路径（1,2,3）")
  private String categoryPath;

  @ApiModelProperty("品类层级")
  private Integer categoryLevel;

  @ApiModelProperty("品类名称")
  private String categoryName;

  @ApiModelProperty("品类对应的图片URL")
  private String categoryIconUrl;

  @ApiModelProperty("状态 1启用 0禁用")
  private Integer status;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty("0未删除 1删除")
  private Integer deleted;
}
