package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.interfaces.services.LikeService;
import ar.edu.itba.paw.interfaces.services.ArgumentService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ArgumentNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserAlreadyLikedException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeServiceImpl.class);
    @Autowired
    private LikeDao likeDao;

    @Autowired
    private ArgumentService argumentService;

    @Autowired
    private UserService userService;

    @Override
    public Optional<Like> getLike(Argument argument, User user) {
        return likeDao.getLike(user, argument);
    }

    @Override
    @Transactional
    public void likeArgument(long argumentId, String username) {
        Argument argument = argumentService.getArgumentById(argumentId).orElseThrow(() -> {
            LOGGER.error("Cannot like argument {} because it does not exist", argumentId);
            return new ArgumentNotFoundException();
        });
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot like argument {} because user {} does not exist", argumentId, username);
            return new UserNotFoundException();
        });

        getLike(argument, user).ifPresent(l -> {
            LOGGER.error("Cannot like argument {} because user {} already liked it", argumentId, username);
            throw new UserAlreadyLikedException();
        });

        likeDao.likeArgument(user, argument);
    }

    @Override
    @Transactional
    public void unlikeArgument(long argumentId, String username) {
        Argument argument = argumentService.getArgumentById(argumentId).orElseThrow(() -> {
            LOGGER.error("Cannot unlike argument {} because it does not exist", argumentId);
            return new ArgumentNotFoundException();
        });
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot unlike argument {} because user {} does not exist", argumentId, username);
            return new UserNotFoundException();
        });

        likeDao.unlikeArgument(user, argument);
    }

    @Override
    public boolean isLiked(User user, Argument argument) {
        return getLike(argument, user).isPresent();
    }
}
