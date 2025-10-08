package com.enba.cloud.goods.api.category.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryUpdateStatusReq {

  @ApiModelProperty("品类ID")
  @NotNull(message = "品类ID不能为空")
  private Long id;

  @ApiModelProperty("状态 1启用 0禁用")
  @NotNull(message = "状态不能为空")
  private Integer status;
}
