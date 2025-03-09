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
package de.enflexit.awb.samples.simulationService;

import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.SimulationServiceHelper;
import de.enflexit.awb.simulation.agents.SimulationAgent;

/**
 * The agent can be used in order to test the synchronisation of the time, which is provided by 
 * the {@link SimulationService}.<br> 
 * Just start one agent of this kind locally and the other on a remote container.<br>
 * In order to start a new remote container, set up the background system and start the agent 
 * {@link RemoteStarterAgent}.<br> 
 * The usage of this agent is described in its class.<br>
 * 
 * @see SimulationService
 * @see SimulationServiceHelper#getSynchTimeMillis()
 * @see SimulationServiceHelper#getSynchTimeDate()
 * @see SimulationServiceHelper#getSynchTimeDifferenceMillis()
 * @see RemoteStarterAgent
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ASynchTimeTestAgent extends SimulationAgent { 

	private static final long serialVersionUID = 1L;
	
	private String myName;
	private SynchTimeGUI gui;

	private Date curSynchDate;
	private long currSynchDiff = 0;

	
	
	/**
	 * Enables to access the SimulationService if available.
	 * @return the simulation service
	 */
	private SimulationServiceHelper getSimulationService() {
		SimulationServiceHelper simHelper = null;
		try {
			simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
		} catch (ServiceException se) {
			//se.printStackTrace();
			System.err.println("[" + this.getClass().getSimpleName() + "] No SimulationService available!");
		}
		return simHelper;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#setup()
	 */
	protected void setup() { 
		
		this.myName = this.getLocalName();
		
		SimulationServiceHelper simService = this.getSimulationService();
		if (simService!=null) {
			try {
				if (simService.getManagerAgent()==null) {
					// --- Register as managing agent -----
					simService.setManagerAgent(this.getAID());

				} else {
					// --- Wait for the Service-Trigger ---
					this.sensorPlugIn();
				}
				
			} catch (ServiceException se) {
				se.printStackTrace();
			}
		}
		
		// --- Start the Ticker Behaviour -----------------
		this.addBehaviour(new ShowTimeBehaviour(this,1000));
		// --- Start and show the GUI ---------------------
		this.startGUI();
		
	} 
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#takeDown()
	 */
	protected void takeDown() {
		super.takeDown();
		stopGUI();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#beforeMove()
	 */
	protected void beforeMove() {
		super.beforeMove();
		stopGUI();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#afterMove()
	 */
	protected void afterMove() {
		super.afterMove();
		
		this.startGUI();
		
		SimulationServiceHelper simService = this.getSimulationService();
		if (simService!=null) {
			try {
				if (simService.getManagerAgent()==null) {
					simService.setManagerAgent(this.getAID());
					// --- Start a Ticker -----------------------------
					this.addBehaviour(new ShowTimeBehaviour(this,1000));
				}
				
			} catch (ServiceException se) {
				se.printStackTrace();
			}
		}
		
	}
	
	/**
	 * This method starts the clock GUI, which will display the current and the synchronized time.
	 */
	private void startGUI(){
		// --- Open the UI ----------------------
		gui = new SynchTimeGUI(null);
		gui.setVisible(true);
		// --- Get time information -------------
		this.updateTimeInformation();
	}
	
	/**
	 * Will remove and destroy the clock GUI.
	 */
	private void stopGUI() {
		if (gui!=null){
			gui.setVisible(false);
			gui = null;
		}
	}

	/**
	 * Updates the local time information.
	 */
	private void updateTimeInformation() {
		this.updateTimeInformation(false);
	}
	/**
	 * Updates the local time information.
	 * @param stepSimulation the indicator to step the simulation and thus to notify other agents about progress
	 */
	private void updateTimeInformation(boolean stepSimulation) {
		
		// --- Set local variables ------------------------
		SimulationServiceHelper simService = this.getSimulationService();
		if (simService!=null) {
			try {
				curSynchDate = simService.getSynchTimeDate();
				currSynchDiff = simService.getSynchTimeDifferenceMillis();
				if (stepSimulation==true) {
					simService.stepSimulation(null, 0, true);
				}
				
			} catch (ServiceException se) {
				se.printStackTrace();
			}
			
		} else {
			curSynchDate = new Date(System.currentTimeMillis());
			currSynchDiff = 0;
		}
		
		// --- Update visualization ? ---------------------
		if (gui!=null) {
			gui.setTime(curSynchDate, currSynchDiff);	
		}
	}
	
	/**
	 * The ShowTimeBehaviour.
	 */
	class ShowTimeBehaviour extends TickerBehaviour { 

		private static final long serialVersionUID = 1L;
		
		/**
		 * Instantiates a new show time behaviour.
		 *
		 * @param a the agent instance
		 * @param period the ticker period
		 */
		public ShowTimeBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		protected void onTick() {
			ASynchTimeTestAgent.this.updateTimeInformation(true);
		} 

	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#onEnvironmentStimulus()
	 */
	@Override
	public void onEnvironmentStimulus() {
		this.updateTimeInformation();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.sensoring.ServiceSensorInterface#setPauseSimulation(boolean)
	 */
	@Override
	public void setPauseSimulation(boolean isPauseSimulation) {
		System.out.println("No pause action for " + this.getLocalName() + " specified!");
	}
	@Override
	public void setMigration(Location newLocation) {
		System.out.println("No migration action for " + this.getLocalName() + " specified!");
	}


	
	/**
	 * The Class SynchTimeGUI.
	 */
	public class SynchTimeGUI extends JDialog {

		private static final long serialVersionUID = 1L;
		
		private JPanel jContentPane = null;

		private JLabel jLabelTime = null;
		private JLabel jLabelTimeLocal = null;
		private JLabel jLabelTimeDiff = null;
		private JLabel jLabelTimeLocalCaption = null;
		private JLabel jLabelTimeCaption = null;
		private JLabel jLabelTimeDiffCaption = null;
		
		
		/**
		 * Sets the time.
		 *
		 * @param currSynchTime the curr synch time
		 * @param difference the difference
		 */
		public void setTime(Date currSynchTime, Long difference) {
			SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yy HH:mm:ss.S" );
			jLabelTimeLocal.setText(df.format(new Date(System.currentTimeMillis())));
			jLabelTime.setText(df.format(currSynchTime));
			jLabelTimeDiff.setText(difference.toString());
		}
		
		/**
		 * Instantiates a new synch time gui.
		 *
		 * @param owner the owner
		 */
		public SynchTimeGUI(Frame owner) {
			super(owner);
			initialize();
		}
		
		/**
		 * This method initializes this.
		 *
		 * @return void
		 */
		private void initialize() {
			this.setSize(397, 191);
			this.setTitle("'" + myName + "' - AgentGUI: Container-Synch-Time-Example");
			this.setContentPane(getJContentPane());
			this.setAlwaysOnTop(true);
		}
		
		/**
		 * This method initializes jContentPane.
		 *
		 * @return javax.swing.JPanel
		 */
		private JPanel getJContentPane() {
			if (jContentPane == null) {
				
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 1;
				gridBagConstraints1.gridy = 0;
				gridBagConstraints1.ipadx = 200;
				gridBagConstraints1.ipady = 0;
				gridBagConstraints1.insets = new Insets(20, 20, 0, 0);
				gridBagConstraints1.fill = GridBagConstraints.NONE;
				gridBagConstraints1.weightx = 1.0;
				gridBagConstraints1.anchor = GridBagConstraints.WEST;
				

				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.gridx = 0;
				gridBagConstraints2.gridy = 0;
				gridBagConstraints2.anchor = GridBagConstraints.WEST;
				gridBagConstraints2.insets = new Insets(20, 20, 0, 0);
				

				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.gridx = 0;
				gridBagConstraints3.gridy = 1;
				gridBagConstraints3.anchor = GridBagConstraints.WEST;
				gridBagConstraints3.insets = new Insets(20, 20, 00, 0);
				
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 1;
				gridBagConstraints.ipadx = 200;
				gridBagConstraints.ipady = 0;
				gridBagConstraints.insets = new Insets(20, 20, 0, 0);
				gridBagConstraints.anchor = GridBagConstraints.WEST;
				
				GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
				gridBagConstraints4.gridx = 1;
				gridBagConstraints4.gridy = 2;
				//gridBagConstraints4.ipadx = 200;
				//gridBagConstraints4.ipady = 0;
				gridBagConstraints4.insets = new Insets(20, 20, 20, 0);
				gridBagConstraints4.anchor = GridBagConstraints.WEST;
				
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.gridx = 0;
				gridBagConstraints5.gridy = 2;
				gridBagConstraints5.anchor = GridBagConstraints.WEST;
				gridBagConstraints5.insets = new Insets(20, 20, 20, 0);
				
				
				jLabelTimeCaption = new JLabel();
				jLabelTimeCaption.setText("Time synch:");
				jLabelTimeCaption.setFont(new Font("Dialog", Font.BOLD, 18));

				jLabelTime = new JLabel();
				jLabelTime.setText("...");
				jLabelTime.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelTime.setFont(new Font("Dialog", Font.BOLD, 18));

				jLabelTimeLocalCaption = new JLabel();
				jLabelTimeLocalCaption.setText("Time local:");
				jLabelTimeLocalCaption.setFont(new Font("Dialog", Font.BOLD, 18));
				
				jLabelTimeLocal = new JLabel();
				jLabelTimeLocal.setText("...");
				jLabelTimeLocal.setPreferredSize(new Dimension(15, 24));
				jLabelTimeLocal.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelTimeLocal.setFont(new Font("Dialog", Font.BOLD, 18));
				
				jLabelTimeDiffCaption = new JLabel();
				jLabelTimeDiffCaption.setText("Diff. [ms]:");
				jLabelTimeDiffCaption.setFont(new Font("Dialog", Font.BOLD, 14));
				
				jLabelTimeDiff = new JLabel();
				jLabelTimeDiff.setText("...");
				jLabelTimeDiff.setPreferredSize(new Dimension(15, 24));
				jLabelTimeDiff.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelTimeDiff.setFont(new Font("Dialog", Font.BOLD, 14));
				
				
				
				jContentPane = new JPanel();
				jContentPane.setFont(new Font("Dialog", Font.BOLD, 18));
				jContentPane.setLayout(new GridBagLayout());
				jContentPane.add(jLabelTime, gridBagConstraints);
				jContentPane.add(jLabelTimeCaption, gridBagConstraints3);
				jContentPane.add(jLabelTimeLocal, gridBagConstraints1);
				jContentPane.add(jLabelTimeLocalCaption, gridBagConstraints2);
				
				jContentPane.add(jLabelTimeDiffCaption, gridBagConstraints5);
				jContentPane.add(jLabelTimeDiff, gridBagConstraints4);
				
			}
			return jContentPane;
		}

	}


} 
