package com.enba.cloud.file.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author 恩爸编程
 * @since 2025-05-26
 */
@TableName("t_file")
@ApiModel(value = "File对象", description = "")
@Data
public class File implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("主键")
  private Long id;

  @ApiModelProperty("文件名称")
  private String fileName;

  @ApiModelProperty("文件类型")
  private String fileType;

  @ApiModelProperty("文件地址")
  private String fileUrl;

  @ApiModelProperty("描述")
  private String fileDes;

  @ApiModelProperty("业务主键id")
  private Long bizId;

  /**
   * @see com.enba.mallapi.enums.FileBizCodeEnum
   */
  @ApiModelProperty("业务场景code")
  private String bizCode;

  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty("0未删除 1删除")
  private Integer deleted;
}
