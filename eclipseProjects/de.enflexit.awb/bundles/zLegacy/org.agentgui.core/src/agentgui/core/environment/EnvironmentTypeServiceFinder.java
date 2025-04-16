/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.environment;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;

/**
 * The Class EnvironmentTypeServiceFinder enables to search for environment types
 * that are provide by an OSGI declarative service.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class EnvironmentTypeServiceFinder {

	public static final String SERVICE_REFERENCE_FILTER = "(component.factory=org.awb.env.EnvironmentTypeService)";
	
	
	/**
	 * Will evaluate the OSGI service for {@link EnvironmentTypeService}s and return the result as list of {@link EnvironmentType}.
	 * @return the list of {@link EnvironmentType}s found
	 */
	public static List<EnvironmentType> findEnvironmentTypeServices() {
		return findEnvironmentTypeServices(false);
	}
	/**
	 * Will evaluate the OSGI service for {@link EnvironmentTypeService}s and return the result as list of {@link EnvironmentType}.
	 * @param showSystemOutputIfNoServiceWasFound indicates to show system output if no service was found
	 * @return the list of {@link EnvironmentType}s found
	 */
	public static List<EnvironmentType> findEnvironmentTypeServices(boolean showSystemOutputIfNoServiceWasFound) {
		
		List<EnvironmentType> envTypeList = new ArrayList<>();
		
		// ------------------------------------------------------------------------------
		// --- Check the current service references -------------------------------------
		// ------------------------------------------------------------------------------
		try {
			// --- Check for the ServiceReference ---------------------------------------
			BundleContext bundleContext = FrameworkUtil.getBundle(EnvironmentTypeServiceFinder.class).getBundleContext();
			ServiceReference<?>[] serviceReferences = bundleContext.getServiceReferences(ComponentFactory.class.getName(), SERVICE_REFERENCE_FILTER);
			if (serviceReferences!=null) {
				for (int i = 0; i < serviceReferences.length; i++) {
					@SuppressWarnings("unchecked")
					ServiceReference<ComponentFactory<EnvironmentTypeService>> serviceRef = (ServiceReference<ComponentFactory<EnvironmentTypeService>>) serviceReferences[i];
					ComponentFactory<EnvironmentTypeService> compFactory = (ComponentFactory<EnvironmentTypeService>) bundleContext.getService(serviceRef);
					ComponentInstance<EnvironmentTypeService> compInstance = compFactory.newInstance(null);
					// --- Create an instance of the service ----------------------------
					EnvironmentTypeService ets = compInstance.getInstance();
					envTypeList.add(ets.getEnvironmentType());
				}
				
			} else {
				if (showSystemOutputIfNoServiceWasFound==true) {
					System.err.println("=> " + EnvironmentTypeServiceFinder.class.getSimpleName() + ": Could not find any service for '" + SERVICE_REFERENCE_FILTER + "'.");
					System.err.println("   Ensure that the following bundles are configured in your start configuration:");
					System.err.println("   org.eclipse.core.runtime - Start Level=1 - Auto-Start=true");
					System.err.println("   org.apache.felix.scr     - Start Level=2 - Auto-Start=true");
				}
			}
			
		} catch (InvalidSyntaxException isEx) {
			isEx.printStackTrace();
		}
		return envTypeList;
	}
	
}
