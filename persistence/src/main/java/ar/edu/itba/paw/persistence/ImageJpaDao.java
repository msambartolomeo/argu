package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ImageJpaDao implements ImageDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Image> getImage(long id) {
        return Optional.ofNullable(em.find(Image.class, id));
    }

    @Override
    public Image createImage(byte[] data) {
        final Image image = new Image(data);
        em.persist(image);
        return image;
    }

    @Override
    public void deleteImage(long id) {
        em.remove(em.find(Image.class, id));
    }
}
