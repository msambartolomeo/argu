package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.UserRole;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@ContextConfiguration(classes = TestConfig.class)
public class DebateJdbcDaoParamTest {

    @ClassRule
    public static final SpringClassRule scr = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();

    private final static String DEBATE_TABLE = "debate";
    private final static String DEBATE_TABLE_ID = "debateid";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static String DEBATE_DATE_1 = LocalDateTime.parse("2022-01-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final static String DEBATE_DATE_2 = LocalDateTime.parse("2022-01-02T00:00:00", DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final static String DEBATE_DATE_3 = LocalDateTime.parse("2022-01-03T00:00:00", DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final static String PUBLIC_DEBATE_DATE_1 = LocalDateTime.parse(DEBATE_DATE_1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy"));
    private final static String PUBLIC_DEBATE_DATE_2 = LocalDateTime.parse(DEBATE_DATE_2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy"));
    private final static String PUBLIC_DEBATE_DATE_3 = LocalDateTime.parse(DEBATE_DATE_3, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy"));
    private final static DebateCategory DEBATE_CATEGORY_OTHER = DebateCategory.OTHER;
    private final static DebateCategory DEBATE_CATEGORY_CULTURE = DebateCategory.CULTURE;
    private final static DebateCategory DEBATE_CATEGORY_POLITICS = DebateCategory.POLITICS;
    private final static DebateStatus DEBATE_STATUS_OPEN = DebateStatus.OPEN;
    private final static DebateStatus DEBATE_STATUS_CLOSED = DebateStatus.CLOSED;

    private final static String USER_TABLE = "users";
    private final static String USER_TABLE_ID = "userid";
    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
    private final static UserRole USER_ROLE = UserRole.USER;

    @Autowired
    private DataSource ds;
    private JdbcTemplate jdbcTemplate;
    private DebateDao debateDao;
    private long userId;
    private long debateId;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        debateDao = new DebateJdbcDao(ds);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(DEBATE_TABLE)
                .usingGeneratedKeyColumns(DEBATE_TABLE_ID);
        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns(USER_TABLE_ID);

        // Insert user
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("date", USER_DATE.toString());
        userData.put("role", USER_ROLE.ordinal());
        userId = jdbcInsertUser.executeAndReturnKey(userData).longValue();

        // Insert various debates
        Map<String, Object> debateData = new HashMap<>();
    }

    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEBATE_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
    }

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, 10, null, null, null, null, null, new ArrayList<PublicDebate>()},
                {0, 10, null, null, null, null, null, new ArrayList<PublicDebate>()},
        });
    }
    public DebateJdbcDaoParamTest(int page, int pageSize, String searchQuery, DebateCategory category,
                                  DebateOrder order, DebateStatus status, LocalDate date,
                                  List<PublicDebate> expectedDebates) {
        this.page = page;
        this.pageSize = pageSize;
        this.searchQuery = searchQuery;
        this.category = category;
        this.order = order;
        this.status = status;
        this.date = date;
        this.expectedDebates = expectedDebates;
    }
    private int page;
    private int pageSize;
    private String searchQuery;
    private DebateCategory category;
    private DebateOrder order;
    private DebateStatus status;
    private LocalDate date;
    private List<PublicDebate> expectedDebates;

    @Test
    public void testGetPublicDebatesDiscovery() {
        List<PublicDebate> debates = debateDao.getPublicDebatesGeneral(page, pageSize, searchQuery, category, order, status, date);

        assertArrayEquals(expectedDebates.toArray(), debates.toArray());
    }

    @Test
    public void testGetPublicDebatesCount() {
        int count = debateDao.getPublicDebatesCount(searchQuery, category, status, date);

        assertEquals(expectedDebates.size(), count);
    }
}
