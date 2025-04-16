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
package org.agentgui.bundle.evaluation;

import java.util.List;
import java.util.Vector;

import de.enflexit.common.ServiceFinder;
import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;

/**
 * The Class BundleClassFilterCollector is used to collect and define AbstractBundleClassFilter 
 * for the search for classes within the application context bundles.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BundleClassFilterCollector {

	/**
	 * Collect and defines the set of bundle class filter for the application context
	 * by predefining the .
	 */
	public static void collectAndDefineSetOfBundleClassFilter() {
		
		BundleEvaluator be = BundleEvaluator.getInstance(); 
		
		// ----------------------------------------------------------
		// --- Add the locally known filter first ------------------- 
		// ----------------------------------------------------------
		be.addBundleClassFilter(new FilterForAgent(), false);
		be.addBundleClassFilter(new FilterForBaseService(), false);
		be.addBundleClassFilter(new FilterForOntology(), false);
		
		be.addBundleClassFilter(new FilterForProjectPlugIn(), false);
		be.addBundleClassFilter(new FilterForStaticLoadBalancing(), false);
		be.addBundleClassFilter(new FilterForDynamicLoadBalancing(), false);

		be.addBundleClassFilter(new FilterForTimeModel(), false);
	
		// ----------------------------------------------------------
		// --- Check for filter services ----------------------------
		// ----------------------------------------------------------
		List<BundleClassFilterService> filterServices = ServiceFinder.findServices(BundleClassFilterService.class);
		for (int i=0; i<filterServices.size(); i++) {
			
			BundleClassFilterService bcfService = filterServices.get(i);
			if (bcfService==null) continue;
			
			Vector<AbstractBundleClassFilter> filterVector = bcfService.getVectorOfBundleClassFilter();
			for (int j=0; j<filterVector.size(); j++) {
				AbstractBundleClassFilter filter = filterVector.get(j);
				if (filter!=null) {
					be.addBundleClassFilter(filter, false);			
				}
			}
		}
		
	}
	
}
