package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateVote;
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
public class VoteJpaDaoTest {

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

    private final static DebateVote VOTE = DebateVote.FOR;

    private User user;
    private Debate debate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private VoteJpaDao voteJpaDao;

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
    public void testAddVote() {
        Vote vote = voteJpaDao.addVote(user, debate, VOTE);

        assertNotNull(vote);
        assertEquals(vote, em.find(Vote.class, vote.getUserDebateKey()));
    }

    @Test
    public void testGetVote() {
        Vote vote = new Vote(user, debate, VOTE);
        em.persist(vote);

        Optional<Vote> v = voteJpaDao.getVote(user, debate);

        assertTrue(v.isPresent());
        assertEquals(vote, v.get());
    }

    @Test
    public void testGetVoteNotExists() {
        Optional<Vote> vote = voteJpaDao.getVote(user, debate);

        assertFalse(vote.isPresent());
    }

    @Test
    public void testDeleteVote() {
        Vote vote = new Vote(user, debate, VOTE);
        em.persist(vote);

        voteJpaDao.delete(vote);

        assertNull(em.find(Vote.class, vote.getUserDebateKey()));
    }
}
