package com.enba.cloud.common.utils;

import java.util.Collection;
import java.util.List;
import org.springframework.util.AntPathMatcher;

public class StringUtil {

  public static boolean matches(String str, List<String> strs) {
    if (isEmpty(str) || isEmpty(strs)) {
      return false;
    }
    for (String pattern : strs) {
      if (isMatch(pattern, str)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isMatch(String pattern, String url) {
    AntPathMatcher matcher = new AntPathMatcher();
    return matcher.match(pattern, url);
  }

  public static boolean isEmpty(Collection<?> coll) {
    return isNull(coll) || coll.isEmpty();
  }

  public static boolean isNull(Object object) {
    return object == null;
  }

  public static boolean isEmpty(String str) {
    return isNull(str) || "".equals(str.trim());
  }
}
