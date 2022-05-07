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
import ar.edu.itba.paw.model.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.DebateOponentException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DebateServiceImpl implements DebateService {

    private static final int PAGE_SIZE = 5;
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

    @Transactional
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

    private void verifyDebateFilters(String category, String status, String date) {
        if (status != null && !status.equals("open") && !status.equals("closed"))
            throw new DebateNotFoundException(); // TODO change exception (?)
        if (category != null && Arrays.stream(DebateCategory.values()).noneMatch((c) -> c.getName().equals(category)))
            throw new CategoryNotFoundException();
        if (date != null && !date.matches("\\d{2}-\\d{2}-\\d{4}"))
            throw new DebateNotFoundException(); // TODO change exception (?)
    }

    @Override
    public List<PublicDebate> get(String page, String search, String category, String order, String status, String date) {
        if (!page.matches("-?\\d+")) throw new DebateNotFoundException();
        if (order != null && Arrays.stream(DebateOrder.values()).noneMatch((o) -> o.getName().equals(order)))
            throw new DebateNotFoundException(); // TODO change exception (?)
        verifyDebateFilters(category, status, date);
        return debateDao.getPublicDebatesGeneral(Integer.parseInt(page), PAGE_SIZE, search, category, order, status, date);
    }

    @Override
    public int getPages(String search, String category, String status, String date) {
        verifyDebateFilters(category, status, date);
        return (int) Math.ceil(debateDao.getPublicDebatesCount(search, category, status, date) / (double) PAGE_SIZE);
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

    @Transactional
    @Override
    public void subscribeToDebate(long userid, long debateid) {
        debateDao.subscribeToDebate(userid, debateid);
    }
    @Transactional
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
