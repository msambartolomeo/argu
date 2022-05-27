package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.keys.UserPostKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class LikeJpaDao implements LikeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Like> getLike(User user, Post post) {
        return Optional.ofNullable(em.find(Like.class, new UserPostKey(user.getUserId(), post.getPostId())));
    }

    @Override
    public Set<Long> getUserPostsLikesInDebate(long userid, long debateid) {
        final Query query = em.createNativeQuery("SELECT likes.postid FROM posts JOIN likes ON posts.postid = likes.postid WHERE debateid = :debateid AND likes.userid = :userid");
        query.setParameter("debateid", debateid);
        query.setParameter("userid", userid);

        @SuppressWarnings("unchecked")
        Set<Long> ids = (Set<Long>) query.getResultList().stream().map(o -> ((Integer) o).longValue()).collect(Collectors.toSet());

        return ids;
    }

    @Override
    public void likePost(User user, Post post) {
        final Like like = new Like(user, post);
        em.persist(like);
    }

    @Override
    public void unlikePost(User user, Post post){
        final Like toRemove = em.find(Like.class, new UserPostKey(user.getUserId(), post.getPostId()));
        em.remove(toRemove);
    }

}
