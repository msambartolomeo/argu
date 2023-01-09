package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.webapp.utils.CacheResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("/images")
@Component
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Path("/{id}")
    @Produces("image/*")
    @GET
    public Response getImage(@PathParam("id") final long id, @Context Request request) {
        final Image image = imageService.getImage(id).orElseThrow(ImageNotFoundException::new);

        return CacheResponseBuilder.buildResponse(id, image.getData(), request);
    }
}
