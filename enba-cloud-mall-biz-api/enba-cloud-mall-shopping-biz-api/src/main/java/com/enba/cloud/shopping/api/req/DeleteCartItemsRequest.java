package com.enba.cloud.shopping.api.req;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class DeleteCartItemsRequest {

  @ApiModelProperty("购物车项id")
  @NotNull(message = "购物车项id不能为空")
  @Size(min = 1, message = "至少选择一个购物车项")
  private List<Long> itemIds;
}
