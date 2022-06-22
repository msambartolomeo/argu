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
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenDebateException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DebateServiceImpl implements DebateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebateServiceImpl.class);
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
    @Transactional
    public Optional<Debate> getDebateById(long debateId) {
        Optional<Debate> debate = debateDao.getDebateById(debateId);
        if(debate.isPresent() && debate.get().getStatus() == DebateStatus.DELETED) return Optional.empty();
        return debate;
    }

    @Transactional
    @Override
    public Debate create(String name, String description, String creatorUsername, boolean isCreatorFor, String opponentUsername,
                         byte[] image,
                         DebateCategory category) {
        User creator = userService.getUserByUsername(creatorUsername).orElseThrow(() -> {
            LOGGER.error("Cannot create new Debate with name {} because creator User {} does not exist", name, creatorUsername);
            return new UserNotFoundException();
        });
        User opponent = userService.getUserByUsername(opponentUsername).orElseThrow(() -> {
            LOGGER.error("Cannot create new Debate with name {} because opponent User {} does not exist", name, opponentUsername);
            return new UserNotFoundException();
        });
        Debate createdDebate;
        if (image.length == 0)
            createdDebate = debateDao.create(name, description, creator, isCreatorFor, opponent, null, category);
        else
            createdDebate = debateDao.create(name, description, creator, isCreatorFor, opponent, imageService.createImage(image), category);
        emailService.notifyNewInvite(opponent.getEmail(), creatorUsername, createdDebate, opponent.getLocale());
        return createdDebate;
    }

    @Override
    @Transactional
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
    @Transactional
    public List<Debate> getMostSubscribed() {
        return debateDao.getDebatesDiscovery(0, 3, null, null, DebateOrder.SUBS_DESC, null, null);
    }

    @Override
    @Transactional
    public List<Debate> getProfileDebates(String list, long userId, int page) {
        if (list.equals("mydebates"))
            return getUserDebates(userId, page);

        if (page < 0) {
            return Collections.emptyList();
        }
        return debateDao.getSubscribedDebatesByUser(userId, page);
    }

    @Override
    @Transactional
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
    @Transactional
    public int getUserDebatesPageCount(long userId) {
        return (int) Math.ceil(debateDao.getUserDebatesCount(userId) / (double) PAGE_SIZE);
    }

    @Transactional
    @Override
    public void startConclusion(long id, String username) {
        Debate debate = getDebateById(id).orElseThrow(() -> {
            LOGGER.error("Cannot start conclusion of Debate with id {} because it does not exist", id);
            return new DebateNotFoundException();
        });

        if (debate.getStatus() != DebateStatus.OPEN || !(username.equals(debate.getCreator().getUsername()) || username.equals(debate.getOpponent().getUsername()))) {
            LOGGER.error("Cannot start conclusion of Debate with id {} because it is not open or the user {} is not the creator or the opponent", id, username);
            throw new ForbiddenDebateException();
        }

        debate.setStatus(DebateStatus.CLOSING);
    }

    @Transactional
    @Override
    public void deleteDebate(long id, String username) {
        Debate debate = getDebateById(id).orElseThrow(() -> {
            LOGGER.error("Cannot delete Debate {} because it does not exist", id);
            return new DebateNotFoundException();
        });

        if (debate.getStatus() == DebateStatus.DELETED || !username.equals(debate.getCreator().getUsername())) {
            LOGGER.error("Cannot delete Debate {} because it is already deleted or the user {} is not the creator", id, username);
            throw new ForbiddenDebateException();
        }

        debate.setStatus(DebateStatus.DELETED);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // Runs at midnight every day
    public void closeVotes() {
        for (Debate debate : debateDao.getDebatesToClose()) {
            debate.closeDebate();
        }
    }

    @Override
    public List<Debate> getRecommendedDebates(long debateid) {
        Debate debate = getDebateById(debateid).orElseThrow(() -> {
            LOGGER.error("Cannot get recommended debates for Debate with id {} because it does not exist", debateid);
            return new DebateNotFoundException();
        });
        return debateDao.getRecommendedDebates(debate);
    }

    @Override
    public List<Debate> getRecommendedDebates(long debateid, String username) {
        Debate debate = getDebateById(debateid).orElseThrow(() -> {
            LOGGER.error("Cannot get recommended debates for Debate with id {} because it does not exist", debateid);
            return new DebateNotFoundException();
        });
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot get recommended debates for Debate with id {} because user with username {} does not exist", debateid, username);
            return new UserNotFoundException();
        });
        return debateDao.getRecommendedDebates(debate, user);
    }
}
