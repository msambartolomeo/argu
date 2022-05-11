package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;
import ar.edu.itba.paw.model.PublicPostWithUserLike;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
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
import static org.junit.Assert.*;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PostJdbcDaoTest {
    @Autowired
    private DataSource ds;

    private final static String POSTS_TABLE = "posts";
    private final static String POST_TABLE_ID = "postid";
    private final static String POST_CONTENT = "Post Content";
    private final static String POST_DATE = LocalDateTime.parse("2022-01-01T00:00:00")
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final static ArgumentStatus POST_STATUS = ArgumentStatus.ARGUMENT;
    private final static int POST_PAGE = 0;


    private final static String IMAGES_TABLE = "images";
    private final static String IMAGE_TABLE_ID = "imageid";
    private final static byte[] IMAGE_DATA = new byte[] {1, 2, 3, 4, 5};

    private final static String USER_TABLE = "users";
    private final static String USERS_TABLE_ID = "userid";
    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
    private final static UserRole USER_ROLE = UserRole.USER;

    private final static String DEBATES_TABLE = "debates";
    private final static String DEBATE_TABLE_ID = "debateid";
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static String DEBATE_DATE = LocalDateTime.parse("2022-01-01T00:00:00")
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final static String PUBLIC_DEBATE_DATE = LocalDateTime.parse(DEBATE_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy"));
    private final DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;
    private final static DebateStatus DEBATE_STATUS = DebateStatus.OPEN;

    private final static String LIKES_TABLE = "likes";


    private PostJdbcDao postDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertUsers;
    private SimpleJdbcInsert jdbcInsertDebates;
    private SimpleJdbcInsert jdbcInsertImages;
    private SimpleJdbcInsert jdbcInsertLikes;

    private long postUserId;
    private long postDebateId;
    private Long imageId;

    @Before
    public void setUp() {
        postDao = new PostJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsertUsers = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns(USERS_TABLE_ID);

        jdbcInsertDebates = new SimpleJdbcInsert(ds).
                withTableName(DEBATES_TABLE).
                usingGeneratedKeyColumns(DEBATE_TABLE_ID);
        jdbcInsertImages = new SimpleJdbcInsert(ds)
                .withTableName(IMAGES_TABLE)
                .usingGeneratedKeyColumns(IMAGE_TABLE_ID);
        jdbcInsertLikes = new SimpleJdbcInsert(ds)
                .withTableName(LIKES_TABLE);

        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("password", USER_PASSWORD);
        userData.put("email", USER_EMAIL);
        userData.put("created_date", USER_DATE.toString());
        userData.put("role", USER_ROLE.ordinal());
        postUserId = jdbcInsertUsers.executeAndReturnKey(userData).longValue();

        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        debateData.put("created_date", DEBATE_DATE);
        debateData.put("category", DEBATE_CATEGORY.ordinal());
        debateData.put("status", DEBATE_STATUS.ordinal());
        postDebateId = jdbcInsertDebates.executeAndReturnKey(debateData).longValue();

        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("data", IMAGE_DATA);
        imageId = jdbcInsertImages.executeAndReturnKey(imageData).longValue();

        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(POSTS_TABLE)
                .usingGeneratedKeyColumns(POST_TABLE_ID);
    }

    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE, LIKES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, POSTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE, DEBATES_TABLE);
    }

    @Test
    public void testCreatePost() {
        Post post = postDao.create(postUserId, postDebateId, POST_CONTENT, null, POST_STATUS);

        assertNotNull(post);
        assertEquals(postUserId, post.getUserId());
        assertEquals(postDebateId, post.getDebateId());
        assertEquals(POST_CONTENT, post.getContent());
        assertEquals(POST_STATUS, post.getStatus());
        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, POSTS_TABLE), 1);
    }

    @Test
    public void testCreatePostWithImage() {
        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("data", IMAGE_DATA);
        Long imageId = jdbcInsertImages.executeAndReturnKey(imageData).longValue();

        Post post = postDao.create(postUserId, postDebateId, POST_CONTENT, imageId, POST_STATUS);

        assertNotNull(post);
        assertEquals(postUserId, post.getUserId());
        assertEquals(postDebateId, post.getDebateId());
        assertEquals(POST_CONTENT, post.getContent());
        assertEquals(imageId, post.getImageId());
        assertEquals(POST_STATUS, post.getStatus());
        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, POSTS_TABLE), 1);
    }

    @Test
    public void testGetPublicPostsByDebateEmpty() {
        List<PublicPost> posts = postDao.getPublicPostsByDebate(postDebateId, POST_PAGE);

        assertTrue(posts.isEmpty());
    }

    @Test
    public void testGetPublicPostsByDebate() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        jdbcInsert.execute(postData);

        List<PublicPost> posts = postDao.getPublicPostsByDebate(postDebateId, POST_PAGE);

        assertEquals(1, posts.size());
        assertEquals(USER_USERNAME, posts.get(0).getUsername());
        assertEquals(postDebateId, posts.get(0).getDebateId());
        assertEquals(POST_CONTENT, posts.get(0).getContent());
        assertEquals(imageId, posts.get(0).getImageId());
        assertEquals(PUBLIC_DEBATE_DATE, posts.get(0).getCreatedDate());
        assertEquals(POST_STATUS, posts.get(0).getStatus());
    }

    @Test
    public void testGetPostsByDebateCountEmpty() {
        int count = postDao.getPostsByDebateCount(postDebateId);

        assertEquals(0, count);
    }

    @Test
    public void testGetPostsByDebateCount() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        jdbcInsert.execute(postData);

        int count = postDao.getPostsByDebateCount(postDebateId);

        assertEquals(1, count);
    }

    @Test
    public void testGetPublicPostsByDebateWithIsLiked() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        long postId = jdbcInsert.executeAndReturnKey(postData).longValue();

        final Map<String, Object> likeData = new HashMap<>();
        likeData.put("userid", postUserId);
        likeData.put("postid", postId);
        jdbcInsertLikes.execute(likeData);

        List<PublicPostWithUserLike> posts = postDao.getPublicPostsByDebateWithIsLiked(postDebateId, postUserId, POST_PAGE);

        assertEquals(1, posts.size());
        assertEquals(USER_USERNAME, posts.get(0).getUsername());
        assertEquals(postDebateId, posts.get(0).getDebateId());
        assertEquals(POST_CONTENT, posts.get(0).getContent());
        assertEquals(imageId, posts.get(0).getImageId());
        assertEquals(PUBLIC_DEBATE_DATE, posts.get(0).getCreatedDate());
        assertEquals(POST_STATUS, posts.get(0).getStatus());
        assertTrue(posts.get(0).getLiked());
    }

    @Test
    public void testGetPublicPostsByDebateWithIsLikedEmpty() {
        List<PublicPostWithUserLike> posts = postDao.getPublicPostsByDebateWithIsLiked(postDebateId, postUserId, POST_PAGE);

        assertEquals(0, posts.size());
    }

    @Test
    public void testLikePost() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        long postId = jdbcInsert.executeAndReturnKey(postData).longValue();

        postDao.likePost(postId, postUserId);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, LIKES_TABLE));
    }

    @Test
    public void testUnlikePostEmpty() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        long postId = jdbcInsert.executeAndReturnKey(postData).longValue();

        postDao.unlikePost(postId, postUserId);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, LIKES_TABLE));
    }

    @Test
    public void testUnlikePost() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        long postId = jdbcInsert.executeAndReturnKey(postData).longValue();

        final Map<String, Object> likeData = new HashMap<>();
        likeData.put("userid", postUserId);
        likeData.put("postid", postId);
        jdbcInsertLikes.execute(likeData);

        postDao.unlikePost(postId, postUserId);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, LIKES_TABLE));
    }

    @Test
    public void testHasLikedEmpty() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        long postId = jdbcInsert.executeAndReturnKey(postData).longValue();

        boolean hasLiked = postDao.hasLiked(postId, postUserId);

        assertFalse(hasLiked);
    }

    @Test
    public void testHasLiked() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        long postId = jdbcInsert.executeAndReturnKey(postData).longValue();

        final Map<String, Object> likeData = new HashMap<>();
        likeData.put("userid", postUserId);
        likeData.put("postid", postId);
        jdbcInsertLikes.execute(likeData);

        boolean hasLiked = postDao.hasLiked(postId, postUserId);

        assertTrue(hasLiked);
    }

    @Test
    public void testGetLastArgumentEmpty() {
        Optional<PublicPost> post = postDao.getLastArgument(postDebateId);

        assertFalse(post.isPresent());
    }

    @Test
    public void testGetLastArgument() {
        final Map<String, Object> postData = new HashMap<>();
        postData.put("debateid", postDebateId);
        postData.put("userid", postUserId);
        postData.put("content", POST_CONTENT);
        postData.put("imageid", imageId);
        postData.put("created_date", POST_DATE);
        postData.put("status", POST_STATUS.ordinal());
        long postId = jdbcInsert.executeAndReturnKey(postData).longValue();

        Optional<PublicPost> post = postDao.getLastArgument(postDebateId);

        assertTrue(post.isPresent());
        assertEquals(postId, post.get().getPostId());
        assertEquals(postDebateId, post.get().getDebateId());
        assertEquals(USER_USERNAME, post.get().getUsername());
        assertEquals(POST_CONTENT, post.get().getContent());
        assertEquals(PUBLIC_DEBATE_DATE, post.get().getCreatedDate());
        assertEquals(POST_STATUS, post.get().getStatus());
    }
}
