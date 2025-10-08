package com.enba.cloud.goods.brand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enba.cloud.goods.api.brand.entity.Brand;
import com.enba.cloud.goods.brand.mapper.BrandMapper;
import com.enba.cloud.goods.brand.service.IBrandService;
import org.springframework.stereotype.Service;

/**
 * 品牌 服务实现类
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {}
