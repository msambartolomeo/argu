package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ArgumentDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentServiceImplTest {

    private final static int VALID_PAGE = 0;
    private final static int NOT_VALID_PAGE = -1;

    private final static String POST_CONTENT = "Post Content";

    private final static long ID = 1;

    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static String USER_USERNAME_2 = "username2";
    private final static String USER_PASSWORD_2 = "password";
    private final static String USER_EMAIL_2 = "test2@test.com";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";

    private final static User USER = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    private final static User USER_2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2);
    private final static Debate DEBATE = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, USER, USER_2, null, DebateCategory.OTHER);

    @InjectMocks
    private ArgumentServiceImpl argumentService = new ArgumentServiceImpl();

    @Mock
    private ArgumentDao argumentDao;
    @Mock
    private EmailService emailService;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;
    @Mock
    private DebateService debateService;
    @Mock
    private LikeService likeService;

    @Test
    public void testGetArgumentsByDebate() {
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new Argument(USER, DEBATE, POST_CONTENT, null, ArgumentStatus.ARGUMENT));

        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(DEBATE));
        when(argumentDao.getArgumentsByDebate(any(Debate.class), anyInt())).thenReturn(arguments);

        List<Argument> a = argumentService.getArgumentsByDebate(ID, null, VALID_PAGE);

        assertFalse(a.isEmpty());

        assertEquals(arguments.get(0).getContent(), a.get(0).getContent());
        assertEquals(arguments.get(0).getLikesCount(), a.get(0).getLikesCount());
        assertEquals(arguments.get(0).getStatus(), a.get(0).getStatus());
    }

    @Test
    public void testGetArgumentsByDebateNotFound() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(DEBATE));

        List<Argument> p = argumentService.getArgumentsByDebate(ID, null, VALID_PAGE);

        assertTrue(p.isEmpty());
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetArgumentsByDebateDebateNotFound() {
        argumentService.getArgumentsByDebate(ID, null, VALID_PAGE);
    }

    @Test
    public void testGetArgumentsByDebateNotValidPage() {
        List<Argument> p = argumentService.getArgumentsByDebate(ID, null, NOT_VALID_PAGE);

        assertTrue(p.isEmpty());
    }

    @Test
    public void testGetArgumentsByDebateWithUserLike() {
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new Argument(USER, DEBATE, POST_CONTENT, null, ArgumentStatus.ARGUMENT));

        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(DEBATE));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(USER));
        when(likeService.isLiked(any(User.class), any(Argument.class))).thenReturn(true);
        when(argumentDao.getArgumentsByDebate(any(Debate.class), anyInt())).thenReturn(arguments);

        List<Argument> a = argumentService.getArgumentsByDebate(ID, USER_USERNAME, VALID_PAGE);

        assertFalse(a.isEmpty());

        assertEquals(arguments.get(0).getContent(), a.get(0).getContent());
        assertEquals(arguments.get(0).getLikesCount(), a.get(0).getLikesCount());
        assertEquals(arguments.get(0).getStatus(), a.get(0).getStatus());
        assertTrue(a.get(0).isLikedByUser());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetArgumentsByDebateWithUserLikeNotValidUser() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(DEBATE));

        argumentService.getArgumentsByDebate(ID, USER_USERNAME, VALID_PAGE);
    }
