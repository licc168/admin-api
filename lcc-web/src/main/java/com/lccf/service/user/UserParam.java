package com.lccf.service.user;

import com.lccf.service.base.PageParam;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author lichangchao
 * @Time 2017 -04-13 20:22:31
 */
@Data
public class UserParam extends PageParam implements Serializable {
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$",message = "用户名必须是数字和字母组合 6-10位之间")
    private String userName;

    private String realName;

    private String email;

    private String mobile;

    private String password;


}
