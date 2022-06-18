package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;

public interface EmailService {
    void sendEmailSelf(String subject, String body);
    void notifyNewArgument(String to, String from, long debateId, String debateName);
    void notifyNewInvite(String to, String from, Debate debate, String debateName);
}
