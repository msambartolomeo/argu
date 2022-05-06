package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.User;
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
    public List<PublicDebate> get(int page, String search) {
        if (search != null)
            return debateDao.getQuery(page, search);
         else
             return debateDao.getAll(page);
    }

    @Override
    public int getCount(String search) {
        if (search != null)
            return debateDao.getQueryCount(search);
        else
            return debateDao.getAllcount();
    }

    @Override
    public List<PublicDebate> getFromCategory(DebateCategory category, int page) {
        return debateDao.getAllFromCategory(category, page);
    }

    @Override
    public int getFromCategoryCount(DebateCategory category) {
        return debateDao.getAllFromCategoryCount(category);
    }

    @Override
    public List<PublicDebate> getMostSubscribed() {
        return debateDao.getMostSubscribed();
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

    @Override
    public List<PublicDebate> getProfileDebates(String list, long userid, int page) {
        if(list == null)
            return debateDao.getSubscribedDebatesByUsername(userid, page);
        else if(list.compareTo("mydebates") == 0)
            return debateDao.getMyDebates(userid, page);
        else if(list.compareTo("subscribed") == 0)
            return debateDao.getSubscribedDebatesByUsername(userid, page);

        return debateDao.getSubscribedDebatesByUsername(userid, page);
    }

    @Override
    public int getProfileDebatesPageCount(String list, long userid) {
        int count;
        if(list == null)
            count = debateDao.getSubscribedDebatesByUsernameCount(userid);
        else if(list.compareTo("mydebates") == 0)
            count = debateDao.getMyDebatesCount(userid);
        else
            count = debateDao.getSubscribedDebatesByUsernameCount(userid);
        return (int) Math.ceil(count / 5.0);
    }
}
