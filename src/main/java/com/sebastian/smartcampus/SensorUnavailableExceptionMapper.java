package com.sebastian.smartcampus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                403,
                "Sensor is in maintenance mode and cannot accept readings"
        );

        return Response.status(403)
                .entity(errorMessage)
                .build();
    }
}