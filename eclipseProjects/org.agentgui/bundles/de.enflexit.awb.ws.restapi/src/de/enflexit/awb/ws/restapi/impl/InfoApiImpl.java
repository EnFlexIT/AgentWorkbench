package de.enflexit.awb.ws.restapi.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.InfoApi;
import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;

/**
 * The individual implementation class for the {@link InfoApi}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class InfoApiImpl extends InfoApiService {


	@Override
	public Response infoGet(SecurityContext securityContext) throws NotFoundException {
		
		SystemInformation sysInfo = new SystemInformation();
		sysInfo.setId(42l);
		
		
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(sysInfo).build();
	}

}
