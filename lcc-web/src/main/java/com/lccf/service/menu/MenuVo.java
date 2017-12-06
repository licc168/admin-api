package com.lccf.service.menu;


import com.lccf.domain.Menu;
import lombok.Data;

@Data
public class MenuVo extends Menu{
    private Long id;
    private String path;
    private String title;
    private String icon;
    private Boolean selected;
    private Boolean expanded;
    private Boolean deleteFlag;
    private Long parentId;
    private Integer orderNum;
    private String parentTitle;


}
