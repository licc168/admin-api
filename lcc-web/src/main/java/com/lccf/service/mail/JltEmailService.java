package com.lccf.service.mail;

import com.lccf.domain.JltEmailTpl;
import com.lccf.exception.BusinessException;
import com.lccf.param.EmailReq;
import java.util.Optional;


/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 17:05
 * @see
 */
public interface JltEmailService {
  /**
   * 根据模板编号查询内容
   */
  Optional<JltEmailTpl> getContentByCode(String code);
  boolean isOpen(Integer appId);
  void sendEmail(EmailReq req, JltEmailTpl tpl) throws BusinessException;

}
