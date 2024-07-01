package de.enflexit.awb.ws.restapi.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import agentgui.core.application.Application;
import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.VersionApiService;
import de.enflexit.awb.ws.restapi.gen.model.Version;
import de.enflexit.common.VersionInfo;

/**
 * The Class VersionApiImpl.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class VersionApiImpl extends VersionApiService {

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.restapi.gen.VersionApiService#versionGet(javax.ws.rs.core.SecurityContext)
	 */
	@Override
	public Response versionGet(SecurityContext securityContext) throws NotFoundException {
		
		VersionInfo vInfo = Application.getGlobalInfo().getVersionInfo();
		Version version = new Version();
		if (vInfo!=null) {
			version.setMajor(vInfo.getVersionMajor());
			version.setMinor(vInfo.getVersionMinor());
			version.setMicro(vInfo.getVersionMicro());
			version.setQualifier(vInfo.getVersionQualifier());
		}
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(version).build();
	}

}
