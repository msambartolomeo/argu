package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.Debate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DebateServiceImplTest {

    private final static int PAGE = 1;
    private final static long DEBATE_ID = 1;
    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";

    @InjectMocks
    private DebateServiceImpl debateService = new DebateServiceImpl();

    @Mock
    private DebateDao debateDao;

    @Test
    public void testCreateDebate() {
        Debate debate = new Debate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION);
        Mockito.when(debateDao.create(Mockito.anyString(), Mockito.anyString())).thenReturn(debate);


        Debate d = debateService.create(DEBATE_NAME, DEBATE_DESCRIPTION);

        assertEquals(debate, d);
    }

    @Test
    public void testGetDebateById() {
        Debate debate = new Debate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION);
        Mockito.when(debateDao.getDebateById(DEBATE_ID)).thenReturn(Optional.of(debate));

        Optional<Debate> d = debateService.getDebateById(DEBATE_ID);

        assertTrue(d.isPresent());
        assertEquals(debate, d.get());
    }

    @Test
    public void testGetDebateDoesntExist() {
        Mockito.when(debateDao.getDebateById(DEBATE_ID)).thenReturn(Optional.empty());

        Optional<Debate> d = debateService.getDebateById(DEBATE_ID);

        assertFalse(d.isPresent());
    }

    @Test
    public void testGetAll() {
        Debate debate = new Debate(DEBATE_ID, DEBATE_NAME, DEBATE_DESCRIPTION);
        List<Debate> debates = new ArrayList<>();
        debates.add(debate);
        Mockito.when(debateDao.getAll(Mockito.anyInt())).thenReturn(debates);

        List<Debate> dl = debateService.getAll(PAGE);

        assertFalse(dl.isEmpty());
        assertEquals(debate, dl.get(0));
    }

    @Test
    public void testGetAllEmpty() {
        Mockito.when(debateDao.getAll(Mockito.anyInt())).thenReturn(new ArrayList<>());

        List<Debate> dl = debateService.getAll(PAGE);

        assertTrue(dl.isEmpty());
    }
}
