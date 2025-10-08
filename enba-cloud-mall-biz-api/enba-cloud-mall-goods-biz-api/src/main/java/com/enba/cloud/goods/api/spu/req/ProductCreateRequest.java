package com.enba.cloud.goods.api.spu.req;

import com.enba.boot.core.exception.BizException;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCreateRequest {

  @ApiModelProperty(value = "SPU信息", required = true)
  @NotNull(message = "SPU信息不能为空")
  @Valid
  private SpuInfoRequest spuInfo;

  @ApiModelProperty(value = "SKU信息列表，规格列表", required = true)
  @NotNull(message = "SKU信息列表不能为空")
  @Size(min = 1, message = "SKU信息列表不能为空")
  @Valid
  private List<SkuInfo> skuList;

  @ApiModelProperty(value = "规格列表", required = true)
  @NotNull(message = "规格列表不能为空")
  @Size(min = 1, message = "规格列表不能为空")
  @Valid
  private List<SpecInfo> specList;

  @Data
  public static class SpuInfoRequest {
    // SPU信息
    @ApiModelProperty(value = "单位ID", required = true)
    @NotNull(message = "单位ID不能为空")
    private Long unitId;

    @ApiModelProperty(value = "品牌ID", required = true)
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;

    @ApiModelProperty(value = "分类ID", required = true)
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "spu名称", required = true)
    @NotBlank(message = "spu名称不能为空")
    private String spuName;

    @ApiModelProperty(value = "商品描述", required = true)
    @NotBlank(message = "商品描述不能为空")
    private String spuDesc;

    @ApiModelProperty(value = "spu主图", required = true)
    @NotBlank(message = "spu主图不能为空")
    private String headerPicUrl;

    @ApiModelProperty(value = "spu主图Id", required = true)
    @NotNull(message = "spu主图Id不能为空")
    private Long headerPicId;

    @ApiModelProperty(value = "spu详情图", required = true)
    @NotNull(message = "spu详情图不能为空")
    @Size(min = 1, message = "spu详情图不能为空")
    private Set<Long> spuDetailPicIdSet;

    /**
     * @see com.enba.mallapi.enums.SpuIsSpuEnum
     */
    @ApiModelProperty(value = "是否是SPU商品(0-否,1-是)", required = true)
    @NotNull(message = "是否是SPU商品不能为空")
    private Integer isSpu;

    /**
     * @see com.enba.mallapi.enums.SpuisRefundEnum
     */
    @ApiModelProperty(value = "0不可退换 1可退换", required = true)
    @NotNull(message = "是否可退换不能为空")
    private Integer isRefund;

    /**
     * @see com.enba.mallapi.enums.SpuIsPostEnum
     */
    @ApiModelProperty(value = "0不包邮 1包邮", required = true)
    @NotNull(message = "是否包邮不能为空")
    private Integer isPost;

    @ApiModelProperty(value = "安装清单", required = true)
    @NotBlank(message = "安装清单不能为空")
    private String installList;

    @ApiModelProperty(value = "规格包装", required = true)
    @NotBlank(message = "规格包装不能为空")
    private String specPackage;

    @ApiModelProperty(value = "售后保证", required = true)
    @NotBlank(message = "售后保证不能为空")
    private String postSaleGuarantee;

    /**
     * @see com.enba.mallapi.enums.SpuShelveStatusEnum
     */
    @ApiModelProperty(value = "上架状态（1已上架 0已下架 2待上架）", required = true)
    @NotNull(message = "上架状态不能为空")
    private Integer shelveStatus;

    /**
     * @see com.enba.mallapi.enums.SpuAuditStatusEnum
     */
    @ApiModelProperty(value = "审核状态（0待审核 1审核通过 2审核不通过 3:草稿）", required = true)
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;
  }

  @Data
  public static class SkuInfo {
    private Long id;

    @ApiModelProperty(value = "sku名称", required = true)
    @NotBlank(message = "sku名称不能为空")
    private String skuName;

    @ApiModelProperty(value = "sku主图地址", required = true)
    @NotBlank(message = "sku主图地址不能为空")
    private String skuPicUrl;

    @ApiModelProperty(value = "sku主图ID", required = true)
    @NotNull(message = "sku主图ID不能为空")
    private Long skuPicId;

    @ApiModelProperty(value = "最小购买量", required = true)
    @NotNull(message = "最小购买量不能为空")
    @Positive(message = "库存必须大于0")
    private Integer moq;

    @ApiModelProperty(value = "库存", required = true)
    @NotNull(message = "库存不能为空")
    @Positive(message = "库存必须大于0")
    private Integer inventory;

    @ApiModelProperty(value = "单价", required = true)
    @NotNull(message = "单价不能为空")
    @Positive(message = "库存必须大于0")
    private BigDecimal skuPrice;

    @ApiModelProperty(value = "SKU描述", required = true)
    @NotBlank(message = "SKU描述不能为空")
    private String skuDesc;

    @ApiModelProperty(
        value =
            "规格 eg：[{\"valueId\":111,\"attrId\":222,\"valueName\":\"参数值名称\",\"attrName\":\"参数名称\"}]",
        required = true)
    @NotBlank(message = "规格不能为空")
    private String skuSpecEnums;

    /**
     * @see com.enba.mallapi.enums.SkuStatusEnum
     */
    @ApiModelProperty(value = "是否生效（1生效中 0失效）", required = true)
    @NotNull(message = "是否生效不能为空")
    private Integer status;
  }

  @Data
  public static class SpecInfo {
    private Long id;

    @ApiModelProperty(value = "参数名称", required = true)
    @NotBlank(message = "参数名称不能为空")
    private String specName;

    @ApiModelProperty(value = "参数值", required = true)
    @NotNull(message = "参数值不能为空")
    @Size(min = 1, message = "参数值不能为空")
    @Valid
    private List<SpecValue> specValueList;
  }

  @Data
  public static class SpecValue {
    private Long id;

    @ApiModelProperty(value = "参数值", required = true)
    @NotBlank(message = "参数值不能为空")
    private String specValue;

    @ApiModelProperty("参数名称ID")
    private Long specId;
  }

  /** 1. 检查参数名称重复 2. 检查同一参数下参数值重复 */
  public void checkSpectNameRepeat() {
    int size = this.specList.size();
    long count = this.specList.stream().map(SpecInfo::getSpecName).distinct().count();
    if (size != count) {
      BizException.throwEx("参数名称禁止重复");
    }

    for (SpecInfo specInfo : this.specList) {
      size = specInfo.getSpecValueList().size();
      count = specInfo.getSpecValueList().stream().map(SpecValue::getSpecValue).distinct().count();
      if (size != count) {
        BizException.throwEx("同一参数下参数值禁止重复");
      }
    }
  }
}
