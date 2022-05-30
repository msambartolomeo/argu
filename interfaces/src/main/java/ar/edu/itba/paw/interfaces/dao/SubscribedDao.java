package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Subscribed;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface SubscribedDao {
    Subscribed subscribeToDebate(User user, Debate debate);

    Optional<Subscribed> getSubscribed(User user, Debate debate);

    void unsubscribe(Subscribed subscribed);
}
