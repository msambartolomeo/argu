package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.interfaces.services.ArgumentService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.exceptions.ArgumentNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenArgumentException;
import ar.edu.itba.paw.model.exceptions.UserAlreadyLikedException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LikeServiceImplTest {

    private final static String CONTENT = "Content";
    private final static long ID = 1;

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static String USER_USERNAME_2 = "username2";
    private final static String USER_PASSWORD_2 = "password";
    private final static String USER_EMAIL_2 = "test2@test.com";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";

    @InjectMocks
    private LikeServiceImpl likeService = new LikeServiceImpl();

    private Argument argument;
    private User user;

    @Mock
    private LikeDao likeDao;
    @Mock
    private ArgumentService argumentService;
    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
        User user2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2);
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, true, user2, null, DebateCategory.OTHER);
        argument = new Argument(user, debate, CONTENT, null, ArgumentStatus.ARGUMENT);
    }

    @Test
    public void getLikeNotFound() {
        Optional<Like> l = likeService.getLike(argument, user);

        assertFalse(l.isPresent());
    }

    @Test
    public void getLike() {
        Like like = new Like(user, argument);
        when(likeDao.getLike(user, argument)).thenReturn(Optional.of(like));

        Optional<Like> l = likeService.getLike(argument, user);

        assertTrue(l.isPresent());
        assertEquals(like, l.get());
    }

    @Test(expected = ArgumentNotFoundException.class)
    public void testLikeArgumentNotValidArgument() {
        likeService.likeArgument(ID, USER_USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLikeArgumentNotValidUser() {
        when(argumentService.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        likeService.likeArgument(ID, USER_USERNAME);
    }

    @Test(expected = ForbiddenArgumentException.class)
    public void testLikeArgumentDeleted() {
        argument.deleteArgument();
        when(argumentService.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        likeService.likeArgument(ID, USER_USERNAME);
    }

    @Test(expected = UserAlreadyLikedException.class)
    public void testLikeArgumentAlreadyLiked() {
        Like like = new Like(user, argument);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(argumentService.getArgumentById(anyLong())).thenReturn(Optional.of(argument));
        when(likeDao.getLike(any(User.class), any(Argument.class))).thenReturn(Optional.of(like));

        likeService.likeArgument(ID, USER_USERNAME);
    }

    @Test
    public void testLikeArgument() {
        Like like = new Like(user, argument);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(argumentService.getArgumentById(anyLong())).thenReturn(Optional.of(argument));
        when(likeDao.likeArgument(any(User.class), any(Argument.class))).thenReturn(like);

        Like l = likeService.likeArgument(ID, USER_USERNAME);

        assertNotNull(l);
        assertEquals(like, l);
    }

    @Test(expected = ArgumentNotFoundException.class)
    public void testUnlikeArgumentNotValidArgument() {
        likeService.unlikeArgument(ID, USER_USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUnlikeArgumentNotValidUser() {
        when(argumentService.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        likeService.unlikeArgument(ID, USER_USERNAME);
    }

    @Test(expected = ForbiddenArgumentException.class)
    public void testUnlikeArgumentDeleted() {
        argument.deleteArgument();
        when(argumentService.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        likeService.unlikeArgument(ID, USER_USERNAME);
    }

    @Test
    public void testUnlikeArgument() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(argumentService.getArgumentById(anyLong())).thenReturn(Optional.of(argument));

        likeService.unlikeArgument(ID, USER_USERNAME);

        verify(likeDao).unlikeArgument(any(User.class), any(Argument.class));
    }

    @Test
    public void testIsLikedNotLiked() {
        boolean isLiked = likeService.isLiked(user, argument);

        assertFalse(isLiked);
    }

    @Test
    public void testIsLikedLiked() {
        when(likeDao.getLike(user, argument)).thenReturn(Optional.of(new Like(user, argument)));

        boolean isLiked = likeService.isLiked(user, argument);

        assertTrue(isLiked);
    }
}