//
//    @Test
//    public void testGetPostsByDebatePageCount() {
//        int postCount = 47;
//        int expectedPageCount = 10;
//        when(argumentDao.getPostsByDebateCount(anyLong())).thenReturn(postCount);
//
//        int pc = argumentService.getPostsByDebatePageCount(POST_ID);
//
//        assertEquals(expectedPageCount, pc);
//    }
//
//    @Test
//    public void testGetPostsByDebatePageCountNoPosts() {
//        int postCount = 0;
//        int expectedPageCount = 0;
//        when(argumentDao.getPostsByDebateCount(anyLong())).thenReturn(postCount);
//
//        int pc = argumentService.getPostsByDebatePageCount(POST_ID);
//
//        assertEquals(expectedPageCount, pc);
//    }
//
//    @Test
//    public void testlikePost() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        PublicPost post = new PublicPost(POST_ID, POST_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(argumentDao.getPublicPostById(anyLong())).thenReturn(Optional.of(post));
//
//        argumentService.likePost(POST_ID, USER_USERNAME);
//
//        verify(argumentDao).likePost(anyLong(), anyLong());
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testLikePostNotValidUser() {
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());
//
//        argumentService.likePost(POST_ID, USER_USERNAME);
//    }
//
//    @Test(expected = PostNotFoundException.class)
//    public void testLikePostNotValidPost() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        argumentService.likePost(POST_ID, USER_USERNAME);
//    }
//    @Test(expected = UserAlreadyLikedException.class)
//    public void testLikePostAlreadyLiked() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        PublicPost post = new PublicPost(POST_ID, POST_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(argumentDao.getPublicPostById(anyLong())).thenReturn(Optional.of(post));
//        when(argumentDao.hasLiked(anyLong(), anyLong())).thenReturn(true);
//
//        argumentService.likePost(POST_ID, USER_USERNAME);
//    }
//
//    @Test
//    public void testUnlikePost() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        argumentService.unlikePost(POST_ID, USER_USERNAME);
//
//        verify(argumentDao).unlikePost(anyLong(), anyLong());
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testUnlikePostNotValidUser() {
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());
//
//        argumentService.unlikePost(POST_ID, USER_USERNAME);
//    }
//
//    @Test
//    public void testGetLastArgument() {
//        PublicPost post = new PublicPost(POST_ID, POST_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));
//
//        Optional<PublicPost> p = argumentService.getLastArgument(POST_ID);
//
//        assertTrue(p.isPresent());
//        assertEquals(post.getPostId(), p.get().getPostId());
//        assertEquals(post.getUsername(), p.get().getUsername());
//        assertEquals(post.getDebateId(), p.get().getDebateId());
//        assertEquals(post.getContent(), p.get().getContent());
//        assertEquals(post.getLikes(), p.get().getLikes());
//        assertEquals(post.getCreatedDate(), p.get().getCreatedDate());
//        assertEquals(post.getImageId(), p.get().getImageId());
//        assertEquals(post.getStatus(), p.get().getStatus());
//    }
//
//    @Test
//    public void testGetLastArgumentNotFound() {
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.empty());
//
//        Optional<PublicPost> p = argumentService.getLastArgument(POST_ID);
//
//        assertFalse(p.isPresent());
//    }
//    @Test
//    public void testGetArgumentStatusFirstIntroductionUserIsCreator() {
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.empty());
//
//        ArgumentStatus status = argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, DEBATE_CREATOR);
//
//        assertEquals(ArgumentStatus.INTRODUCTION, status);
//    }
//
//    @Test(expected = ForbiddenPostException.class)
//    public void testGetArgumentStatusFirstIntroductionUserIsNotCreator() {
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.empty());
//
//        argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, USER_USERNAME);
//    }
//
//    @Test(expected = ForbiddenPostException.class)
//    public void testGetArgumentStatusLastArgumentFromSameUser() {
//        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.INTRODUCTION);
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));
//
//        argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, USER_USERNAME);
//    }
//
//    @Test
//    public void testGetArgumentStatusLastWasFirstIntroduction() {
//        PublicPost post = new PublicPost(POST_ID, DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.INTRODUCTION);
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));
//
//        ArgumentStatus status = argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, USER_USERNAME);
//
//        assertEquals(ArgumentStatus.INTRODUCTION, status);
//    }
//
//    @Test
//    public void testGetArgumentStatusLastWasSecondIntroduction() {
//        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.INTRODUCTION);
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));
//
//        ArgumentStatus status = argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, DEBATE_CREATOR);
//
//        assertEquals(ArgumentStatus.ARGUMENT, status);
//    }
//
//    @Test
//    public void testGetArgumentStatusLastWasArgumentDebateIsOpen() {
//        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));
//
//        ArgumentStatus status = argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.OPEN, DEBATE_CREATOR, DEBATE_CREATOR);
//
//        assertEquals(ArgumentStatus.ARGUMENT, status);
//    }
//
//    @Test
//    public void testGetArgumentStatusLastWasArgumentDebateIsClosing() {
//        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));
//
//        ArgumentStatus status = argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.CLOSING, DEBATE_CREATOR, DEBATE_CREATOR);
//
//        assertEquals(ArgumentStatus.CONCLUSION, status);
//    }
//
//    @Test
//    public void testGetArgumentStatusLastWasConclusion() {
//        PublicPost post = new PublicPost(POST_ID, USER_USERNAME, DEBATE_ID, POST_CONTENT, LIKES, POST_DATE, IMAGE_ID, ArgumentStatus.CONCLUSION);
//        when(argumentDao.getLastArgument(anyLong())).thenReturn(Optional.of(post));
//
//        ArgumentStatus status = argumentService.getArgumentStatus(DEBATE_ID, DebateStatus.CLOSING, DEBATE_CREATOR, DEBATE_CREATOR);
//
//        assertEquals(ArgumentStatus.CONCLUSION, status);
//        verify(debateService).closeDebate(anyLong());
//    }
//
//    @Test
//    public void testSendEmailToSubscribedUsersSubscribedUserPosted() {
//        List<User> users = new ArrayList<>();
//        users.add(new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE));
//        when(userService.getSubscribedUsersByDebate(anyLong())).thenReturn(users);
//
//        argumentService.sendEmailToSubscribedUsers(DEBATE_ID, USER_ID, "subject", "body");
//
//        verify(emailService, times(0)).notifyNewPost(anyString(), anyString(), anyLong(), anyString());
//    }
//
//    @Test
//    public void testSendEmailToSubscribedUsers() {
//        List<User> users = new ArrayList<>();
//        users.add(new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE));
//        when(userService.getSubscribedUsersByDebate(anyLong())).thenReturn(users);
//
//        argumentService.sendEmailToSubscribedUsers(DEBATE_ID, USER_ID_2, "subject", "body");
//
//        verify(emailService).notifyNewPost(anyString(), anyString(), anyLong(), anyString());
//    }
//
//    @Test(expected = DebateNotFoundException.class)
//    public void testCreatePostNoDebate() {
//        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.empty());
//
//        argumentService.create(USER_USERNAME, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testCreatePostNoUser() {
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
//        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());
//
//        argumentService.create(USER_USERNAME, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
//    }
//
//    @Test(expected = ForbiddenPostException.class)
//    public void testCreatePostClosedDebate() {
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.CLOSED, FOR_COUNT,AGAINST_COUNT );
//        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        argumentService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
//    }
//
//    @Test(expected = ForbiddenPostException.class)
//    public void testCreatePostDeletedDebate() {
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
//        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        argumentService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
//    }
//
//    @Test(expected = ForbiddenPostException.class)
//    public void testCreatePostUnauthorizedUser() {
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        argumentService.create(USER_USERNAME, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
//    }
//
//    @Test
//    public void testCreatePostNoImage() {
//        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT, POST_DATE, null, ArgumentStatus.ARGUMENT);
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
//        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(argumentDao.create(anyLong(), anyLong(), anyString(), any(), any(ArgumentStatus.class))).thenReturn(post);
//
//        Post p = argumentService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, new byte[0]);
//
//        assertEquals(p.getUserId(), post.getUserId());
//        assertEquals(p.getDebateId(), post.getDebateId());
//        assertEquals(p.getContent(), post.getContent());
//        assertEquals(p.getImageId(), post.getImageId());
//        assertEquals(p.getStatus(), post.getStatus());
//    }
//
//    @Test
//    public void testCreatePost() {
//        Post post = new Post(POST_ID, USER_ID, DEBATE_ID, POST_CONTENT, POST_DATE, IMAGE_ID, ArgumentStatus.ARGUMENT);
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
//        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        Image image = new Image(IMAGE_ID, IMAGE_DATA);
//        when(debateService.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(argumentDao.create(anyLong(), anyLong(), anyString(), any(), any(ArgumentStatus.class))).thenReturn(post);
//        when(imageService.createImage(any(byte[].class))).thenReturn(image);
//
//        Post p = argumentService.create(DEBATE_CREATOR, DEBATE_ID, POST_CONTENT, IMAGE_DATA);
//
//        assertEquals(p.getUserId(), post.getUserId());
//        assertEquals(p.getDebateId(), post.getDebateId());
//        assertEquals(p.getContent(), post.getContent());
//        assertEquals(p.getImageId(), post.getImageId());
//        assertEquals(p.getStatus(), post.getStatus());
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testHasLikedInvalidUser() {
//        argumentService.hasLiked(POST_ID, USER_USERNAME);
//    }
//
//    @Test
//    public void testHasLiked() {
//        User user = new User(USER_ID, DEBATE_CREATOR, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(argumentDao.hasLiked(anyLong(), anyLong())).thenReturn(true);
//
//        boolean hasLiked = argumentService.hasLiked(POST_ID, DEBATE_CREATOR);
//
//        assertTrue(hasLiked);
//    }
}
