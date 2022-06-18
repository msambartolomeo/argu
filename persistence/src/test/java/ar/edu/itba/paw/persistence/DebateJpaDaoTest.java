package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Subscribed;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DebateJpaDaoTest {

    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Test";
    private final static String DEBATE_DESCRIPTION = "Debate Test Description";
    private final static DebateCategory DEBATE_CATEGORY = DebateCategory.OTHER;

    private final static String CREATOR_USERNAME = "creator_username";
    private final static String CREATOR_PASSWORD = "creator_password";
    private final static String CREATOR_EMAIL = "creator@creator.com";

    private final static String OPPONENT_USERNAME = "opponent_username";
    private final static String OPPONENT_PASSWORD = "opponent_password";
    private final static String OPPONENT_EMAIL = "opponent@opponent.com";

    private final static boolean IS_CREATOR_FOR = true;

    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    private final static int PAGE = 0;

    private User creator;
    private User opponent;
    private Image image;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DebateJpaDao debateJpaDao;

    @Before
    public void setUp() {
        creator = new User(CREATOR_EMAIL, CREATOR_USERNAME, CREATOR_PASSWORD);
        opponent = new User(OPPONENT_EMAIL, OPPONENT_USERNAME, OPPONENT_PASSWORD);
        image = new Image(IMAGE_DATA);
        em.persist(creator);
        em.persist(opponent);
        em.persist(image);
    }

    @Test
    public void testCreateDebate() {
        Debate debate = debateJpaDao.create(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);

        assertNotNull(debate);
        assertEquals(DEBATE_NAME, debate.getName());
        assertEquals(DEBATE_DESCRIPTION, debate.getDescription());
        assertEquals(creator.getUserId(), debate.getCreator().getUserId());
        assertTrue(debate.getIsCreatorFor());
        assertEquals(opponent.getUserId(), debate.getOpponent().getUserId());
        assertEquals(DEBATE_CATEGORY, debate.getCategory());
    }

    @Test
    public void testCreateDebateWithImage() {
        Debate debate = debateJpaDao.create(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, image, DEBATE_CATEGORY);

        assertNotNull(debate);
        assertEquals(DEBATE_NAME, debate.getName());
        assertEquals(DEBATE_DESCRIPTION, debate.getDescription());
        assertEquals(creator.getUserId(), debate.getCreator().getUserId());
        assertTrue(debate.getIsCreatorFor());
        assertEquals(opponent.getUserId(), debate.getOpponent().getUserId());
        assertEquals(image.getData(), debate.getImage().getData());
        assertEquals(DEBATE_CATEGORY, debate.getCategory());
    }

    @Test
    public void testGetDebateByIdEmpty() {
        Optional<Debate> debate = debateJpaDao.getDebateById(DEBATE_ID);

        assertFalse(debate.isPresent());
    }

    @Test
    public void testGetDebateById() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);

        em.persist(debate);

        Optional<Debate> maybeDebate = debateJpaDao.getDebateById(debate.getDebateId());

        assertTrue(maybeDebate.isPresent());
        assertEquals(DEBATE_NAME, debate.getName());
        assertEquals(DEBATE_DESCRIPTION, debate.getDescription());
        assertEquals(creator.getUserId(), debate.getCreator().getUserId());
        assertTrue(debate.getIsCreatorFor());
        assertEquals(opponent.getUserId(), debate.getOpponent().getUserId());
        assertEquals(DEBATE_CATEGORY, debate.getCategory());
    }

    @Test
    public void testGetSubscribedDebatesEmpty() {
        List<Debate> debates = debateJpaDao.getSubscribedDebatesByUser(creator.getUserId(), PAGE);

        assertTrue(debates.isEmpty());
    }

    @Test
    public void testGetSubscribedDebates() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);
        em.persist(debate);

        Subscribed subscribed = new Subscribed(creator, debate);
        em.persist(subscribed);

        List<Debate> debates = debateJpaDao.getSubscribedDebatesByUser(creator.getUserId(), PAGE);

        assertFalse(debates.isEmpty());
        assertEquals(DEBATE_NAME, debates.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, debates.get(0).getDescription());
        assertEquals(creator.getUserId(), debates.get(0).getCreator().getUserId());
        assertTrue(debates.get(0).getIsCreatorFor());
        assertEquals(opponent.getUserId(), debates.get(0).getOpponent().getUserId());
        assertEquals(DEBATE_CATEGORY, debates.get(0).getCategory());
    }

    @Test
    public void testGetSubscribedDebatesByUserIdCountEmpty() {
        int count = debateJpaDao.getSubscribedDebatesByUserCount(creator.getUserId());

        assertEquals(0, count);
    }

    @Test
    public void testGetSubscribedDebatesByUserIdCount() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);
        em.persist(debate);

        Subscribed subscribed = new Subscribed(creator, debate);
        em.persist(subscribed);

        int count = debateJpaDao.getSubscribedDebatesByUserCount(creator.getUserId());

        assertEquals(1, count);
    }

    @Test
    public void getDebatesDiscovery() {
        // TODO: Do tests with discovery
    }

    @Test
    public void testGetDebatesCountEmpty() {
        // TODO: Do tests with discovery
    }

    @Test
    public void testGetUserDebatesEmpty() {
        List<Debate> userDebates = debateJpaDao.getUserDebates(creator.getUserId(), PAGE);

        assertTrue(userDebates.isEmpty());
    }

    @Test
    public void testGetUserDebatesCreator() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);
        em.persist(debate);

        List<Debate> creatorDebates = debateJpaDao.getUserDebates(creator.getUserId(), PAGE);

        assertEquals(1, creatorDebates.size());
        assertEquals(DEBATE_NAME, creatorDebates.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, creatorDebates.get(0).getDescription());
        assertEquals(creator.getUserId(), creatorDebates.get(0).getCreator().getUserId());
        assertTrue(creatorDebates.get(0).getIsCreatorFor());
        assertEquals(opponent.getUserId(), creatorDebates.get(0).getOpponent().getUserId());
        assertEquals(DEBATE_CATEGORY, creatorDebates.get(0).getCategory());
    }

    @Test
    public void testGetUserDebatesOpponent() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);
        em.persist(debate);

        List<Debate> opponentDebates = debateJpaDao.getUserDebates(opponent.getUserId(), PAGE);

        assertEquals(1, opponentDebates.size());
        assertEquals(DEBATE_NAME, opponentDebates.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, opponentDebates.get(0).getDescription());
        assertEquals(creator.getUserId(), opponentDebates.get(0).getCreator().getUserId());
        assertTrue(opponentDebates.get(0).getIsCreatorFor());
        assertEquals(opponent.getUserId(), opponentDebates.get(0).getOpponent().getUserId());
        assertEquals(DEBATE_CATEGORY, opponentDebates.get(0).getCategory());
    }

    @Test
    public void testGetUserDebatesCountEmpty() {
        int count = debateJpaDao.getUserDebatesCount(creator.getUserId());

        assertEquals(0, count);
    }

    @Test
    public void testGetUserDebatesCount() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);
        em.persist(debate);

        int count = debateJpaDao.getUserDebatesCount(creator.getUserId());

        assertEquals(1, count);
    }

    @Test
    public void testGetDebateToCloseEmpty() {
        List<Debate> debates = debateJpaDao.getDebatesToClose();

        assertTrue(debates.isEmpty());
    }

    @Test
    public void testGetDebateToClose() {
        Debate debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY);
        debate.setDateToClose(LocalDate.now());
        debate.setStatus(DebateStatus.VOTING);

        em.persist(debate);

        List<Debate> debatesToClose = debateJpaDao.getDebatesToClose();

        assertEquals(1, debatesToClose.size());
        assertEquals(DEBATE_NAME, debatesToClose.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, debatesToClose.get(0).getDescription());
        assertEquals(creator.getUserId(), debatesToClose.get(0).getCreator().getUserId());
        assertTrue(debatesToClose.get(0).getIsCreatorFor());
        assertEquals(opponent.getUserId(), debatesToClose.get(0).getOpponent().getUserId());
        assertEquals(DEBATE_CATEGORY, debatesToClose.get(0).getCategory());
    }
}
