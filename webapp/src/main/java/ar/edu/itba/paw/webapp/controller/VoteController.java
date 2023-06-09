package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.VoteService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.exceptions.VoteNotFoundException;
import ar.edu.itba.paw.webapp.dto.VoteDto;
import ar.edu.itba.paw.webapp.form.VoteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Path("/debates/{debateId}/votes")
@Component
public class VoteController {
    @PathParam("debateId")
    private long debateId;

    @Autowired
    private VoteService voteService;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response vote(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url,
            @Valid @NotNull(message = "missing body") final VoteForm form
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        voteService.addVote(debateId, username, DebateVote.valueOf(form.getVote().toUpperCase()));

        return Response.created(null).build();
    }

    @DELETE
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response unvote(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        if (voteService.removeVote(debateId, username)) {
            return Response.noContent().build();
        }

        throw new VoteNotFoundException();
    }

    @GET
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response getVote(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        Optional<Vote> vote = voteService.getVote(debateId, username);
        if (vote.isPresent()) {
            return Response.ok(VoteDto.fromVote(vote.get())).build();
        }

        throw new VoteNotFoundException();
    }
}
