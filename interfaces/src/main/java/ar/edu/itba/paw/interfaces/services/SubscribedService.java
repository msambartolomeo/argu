package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Subscribed;

import java.util.Optional;

public interface SubscribedService {
    Subscribed subscribeToDebate(String username, long debateId);
    boolean unsubscribeToDebate(String username, long debateId);

    boolean isUserSubscribed(String username, long debateId);

    Optional<Subscribed> getSubscribed(String username, long debateId);
}
