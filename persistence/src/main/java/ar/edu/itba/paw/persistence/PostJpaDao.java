package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        final Query query = em.createNativeQuery("SELECT COUNT(*) FROM posts2 WHERE debateid = :id");
        query.setParameter("id", debateId);

        Optional<?> queryResult = query.getResultList().stream().findFirst();
        return queryResult.map(o -> ((BigInteger) o).intValue()).orElse(0);
    }

    @Override
    public List<Post> getPostsByDebate(Debate debate, User user, int page) {
        final Query idQuery = em.createNativeQuery("SELECT postid FROM posts2 WHERE debateid = :debateid LIMIT 5 OFFSET :offset");
        idQuery.setParameter("debateid", debate.getDebateId());
        idQuery.setParameter("offset", page * 5);

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((BigInteger) o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Post> query = em.createQuery("FROM Post p WHERE p.postId IN :ids", Post.class);
        query.setParameter("ids", ids);

        final List<Post> posts = query.getResultList();

        final Query likeQuery = em.createNativeQuery("SELECT postid FROM likes2 WHERE userid = :userid AND postid = :postid");
        likeQuery.setParameter("userid", user.getUserId());

        for(Post post : posts) {
            likeQuery.setParameter("postid", post.getPostId());
            if(likeQuery.getResultList().size() == 1) post.setLikedByUser(true);
        }

        return posts;
    }

    @Override
    public Post create(User user, Debate debate, String content, Image image, ArgumentStatus status) {
        final Post post = new Post(user, debate, content, image, status);
        em.persist(post);
        return post;
    }

    @Override
    public Optional<Post> getLastArgument(Debate debate) {
        final TypedQuery<Post> query = em.createQuery("FROM Post p WHERE p.debate = :debate ORDER BY p.creationDate DESC ", Post.class);
        query.setParameter("debate", debate);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }
}
