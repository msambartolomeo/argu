package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exceptions.UserConflictException;
import ar.edu.itba.paw.webapp.dto.UserConflictExceptionDto;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserConflictExceptionMapper implements ExceptionMapper<UserConflictException> {

    @Override
    public Response toResponse(UserConflictException exception) {
        final UserConflictExceptionDto dto = UserConflictExceptionDto.fromException(exception);
        return Response.status(Response.Status.CONFLICT).entity(dto).build();
    }
}
