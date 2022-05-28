package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.interfaces.services.LikeService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.PostNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserAlreadyLikedException;
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
    public void likePost(long postId, String username) {
        Post post = postService.getPostById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);

        getLike(post, user).ifPresent(l -> {throw new UserAlreadyLikedException();});

        likeDao.likePost(user, post);
    }

    @Override
    @Transactional
    public void unlikePost(long postId, String username) {
        Post post = postService.getPostById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);

        likeDao.unlikePost(user, post);
    }

    @Override
    public boolean isLiked(User user, Post post) {
        return getLike(post, user).isPresent();
    }
}
