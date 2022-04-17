package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailService emailService;

    @Override
    public Optional<Post> getPostById(long id) {
        return postDao.getPostById(id);
    }

    @Override
    public Post create(long userId, long debateId, String content) {
        Post createdPost = postDao.create(userId, debateId, content);
        sendEmailToSubscribedUsers(debateId, userId);
        return createdPost;
    }

    private void sendEmailToSubscribedUsers(long debateId, long userId) {
        for (User user : userDao.getSuscribedUsersByDebate(debateId)) {
            if (user.getId() != userId) // Si no es el usuario que creo el post
                emailService.notifyNewPost(user.getEmail());
        }
    }

    @Override
    public Post createWithEmail(String userEmail, long debateId, String content) {
        Optional<User> optionalUser = userDao.getUserByEmail(userEmail);
        User user = optionalUser.orElseGet(() -> userDao.create(userEmail));
        return create(user.getId(), debateId, content);
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
}