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
package de.enflexit.awb.simulation.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.classLoadService.ClassLoadServiceUtility;
import de.enflexit.awb.core.jade.Platform;
import de.enflexit.awb.core.jade.PlatformStateInformation.PlatformState;
import de.enflexit.awb.core.project.DistributionSetup;
import de.enflexit.awb.core.simulation.balancing.StaticLoadBalancing;
import de.enflexit.awb.core.simulation.balancing.StaticLoadBalancingBase;
import de.enflexit.awb.core.ui.AwbMainWindow;
import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.SimulationServiceHelper;

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
public class LoadExecutionAgent extends Agent {

	private static final long serialVersionUID = 7768569966599564839L;

	/** The Constant BASE_ACTION_Start. */
	public final static int BASE_ACTION_Start = 0; 
	/** The Constant BASE_ACTION_Pause. */
	public final static int BASE_ACTION_Pause = 1;
	/** The Constant BASE_ACTION_Restart. */
	public final static int BASE_ACTION_Restart = 2;
	
	private int startArg;
	
	private AwbMainWindow mainWindow = Application.getMainWindow();
	private Platform platform = Application.getJadePlatform();
	
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		
		Object[] startArgs = getArguments();
		this.startArg = (Integer) startArgs[0];
		
		this.addBehaviour(new DoStartAction(this));
	}
	
	/**
	 * The Class DoStartAction.
	 */
	private class DoStartAction extends OneShotBehaviour {

		private static final long serialVersionUID = -559016842727286483L;

		public DoStartAction(Agent agent) {
			super(agent);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			
			// --- If the Action is a 'start'-action, it can be also a 'restart'-action -----
			if (startArg==BASE_ACTION_Start) {
				if (mainWindow!=null && mainWindow.isEnableSimStop()) {
					startArg = BASE_ACTION_Restart;
				}
			}
			
			// --- Do the wanted/necessary action -------------------------------------------
			switch (startArg) {
			case BASE_ACTION_Start:
				// --- PlatformState will be set in balancing class ! ---------
				StaticLoadBalancingBase staticBalancing = getStartAndStaticLoadBalancingClass(myAgent);
				if (staticBalancing!=null) {
					myAgent.addBehaviour(staticBalancing);	
					if (mainWindow!=null) {
						mainWindow.setEnableSimStart(false);
						mainWindow.setEnableSimPause(true);
						mainWindow.setEnableSimStop(true);
						mainWindow.enableSetupSelector(false);
					}
				} else {
					myAgent.doDelete();
				}
				break;

			case BASE_ACTION_Pause:
				LoadExecutionAgent.this.platform.setPlatformState(PlatformState.PausingMAS);
				setPauseSimulation(true);
				if (mainWindow!=null) {
					mainWindow.setEnableSimStart(true);
					mainWindow.setEnableSimPause(false);
					mainWindow.setEnableSimStop(true);
				}
				myAgent.doDelete();
				break;
				
			case BASE_ACTION_Restart:
				LoadExecutionAgent.this.platform.setPlatformState(PlatformState.RestartingMAS);
				setPauseSimulation(false);
				if (mainWindow!=null) {
					mainWindow.setEnableSimStart(false);
					mainWindow.setEnableSimPause(true);
					mainWindow.setEnableSimStop(true);
				}
				Application.getJadePlatform().setPlatformState(PlatformState.RunningMAS);
				myAgent.doDelete();
				break;
			}
		}
		
	}
	
	/**
	 * This method will pause or restart the current simulation.
	 * @param isPause true, if the MAS has to be paused
	 */
	private void setPauseSimulation(boolean isPause) {
		
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.setPauseSimulation(isPause);
		} catch (ServiceException ex) {
			ex.printStackTrace();
		}		
	}
	
	
	/**
	 * This method will initiate the one shot behaviour of the type {@link StaticLoadBalancingBase}
	 * in order to start the agency.
	 *
	 * @return the start and static load balancing instance
	 */
	private StaticLoadBalancingBase getStartAndStaticLoadBalancingClass(Agent myAgent) {
		
		// --- Get the current distribution setup ---------
		DistributionSetup currDisSetup = Application.getProjectFocused().getDistributionSetup();
		if (currDisSetup.isDoStaticLoadBalancing()==true) {

			try {
				return ClassLoadServiceUtility.getStaticLoadBalancing(currDisSetup.getStaticLoadBalancingClass(), myAgent);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		} else {
			return new StaticLoadBalancing(this);
		}
		return null;
	}
	
}
