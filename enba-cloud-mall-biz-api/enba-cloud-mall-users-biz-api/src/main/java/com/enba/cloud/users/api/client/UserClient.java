package com.enba.cloud.users.api.client;

import com.enba.boot.core.base.Result;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.common.constants.ServiceAppConstants;
import com.enba.cloud.users.api.factory.UserClientFallbackFactory;
import com.enba.cloud.users.api.resp.PersonalCenterResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    contextId = "UserClient1",
    name = ServiceAppConstants.USER_SERVICE_NAME,
    fallbackFactory = UserClientFallbackFactory.class)
@ResponseBody
public interface UserClient {

  @ApiOperation("个人中心-用户详情")
  @GetMapping("/api/user/personal-center")
  Result<PersonalCenterResp> personalCenter(@RequestParam(name = "userId") Long userId);
}
