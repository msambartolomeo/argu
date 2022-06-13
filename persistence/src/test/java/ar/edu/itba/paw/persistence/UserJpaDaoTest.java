package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
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
public class UserJpaDaoTest {

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserJpaDao userJpaDao;

    @Test
    public void testCreate() {
        final User user = userJpaDao.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL);

        assertNotNull(user);
        assertEquals(user, em.find(User.class, user.getUserId()));
    }

    @Test
    public void getUserByUsernameDoesntExist() {
        final Optional<User> user = userJpaDao.getUserByUsername(USER_USERNAME);

        assertFalse(user.isPresent());
    }

    @Test
    public void getUserByUsername() {
        final User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
        em.persist(user);

        Optional<User> u = userJpaDao.getUserByUsername(USER_USERNAME);

        assertTrue(u.isPresent());
        assertEquals(user, u.get());
    }

    @Test
    public void getUserByEmailDoesntExist() {
        final Optional<User> user = userJpaDao.getUserByEmail(USER_EMAIL);

        assertFalse(user.isPresent());
    }

    @Test
    public void getUserByEmail() {
        final User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
        em.persist(user);

        Optional<User> u = userJpaDao.getUserByEmail(USER_EMAIL);

        assertTrue(u.isPresent());
        assertEquals(user, u.get());
    }

}
