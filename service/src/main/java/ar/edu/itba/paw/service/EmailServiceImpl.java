package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.model.Debate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    Environment env;

    @Autowired
    private MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final String baseUrl = "http://pawserver.it.itba.edu.ar/paw-2022a-06/";

    @Autowired
    public EmailServiceImpl(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Async
    @Override
    public void sendEmailSelf(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@argu.com");
        message.setTo(env.getProperty("spring.mail.username"));
        message.setSubject(subject);
        message.setText(body);
        try {
            emailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("Error sending email us, exception was: ", e);
        }
    }

    @Async
    @Override
    public void notifyNewArgument(String to, String from, Debate debate, Locale locale) {
        String subject = messageSource.getMessage("new-argument.subject", null, locale);
        String message = messageSource.getMessage("new-argument.message", null, locale);
        sendEmail(to, from, debate.getName(), baseUrl + "debates/" + debate.getDebateId(), subject, message, locale);
    }

    @Async
    @Override
    public void notifyNewInvite(String to, String from, Debate debate, Locale locale) {
        String subject = messageSource.getMessage("new-invite.subject", null, locale);
        String message = messageSource.getMessage("new-invite.message", null, locale);
        sendEmail(to, from, debate.getName(), baseUrl + "debates/" + debate.getDebateId(), subject, message, locale);
    }

    private void sendEmail(String to, String from, String debateName, String url, String subject, String message, Locale locale) {
        final Context ctx = new Context(locale);
        ctx.setVariable("senderName", from);
        ctx.setVariable("url", url);
        ctx.setVariable("debateName", debateName);
        ctx.setVariable("message", message);

        final String htmlContent = templateEngine.process("classpath:email-templates/notification-email.html", ctx);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setText(htmlContent, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("noreply@argu.com");

            emailSender.send(mimeMessage);
        } catch (Exception e) {
            LOGGER.warn("Email sending threw messaging exception: ", e);
        }
    }
}