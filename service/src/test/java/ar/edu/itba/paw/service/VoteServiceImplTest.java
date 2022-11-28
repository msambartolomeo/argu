package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserAlreadyVotedException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.exceptions.DebateAlreadyDeletedException;
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
public class VoteServiceImplTest {

    private final static long DEBATE_ID = 1;
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
    private VoteServiceImpl voteService = new VoteServiceImpl();

    @Mock
    private VoteDao voteDao;

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
    public void testAddVoteNotValidUser() {
        voteService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testAddVoteDebateNotFound() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        voteService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
    }

    @Test(expected = DebateAlreadyDeletedException.class)
    public void testAddVoteDebateClosed() {
        debate.setStatus(DebateStatus.CLOSED);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        voteService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
    }

    @Test(expected = UserAlreadyVotedException.class)
    public void testAddVoteAlreadyVoted() {
        Vote vote = new Vote(user, debate, DebateVote.FOR);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(voteDao.getVote(any(User.class), any(Debate.class))).thenReturn(Optional.of(vote));

        voteService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
    }

    @Test
    public void testAddVote() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(voteDao.getVote(any(User.class), any(Debate.class))).thenReturn(Optional.empty());

        voteService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);

        verify(voteDao).addVote(any(User.class), any(Debate.class), any(DebateVote.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserVoteInvalidUser() {
        voteService.getVote(DEBATE_ID, USER_USERNAME);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetUserVoteInvalidDebate() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        voteService.getVote(DEBATE_ID, USER_USERNAME);
    }

    @Test
    public void testGetUserVoteNotVoted() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(voteDao.getVote(any(User.class), any(Debate.class))).thenReturn(Optional.empty());

        Optional<Vote> vote = voteService.getVote(DEBATE_ID, USER_USERNAME);

        assertFalse(vote.isPresent());
    }

    @Test
    public void testGetUserVoteFor() {
        Vote vote = new Vote(user, debate, DebateVote.FOR);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(voteDao.getVote(any(User.class), any(Debate.class))).thenReturn(Optional.of(vote));

        Optional<Vote> v = voteService.getVote(DEBATE_ID, USER_USERNAME);

        assertTrue(v.isPresent());
        assertEquals(vote, v.get());
    }

    @Test
    public void testGetUserVoteAgainst() {
        Vote vote = new Vote(user, debate, DebateVote.AGAINST);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(voteDao.getVote(any(User.class), any(Debate.class))).thenReturn(Optional.of(vote));

        Optional<Vote> v = voteService.getVote(DEBATE_ID, USER_USERNAME);

        assertTrue(v.isPresent());
        assertEquals(vote, v.get());
    }

    @Test(expected = UserNotFoundException.class)
    public void testRemoveVoteNotValidUser() {
        voteService.removeVote(DEBATE_ID, USER_USERNAME);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testRemoveVoteDebateNotFound() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        voteService.removeVote(DEBATE_ID, USER_USERNAME);
    }

    @Test(expected = DebateAlreadyDeletedException.class)
    public void testRemoveVoteDebateClosed() {
        debate.setStatus(DebateStatus.CLOSED);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        voteService.removeVote(DEBATE_ID, USER_USERNAME);
    }

    @Test
    public void testRemoveVote() {
        Vote vote = new Vote(user, debate, DebateVote.FOR);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(voteDao.getVote(any(User.class), any(Debate.class))).thenReturn(Optional.of(vote));

        voteService.removeVote(DEBATE_ID, USER_USERNAME);

        verify(voteDao).delete(any(Vote.class));
    }

}
