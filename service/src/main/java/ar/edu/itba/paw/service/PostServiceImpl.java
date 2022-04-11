package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Post;
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

    @Override
    public Optional<Post> getPostById(int id) {
        return postDao.getPostById(id);
    }

    @Override
    public Post create(long userId, long debateId, String content) {
        // TODO: Ver el warning del get() y las excepciones
        User user;
        Debate debate = new DebateServiceImpl().getDebateById(debateId).orElseThrow(() -> new IllegalArgumentException("Debate not found"));
        EmailService emailService = new EmailServiceImpl();

        for (Post post : postDao.getAllPostByDebate(debateId)) {
            user = userDao.getUserById(post.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
            // TODO: Fijarse que no hagan Injection en el debate.getName();
            if (user.getId() != userId) // Si no es el usuario que creo el post
                emailService.notifyNewPost(user.getEmail());
        }
        return postDao.create(userId, debateId, content);
    }

    @Override
    public Post createWithEmail(String userEmail, long debateId, String content) {
        Optional<User> optionalUser = userDao.getUserByEmail(userEmail);
        User user;
        user = optionalUser.orElseGet(() -> userDao.create(userEmail));
        return postDao.create(user.getId(), debateId, content);
    }

    @Override
    public List<Post> getPostsByDebate(long debateId, int page) {
        return postDao.getPostsByDebate(debateId, page);
    }
}