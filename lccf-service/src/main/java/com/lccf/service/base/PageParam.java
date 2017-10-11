package com.lccf.service.base;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 分页参数
 *
 * @author lichangchao
 * @date 2017 -04-25 19:25:03
 */
@Data
public class PageParam  extends BaseParam{
    private Integer page = 0;
    private Integer size = 10;
    private Sort sort;

    public PageRequest transPageRequest() {

            return new PageRequest(this.page, this.size, this.sort);


    }
}
