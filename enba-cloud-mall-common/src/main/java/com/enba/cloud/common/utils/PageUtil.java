package com.enba.cloud.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class PageUtil {

  /**
   * 获取分页数据
   *
   * @param supplier 数据源
   * @param targetType 目标类型
   * @param <pojo> pojo类型
   * @param <resp> 响应类型
   * @return 分页数据
   */
  public static <pojo, resp> Page<resp> getPage(
      Supplier<Page<pojo>> supplier, Class<resp> targetType) {
    Page<resp> spuPageRespPage = new Page<>();

    Page<pojo> pojoPage = supplier.get();

    List<resp> respList = BeanUtil.copyToList(pojoPage.getRecords(), targetType);
    BeanUtils.copyProperties(pojoPage, spuPageRespPage);
    spuPageRespPage.setRecords(respList);

    return spuPageRespPage;
  }
}
