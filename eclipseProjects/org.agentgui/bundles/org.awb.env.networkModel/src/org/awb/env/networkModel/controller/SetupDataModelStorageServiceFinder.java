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
package org.awb.env.networkModel.controller;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentFactory;

/**
 * The Class SetupDataModelStorageServiceFinder enables to search for environment types
 * that are provide by an OSGI declarative service.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class SetupDataModelStorageServiceFinder {

	/**
	 * Will evaluate the OSGI service for {@link SetupDataModelStorageService}s and return the services found.
	 * @return the list of {@link EnvironmentType}s found
	 */
	public static List<SetupDataModelStorageService> findSetupDataModelStorageServices() {
		return findSetupDataModelStorageServices(false);
	}
	/**
	 * Will evaluate the OSGI service for {@link SetupDataModelStorageService}s and return the services found.
	 * @param showSystemOutputIfNoServiceWasFound indicates to show system output if no service was found
	 * @return the list of {@link SetupDataModelStorageService}s found
	 */
	public static List<SetupDataModelStorageService> findSetupDataModelStorageServices(boolean showSystemOutputIfNoServiceWasFound) {
		
		List<SetupDataModelStorageService> sdmServiceList = new ArrayList<>();
		
		// ------------------------------------------------------------------------------
		// --- Check the current service references -------------------------------------
		// ------------------------------------------------------------------------------
		try {
			// --- Check for the ServiceReference ---------------------------------------
			BundleContext bundleContext = FrameworkUtil.getBundle(SetupDataModelStorageServiceFinder.class).getBundleContext();
			ServiceReference<?>[] serviceReferences = bundleContext.getServiceReferences(SetupDataModelStorageService.class.getName(), null);
			if (serviceReferences!=null) {
				for (int i = 0; i < serviceReferences.length; i++) {
					@SuppressWarnings("unchecked")
					ServiceReference<ComponentFactory<SetupDataModelStorageService>> serviceRef = (ServiceReference<ComponentFactory<SetupDataModelStorageService>>) serviceReferences[i];
					Object service = bundleContext.getService(serviceRef);
					if (service instanceof SetupDataModelStorageService) {
						SetupDataModelStorageService sdmService = (SetupDataModelStorageService) service;
						sdmServiceList.add(sdmService);
					}
				}
				
			} else {
				if (showSystemOutputIfNoServiceWasFound==true) {
					System.err.println("=> " + SetupDataModelStorageServiceFinder.class.getSimpleName() + ": Could not find any service for '" + SetupDataModelStorageService.class.getName() + "'.");
					System.err.println("   Ensure that the following bundles are configured in your start configuration:");
					System.err.println("   org.eclipse.core.runtime - Start Level=1 - Auto-Start=true");
					System.err.println("   org.apache.felix.scr     - Start Level=2 - Auto-Start=true");
				}
			}
			
		} catch (InvalidSyntaxException isEx) {
			isEx.printStackTrace();
		}
		return sdmServiceList;
	}
	
}
