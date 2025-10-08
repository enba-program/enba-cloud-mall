package com.enba.cloud.goods.unit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.cloud.goods.api.unit.entity.Unit;
import com.enba.cloud.goods.unit.mapper.UnitMapper;
import com.enba.cloud.goods.unit.service.IUnitService;
import org.springframework.stereotype.Service;

/**
 * 单位 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {}
