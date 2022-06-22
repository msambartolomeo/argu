package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenDebateException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
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
public class DebateServiceImplTest {

    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;

    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    private final static long CREATOR_ID = 1;
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
        creator = new User(CREATOR_EMAIL, CREATOR_USERNAME, CREATOR_PASSWORD, Locale.ENGLISH);
        opponent = new User(OPPONENT_EMAIL, OPPONENT_USERNAME, OPPONENT_PASSWORD, Locale.ENGLISH);
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

    @Test
    public void testCreateDebate() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);

        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(creator));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(opponent));
        when(debateDao.create(anyString(), anyString(), any(User.class), anyBoolean(), any(User.class), any(), any(DebateCategory.class)))
                .thenReturn(debate);

        Debate d = debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, CREATOR_USERNAME, IS_CREATOR_FOR, OPPONENT_USERNAME, IMAGE_DATA, DEBATE_CATEGORY);

        assertNotNull(d);
        assertEquals(debate, d);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateCreatorNotFound() {
        debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, CREATOR_USERNAME, IS_CREATOR_FOR, OPPONENT_USERNAME, IMAGE_DATA, DebateCategory.OTHER);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateOpponentNotFound() {
        when(userService.getUserByUsername(CREATOR_USERNAME)).thenReturn(Optional.of(creator));

        debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION, CREATOR_USERNAME, IS_CREATOR_FOR, OPPONENT_USERNAME, IMAGE_DATA, DebateCategory.OTHER);
    }

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
        assertNull(debate.getImage());
        assertNull(d.getImage());
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

    @Test
    public void testGetProfileDebatesSubscribed() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        List<Debate> debates = new ArrayList<>();
        debates.add(debate);
        when(debateDao.getSubscribedDebatesByUser(anyLong(), anyInt())).thenReturn(debates);

        List<Debate> dl = debateService.getProfileDebates("subscribed", CREATOR_ID, VALID_PAGE);

        assertFalse(dl.isEmpty());
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
    public void testGetUserDebatesInvalidPage() {
        List<Debate> dl = debateService.getUserDebates(CREATOR_ID, INVALID_PAGE);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetProfileUserDebatesEmpty() {
        List<Debate> dl = debateService.getProfileDebates("mydebates", CREATOR_ID, VALID_PAGE);

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
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        debateService.startConclusion(DEBATE_ID, CREATOR_USERNAME);

        assertEquals(DebateStatus.CLOSING, debateService.getDebateById(DEBATE_ID).get().getStatus());
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

    @Test(expected = DebateNotFoundException.class)
    public void testGetRecommendedDebatesWithUserDebateNotFound() {
        List<Debate> dl = debateService.getRecommendedDebates(DEBATE_ID, CREATOR_USERNAME);

        assertTrue(dl.isEmpty());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetRecommendedDebatesWithUserUserNotFound() {
        final Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        List<Debate> dl = debateService.getRecommendedDebates(DEBATE_ID, CREATOR_USERNAME);

        assertTrue(dl.isEmpty());
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetRecommendedDebatesDebateNotFound() {
        List<Debate> dl = debateService.getRecommendedDebates(DEBATE_ID);

        assertTrue(dl.isEmpty());
    }

    @Test
    public void testGetRecommendedDebates() {
        final Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(debateDao.getRecommendedDebates(any(Debate.class))).thenReturn(Collections.singletonList(debate));

        List<Debate> dl = debateService.getRecommendedDebates(DEBATE_ID);

        assertFalse(dl.isEmpty());
        assertEquals(debate, dl.get(0));
    }

    @Test
    public void testGetRecommendedDebatesWithUser() {
        final Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, image, DEBATE_CATEGORY);
        when(debateDao.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(creator));
        when(debateDao.getRecommendedDebates(any(Debate.class), any(User.class))).thenReturn(Collections.singletonList(debate));

        List<Debate> dl = debateService.getRecommendedDebates(DEBATE_ID, CREATOR_USERNAME);

        assertFalse(dl.isEmpty());
        assertEquals(debate, dl.get(0));
    }
}
