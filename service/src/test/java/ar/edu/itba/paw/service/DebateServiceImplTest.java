package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.*;
import ar.edu.itba.paw.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
    private final static DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;
    private final static int FOR_COUNT = 1;
    private final static int AGAINST_COUNT = 1;
    private final static int SUBSCRIBED_COUNT = 1;
    private final static LocalDateTime DEBATE_DATE = LocalDateTime.of(2018, 1, 1, 0, 0);

    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    private final static long CREATOR_ID = 1;
    private final static long OPPONENT_ID = 2;
    private final static String CREATOR_USERNAME = "creator_username";
    private final static String OPPONENT_USERNAME = "opponent_username";
    private final static String CREATOR_PASSWORD = "c_password";
    private final static String OPPONENT_PASSWORD = "o_password";
    private final static String CREATOR_EMAIL = "creator@test.com";
    private final static String OPPONENT_EMAIL = "opponent@test.com";
    private final static boolean IS_CREATOR_FOR = true;

    private final static String INVALID_USERNAME = "invalid_username";

    private final static int VALID_PAGE = 0;
    private final static int INVALID_PAGE = -1;

    private User creator;
    private User opponent;
    private Image image;

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

    @Before
    public void setUp() {
        creator = new User(CREATOR_EMAIL, CREATOR_USERNAME, CREATOR_PASSWORD);
        opponent = new User(OPPONENT_EMAIL, OPPONENT_USERNAME, OPPONENT_PASSWORD);
        image = new Image(IMAGE_DATA);
    }

    @Test
    public void testGetDebateById() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);

        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        Optional<Debate> d = debateService.getDebateById(DEBATE_ID);
        assertTrue(d.isPresent());
        assertEquals(d.get().getName(), DEBATE_NAME);
        assertEquals(d.get().getDescription(), DEBATE_DESCRIPTION);
        assertEquals(d.get().getCreator(), creator);
        assertTrue(d.get().getIsCreatorFor());
        assertEquals(d.get().getOpponent(), opponent);
        assertEquals(d.get().getImage().getData(), IMAGE_DATA);
        assertEquals(d.get().getCategory(), DEBATE_CATEGORY);
    }

    @Test
    public void testGetDebateByIdNotFound() {
        when(debateDao.getDebateById(DEBATE_ID)).thenReturn(Optional.empty());

        Optional<Debate> d = debateService.getDebateById(DEBATE_ID);

        assertFalse(d.isPresent());
    }

    // TODO: Check what's wrong -> error with debateId
    @Test
    public void testCreateDebate() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);

        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(creator));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(opponent));
        when(imageService.createImage(new byte[]{anyByte()})).thenReturn(image);
        when(debateDao.create(anyString(), anyString(), any(User.class), anyBoolean(), any(User.class), any(), any(DebateCategory.class)))
                .thenReturn(debate);

        Debate d = debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, CREATOR_USERNAME, IS_CREATOR_FOR, OPPONENT_USERNAME, IMAGE_DATA, DEBATE_CATEGORY);

        assertNotNull(d);
        assertEquals(debate, d);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateCreatorOrOpponentNotFound() {
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, CREATOR_USERNAME, IS_CREATOR_FOR, OPPONENT_USERNAME, IMAGE_DATA, DebateCategory.OTHER);
    }

    // TODO: Check what's wrong -> error with debateId
    @Test
    public void testCreateNoImage() {
        when(userService.getUserByUsername(CREATOR_USERNAME)).thenReturn(Optional.of(creator));
        when(userService.getUserByUsername(OPPONENT_USERNAME)).thenReturn(Optional.of(opponent));
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, null, DEBATE_CATEGORY);
        when(debateDao.create(anyString(), anyString(), any(User.class), anyBoolean(), any(User.class), any(), any(DebateCategory.class))).thenReturn(debate);

        Debate d = debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, CREATOR_USERNAME, IS_CREATOR_FOR, OPPONENT_USERNAME, new byte[0], DebateCategory.OTHER);

        assertEquals(debate.getName(), d.getName());
        assertEquals(debate.getDescription(), d.getDescription());
        assertEquals(debate.getCreator(), d.getCreator());
        assertTrue(d.getIsCreatorFor());
        assertEquals(debate.getOpponent(), d.getOpponent());
        assertEquals(debate.getCategory(), d.getCategory());
        assertEquals(debate.getImage(), d.getImage());
    }

    @Test
    public void testGetDebates() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, null, DEBATE_CATEGORY);
        List<Debate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getDebatesDiscovery(anyInt(), anyInt(), any(), any(), any(), any(), any()))
                .thenReturn(debates);

        List<Debate> dl = debateService.get(VALID_PAGE, null, null, null, null, null);

        assertFalse(dl.isEmpty());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCreator(), dl.get(0).getCreator());
        assertEquals(debate.getOpponent(), dl.get(0).getOpponent());
        assertEquals(debate.getImage(), dl.get(0).getImage());
        assertEquals(debate.getCategory(), dl.get(0).getCategory());
        assertEquals(debate.getStatus(), dl.get(0).getStatus());
        assertTrue(dl.get(0).getIsCreatorFor());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());

    }

    @Test
    public void testGetDebatesEmpty() {
        List<Debate> dl = debateService.get(VALID_PAGE, null, null, null, null, null);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetDebatesInvalidPage() {
        List<Debate> dl = debateService.get(INVALID_PAGE, null, null, null, null, null);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetMostSubscribed() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        List<Debate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getDebatesDiscovery(anyInt(), anyInt(), any(), any(), eq(DebateOrder.SUBS_DESC), any(), any())).thenReturn(debates);

        List<Debate> dl = debateService.getMostSubscribed();

        assertFalse(dl.isEmpty());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCategory(), dl.get(0).getCategory());
        assertEquals(debate.getOpponent(), dl.get(0).getOpponent());
        assertTrue(dl.get(0).getIsCreatorFor());
        assertEquals(debate.getImage(), dl.get(0).getImage());
        assertEquals(debate.getCategory(), dl.get(0).getCategory());
        assertEquals(debate.getStatus(), dl.get(0).getStatus());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());

    }

    @Test
    public void testGetMostSubscribedEmpty() {
        List<Debate> dl = debateService.getMostSubscribed();

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetDebatePageCount() {
        int argumentCount = 47;
        int expectedPageCount = 10;
        when(debateDao.getDebatesCount(any(), any(), any(), any()))
                .thenReturn(argumentCount);

        int pc = debateService.getPages(null, null, null, null);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testGetDebatePageCountNoArguments() {
        int argumentCount = 0;
        int expectedPageCount = 0;
        when(debateDao.getDebatesCount(any(), any(), any(), any())).thenReturn(argumentCount);

        int pc = debateService.getPages(null, null, null, null);

        assertEquals(expectedPageCount, pc);
    }

//    @Test
//    public void testSubscribeToDebate() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT,AGAINST_COUNT );
//        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//
//        debateService.subscribeToDebate(USER_USERNAME, DEBATE_ID);
//
//        verify(debateDao).subscribeToDebate(anyLong(), anyLong());
//    }
//
//    @Test(expected = DebateNotFoundException.class)
//    public void testSubscribeToDebateNotValidDebate() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        debateService.subscribeToDebate(USER_USERNAME, DEBATE_ID);
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testSubscribeToDebateNotValidUser() {
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());
//
//        debateService.subscribeToDebate(USER_USERNAME, DEBATE_ID);
//    }
//
//    @Test
//    public void testUnsubscribeToDebate() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        debateService.unsubscribeToDebate(USER_USERNAME, DEBATE_ID);
//
//        verify(debateDao).unsubscribeToDebate(anyLong(), anyLong());
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testUnsubscribeToDebateNotValidUser() {
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());
//
//        debateService.unsubscribeToDebate(USER_USERNAME, DEBATE_ID);
//    }
//
//    @Test
//    public void testIsUserSubscribed() {
//        boolean isSubscribed = true;
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(debateDao.isUserSubscribed(anyLong(), anyLong())).thenReturn(isSubscribed);
//
//        boolean s = debateService.isUserSubscribed(USER_USERNAME, DEBATE_ID);
//
//        assertEquals(isSubscribed, s);
//    }
//
//    @Test
//    public void testIsUserNotSubscribed() {
//        boolean isSubscribed = false;
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(debateDao.isUserSubscribed(anyLong(), anyLong())).thenReturn(isSubscribed);
//
//        boolean s = debateService.isUserSubscribed(USER_USERNAME, DEBATE_ID);
//
//        assertEquals(isSubscribed, s);
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testIsUserSubscribedNotValidUser() {
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());
//
//        debateService.unsubscribeToDebate(USER_USERNAME, DEBATE_ID);
//    }
//
    @Test
    public void testGetProfileDebatesSubscribed() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        List<Debate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getSubscribedDebatesByUser(anyLong(), anyInt())).thenReturn(debates);

        List<Debate> dl = debateService.getProfileDebates("subscribed", CREATOR_ID, VALID_PAGE);

        assertFalse(dl.isEmpty());
//        assertEquals(debate.getDebateId(), dl.get(0).getDebateId());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCreator(), dl.get(0).getCreator());
        assertEquals(debate.getOpponent(), dl.get(0).getOpponent());
        assertTrue(dl.get(0).getIsCreatorFor());
        assertEquals(debate.getImage(), dl.get(0).getImage());
        assertEquals(debate.getCategory(), dl.get(0).getCategory());
        assertEquals(debate.getStatus(), dl.get(0).getStatus());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());
    }

    @Test
    public void testGetProfileUserDebates() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        List<Debate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getUserDebates(anyLong(), anyInt())).thenReturn(debates);

        List<Debate> dl = debateService.getProfileDebates("mydebates", CREATOR_ID, VALID_PAGE);

        assertFalse(dl.isEmpty());
