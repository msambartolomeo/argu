package ar.edu.itba.paw.interfaces.services;

import java.util.Locale;

public interface EmailService {
    void sendEmailSelf(String subject, String body);
    void notifyNewArgument(String to, String from, long debateId, String debateName, Locale locale);
    void notifyNewInvite(String to, String from, long debateId, String debateName, Locale locale);
}
