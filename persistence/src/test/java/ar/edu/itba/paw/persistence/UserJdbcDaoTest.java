package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
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
    private SimpleJdbcInsert jdbcInsertSubscribed;
    private SimpleJdbcInsert jdbcInsertImages;
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

    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static String DEBATE_DATE = LocalDateTime.parse("2022-01-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;
    private final static DebateStatus DEBATE_STATUS = DebateStatus.OPEN;
    private final static String DEBATES_TABLE = "debates";
    private final static String DEBATES_ID = "debateid";

    private final static String SUBSCRIBED_TABLE = "subscribed";

    private final static String IMAGE_TABLE = "images";
    private final static String IMAGE_TABLE_ID = "imageid";
    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3};


    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns(ID);
        jdbcInsertDebates = new SimpleJdbcInsert(ds)
                .withTableName(DEBATES_TABLE)
                .usingGeneratedKeyColumns(DEBATES_ID);
        jdbcInsertSubscribed = new SimpleJdbcInsert(ds)
                .withTableName(SUBSCRIBED_TABLE);
        jdbcInsertImages = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE)
                .usingGeneratedKeyColumns(IMAGE_TABLE_ID);
    }
    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE, SUBSCRIBED_TABLE);
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
    public void testGetUserByUsernameDoesntExist() {
        Optional<User> user = userDao.getUserByUsername(USER_USERNAME);

        assertFalse(user.isPresent());
    }

    @Test
    public void testGetUserByUsernameExists() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("created_date", USER_DATE.toString());
        userData.put("role", UserRole.getValue(USER_ROLE));
        Number key = jdbcInsert.executeAndReturnKey(userData);

        Optional<User> user = userDao.getUserByUsername(USER_USERNAME);

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

    @Test
    public void testGetSubscribedUsersByDebateEmpty() {
        List<User> users = userDao.getSubscribedUsersByDebate(DEBATE_ID);

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetSubscribedUsersByDebate() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("created_date", USER_DATE.toString());
        userData.put("role", UserRole.getValue(USER_ROLE));
        Number userKey = jdbcInsert.executeAndReturnKey(userData);

        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE.toString());
        debateData.put("status", DebateStatus.getFromStatus(DEBATE_STATUS));
        debateData.put("category", DebateCategory.getFromCategory(DEBATE_CATEGORY));
        debateData.put("creatorid", userKey.longValue());
        debateData.put("opponentid", userKey.longValue());
        Number debateKey = jdbcInsertDebates.executeAndReturnKey(debateData);

        final Map<String, Object> subscribedData = new HashMap<>();
        subscribedData.put("debateid", debateKey.longValue());
        subscribedData.put("userid", userKey.longValue());
        jdbcInsertSubscribed.execute(subscribedData);


        List<User> users = userDao.getSubscribedUsersByDebate(debateKey.longValue());

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(USER_USERNAME, users.get(0).getUsername());
        assertEquals(USER_PASSWORD, users.get(0).getPassword());
        assertEquals(USER_EMAIL, users.get(0).getEmail());
        assertEquals(USER_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), users.get(0).getCreatedDate());
        assertEquals(USER_ROLE, users.get(0).getRole());
    }

    @Test
    public void testUpdateImage() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("created_date", USER_DATE.toString());
        userData.put("role", UserRole.getValue(USER_ROLE));
        Number userKey = jdbcInsert.executeAndReturnKey(userData);

        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("data", IMAGE_DATA);
        Number imageKey = jdbcInsertImages.executeAndReturnKey(imageData);

        userDao.updateImage(userKey.longValue(), imageKey.longValue());
    }
}
