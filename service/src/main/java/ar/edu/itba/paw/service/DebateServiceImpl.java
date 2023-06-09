package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateClosedException;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
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
        if (debate.isPresent() && debate.get().getStatus() == DebateStatus.DELETED) return Optional.empty();
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
    public List<Debate> get(int page, int size, String search, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date) {
        if (page < 0 || size <= 0)
            return Collections.emptyList();
        return debateDao.getDebatesDiscovery(page, size, search, category, order, status, date);
    }

    @Override
    public int getPages(int size, String search, DebateCategory category, DebateStatus status, LocalDate date) {
        return (int) Math.ceil(debateDao.getDebatesCount(search, category, status, date) / (double) size);
    }

    @Override
    @Transactional
    public List<Debate> getUserDebates(String username, int page, int size, boolean subscribed) {
        if (page < 0) {
            return Collections.emptyList();
        }

        final User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot get user debates for User {} because it does not exist", username);
            return new UserNotFoundException();
        });

        if (subscribed) {
            return debateDao.getSubscribedDebatesByUser(user, page, size);
        }
        return debateDao.getUserDebates(user, page, size);
    }

    @Override
    @Transactional
    public int getUserDebatesPageCount(String username, int size, boolean subscribed) {
        final User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot get user debates for User {} because it does not exist", username);
            return new UserNotFoundException();
        });

        if (subscribed) {
            return (int) Math.ceil(debateDao.getSubscribedDebatesByUserCount(user) / (double) size);
        }
        return (int) Math.ceil(debateDao.getUserDebatesCount(user) / (double) size);
    }

    @Transactional
    @Override
    public void startConclusion(long id) {
        Debate debate = debateDao.getDebateById(id).orElseThrow(() -> {
            LOGGER.error("Cannot start conclusion of Debate with id {} because it does not exist", id);
            return new DebateNotFoundException();
        });

        if (debate.getStatus() != DebateStatus.OPEN) {
            LOGGER.error("Cannot start conclusion of Debate with id {} because it is not open", id);
            throw new DebateClosedException();
        }

        debate.setStatus(DebateStatus.CLOSING);
    }

    @Transactional
    @Override
    public void deleteDebate(long id) {
        Debate debate = getDebateById(id).orElseThrow(() -> {
            LOGGER.error("Cannot delete Debate {} because it does not exist", id);
            return new DebateNotFoundException();
        });

        final Image image = debate.getImage();
        debate.deleteDebate();
        if (image != null) {
            imageService.deleteImage(image);
        }
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
    public List<Debate> getRecommendedDebates(long debateId, String username) {
        Debate debate = getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot get recommended debates for Debate with id {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });

        if (username != null) {
            User user = userService.getUserByUsername(username).orElseThrow(() -> {
                LOGGER.error("Cannot get recommended debates for Debate with id {} because user with username {} does not exist", debateId, username);
                return new UserNotFoundException();
            });
            return debateDao.getRecommendedDebates(debate, user);
        }

        return debateDao.getRecommendedDebates(debate);
    }
}
