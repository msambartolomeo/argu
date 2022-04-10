package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {

    private UserJdbcDao userDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    private DataSource ds;

    private final static long USER_ID = 1;
    private final static String USER_EMAIL = "test@test.com";
    private final static String USER_TABLE = "users";
    private final static String ID = "userid";
    private final static int USERS_PAGE = 0;

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns(ID);
    }
    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
    }

    @Test
    public void testCreateUser() {
        User user = userDao.create(USER_EMAIL);

        assertNotNull(user);
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testGetUserByIdExists() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", USER_EMAIL);
        Number key = jdbcInsert.executeAndReturnKey(userData);

        Optional<User> user = userDao.getUserById(key.longValue());

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
    }
    @Test
    public void testGetUserByIdDoesntExist() {

        Optional<User> user = userDao.getUserById(USER_ID);

        assertFalse(user.isPresent());
    }

    @Test
    public void testGetUserByEmailDoesntExist() {
        Optional<User> user = userDao.getUserByEmail(USER_EMAIL);

        assertFalse(user.isPresent());
    }

    @Test
    public void testGetUserByEmailExists() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", USER_EMAIL);
        jdbcInsert.execute(userData);

        Optional<User> user = userDao.getUserByEmail(USER_EMAIL);

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
    }

    @Test
    public void testGetAllUsersEmpty() {

        List<User> noUsers = userDao.getAll(USERS_PAGE);

        assertTrue(noUsers.isEmpty());
    }

    @Test
    public void testGetAllUsers() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", USER_EMAIL);
        jdbcInsert.execute(userData);

        List<User> users = userDao.getAll(USERS_PAGE);

        assertEquals(1, users.size());
        assertEquals(USER_EMAIL, users.get(0).getEmail());
    }
}
