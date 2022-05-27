package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.keys.UserDebateKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VoteJpaDao implements VoteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Vote addVote(User user, Debate debate, DebateVote vote) {
        Vote v = new Vote(user, debate, vote);
        em.persist(v);
        return v;
    }

    @Override
    public Optional<Vote> getVote(User user, Debate debate) {
        return Optional.ofNullable(em.find(Vote.class, new UserDebateKey(user.getUserId(), debate.getDebateId())));
    }

    @Override
    public void delete(Vote vote) {
        em.remove(vote);
    }
}
