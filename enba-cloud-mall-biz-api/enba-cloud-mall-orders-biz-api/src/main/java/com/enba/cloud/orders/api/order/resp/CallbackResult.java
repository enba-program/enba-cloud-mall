package com.enba.cloud.orders.api.order.resp;

import lombok.Data;

@Data
public class CallbackResult {

  private String code;
  private String message;

  public static CallbackResult ok() {
    CallbackResult callbackResult = new CallbackResult();
    callbackResult.setCode("SUCCESS");
    callbackResult.setMessage("成功");
    return callbackResult;
  }

  public static CallbackResult err() {
    CallbackResult callbackResult = new CallbackResult();
    callbackResult.setCode("FAIL");
    callbackResult.setMessage("失败");
    return callbackResult;
  }
}
