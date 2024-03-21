package de.enflexit.awb.ws.restapi.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.ExecutionStateApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.ExecutionState;

/**
 * The individual implementation class for the {@link StateApi}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExecutionStateApiImpl extends ExecutionStateApiService {

	@Override
	public Response executionStateGet(SecurityContext securityContext) throws NotFoundException {
		
		ExecutionState execState = new ExecutionState();
		
		// TODO
		
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(execState).build();
	}

}
