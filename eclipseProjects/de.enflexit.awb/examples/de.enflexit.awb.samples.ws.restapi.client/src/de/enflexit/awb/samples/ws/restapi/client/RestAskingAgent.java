package de.enflexit.awb.samples.ws.restapi.client;

import de.enflexit.awb.samples.ws.restapi.client.gen.AdminsApi;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.ExecutionState;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.SystemInformation;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.SystemLoad;
import jade.core.Agent;

/**
 * The Class RestAskingAgent.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class RestAskingAgent extends Agent {

	private static final long serialVersionUID = -9754393650175834L;

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {

		try {

			AdminsApi api = new AdminsApi();
			
			SystemInformation sysInfo = api.infoGet();
			SystemLoad sysLoad = api.loadGet();
			ExecutionState execState =  api.stateGet();
			
			System.out.println("Info from agent " + this.getLocalName() + ": Nice ;-):");
			System.out.println(sysInfo);
			System.out.println(sysLoad);
			System.out.println(execState);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
	}
	
	
}
