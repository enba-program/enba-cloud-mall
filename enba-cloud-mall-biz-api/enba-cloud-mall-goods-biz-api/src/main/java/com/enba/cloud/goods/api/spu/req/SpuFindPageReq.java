package com.enba.cloud.goods.api.spu.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpuFindPageReq {
  @ApiModelProperty("页码")
  @NotNull(message = "页码不能为空")
  private Integer pageNum;

  @ApiModelProperty("每页数量")
  @NotNull(message = "每页数量不能为空")
  private Integer pageSize;

  @ApiModelProperty("spu编码")
  private String spuCode;

  @ApiModelProperty("spu名称")
  private String spuName;

  @ApiModelProperty("品牌ID")
  private Long brandId;

  @ApiModelProperty("单位ID")
  private Long unitId;

  @ApiModelProperty("分类ID")
  private Long categoryId;

  /**
   * @see com.enba.mallapi.enums.SpuIsSpuEnum
   */
  @ApiModelProperty("0单规格 1多规格")
  private Integer isSpu;

  /**
   * @see com.enba.mallapi.enums.SpuisRefundEnum
   */
  @ApiModelProperty("是否可退换(0-不可退换,1-可退换)")
  private Integer isRefund;

  /**
   * @see com.enba.mallapi.enums.SpuIsPostEnum
   */
  @ApiModelProperty("是否包邮(0-不包邮,1-包邮)")
  private Integer isPost;

  /**
   * @see com.enba.mallapi.enums.SpuShelveStatusEnum
   */
  @ApiModelProperty("上架状态（1已上架 0已下架 2待上架）")
  private Integer shelveStatus;

  /**
   * @see com.enba.mallapi.enums.SpuAuditStatusEnum
   */
  @ApiModelProperty("审核状态（0待审核 1审核通过 2审核不通过 3草稿）")
  private Integer auditStatus;
}
