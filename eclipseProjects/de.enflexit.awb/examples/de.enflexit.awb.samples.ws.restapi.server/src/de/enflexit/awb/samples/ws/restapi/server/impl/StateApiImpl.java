package de.enflexit.awb.samples.ws.restapi.server.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.enflexit.awb.samples.ws.restapi.server.RestApiConfiguration;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;
import de.enflexit.awb.samples.ws.restapi.server.gen.StateApi;
import de.enflexit.awb.samples.ws.restapi.server.gen.StateApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.model.ExecutionState;

/**
 * The individual implementation class for the {@link StateApi}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class StateApiImpl extends StateApiService {

	@Override
	public Response stateGet(SecurityContext securityContext) throws NotFoundException {
		
		ExecutionState execState = new ExecutionState();
		
		
		// TODO
		
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(execState).build();
	}

}
