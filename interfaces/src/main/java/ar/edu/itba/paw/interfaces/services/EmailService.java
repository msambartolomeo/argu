package ar.edu.itba.paw.interfaces.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void notifyNewPost(String to);
}
