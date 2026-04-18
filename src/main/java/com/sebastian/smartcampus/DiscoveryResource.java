package com.sebastian.smartcampus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getApiInfo() {
        return "{"
                + "\"name\":\"Smart Campus API\","
                + "\"version\":\"v1\","
                + "\"admin\":\"sebastian@westminster.ac.uk\","
                + "\"resources\":{"
                + "\"rooms\":\"/api/v1/rooms\","
                + "\"sensors\":\"/api/v1/sensors\""
                + "}"
                + "}";
    }
}