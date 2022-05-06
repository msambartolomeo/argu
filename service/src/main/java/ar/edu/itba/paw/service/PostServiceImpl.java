package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DebateDao debateDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ImageService imageService;

    @Override
    public Optional<Post> getPostById(long id) {
        return postDao.getPostById(id);
    }

    @Transactional
    @Override
    public Post create(long userId, long debateId, String content, byte[] image) {
        Post createdPost;
        Debate debate = debateDao.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        if (debate.getDebateStatus() != DebateStatus.OPEN || (debate.getCreatorId() != userId && debate.getOpponentId() != userId))
            throw new ForbiddenPostException();
        if (image.length == 0) {
            createdPost = postDao.create(userId, debateId, content,null);
        } else {
            long imageId = imageService.createImage(image);
            createdPost = postDao.create(userId, debateId, content, imageId);
        }
        sendEmailToSubscribedUsers(debateId, userId);
        return createdPost;
    }

    private void sendEmailToSubscribedUsers(long debateId, long userId) {
        for (User user : userDao.getSubscribedUsersByDebate(debateId)) {
            if (user.getUserId() != userId) // Si no es el usuario que creo el post
                emailService.notifyNewPost(user.getEmail());
        }
    }

    // TODO: Deprecate this method
    @Override
    public Post createWithEmail(String userEmail, long debateId, String content) {
        Optional<User> optionalUser = userDao.getUserByEmail(userEmail);
        //TODO: Change method in users
        //User user = optionalUser.orElseGet(() -> userDao.create(userEmail));
        //return create(user.getId(), debateId, content);
        return null;
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
        return postDao.getPostsByDebateCount(debateId);
    }

    @Transactional
    @Override
    public void likePost(long postId, long userId) {
        postDao.likePost(postId, userId);
    }

    @Transactional
    @Override
    public void unlikePost(long postId, long userId) {
        postDao.unlikePost(postId, userId);
    }

    @Override
    public boolean hasLiked(long postId, long userId) {
        return postDao.hasLiked(postId, userId);
    }

    @Override
    public List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, long userId, int page) {
        return postDao.getPublicPostsByDebateWithIsLiked(debateId, userId, page);
    }
}