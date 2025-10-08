package com.enba.cloud.goods.api.spu.resp;


import com.enba.cloud.common.enums.SpuAuditStatusEnum;
import com.enba.cloud.common.enums.SpuShelveStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SpuResp implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @ApiModelProperty("单位id")
  private Long unitId;

  @ApiModelProperty("品牌id")
  private Long brandId;

  @ApiModelProperty("分类id")
  private Long categoryId;

  @ApiModelProperty("spu编码")
  private String spuCode;

  @ApiModelProperty("spu名称")
  private String spuName;

  @ApiModelProperty("描述")
  private String spuDesc;

  @ApiModelProperty("spu主图")
  private String headerPicUrl;

  @ApiModelProperty("默认展示sku")
  private Long defaultSkuId;

  @ApiModelProperty("0单规格 1多规格")
  private Integer isSpu;

  @ApiModelProperty("0不可退换 1可退换")
  private Integer isRefund;

  @ApiModelProperty("0不包邮 1包邮")
  private Integer isPost;

  @ApiModelProperty("安装清单")
  private String installList;

  @ApiModelProperty("规格包装")
  private String specPackage;

  @ApiModelProperty("售后保障")
  private String postSaleGuarantee;

  @ApiModelProperty("上架状态（1已上架 0已下架 2待上架）")
  private Integer shelveStatus;

  @ApiModelProperty("上架状态中文描述")
  public String getShelveStatusText() {
    return SpuShelveStatusEnum.getByShelveStatus(shelveStatus).getName();
  }

  @ApiModelProperty("审核状态（0待审核 1审核通过 2删除不通过 3草稿）")
  private Integer auditStatus;

  @ApiModelProperty("审核状态中文描述")
  public String getAuditStatusText() {
    return SpuAuditStatusEnum.getByAuditStatus(auditStatus).getName();
  }

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;
}
