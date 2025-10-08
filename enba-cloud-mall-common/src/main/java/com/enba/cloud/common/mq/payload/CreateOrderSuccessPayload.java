package com.enba.cloud.common.mq.payload;

import com.alibaba.fastjson2.JSON;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CreateOrderSuccessPayload implements Serializable {

  private static final long serialVersionUID = 1L;

  // 订单号
  private String orderNo;

  public String serialize() {
    return JSON.toJSONString(this);
  }
}
