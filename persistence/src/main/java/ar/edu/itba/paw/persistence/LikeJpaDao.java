package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LikeDao;
import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.keys.UserPostKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class LikeJpaDao implements LikeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Like> getLike(User user, Argument argument) {
        return Optional.ofNullable(em.find(Like.class, new UserPostKey(user.getUserId(), argument.getArgumentId())));
    }

    @Override
    public Like likeArgument(User user, Argument argument) {
        final Like like = new Like(user, argument);
        em.persist(like);
        return like;
    }

    @Override
    public void unlikeArgument(User user, Argument argument){
        final Like toRemove = em.find(Like.class, new UserPostKey(user.getUserId(), argument.getArgumentId()));
        em.remove(toRemove);
    }

}
