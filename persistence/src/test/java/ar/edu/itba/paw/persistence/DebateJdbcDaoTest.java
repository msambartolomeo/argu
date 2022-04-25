package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Debate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import static org.junit.Assert.*;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DebateJdbcDaoTest {

    private DebateJdbcDao debateDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    private DataSource ds;

    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";
    private final static String DEBATES_TABLE = "debates";
    private final static String ID = "debateid";
    private final static int DEBATES_PAGE = 0;

    @Before
    public void setUp() {
        debateDao = new DebateJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(DEBATES_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    @After
    public void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEBATES_TABLE);
    }

//    @Test
//    public void testCreateDebate() {
//        Debate debate = debateDao.create(DEBATE_NAME, DEBATE_DESCRIPTION, null);
//
//        assertNotNull(debate);
//        assertEquals(DEBATE_NAME, debate.getName());
//        assertEquals(DEBATE_DESCRIPTION, debate.getDescription());
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, DEBATES_TABLE));
//    }

    @Test
    public void testGetDebateByIdExists() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        Number key = jdbcInsert.executeAndReturnKey(debateData);

        Optional<Debate> debate = debateDao.getDebateById(key.longValue());

        assertTrue(debate.isPresent());
        assertEquals(DEBATE_NAME, debate.get().getName());
        assertEquals(DEBATE_DESCRIPTION, debate.get().getDescription());
    }

    @Test
    public void testGetDebateByIdDoesntExist() {
        Optional<Debate> debate = debateDao.getDebateById(DEBATE_ID);

        assertFalse(debate.isPresent());
    }

    @Test
    public void testGetAllDebates() {
        final Map<String, Object> debateData = new HashMap<>();
        debateData.put("name", DEBATE_NAME);
        debateData.put("description", DEBATE_DESCRIPTION);
        jdbcInsert.execute(debateData);

        List<Debate> debates = debateDao.getAll(DEBATES_PAGE);

        assertEquals(1, debates.size());
        assertEquals(DEBATE_NAME, debates.get(0).getName());
        assertEquals(DEBATE_DESCRIPTION, debates.get(0).getDescription());
    }

    @Test
    public void testGetAllEmpty() {
        List<Debate> debates = debateDao.getAll(DEBATES_PAGE);

        assertTrue(debates.isEmpty());
    }
}
