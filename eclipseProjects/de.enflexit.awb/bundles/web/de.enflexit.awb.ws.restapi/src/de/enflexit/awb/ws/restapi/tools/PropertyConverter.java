package de.enflexit.awb.ws.restapi.tools;

/**
 * The Class PropertyConverter converts between AWB properties and REST properties.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyConverter {

	/**
	 * Converts AWB properties to web application properties as specified in this REST API.
	 *
	 * @param awbProps the AWB properties to convert
	 * @return the de.enflexit.awb.ws.restapi.gen.model. properties
	 */
	public static de.enflexit.awb.ws.restapi.gen.model.Properties toWebAppProperties(de.enflexit.common.properties.Properties awbProps) {
		
		de.enflexit.awb.ws.restapi.gen.model.Properties webAppProps = new de.enflexit.awb.ws.restapi.gen.model.Properties();
		
		// TODO
		
		return webAppProps;
	}
	
	/**
	 * Converts web application properties to AWB properties.
	 *
	 * @param awbProps the awb props
	 * @return the de.enflexit.common.properties. properties
	 */
	public static de.enflexit.common.properties.Properties toAwbProperties(de.enflexit.awb.ws.restapi.gen.model.Properties awbProps) {
		
		de.enflexit.common.properties.Properties webAppProps = new de.enflexit.common.properties.Properties();
		
		// TODO
		
		return webAppProps;
	}
	
	
}
