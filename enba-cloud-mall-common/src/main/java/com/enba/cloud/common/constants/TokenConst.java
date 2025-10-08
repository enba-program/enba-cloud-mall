package com.enba.cloud.common.constants;

public interface TokenConst {

  //  返给前端token名称
  String TOKEN_NAME = "token";
  //  token存redis的key
  String LOGIN_H5_TOKEN_KEY = "login_h5_token_key";
  Long LOGIN_H5_TOKEN_TIMEOUT = 60 * 60 * 24 * 7L; // 7天
  String TOKEN_HEADER = "Authorization";
  String TOKEN_PREFIX = "Bearer ";
  String TOKEN_USER_ID = "user_id";
  String TOKEN_USER_NAME = "user_name";
  String TOKEN_USER_NICK_NAME = "nick_name";
  String TOKEN_USER_AVATAR = "user_avatar";
  String TOKEN_USER_TYPE = "user_type";
  String TOKEN_USER_ROLES = "user_roles";
  String TOKEN_USER_PERMISSIONS = "user_permissions";
  String TOKEN_USER_EMAIL = "user_email";
  String TOKEN_USER_PHONE = "user_phone";

  // 系統ID
  String SYSTEM_USER_ID = "0L";
  // 系統名称
  String SYSTEM_USER_NAME = "system";

  /**
   * 获取redis缓存key（h5项目使用）
   *
   * @param userId 用户id
   * @return redis缓存key
   */
  static String getH5TokenKey(String userId) {
    return TokenConst.LOGIN_H5_TOKEN_KEY + ":" + userId;
  }
}
