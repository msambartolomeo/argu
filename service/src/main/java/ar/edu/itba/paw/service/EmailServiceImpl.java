package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@noreddit.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Value("classpath:html/notification-email.html")
    private Resource notificationEmail;

    @Override
    public void notifyNewPost(String to) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        //TODO: Decidir entre texto plano o html. Además, verificar que funcione.
        String htmlMsg = ResourceReader.asString(notificationEmail);
        //String htmlMsg = "<h3>Hello World!</h3>"; 
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(to);
            helper.setSubject("New post in a debate you're following!");
            helper.setFrom("noreply@noreddit.com"); //TODO: Actualizar el nombre
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
