package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ArgumentJpaDaoTest {

    private final static String CONTENT = "content";
    private final static long ARGUMENT_ID = 1;

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
    private Image image;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ArgumentJpaDao argumentJpaDao;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
        User user2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2);
        debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, true, user2, null, DebateCategory.OTHER);
        em.persist(user);
        em.persist(user2);
        em.persist(debate);
    }

    @Test
    public void testCreateArgument() {
        final Argument argument = argumentJpaDao.create(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);

        assertNotNull(argument);
        assertEquals(argument, em.find(Argument.class, argument.getArgumentId()));
    }

    @Test
    public void testGetArgumentByIdDoesntExist() {
        Optional<Argument> argument = argumentJpaDao.getArgumentById(ARGUMENT_ID);

        assertFalse(argument.isPresent());
    }

    @Test
    public void testGetArgumentById() {
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        em.persist(argument);

        Optional<Argument> a = argumentJpaDao.getArgumentById(argument.getArgumentId());

        assertTrue(a.isPresent());
        assertEquals(argument, a.get());
    }

    @Test
    public void testGetLastArgumentDoesntExist() {
        Optional<Argument> argument = argumentJpaDao.getLastArgument(debate);

        assertFalse(argument.isPresent());
    }

    @Test
    public void testGetLastArgument() {
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        em.persist(argument);

        Optional<Argument> a = argumentJpaDao.getLastArgument(debate);

        assertTrue(a.isPresent());
        assertEquals(argument, a.get());
    }

    @Test
    public void testGetArgumentsByDebateCountNoArguments() {
        int count = argumentJpaDao.getArgumentsByDebateCount(debate.getDebateId());

        assertEquals(0, count);
    }

    @Test
    public void testGetArgumentsByDebateCount() {
        em.persist(new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT));

        int count = argumentJpaDao.getArgumentsByDebateCount(debate.getDebateId());

        assertEquals(1, count);
    }

    @Test
    public void getArgumentsByDebateDoesntExist() {
        List<Argument> arguments = argumentJpaDao.getArgumentsByDebate(debate, PAGE);

        assertTrue(arguments.isEmpty());
    }

    @Test
    public void getArgumentsByDebate() {
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        em.persist(argument);

        List<Argument> arguments = argumentJpaDao.getArgumentsByDebate(debate, PAGE);

        assertFalse(arguments.isEmpty());
        assertEquals(1, arguments.size());
        assertEquals(argument, arguments.get(0));
    }

}
