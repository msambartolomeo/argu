package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
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
    public Debate create(String name, String description, User creator, boolean isCreatorFor, User opponent, Image image, DebateCategory category) {
        Debate debate = new Debate(name, description, creator, isCreatorFor, opponent, image, category);
        em.persist(debate);
        return debate;
    }

    @Override
    public Optional<Debate> getDebateById(long id) {
        return Optional.ofNullable(em.find(Debate.class, id));
    }

    @Override
    public List<Debate> getSubscribedDebatesByUser(User user, int page, int size) {
        Query idQuery = em.createNativeQuery("SELECT debateid FROM debates WHERE debateid IN (SELECT debateid FROM subscribed WHERE userid = :userid) AND status <> 2 ORDER BY created_date DESC LIMIT :limit OFFSET :offset");
        return getDebatesReusable(user.getUserId(), page, size, idQuery);
    }

    @Override
    public int getSubscribedDebatesByUserCount(User user) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM subscribed WHERE userid = :userid");
        query.setParameter("userid", user.getUserId());

        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> ((BigInteger) o).intValue()).orElse(0);
    }

    @Override
    public List<Debate> getDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date) {
        StringBuilder queryString;
        DebateOrder orderBy;

        if (order == null)
            orderBy = DebateOrder.DATE_DESC;
        else
            orderBy = order;

        if (orderBy == DebateOrder.SUBS_DESC || orderBy == DebateOrder.SUBS_ASC)
            queryString = new StringBuilder("SELECT d.debateid FROM debates d LEFT JOIN subscribed s ON d.debateid = s.debateid WHERE TRUE");
        else
            queryString = new StringBuilder("SELECT debateid FROM debates WHERE TRUE");

        Map<String, Object> params = setupDiscovery(searchQuery, category, queryString, status, date);

        switch (orderBy) {
            case DATE_ASC:
                queryString.append(" ORDER BY created_date ASC");
                break;
            case DATE_DESC:
                queryString.append(" ORDER BY created_date DESC");
                break;
            case ALPHA_ASC:
                queryString.append(" ORDER BY name ASC");
                break;
            case ALPHA_DESC:
                queryString.append(" ORDER BY name DESC");
                break;
            case SUBS_ASC:
                queryString.append(" GROUP BY d.debateid ORDER BY COUNT(DISTINCT s.userid) ASC");
                break;
            case SUBS_DESC:
                queryString.append(" GROUP BY d.debateid ORDER BY COUNT(DISTINCT s.userid) DESC");
                break;
        }

        queryString.append(" LIMIT :pageSize OFFSET :page");
        params.put("pageSize", pageSize);
        params.put("page", page * pageSize);

        Query idQuery = em.createNativeQuery(queryString.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            idQuery.setParameter(entry.getKey(), entry.getValue());
        }
        return getDebatesReusable(idQuery);
    }

    @Override
    public int getDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date) {
        StringBuilder queryString = new StringBuilder("SELECT COUNT(*) FROM debates WHERE TRUE");

        Map<String, Object> params = setupDiscovery(searchQuery, category, queryString, status, date);

        Query query = em.createNativeQuery(queryString.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> ((BigInteger) o).intValue()).orElse(0);
    }

    private Map<String, Object> setupDiscovery(String searchQuery, DebateCategory category, StringBuilder queryString, DebateStatus status, LocalDate date) {
        Map<String, Object> params = new HashMap<>();

        if (searchQuery != null) {
            queryString.append(" AND lower(name) LIKE lower(:searchWord)");
            params.put("searchWord", "%" + searchQuery + "%");
        }
        if (category != null) {
            queryString.append(" AND category = :category");
            params.put("category", category.ordinal());
        }
        queryString.append(" AND status <> 2");
        if (status != null && status != DebateStatus.DELETED) {
            params.put("status", status.ordinal());
            if (status == DebateStatus.OPEN) {
                queryString.append(" AND (status = :status OR status = :statusAux OR status = :statusAux2)");
                params.put("statusAux", DebateStatus.CLOSING.ordinal());
                params.put("statusAux2", DebateStatus.VOTING.ordinal());
            } else {
                queryString.append(" AND status = :status");
            }
        }
        if (date != null) {
            LocalDateTime dateTime = date.atStartOfDay();
            queryString.append(" AND created_date >= :date");
            params.put("date", Timestamp.valueOf(dateTime));
        }
        return params;
    }

    @Override
    public List<Debate> getUserDebates(User user, int page, int size) {
        Query idQuery = em.createNativeQuery("SELECT debateid FROM debates WHERE (creatorid = :userid OR opponentid = :userid) AND status <> 2 ORDER BY created_date DESC LIMIT :limit OFFSET :offset");
        return getDebatesReusable(user.getUserId(), page, size, idQuery);
    }

    @Override
    public int getUserDebatesCount(User user) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM debates WHERE (creatorid = :userid OR opponentid = :userid) AND status <> 2");
        query.setParameter("userid", user.getUserId());

        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> ((BigInteger) o).intValue()).orElse(0);
    }

    @Override
    public List<Debate> getDebatesToClose() {
        LocalDate date = LocalDate.now();
        final TypedQuery<Debate> query = em.createQuery("FROM Debate d WHERE d.status = :status AND d.dateToClose = :date", Debate.class);
        query.setParameter("status", DebateStatus.VOTING);
        query.setParameter("date", date);

        return query.getResultList();
    }

    @Override
    public List<Debate> getRecommendedDebates(Debate debate) {
        Query idQuery = em.createNativeQuery("WITH selected_ids AS (\n" +
                "    SELECT debateid\n" +
                "    FROM subscribed s\n" +
                "    WHERE s.debateid IN (\n" +
                "        SELECT debateid\n" +
                "        FROM debates d\n" +
                "        WHERE category = :category\n" +
                "    )\n" +
                "       OR s.userid IN (\n" +
                "        SELECT userid\n" +
                "        FROM subscribed\n" +
                "        WHERE debateid = :debateid\n" +
                "    )\n" +
                "    GROUP BY s.debateid\n" +
                ")\n" +
                "SELECT si.debateid\n" +
                "FROM selected_ids si\n" +
                "LEFT JOIN subscribed s ON si.debateid = s.debateid\n" +
                "INNER JOIN debates d2 on s.debateid = d2.debateid\n" +
                "WHERE si.debateid != :debateid AND d2.status != 2\n" +
                "GROUP BY si.debateid\n" +
                "ORDER BY count(distinct s.userid) DESC LIMIT 3");
        idQuery.setParameter("category", debate.getCategory().ordinal());
        idQuery.setParameter("debateid", debate.getDebateId());

        return getDebatesReusable(idQuery);
    }

    @Override
    public List<Debate> getRecommendedDebates(Debate debate, User user) {
        Query idQuery = em.createNativeQuery("WITH selected_ids AS (\n" +
                "    SELECT debateid\n" +
                "    FROM subscribed s\n" +
                "    WHERE s.debateid IN (\n" +
                "        SELECT debateid\n" +
                "        FROM debates d\n" +
                "        WHERE category = :category\n" +
                "    )\n" +
                "       OR s.userid IN (\n" +
                "        SELECT userid\n" +
                "        FROM subscribed\n" +
                "        WHERE debateid = :debateid\n" +
                "    )\n" +
                "    GROUP BY s.debateid\n" +
                ")\n" +
                "SELECT si.debateid\n" +
                "FROM selected_ids si\n" +
                "LEFT JOIN subscribed s ON si.debateid = s.debateid\n" +
                "INNER JOIN debates d2 on s.debateid = d2.debateid\n" +
                "WHERE si.debateid != :debateid AND d2.status != 2\n" +
                "AND si.debateid NOT IN (\n" +
                "    SELECT debateid\n" +
                "    FROM subscribed s\n" +
                "    WHERE s.userid = :userid\n" +
                "    )\n" +
                "GROUP BY si.debateid\n" +
                "ORDER BY count(distinct s.userid) DESC LIMIT 3");
        idQuery.setParameter("category", debate.getCategory().ordinal());
        idQuery.setParameter("debateid", debate.getDebateId());
        idQuery.setParameter("userid", user.getUserId());

        return getDebatesReusable(idQuery);
    }

    // Extracted code to simplify methods
    private List<Debate> getDebatesReusable(long userId, int page, int size, Query idQuery) {
        idQuery.setParameter("userid", userId);
        idQuery.setParameter("offset", page * size);
        idQuery.setParameter("limit", size);
        return getDebatesReusable(idQuery);
    }

    private List<Debate> getDebatesReusable(Query idQuery) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream()
                .map(o -> ((BigInteger) o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Debate> query = em.createQuery("FROM Debate d WHERE d.id IN :ids", Debate.class);
        query.setParameter("ids", ids);

        List<Debate> unsortedList = query.getResultList();
        List<Debate> sortedList = new ArrayList<>();
        for (Long id : ids)
            sortedList.add(unsortedList.stream().filter(d -> d.getDebateId().equals(id)).findFirst().get());
        return sortedList;
    }
}
