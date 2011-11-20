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
package gasmas.agents.manager;

import jade.core.ServiceException;
import agentgui.core.application.Application;
import agentgui.core.project.Project;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.GraphEnvironmentControllerGUI;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModelDiscrete;

public class NetworManagerAgent extends SimulationManagerAgent {

	private static final long serialVersionUID = 1823164338744218569L;
	
	private Project currProject = null;
	
	@Override
	protected void setup() {
		super.setup();
		
		currProject = Application.ProjectCurr;
		if (currProject==null) {
			takeDown();
		}
		this.addSimulationBehaviour();
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerInterface#doSingleSimulationSequennce()
	 */
	@Override
	public void doSingleSimulationSequennce() {
		
		try {
			simHelper.setEnvironmentModel(this.getEnvironmentModel());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerInterface#getInitialEnvironmentModel()
	 */
	@Override
	public EnvironmentModel getInitialEnvironmentModel() {

		int currentlyDoing = 0;
		final int GET_EnvContGUI = 1;
		final int GET_EnvCont = 2;
		final int GET_NetworkModel = 3;
		
		EnvironmentModel envModel = new EnvironmentModel();
		TimeModelDiscrete myTimeModel = new TimeModelDiscrete(new Long(1000*60));
		
		GraphEnvironmentControllerGUI envPanel = null;
		GraphEnvironmentController envController = null;
		NetworkModel networkModel = null;
		
		try {
			currentlyDoing = GET_EnvContGUI;
			envPanel = (GraphEnvironmentControllerGUI) currProject.getEnvironmentPanel();
			
			currentlyDoing = GET_EnvCont;
			envController = (GraphEnvironmentController) envPanel.getEnvironmentController();
			
			currentlyDoing = GET_NetworkModel;
			networkModel = (NetworkModel) envController.getEnvironmentModelCopy();
			
			envModel.setTimeModel(myTimeModel);
			envModel.setAbstractEnvironment(networkModel);
			
		} catch (Exception ex) {
			String msg = null;
			switch (currentlyDoing) {
			case GET_EnvContGUI:
				msg = ": Could not get GraphEnvironmentControllerGUI!";
				break;
			case GET_EnvCont:
				msg = ": Could not get GraphEnvironmentController!";
				break;
			case GET_NetworkModel:
				msg = ": Could not get NetworkModel!";
				break;
			}
			if (msg==null) {
				ex.printStackTrace();
			} else {
				System.err.println(this.getLocalName() + ": " + msg);
			}
			return null;
			
		}
		return envModel;
	}
	
}
