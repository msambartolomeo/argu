package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
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
    @Autowired
    private EmailService emailService;

    @Transactional
    @Override
    public Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category) {
        User creator = userService.getUserByUsername(creatorUsername).orElseThrow(UserNotFoundException::new);
        User opponent = userService.getUserByUsername(opponentUsername).orElseThrow(UserNotFoundException::new);
        Debate createdDebate;
        if (image.length == 0)
            createdDebate = debateDao.create(name, description, creator.getUserId(), opponent.getUserId(), null, category);
        else
            createdDebate = debateDao.create(name, description, creator.getUserId(), opponent.getUserId(), imageService.createImage(image).getId(), category);
        emailService.notifyNewInvite(opponent.getEmail(), creatorUsername, createdDebate.getDebateId(), createdDebate.getName());
        return createdDebate;
    }

    @Override
    public List<PublicDebate> get(int page, String search, String category, String order, String status, String date) {
        if (page < 0)
            return new ArrayList<>();
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
        if(debateDao.isUserSubscribed(user.getUserId(), debateid))
            throw new UserAlreadySubscribedException();
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
        if (page < 0) {
            return new ArrayList<>();
        }
        if (list.equals("subscribed"))
            return debateDao.getSubscribedDebatesByUserId(userid, page);
        else return debateDao.getMyDebates(userid, page);
    }

    @Override
    public int getProfileDebatesPageCount(String list, long userid) {
        int count;
        if (list.equals("subscribed"))
            count = debateDao.getSubscribedDebatesByUserIdCount(userid);
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
    public String getUserVote(long debateid, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        PublicDebate debate = debateDao.getPublicDebateById(debateid).orElseThrow(DebateNotFoundException::new);
        if(!debateDao.hasUserVoted(debateid, user.getUserId()))
            return null;
        DebateVote debateVote = debateDao.getUserVote(debateid, user.getUserId());

        if(debateVote == DebateVote.FOR) {
            return debate.getCreatorUsername();
        } else
            return debate.getOpponentUsername();
    }

    @Override
    public void startConclusion(long id, String username) {
        PublicDebate debate = getPublicDebateById(id).orElseThrow(DebateNotFoundException::new);

        if (debate.getDebateStatus() != DebateStatus.OPEN || !(username.equals(debate.getCreatorUsername()) || username.equals(debate.getOpponentUsername())))
            throw new ForbiddenPostException(); // TODO change to ForbiddenDebate ?

        debateDao.changeDebateStatus(id, DebateStatus.CLOSING);
    }

    @Override
    public void closeDebate(long id) {
        debateDao.changeDebateStatus(id, DebateStatus.CLOSED);
    }
}
