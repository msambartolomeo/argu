package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ArgumentDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ArgumentJpaDao implements ArgumentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Argument> getArgumentById(long argumentId) {
        return Optional.ofNullable(em.find(Argument.class, argumentId));
    }

    @Override
    public int getArgumentsByDebateCount(long debateId) {
        final Query query = em.createNativeQuery("SELECT COUNT(*) FROM posts WHERE debateid = :id");
        query.setParameter("id", debateId);

        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> ((BigInteger) o).intValue()).orElse(0);
    }

    @Override
    public List<Argument> getArgumentsByDebate(Debate debate, int page) {
        final Query idQuery = em.createNativeQuery("SELECT postid FROM posts WHERE debateid = :debateid ORDER BY created_date LIMIT 5 OFFSET :offset");
        idQuery.setParameter("debateid", debate.getDebateId());
        idQuery.setParameter("offset", page * 5);

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((BigInteger) o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Argument> query = em.createQuery("FROM Argument p WHERE p.argumentId IN :ids", Argument.class);
        query.setParameter("ids", ids);

        List<Argument> arguments = query.getResultList();
        arguments.sort(Comparator.comparing(Argument::getCreationDate));
        return arguments;
    }

    @Override
    public Argument create(User user, Debate debate, String content, Image image, ArgumentStatus status) {
        final Argument argument = new Argument(user, debate, content, image, status);
        em.persist(argument);
        return argument;
    }

    @Override
    public Optional<Argument> getLastArgument(Debate debate) {
        final TypedQuery<Argument> query = em.createQuery("FROM Argument p WHERE p.debate = :debate ORDER BY p.creationDate DESC ", Argument.class);
        query.setParameter("debate", debate);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }
}
