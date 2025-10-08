package com.enba.cloud.goods.api.spu.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.enba.cloud.common.enums.SpuAuditStatusEnum;
import com.enba.cloud.common.enums.SpuShelveStatusEnum;
import com.enba.cloud.goods.api.spu.bo.SpecNameValueBo;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ProductDetailResponse {

  @ApiModelProperty("spu信息")
  private SpuInfoResp spuInfo;

  @ApiModelProperty("sku列表,规格列表")
  private List<SkuInfoResp> skuList;

  @ApiModelProperty("参数列表")
  private List<SpecDetail> specList;

  /** 填充skuId和参数值SpecValueId的映射关系 */
  @ApiModelProperty("根据参数值SpecValueId找到对应规格skuId eg: 111,2222,33=>4444")
  public Map<String, Long> getSpecValueIdMap() {
    Map<String, Long> specValueSkuMap = new HashMap();

    this.skuList.forEach(
        skuInfoResp -> {
          StringBuilder specValueIds = new StringBuilder();
          for (SpecNameValueBo bo :
              JSON.parseArray(skuInfoResp.getSkuSpecEnums(), SpecNameValueBo.class)) {
            specValueIds.append(bo.getValueId()).append(":");
          }

          specValueSkuMap.put(specValueIds.toString(), skuInfoResp.getId());
        });

    return specValueSkuMap;
  }

  @Data
  public static class SpuInfoResp {
    // SPU信息
    private Long id;

    @ApiModelProperty("商品单位ID")
    private Long unitId;

    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("spu编码")
    private String spuCode;

    @ApiModelProperty("spu名称")
    private String spuName;

    @ApiModelProperty("商品描述")
    private String spuDesc;

    @ApiModelProperty("spu主图")
    private String headerPicUrl;

    @ApiModelProperty("默认展示sku")
    private Long defaultSkuId;

    @ApiModelProperty("是否是SPU商品(0-否,1-是)")
    private Integer isSpu;

    @ApiModelProperty("0不可退换 1可退换")
    private Integer isRefund;

    @ApiModelProperty("0不包邮 1包邮")
    private Integer isPost;

    @ApiModelProperty("安装清单")
    private String installList;

    @ApiModelProperty("规格包装")
    private String specPackage;

    @ApiModelProperty("售后保证")
    private String postSaleGuarantee;

    @ApiModelProperty("上架状态（1已上架 0已下架 2待上架）")
    private Integer shelveStatus;

    @ApiModelProperty("审核状态（0待审核 1审核通过 2审核不通过 3草稿）")
    private Integer auditStatus;

    @ApiModelProperty("上架状态中文描述")
    public String getShelveStatusText() {
      return SpuShelveStatusEnum.getByShelveStatus(shelveStatus).getName();
    }

    @ApiModelProperty("审核状态中文描述")
    public String getAuditStatusText() {
      return SpuAuditStatusEnum.getByAuditStatus(auditStatus).getName();
    }

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
  }

  @Data
  public static class SkuInfoResp {
    private Long id;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("sku主图地址")
    private String skuPicUrl;

    @ApiModelProperty("最小购买量")
    private Integer moq;

    @ApiModelProperty("库存")
    private Integer inventory;

    @ApiModelProperty("单价")
    private BigDecimal skuPrice;

    @ApiModelProperty(
        "规格 eg：[{\"valueId\":111,\"attrId\":222,\"valueName\":\"参数值名称\",\"attrName\":\"参数名称\"}]")
    private String skuSpecEnums;

    @ApiModelProperty("sku属性值")
    public String getSpecValueName() {
      StringBuilder specValueName = new StringBuilder();
      for (Object o : JSON.parseArray(skuSpecEnums)) {
        JSONObject skuSpecObject = JSONObject.parseObject(JSONObject.toJSONString(o));

        String attrName = skuSpecObject.getString("attrName");
        String valueName = skuSpecObject.getString("valueName");
        specValueName.append(attrName).append(":").append(valueName);
      }

      return specValueName.toString();
    }

    @ApiModelProperty("sku属性Id")
    public String getSpecValueId() {
      StringBuilder specValueId = new StringBuilder();
      for (Object o : JSON.parseArray(skuSpecEnums)) {
        JSONObject skuSpecObject = JSONObject.parseObject(JSONObject.toJSONString(o));

        String valueId = skuSpecObject.getString("valueId");

        specValueId.append(valueId).append(":");
      }

      return specValueId.toString();
    }

    @ApiModelProperty("规格MD5,方便编辑操作")
    private String skuSpecMd5;

    @ApiModelProperty("sku描述")
    private String skuDesc;

    @ApiModelProperty("是否生效（1生效中 0失效）")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
  }

  @Data
  public static class SpecDetail {
    private Long id;

    @ApiModelProperty("规格名称")
    private String specName;

    @ApiModelProperty("规格值")
    private List<SpecValue> values;
  }

  @Data
  public static class SpecValue {
    private Long id;

    @ApiModelProperty("规格名称")
    private String specName;

    @ApiModelProperty("规格值")
    private String specValue;

    private Long specId; // 规格ID
  }
}
