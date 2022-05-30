package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {
    Optional<Image> getImage(long id);

    Image createImage(byte[] data);

    void deleteImage(Image image);
}
