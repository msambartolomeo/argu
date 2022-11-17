package ar.edu.itba.paw.webapp.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageUtils {
    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 10;

    public static Response.Status checkError(FormDataBodyPart imageDetails) {
        List<String> length_headers = imageDetails.getHeaders().get("Content-Length");
        if (length_headers != null && !length_headers.isEmpty()) {
            int length;
            try {
                length = Integer.parseInt(length_headers.get(0));
            } catch (NumberFormatException e) {
                return null;
            }
            if (length > 1024 * 1024 * 10) {
                return Response.Status.REQUEST_ENTITY_TOO_LARGE;
            }
        }
        return null;
    }

    public static byte[] getImage(InputStream imageInput) throws IOException {
        try(BoundedInputStream boundedInputStream = new BoundedInputStream(imageInput, MAX_IMAGE_SIZE)) {
            return IOUtils.toByteArray(boundedInputStream);
        }
    }
}
