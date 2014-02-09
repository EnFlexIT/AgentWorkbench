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
package agentgui.core.agents.behaviour;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import agentgui.core.agents.UtilityAgent;
import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.ExecutionMode;

/**
 * This behaviour will be used by the UtilityAgent, if the application is 
 * executed in {@link ExecutionMode#DEVICE_SYSTEM} modus and if this modus 
 * is selected as single agent tool {@link DeviceSystemExecutionMode#AGENT}.
 * Starting this {@link TickerBehaviour} will create a file named
 * 'AgentGuiShutDownXXXX.txt' in the Agent.GUI root directory, where XXXX
 * is the current . Deleting this
 * file will shutdown the current Execution
 * 
 * @see UtilityAgent
 * @see ExecutionMode#DEVICE_SYSTEM
 * @see DeviceSystemExecutionMode#AGENT
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ExitDeviceExecutionBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -5888248028098540315L;

	private final String ShutDownFileNameTemplate = "AgentGuiShutDownXXXX.txt";
	private File shutDownFile;
	
	/**
	 * Instantiates a new exit device execution behaviour.
	 *
	 * @param agent the agent
	 * @param period the period
	 */
	public ExitDeviceExecutionBehaviour(Agent agent, long period) {
		super(agent, period);
		this.createShutDownFile();
	}

	@Override
	protected void onTick() {
		// --- Check if the file is still there. ----------  
		// --- If not, terminate the whole platform -------
		if (this.shutDownFile.exists()==false) {
			new ShutDown().start();
			this.stop();
			myAgent.doDelete();
		}
	}

	/**
	 * Creates the shutdown file.
	 */
	private void createShutDownFile() {
		
		// --- Determine the current JADE port ------------
		String platformName = myAgent.getContainerController().getPlatformName();
		int cut1 = platformName.indexOf(":") + 1;
		int cut2 = platformName.indexOf("/", cut1);
		String portNoString = platformName.substring(cut1, cut2);
		
		// --- Configure file location and name -----------
		String agentGuiRoot = Application.getGlobalInfo().PathBaseDir();
		String shutDownFile = ShutDownFileNameTemplate.replace("XXXX", portNoString);
		this.shutDownFile = new File(agentGuiRoot + shutDownFile);
		
		// --- Write the shutdown text file ---------------
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(this.shutDownFile));
			writer.write("=> Just delete this file in order to stop the execution of Agent.GUI! <=");
	        
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
		
	}
	
	/**
	 * The private class ShutDown is just a small simple 
	 * Thread that shuts down the current application.
	 */
	private class ShutDown extends Thread {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			Application.quit();
		}
	}
	
}
