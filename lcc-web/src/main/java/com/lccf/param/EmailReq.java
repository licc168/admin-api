package com.lccf.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发送邮件请求参数
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 16:48
 * @see
 */
@Data
@ApiModel(value = "邮件参数")
public class EmailReq {
  @NotNull(message = "邮件接受者不能为空")
  @Pattern(regexp="[\\w-.]+@[\\w-]+(.[\\w_-]+)+",message = "邮箱格式不正确")
  @ApiModelProperty(name = "to",value="邮件接受者")
  String to;


  @NotNull(message = "模板编码不能为空")
  @ApiModelProperty(name = "tplCode",value="模板编码")
  String tplCode;

  @ApiModelProperty(name = "tplParam",value="模板参数")
  String tplParam;

  @ApiModelProperty(name = "subject",value="主题")
  String subject;


}
