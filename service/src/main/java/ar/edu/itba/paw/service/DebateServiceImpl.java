package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.exceptions.DebateOponentException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (creatorUsername.equals(opponentUsername)) throw new DebateOponentException(opponentUsername, name, description, category);
        User creator = userDao.getUserByUsername(creatorUsername).orElseThrow(UserNotFoundException::new);
        User opponent = userDao.getUserByUsername(opponentUsername).orElseThrow(() -> new DebateOponentException(opponentUsername, name, description, category));
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
    public List<PublicDebate> get(int page, String search, String category) {
        if (search != null && category != null)
            return debateDao.getAll(page);
            // TODO: Implement search and category
//            return debateDao.getSearchCategory(page, search, DebateCategory.valueOf(category.toUpperCase()));
        if (search != null)
            return debateDao.getQuery(page, search);
        if (category != null)
            return debateDao.getAllFromCategory(DebateCategory.valueOf(category.toUpperCase()), page);
        return debateDao.getAll(page);
    }

    @Override
    public int getPages(String search, String category) {
        int count;
        if (search != null && category != null)
            count = debateDao.getAllcount();
            // TODO count = debateDao.getSearchCategoryCount(search, DebateCategory.valueOf(category.toUpperCase()));
        else if (search != null)
            count = debateDao.getQueryCount(search);
        else if (category != null)
            count = debateDao.getAllFromCategoryCount(DebateCategory.valueOf(category.toUpperCase()));
        else count = debateDao.getAllcount();
        return (int) Math.ceil(count / 5.0);
    }

    @Override
    public List<PublicDebate> getMostSubscribed() {
        return debateDao.getMostSubscribed();
        // return debateDao.getPublicDebatesGeneral(0, 3, null, null, DebateOrder.SUBS.setDescending());
    }

    @Override
    public Optional<PublicDebate> getPublicDebateById(long id) {
        return debateDao.getPublicDebateById(id);
    }

    @Override
    public void subscribeToDebate(long userid, long debateid) {
        debateDao.subscribeToDebate(userid, debateid);
    }
    @Override
    public void unsubscribeToDebate(long userid, long debateid) {
        debateDao.unsubscribeToDebate(userid, debateid);
    }
    @Override
    public boolean isUserSubscribed(long userid, long debateid) {
        return debateDao.isUserSubscribed(userid, debateid);
    }
}
