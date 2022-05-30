package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageDao {
    Optional<Image> getImage(long id);

    Image createImage(byte[] data);

    void deleteImage(Image image);
}
