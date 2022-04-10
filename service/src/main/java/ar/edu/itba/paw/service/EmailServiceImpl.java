package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

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

    @Override
    public void notifyNewPost(String to) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<h3>Hello World!</h3>"; //TODO: change this
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo("someone@abc.com");
            helper.setSubject("New post in a debate you're following!");
            helper.setFrom("abc@gmail.com");
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace(); //TODO: handle exception
        }
    }
}
