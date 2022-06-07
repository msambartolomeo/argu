package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ChatDao;
import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
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
public class ChatJpaDao implements ChatDao {

    static final int PAGE_SIZE = 15;
    @PersistenceContext
    private EntityManager em;

    @Override
    public Chat create(User user, Debate debate, String message) {
        final Chat chat = new Chat(user, debate, message);
        em.persist(chat);
        return chat;
    }

    @Override
    public List<Chat> getDebateChat(Debate debate, int page) {
        final Query idQuery = em.createNativeQuery("SELECT chatid FROM chats WHERE debateid = :debateid ORDER BY created_date DESC LIMIT :page_size OFFSET :offset");
        idQuery.setParameter("debateid", debate.getDebateId());
        idQuery.setParameter("page_size", PAGE_SIZE);
        idQuery.setParameter("offset", page * PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((BigInteger) o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Chat> query = em.createQuery("FROM Chat c WHERE c.chatId IN :ids", Chat.class);
        query.setParameter("ids", ids);

        List<Chat> chats = query.getResultList();
        chats.sort(Comparator.comparing(Chat::getCreationDate));
        return chats;
    }

    @Override
    public int getDebateChatsCount(long debateId) {
        final Query query = em.createNativeQuery("SELECT COUNT(*) FROM chats WHERE debateid = :debateid");
        query.setParameter("debateid", debateId);

        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> ((BigInteger) o).intValue()).orElse(0);
    }
}
