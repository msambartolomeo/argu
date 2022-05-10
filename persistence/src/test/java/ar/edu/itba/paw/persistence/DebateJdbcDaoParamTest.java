package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import javax.sql.DataSource;
import java.util.Arrays;

@RunWith(Parameterized.class)
@ContextConfiguration(classes = TestConfig.class)
public class DebateJdbcDaoParamTest {

    @ClassRule
    public static final SpringClassRule scr = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 1},
                {2, 2},
        });
    }

    private long debateId;
    private long expected;

    public DebateJdbcDaoParamTest(int debateId, int expected) {
        this.debateId = debateId;
        this.expected = expected;
    }

    @Autowired
    private DataSource ds;
    private DebateDao debateDao;

    @Before
    public void setUp() {
        debateDao = new DebateJdbcDao(ds);
    }
    @After
    public void tearDown() {
        debateDao = null;
    }

    @Test
    public void test() {
        debateDao.unsubscribeToDebate(debateId, expected);
    }
}
