package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenPostException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private static final int PAGE_SIZE = 5;
    @Autowired
    private PostDao postDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private DebateService debateService;

    @Override
    public Optional<Post> getPostById(long id) {
        return postDao.getPostById(id);
    }

    @Transactional
    @Override
    public Post create(String username, long debateId, String content, byte[] image) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);

        if (debate.getDebateStatus() == DebateStatus.CLOSED || debate.getDebateStatus() == DebateStatus.DELETED || (debate.getCreatorId() != user.getUserId() && debate.getOpponentId() != user.getUserId())) {
            throw new ForbiddenPostException();
        }

        Optional<PublicPost> lastArgument = getLastArgument(debateId);
        ArgumentStatus status;

        if (!lastArgument.isPresent()) {
            if (user.getUserId() != debate.getCreatorId())
                throw new ForbiddenPostException();
            status = ArgumentStatus.INTRODUCTION;
        } else {
            if (user.getUsername().equals(lastArgument.get().getUsername()))
                throw new ForbiddenPostException();

            switch (lastArgument.get().getStatus()) {
                case INTRODUCTION:
                    if (user.getUserId() != debate.getCreatorId())
                        status = ArgumentStatus.INTRODUCTION;
                    else
                        status = ArgumentStatus.ARGUMENT;
                    break;
                case ARGUMENT:
                    if (debate.getDebateStatus() != DebateStatus.CLOSING)
                        status = ArgumentStatus.ARGUMENT;
                    else
                        status = ArgumentStatus.CONCLUSION;
                    break;
                case CONCLUSION:
                    status = ArgumentStatus.CONCLUSION;
                    // TODO: change debate status to CLOSED
                    break;
                default:
                    throw new ForbiddenPostException();
            }
        }

        Post createdPost;
        if (image.length == 0) {
            createdPost = postDao.create(user.getUserId(), debateId, content,null, status);
        } else {
            long imageId = imageService.createImage(image);
            createdPost = postDao.create(user.getUserId(), debateId, content, imageId, status);
        }
        sendEmailToSubscribedUsers(debateId, user.getUserId(), user.getUsername(), debate.getName());
        return createdPost;
    }

    private void sendEmailToSubscribedUsers(long debateId, long userId, String fromUsername, String debateName) {
        for (User user : userService.getSubscribedUsersByDebate(debateId)) {
            if (user.getUserId() != userId) // Si no es el usuario que creo el post
                emailService.notifyNewPost(user.getEmail(), fromUsername, debateId, debateName);
        }
    }

    @Override
    public Optional<PublicPost> getPublicPostById(long id) {
        return postDao.getPublicPostById(id);
    }

    @Override
    public List<PublicPost> getPublicPostsByDebate(long debateId, int page) {
        return postDao.getPublicPostsByDebate(debateId, page);
    }

    @Override
    public List<Post> getPostsByDebate(long debateId, int page) {
        return postDao.getPostsByDebate(debateId, page);
    }

    @Override
    public int getPostsByDebateCount(long debateId) {
        return (int) Math.ceil(postDao.getPostsByDebateCount(debateId) / (double) PAGE_SIZE);
    }

    @Transactional
    @Override
    public void likePost(long postId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        postDao.likePost(postId, user.getUserId());
    }

    @Transactional
    @Override
    public void unlikePost(long postId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        postDao.unlikePost(postId, user.getUserId());
    }

    @Override
    public boolean hasLiked(long postId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        return postDao.hasLiked(postId, user.getUserId());
    }

    @Override
    public List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, String username, int page) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        return postDao.getPublicPostsByDebateWithIsLiked(debateId, user.getUserId(), page);
    }

    @Override
    public Optional<PublicPost> getLastArgument(long debateIdNum) {
        return postDao.getLastArgument(debateIdNum);
    }
}