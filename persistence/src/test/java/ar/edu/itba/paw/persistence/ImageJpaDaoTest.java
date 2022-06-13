package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJpaDaoTest {

    private final static long IMAGE_ID = 1;
    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ImageJpaDao imageJpaDao;

    @Test
    public void testCreateImage() {
        Image image = imageJpaDao.createImage(IMAGE_DATA);

        assertNotNull(image);
        assertEquals(image, em.find(Image.class, image.getId()));
    }

    @Test
    public void testGetImageDoesntExist() {
        Optional<Image> image = imageJpaDao.getImage(IMAGE_ID);

        assertFalse(image.isPresent());
    }

    @Test
    public void testGetImage() {
        Image image = new Image(IMAGE_DATA);
        em.persist(image);

        Optional<Image> i = imageJpaDao.getImage(image.getId());

        assertTrue(i.isPresent());
        assertArrayEquals(image.getData(), i.get().getData());
    }

    @Test
    public void testDeleteImage() {
        Image image = new Image(IMAGE_DATA);
        em.persist(image);

        imageJpaDao.deleteImage(image);

        assertNull(em.find(Image.class, IMAGE_ID));
    }
}
