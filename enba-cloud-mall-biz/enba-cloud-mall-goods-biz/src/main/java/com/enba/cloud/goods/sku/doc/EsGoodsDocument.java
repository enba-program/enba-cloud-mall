package com.enba.cloud.goods.sku.doc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "goods_doc", createIndex = true)
public class EsGoodsDocument {
  @Id private Long esId;

  /** 创建时间 */
  @Field(
      type = FieldType.Date,
      format = DateFormat.date_hour_minute_second,
      pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  /** 品牌ID */
  @Field(type = FieldType.Long)
  private Long brandId;

  /** 品牌名称 */
  @Field(type = FieldType.Keyword)
  private String brandName;

  /** 品牌图片地址 */
  @Field(index = false)
  private String brandImage;

  /** 分类ID */
  @Field(type = FieldType.Long)
  private Long categoryId;

  /** 分类名称 */
  @Field(type = FieldType.Keyword)
  private String categoryName;

  /** 分类编码 */
  @Field(type = FieldType.Keyword)
  private String categoryCode;

  /** 品类对应的图片URL */
  @Field(index = false)
  private String categoryIconUrl;

  /** 单位ID */
  @Field(type = FieldType.Long)
  private Long unitId;

  /** 单位名称 */
  @Field(type = FieldType.Keyword)
  private String unitName;

  /** SPUID */
  @Field(type = FieldType.Long)
  private Long spuId;

  /** spu编码 */
  @Field(type = FieldType.Keyword)
  private String spuCode;

  /** spu名称 */
  @Field(type = FieldType.Keyword)
  private String spuName;

  /** spu描述 */
  @Field(index = false)
  private String spuDesc;

  /** spu主图 */
  @Field(index = false)
  private String headerPicUrl;

  /** 默认展示sku */
  @Field(index = false)
  private Long defaultSkuId;

  /** 0单规格 1多规格 */
  @Field(type = FieldType.Integer)
  private Integer isSpu;

  /** 0不可退换 1可退换 */
  @Field(type = FieldType.Integer)
  private Integer isRefund;

  /** 0不包邮 1包邮 */
  @Field(type = FieldType.Integer)
  private Integer isPost;

  /** 安装清单 */
  @Field(index = false)
  private String installList;

  /** 规格包装 */
  @Field(index = false)
  private String specPackage;

  /** 售后保障 */
  @Field(index = false)
  private String postSaleGuarantee;

  /** 上架状态（1已上架 0已下架 2待上架） */
  @Field(type = FieldType.Integer)
  private Integer shelveStatus;

  /** SKU列表 */
  @Field(type = FieldType.Nested)
  private List<EsSkuListDoc> skuList;

  /** 搜索关键字 */
  @Field(type = FieldType.Text, analyzer = "ik_smart")
  private String searchKeyword1;

  /** 搜索关键字 */
  @Field(type = FieldType.Text, analyzer = "ik_max_word")
  private String searchKeyword2;

  // 设计searchKeyword1 ，searchKeyword2是为了灵活应对搜索关键字

  @Data
  public static class EsSkuListDoc {

    /** sku编码 */
    @Field(type = FieldType.Keyword)
    private String skuCode;

    /** sku名称 */
    @Field(type = FieldType.Keyword)
    private String skuName;

    /** 最小购买量 */
    @Field(type = FieldType.Integer)
    private Integer moq;

    /** 库存 */
    @Field(type = FieldType.Integer)
    private Integer inventory;

    /** 单价 */
    @Field(type = FieldType.Double)
    private BigDecimal skuPrice;

    /** 规格 */
    @Field(index = false)
    private String skuSpecEnums;

    /** sku描述 */
    @Field(index = false)
    private String skuDesc;

    /** 是否生效（1生效中 0失效） */
    @Field(type = FieldType.Integer)
    private Integer status;
  }
}
