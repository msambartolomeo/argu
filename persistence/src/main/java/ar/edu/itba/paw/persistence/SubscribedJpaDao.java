package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SubscribedDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Subscribed;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.keys.UserDebateKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class SubscribedJpaDao implements SubscribedDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Subscribed subscribeToDebate(User user, Debate debate) {
        Subscribed subscribed = new Subscribed(user, debate);
        em.persist(subscribed);
        return subscribed;
    }

    @Override
    public Optional<Subscribed> getSubscribed(User user, Debate debate) {
        return Optional.ofNullable(em.find(Subscribed.class,
                new UserDebateKey(user.getUserId(), debate.getDebateId())));
    }

    @Override
    public void unsubscribe(Subscribed subscribed) {
        em.remove(subscribed);
    }
}
