package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.DebateCategory;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DebateServiceImpl implements DebateService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DebateDao debateDao;
    @Autowired
    private ImageService imageService;

    @Override
    public Optional<Debate> getDebateById(long id) {
        return debateDao.getDebateById(id);
    }

    @Override
    public Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category) {
        if (creatorUsername.equals(opponentUsername)) // TODO: Change proper Exceptions
            throw new IllegalArgumentException("The creator and opponent cannot be the same");
        User creator = userDao.getUserByUsername(creatorUsername).orElseThrow(UserNotFoundException::new);
        User opponent = userDao.getUserByUsername(opponentUsername).orElseThrow(UserNotFoundException::new);
        if (image.length == 0)
            return debateDao.create(name, description, creator.getUserId(), opponent.getUserId(), null, category);
        else
            return debateDao.create(name, description, creator.getUserId(), opponent.getUserId(), imageService.createImage(image), category);
    }

    @Override
    public List<Debate> getSubscribedDebatesByUsername(long userid, int page) {
        return debateDao.getSubscribedDebatesByUsername(userid, page);
    }

    @Override
    public List<Debate> get(int page, String search) {
        if (search != null)
            return debateDao.getQuery(page, search);
         else
             return debateDao.getAll(page);

    }

    @Override
    public List<Debate> getFromCategory(DebateCategory category, int page) {
        return debateDao.getAllFromCategory(category, page);
    }

    @Override
    public List<Debate> getMostSubscribed() {
        return debateDao.getMostSubscribed();
    }
}
