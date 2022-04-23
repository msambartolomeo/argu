package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    public Optional<Image> getImage(long id) {
        return imageDao.getImage(id);
    }

    @Override
    public long createImage(byte[] data) {
        return imageDao.createImage(data);
    }

    @Override
    public void deleteImage(long id) {
        imageDao.deleteImage(id);
    }
}
