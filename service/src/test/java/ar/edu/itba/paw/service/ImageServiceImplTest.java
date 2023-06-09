package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Image;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    private final static long IMAGE_ID = 1;
    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    @InjectMocks
    private ImageServiceImpl imageService = new ImageServiceImpl();

    @Mock
    private ImageDao imageDao;

    @Test
    public void testGetImageEmpty() {
        when(imageDao.getImage(anyLong())).thenReturn(Optional.empty());

        Optional<Image> i = imageService.getImage(IMAGE_ID);

        assertFalse(i.isPresent());
    }

    @Test
    public void testGetImage() {
        Image image = new Image(IMAGE_DATA);
        when(imageDao.getImage(anyLong())).thenReturn(Optional.of(image));

        Optional<Image> i = imageService.getImage(IMAGE_ID);

        assertTrue(i.isPresent());
        assertEquals(image, i.get());
    }

    @Test
    public void testCreateImage() {
        Image image = new Image(IMAGE_DATA);
        when(imageDao.createImage(any(byte[].class))).thenReturn(image);

        Image i = imageService.createImage(IMAGE_DATA);

        assertEquals(image.getData(), i.getData());
    }
}
