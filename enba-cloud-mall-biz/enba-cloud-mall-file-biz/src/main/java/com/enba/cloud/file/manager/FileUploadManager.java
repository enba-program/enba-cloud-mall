package com.enba.cloud.file.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.enba.boot.oss.FileUploadHelper;
import com.enba.cloud.common.enums.FileBizCodeEnum;
import com.enba.cloud.file.api.entity.File;
import com.enba.cloud.file.mapper.FileMapper;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadManager {

  private static final Logger log = LoggerFactory.getLogger(FileUploadManager.class);

  @PostConstruct
  public void init() {
    staticEndpoint = endpoint;
    log.info("初始化文件上传管理器: endpoint={}", staticEndpoint);
  }

  private static FileUploadHelper fileUploadHelper;

  private static FileMapper fileMapper;

  public FileUploadManager(FileUploadHelper fileUploadHelper, FileMapper fileMapper) {
    FileUploadManager.fileUploadHelper = fileUploadHelper;
    FileUploadManager.fileMapper = fileMapper;
  }

  @Value("${file-upload.aliyun-oss.endpoint}")
  private String endpoint;

  private static String staticEndpoint;

  /**
   * 批量获取文件信息
   *
   * @param fileIdSet 文件ID集合
   * @return 文件信息列表
   */
  public static List<File> getFileInfoListById(Set<Long> fileIdSet) {
    return fileMapper.selectList(
        Wrappers.<File>lambdaQuery().in(File::getId, fileIdSet).eq(File::getDeleted, 0));
  }

  /**
   * 根据业务主键id 获取文件信息
   *
   * @param bizId 业务主键id
   * @return 文件信息列表
   */
  public static List<File> getFileInfoListByBizId(Long bizId) {
    return fileMapper.selectList(
        Wrappers.<File>lambdaQuery().eq(File::getBizId, bizId).eq(File::getDeleted, 0));
  }

  /**
   * 更新文件业务主键id
   *
   * @param fileIdSet 文件ID集合
   * @param bizId 业务主键id
   */
  public static void updateFileBizId(Set<Long> fileIdSet, Long bizId) {
    fileMapper.update(
        Wrappers.<File>lambdaUpdate().set(File::getBizId, bizId).in(File::getId, fileIdSet));
  }

  /**
   * 上传文件
   *
   * @param multipartFile 文件
   * @param codeEnum 文件业务场景枚举
   * @return 文件地址
   */
  public static Long upload(MultipartFile multipartFile, FileBizCodeEnum codeEnum) {
    Objects.requireNonNull(codeEnum, "文件业务场景异常");
    Objects.requireNonNull(multipartFile, "上传文件不能为空");

    String objectName =
        codeEnum.getBizCode()
            + "/"
            + generateUniqueFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
    fileUploadHelper.uploadFile(multipartFile, codeEnum.getBucketName(), objectName);

    File file = new File();
    file.setFileName(objectName);
    file.setFileType(getExtensionName(multipartFile.getOriginalFilename()));
    file.setFileUrl(staticEndpoint + objectName);
    file.setFileDes("");
    file.setBizId(0L);
    file.setBizCode(codeEnum.getBizCode());
    fileMapper.insert(file);

    return file.getId();
  }

  private static String getExtensionName(String originalFilename) {
    // 获取文件扩展名（如 .jpg, .png）
    String extension = "";
    int lastDotIndex = originalFilename.lastIndexOf(".");
    if (lastDotIndex > 0) {
      extension = originalFilename.substring(lastDotIndex);
    }

    return extension;
  }

  private static String generateUniqueFileName(String originalFilename) {
    // 获取文件扩展名（如 .jpg, .png）
    String extension = "";
    int lastDotIndex = originalFilename.lastIndexOf(".");
    if (lastDotIndex > 0) {
      extension = originalFilename.substring(lastDotIndex);
    }

    // 生成 UUID + 原始文件名（避免乱码）
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex);

    // 组合成新文件名（UUID + 原始文件名 + 扩展名）
    return uuid + "-" + fileNameWithoutExtension + extension;
    // 示例：550e8400e29b41d4a716-446655440000-example.jpg
  }
}
