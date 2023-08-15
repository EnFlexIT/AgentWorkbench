package de.enflexit.awb.samples.ws.restapi.server.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import agentgui.simulationService.load.LoadMeasureThread;
import de.enflexit.awb.samples.ws.restapi.server.RestApiConfiguration;
import de.enflexit.awb.samples.ws.restapi.server.gen.LoadApi;
import de.enflexit.awb.samples.ws.restapi.server.gen.LoadApiService;
import de.enflexit.awb.samples.ws.restapi.server.gen.NotFoundException;
import de.enflexit.awb.samples.ws.restapi.server.gen.model.SystemLoad;

/**
 * The individual implementation class for the {@link LoadApi}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LoadApiImpl extends LoadApiService {

	@Override
	public Response loadGet(SecurityContext securityContext) throws NotFoundException {
				
		// --- Create return value -----------------------
		SystemLoad sysLoad = new SystemLoad();
		sysLoad.setCpuUsage(LoadMeasureThread.getLoadCPU());
		sysLoad.setMemUsage(LoadMeasureThread.getLoadRAM());
		sysLoad.setHeapUsage(LoadMeasureThread.getLoadMemoryJVM());

		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(sysLoad).build();
	}

}
