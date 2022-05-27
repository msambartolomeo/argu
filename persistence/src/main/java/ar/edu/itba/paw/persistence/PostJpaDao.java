package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class PostJpaDao implements PostDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Post> getPostById(long postId) {
        return Optional.ofNullable(em.find(Post.class, postId));
    }

    @Override
    public int getPostsByDebateCount(long debateId) {
        final Query query = em.createNativeQuery("SELECT COUNT(*) FROM posts WHERE debateid = :id");
        query.setParameter("id", debateId);
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, long userId, int page) {
         /*final Query query = em.createNativeQuery("SELECT postid, debateid, username, content, likes, created_date, imageid, (SELECT COUNT(*) FROM likes WHERE userid = ? AND postid = public_posts.postid) as isliked, status FROM public_posts WHERE debateid = ? ORDER BY created_date LIMIT 5 OFFSET :offset");
        query.setParameter("offset", page * 5);
        List<PublicPostWithUserLike> = (List<PublicPostWithUserLike>) query.getResultList().stream().map(
                (postid, debateid, username, content, created_date, imageid, likes, status, isliked) ->
                        new PublicPostWithUserLike(
                                postid,
                                username,
                                debateid,
                                content,
                                likes,
                                (LocalDateTime) created_date,
                                imageid,
                                ArgumentStatus.getFromInt(((Number)status).intValue()),
                                (Boolean) isliked
        ).collect(Collectors.toList());*/
        return null;
    }

    @Override
    public Post create(User user, Debate debate, String content, Image image, ArgumentStatus status) {
        final Post post = new Post(user, debate, content, image, status);
        em.persist(post);
        return post;
    }

    @Override
    public List<PublicPost> getPublicPostsByDebate(long debateId, int page) {
        return null;
    }

    @Override
    public void likePost(long postId, long userId) {

    }

    @Override
    public void unlikePost(long postId, long userId) {

    }

    @Override
    public boolean hasLiked(long postId, long userId) {
        return false;
    }

    @Override
    public Optional<PublicPost> getLastArgument(long debateId) {
//        final TypedQuery<Post> query = em.createQuery("FROM Post AS p WHERE p.debate.debateid = :id ORDER BY p.created_date DESC LIMIT 1", Post.class);
//        query.setParameter("id", debateId);
//        return Optional.ofNullable(query.getSingleResult());
        return null;
    }
}
