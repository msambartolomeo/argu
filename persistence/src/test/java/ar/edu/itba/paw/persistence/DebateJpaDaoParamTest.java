package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(Parameterized.class)
@ContextConfiguration(classes = TestConfig.class)
public class DebateJpaDaoParamTest {

    @ClassRule
    public static final SpringClassRule scr = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();

    private final static String DEBATE_NAME_1 = "A Debate Name Test";
    private final static String DEBATE_NAME_2 = "B Debate Name Test";
    private final static String DEBATE_NAME_3 = "C Debate Name Test";
    private final static LocalDate DEBATE_DATE = LocalDate.now();
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static DebateCategory DEBATE_CATEGORY_1 = DebateCategory.CULTURE;
    private final static DebateCategory DEBATE_CATEGORY_2 = DebateCategory.POLITICS;
    private final static DebateCategory DEBATE_CATEGORY_3 = DebateCategory.OTHER;

    private final static String CREATOR_USERNAME = "creator_username";
    private final static String CREATOR_PASSWORD = "creator_password";
    private final static String CREATOR_EMAIL = "creator@creator.com";

    private final static String OPPONENT_USERNAME = "opponent_username";
    private final static String OPPONENT_PASSWORD = "opponent_password";
    private final static String OPPONENT_EMAIL = "opponent@opponent.com";

    private final static boolean IS_CREATOR_FOR = true;

    private static User creator;
    private static User opponent;
    private Debate debate1;
    private Debate debate2;
    private Debate debate3;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DebateJpaDao debateJpaDao;

    @Before
    public void setUp() throws InterruptedException {
        creator = new User(CREATOR_EMAIL, CREATOR_USERNAME, CREATOR_PASSWORD);
        opponent = new User(OPPONENT_EMAIL, OPPONENT_USERNAME, OPPONENT_PASSWORD);
        em.persist(creator);
        em.persist(opponent);

        debate1 = new Debate(DEBATE_NAME_1, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY_1);
        em.persist(debate1);
        TimeUnit.MILLISECONDS.sleep(1);
        debate2 = new Debate(DEBATE_NAME_2, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY_2);
        em.persist(debate2);
        TimeUnit.MILLISECONDS.sleep(1);
        debate3 = new Debate(DEBATE_NAME_3, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR,
                opponent, null, DEBATE_CATEGORY_3);
        debate3.setStatus(DebateStatus.CLOSED);
        em.persist(debate3);
    }

    @Parameters
    public static Iterable<Object[]> data() {
        creator = new User(CREATOR_EMAIL, CREATOR_USERNAME, CREATOR_PASSWORD);
        opponent = new User(OPPONENT_EMAIL, OPPONENT_USERNAME, OPPONENT_PASSWORD);
        Debate debate1 = new Debate(DEBATE_NAME_1, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, null, DEBATE_CATEGORY_1);
        Debate debate2 = new Debate(DEBATE_NAME_2, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, null, DEBATE_CATEGORY_2);
        Debate debate3 = new Debate(DEBATE_NAME_3, DEBATE_DESCRIPTION, creator, IS_CREATOR_FOR, opponent, null, DEBATE_CATEGORY_3);
        debate3.setStatus(DebateStatus.CLOSED);
        return Arrays.asList(new Object[][]{
                {0, 3, null, null, null, null, null, 3, Arrays.asList( // Shows default condition, DebateOrder.DATE_DESC
                        debate3, debate2, debate1)
                },
                {0, 3, null, null, DebateOrder.ALPHA_ASC, null, null, 3, Arrays.asList( // Shows different order
                        debate1, debate2, debate3)
                },
                {0, 2, null, null, DebateOrder.ALPHA_ASC, null, null, 3, Arrays.asList( // Shows pagination limit
                        debate1, debate2)
                },
                {0, 3, "a deBate", null, DebateOrder.ALPHA_ASC, null, null, 1, Arrays.asList( // Shows case insensitive search by name
                        debate1)
                },
                {0, 3, "no results", null, DebateOrder.ALPHA_ASC, null, null, 0, Collections.emptyList()}, // Shows no results
                {0, 3, null, DEBATE_CATEGORY_2, null, null, null, 1, Arrays.asList( // Shows filter by category
                        debate2)
                },
                {0, 3, null, null, DebateOrder.SUBS_ASC, DebateStatus.OPEN, null, 2, Arrays.asList( // Shows filter by status Open
                        debate1, debate2)
                },
                {0, 3, null, null, null, DebateStatus.CLOSED, null, 1, Arrays.asList( // Shows filter by status Closed
                        debate3)
                },
                {0, 3, null, null, null, null, DEBATE_DATE, 3, Arrays.asList( // Shows filter by date from
                        debate3, debate2, debate1)
                },
                {0, 3, "test", DEBATE_CATEGORY_1, DebateOrder.DATE_ASC, DebateStatus.OPEN, DEBATE_DATE, 1, Arrays.asList(
                        debate1)
                },
        });
    }

    public DebateJpaDaoParamTest(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order,
                                  DebateStatus status, LocalDate date, int expectedTotal, List<Debate> expectedDebates) {
        this.page = page;
        this.pageSize = pageSize;
        this.searchQuery = searchQuery;
        this.category = category;
        this.order = order;
        this.status = status;
        this.date = date;
        this.expectedTotal = expectedTotal;
        this.expectedDebates = expectedDebates;
    }

    private int page;
    private int pageSize;
    private String searchQuery;
    private DebateCategory category;
    private DebateOrder order;
    private DebateStatus status;
    private LocalDate date;
    private List<Debate> expectedDebates;
    private int expectedTotal;

    @Test
    public void testGetDebatesDiscovery() {
        List<Debate> debates = debateJpaDao.getDebatesDiscovery(page, pageSize, searchQuery, category, order, status, date);

        assertEquals(expectedDebates.size(), debates.size());
        for(int i = 0; i < debates.size(); i++) {
            assertEquals(expectedDebates.get(i).getName(), debates.get(i).getName());
            assertEquals(expectedDebates.get(i).getDescription(), debates.get(i).getDescription());
            assertEquals(expectedDebates.get(i).getCreator().getEmail(), debates.get(i).getCreator().getEmail());
            assertEquals(expectedDebates.get(i).getIsCreatorFor(), debates.get(i).getIsCreatorFor());
            assertEquals(expectedDebates.get(i).getOpponent().getEmail(), debates.get(i).getOpponent().getEmail());
            assertEquals(expectedDebates.get(i).getCreatedDate().toLocalDate(), debates.get(i).getCreatedDate().toLocalDate());
            assertEquals(expectedDebates.get(i).getCategory(), debates.get(i).getCategory());
            assertEquals(expectedDebates.get(i).getStatus(), debates.get(i).getStatus());
            assertEquals(expectedDebates.get(i).getForCount(), debates.get(i).getForCount());
            assertEquals(expectedDebates.get(i).getAgainstCount(), debates.get(i).getAgainstCount());
        }
    }

    @Test
    public void testGetDebatesCount() {
        int count = debateJpaDao.getDebatesCount(searchQuery, category, status, date);

        assertEquals(expectedTotal, count);
    }
}
