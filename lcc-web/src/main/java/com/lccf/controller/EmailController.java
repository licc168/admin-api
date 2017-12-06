package com.lccf.controller;

import com.lccf.base.controller.BaseController;
import com.lccf.constants.Constants;
import com.lccf.domain.JltEmailTpl;
import com.lccf.exception.BusinessException;
import com.lccf.param.EmailReq;
import com.lccf.service.mail.JltEmailService;
import com.lccf.util.ResponseVo;
import com.lccf.util.ResponseVoUtil;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <strong>发送邮件</strong>
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 16:46
 * @see
 */
@RestController
@RequestMapping("app")
public class EmailController extends BaseController {
    @Resource
    JltEmailService jltEmailService;

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "发送邮件", httpMethod = "POST", response = ResponseEntity.class)
    public ResponseVo sendEmail(@ApiParam(value = "邮件发送参数", required = true)@RequestBody @Validated EmailReq emailReq) {
       //限制频率
        String  key = EMAIL_KEY+emailReq.getTo();
        boolean apiFlag = this.apiFrequency(key,600L,5);
        if(!apiFlag){
            throw  new BusinessException("频率限制:10分钟内5封");
        }

        //获取模板判断模板是否存在
        Optional<JltEmailTpl> jltEmailTplOptional = jltEmailService.getContentByCode(emailReq.getTplCode());
        jltEmailTplOptional.orElseThrow(() ->new BusinessException("模板不存在"));

        //判断该应用邮件功能是否启用
        boolean isOpen =  jltEmailService.isOpen(jltEmailTplOptional.get().getAppId());
        if(!isOpen){
            throw  new BusinessException("该应用邮件功能被禁用");
        }
        //发送邮件
        jltEmailService.sendEmail(emailReq, jltEmailTplOptional.get());
        return ResponseVoUtil.successMsg("发送成功");
    }


}
