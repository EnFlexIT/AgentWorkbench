package de.enflexit.awb.samples.ws.restapi.server;

import java.util.HashMap;

import de.enflexit.awb.samples.ws.restapi.server.impl.InfoApiImpl;
import de.enflexit.awb.samples.ws.restapi.server.impl.LoadApiImpl;
import de.enflexit.awb.samples.ws.restapi.server.impl.StateApiImpl;

/**
 * The Class ServletInitParameter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServletInitParameter extends HashMap<String, String>{

	private static final long serialVersionUID = -8419804672023761910L;

	/**
	 * Instantiates a new awb rest api init parameter.
	 */
	public ServletInitParameter() {
		
		this.put("InfoApi.implementation", InfoApiImpl.class.getName());
		this.put("LoadApi.implementation", LoadApiImpl.class.getName());
		this.put("StateApi.implementation", StateApiImpl.class.getName());
		
	}
}
