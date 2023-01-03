package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/images")
@Component
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Path("/{id}")
    @GET
    public Response getImage(@PathParam("id") long id) {
        Optional<Image> image = imageService.getImage(id);

        if (image.isPresent()) {
            return Response.ok(image.get().getData()).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
