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
package agentgui.envModel.p2Dsvg.behaviours;

import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

/**
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 *
 */
public class TakePassiveObjectBehaviour extends OneShotBehaviour {
	
	
	private static final long serialVersionUID = 1L;
	private String objectID;
	
	public TakePassiveObjectBehaviour(String objectID){
		this.objectID = objectID;
	}

	@Override
	public void action() {
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) myAgent.getHelper(EnvironmentProviderService.SERVICE_NAME);
			// Get object from the EPS
			Physical2DObject object = helper.getObject(objectID);
			// Check type
			if(object instanceof PassiveObject){
				// take
				helper.assignPassiveObject(objectID, myAgent.getLocalName());
			}else{
				// Spaeter
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
