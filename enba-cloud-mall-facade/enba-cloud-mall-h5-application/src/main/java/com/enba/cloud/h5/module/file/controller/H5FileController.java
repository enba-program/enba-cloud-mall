package com.enba.cloud.h5.module.file.controller;

import com.enba.boot.core.base.Result;
import com.enba.cloud.file.api.client.FileCommonClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 恩爸编程
 * @since 2025-05-26
 */
@RestController
@RequestMapping("/api/file")
public class H5FileController {

  @Autowired private FileCommonClient fileCommonClient;

  @ApiOperation("文件上传")
  @PostMapping("/upload")
  public Result<Long> upload(
      @RequestParam(name = "file") MultipartFile multipartFile,
      @RequestParam(name = "bizCode") String bizCode) {
    return fileCommonClient.upload(multipartFile, bizCode);
  }
}
