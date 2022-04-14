package de.enflexit.awb.ws.restapi.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Variant;

import de.enflexit.awb.ws.restapi.gen.InfoApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;

/**
 * The Class InfoApiImpl.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class InfoApiImpl extends InfoApiService {


	@Override
	public Response infoGet(SecurityContext securityContext) throws NotFoundException {
		
		SystemInformation sysInfo = new SystemInformation();
		sysInfo.setId(42l);
		
		
		Variant variant = new Variant(MediaType.APPLICATION_JSON_TYPE, "de", "UTF-8");
		
		return Response.ok().variant(variant).entity(sysInfo).build();
	}

}
