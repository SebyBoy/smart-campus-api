package com.sebastian.smartcampus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                "An unexpected internal server error occurred",
                500,
                "Please contact the API administrator"
        );

        return Response.status(500)
                .entity(errorMessage)
                .build();
    }
}