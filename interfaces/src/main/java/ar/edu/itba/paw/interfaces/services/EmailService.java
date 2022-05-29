package ar.edu.itba.paw.interfaces.services;

public interface EmailService {
    void sendEmailSelf(String subject, String body);
    void notifyNewArgument(String to, String from, long debateId, String debateName);
    void notifyNewInvite(String to, String from, long debateId, String debateName);
}
