package com.lccf.service.base;

import java.util.Date;

/**
 *
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/10/11 18:14
 * @see
 */
public class BaseParam {
    private Long id;
    private Date createTime;
    private Date updateTime;

    public void setCreateTime(Date createTime) {
        if (this.id == null) {
            this.createTime = new Date();
        }

    }

    public void setUpdateTime(Date updateTime) {
        if (this.id != null) {
            this.updateTime = new Date();
        }
    }
}
