package com.enba.cloud.goods.api.unit.req;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 单位
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Data
public class UnitReq implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("单位名称")
  @NotBlank(message = "单位名称不能为空")
  private String unitName;

  @ApiModelProperty("状态 1启用 0禁用")
  @NotNull(message = "状态不能为空")
  private Integer status;
}
