package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ArgumentDao;
import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ArgumentServiceImpl implements ArgumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentServiceImpl.class);
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
    private LikeDao likeDao;

    @Transactional
    @Override
    public Argument create(String username, long debateId, String content, byte[] image) {
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot create new Argument because Debate {} does not exist", debateId);
            return new DebateNotFoundException();
        });
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot create new Argument on Debate {} because User {} does not exist", debateId, username);
            return new UserNotFoundException();
        });

        if (debate.getStatus() == DebateStatus.CLOSED || debate.getStatus() == DebateStatus.DELETED || debate.getStatus() == DebateStatus.VOTING) {
            LOGGER.error("Cannot create new Argument on Debate {} because it is closed", debateId);
            throw new DebateClosedException();
        }
        if (!debate.getCreator().getUsername().equals(username) && !debate.getOpponent().getUsername().equals(username)) {
            LOGGER.error("Cannot create new Argument on Debate {} because the requesting user {} is not the creator or the opponent", debateId, username);
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
    @Transactional
    ArgumentStatus getArgumentStatus(Debate debate, User user) {
        Optional<Argument> lastArgument = argumentDao.getLastArgument(debate);

        final String creatorUsername = debate.getCreator().getUsername();
        final String username = user.getUsername();

        if (!lastArgument.isPresent()) {
            if (!creatorUsername.equals(username)) {
                LOGGER.error("Cannot create new Argument on Debate {} because it is not the turn of the requesting user {}", debate.getName(), username);
                throw new ArgumentTurnException();
            }
            return ArgumentStatus.INTRODUCTION;
        } else {
            if (username.equals(lastArgument.get().getUser().getUsername())) {
                LOGGER.error("Cannot create new Argument on Debate {} because it is not the turn of the requesting user {}", debate.getName(), username);
                throw new ArgumentTurnException();
            }

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
                    debate.startVoting();
                    return ArgumentStatus.CONCLUSION;
                default:
                    LOGGER.error("Cannot create new Argument on Debate {} because it is not the turn of the requesting user {}", debate.getName(), username);
                    throw new ArgumentTurnException();
            }
        }
    }

    // Package-private for testing
    @Async
    void sendEmailToSubscribedUsers(Debate debate, User user) {
        if (debate.getSubscribedUsers() == null)
            return;
        for (User u : debate.getSubscribedUsers()) {
            if (!u.getUsername().equals(user.getUsername())) { // Si no es el usuario que creo el post
                emailService.notifyNewArgument(u.getEmail(), user.getUsername(), debate, u.getLocale());
            }
        }
    }

    @Override
    @Transactional
    public Optional<Argument> getArgumentById(long argumentId) {
        return argumentDao.getArgumentById(argumentId);
    }

    @Override
    public int getArgumentByDebatePageCount(long debateId, int size) {
        return (int) Math.ceil(argumentDao.getArgumentsByDebateCount(debateId) / (double) size);
    }

    @Override
    @Transactional
    public List<Argument> getArgumentsByDebate(long debateId, String username, int page, int size) {
        if (page < 0)
            return Collections.emptyList();
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot get Arguments for Debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        List<Argument> arguments = argumentDao.getArgumentsByDebate(debate, page, size);

        if (username != null) {
            User user = userService.getUserByUsername(username).orElseThrow(() -> {
                LOGGER.error("Cannot get Arguments for Debate {} because user {} does not exist", debateId, username);
                return new UserNotFoundException();
            });
            for (Argument argument : arguments) {
                argument.setLikedByUser(likeDao.getLike(user, argument).isPresent());
            }
        }

        return arguments;
    }

    @Override
    @Transactional
    public Optional<Argument> getLastArgument(long debateId) {
        final Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot get last Argument for Debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        return argumentDao.getLastArgument(debate);
    }

    @Override
    @Transactional
    public void deleteArgument(long argumentId, String username) {
        Argument argument = argumentDao.getArgumentById(argumentId).orElseThrow(() -> {
            LOGGER.error("Cannot delete argument {} because it does not exist", argumentId);
            return new ArgumentNotFoundException();
        });

        if (argument.getDeleted()) {
            LOGGER.error("Cannot delete argument {} because it is already deleted", argumentId);
            throw new ArgumentDeletedException();
        }

        if(argument.getUser().getUsername() == null || !argument.getUser().getUsername().equals(username)) {
            LOGGER.error("Cannot delete argument {} because user is not the creator or the opponent", argumentId);
            throw new ForbiddenArgumentException();
        }

        if(argument.getImage() != null)
            imageService.deleteImage(argument.getImage());

        argument.deleteArgument();
    }
}