package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Subscribed;
import java.util.Optional;

public interface SubscribedService {
    Subscribed subscribeToDebate(String username, long debateId);
    void unsubscribeToDebate(String username, long debateId);
}
