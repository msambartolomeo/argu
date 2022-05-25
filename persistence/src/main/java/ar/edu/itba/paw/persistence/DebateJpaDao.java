package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DebateJpaDao implements DebateDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Debate create(String name, String description, Long creatorId, Long opponentId, Long imageId, DebateCategory category) {
        Debate debate = new Debate(name, description, creatorId, opponentId, imageId, category, DebateStatus.OPEN);
        em.persist(debate);
        return debate;
    }

    @Override
    public Optional<Debate> getDebateById(long id) {
        return Optional.ofNullable(em.find(Debate.class, id));
    }

    @Override
    public Optional<PublicDebate> getPublicDebateById(long id) {
        return Optional.empty();
    }

    @Override
    public List<PublicDebate> getSubscribedDebatesByUserId(long userid, int page) {
        return null;
    }

    @Override
    public int getSubscribedDebatesByUserIdCount(long userid) {
        return 0;
    }

    @Override
    public void subscribeToDebate(long userid, long debateid) {

    }

    @Override
    public void unsubscribeToDebate(long userid, long debateid) {

    }

    @Override
    public boolean isUserSubscribed(long userid, long debateid) {
        return false;
    }

    @Override
    public List<PublicDebate> getPublicDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date) {
        return null;
    }

    @Override
    public int getPublicDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date) {
        return 0;
    }

    @Override
    public List<PublicDebate> getMyDebates(long userid, int page) {
        return null;
    }

    @Override
    public int getMyDebatesCount(long userid) {
        return 0;
    }

    @Override
    public void addVote(long debateId, long userId, DebateVote vote) {

    }

    @Override
    public void removeVote(long debateId, long userId) {

    }

    @Override
    public Boolean hasUserVoted(long debateId, long userId) {
        return null;
    }

    @Override
    public DebateVote getUserVote(long debateid, long userid) {
        return null;
    }

    @Override
    public void changeDebateStatus(long id, DebateStatus status) {

    }
}
