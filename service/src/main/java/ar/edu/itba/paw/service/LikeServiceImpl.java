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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

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
        Argument argument = argumentService.getArgumentById(argumentId).orElseThrow(ArgumentNotFoundException::new);
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);

        getLike(argument, user).ifPresent(l -> {throw new UserAlreadyLikedException();});

        likeDao.likeArgument(user, argument);
    }

    @Override
    @Transactional
    public void unlikeArgument(long argumentId, String username) {
        Argument argument = argumentService.getArgumentById(argumentId).orElseThrow(ArgumentNotFoundException::new);
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);

        likeDao.unlikeArgument(user, argument);
    }

    @Override
    public boolean isLiked(User user, Argument argument) {
        return getLike(argument, user).isPresent();
    }
}
