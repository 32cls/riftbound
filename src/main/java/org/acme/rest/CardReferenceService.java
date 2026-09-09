package org.acme.rest;

import org.acme.dto.CardRequestDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;

@RegisterRestClient
public interface CardReferenceService {

	@GET
    CardRequestDto getBatchCards(@QueryParam("size") int size, @QueryParam("page") int page);

}
