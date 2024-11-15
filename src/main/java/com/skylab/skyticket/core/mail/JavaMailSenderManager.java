package com.skylab.skyticket.core.mail;

import com.skylab.skyticket.core.results.ErrorResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.core.results.SuccessResult;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class JavaMailSenderManager implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public JavaMailSenderManager(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Result sendMail(String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(mimeMessage);
            return new SuccessResult("Mail başarıyla gönderildi!", HttpStatus.OK);
        } catch (Exception e) {
            return new ErrorResult("Mail gönderilirken bir hata oluştu!", HttpStatus.BAD_REQUEST);
        }
    }
}
