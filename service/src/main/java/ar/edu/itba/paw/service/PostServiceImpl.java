package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        sendEmailToSubscribedUsers(debateId, user.getUserId(), user.getUsername(), debate.getName());
        return createdPost;
    }

    // Package-private for testing
    ArgumentStatus getArgumentStatus(long debateId, DebateStatus debateStatus, String creatorUsername, String username) {
        Optional<PublicPost> lastArgument = getLastArgument(debateId);

        if (!lastArgument.isPresent()) {
            if (!creatorUsername.equals(username))
                throw new ForbiddenPostException();
            return ArgumentStatus.INTRODUCTION;
        } else {
            if (username.equals(lastArgument.get().getUsername()))
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
                    debateService.closeDebate(debateId);
                    return ArgumentStatus.CONCLUSION;
                default:
                    throw new ForbiddenPostException();
            }
        }
    }

    // Package-private for testing
    void sendEmailToSubscribedUsers(long debateId, long userId, String fromUsername, String debateName) {
        for (User user : userService.getSubscribedUsersByDebate(debateId)) {
            if (user.getUserId() != userId) { // Si no es el usuario que creo el post
                emailService.notifyNewPost(user.getEmail(), fromUsername, debateId, debateName);
            }
        }
    }

    @Override
    public Optional<Post> getPostById(long postId) {
        return postDao.getPostById(postId);
    }

    @Override
    public List<PublicPost> getPublicPostsByDebate(long debateId, int page) {
        if (page < 0)
            return new ArrayList<>();
        return postDao.getPublicPostsByDebate(debateId, page);
    }

    @Override
    public int getPostsByDebatePageCount(long debateId) {
        return (int) Math.ceil(postDao.getPostsByDebateCount(debateId) / (double) PAGE_SIZE);
    }

    @Transactional
    @Override
    public void likePost(long postId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Post post = getPostById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Like> like = likeService.getLike(post, user);

        if(like.isPresent()) throw new UserAlreadyLikedException();
        likeService.likePost(postId, user.getUserId());
    }

    @Transactional
    @Override
    public void unlikePost(long postId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        likeService.unlikePost(postId, user.getUserId());
    }

    @Override
    public List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, String username, int page) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        if (page < 0)
            return new ArrayList<>();
        return postDao.getPublicPostsByDebateWithIsLiked(debateId, user.getUserId(), page);
    }

    @Override
    public Optional<PublicPost> getLastArgument(long debateIdNum) {
        return postDao.getLastArgument(debateIdNum);
    }
}