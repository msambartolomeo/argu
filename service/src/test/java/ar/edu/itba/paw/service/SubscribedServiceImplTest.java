package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.SubscribedDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Subscribed;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserAlreadySubscribedException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscribedServiceImplTest {

    private final static long ID = 1;
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;

    private final static boolean IS_CREATOR_FOR = true;

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static String USER2_USERNAME = "username2";
    private final static String USER2_PASSWORD = "password";
    private final static String USER2_EMAIL = "test2@test.com";

    private final static String INVALID_USERNAME = "invalid_username";

    private User user;
    private Debate debate;

    @InjectMocks
    private SubscribedServiceImpl subscribedService = new SubscribedServiceImpl();

    @Mock
    private SubscribedDao subscribedDao;

    @Mock
    private UserService userService;

    @Mock
    private DebateService debateService;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        User user2 = new User(USER2_EMAIL, USER2_USERNAME, USER2_PASSWORD, Locale.ENGLISH);
        debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, IS_CREATOR_FOR, user2, null, DEBATE_CATEGORY);
    }

    @Test(expected = UserNotFoundException.class)
    public void testSubscribeToDebateNotValidUser() {
        subscribedService.subscribeToDebate(INVALID_USERNAME, ID);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testSubscribeToDebateNotValidDebate() {
        when(userService.getUserByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
        subscribedService.subscribeToDebate(USER_USERNAME, ID);
    }

    @Test(expected = UserAlreadySubscribedException.class)
    public void testSubscribeToDebateAlreadySubscribed() {
        Subscribed subscribed = new Subscribed(user, debate);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(subscribedDao.getSubscribed(any(User.class), any(Debate.class))).thenReturn(Optional.of(subscribed));

        subscribedService.subscribeToDebate(USER_USERNAME, ID);
    }

    @Test
    public void testSubscribeToDebate() {
        Subscribed subscribed = new Subscribed(user, debate);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(subscribedDao.subscribeToDebate(any(User.class), any(Debate.class))).thenReturn(subscribed);

        Subscribed s = subscribedService.subscribeToDebate(USER_USERNAME, ID);

        assertNotNull(s);
        assertEquals(subscribed, s);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUnsubscribeToDebateNotValidUser() {
        subscribedService.unsubscribeToDebate(INVALID_USERNAME, ID);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testUnsubscribeToDebateNotValidDebate() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        subscribedService.unsubscribeToDebate(USER_USERNAME, ID);
    }

    @Test
    public void testUnsubscribeToDebate() {
        Subscribed subscribed = new Subscribed(user, debate);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(subscribedDao.getSubscribed(any(User.class), any(Debate.class))).thenReturn(Optional.of(subscribed));

        subscribedService.unsubscribeToDebate(USER_USERNAME, ID);
        verify(subscribedDao).unsubscribe(any(Subscribed.class));
    }

    @Test
    public void testIsUserSubscribedNotFound() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        assertFalse(subscribedService.isUserSubscribed(USER_USERNAME, ID));
    }

    @Test
    public void testIsUserSubscribedNotSubscribed() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        boolean isSubscribed = subscribedService.isUserSubscribed(USER_USERNAME, ID);

        assertFalse(isSubscribed);
    }

    @Test
    public void testIsUserSubscribedSubscribed() {
        Subscribed subscribed = new Subscribed(user, debate);
        when(subscribedDao.getSubscribed(any(User.class), any(Debate.class))).thenReturn(Optional.of(subscribed));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        boolean isSubscribed = subscribedService.isUserSubscribed(USER_USERNAME, ID);

        assertTrue(isSubscribed);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetSubscribedNotValidUser() {
        subscribedService.getSubscribed(INVALID_USERNAME, ID);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetSubscribedNotValidDebate() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        subscribedService.getSubscribed(USER_USERNAME, ID);
    }

    @Test
    public void testGetSubscribedNotExists() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(subscribedDao.getSubscribed(any(User.class), any(Debate.class))).thenReturn(Optional.empty());

        Optional<Subscribed> subscribed = subscribedService.getSubscribed(USER_USERNAME, ID);

        assertFalse(subscribed.isPresent());
    }

    @Test
    public void testGetSubscribed() {
        Subscribed subscribed = new Subscribed(user, debate);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(subscribedDao.getSubscribed(any(User.class), any(Debate.class))).thenReturn(Optional.of(subscribed));

        Optional<Subscribed> s = subscribedService.getSubscribed(USER_USERNAME, ID);

        assertTrue(s.isPresent());
        assertEquals(subscribed, s.get());
    }
}
