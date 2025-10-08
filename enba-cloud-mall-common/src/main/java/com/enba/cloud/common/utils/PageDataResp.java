package com.enba.cloud.common.utils;

import java.util.function.Function;
import lombok.Data;

@Data
public class PageDataResp {

  private int pageNum;
  private int pageSize;
  private long total;
  private Object dataList;

  /**
   * 转换数据
   *
   * @param func 转换器
   */
  public void transformData(Function<Object,Object> func) {
    this.dataList = func.apply(dataList);
  }
}
