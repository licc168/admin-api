package com.lccf.service.mail.impl;

import com.lccf.domain.JltEmailLog;
import com.lccf.domain.JltEmailTpl;
import com.lccf.enums.StatusEmun;
import com.lccf.param.EmailReq;
import com.lccf.repository.JltEmialLogRepository;
import com.lccf.repository.JltEmialTplRepository;
import com.lccf.service.mail.JltEmailService;
import com.lccf.service.mail.MailService;
import com.lccf.util.StringUtil;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;



/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 17:08
 * @see
 */
@Service
@Transactional
public class JltEmailServiceImpl implements JltEmailService {
  @Resource
  JltEmialTplRepository emialTplRepository;
  @Resource
  JltEmialLogRepository emialLogRepository;



  @Resource
  MailService mailService;
  @Override
  public Optional<JltEmailTpl> getContentByCode(String code) {
    List<JltEmailTpl> list = emialTplRepository.findByCodeAndDeleteFlag(code,false);
    if(CollectionUtils.isEmpty(list))return Optional.empty();
    return Optional.of(list.get(0));
  }

  @Override
  public boolean isOpen(Integer appId) {
    //TODO 预留业务
    return true;
  }

  @Override
  public void sendEmail(EmailReq req, JltEmailTpl tpl) {
    String content = StringUtil.transContent(tpl.getContent(),req.getTplParam());

    boolean flag =  mailService.sendEmail(req.getTo(),req.getSubject(),content,false,true);
    JltEmailLog jltEmailLog = new JltEmailLog();
    jltEmailLog.setAppId(tpl.getAppId());
    jltEmailLog.setContent(content);
    jltEmailLog.setDeleteFlag(false);
    jltEmailLog.setRecipient(req.getTo());
    jltEmailLog.setSubject(req.getSubject());
    jltEmailLog.setCreateTime(new Date());
    jltEmailLog.setStatus(flag? StatusEmun.SUCCESS.getCode():StatusEmun.FAIL.getCode());
    emialLogRepository.save(jltEmailLog);
  }
}
