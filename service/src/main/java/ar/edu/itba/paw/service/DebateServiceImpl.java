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
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DebateServiceImpl implements DebateService {

    private static final int PAGE_SIZE = 5;
    @Autowired
    private DebateDao debateDao;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;

    @Override
    public Optional<Debate> getDebateById(long id) {
        return debateDao.getDebateById(id);
    }

    @Transactional
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

    @Transactional
    @Override
    public void subscribeToDebate(String username, long debateid) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        debateDao.subscribeToDebate(user.getUserId(), debateid);
    }
    @Transactional
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

    @Override
    public List<PublicDebate> getProfileDebates(String list, long userid, int page) {
        if (list.equals("subscribed"))
            return debateDao.getSubscribedDebatesByUsername(userid, page);
        else return debateDao.getMyDebates(userid, page);
    }

    @Override
    public int getProfileDebatesPageCount(String list, long userid) {
        int count;
        if (list.equals("subscribed"))
            count = debateDao.getSubscribedDebatesByUsernameCount(userid);
        else count = debateDao.getMyDebatesCount(userid);
        return (int) Math.ceil(count / (double) PAGE_SIZE);
    }

    @Override
    public void addVote(long debateId, String username, DebateVote vote) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        if (debateDao.hasUserVoted(debateId, user.getUserId()))
            throw new UserAlreadyVotedException();
        debateDao.addVote(debateId, user.getUserId(), vote);
    }

    @Override
    public void removeVote(long debateId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        debateDao.removeVote(debateId, user.getUserId());
    }

    @Override
    public Boolean hasUserVoted(long debateid, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        return debateDao.hasUserVoted(debateid, user.getUserId());
    }

    @Override
    public String getUserVote(long debateid, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        DebateVote debateVote = debateDao.getUserVote(debateid, user.getUserId());
        PublicDebate debate = debateDao.getPublicDebateById(debateid).orElseThrow(DebateNotFoundException::new);

        if(debateVote == DebateVote.FOR) {
            return debate.getCreatorUsername();
        } else
            return debate.getOpponentUsername();
    }
}
