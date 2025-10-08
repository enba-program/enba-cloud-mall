package com.enba.cloud.goods.api.spu.bo;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SpecNameValueBo {
  // t_sku_spec表id
  private Long attrId;

  // t_sku_spec表spec_name
  private String attrName;

  // t_sku_spec_enum表id
  private Long valueId;

  // t_sku_spec_enum表spec_value
  private String valueName;

  public static void main(String[] args) {
    List<SpecNameValueBo> list = new ArrayList();
    SpecNameValueBo specNameValueBo = new SpecNameValueBo();

    specNameValueBo.setAttrName("参数名111");
    specNameValueBo.setValueName("参数值111");

    list.add(specNameValueBo);

    System.out.println(JSON.toJSONString(list));
  }
}
