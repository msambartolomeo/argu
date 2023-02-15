package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exceptions.StatusCodeException;
import ar.edu.itba.paw.webapp.dto.ExceptionErrorDto;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StatusCodeExceptionMapper implements ExceptionMapper<StatusCodeException> {

    @Override
    public Response toResponse(StatusCodeException exception) {
        final ExceptionErrorDto dto = ExceptionErrorDto.fromException(exception);
        return Response.status(exception.getStatusCode()).entity(dto).build();
    }
}