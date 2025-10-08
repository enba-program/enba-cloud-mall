package com.enba.cloud.file.api.client;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.file.api.factory.FileCommonClientFallbackFactory;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
    contextId = "FileCommonClient1",
    name = ServiceAppConstants.FILE_SERVICE_NAME,
    fallbackFactory = FileCommonClientFallbackFactory.class)
@ResponseBody
public interface FileCommonClient {

  @ApiOperation("文件上传")
  @PostMapping("/api/file/upload")
  Result<Long> upload(
      @RequestParam(name = "file") MultipartFile multipartFile,
      @RequestParam(name = "bizCode") String bizCode);

  /**
   * 更新文件业务主键id
   *
   * @param fileIdSet 文件ID集合
   * @param bizId 业务主键id
   */
  @PostMapping("/api/file/updateFileBizId")
  Result<Boolean> updateFileBizId(@RequestBody Set<Long> fileIdSet,@RequestParam(name = "bizId") Long bizId);
}
