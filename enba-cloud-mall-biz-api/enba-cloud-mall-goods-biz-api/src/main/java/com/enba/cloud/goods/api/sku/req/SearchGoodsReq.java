package com.enba.cloud.goods.api.sku.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchGoodsReq {

  @ApiModelProperty(value = "关键字")
  private String keyword;

  @ApiModelProperty(value = "页码")
  private Integer pageNum;

  @ApiModelProperty(value = "页大小")
  private Integer pageSize;

  @ApiModelProperty(value = "排序字段")
  private String field;

  @ApiModelProperty(value = "排序方式")
  private String order;

  private Long brandId;

  public SearchGoodsReq() {
    this.pageNum = 1;
    this.pageSize = 10;
    if (keyword == null) keyword = "";
    if (field == null) field = "createTime";
    if (order == null) order = "asc";
  }

  /** 检查查询字段是否支持 */
  public void checkSearchField() {}
}
