package com.enba.cloud.goods.api.spu.bo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class EsGoodsBo {

  private Long spuId;

  // 品牌信息

  /** 品牌ID */
  private Long brandId;

  /** 品牌名称 */
  private String brandName;

  /** 品牌图片地址 */
  private String brandImage;

  // 类目信息

  /** 分类ID */
  private Long categoryId;

  /** 分类名称 */
  private String categoryName;

  /** 分类编码 */
  private String categoryCode;

  /** 品类对应的图片URL */
  private String categoryIconUrl;

  //  单位信息

  /** 单位ID */
  private Long unitId;

  /** 单位名称 */
  private String unitName;

  //  sku信息
  /** sku编码 */
  private String skuCode;

  /** sku名称 */
  private String skuName;

  /** 最小购买量 */
  private Integer moq;

  /** 库存 */
  private Integer inventory;

  /** 单价 */
  private BigDecimal skuPrice;

  /** 规格 */
  private String skuSpecEnums;

  /** sku描述 */
  private String skuDesc;

  /** 是否生效（1生效中 0失效） */
  private Integer status;
}
