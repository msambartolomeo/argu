package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    public Optional<Image> getImage(long id) {
        return imageDao.getImage(id);
    }

    @Transactional
    @Override
    public Image createImage(byte[] data) {
        return imageDao.createImage(data);
    }

    @Transactional
    @Override
    public void deleteImage(long id) {
        imageDao.deleteImage(id);
    }
}
