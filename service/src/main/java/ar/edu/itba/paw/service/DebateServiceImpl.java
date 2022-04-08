package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.DebateDao;
import ar.edu.itba.paw.interfaces.DebateService;
import ar.edu.itba.paw.model.Debate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DebateServiceImpl implements DebateService {

    @Autowired
    private DebateDao debateDao;

    @Override
    public Optional<Debate> getDebateById(long id) {
        return debateDao.getDebateById(id);
    }

    @Override
    public Debate create(String name, String description) {
        return debateDao.create(name, description);
    }
}
