package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.SubscribedDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.SubscribedService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Subscribed;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserAlreadySubscribedException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SubscribedServiceImpl implements SubscribedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribedServiceImpl.class);
    @Autowired
    SubscribedDao subscribedDao;
    @Autowired
    UserService userService;
    @Autowired
    DebateService debateService;

    @Override
    @Transactional
    public Subscribed subscribeToDebate(String username, long debateId) {
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot subscribe to debate {} because user {} does not exist", debateId, username);
            return new UserNotFoundException();
        });
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot subscribe to debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        if (subscribedDao.getSubscribed(user, debate).isPresent()) {
            LOGGER.error("Cannot subscribe to debate {} because user {} already subscribed", debateId, username);
            throw new UserAlreadySubscribedException();
        }
        User creator = debate.getCreator();
        User opponent = debate.getOpponent();
        if (!user.equals(creator) && !user.equals(opponent)) {
            creator.addSubPoints();
            opponent.addSubPoints();
        }
        return subscribedDao.subscribeToDebate(user, debate);
    }

    @Override
    @Transactional
    public void unsubscribeToDebate(String username, long debateId) {
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot unsubscribe to debate {} because user {} does not exist", debateId, username);
            return new UserNotFoundException();
        });
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot unsubscribe to debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        subscribedDao.getSubscribed(user, debate).ifPresent(s -> {
            User creator = debate.getCreator();
            User opponent = debate.getOpponent();
            if (!user.equals(creator) && !user.equals(opponent)) {
                creator.removeSubPoints();
                opponent.removeSubPoints();
            }
            subscribedDao.unsubscribe(s);
        });
    }

    @Override
    public boolean isUserSubscribed(String username, long debateId) {
        return getSubscribed(username, debateId).isPresent();
    }

    @Override
    public Optional<Subscribed> getSubscribed(String username, long debateId) {
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot get subscription to debate {} because user {} does not exist", debateId, username);
            return new UserNotFoundException();
        });
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot get subscription to debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        return subscribedDao.getSubscribed(user, debate);
    }
}
