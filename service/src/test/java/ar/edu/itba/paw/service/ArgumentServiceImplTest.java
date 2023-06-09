package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ArgumentDao;
import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentServiceImplTest {

    private final static int VALID_PAGE = 0;
    private final static int NOT_VALID_PAGE = -1;
    
    private final static String CONTENT = "Content";

    private final static long ID = 1;

    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static String USER_USERNAME_2 = "username2";
    private final static String USER_PASSWORD_2 = "password";
    private final static String USER_EMAIL_2 = "test2@test.com";
    private final static String USER_USERNAME_3 = "username2";
    private final static String USER_PASSWORD_3 = "password";
    private final static String USER_EMAIL_3 = "test2@test.com";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";

    private User user;
    private User user2;
    private User user3;
    private Debate debate;
    private Image image;

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
    private LikeDao likeDao;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        user2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2, Locale.ENGLISH);
        user3 = new User(USER_EMAIL_3, USER_USERNAME_3, USER_PASSWORD_3, Locale.ENGLISH);
        debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, true, user2, null, DebateCategory.OTHER);
        image = new Image(IMAGE_DATA);
    }

    @Test
    public void getArgumentByIdNotFound() {
        Optional<Argument> a = argumentService.getArgumentById(ID);

        assertFalse(a.isPresent());
    }

    @Test
    public void getArgumentById() {
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        when(argumentDao.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        Optional<Argument> a = argumentService.getArgumentById(ID);

        assertTrue(a.isPresent());
        assertEquals(argument.getContent(), a.get().getContent());
    }

    @Test
    public void testGetArgumentsByDebate() {
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT));

        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(argumentDao.getArgumentsByDebate(any(Debate.class), anyInt(), anyInt())).thenReturn(arguments);

        List<Argument> a = argumentService.getArgumentsByDebate(ID, null, VALID_PAGE, 5);

        assertFalse(a.isEmpty());

        assertEquals(arguments.get(0).getContent(), a.get(0).getContent());
        assertEquals(arguments.get(0).getLikesCount(), a.get(0).getLikesCount());
        assertEquals(arguments.get(0).getStatus(), a.get(0).getStatus());
    }

    @Test
    public void testGetArgumentsByDebateNotFound() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        List<Argument> a = argumentService.getArgumentsByDebate(ID, null, VALID_PAGE, 5);

        assertTrue(a.isEmpty());
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetArgumentsByDebateDebateNotFound() {
        argumentService.getArgumentsByDebate(ID, null, VALID_PAGE, 5);
    }

    @Test
    public void testGetArgumentsByDebateNotValidPage() {
        List<Argument> a = argumentService.getArgumentsByDebate(ID, null, NOT_VALID_PAGE, 5);

        assertTrue(a.isEmpty());
    }

    @Test
    public void testGetArgumentsByDebateWithUserLike() {
        List<Argument> arguments = new ArrayList<>();
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        arguments.add(argument);

        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(likeDao.getLike(any(User.class), any(Argument.class))).thenReturn(Optional.of(new Like(user, argument)));
        when(argumentDao.getArgumentsByDebate(any(Debate.class), anyInt(), anyInt())).thenReturn(arguments);

        List<Argument> a = argumentService.getArgumentsByDebate(ID, USER_USERNAME, VALID_PAGE, 5);

        assertFalse(a.isEmpty());

        assertEquals(arguments.get(0).getContent(), a.get(0).getContent());
        assertEquals(arguments.get(0).getLikesCount(), a.get(0).getLikesCount());
        assertEquals(arguments.get(0).getStatus(), a.get(0).getStatus());
        assertTrue(a.get(0).isLikedByUser());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetArgumentsByDebateWithUserLikeNotValidUser() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        argumentService.getArgumentsByDebate(ID, USER_USERNAME, VALID_PAGE, 5);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetLastArgumentNoValidDebate() {
        argumentService.getLastArgument(ID);
    }

    @Test
    public void testGetLastArgumentNotFound() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        Optional<Argument> a = argumentService.getLastArgument(ID);

        assertFalse(a.isPresent());
    }

    @Test
    public void testGetLastArgument() {
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        when(argumentDao.getLastArgument(any(Debate.class))).thenReturn(Optional.of(argument));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        Optional<Argument> a = argumentService.getLastArgument(ID);

        assertTrue(a.isPresent());
        assertEquals(argument.getContent(), a.get().getContent());
    }

    @Test(expected = ArgumentNotFoundException.class)
    public void testDeleteArgumentNoValidArgument() {
        argumentService.deleteArgument(ID);
    }

    @Test
    public void testDeleteArgument() {
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
        when(argumentDao.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        argumentService.deleteArgument(ID);
    }

    @Test // image must be deleted
    public void testDeleteArgumentWithImage() {
        Argument argument = new Argument(user, debate, CONTENT, image, ArgumentStatus.ARGUMENT);
        when(argumentDao.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        argumentService.deleteArgument(ID);

        verify(imageService).deleteImage(any(Image.class));
    }

    @Test
    public void testGetArgumentsByDebatePageCount() {
        int argumentCount = 47;
        int expectedPageCount = 10;
        when(argumentDao.getArgumentsByDebateCount(anyLong())).thenReturn(argumentCount);

        int pc = argumentService.getArgumentByDebatePageCount(ID, 5);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testGetArgumentsByDebatePageCountNoArguments() {
        int argumentCount = 0;
        int expectedPageCount = 0;
        when(argumentDao.getArgumentsByDebateCount(anyLong())).thenReturn(argumentCount);

        int pc = argumentService.getArgumentByDebatePageCount(ID, 5);

        assertEquals(expectedPageCount, pc);
    }

    @Mock
    private Debate debateMock;

    @Test
    public void testSendEmailToSubscribedUsersSubscribedUserArgument() {
        Set<User> users = new HashSet<>();
        users.add(user);
        // Mock debate with subscribed users
        when(debateMock.getSubscribedUsers()).thenReturn(users);

        argumentService.sendEmailToSubscribedUsers(debateMock, user);

        verify(emailService, times(0)).notifyNewArgument(anyString(), anyString(), any(Debate.class), any(Locale.class));
    }

    @Test
    public void testSendEmailToSubscribedUsers() {
        Set<User> users = new HashSet<>();
        users.add(user);
        // Mock debate with subscribed users
        when(debateMock.getSubscribedUsers()).thenReturn(users);

        argumentService.sendEmailToSubscribedUsers(debateMock, user2);

        verify(emailService).notifyNewArgument(anyString(), anyString(), any(Debate.class), any(Locale.class));
    }

    @Test
    public void testGetArgumentStatusFirstIntroductionUserIsCreator() {
        ArgumentStatus status = argumentService.getArgumentStatus(debate, user);

        assertEquals(ArgumentStatus.INTRODUCTION, status);
    }

    @Test(expected = ArgumentTurnException.class)
    public void testGetArgumentStatusFirstIntroductionUserIsNotCreator() {
        argumentService.getArgumentStatus(debate, user2);
    }

    @Test(expected = ArgumentTurnException.class)
    public void testGetArgumentStatusLastArgumentFromSameUser() {
        Argument argument = new Argument(user, debate, CONTENT, image, ArgumentStatus.ARGUMENT);
        when(argumentDao.getLastArgument(any(Debate.class))).thenReturn(Optional.of(argument));

        argumentService.getArgumentStatus(debate, user);
    }

    @Test
    public void testGetArgumentStatusLastWasFirstIntroduction() {
        Argument argument = new Argument(user, debate, CONTENT, image, ArgumentStatus.INTRODUCTION);
        when(argumentDao.getLastArgument(any(Debate.class))).thenReturn(Optional.of(argument));

        ArgumentStatus status = argumentService.getArgumentStatus(debate, user2);

        assertEquals(ArgumentStatus.INTRODUCTION, status);
    }

    @Test
    public void testGetArgumentStatusLastWasSecondIntroduction() {
        Argument argument = new Argument(user2, debate, CONTENT, image, ArgumentStatus.INTRODUCTION);
        when(argumentDao.getLastArgument(any(Debate.class))).thenReturn(Optional.of(argument));

        ArgumentStatus status = argumentService.getArgumentStatus(debate, user);

        assertEquals(ArgumentStatus.ARGUMENT, status);
    }

    @Test
    public void testGetArgumentStatusLastWasArgumentDebateIsOpen() {
        Argument argument = new Argument(user2, debate, CONTENT, image, ArgumentStatus.ARGUMENT);
        when(argumentDao.getLastArgument(any(Debate.class))).thenReturn(Optional.of(argument));

        ArgumentStatus status = argumentService.getArgumentStatus(debate, user);

        assertEquals(ArgumentStatus.ARGUMENT, status);
    }

    @Test
    public void testGetArgumentStatusLastWasArgumentDebateIsClosing() {
        Argument argument = new Argument(user2, debate, CONTENT, image, ArgumentStatus.ARGUMENT);
        when(argumentDao.getLastArgument(any(Debate.class))).thenReturn(Optional.of(argument));
        debate.setStatus(DebateStatus.CLOSING);

        ArgumentStatus status = argumentService.getArgumentStatus(debate, user);

        assertEquals(ArgumentStatus.CONCLUSION, status);
    }

    @Test
    public void testGetArgumentStatusLastWasConclusion() {
        Argument argument = new Argument(user2, debate, CONTENT, image, ArgumentStatus.CONCLUSION);
        when(argumentDao.getLastArgument(any(Debate.class))).thenReturn(Optional.of(argument));

        ArgumentStatus status = argumentService.getArgumentStatus(debate, user);

        assertEquals(ArgumentStatus.CONCLUSION, status);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testCreateArgumentNoDebate() {
        argumentService.create(USER_USERNAME, ID, CONTENT, null);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateArgumentNoUser() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        argumentService.create(USER_USERNAME, ID, CONTENT, null);
    }

    @Test(expected = DebateClosedException.class)
    public void testCreateArgumentClosedDebate() {
        debate.setStatus(DebateStatus.CLOSED);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        argumentService.create(USER_USERNAME, ID, CONTENT, null);
    }

    @Test(expected = DebateClosedException.class)
    public void testCreateArgumentDeletedDebate() {
        debate.setStatus(DebateStatus.DELETED);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        argumentService.create(USER_USERNAME, ID, CONTENT, null);
    }

    @Test(expected = DebateClosedException.class)
    public void testCreateArgumentVotingDebate() {
        debate.setStatus(DebateStatus.VOTING);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        argumentService.create(USER_USERNAME, ID, CONTENT, null);
    }

    @Test(expected = ArgumentTurnException.class)
    public void testCreateArgumentUnauthorizedUser() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user3));

        argumentService.create(USER_USERNAME, ID, CONTENT, IMAGE_DATA);
    }

    @Test
    public void testCreateArgument() {
        Argument argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.INTRODUCTION);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(argumentDao.create(any(User.class), any(Debate.class), anyString(), any(), any(ArgumentStatus.class))).thenReturn(argument);

        Argument a = argumentService.create(USER_USERNAME, ID, CONTENT, new byte[0]);

        assertNotNull(a);
        assertEquals(argument, a);
    }

    @Test
    public void testCreateArgumentWithImage() {
        Argument argument = new Argument(user, debate, CONTENT, image, ArgumentStatus.INTRODUCTION);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(imageService.createImage(any(byte[].class))).thenReturn(image);
        when(argumentDao.create(any(User.class), any(Debate.class), anyString(), any(), any(ArgumentStatus.class))).thenReturn(argument);

        Argument a = argumentService.create(USER_USERNAME, ID, CONTENT, IMAGE_DATA);

        assertNotNull(a);
        assertEquals(argument, a);
        assertEquals(image, a.getImage());
    }
}
