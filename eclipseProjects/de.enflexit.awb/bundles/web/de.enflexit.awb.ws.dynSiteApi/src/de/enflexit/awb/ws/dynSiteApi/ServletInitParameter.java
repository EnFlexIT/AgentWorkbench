package de.enflexit.awb.ws.dynSiteApi;

import java.util.HashMap;

import de.enflexit.awb.ws.dynSiteApi.impl.ContentApiServiceImpl;
import de.enflexit.awb.ws.dynSiteApi.impl.ContentElementApiServiceImpl;
import de.enflexit.awb.ws.dynSiteApi.impl.MenuApiServiceImpl;


/**
 * The Class ServletInitParameter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServletInitParameter extends HashMap<String, String>{

	private static final long serialVersionUID = -8419804672023761910L;

	/**
	 * Instantiates a new servlet init parameter.
	 */
	public ServletInitParameter() {

		this.put("ContentApi.implementation", ContentApiServiceImpl.class.getName());
		this.put("MenuApi.implementation", MenuApiServiceImpl.class.getName());
		this.put("ContentElementApi.implementation", ContentElementApiServiceImpl.class.getName());
		
	}
}
