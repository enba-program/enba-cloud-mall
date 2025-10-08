package com.enba.cloud.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.enba.cloud.file.api.entity.File;
import com.enba.cloud.file.mapper.FileMapper;
import com.enba.cloud.file.service.IFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
