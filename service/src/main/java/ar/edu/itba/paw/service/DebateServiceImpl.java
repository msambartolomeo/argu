package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Debate> getDebateById(long debateId) {
        return debateDao.getDebateById(debateId);
    }

    @Transactional
    @Override
    public Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category) {
        User creator = userService.getUserByUsername(creatorUsername).orElseThrow(UserNotFoundException::new);
        User opponent = userService.getUserByUsername(opponentUsername).orElseThrow(UserNotFoundException::new);
        Debate createdDebate;
        if (image.length == 0)
            createdDebate = debateDao.create(name, description, creator, opponent, null, category);
        else
            createdDebate = debateDao.create(name, description, creator, opponent, imageService.createImage(image), category);
        emailService.notifyNewInvite(opponent.getEmail(), creatorUsername, createdDebate.getDebateId(), createdDebate.getName());
        return createdDebate;
    }

    @Override
    public List<Debate> get(int page, String search, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date) {
        if (page < 0)
            return Collections.emptyList();
        return debateDao.getDebatesDiscovery(page, PAGE_SIZE, search, category, order, status, date);
    }

    @Override
    public int getPages(String search, DebateCategory category, DebateStatus status, LocalDate date) {
        return (int) Math.ceil(debateDao.getDebatesCount(search, category, status, date) / (double) PAGE_SIZE);
    }

    @Override
    public List<Debate> getMostSubscribed() {
        return debateDao.getDebatesDiscovery(0, 3, null, null, DebateOrder.SUBS_DESC, null, null);
    }

    @Override
    public List<Debate> getProfileDebates(String list, long userId, int page) {
        if (list.equals("mydebates"))
            return getUserDebates(userId, page);

        if (page < 0) {
            return Collections.emptyList();
        }
        return debateDao.getSubscribedDebatesByUser(userId, page);
    }

    @Override
    public List<Debate> getUserDebates(long userId, int page) {
        if (page < 0) {
            return Collections.emptyList();
        }
        return debateDao.getUserDebates(userId, page);
    }

    @Override
    public int getProfileDebatesPageCount(String list, long userId) {
        if (list.equals("mydebates"))
            return getUserDebatesPageCount(userId);

        return (int) Math.ceil(debateDao.getSubscribedDebatesByUserCount(userId) / (double) PAGE_SIZE);
    }

    @Override
    public int getUserDebatesPageCount(long userId) {
        return (int) Math.ceil(debateDao.getUserDebatesCount(userId) / (double) PAGE_SIZE);
    }

    @Transactional
    @Override
    public void startConclusion(long id, String username) {
        Debate debate = getDebateById(id).orElseThrow(DebateNotFoundException::new);

        if (debate.getStatus() != DebateStatus.OPEN || !(username.equals(debate.getCreator().getUsername()) || username.equals(debate.getOpponent().getUsername())))
            throw new ForbiddenDebateException();

        debate.setStatus(DebateStatus.CLOSING);
    }

}
