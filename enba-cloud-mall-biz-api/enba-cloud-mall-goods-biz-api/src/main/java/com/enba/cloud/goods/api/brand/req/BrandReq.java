package com.enba.cloud.goods.api.brand.req;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 品牌
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Data
public class BrandReq implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("品牌名称")
  @NotBlank(message = "品牌名称不能为空")
  private String brandName;

  @ApiModelProperty("品牌图片地址")
  @NotBlank(message = "品牌图片地址不能为空")
  private String brandImage;

  @ApiModelProperty("状态 1启用 0禁用")
  @NotNull(message = "状态不能为空")
  private Integer status;
}
