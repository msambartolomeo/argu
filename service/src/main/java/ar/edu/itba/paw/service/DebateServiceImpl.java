package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DebateServiceImpl implements DebateService {

    private static final int PAGE_SIZE = 5;
    @Autowired
    private UserService userService;
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
        User creator = userService.getUserByUsername(creatorUsername).orElseThrow(UserNotFoundException::new);
        User opponent = userService.getUserByUsername(opponentUsername).orElseThrow(UserNotFoundException::new);
        if (image.length == 0)
            return debateDao.create(name, description, creator.getUserId(), opponent.getUserId(), null, category);
        else
            return debateDao.create(name, description, creator.getUserId(), opponent.getUserId(), imageService.createImage(image), category);
    }

    @Override
    public List<PublicDebate> getSubscribedDebatesByUsername(long userid, int page) {
        return debateDao.getSubscribedDebatesByUsername(userid, page);
    }

    @Override
    public int getSubscribedDebatesByUsernameCount(long userid) {
        return debateDao.getSubscribedDebatesByUsernameCount(userid);
    }

    @Override
    public List<PublicDebate> get(int page, String search, String category, String order, String status, String date) {
        return debateDao.getPublicDebatesGeneral(page, PAGE_SIZE, search, category, order, status, date);
    }

    @Override
    public int getPages(String search, String category, String status, String date) {
        return (int) Math.ceil(debateDao.getPublicDebatesCount(search, category, status, date) / (double) PAGE_SIZE);
    }

    @Override
    public List<PublicDebate> getMostSubscribed() {
        return debateDao.getPublicDebatesGeneral(0, 3, null, null, String.valueOf(DebateOrder.SUBS_DESC), null, null);
    }

    @Override
    public Optional<PublicDebate> getPublicDebateById(long id) {
        return debateDao.getPublicDebateById(id);
    }

    @Override
    public void subscribeToDebate(String username, long debateid) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        debateDao.subscribeToDebate(user.getUserId(), debateid);
    }
    @Override
    public void unsubscribeToDebate(String username, long debateid) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        debateDao.unsubscribeToDebate(user.getUserId(), debateid);
    }
    @Override
    public boolean isUserSubscribed(String username, long debateid) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        return debateDao.isUserSubscribed(user.getUserId(), debateid);
    }
}
