package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.UserRole;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {

    private UserJdbcDao userDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertDebates;
    private SimpleJdbcInsert jdbcInsertPosts;
    @Autowired
    private DataSource ds;

    private final static long USER_ID = 1;
    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
    private final static UserRole USER_ROLE = UserRole.USER;
    private final static String USER_TABLE = "users";
    private final static String ID = "userid";
    private final static int USERS_PAGE = 0;

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static String DEBATES_TABLE = "debates";
    private final static String DEBATES_ID = "debateid";

    private final static String POST_CONTENT = "Post Content";
    private final static String POSTS_TABLE = "posts";
    private final static String POSTS_ID = "postid";

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns(ID);
        jdbcInsertPosts = new SimpleJdbcInsert(ds)
                .withTableName(POSTS_TABLE)
                .usingGeneratedKeyColumns(POSTS_ID);
        jdbcInsertDebates = new SimpleJdbcInsert(ds)
                .withTableName(DEBATES_TABLE)
                .usingGeneratedKeyColumns(DEBATES_ID);
    }
    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, POSTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEBATES_TABLE, USER_TABLE);
    }

    @Test
    public void testCreateUser() {
        User user = userDao.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL);

        assertNotNull(user);
        assertEquals(USER_USERNAME, user.getUsername());
        assertEquals(USER_PASSWORD, user.getPassword());
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(UserRole.USER, user.getRole());
        assertNull(user.getImageId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testGetUserByIdExists() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("created_date", USER_DATE.toString());
        userData.put("role", UserRole.getValue(USER_ROLE));
        Number key = jdbcInsert.executeAndReturnKey(userData);

        Optional<User> user = userDao.getUserById(key.longValue());

        assertTrue(user.isPresent());
        assertEquals(USER_USERNAME, user.get().getUsername());
        assertEquals(USER_PASSWORD, user.get().getPassword());
        assertEquals(USER_EMAIL, user.get().getEmail());
        assertEquals(USER_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), user.get().getCreatedDate());
        assertEquals(USER_ROLE, user.get().getRole());
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
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("created_date", USER_DATE.toString());
        userData.put("role", UserRole.getValue(USER_ROLE));
        Number key = jdbcInsert.executeAndReturnKey(userData);

        Optional<User> user = userDao.getUserByEmail(USER_EMAIL);

        assertTrue(user.isPresent());
        assertEquals(USER_USERNAME, user.get().getUsername());
        assertEquals(USER_PASSWORD, user.get().getPassword());
        assertEquals(USER_EMAIL, user.get().getEmail());
        assertEquals(USER_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), user.get().getCreatedDate());
        assertEquals(USER_ROLE, user.get().getRole());
    }

    @Test
    public void testUpdateLegacyUser() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", USER_EMAIL);
        Number key = jdbcInsert.executeAndReturnKey(userData);

        User user = userDao.updateLegacyUser(key.longValue(), USER_USERNAME, USER_PASSWORD, USER_EMAIL);

        assertNotNull(user);
        assertEquals(USER_USERNAME, user.getUsername());
        assertEquals(USER_PASSWORD, user.getPassword());
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(UserRole.USER, user.getRole());
        assertNull(user.getImageId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

}
