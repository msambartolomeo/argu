package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PostJdbcDaoTest {

    private PostJdbcDao postDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    private DataSource ds;

    private final static long POST_ID = 1;
    private final static String POST_CONTENT = "Post Content";
    private final static String POSTS_TABLE = "posts";
    private final static String ID = "postid";
    private final static int POSTS_PAGE = 0;

    private final static String USER_EMAIL = "test@test.com";
    private final static String USER_TABLE = "users";
    private final static String USERS_ID = "userid";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static String DEBATES_TABLE = "debates";
    private final static String DEBATES_ID = "debateid";


    private long postUserId;
    private long postDebateId;

    @Before
    public void setUp() {
        postDao = new PostJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);

        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", USER_EMAIL);
        postUserId = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).
                usingGeneratedKeyColumns(USERS_ID).
                executeAndReturnKey(userData).longValue();

        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        postDebateId = new SimpleJdbcInsert(ds).withTableName(DEBATES_TABLE).
                usingGeneratedKeyColumns(DEBATES_ID).
                executeAndReturnKey(debateData).longValue();

        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(POSTS_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, POSTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE, DEBATES_TABLE);
    }

    @Test
    public void testCreatePost() {
        Post post = postDao.create(postUserId, postDebateId, POST_CONTENT, null);

        assertNotNull(post);
        assertEquals(postUserId, post.getUserId());
        assertEquals(postDebateId, post.getDebateId());
        assertEquals(POST_CONTENT, post.getContent());
        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, POSTS_TABLE), 1);
    }

    @Test
    public void testGetPostByIdDoesntExist() {
        Optional<Post> post = postDao.getPostById(POST_ID);

        assertFalse(post.isPresent());
    }

//    @Test
//    public void testGetPostByIdExists() {
//        final Map<String, Object> postData = new HashMap<>();
//        postData.put("userid", postUserId);
//        postData.put("debateid", postDebateId);
//        postData.put("content", POST_CONTENT);
//        Number key = jdbcInsert.executeAndReturnKey(postData);
//
//        Optional<Post> post = postDao.getPostById(key.longValue());
//
//        assertTrue(post.isPresent());
//        assertEquals(postUserId, post.get().getUserId());
//        assertEquals(postDebateId, post.get().getDebateId());
//        assertEquals(POST_CONTENT, post.get().getContent());
//    }

//    @Test
//    public void testGetPostByDebateIdDoesntExist() {
//        List<Post> post = postDao.getPostsByDebate(postDebateId, POSTS_PAGE);
//
//        assertTrue(post.isEmpty());
//    }

//    @Test
//    public void testGetPostByDebateIdExists() {
//        final Map<String, Object> postData = new HashMap<>();
//        postData.put("userid", postUserId);
//        postData.put("debateid", postDebateId);
//        postData.put("content", POST_CONTENT);
//        Number key = jdbcInsert.executeAndReturnKey(postData);
//
//        List<Post> post = postDao.getPostsByDebate(postDebateId, POSTS_PAGE);
//
//        assertFalse(post.isEmpty());
//        assertEquals(key.longValue(), post.get(0).getPostId());
//        assertEquals(postUserId, post.get(0).getUserId());
//        assertEquals(postDebateId, post.get(0).getDebateId());
//        assertEquals(POST_CONTENT, post.get(0).getContent());
//    }

//    @Test
//    public void testGetPublicPostByIdDoesntExist() {
//        Optional<PublicPost> post = postDao.getPublicPostById(POST_ID);
//
//        assertFalse(post.isPresent());
//    }

//    @Test
//    public void testGetPublicPostByIdExist() {
//        final Map<String, Object> postData = new HashMap<>();
//        postData.put("userid", postUserId);
//        postData.put("debateid", postDebateId);
//        postData.put("content", POST_CONTENT);
//        Number key = jdbcInsert.executeAndReturnKey(postData);
//
//        Optional<PublicPost> post = postDao.getPublicPostById(key.longValue());
//
//        assertTrue(post.isPresent());
//        assertEquals(postDebateId, post.get().getDebateId());
//        assertEquals(POST_CONTENT, post.get().getContent());
//    }


//    @Test
//    public void testGetPublicPostByDebateIdDoesntExist() {
//        List<PublicPost> post = postDao.getPublicPostsByDebate(postDebateId, POSTS_PAGE);
//
//        assertTrue(post.isEmpty());
//    }

//    @Test
//    public void testGetPublicPostByDebateIdExists() {
//        final Map<String, Object> postData = new HashMap<>();
//        postData.put("userid", postUserId);
//        postData.put("debateid", postDebateId);
//        postData.put("content", POST_CONTENT);
//        Number key = jdbcInsert.executeAndReturnKey(postData);
//
//        List<PublicPost> post = postDao.getPublicPostsByDebate(postDebateId, POSTS_PAGE);
//
//        assertFalse(post.isEmpty());
//        assertEquals(key.longValue(), post.get(0).getPostId());
//        assertEquals(postDebateId, post.get(0).getDebateId());
//        assertEquals(POST_CONTENT, post.get(0).getContent());
//    }
}
