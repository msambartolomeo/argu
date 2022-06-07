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
import java.util.concurrent.TimeUnit;

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
    private User user2;
    private Debate debate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ChatJpaDao chatJpaDao;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
        user2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2);
        debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, user2, null, DebateCategory.OTHER);
        em.persist(user);
        em.persist(user2);
        em.persist(debate);
    }

    @Test
    public void testCreateChat() {
        Chat c = chatJpaDao.create(user, debate, MESSAGE);

        assertNotNull(c);
        assertEquals(MESSAGE, c.getMessage());
        assertEquals(user.getUserId(), c.getUser().getUserId());
        assertEquals(debate.getDebateId(), c.getDebate().getDebateId());
    }

    @Test
    public void testGetDebateChatDoesntExist() {
        List<Chat> chats = chatJpaDao.getDebateChat(debate, PAGE);

        assertTrue(chats.isEmpty());
    }

    @Test
    public void testGetDebateChat() {
        em.persist(new Chat(user, debate, MESSAGE));

        List<Chat> chats = chatJpaDao.getDebateChat(debate, PAGE);

        assertFalse(chats.isEmpty());
        assertEquals(1, chats.size());
        assertEquals(MESSAGE, chats.get(0).getMessage());
    }

    @Test
    public void testGetDebateChatPaginationAndOrder() throws InterruptedException {
        for (int i = 0; i <= ChatJpaDao.PAGE_SIZE; i++) { // Create one more than a page
            em.persist(new Chat(user, debate, MESSAGE + i));
            TimeUnit.MILLISECONDS.sleep(1); // TODO: Ask if this is good practice
        }

        List<Chat> chats = chatJpaDao.getDebateChat(debate, PAGE);

        assertFalse(chats.isEmpty());
        assertEquals(ChatJpaDao.PAGE_SIZE, chats.size());

        // Check that the first one is not present
        for (Chat chat : chats) {
            assertNotEquals(MESSAGE + 0, chat.getMessage());
        }

        // test correct order
        for (int i = 1; i <= ChatJpaDao.PAGE_SIZE; i++) {
            assertEquals(MESSAGE + i, chats.get(i - 1).getMessage());
        }
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
