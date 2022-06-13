package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
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

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class LikeJpaDaoTest {

    private final static String CONTENT = "content";

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";

    private final static String USER_USERNAME_2 = "username2";
    private final static String USER_PASSWORD_2 = "password";
    private final static String USER_EMAIL_2 = "test2@test.com";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";

    private User user;
    private Argument argument;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private LikeJpaDao likeJpaDao;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
        User user2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2);
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, true, user2, null, DebateCategory.OTHER);
        argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        em.persist(user);
        em.persist(user2);
        em.persist(debate);
        em.persist(argument);
    }

    @Test
    public void testLikeArgument() {
        final Like like = likeJpaDao.likeArgument(user, argument);

        assertNotNull(like);
        assertEquals(like, em.find(Like.class, like.getUserPostKey()));
    }

    @Test
    public void testUnlikeArgument() {
        Like like = new Like(user, argument);
        em.persist(like);

        likeJpaDao.unlikeArgument(user, argument);

        assertNull(em.find(Like.class, like.getUserPostKey()));
    }

    @Test
    public void testGetLikeDoesntExist() {
        Optional<Like> like = likeJpaDao.getLike(user, argument);

        assertFalse(like.isPresent());
    }

    @Test
    public void testGetLike() {
        Like like = new Like(user, argument);
        em.persist(like);

        Optional<Like> l = likeJpaDao.getLike(user, argument);

        assertTrue(l.isPresent());
        assertEquals(like, l.get());
    }
}
