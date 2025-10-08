package com.enba.cloud.file.api.factory;

import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.file.api.client.FileCommonClient;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileCommonClientFallbackFactory implements FallbackFactory<FileCommonClient> {

  private static final Logger log = LoggerFactory.getLogger(FileCommonClientFallbackFactory.class);

  @Override
  public FileCommonClient create(Throwable cause) {
    return new FileCommonClient() {
      @Override
      public Result<Long> upload(MultipartFile multipartFile, String bizCode) {
        log.error("FileCommonClient.upload调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "FileCommonClient upload error");
      }

      @Override
      public Result<Boolean> updateFileBizId(Set<Long> fileIdSet, Long bizId) {
        log.error("FileCommonClient.updateFileBizId调用异常: {} ", cause.getMessage(), cause);
        return Result.err(StatusEnum.ERR.getCode(), "FileCommonClient updateFileBizId error");
      }
    };
  }
}
