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
package agentgui.simulationService.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import agentgui.core.application.Application;
import agentgui.core.application.Project;
import agentgui.core.gui.CoreWindow;
import agentgui.core.sim.setup.DistributionSetup;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.balancing.StaticLoadBalancing;
import agentgui.simulationService.balancing.StaticLoadBalancingBase;

/**
 * This agent manages the start, pause, restart or stop of a simulation.
 * For the start of an agency, the agent will take the parameters set in
 * the current projects {@link DistributionSetup}, so that an eventually
 * set class of the type {@link StaticLoadBalancingBase} can be applied.
 * 
 * @see StaticLoadBalancingBase
 * @see DistributionSetup
 * @see Project
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SimStartAgent extends Agent {

	private static final long serialVersionUID = 7768569966599564839L;

	/** The Constant BASE_ACTION_Start. */
	public final static int BASE_ACTION_Start = 0; 
	/** The Constant BASE_ACTION_Pause. */
	public final static int BASE_ACTION_Pause = 1;
	/** The Constant BASE_ACTION_Restart. */
	public final static int BASE_ACTION_Restart = 2;
	/** The Constant BASE_ACTION_Stop. */
	public final static int BASE_ACTION_Stop = 3;
	
	private CoreWindow mainWindow = Application.MainWindow;
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		
		Object[] startArgs = getArguments();
		int startArg = (Integer) startArgs[0];
		
		// --- If the Action is a 'start'-action, it can be also a 'restart'-action -----
		if (startArg==BASE_ACTION_Start) {
			if (mainWindow.isEnabledSimStop()) {
				startArg = BASE_ACTION_Restart;
			}
		}
		
		// --- Do the wanted/necessary action -------------------------------------------
		switch (startArg) {
		case BASE_ACTION_Start:
			StaticLoadBalancingBase staticBalancing = this.getStartAndStaticLoadBalancingClass();
			if (staticBalancing!=null) {
				this.addBehaviour(staticBalancing);	
				mainWindow.setEnableSimStart(false);
				mainWindow.setEnableSimPause(true);
				mainWindow.setEnableSimStop(true);
			} else {
				this.doDelete();
			}
			break;

		case BASE_ACTION_Pause:
			this.setPauseSimulation(true);
			mainWindow.setEnableSimStart(true);
			mainWindow.setEnableSimPause(false);
			mainWindow.setEnableSimStop(true);
			this.doDelete();
			break;
		case BASE_ACTION_Restart:
			this.setPauseSimulation(false);
			mainWindow.setEnableSimStart(false);
			mainWindow.setEnableSimPause(true);
			mainWindow.setEnableSimStop(true);
			this.doDelete();
			break;
		case BASE_ACTION_Stop:
			//Application.JadePlatform.jadeStop();
			mainWindow.setEnableSimStart(true);
			mainWindow.setEnableSimPause(false);
			mainWindow.setEnableSimStop(false);
			this.doDelete();
			break;
		}
		
	}
	
	/**
	 * This method will pause or restart the current simulation.
	 *
	 * @param pause the pause simulation
	 */
	private void setPauseSimulation(boolean pause) {
		
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.setPauseSimulation(pause);
		} catch (ServiceException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * This method will initiate the one shot behaviour of the type {@link StaticLoadBalancingBase}
	 * in order to start the agency.
	 *
	 * @return the start and static load balancing instance
	 */
	private StaticLoadBalancingBase getStartAndStaticLoadBalancingClass() {
		
		// --- Get the current simulation setup -----------
		SimulationSetup currSimSetup = Application.ProjectCurr.simulationSetups.getCurrSimSetup();
		
		// --- Get the current distribution setup ---------
		DistributionSetup currDisSetup = currSimSetup.getDistributionSetup();
		if (currDisSetup.isDoStaticLoadBalancing()== true) {

			try {
				@SuppressWarnings("unchecked")
				Class<? extends StaticLoadBalancingBase> staticLoadBalancingClass = (Class<? extends StaticLoadBalancingBase>) Class.forName(currDisSetup.getStaticLoadBalancingClass());	
				return staticLoadBalancingClass.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		} else {
			return new StaticLoadBalancing();
		}
		return null;
	}
	
}
