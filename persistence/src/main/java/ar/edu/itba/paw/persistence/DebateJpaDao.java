package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.keys.UserDebateKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DebateJpaDao implements DebateDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Debate create(String name, String description, User creator, User opponent, Image image, DebateCategory category) {
        Debate debate = new Debate(name, description, creator, opponent, image, category, DebateStatus.OPEN);
        em.persist(debate);
        return debate;
    }

    @Override
    public Optional<Debate> getDebateById(long id) {
        return Optional.ofNullable(em.find(Debate.class, id));
    }

    @Override
    public List<Debate> getSubscribedDebatesByUserId(long userid, int page) {
        Query idQuery = em.createNativeQuery("SELECT debateid FROM debates WHERE debateid IN (SELECT debateid FROM subscribed WHERE userid = :userid LIMIT 5 OFFSET :offset)");
        idQuery.setParameter("userid", userid);
        idQuery.setParameter("offset", page * 5);
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream()
                .map(o -> ((Integer) o).longValue()).collect(Collectors.toList());

        final TypedQuery<Debate> query = em.createQuery("FROM debates AS d WHERE d.id IN :ids", Debate.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public int getSubscribedDebatesByUserIdCount(long userid) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM subscribed WHERE userid = :userid");
        query.setParameter("userid", userid);
        return ((Number) query.getSingleResult()).intValue();
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
    @Deprecated
    public void removeVote(long debateId, long userId) {

    }

    @Override
    @Deprecated
    public Boolean hasUserVoted(long debateId, long userId) {
        return null;
    }
}
