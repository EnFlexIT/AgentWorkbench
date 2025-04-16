package de.enflexit.awb.ws.restapi;

import java.util.HashMap;

import de.enflexit.awb.ws.restapi.impl.InfoApiImpl;
import de.enflexit.awb.ws.restapi.impl.LoadApiImpl;
import de.enflexit.awb.ws.restapi.impl.ExecutionStateApiImpl;
import de.enflexit.awb.ws.restapi.impl.UserApiImpl;
import de.enflexit.awb.ws.restapi.impl.VersionApiImpl;

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
		
		this.put("UserApi.implementation", UserApiImpl.class.getName());
		this.put("InfoApi.implementation", InfoApiImpl.class.getName());
		this.put("LoadApi.implementation", LoadApiImpl.class.getName());
		this.put("ExecutionStateApi.implementation", ExecutionStateApiImpl.class.getName());
		this.put("VersionApi.implementation", VersionApiImpl.class.getName());
	}
}
