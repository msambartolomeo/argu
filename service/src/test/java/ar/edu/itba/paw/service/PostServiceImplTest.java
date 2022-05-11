package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.UserRole;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenPostException;
import ar.edu.itba.paw.model.exceptions.PostNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

    private final static int VALID_PAGE = 0;
    private final static int NOT_VALID_PAGE = -1;
    private final static long POST_ID = 1;
    private final static String POST_CONTENT = "Post Content";
    private final static String POST_USERNAME = "username";
    private final static long IMAGE_ID = 1;
    private final static long DEBATE_ID = 1;
    private final static int LIKES = 1;
    private final static boolean IS_LIKED = true;
    private final static LocalDateTime POST_DATE = LocalDateTime.of(2018, 1, 1, 0, 0);
    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    private final static long USER_ID = 1;
    private final static long USER_ID_2 = 2;
    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
    private final static UserRole USER_ROLE = UserRole.USER;

    private static final String DEBATE_CREATOR = "debateCreator";
    private static final String DEBATE_OPPONENT = "debateOpponent";
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static LocalDateTime DEBATE_DATE = LocalDateTime.of(2018, 1, 1, 0, 0);
    private final static int FOR_COUNT = 1;
    private final static int AGAINST_COUNT = 1;
    private final static int SUBSCRIBED_COUNT = 1;


    @InjectMocks
    private PostServiceImpl postService = new PostServiceImpl();

    @Mock
    private PostDao postDao;
    @Mock
    private EmailService emailService;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;
    @Mock
    private DebateService debateService;

    @Test
    public void testGetPublicPostsByDebate() {
        List<PublicPost> posts = new ArrayList<>();
        posts.add(new PublicPost(POST_ID, POST_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT));

        when(postDao.getPublicPostsByDebate(anyLong(), anyInt())).thenReturn(posts);

        List<PublicPost> p = postService.getPublicPostsByDebate(POST_ID, VALID_PAGE);

        assertFalse(p.isEmpty());

        assertEquals(posts.get(0).getPostId(), p.get(0).getPostId());
        assertEquals(posts.get(0).getUsername(), p.get(0).getUsername());
        assertEquals(posts.get(0).getDebateId(), p.get(0).getDebateId());
        assertEquals(posts.get(0).getContent(), p.get(0).getContent());
        assertEquals(posts.get(0).getLikes(), p.get(0).getLikes());
        assertEquals(posts.get(0).getCreatedDate(), p.get(0).getCreatedDate());
        assertEquals(posts.get(0).getImageId(), p.get(0).getImageId());
        assertEquals(posts.get(0).getStatus(), p.get(0).getStatus());
    }

    @Test
    public void testGetPublicPostByDebateNotFound() {
        Mockito.when(postDao.getPublicPostsByDebate(anyLong(), anyInt())).thenReturn(new ArrayList<>());

        List<PublicPost> p = postService.getPublicPostsByDebate(POST_ID, VALID_PAGE);

        assertTrue(p.isEmpty());
    }

    @Test
    public void testGetPublicPostsByDebateNotValidPage() {
        List<PublicPost> p = postService.getPublicPostsByDebate(POST_ID, NOT_VALID_PAGE);

        assertTrue(p.isEmpty());
    }

    @Test
    public void testGetPublicPostsByDebateWithUserLike() {
        List<PublicPostWithUserLike> posts = new ArrayList<>();
        posts.add(new PublicPostWithUserLike(POST_ID, POST_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT, IS_LIKED));
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(postDao.getPublicPostsByDebateWithIsLiked(anyLong(), anyLong(), anyInt())).thenReturn(posts);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        List<PublicPostWithUserLike> p = postService.getPublicPostsByDebateWithIsLiked(POST_ID, USER_USERNAME, VALID_PAGE);

        assertFalse(p.isEmpty());

        assertEquals(posts.get(0).getPostId(), p.get(0).getPostId());
        assertEquals(posts.get(0).getUsername(), p.get(0).getUsername());
        assertEquals(posts.get(0).getDebateId(), p.get(0).getDebateId());
        assertEquals(posts.get(0).getContent(), p.get(0).getContent());
        assertEquals(posts.get(0).getLikes(), p.get(0).getLikes());
        assertEquals(posts.get(0).getCreatedDate(), p.get(0).getCreatedDate());
        assertEquals(posts.get(0).getImageId(), p.get(0).getImageId());
        assertEquals(posts.get(0).getStatus(), p.get(0).getStatus());
        assertEquals(posts.get(0).getLiked(), p.get(0).getLiked());
    }

    @Test
    public void testGetPublicPostByDebateWithUserLikeNotFound() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(postDao.getPublicPostsByDebateWithIsLiked(anyLong(), anyLong(), anyInt())).thenReturn(new ArrayList<>());
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        List<PublicPostWithUserLike> p = postService.getPublicPostsByDebateWithIsLiked(POST_ID, USER_USERNAME, VALID_PAGE);

        assertTrue(p.isEmpty());
    }

    @Test
    public void testGetPublicPostsByDebateWithUserLikeNotValidPage() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        List<PublicPostWithUserLike> p = postService.getPublicPostsByDebateWithIsLiked(POST_ID, USER_USERNAME, NOT_VALID_PAGE);

        assertTrue(p.isEmpty());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetPublicPostsByDebateWithUserLikeNotValidUser() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        postService.getPublicPostsByDebateWithIsLiked(POST_ID, USER_USERNAME, NOT_VALID_PAGE);
    }

    @Test
    public void testGetPostsByDebatePageCount() {
        int postCount = 47;
        int expectedPageCount = 10;
        when(postDao.getPostsByDebateCount(anyLong())).thenReturn(postCount);

        int pc = postService.getPostsByDebatePageCount(POST_ID);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testGetPostsByDebatePageCountNoPosts() {
        int postCount = 0;
        int expectedPageCount = 0;
        when(postDao.getPostsByDebateCount(anyLong())).thenReturn(postCount);

        int pc = postService.getPostsByDebatePageCount(POST_ID);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testlikePost() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        postService.likePost(POST_ID, USER_USERNAME);

        verify(postDao).likePost(anyLong(), anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testLikePostNotValidUser() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        postService.likePost(POST_ID, USER_USERNAME);
    }


    // TODO chequear cuando tiremos la excepcion de que no existe el post
    @Test(expected = PostNotFoundException.class)
    public void testLikePostNotValidPost() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        postService.likePost(POST_ID, USER_USERNAME);
    }

    @Test
    public void testUnlikePost() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        postService.unlikePost(POST_ID, USER_USERNAME);

        verify(postDao).unlikePost(anyLong(), anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testUnlikePostNotValidUser() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        postService.unlikePost(POST_ID, USER_USERNAME);
    }


    // TODO chequear cuando tiremos la excepcion de que no existe el post
    @Test(expected = PostNotFoundException.class)
    public void testUnlikePostNotValidPost() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        postService.unlikePost(POST_ID, USER_USERNAME);
    }

    // TODO tests de si ya esta likeado antes de hacer el like / unlike

    @Test
    public void testGetLastArgument() {
        PublicPost post = new PublicPost(POST_ID, POST_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));

        Optional<PublicPost> p = postService.getLastArgument(POST_ID);

        assertTrue(p.isPresent());
        assertEquals(post.getPostId(), p.get().getPostId());
        assertEquals(post.getUsername(), p.get().getUsername());
        assertEquals(post.getDebateId(), p.get().getDebateId());
        assertEquals(post.getContent(), p.get().getContent());
        assertEquals(post.getLikes(), p.get().getLikes());
        assertEquals(post.getCreatedDate(), p.get().getCreatedDate());
        assertEquals(post.getImageId(), p.get().getImageId());
        assertEquals(post.getStatus(), p.get().getStatus());
    }

    @Test
    public void testGetLastArgumentNotFound() {
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.empty());

        Optional<PublicPost> p = postService.getLastArgument(POST_ID);

        assertFalse(p.isPresent());
    }
    @Test
    public void testGetArgumentStatusFirstIntroductionUserIsCreator() {
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.empty());

        ArgumentStatus status = postService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, DEBATE_CREATOR);

        assertEquals(ArgumentStatus.INTRODUCTION, status);
    }

    @Test(expected = ForbiddenPostException.class)
    public void testGetArgumentStatusFirstIntroductionUserIsNotCreator() {
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.empty());

        postService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, USER_USERNAME);
    }

    @Test(expected = ForbiddenPostException.class)
    public void testGetArgumentStatusLastArgumentFromSameUser() {
        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.INTRODUCTION);
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));

        postService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, USER_USERNAME);
    }

    @Test
    public void testGetArgumentStatusLastWasFirstIntroduction() {
        PublicPost post = new PublicPost(POST_ID, DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.INTRODUCTION);
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));

        ArgumentStatus status = postService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, USER_USERNAME);

        assertEquals(ArgumentStatus.INTRODUCTION, status);
    }

    @Test
    public void testGetArgumentStatusLastWasSecondIntroduction() {
        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.INTRODUCTION);
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));

        ArgumentStatus status = postService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, DEBATE_CREATOR);

        assertEquals(ArgumentStatus.ARGUMENT, status);
    }

    @Test
    public void testGetArgumentStatusLastWasArgumentDebateIsOpen() {
        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));

        ArgumentStatus status = postService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, DEBATE_CREATOR);

        assertEquals(ArgumentStatus.ARGUMENT, status);
    }

    @Test
    public void testGetArgumentStatusLastWasArgumentDebateIsClosing() {
        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));

        ArgumentStatus status = postService.getArgumentStatus(DEBATE_ID, DebateStatus.CLOSING, DEBATE_CREATOR, DEBATE_CREATOR);

        assertEquals(ArgumentStatus.CONCLUSION, status);
    }

    @Test
    public void testGetArgumentStatusLastWasConclusion() {
        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.CONCLUSION);
        when(postDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));

        ArgumentStatus status = postService.getArgumentStatus(DEBATE_ID, DebateStatus.CLOSING, DEBATE_CREATOR, DEBATE_CREATOR);

        assertEquals(ArgumentStatus.CONCLUSION, status);
        verify(debateService).closeDebate(anyLong());
    }

    @Test
    public void testSendEmailToSubscribedUsersSubscribedUserPosted() {
        List<User> users = new ArrayList<>();
        users.add(new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE));
        when(userService.getSubscribedUsersByDebate(anyLong())).thenReturn(users);

        postService.sendEmailToSubscribedUsers(DEBATE_ID, USER_ID, "subject", "body");

        verify(emailService, times(0)).notifyNewPost(anyString(), anyString(), anyLong(), anyString());
    }

    @Test
    public void testSendEmailToSubscribedUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE));
        when(userService.getSubscribedUsersByDebate(anyLong())).thenReturn(users);

        postService.sendEmailToSubscribedUsers(DEBATE_ID, USER_ID_2, "subject", "body");

        verify(emailService).notifyNewPost(anyString(), anyString(), anyLong(), anyString());
    }

    @Test(expected = DebateNotFoundException.class)
    public void testCreatePostNoDebate() {
        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.empty());

        postService.create(USER_USERNAME, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreatePostNoUser() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        postService.create(USER_USERNAME, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
    }

    @Test(expected = ForbiddenPostException.class)
    public void testCreatePostClosedDebate() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.CLOSED, FOR_COUNT,AGAINST_COUNT );
        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        postService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
    }

    @Test(expected = ForbiddenPostException.class)
    public void testCreatePostDeletedDebate() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        postService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
    }

    @Test(expected = ForbiddenPostException.class)
    public void testCreatePostUnauthorizedUser() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        postService.create(USER_USERNAME, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
    }

    @Test
    public void testCreatePostNoImage() {
        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT, POST_DATE, null, ArgumentStatus.ARGUMENT);
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(postDao.create(anyLong(), anyLong(), anyString(), any(), any(ArgumentStatus.class))).thenReturn(post);

        Post p = postService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, new byte[0]);

        assertEquals(p.getUserId(), post.getUserId());
        assertEquals(p.getDebateId(), post.getDebateId());
        assertEquals(p.getContent(), post.getContent());
        assertEquals(p.getImageId(), post.getImageId());
        assertEquals(p.getStatus(), post.getStatus());
    }

    @Test
    public void testCreatePost() {
        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        Image image = new Image(IMAGE_ID, IMAGE_DATA);
        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(postDao.create(anyLong(), anyLong(), anyString(), any(), any(ArgumentStatus.class))).thenReturn(post);
        when(imageService.createImage(any(byte[].class))).thenReturn(image);

        Post p = postService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, IMAGE_DATA);

        assertEquals(p.getUserId(), post.getUserId());
        assertEquals(p.getDebateId(), post.getDebateId());
        assertEquals(p.getContent(), post.getContent());
        assertEquals(p.getImageId(), post.getImageId());
        assertEquals(p.getStatus(), post.getStatus());
    }

    @Test(expected = UserNotFoundException.class)
    public void testHasLikedInvalidUser() {
        postService.hasLiked(POST_ID, USER_USERNAME);
    }

    @Test
    public void testHasLiked() {
        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(postDao.hasLiked(anyLong(), anyLong())).thenReturn(true);

        boolean hasLiked = postService.hasLiked(POST_ID, DEBATE_CREATOR);

        assertTrue(hasLiked);
    }
}
