package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.*;
import ar.edu.itba.paw.model.exceptions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DebateServiceImplTest {

    private final static int PAGE = 1;
    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private static final String DEBATE_CREATOR = "debateCreator";
    private static final String DEBATE_OPPONENT = "debateOpponent";
    private final static int FOR_COUNT = 1;
    private final static int AGAINST_COUNT = 1;
    private final static int SUBSCRIBED_COUNT = 1;
    private final static LocalDateTime DEBATE_DATE = LocalDateTime.of(2018, 1, 1, 0, 0);

    private final static long IMAGE_ID = 1;
    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    private final static long USER_ID = 1;
    private final static long USER_ID_2 = 2;
    private final static String USER_USERNAME = "username";
    private final static String USER_USERNAME_2 = "username2";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static String USER_EMAIL_2 = "test2@test.com";
    private final static Date USER_DATE = Date.valueOf(LocalDate.parse("2022-01-01"));
    private final static UserRole USER_ROLE = UserRole.USER;

    private final static int VALID_PAGE = 0;
    private final static int INVALID_PAGE = -1;

    @InjectMocks
    private DebateServiceImpl debateService = new DebateServiceImpl();

    @Mock
    private DebateDao debateDao;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;

    @Test
    public void testGetPublicDebateById() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT, AGAINST_COUNT);

        when(debateDao.getPublicDebateById(DEBATE_ID)).thenReturn(Optional.of(debate));

        Optional<PublicDebate> d = debateService.getPublicDebateById(DEBATE_ID);

        assertTrue(d.isPresent());
        assertEquals(debate.getDebateId(), d.get().getDebateId());
        assertEquals(debate.getName(), d.get().getName());
        assertEquals(debate.getDescription(), d.get().getDescription());
        assertEquals(debate.getCreatorUsername(), d.get().getCreatorUsername());
        assertEquals(debate.getOpponentUsername(), d.get().getOpponentUsername());
        assertEquals(debate.getImageId(), d.get().getImageId());
        assertEquals(debate.getCreatedDate(), d.get().getCreatedDate());
        assertEquals(debate.getDebateCategory(), d.get().getDebateCategory());
        assertEquals(debate.getSubscribedUsers(), d.get().getSubscribedUsers());
        assertEquals(debate.getDebateStatus(), d.get().getDebateStatus());
        assertEquals(debate.getForCount(), d.get().getForCount());
        assertEquals(debate.getAgainstCount(), d.get().getAgainstCount());
    }

    @Test
    public void testGetPublicDebateByIdNotFound() {
        when(debateDao.getPublicDebateById(DEBATE_ID)).thenReturn(Optional.empty());

        Optional<PublicDebate> d = debateService.getPublicDebateById(DEBATE_ID);

        assertFalse(d.isPresent());
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateCreatorOrOpponentNotFound() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_DATA, DebateCategory.OTHER);
    }

    @Test
    public void testCreateNoImage() {
        User creator = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        User opponent = new User(USER_ID_2, USER_USERNAME_2, USER_PASSWORD, USER_EMAIL_2, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(DEBATE_CREATOR)).thenReturn(Optional.of(creator));
        when(userService.getUserByUsername(DEBATE_OPPONENT)).thenReturn(Optional.of(opponent));
        Debate debate = new Debate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, USER_ID, USER_ID_2, DEBATE_DATE, null, DebateCategory.OTHER, DebateStatus.OPEN);
        when(debateDao.create(anyString(), anyString(), anyLong(), anyLong(), any(), any(DebateCategory.class))).thenReturn(debate);

        Debate d = debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, new byte[0], DebateCategory.OTHER);

        assertEquals(debate.getName(), d.getName());
        assertEquals(debate.getDescription(), d.getDescription());
        assertEquals(debate.getCreatorId(), d.getCreatorId());
        assertEquals(debate.getOpponentId(), d.getOpponentId());
        assertEquals(debate.getDebateCategory(), d.getDebateCategory());
        assertEquals(debate.getDebateStatus(), d.getDebateStatus());
        assertEquals(debate.getImageId(), d.getImageId());
    }

    @Test
    public void testCreateWithImage() {
        User creator = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        User opponent = new User(USER_ID_2, USER_USERNAME_2, USER_PASSWORD, USER_EMAIL_2, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(DEBATE_CREATOR)).thenReturn(Optional.of(creator));
        when(userService.getUserByUsername(DEBATE_OPPONENT)).thenReturn(Optional.of(opponent));
        Debate debate = new Debate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, USER_ID, USER_ID_2, DEBATE_DATE, IMAGE_ID, DebateCategory.OTHER, DebateStatus.OPEN);
        when(debateDao.create(anyString(), anyString(), anyLong(), anyLong(), anyLong(), any(DebateCategory.class))).thenReturn(debate);
        Image image = new Image(IMAGE_ID, IMAGE_DATA);
        when(imageService.createImage(any(byte[].class))).thenReturn(image);

        Debate d = debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_DATA, DebateCategory.OTHER);

        assertEquals(debate.getName(), d.getName());
        assertEquals(debate.getDescription(), d.getDescription());
        assertEquals(debate.getCreatorId(), d.getCreatorId());
        assertEquals(debate.getOpponentId(), d.getOpponentId());
        assertEquals(debate.getDebateCategory(), d.getDebateCategory());
        assertEquals(debate.getDebateStatus(), d.getDebateStatus());
        assertEquals(debate.getImageId(), d.getImageId());
    }

    @Test
    public void testGetDebates() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
        List<PublicDebate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getPublicDebatesDiscovery(anyInt(),anyInt(),any(), any(), any(), any(), any())).thenReturn(debates);

        List<PublicDebate> dl = debateService.get(VALID_PAGE, null, null, null, null, null);

        assertFalse(dl.isEmpty());
        assertEquals(debate.getDebateId(), dl.get(0).getDebateId());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCreatorUsername(), dl.get(0).getCreatorUsername());
        assertEquals(debate.getOpponentUsername(), dl.get(0).getOpponentUsername());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getDebateCategory(), dl.get(0).getDebateCategory());
        assertEquals(debate.getDebateStatus(), dl.get(0).getDebateStatus());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());

    }

    @Test
    public void testGetDebatesEmpty() {
        List<PublicDebate> dl = debateService.get(VALID_PAGE, null, null, null, null, null);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetDebatesInvalidPage() {
        List<PublicDebate> dl = debateService.get(INVALID_PAGE, null, null, null, null, null);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetMostSubscribed() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
        List<PublicDebate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getPublicDebatesDiscovery(anyInt(),anyInt(),any(), any(), eq(DebateOrder.SUBS_DESC), any(), any())).thenReturn(debates);

        List<PublicDebate> dl = debateService.getMostSubscribed();

        assertFalse(dl.isEmpty());
        assertEquals(debate.getDebateId(), dl.get(0).getDebateId());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCreatorUsername(), dl.get(0).getCreatorUsername());
        assertEquals(debate.getOpponentUsername(), dl.get(0).getOpponentUsername());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getDebateCategory(), dl.get(0).getDebateCategory());
        assertEquals(debate.getDebateStatus(), dl.get(0).getDebateStatus());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());

    }

    @Test
    public void testGetMostSubscribedEmpty() {
        List<PublicDebate> dl = debateService.getMostSubscribed();

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetPages() {

    }

    @Test
    public void testGetDebatePageCount() {
        int postCount = 47;
        int expectedPageCount = 10;
        when(debateDao.getPublicDebatesCount(any(), any(), any(), any())).thenReturn(postCount);

        int pc = debateService.getPages(null, null, null, null);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testGetDebatePageCountNoPosts() {
        int postCount = 0;
        int expectedPageCount = 0;
        when(debateDao.getPublicDebatesCount(any(), any(), any(), any())).thenReturn(postCount);

        int pc = debateService.getPages(null, null, null, null);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testSubscribeToDebate() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.subscribeToDebate(USER_USERNAME, DEBATE_ID);

        verify(debateDao).subscribeToDebate(anyLong(), anyLong());
    }

    @Test(expected = DebateNotFoundException.class)
    public void testSubscribeToDebateNotValidDebate() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        debateService.subscribeToDebate(USER_USERNAME, DEBATE_ID);
    }

    @Test(expected = UserNotFoundException.class)
    public void testSubscribeToDebateNotValidUser() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        debateService.subscribeToDebate(USER_USERNAME, DEBATE_ID);
    }

    // TODO tests de si ya esta likeado antes de hacer el like / unlike

    @Test
    public void testUnsubscribeToDebate() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        debateService.unsubscribeToDebate(USER_USERNAME, DEBATE_ID);

        verify(debateDao).unsubscribeToDebate(anyLong(), anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testUnsubscribeToDebateNotValidUser() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        debateService.unsubscribeToDebate(USER_USERNAME, DEBATE_ID);
    }

    @Test
    public void testIsUserSubscribed() {
        boolean isSubscribed = true;
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateDao.isUserSubscribed(anyLong(), anyLong())).thenReturn(isSubscribed);

        boolean s = debateService.isUserSubscribed(USER_USERNAME, DEBATE_ID);

        assertEquals(isSubscribed, s);
    }

    @Test
    public void testIsUserNotSubscribed() {
        boolean isSubscribed = false;
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateDao.isUserSubscribed(anyLong(), anyLong())).thenReturn(isSubscribed);

        boolean s = debateService.isUserSubscribed(USER_USERNAME, DEBATE_ID);

        assertEquals(isSubscribed, s);
    }

    @Test(expected = UserNotFoundException.class)
    public void testIsUserSubscribedNotValidUser() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        debateService.unsubscribeToDebate(USER_USERNAME, DEBATE_ID);
    }

    @Test
    public void testGetProfileDebatesSubscribed() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
        List<PublicDebate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getSubscribedDebatesByUserId(anyLong(), anyInt())).thenReturn(debates);

        List<PublicDebate> dl = debateService.getProfileDebates("subscribed", USER_ID, VALID_PAGE);

        assertFalse(dl.isEmpty());
        assertEquals(debate.getDebateId(), dl.get(0).getDebateId());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCreatorUsername(), dl.get(0).getCreatorUsername());
        assertEquals(debate.getOpponentUsername(), dl.get(0).getOpponentUsername());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getDebateCategory(), dl.get(0).getDebateCategory());
        assertEquals(debate.getDebateStatus(), dl.get(0).getDebateStatus());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());
    }

    @Test
    public void testGetProfileMyDebates() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
        List<PublicDebate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getMyDebates(anyLong(), anyInt())).thenReturn(debates);

        List<PublicDebate> dl = debateService.getProfileDebates("mydebates", USER_ID, VALID_PAGE);

        assertFalse(dl.isEmpty());
        assertEquals(debate.getDebateId(), dl.get(0).getDebateId());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCreatorUsername(), dl.get(0).getCreatorUsername());
        assertEquals(debate.getOpponentUsername(), dl.get(0).getOpponentUsername());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getDebateCategory(), dl.get(0).getDebateCategory());
        assertEquals(debate.getDebateStatus(), dl.get(0).getDebateStatus());
        assertEquals(debate.getImageId(), dl.get(0).getImageId());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());
    }

    @Test
    public void testGetProfileInvalidPage() {
        List<PublicDebate> dl = debateService.getProfileDebates("subscribed", USER_ID, INVALID_PAGE);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetProfileMyDebatesEmpty() {
        List<PublicDebate> dl = debateService.getProfileDebates("subscribed", USER_ID, VALID_PAGE);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetProfileSubscribedEmpty() {
        List<PublicDebate> dl = debateService.getProfileDebates("subscribed", USER_ID, VALID_PAGE);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testProfileMyDebatesPageCount() {
        int postCount = 47;
        int expectedPageCount = 10;
        when(debateDao.getMyDebatesCount(anyLong())).thenReturn(postCount);

        int pc = debateService.getProfileDebatesPageCount("mydebates", USER_ID);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testProfileSubscribedPageCount() {
        int postCount = 47;
        int expectedPageCount = 10;
        when(debateDao.getSubscribedDebatesByUserIdCount(anyLong())).thenReturn(postCount);

        int pc = debateService.getProfileDebatesPageCount("subscribed", USER_ID);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testAddVote() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(false);

        debateService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);

        verify(debateDao).addVote(anyLong(), anyLong(), any(DebateVote.class));
    }

    @Test(expected = UserAlreadyVotedException.class)
    public void testAddVoteAlreadyVoted() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(true);

        debateService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);

    }

    @Test(expected = UserNotFoundException.class)
    public void testAddVoteNotValidUser() {
        debateService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
    }

    @Test
    public void testRemoveVote() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        debateService.removeVote(DEBATE_ID, USER_USERNAME);

        verify(debateDao).removeVote(anyLong(), anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testRemoveVoteNotValidUser() {
        debateService.removeVote(DEBATE_ID, USER_USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetUserVoteInvalidUser() {
        debateService.getUserVote(DEBATE_ID, USER_USERNAME);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetUserVoteInvalidDebate() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        debateService.getUserVote(DEBATE_ID, USER_USERNAME);
    }

    @Test
    public void testGetUserVoteNotVoted() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));

        String vote = debateService.getUserVote(DEBATE_ID, USER_USERNAME);

        assertNull(vote);
    }

    @Test
    public void testGetUserVoteFor() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(true);
        when(debateDao.getUserVote(anyLong(), anyLong())).thenReturn(DebateVote.FOR);

        String vote = debateService.getUserVote(DEBATE_ID, USER_USERNAME);

        assertEquals(DEBATE_CREATOR, vote);
    }

    @Test
    public void testGetUserVoteAgainst() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(true);
        when(debateDao.getUserVote(anyLong(), anyLong())).thenReturn(DebateVote.AGAINST);

        String vote = debateService.getUserVote(DEBATE_ID, USER_USERNAME);

        assertEquals(DEBATE_OPPONENT, vote);
    }

    @Test
    public void testCloseDebate() {
        debateService.closeDebate(DEBATE_ID);

        verify(debateDao).changeDebateStatus(anyLong(), eq(DebateStatus.CLOSED));
    }

    @Test(expected = DebateNotFoundException.class)
    public void testStartConclusionNotValidDebate() {
        debateService.startConclusion(DEBATE_ID, USER_USERNAME);
    }

    @Test(expected = ForbiddenDebateException.class)
    public void testStartConclusionInvalidUser() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, USER_USERNAME);
    }

    @Test(expected = ForbiddenDebateException.class)
    public void testStartConclusionAlreadyStarted() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.CLOSING, FOR_COUNT, AGAINST_COUNT);
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, DEBATE_CREATOR);
    }

    @Test(expected = ForbiddenDebateException.class)
    public void testStartConclusionClosed() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.CLOSED, FOR_COUNT, AGAINST_COUNT);
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, DEBATE_CREATOR);
    }

    @Test
    public void testStartConclusion() {
        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, DEBATE_CREATOR);

        verify(debateDao).changeDebateStatus(anyLong(), eq(DebateStatus.CLOSING));
    }
}
