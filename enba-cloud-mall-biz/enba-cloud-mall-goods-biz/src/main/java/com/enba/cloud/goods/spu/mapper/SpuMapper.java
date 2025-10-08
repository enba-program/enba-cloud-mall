package com.enba.cloud.goods.spu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enba.cloud.goods.api.spu.bo.EsGoodsBo;
import com.enba.cloud.goods.api.spu.entity.Spu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * SPU表 Mapper 接口
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Mapper
public interface SpuMapper extends BaseMapper<Spu> {

  /**
   * @param spuIdList
   * @param pageNo
   * @param pageSize
   * @return
   */
  List<EsGoodsBo> getPageSynchAll(
      @Param("spuIdList") List<Long> spuIdList,
      @Param("pageNo") int pageNo,
      @Param("pageSize") int pageSize);
}
