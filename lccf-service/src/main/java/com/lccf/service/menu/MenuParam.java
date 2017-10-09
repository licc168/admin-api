package com.lccf.service.menu;

import com.lccf.service.base.PageParam;
import javax.persistence.Column;
import lombok.Data;

/**
 * @author lichangchao
 * @date 2017 -05-02 20:47:49
 */
@Data
public class MenuParam extends PageParam{
  private Long id;
  private String path;
  private String title;
  private String icon;
  private Boolean selected = false;
  private Boolean expanded = false;
  private Boolean deleteFlag = false;;
  private Long parentId;
  private Integer orderNum;


}
