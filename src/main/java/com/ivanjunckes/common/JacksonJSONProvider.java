package com.ivanjunckes.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JacksonJSONProvider extends com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider { 

    public JacksonJSONProvider() { 
        super();
        _mapperConfig.getDefaultMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

} 
