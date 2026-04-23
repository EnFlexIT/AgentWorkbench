package de.enflexit.awb.core.propertyBusServices;

import java.util.Vector;

import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.ApplicationPropertyBus;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceAgents can be used to get the names 
 * of all registered classes extending jade.core.Agent through the 
 * {@link ApplicationPropertyBus}.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceAgents implements PropertyBusService {

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "AWB.AGENTS";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		return false;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();
		// --- Get all registered classes extending jade.core.Agent -------------------------------
		AbstractBundleClassFilter agentFilter = BundleEvaluator.getInstance().getBundleClassFilterByClass(jade.core.Agent.class);
		Vector<String> agents = agentFilter.getClassesFound();

		// --- Set the results --------------------------------------------------------------------
		int agentCounter = 0;
		for (String agent : agents) {
			properties.setStringValue("agent["+ agentCounter + "].classname", agent);
			agentCounter++;
		}
		return properties;
	}
}