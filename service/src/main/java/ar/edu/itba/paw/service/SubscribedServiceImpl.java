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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscribedServiceImpl implements SubscribedService {

    @Autowired
    SubscribedDao subscribedDao;
    @Autowired
    UserService userService;
    @Autowired
    DebateService debateService;

    @Override
    public Subscribed subscribeToDebate(String username, long debateId) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        if (subscribedDao.getSubscribed(user, debate).isPresent()) {
            throw new UserAlreadySubscribedException();
        }
        return subscribedDao.subscribeToDebate(user, debate);
    }

    @Override
    public void unsubscribeToDebate(String username, long debateId) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        Optional<Subscribed> subscribed = subscribedDao.getSubscribed(user, debate);
        subscribed.ifPresent(s -> subscribedDao.unsubscribe(s));
    }
}
