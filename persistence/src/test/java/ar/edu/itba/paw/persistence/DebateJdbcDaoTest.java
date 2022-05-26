package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DebateJdbcDaoTest {
    @Autowired
    private DataSource ds;

    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static String DEBATE_DATE = LocalDateTime.parse("2022-01-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final static String PUBLIC_DEBATE_DATE = LocalDateTime.parse(DEBATE_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
    private final static DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;
    private final static DebateStatus DEBATE_STATUS = DebateStatus.OPEN;
    private final static DebateVote DEBATE_VOTE_FOR = DebateVote.FOR;
    private final static DebateVote DEBATE_VOTE_AGAINST = DebateVote.AGAINST;
    private final static String DEBATES_TABLE = "debates";
    private final static String DEBATES_TABLE_ID = "debateid";
    private final static int DEBATES_PAGE = 0;

    private final static String USER_USERNAME = "username";
    private final static String USER_OTHER_USERNAME = "otherusername";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static String USER_OTHER_EMAIL = "test@other.com";
    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
    private final static UserRole USER_ROLE = UserRole.USER;
    private final static String USER_TABLE = "users";
    private final static String USER_TABLE_ID = "userid";

    private final static String SUBSCRIBED_TABLE = "subscribed";

    private final static String IMAGES_TABLE = "images";
    private final static String IMAGE_TABLE_ID = "imageid";
    private final static byte[] IMAGE_DATA = new byte[] {1, 2, 3, 4, 5};

    private final static String VOTES_TABLE = "votes";

    private DebateJpaDao debateDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertImage;
    private SimpleJdbcInsert jdbcInsertSubscribed;
    private SimpleJdbcInsert jdbcInsertVotes;
    private Long userId;
    @Before
    public void setUp() {
        debateDao = new DebateJpaDao();
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(DEBATES_TABLE)
                .usingGeneratedKeyColumns(DEBATES_TABLE_ID);
        jdbcInsertUser = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns(USER_TABLE_ID);
        jdbcInsertImage = new SimpleJdbcInsert(ds)
                .withTableName(IMAGES_TABLE)
                .usingGeneratedKeyColumns(IMAGE_TABLE_ID);
        jdbcInsertSubscribed = new SimpleJdbcInsert(ds)
                .withTableName(SUBSCRIBED_TABLE);
        jdbcInsertVotes = new SimpleJdbcInsert(ds)
                .withTableName(VOTES_TABLE);

        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("date", USER_DATE.toString());
        userData.put("role", USER_ROLE.ordinal());
        userId = jdbcInsertUser.executeAndReturnKey(userData).longValue();
    }

    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, VOTES_TABLE, SUBSCRIBED_TABLE, IMAGES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEBATES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
    }

    @Test
    public void testCreate() {
        Debate debate = debateDao.create(DEBATE_NAME, DEBATE_DESCRIPTION, userId, userId, null, DEBATE_CATEGORY);

        assertNotNull(debate);
        assertEquals(DEBATE_NAME, debate.getName());
        assertEquals(DEBATE_DESCRIPTION, debate.getDescription());
        assertEquals(userId, debate.getCreatorId());
        assertEquals(userId, debate.getOpponentId());
        assertEquals(DEBATE_CATEGORY, debate.getCategory());
    }

    @Test
    public void testCreateWithImage() {
        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("data", IMAGE_DATA);
        final Long imageId = jdbcInsertImage.executeAndReturnKey(imageData).longValue();

        Debate debate = debateDao.create(DEBATE_NAME, DEBATE_DESCRIPTION, userId, userId, imageId, DEBATE_CATEGORY);

        assertNotNull(debate);
        assertEquals(DEBATE_NAME, debate.getName());
        assertEquals(DEBATE_DESCRIPTION, debate.getDescription());
        assertEquals(userId, debate.getCreatorId());
        assertEquals(userId, debate.getOpponentId());
        assertEquals(imageId, debate.getImageId());
        assertEquals(DEBATE_CATEGORY, debate.getCategory());
    }

    @Test
    public void testGetPublicDebateByIdEmpty() {
        Optional<PublicDebate> debate = debateDao.getPublicDebateById(DEBATE_ID);

        assertFalse(debate.isPresent());
    }

    @Test
    public void testGetPublicDebateById() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();


        Optional<PublicDebate> debate = debateDao.getPublicDebateById(debateKey);

        assertTrue(debate.isPresent());
        assertEquals(DEBATE_NAME, debate.get().getName());
        assertEquals(DEBATE_DESCRIPTION, debate.get().getDescription());
        assertEquals(PUBLIC_DEBATE_DATE, debate.get().getCreatedDate());
        assertEquals(DEBATE_STATUS, debate.get().getDebateStatus());
        assertEquals(DEBATE_CATEGORY, debate.get().getDebateCategory());
        assertEquals(USER_USERNAME, debate.get().getCreatorUsername());
        assertEquals(USER_USERNAME, debate.get().getOpponentUsername());
    }

    @Test
    public void testGetSuscribedDebatesEmpty() {
        List<PublicDebate> debates = debateDao.getSubscribedDebatesByUserId(userId, DEBATES_PAGE);

        assertTrue(debates.isEmpty());
    }

    @Test
    public void testGetSuscribedDebates() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> subscribedData = new HashMap<>();
        subscribedData.put("userid", userId);
        subscribedData.put("debateid", debateKey);
        jdbcInsertSubscribed.execute(subscribedData);

        List<PublicDebate> debates = debateDao.getSubscribedDebatesByUserId(userId, DEBATES_PAGE);

        assertFalse(debates.isEmpty());
        assertEquals(DEBATE_NAME, debates.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, debates.get(0).getDescription());
        assertEquals(PUBLIC_DEBATE_DATE, debates.get(0).getCreatedDate());
        assertEquals(DEBATE_STATUS, debates.get(0).getDebateStatus());
        assertEquals(DEBATE_CATEGORY, debates.get(0).getDebateCategory());
        assertEquals(USER_USERNAME, debates.get(0).getCreatorUsername());
        assertEquals(USER_USERNAME, debates.get(0).getOpponentUsername());
    }

    @Test
    public void testGetSubscribedDebatesByUserIdCountEmpty() {
        int count = debateDao.getSubscribedDebatesByUserIdCount(userId);

        assertEquals(0, count);
    }

    @Test
    public void testGetSubscribedDebatesByUserIdCount() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> subscribedData = new HashMap<>();
        subscribedData.put("userid", userId);
        subscribedData.put("debateid", debateKey);
        jdbcInsertSubscribed.execute(subscribedData);

        int count = debateDao.getSubscribedDebatesByUserIdCount(userId);

        assertEquals(1, count);
    }

    @Test
    public void testSubscribeToDebate() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        debateDao.subscribeToDebate(userId, debateKey);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SUBSCRIBED_TABLE));
    }

    @Test
    public void testUnsubscribeDebate() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> subscribedData = new HashMap<>();
        subscribedData.put("userid", userId);
        subscribedData.put("debateid", debateKey);
        jdbcInsertSubscribed.execute(subscribedData);

        debateDao.unsubscribeToDebate(userId, debateKey);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, SUBSCRIBED_TABLE));
    }

    @Test
    public void testUnsubscribeDebateEmpty() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        debateDao.unsubscribeToDebate(userId, debateKey);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, SUBSCRIBED_TABLE));
    }

    @Test
    public void testIsUserSubscribedEmpty() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        boolean isSubscribed = debateDao.isUserSubscribed(userId, debateKey);

        assertFalse(isSubscribed);
    }

    @Test
    public void testIsUserSubscribed() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> subscribedData = new HashMap<>();
        subscribedData.put("userid", userId);
        subscribedData.put("debateid", debateKey);
        jdbcInsertSubscribed.execute(subscribedData);

        boolean isSubscribed = debateDao.isUserSubscribed(userId, debateKey);

        assertTrue(isSubscribed);
    }

    @Test
    public void testGetMyDebatesSameUser() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        List<PublicDebate> debates = debateDao.getMyDebates(userId, DEBATES_PAGE);

        assertEquals(1, debates.size());
        assertEquals(debateKey, debates.get(0).getDebateId());
        assertEquals(DEBATE_NAME, debates.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, debates.get(0).getDescription());
        assertEquals(PUBLIC_DEBATE_DATE, debates.get(0).getCreatedDate());
        assertEquals(DEBATE_STATUS, debates.get(0).getDebateStatus());
        assertEquals(DEBATE_CATEGORY, debates.get(0).getDebateCategory());
        assertEquals(USER_USERNAME, debates.get(0).getCreatorUsername());
        assertEquals(USER_USERNAME, debates.get(0).getOpponentUsername());
    }

    @Test
    public void testGetMyDebatesDifferentUser() {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_OTHER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_OTHER_EMAIL);
        userData.put("created_date", USER_DATE.toString());
        userData.put("role", USER_ROLE.ordinal());
        long userKey = jdbcInsertUser.executeAndReturnKey(userData).longValue();

        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userKey);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        List<PublicDebate> debates = debateDao.getMyDebates(userKey, DEBATES_PAGE);

        assertEquals(1, debates.size());
        assertEquals(debateKey, debates.get(0).getDebateId());
        assertEquals(DEBATE_NAME, debates.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, debates.get(0).getDescription());
        assertEquals(PUBLIC_DEBATE_DATE, debates.get(0).getCreatedDate());
        assertEquals(DEBATE_STATUS, debates.get(0).getDebateStatus());
        assertEquals(DEBATE_CATEGORY, debates.get(0).getDebateCategory());
        assertEquals(USER_USERNAME, debates.get(0).getCreatorUsername());
        assertEquals(USER_OTHER_USERNAME, debates.get(0).getOpponentUsername());
    }

    @Test
    public void testGetMyDebatesCount() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        jdbcInsert.execute(debateData);

        int count = debateDao.getMyDebatesCount(userId);

        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DEBATES_TABLE,
                "creatorid = " + userId  +" OR opponentid = " + userId), count);
    }

    @Test
    public void testGetMyDebatesCountEmpty() {
        int count = debateDao.getMyDebatesCount(userId);

        assertEquals(0, count);
    }

    @Test
    public void testAddVoteFor() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        debateDao.addVote(debateKey, userId, DEBATE_VOTE_FOR);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VOTES_TABLE, "vote = " + DEBATE_VOTE_FOR.ordinal()));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VOTES_TABLE, "vote = " + DEBATE_VOTE_AGAINST.ordinal()));
    }

    @Test
    public void testAddVoteAgainst() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        debateDao.addVote(debateKey, userId, DEBATE_VOTE_AGAINST);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VOTES_TABLE, "vote = " + DEBATE_VOTE_FOR.ordinal()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VOTES_TABLE, "vote = " + DEBATE_VOTE_AGAINST.ordinal()));
    }

    @Test
    public void testRemoveVoteEmpty() {
        debateDao.removeVote(DEBATE_ID, userId);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, VOTES_TABLE));
    }

    @Test
    public void testRemoveVote() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> voteData = new HashMap<>();
        voteData.put("debateid", debateKey);
        voteData.put("userid", userId);
        voteData.put("vote", DEBATE_VOTE_FOR.ordinal());
        jdbcInsertVotes.execute(voteData);

        debateDao.removeVote(debateKey, userId);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, VOTES_TABLE));
    }

    @Test
    public void testHasUserVotedFalse() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        boolean result = debateDao.hasUserVoted(debateKey, userId);

        assertFalse(result);
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, VOTES_TABLE));
    }

    @Test
    public void testHasUserVotedTrue() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> voteData = new HashMap<>();
        voteData.put("debateid", debateKey);
        voteData.put("userid", userId);
        voteData.put("vote", DEBATE_VOTE_FOR.ordinal());
        jdbcInsertVotes.execute(voteData);

        boolean result = debateDao.hasUserVoted(debateKey, userId);

        assertTrue(result);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VOTES_TABLE, "vote = " + DEBATE_VOTE_FOR.ordinal()));
    }
    @Test
    public void testGetUserVoteFor() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> voteData = new HashMap<>();
        voteData.put("debateid", debateKey);
        voteData.put("userid", userId);
        voteData.put("vote", DEBATE_VOTE_FOR.ordinal());
        jdbcInsertVotes.execute(voteData);

        DebateVote result = debateDao.getUserVote(debateKey, userId);

        assertEquals(DEBATE_VOTE_FOR, result);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, VOTES_TABLE));
    }

    @Test
    public void testGetUserVoteAgainst() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> voteData = new HashMap<>();
        voteData.put("debateid", debateKey);
        voteData.put("userid", userId);
        voteData.put("vote", DEBATE_VOTE_AGAINST.ordinal());
        jdbcInsertVotes.execute(voteData);

        DebateVote result = debateDao.getUserVote(debateKey, userId);

        assertEquals(DEBATE_VOTE_AGAINST, result);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, VOTES_TABLE));
    }

    @Test
    public void testChangeDebateStatusEmpty() {
        debateDao.changeDebateStatus(DEBATE_ID, DebateStatus.OPEN);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, DEBATES_TABLE));
    }

    @Test
    public void testChangeDebateStatus() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("status", DEBATE_STATUS.ordinal());
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("creatorid", userId);
        debateData.put("opponentid", userId);
        long debateKey = jdbcInsert.executeAndReturnKey(debateData).longValue();

        debateDao.changeDebateStatus(debateKey, DebateStatus.CLOSED);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DEBATES_TABLE, "status = " + DebateStatus.OPEN.ordinal()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DEBATES_TABLE, "status = " + DebateStatus.CLOSED.ordinal()));
    }
}