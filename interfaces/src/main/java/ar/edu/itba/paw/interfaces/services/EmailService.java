package ar.edu.itba.paw.interfaces.services;

import java.util.Locale;
import ar.edu.itba.paw.model.Debate;

public interface EmailService {
    void sendEmailSelf(String subject, String body);
    void notifyNewArgument(String to, String from, Debate debate, Locale locale);
    void notifyNewInvite(String to, String from, Debate debate, Locale locale);
}
