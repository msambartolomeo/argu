package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ArgumentDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenArgumentException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ArgumentServiceImpl implements ArgumentService {
    private static final int PAGE_SIZE = 5;
    @Autowired
    private ArgumentDao argumentDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private DebateService debateService;
    @Autowired
    private LikeService likeService;

    @Transactional
    @Override
    public Argument create(String username, long debateId, String content, byte[] image) {
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        if (debate.getStatus() == DebateStatus.CLOSED || debate.getStatus() == DebateStatus.DELETED || (!debate.getCreator().getUsername().equals(username) && !debate.getOpponent().getUsername().equals(username))) {
            throw new ForbiddenArgumentException();
        }

        ArgumentStatus status = getArgumentStatus(debate, user);

        Argument createdArgument;
        if (image.length == 0) {
            createdArgument = argumentDao.create(user, debate, content,null, status);
        } else {
            Image newImage = imageService.createImage(image);
            createdArgument = argumentDao.create(user, debate, content, newImage, status);
        }
        sendEmailToSubscribedUsers(debate, user);
        return createdArgument;
    }

    // Package-private for testing
    ArgumentStatus getArgumentStatus(Debate debate, User user) {
        Optional<Argument> lastArgument = getLastArgument(debate.getDebateId());

        final String creatorUsername = debate.getCreator().getUsername();
        final String username = user.getUsername();

        if (!lastArgument.isPresent()) {
            if (!creatorUsername.equals(username))
                throw new ForbiddenArgumentException();
            return ArgumentStatus.INTRODUCTION;
        } else {
            if (username.equals(lastArgument.get().getUser().getUsername()))
                throw new ForbiddenArgumentException();

            switch (lastArgument.get().getStatus()) {
                case INTRODUCTION:
                    if (!creatorUsername.equals(username))
                        return ArgumentStatus.INTRODUCTION;
                    else
                        return ArgumentStatus.ARGUMENT;
                case ARGUMENT:
                    if (debate.getStatus() != DebateStatus.CLOSING)
                        return ArgumentStatus.ARGUMENT;
                    else
                        return ArgumentStatus.CONCLUSION;
                case CONCLUSION:
                    debate.setStatus(DebateStatus.CLOSED);
                    return ArgumentStatus.CONCLUSION;
                default:
                    throw new ForbiddenArgumentException();
            }
        }
    }

    // Package-private for testing
    @Async // TODO: test that it works
    void sendEmailToSubscribedUsers(Debate debate, User user) {
        for (User u : debate.getSubscribedUsers()) {
            if (u.getUserId().equals(user.getUserId())) { // Si no es el usuario que creo el post
                emailService.notifyNewArgument(u.getEmail(), user.getUsername(), debate.getDebateId(), debate.getName());
            }
        }
    }

    @Override
    public Optional<Argument> getArgumentById(long argumentId) {
        return argumentDao.getArgumentById(argumentId);
    }

    @Override
    public int getArgumentByDebatePageCount(long debateId) {
        return (int) Math.ceil(argumentDao.getArgumentsByDebateCount(debateId) / (double) PAGE_SIZE);
    }

    @Override
    public List<Argument> getArgumentsByDebate(long debateId, String username, int page) {
        if (page < 0)
            return Collections.emptyList();
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        List<Argument> arguments = argumentDao.getArgumentsByDebate(debate, page);

        if (username != null) {
            User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
            for (Argument argument : arguments) {
                argument.setLikedByUser(likeService.isLiked(user, argument));
            }
        }

        return arguments;
    }

    @Override
    public Optional<Argument> getLastArgument(long debateId) {
        final Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        return argumentDao.getLastArgument(debate);
    }
}