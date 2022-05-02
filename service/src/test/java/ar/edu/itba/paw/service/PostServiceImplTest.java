package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

    private final static int PAGE = 1;
    private final static long POST_ID = 1;
    private final static String POST_CONTENT = "Post Content";
    private final static long USER_ID = 1;
    private final static long DEBATE_ID = 1;
    private final static String USER_EMAIL = "test@test.com";
    private final static int LIKES = 1;
    private final static LocalDateTime POST_DATE = LocalDateTime.of(2018, 1, 1, 0, 0);


    @InjectMocks
    private PostServiceImpl postService = new PostServiceImpl();

    @Mock
    private PostDao postDao;
    @Mock
    private UserDao userDao;
    @Mock
    private EmailService emailService;

//    @Test
//    public void testCreatePost() {
//        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT, POST_DATE);
//        Mockito.when(postDao.create(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.any())).thenReturn(post);
//
//        Post p = postService.create(USER_ID, DEBATE_ID, POST_CONTENT, new byte[]{});
//
//        assertEquals(post, p);
//    }

//    @Test
//    public void testCreatePostWithEmail() {
//        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT);
//        Mockito.when(userDao.getUserByEmail(Mockito.anyString())).thenReturn(Optional.of(new User(USER_ID, USER_EMAIL)));
//        Mockito.when(postDao.create(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(post);
//
//        Post p = postService.createWithEmail(USER_EMAIL, DEBATE_ID, POST_CONTENT);
//
//        assertEquals(post, p);
//    }

    @Test
    public void testGetPostByIdDoesntExist() {
        Mockito.when(postDao.getPostById(Mockito.anyLong())).thenReturn(Optional.empty());

        Optional<Post> p = postService.getPostById(POST_ID);

        assertFalse(p.isPresent());
    }

    @Test
    public void testGetPostById() {
        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT, POST_DATE);
        Mockito.when(postDao.getPostById(Mockito.anyLong())).thenReturn(Optional.of(post));

        Optional<Post> p = postService.getPostById(POST_ID);

        assertTrue(p.isPresent());
        assertEquals(post, p.get());
    }

    @Test
    public void testGetPublicPostByIdDoesntExist() {
        Mockito.when(postDao.getPublicPostById(Mockito.anyLong())).thenReturn(Optional.empty());

        Optional<PublicPost> p = postService.getPublicPostById(POST_ID);

        assertFalse(p.isPresent());
    }

//    @Test
//    public void testGetPublicPostById() {
//        PublicPost post = new PublicPost(POST_ID, USER_EMAIL, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE);
//        Mockito.when(postDao.getPublicPostById(Mockito.anyLong())).thenReturn(Optional.of(post));
//
//        Optional<PublicPost> p = postService.getPublicPostById(POST_ID);
//
//        assertTrue(p.isPresent());
//        assertEquals(post, p.get());
//    }

    @Test
    public void testGetPostByDebateIsEmpty() {
        Mockito.when(postDao.getPostsByDebate(Mockito.anyLong(),Mockito.anyInt())).thenReturn(new ArrayList<>());

        List<Post> pl = postService.getPostsByDebate(POST_ID, PAGE);

        assertTrue(pl.isEmpty());
    }

    @Test
    public void testGetPostByDebate() {
        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT, POST_DATE);
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        Mockito.when(postDao.getPostsByDebate(Mockito.anyLong(), Mockito.anyInt())).thenReturn(posts);

        List<Post> pl = postService.getPostsByDebate(POST_ID, PAGE);

        assertFalse(pl.isEmpty());
        assertEquals(post, pl.get(0));
    }

    @Test
    public void testGetPublicPostByDebateDoesntExist() {
        Mockito.when(postDao.getPublicPostsByDebate(Mockito.anyLong(),Mockito.anyInt())).thenReturn(new ArrayList<>());

        List<PublicPost> pl = postService.getPublicPostsByDebate(POST_ID, PAGE);

        assertTrue(pl.isEmpty());
    }

//    @Test
//    public void testGetPublicPostByDebate() {
//        PublicPost post = new PublicPost(POST_ID, USER_EMAIL, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE);
//        List<PublicPost> posts = new ArrayList<>();
//        posts.add(post);
//        Mockito.when(postDao.getPublicPostsByDebate(Mockito.anyLong(), Mockito.anyInt())).thenReturn(posts);
//
//        List<PublicPost> pl = postService.getPublicPostsByDebate(POST_ID, PAGE);
//
//        assertFalse(pl.isEmpty());
//        assertEquals(post, pl.get(0));
//    }
}
