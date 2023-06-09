package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.interfaces.services.ArgumentService;
import ar.edu.itba.paw.interfaces.services.LikeService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ArgumentDeletedException;
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
    public Like likeArgument(long argumentId, String username) {
        Argument argument = argumentService.getArgumentById(argumentId).orElseThrow(() -> {
            LOGGER.error("Cannot like argument {} because it does not exist", argumentId);
            return new ArgumentNotFoundException();
        });
        if(argument.getDeleted()) {
            LOGGER.error("Cannot like argument {} because it is deleted", argumentId);
            throw new ArgumentDeletedException();
        }
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot like argument {} because user {} does not exist", argumentId, username);
            return new UserNotFoundException();
        });

        getLike(argument, user).ifPresent(l -> {
            LOGGER.error("Cannot like argument {} because user {} already liked it", argumentId, username);
            throw new UserAlreadyLikedException();
        });

        User creator = argument.getUser();
        if(!creator.equals(user)) {
            creator.addLikePoints();
        }
        return likeDao.likeArgument(user, argument);
    }

    @Override
    @Transactional
    public boolean unlikeArgument(long argumentId, String username) {
        Argument argument = argumentService.getArgumentById(argumentId).orElseThrow(() -> {
            LOGGER.error("Cannot unlike argument {} because it does not exist", argumentId);
            return new ArgumentNotFoundException();
        });
        if(argument.getDeleted()) {
            LOGGER.error("Cannot unlike argument {} because it is deleted", argumentId);
            throw new ArgumentDeletedException();
        }
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot unlike argument {} because user {} does not exist", argumentId, username);
            return new UserNotFoundException();
        });

        final Optional<Like> like = likeDao.getLike(user, argument);
        if(like.isPresent()) {
            User creator = argument.getUser();
            if(!creator.equals(user)) {
                creator.removeLikePoints();
            }
            likeDao.unlikeArgument(like.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean isLiked(long argumentId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot unlike argument {} because user {} does not exist", argumentId, username);
            return new UserNotFoundException();
        });
        Argument argument = argumentService.getArgumentById(argumentId).orElseThrow(() -> {
            LOGGER.error("Cannot like argument {} because it does not exist", argumentId);
            return new ArgumentNotFoundException();
        });

        return isLiked(user, argument);
    }

    @Override
    public boolean isLiked(User user, Argument argument) {
        return getLike(argument, user).isPresent();
    }

}
