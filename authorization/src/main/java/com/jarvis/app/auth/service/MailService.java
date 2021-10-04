package com.jarvis.app.auth.service;

import com.jarvis.app.auth.Consts;
import com.jarvis.app.auth.model.MailMessage;
import com.jarvis.frmk.core.ICore;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.log.LoggerJ;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @LogSlf4j
    private LoggerJ log;

    private String mailTemplate;

    public MailService() throws IOException {
        mailTemplate = IOUtils.toString(getClass().getResourceAsStream("/templates/mail/otp.html"), ICore.CHARSET_UTF_8);
    }

    @Async
    public void send(String fullName, String destination, String code) {
        log.info("send otp");
        log.info("send to:{}", destination);
        String template = mailTemplate;
        template = template.replace("{{username}}", fullName);
        template = template.replace("{{otpCode}}", code);
        asyncSendMail(new MailMessage(destination, Consts.SYS_NON_REPLY_MAIL, "OTP code", template));
    }

    @Async
    public void asyncSendMail(MailMessage request) {
        try {
            log.debug(request.toString());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            helper.setTo(request.getTo());
            helper.setText(request.getContent(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
