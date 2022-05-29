package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    public List<Debate> getSubscribedDebatesByUserId(User user, int page) {
        Query idQuery = em.createNativeQuery("SELECT debateid FROM debates WHERE debateid IN (SELECT debateid FROM subscribed2 WHERE userid = :userid) LIMIT 5 OFFSET :offset");
        idQuery.setParameter("userid", user.getUserId());
        idQuery.setParameter("offset", page * 5);
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream()
                .map(o -> ((BigInteger) o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Debate> query = em.createQuery("FROM Debate d WHERE d.id IN :ids", Debate.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public int getSubscribedDebatesByUserIdCount(long userid) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM subscribed2 WHERE userid = :userid");
        query.setParameter("userid", userid);

        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> ((BigInteger) o).intValue()).orElse(0);
    }

    @Override
    @Deprecated
    public List<PublicDebate> getPublicDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date) {
        return null;
    }

    @Override
    public List<Debate> getDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date) {
        StringBuilder queryString = new StringBuilder("SELECT debateid FROM debates WHERE TRUE");
        Map<String, Object> params = setupDiscovery(searchQuery, category, queryString, status, date);

        queryString.append(" ORDER BY");
        DebateOrder orderBy;
        if (order == null)
            orderBy = DebateOrder.DATE_DESC;
        else
            orderBy = order;

        switch(orderBy) {
            case DATE_ASC: // TODO: ver el tema del ORDER BY con @Formula
                queryString.append(" created_date ASC");
                break;
            case DATE_DESC:
                queryString.append(" created_date DESC");
                break;
            case ALPHA_ASC:
                queryString.append(" name ASC");
                break;
            case ALPHA_DESC:
                queryString.append(" name DESC");
                break;
            case SUBS_ASC:
                queryString.append(" subscribedcount ASC");
                break;
            case SUBS_DESC:
                queryString.append(" subscribedcount DESC");
                break;
        }

        queryString.append(" LIMIT :pageSize OFFSET :page");
        params.put("pageSize", pageSize);
        params.put("page", page * pageSize);

        Query idQuery = em.createNativeQuery(queryString.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            idQuery.setParameter(entry.getKey(), entry.getValue());
        }
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream()
                .map(o -> ((BigInteger) o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Debate> query = em.createQuery("FROM Debate d WHERE d.id IN :ids", Debate.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public int getDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date) {
        return 0;
    }

    private Map<String, Object> setupDiscovery(String searchQuery, DebateCategory category, StringBuilder queryString, DebateStatus status, LocalDate date) {
        Map<String, Object> params = new HashMap<>();

        if(searchQuery != null) {
            queryString.append(" AND lower(name) LIKE lower(:searchWord)");
            params.put("searchWord", "%" + searchQuery + "%");
        }
        if(category != null) {
            queryString.append(" AND category = :category");
            params.put("category", category.ordinal());
        }
        if (status != null) {
            params.put("status", status.ordinal());
            if (status == DebateStatus.OPEN) {
                queryString.append(" AND (status = :status OR status = :status)");
                params.put("status", DebateStatus.CLOSING.ordinal());
            } else {
                queryString.append(" AND status = :status");
            }
        }
        if (date != null) {
            LocalDateTime dateTime = date.atStartOfDay();
            queryString.append(" AND created_date >= :date");
            params.put("date", Timestamp.valueOf(dateTime));
            queryString.append(" AND created_date <= :date");
            params.put("date", Timestamp.valueOf(dateTime.plusDays(1)));
        }
        return params;
    }

    @Override
    @Deprecated
    public int getPublicDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date) {
        return 0;
    }

    @Override
    @Deprecated
    public List<PublicDebate> getMyDebates(long userid, int page) {
        return null;
    }

    @Override
    public List<Debate> getMyDebates(User user, int page) {
        Query idQuery = em.createNativeQuery("SELECT debateid FROM debates WHERE creatorid = :userid OR opponentid = :userid ORDER BY created_date DESC LIMIT 5 OFFSET :offset");
        idQuery.setParameter("userid", user.getUserId());
        idQuery.setParameter("offset", page * 5);
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream()
                .map(o -> ((BigInteger) o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Debate> query = em.createQuery("FROM Debate d WHERE d.id IN :ids", Debate.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public int getMyDebatesCount(User user) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM debates WHERE creatorid = :userid OR opponentid = :userid");
        query.setParameter("userid", user.getUserId());

        query.getSingleResult();

        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> (Integer) o).orElse(0);
    }

    @Override
    @Deprecated
    public int getMyDebatesCount(long userid) {
        return 0;
    }

}
