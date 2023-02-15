package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.model.exceptions.EntityTooLargeImageException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageUtils {
    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 10;

    private static void checkError(FormDataBodyPart imageDetails) {
        List<String> length_headers = imageDetails.getHeaders().get("Content-Length");
        if (length_headers != null && !length_headers.isEmpty()) {
            int length;
            try {
                length = Integer.parseInt(length_headers.get(0));
            } catch (NumberFormatException e) {
                return;
            }
            if (length > 1024 * 1024 * 10) {
                throw new EntityTooLargeImageException();
            }
        }
    }

    public static byte[] getImage(FormDataBodyPart imageDetails, InputStream imageInput) throws IOException {
        if (imageInput == null) {
            return new byte[0];
        }
        checkError(imageDetails);
        try(BoundedInputStream boundedInputStream = new BoundedInputStream(imageInput, MAX_IMAGE_SIZE)) {
            return IOUtils.toByteArray(boundedInputStream);
        }
    }
}
