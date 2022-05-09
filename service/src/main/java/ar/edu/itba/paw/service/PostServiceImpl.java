package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenPostException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    @Override
    public Post create(String username, long debateId, String content, byte[] image) {
        PublicDebate debate = debateService.getPublicDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        if (debate.getDebateStatus() != DebateStatus.OPEN || !debate.getCreatorUsername().equals(username) && !debate.getOpponentUsername().equals(username)) {
            throw new ForbiddenPostException();
        }

        Post createdPost;
        if (image.length == 0) {
            createdPost = postDao.create(user.getUserId(), debateId, content,null);
        } else {
            long imageId = imageService.createImage(image).getId();
            createdPost = postDao.create(user.getUserId(), debateId, content, imageId);
        }
        sendEmailToSubscribedUsers(debateId, user.getUserId());
        return createdPost;
    }

    private void sendEmailToSubscribedUsers(long debateId, long userId) {
        for (User user : userService.getSubscribedUsersByDebate(debateId)) {
            if (user.getUserId() != userId) // Si no es el usuario que creo el post
                emailService.notifyNewPost(user.getEmail());
        }
    }

    @Override
    public List<PublicPost> getPublicPostsByDebate(long debateId, int page) {
        return postDao.getPublicPostsByDebate(debateId, page);
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
}