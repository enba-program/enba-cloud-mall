package com.enba.cloud.goods.api.category.req;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 类目
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Data
public class CategoryReq implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("父级品类ID")
  @NotNull(message = "父级品类ID不能为空")
  private Long parentId;

  @ApiModelProperty("品类名称")
  @NotBlank(message = "品类名称不能为空")
  private String categoryName;

  @ApiModelProperty("品类对应的图片URL")
  private String categoryIconUrl;

  @ApiModelProperty("状态 1启用 0禁用")
  @NotNull(message = "状态不能为空")
  private Integer status;
}
