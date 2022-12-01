package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;

import java.util.Locale;

public interface EmailService {
    void sendEmailSelf(String subject, String body);
    void notifyNewArgument(String to, String from, Debate debate, Locale locale);
    void notifyNewInvite(String to, String from, Debate debate, Locale locale);
}
