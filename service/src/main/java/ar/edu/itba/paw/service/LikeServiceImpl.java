package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.interfaces.services.LikeService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.PostNotFoundException;
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
    private PostService postService;

    @Autowired
    private UserService userService;

    @Override
    public Optional<Like> getLike(Post post, User user) {
        return likeDao.getLike(user, post);
    }

    @Override
    @Transactional
    public void likePost(long postId, long userId) {
        Post post = postService.getPostById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);

        likeDao.likePost(user, post);
    }

    @Override
    @Transactional
    public void unlikePost(long postId, long userId) {
        Post post = postService.getPostById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);

        likeDao.unlikePost(user, post);
    }
}
