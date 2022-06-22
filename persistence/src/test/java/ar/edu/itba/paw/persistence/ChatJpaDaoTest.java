package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ChatJpaDaoTest {

    private final static String MESSAGE = "message";

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";

    private final static String USER_USERNAME_2 = "username2";
    private final static String USER_PASSWORD_2 = "password";
    private final static String USER_EMAIL_2 = "test2@test.com";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";

    private final static int PAGE = 0;

    private User user;
    private Debate debate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ChatJpaDao chatJpaDao;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        User user2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2, Locale.ENGLISH);
        debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, true, user2, null, DebateCategory.OTHER);
        em.persist(user);
        em.persist(user2);
        em.persist(debate);
    }

    @Test
    public void testCreateChat() {
        final Chat chat = chatJpaDao.create(user, debate, MESSAGE);

        assertNotNull(chat);
        assertEquals(chat, em.find(Chat.class, chat.getChatId()));
    }

    @Test
    public void testGetDebateChatDoesntExist() {
        List<Chat> chats = chatJpaDao.getDebateChat(debate, PAGE);

        assertTrue(chats.isEmpty());
    }

    @Test
    public void testGetDebateChat() {
        Chat chat = new Chat(user, debate, MESSAGE);
        em.persist(chat);

        List<Chat> chats = chatJpaDao.getDebateChat(debate, PAGE);

        assertFalse(chats.isEmpty());
        assertEquals(1, chats.size());
        assertEquals(chat, chats.get(0));
    }

    @Test
    public void testGetDebateChatsCountNoChats() {
        int count = chatJpaDao.getDebateChatsCount(debate.getDebateId());

        assertEquals(0, count);
    }

    @Test
    public void testGetDebateChatsCount() {
        em.persist(new Chat(user, debate, MESSAGE));

        int count = chatJpaDao.getDebateChatsCount(debate.getDebateId());

        assertEquals(1, count);
    }
}
