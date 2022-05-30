package ar.edu.itba.paw.persistence;

//import ar.edu.itba.paw.interfaces.dao.DebateDao;
//import ar.edu.itba.paw.model.PublicDebate;
//import ar.edu.itba.paw.model.enums.DebateCategory;
//import ar.edu.itba.paw.model.enums.DebateOrder;
//import ar.edu.itba.paw.model.enums.DebateStatus;
//import ar.edu.itba.paw.model.enums.UserRole;
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//import org.junit.runners.Parameterized.Parameters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.rules.SpringClassRule;
//import org.springframework.test.context.junit4.rules.SpringMethodRule;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(Parameterized.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class DebateJdbcDaoParamTest {
//
//    @ClassRule
//    public static final SpringClassRule scr = new SpringClassRule();
//
//    @Rule
//    public final SpringMethodRule smr = new SpringMethodRule();
//
//    private final static String DEBATE_TABLE = "debates";
//    private final static String DEBATE_TABLE_ID = "debateid";
//
//    private final static String DEBATE_NAME_1 = "A Debate Name Test";
//    private final static String DEBATE_NAME_2 = "B Debate Name Test";
//    private final static String DEBATE_NAME_3 = "C Debate Name Test";
//    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
//    private final static LocalDateTime DEBATE_DATE_1 = LocalDateTime.parse("2022-01-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
//    private final static LocalDateTime DEBATE_DATE_2 = LocalDateTime.parse("2022-01-02T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
//    private final static LocalDateTime DEBATE_DATE_3 = LocalDateTime.parse("2022-01-03T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
//    private final static DebateCategory DEBATE_CATEGORY_1 = DebateCategory.CULTURE;
//    private final static DebateCategory DEBATE_CATEGORY_2 = DebateCategory.POLITICS;
//    private final static DebateCategory DEBATE_CATEGORY_3 = DebateCategory.OTHER;
//    private final static DebateStatus DEBATE_STATUS_OPEN = DebateStatus.OPEN;
//    private final static DebateStatus DEBATE_STATUS_CLOSED = DebateStatus.CLOSED;
//
//    private final static String USER_TABLE = "users";
//    private final static String USER_TABLE_ID = "userid";
//    private final static String USER_USERNAME = "username";
//    private final static String USER_PASSWORD = "password";
//    private final static String USER_EMAIL = "test@test.com";
//    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
//    private final static UserRole USER_ROLE = UserRole.USER;
//
//    private final static String SUBS_TABLE = "subscribed";
//
//    @Autowired
//    private DataSource ds;
//    private JdbcTemplate jdbcTemplate;
//    private DebateDao debateDao;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        debateDao = new DebateJpaDao();
//        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(ds)
//                .withTableName(DEBATE_TABLE)
//                .usingGeneratedKeyColumns(DEBATE_TABLE_ID);
//        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds)
//                .withTableName(USER_TABLE)
//                .usingGeneratedKeyColumns(USER_TABLE_ID);
//
//        // Insert user
//        final Map<String, Object> userData = new HashMap<>();
//        userData.put("username", USER_USERNAME);
//        userData.put("password", USER_PASSWORD);
//        userData.put("email", USER_EMAIL);
//        userData.put("date", USER_DATE.toString());
//        userData.put("role", USER_ROLE.ordinal());
//        long userId = jdbcInsertUser.executeAndReturnKey(userData).longValue();
//
//        // Insert various debates
//        Map<String, Object> debateData = new HashMap<>();
//        debateData.put("name", DEBATE_NAME_1);
//        debateData.put("description", DEBATE_DESCRIPTION);
//        debateData.put("created_date", Timestamp.valueOf(DEBATE_DATE_1));
//        debateData.put("category", DEBATE_CATEGORY_1.ordinal());
//        debateData.put("status", DEBATE_STATUS_OPEN.ordinal());
//        debateData.put("creatorid", userId);
//        debateData.put("opponentid", userId);
//        long debateId1 = jdbcInsert.executeAndReturnKey(debateData).longValue();
//
//        debateData = new HashMap<>();
//        debateData.put("name", DEBATE_NAME_2);
//        debateData.put("description", DEBATE_DESCRIPTION);
//        debateData.put("created_date", Timestamp.valueOf(DEBATE_DATE_2));
//        debateData.put("category", DEBATE_CATEGORY_2.ordinal());
//        debateData.put("status", DEBATE_STATUS_OPEN.ordinal());
//        debateData.put("creatorid", userId);
//        debateData.put("opponentid", userId);
//        long debateId2 = jdbcInsert.executeAndReturnKey(debateData).longValue();
//
//        debateData = new HashMap<>();
//        debateData.put("name", DEBATE_NAME_3);
//        debateData.put("description", DEBATE_DESCRIPTION);
//        debateData.put("created_date", Timestamp.valueOf(DEBATE_DATE_3));
//        debateData.put("category", DEBATE_CATEGORY_3.ordinal());
//        debateData.put("status", DEBATE_STATUS_CLOSED.ordinal());
//        debateData.put("creatorid", userId);
//        debateData.put("opponentid", userId);
//        jdbcInsert.execute(debateData);
//
//        //Insert votes
//        SimpleJdbcInsert jdbcInsertVote = new SimpleJdbcInsert(ds)
//                .withTableName(SUBS_TABLE);
//        Map<String, Object> voteData = new HashMap<>();
//        voteData.put("userid", userId);
//        voteData.put("debateid", debateId1);
//        jdbcInsertVote.execute(voteData);
//
//        voteData = new HashMap<>();
//        voteData.put("userid", userId);
//        voteData.put("debateid", debateId2);
//        jdbcInsertVote.execute(voteData);
//    }
//
//    @After
//    public void tearDown() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, SUBS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEBATE_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
//    }
//
//    @Parameters
//    public static Iterable<Object[]> data() {
//        return Arrays.asList(new Object[][]{
//                {0, 3, null, null, null, null, null, 3, Arrays.asList( // Shows default condition, DebateOrder.DATE_DESC
//                        new PublicDebate(0, DEBATE_NAME_3, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_3, DEBATE_CATEGORY_3, 0, DEBATE_STATUS_CLOSED, 0, 0),
//                        new PublicDebate(0, DEBATE_NAME_2, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_2, DEBATE_CATEGORY_2, 0, DEBATE_STATUS_OPEN, 0, 0),
//                        new PublicDebate(0, DEBATE_NAME_1, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_1, DEBATE_CATEGORY_1, 0, DEBATE_STATUS_OPEN, 0, 0))
//                },
//                {0, 3, null, null, DebateOrder.ALPHA_ASC, null, null, 3, Arrays.asList( // Shows different order
//                        new PublicDebate(0, DEBATE_NAME_1, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_1, DEBATE_CATEGORY_1, 0, DEBATE_STATUS_OPEN, 0, 0),
//                        new PublicDebate(0, DEBATE_NAME_2, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_2, DEBATE_CATEGORY_2, 0, DEBATE_STATUS_OPEN, 0, 0),
//                        new PublicDebate(0, DEBATE_NAME_3, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_3, DEBATE_CATEGORY_3, 0, DEBATE_STATUS_CLOSED, 0, 0))
//                },
//                {0, 2, null, null, DebateOrder.ALPHA_ASC, null, null, 3, Arrays.asList( // Shows pagination limit
//                        new PublicDebate(0, DEBATE_NAME_1, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_1, DEBATE_CATEGORY_1, 0, DEBATE_STATUS_OPEN, 0, 0),
//                        new PublicDebate(0, DEBATE_NAME_2, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_2, DEBATE_CATEGORY_2, 0, DEBATE_STATUS_OPEN, 0, 0))
//                },
//                {0, 3, "a deBate", null, DebateOrder.ALPHA_ASC, null, null, 1, Arrays.asList( // Shows case insensitive search by name
//                        new PublicDebate(0, DEBATE_NAME_1, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_1, DEBATE_CATEGORY_1, 0, DEBATE_STATUS_OPEN, 0, 0))
//                },
//                {0, 3, "no results", null, DebateOrder.ALPHA_ASC, null, null, 0, new ArrayList<>()}, // Shows no results
//                {0, 3, null, DEBATE_CATEGORY_2, null, null, null, 1, Arrays.asList( // Shows filter by category
//                        new PublicDebate(0, DEBATE_NAME_2, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_2, DEBATE_CATEGORY_2, 0, DEBATE_STATUS_OPEN, 0, 0))
//                },
//                {0, 3, null, null, DebateOrder.SUBS_ASC, DebateStatus.OPEN, null, 2, Arrays.asList( // Shows filter by status Open
//                        new PublicDebate(0, DEBATE_NAME_1, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_1, DEBATE_CATEGORY_1, 0, DEBATE_STATUS_OPEN, 0, 0),
//                        new PublicDebate(0, DEBATE_NAME_2, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_2, DEBATE_CATEGORY_2, 0, DEBATE_STATUS_OPEN, 0, 0))
//                },
//                {0, 3, null, null, null, DebateStatus.CLOSED, null, 1, Arrays.asList( // Shows filter by status Closed
//                        new PublicDebate(0, DEBATE_NAME_3, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_3, DEBATE_CATEGORY_3, 0, DEBATE_STATUS_CLOSED, 0, 0))
//                },
//                {0, 3, null, null, null, null, DEBATE_DATE_3.toLocalDate(), 1, Arrays.asList( // Shows filter by created date
//                        new PublicDebate(0, DEBATE_NAME_3, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_3, DEBATE_CATEGORY_3, 0, DEBATE_STATUS_CLOSED, 0, 0))
//                },
//                {0, 3, "test", DEBATE_CATEGORY_1, DebateOrder.DATE_ASC, DEBATE_STATUS_OPEN, DEBATE_DATE_1.toLocalDate(), 1, Arrays.asList( // Shows full configuration, using all filters and an order
//                        new PublicDebate(0, DEBATE_NAME_1, DEBATE_DESCRIPTION, USER_USERNAME, USER_USERNAME, null, DEBATE_DATE_1, DEBATE_CATEGORY_1, 0, DEBATE_STATUS_OPEN, 0, 0))
//                },
//        });
//    }
//    public DebateJdbcDaoParamTest(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order,
//                                  DebateStatus status, LocalDate date, int expectedTotal, List<PublicDebate> expectedDebates) {
//        this.page = page;
//        this.pageSize = pageSize;
//        this.searchQuery = searchQuery;
//        this.category = category;
//        this.order = order;
//        this.status = status;
//        this.date = date;
//        this.expectedTotal = expectedTotal;
//        this.expectedDebates = expectedDebates;
//    }
//    private int page;
//    private int pageSize;
//    private String searchQuery;
//    private DebateCategory category;
//    private DebateOrder order;
//    private DebateStatus status;
//    private LocalDate date;
//    private List<PublicDebate> expectedDebates;
//    private int expectedTotal;
//
//    @Test
//    public void testGetPublicDebatesDiscovery() {
//        List<PublicDebate> debates = debateDao.getPublicDebatesDiscovery(page, pageSize, searchQuery, category, order, status, date);
//
//        assertEquals(expectedDebates.size(), debates.size());
//        for (int i = 0; i < debates.size(); i++) {
//            assertEquals(expectedDebates.get(i).getName(), debates.get(i).getName());
//            assertEquals(expectedDebates.get(i).getDescription(), debates.get(i).getDescription());
//            assertEquals(expectedDebates.get(i).getCreatorUsername(), debates.get(i).getCreatorUsername());
//            assertEquals(expectedDebates.get(i).getOpponentUsername(), debates.get(i).getOpponentUsername());
//            assertEquals(expectedDebates.get(i).getCreatedDate(), debates.get(i).getCreatedDate());
//            assertEquals(expectedDebates.get(i).getDebateCategory(), debates.get(i).getDebateCategory());
//            assertEquals(expectedDebates.get(i).getDebateStatus(), debates.get(i).getDebateStatus());
//            assertEquals(expectedDebates.get(i).getForCount(), debates.get(i).getForCount());
//            assertEquals(expectedDebates.get(i).getAgainstCount(), debates.get(i).getAgainstCount());
//        }
//    }
//
//    @Test
//    public void testGetPublicDebatesCount() {
//        int count = debateDao.getPublicDebatesCount(searchQuery, category, status, date);
//
//        assertEquals(expectedTotal, count);
//        System.out.println("testGetPublicDebatesCount");
//    }
//}
