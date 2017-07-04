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
package agentgui.simulationService.load.threading;

import javax.swing.SwingUtilities;

import agentgui.simulationService.LoadService;
import agentgui.simulationService.agents.LoadMeasureAgent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

/**
 * The Class ThreadMe-asureBehaviour executes the thread measurement on the 
 * distributed platform by using the {@link LoadService}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMeasureBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 7031695042584275556L;
	
	/** The load measure agent. */
	private LoadMeasureAgent loadMeasureAgent;
	
	/** The one shot behavior. */
	private boolean oneShotBehaviour;
	
	
	/**
	 * Instantiates a new thread measure behavior.
	 *
	 * @param loadMeasureAgent the load measure agent
	 * @param period the period
	 */
	public ThreadMeasureBehaviour(LoadMeasureAgent loadMeasureAgent, long period) {
		super(loadMeasureAgent, period);
		this.loadMeasureAgent = loadMeasureAgent;
	}
	
	/**
	 * Returns the instance of {@link LoadMeasureAgent}.
	 * @return the load measure agent
	 */
	private LoadMeasureAgent getLoadMeasureAgent() {
		return loadMeasureAgent;
	}
	
	/**
	 * Checks if this behavior is just used once.
	 * @return true, if is single shot behavior
	 */
	public boolean isOneShotBehaviour() {
		return oneShotBehaviour;
	}
	
	/**
	 * Sets if the measurement is just used once.
	 * @param oneShotBehaviour the new one shot behavior
	 */
	public void setOneShotBehaviour(boolean oneShotBehaviour) {
		this.oneShotBehaviour = oneShotBehaviour;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.TickerBehaviour#onTick()
	 */
	@Override
	protected void onTick() {
		// --- Do the measurement -------------------------
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() {
				doThreadMeasurement();
			}});
		
		// --- Exit, if this is a one shot behavior ------
		if (this.isOneShotBehaviour()==true) {
			this.stop();
		}
	}
	/**
	 * Executes the thread measurement.
	 */
	private void doThreadMeasurement() {
		try {
			this.getLoadMeasureAgent().getLoadServiceHelper().requestThreadMeasurements(this.getLoadMeasureAgent());
			
		} catch (ServiceException se) {
			se.printStackTrace();
		}
	}
}
