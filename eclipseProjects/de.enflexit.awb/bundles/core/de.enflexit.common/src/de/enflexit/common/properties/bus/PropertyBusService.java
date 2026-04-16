package de.enflexit.common.properties.bus;

import de.enflexit.common.properties.Properties;

/**
 * The Interface PropertyBusService describes the actions to be implemented, in order to enable a {@link Properties}-base
 * exchange of information to set or get specific configuration options. To differentiate application areas, the method
 * {@link #getPerformative()} should return a unique identifier.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface PropertyBusService {

	/**
	 * Has to return the performative of the current Service.
	 * @return the performative
	 */
	public String getPerformative();

	/**
	 * Returns the performative that is for sure lower case and not null.
	 * @return the performative not null
	 */
	public default String getPerformativeNotNull() {
		String perf = this.getPerformative();
		if (perf==null || perf.isBlank()==true) {
			perf = this.getClass().getSimpleName();
		}
		return perf.toLowerCase();
	}
	
	/**
	 * Sets the specified properties for the local performative.
	 *
	 * @param properties the properties to set
	 * @param arguments the optional arguments
	 * @return true, if successful
	 */
	public boolean setProperties(Properties properties, String arguments);
	
	/**
	 * Collects and returns the properties for the local performative.
	 *
	 * @param properties the properties that were already found for the performative (usually expected to be <code>null</code>)
	 * @param arguments the optional arguments
	 * @return the properties
	 */
	public Properties getProperties(Properties properties, String arguments);
	
}
