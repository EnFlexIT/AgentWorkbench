package de.enflexit.awb.ws.dynSiteApi;

import java.util.HashMap;

import de.enflexit.awb.ws.dynSiteApi.impl.GetContentApiServiceImpl;
import de.enflexit.awb.ws.dynSiteApi.impl.SetContentApiServiceImpl;


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

		this.put("GetContentApi.implementation", GetContentApiServiceImpl.class.getName());
		this.put("SetContentApi.implementation", SetContentApiServiceImpl.class.getName());
		
	}
}
