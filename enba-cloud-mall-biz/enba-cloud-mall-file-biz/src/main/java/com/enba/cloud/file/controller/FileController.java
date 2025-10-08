package com.enba.cloud.file.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.enums.FileBizCodeEnum;
import com.enba.cloud.file.api.client.FileCommonClient;
import com.enba.cloud.file.manager.FileUploadManager;
import java.util.Set;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 恩爸编程
 * @since 2025-05-26
 */
@RestController
public class FileController implements FileCommonClient {

  @Override
  public Result<Long> upload(MultipartFile multipartFile, String bizCode) {
    return Result.success(
        FileUploadManager.upload(multipartFile, FileBizCodeEnum.getByBizCode(bizCode)));
  }

  @Override
  public Result<Boolean> updateFileBizId(Set<Long> fileIdSet, Long bizId) {
    FileUploadManager.updateFileBizId(fileIdSet, bizId);
    return Result.success(Boolean.TRUE);
  }
}