//        assertEquals(debate.getDebateId(), dl.get(0).getDebateId());
        assertEquals(debate.getName(), dl.get(0).getName());
        assertEquals(debate.getDescription(), dl.get(0).getDescription());
        assertEquals(debate.getCreator(), dl.get(0).getCreator());
        assertEquals(debate.getOpponent(), dl.get(0).getOpponent());
        assertTrue(dl.get(0).getIsCreatorFor());
        assertEquals(debate.getImage(), dl.get(0).getImage());
        assertEquals(debate.getCategory(), dl.get(0).getCategory());
        assertEquals(debate.getStatus(), dl.get(0).getStatus());
        assertEquals(debate.getCreatedDate(), dl.get(0).getCreatedDate());
        assertEquals(debate.getSubscribedUsers(), dl.get(0).getSubscribedUsers());
        assertEquals(debate.getForCount(), dl.get(0).getForCount());
        assertEquals(debate.getAgainstCount(), dl.get(0).getAgainstCount());
    }

    @Test
    public void testGetProfileInvalidPage() {
        List<Debate> dl = debateService.getProfileDebates("subscribed", CREATOR_ID, INVALID_PAGE);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetProfileUserDebatesEmpty() {
        List<Debate> dl = debateService.getProfileDebates("subscribed", CREATOR_ID, VALID_PAGE);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetProfileSubscribedEmpty() {
        List<Debate> dl = debateService.getProfileDebates("subscribed", CREATOR_ID, VALID_PAGE);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testProfileUserDebatesPageCount() {
        int argumentCount = 47;
        int expectedPageCount = 10;
        when(debateDao.getUserDebatesCount(anyLong())).thenReturn(argumentCount);

        int pc = debateService.getProfileDebatesPageCount("mydebates", CREATOR_ID);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testProfileSubscribedPageCount() {
        int argumentCount = 47;
        int expectedPageCount = 10;
        when(debateDao.getSubscribedDebatesByUserCount(anyLong())).thenReturn(argumentCount);

        int pc = debateService.getProfileDebatesPageCount("subscribed", CREATOR_ID);

        assertEquals(expectedPageCount, pc);
    }

//    @Test
//    public void testAddVote() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(false);
//        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//
//        debateService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
//
//        verify(debateDao).addVote(anyLong(), anyLong(), any(DebateVote.class));
//    }
//
//    @Test(expected = UserAlreadyVotedException.class)
//    public void testAddVoteAlreadyVoted() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.DELETED, FOR_COUNT,AGAINST_COUNT );
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(true);
//        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//
//        debateService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
//
//    }
//
//    @Test(expected = DebateNotFoundException.class)
//    public void testAddVoteDebateNotFound() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        debateService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testAddVoteNotValidUser() {
//        debateService.addVote(DEBATE_ID, USER_USERNAME, DebateVote.FOR);
//    }
//
//    @Test
//    public void testRemoveVote() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        debateService.removeVote(DEBATE_ID, USER_USERNAME);
//
//        verify(debateDao).removeVote(anyLong(), anyLong());
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testRemoveVoteNotValidUser() {
//        debateService.removeVote(DEBATE_ID, USER_USERNAME);
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void testGetUserVoteInvalidUser() {
//        debateService.getUserVote(DEBATE_ID, USER_USERNAME);
//    }
//
//    @Test(expected = DebateNotFoundException.class)
//    public void testGetUserVoteInvalidDebate() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//
//        debateService.getUserVote(DEBATE_ID, USER_USERNAME);
//    }
//
//    @Test
//    public void testGetUserVoteNotVoted() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
//        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//
//        String vote = debateService.getUserVote(DEBATE_ID, USER_USERNAME);
//
//        assertNull(vote);
//    }
//
//    @Test
//    public void testGetUserVoteFor() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
//        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(true);
//        when(debateDao.getUserVote(anyLong(), anyLong())).thenReturn(DebateVote.FOR);
//
//        String vote = debateService.getUserVote(DEBATE_ID, USER_USERNAME);
//
//        assertEquals(DEBATE_CREATOR, vote);
//    }
//
//    @Test
//    public void testGetUserVoteAgainst() {
//        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
//        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));
//        PublicDebate debate = new PublicDebate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION, DEBATE_CREATOR, DEBATE_OPPONENT, IMAGE_ID, DEBATE_DATE, DebateCategory.OTHER, SUBSCRIBED_COUNT, DebateStatus.OPEN, FOR_COUNT, AGAINST_COUNT);
//        when(debateDao.getPublicDebateById(anyLong())).thenReturn(Optional.of(debate));
//        when(debateDao.hasUserVoted(anyLong(), anyLong())).thenReturn(true);
//        when(debateDao.getUserVote(anyLong(), anyLong())).thenReturn(DebateVote.AGAINST);
//
//        String vote = debateService.getUserVote(DEBATE_ID, USER_USERNAME);
//
//        assertEquals(DEBATE_OPPONENT, vote);
//    }
//
//    @Test
//    public void testCloseDebate() {
//        debateService.closeDebate(DEBATE_ID);
//
//        verify(debateDao).changeDebateStatus(anyLong(), eq(DebateStatus.CLOSED));
//    }

    @Test(expected = DebateNotFoundException.class)
    public void testStartConclusionNotValidDebate() {
        debateService.startConclusion(DEBATE_ID, CREATOR_USERNAME);
    }

    @Test(expected = ForbiddenDebateException.class)
    public void testStartConclusionInvalidUser() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, INVALID_USERNAME);
    }

    @Test(expected = ForbiddenDebateException.class)
    public void testStartConclusionAlreadyStarted() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        debate.setStatus(DebateStatus.CLOSING);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, CREATOR_USERNAME);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testStartConclusionDebateDeleted() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        debate.setStatus(DebateStatus.DELETED);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, CREATOR_USERNAME);
    }

    @Test(expected = ForbiddenDebateException.class)
    public void testStartConclusionClosed() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        debate.setStatus(DebateStatus.CLOSED);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, CREATOR_USERNAME);
    }

    @Test
    public void testStartConclusion() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, CREATOR_USERNAME);

//        verify(debate).setStatus(eq(DebateStatus.CLOSING));
    }

    @Test(expected = ForbiddenDebateException.class)
    public void testDeleteDebateInvalidUser() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.deleteDebate(DEBATE_ID, INVALID_USERNAME);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testDeleteAlreadyDeletedDebate() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        debate.setStatus(DebateStatus.DELETED);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.deleteDebate(DEBATE_ID, INVALID_USERNAME);
    }

    @Test
    public void testDeleteDebate() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.deleteDebate(DEBATE_ID, CREATOR_USERNAME);

        assertFalse(debateService.getDebateById(DEBATE_ID).isPresent());
    }

    @Test
    public void testCloseVotes() {
        // TODO: Figure out how to test with scheduled
    }
}
