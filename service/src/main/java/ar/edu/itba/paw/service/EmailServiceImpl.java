package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    Environment env;

    @Async
    @Override
    public void sendEmailSelf(String subject, String body) {
        sendEmail(env.getProperty("spring.mail.username"), subject, body);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@argu.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Value("classpath:new-noti-email.html")
    private Resource notificationEmail;

    @Async
    @Override
    public void notifyNewPost(String to) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");


        String htmlMsg = ResourceReader.asString(notificationEmail);
        
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(to);
            helper.setSubject("New post in a debate you're following!");
            helper.setFrom("noreply@argu.com"); //TODO: Actualizar el nombre
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace(); //TODO: handle exception
        }
    }

    public static class ResourceReader {
        public static String asString(Resource resource) {
            try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
