package de.enflexit.awb.ws.restapi.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.ws.restapi.RestApiConfiguration;
import de.enflexit.awb.ws.restapi.gen.ExecutionStateApiService;
import de.enflexit.awb.ws.restapi.gen.NotFoundException;
import de.enflexit.awb.ws.restapi.gen.model.ExecutionState;
import de.enflexit.awb.ws.restapi.gen.model.ExecutionState.DeviceSystemExecutionModeEnum;
import de.enflexit.awb.ws.restapi.gen.model.ExecutionState.ExecutionModeEnum;

/**
 * The individual implementation class for the {@link ExecutionStateApiImpl}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-05-06T22:50:45.497388700+02:00[Europe/Berlin]")
public class ExecutionStateApiImpl extends ExecutionStateApiService {
	
    /* (non-Javadoc)
     * @see de.enflexit.awb.ws.restapi.gen.ExecutionStateApiService#executionStateGet(javax.ws.rs.core.SecurityContext)
     */
    @Override
    public Response executionStateGet(SecurityContext securityContext) throws NotFoundException {
		
		ExecutionState execState = new ExecutionState();
		// --- Set ExecutionMode to response ------------------------
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION: 
			execState.setExecutionMode(ExecutionModeEnum.APPLICATION);
			break;
		case DEVICE_SYSTEM:
			execState.setExecutionMode(ExecutionModeEnum.DEVICE_SYSTEM);
			// --- Set DeviceServiceExecutionMode to response -------
			switch (Application.getGlobalInfo().getDeviceServiceExecutionMode()) {
			case AGENT:
				execState.setDeviceSystemExecutionMode(DeviceSystemExecutionModeEnum.AGENT);
				break;
			case SETUP:
				execState.setDeviceSystemExecutionMode(DeviceSystemExecutionModeEnum.SETUP);
				break;
			}
			break;
		case SERVER:
			execState.setExecutionMode(ExecutionModeEnum.SERVER);
			break;
		case SERVER_MASTER:
			execState.setExecutionMode(ExecutionModeEnum.SERVER_MASTER);
			break;
		case SERVER_SLAVE:
			execState.setExecutionMode(ExecutionModeEnum.SERVER_SLAVE);
			break;
		}
		
		Project project = Application.getProjectFocused();
		if (project!=null) {
			// --- Set Project to response --------------------------
			execState.setProject(project.getProjectName());
			// --- Set Setup to response ----------------------------
			execState.setSetup(project.getSimulationSetupCurrent());
		}
		
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(execState).build();
	}

}
