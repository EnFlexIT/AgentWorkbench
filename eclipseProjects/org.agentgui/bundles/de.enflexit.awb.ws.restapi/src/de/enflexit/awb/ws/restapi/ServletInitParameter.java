package de.enflexit.awb.ws.restapi;

import java.util.HashMap;

import de.enflexit.awb.ws.restapi.impl.InfoApiImpl;

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
		
	}
}
