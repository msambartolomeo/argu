package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Subscribed;
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

import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class SubscribedJpaDaoTest {

    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Test";
    private final static String DEBATE_DESCRIPTION = "Debate Test Description";
    private final static DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;

    private final static boolean IS_CREATOR_FOR = true;

    private final static String USER_EMAIL = "user@test.com";
    private final static String USER_USERNAME = "u_username";
    private final static String USER_PASSWORD = "u_password";

    private final static String USER2_EMAIL = "user2@test.com";
    private final static String USER2_USERNAME = "u2_username";
    private final static String USER2_PASSWORD = "u2_password";

    private User user;
    private Debate debate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SubscribedJpaDao subscribedJpaDao;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        User user2 = new User(USER2_EMAIL, USER2_USERNAME, USER2_PASSWORD, Locale.ENGLISH);
        debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, IS_CREATOR_FOR, user2, null, DEBATE_CATEGORY);

        em.persist(user);
        em.persist(user2);
        em.persist(debate);
    }

    @Test
    public void testSubscribeToDebate() {
        Subscribed subscribed = subscribedJpaDao.subscribeToDebate(user, debate);

        assertNotNull(subscribed);
        assertEquals(subscribed, em.find(Subscribed.class, subscribed.getUserDebateKey()));
    }

    @Test
    public void testGetSubscribed() {
        Subscribed subscribed = new Subscribed(user, debate);
        em.persist(subscribed);

        Optional<Subscribed> s = subscribedJpaDao.getSubscribed(user, debate);

        assertTrue(s.isPresent());
        assertEquals(subscribed, s.get());
    }

    @Test
    public void testGetSubscribedNotExists() {
        Optional<Subscribed> subscribed = subscribedJpaDao.getSubscribed(user, debate);

        assertFalse(subscribed.isPresent());
    }

    @Test
    public void testUnsubscribe() {
        Subscribed subscribed = new Subscribed(user, debate);
        em.persist(subscribed);

        subscribedJpaDao.unsubscribe(subscribed);

        assertNull(em.find(Subscribed.class, subscribed.getUserDebateKey()));
    }
}
