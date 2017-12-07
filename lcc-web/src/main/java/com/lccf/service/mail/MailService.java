package com.lccf.service.mail;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lccf.constants.LccfProperties;


@Service
public class MailService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Resource
    private LccfProperties      lccfProperties;

    @Inject
    private JavaMailSender      javaMailSender;

    @Async
    public boolean sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        boolean flag = true;
        LOGGER.info("发送邮件[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to, subject,
                content);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(lccfProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            LOGGER.debug("发送邮件 to User '{}'", to);
        } catch (Exception e) {
            LOGGER.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
            flag = false;
        }
        return flag;
    }

}
