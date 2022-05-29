package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenPostException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    @Autowired
    private LikeService likeService;

    @Transactional
    @Override
    public Post create(String username, long debateId, String content, byte[] image) {
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        if (debate.getStatus() == DebateStatus.CLOSED || debate.getStatus() == DebateStatus.DELETED || (!debate.getCreator().getUsername().equals(username) && !debate.getOpponent().getUsername().equals(username))) {
            throw new ForbiddenPostException();
        }

        ArgumentStatus status = getArgumentStatus(debateId, debate.getStatus(), debate.getCreator().getUsername(), username);

        Post createdPost;
        if (image.length == 0) {
            createdPost = postDao.create(user, debate, content,null, status);
        } else {
            Image newImage = imageService.createImage(image);
            createdPost = postDao.create(user, debate, content, newImage, status);
        }
        sendEmailToSubscribedUsers(debate, user);
        return createdPost;
    }

    // Package-private for testing
    ArgumentStatus getArgumentStatus(long debateId, DebateStatus debateStatus, String creatorUsername, String username) {
        Optional<Post> lastArgument = getLastArgument(debateId);

        if (!lastArgument.isPresent()) {
            if (!creatorUsername.equals(username))
                throw new ForbiddenPostException();
            return ArgumentStatus.INTRODUCTION;
        } else {
            if (username.equals(lastArgument.get().getUser().getUsername()))
                throw new ForbiddenPostException();

            switch (lastArgument.get().getStatus()) {
                case INTRODUCTION:
                    if (!creatorUsername.equals(username))
                        return ArgumentStatus.INTRODUCTION;
                    else
                        return ArgumentStatus.ARGUMENT;
                case ARGUMENT:
                    if (debateStatus != DebateStatus.CLOSING)
                        return ArgumentStatus.ARGUMENT;
                    else
                        return ArgumentStatus.CONCLUSION;
                case CONCLUSION:
                    //debateService.closeDebate(debateId);
                    // TODO: close debate with model
                    return ArgumentStatus.CONCLUSION;
                default:
                    throw new ForbiddenPostException();
            }
        }
    }

    // Package-private for testing
    @Async // TODO: test that it works
    void sendEmailToSubscribedUsers(Debate debate, User user) {
        for (User u : debate.getSubscribedUsers()) {
            if (u.getUserId().equals(user.getUserId())) { // Si no es el usuario que creo el post
                emailService.notifyNewPost(u.getEmail(), user.getUsername(), debate.getDebateId(), debate.getName());
            }
        }
    }

    @Override
    public Optional<Post> getPostById(long postId) {
        return postDao.getPostById(postId);
    }

    @Override
    public int getPostsByDebatePageCount(long debateId) {
        return (int) Math.ceil(postDao.getPostsByDebateCount(debateId) / (double) PAGE_SIZE);
    }

    @Override
    public List<Post> getPostsByDebate(long debateId, String username, int page) {
        if (page < 0)
            return Collections.emptyList();
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        List<Post> posts = postDao.getPostsByDebate(debate, page);

        if (username != null) {
            User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
            for (Post post : posts) {
                post.setLikedByUser(likeService.isLiked(user, post));
            }
        }

        return posts;
    }

    @Override
    public Optional<Post> getLastArgument(long debateId) {
        final Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        return postDao.getLastArgument(debate);
    }
}