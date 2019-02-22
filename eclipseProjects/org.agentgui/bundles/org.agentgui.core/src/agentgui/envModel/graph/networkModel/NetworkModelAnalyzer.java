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
package agentgui.envModel.graph.networkModel;

import java.util.ArrayList;
import java.util.List;

import agentgui.envModel.graph.controller.GraphEnvironmentController;

/**
 * The Class NetworkModelAnalyzer does what the name promises.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkModelAnalyzer {

	private enum NetworkModelAnalyzerEvent {
		AnalysisStarted,
		AnalysisFinalized
	}
	
	
	private GraphEnvironmentController graphController;
	private NetworkModel networkModel;
	
	private boolean isExecuted;
	private boolean isInterruptAnalyzer;
	
	private List<NetworkModelAnalyzerListener> listener;
	
	
	/**
	 * Instantiates a new network model analyzer.
	 * @param graphController the graph controller
	 */
	public NetworkModelAnalyzer(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}

	/**
	 * Checks if is executed.
	 * @return true, if is executed
	 */
	public boolean isExecuted() {
		return isExecuted;
	}
	/**
	 * Sets the flag for the executed analysis.
	 * @param isExecuted the new executed
	 */
	protected void setExecuted(boolean isExecuted) {
		this.isExecuted = isExecuted;
	}
	
	/**
	 * Checks if is interrupt analyzer.
	 * @return true, if is interrupt analyzer
	 */
	protected boolean isInterruptAnalyzer() {
		return isInterruptAnalyzer;
	}
	
	/**
	 * Stops the NetworkModel analysis.
	 */
	public void stopAnalysis() {
		this.isInterruptAnalyzer = true;
	}
	/**
	 * Starts or restarts the NetworkModel analysis.
	 */
	public void reStartAnalysis() {
		
		// --- Stop a previous execution? -------
		while (this.isExecuted()==true) {
			this.stopAnalysis();
		}
		// --- Start the analysis thread --------
		this.startAnalysisThread();
	}
	/**
	 * Start analysis thread.
	 */
	private void startAnalysisThread() {
		
		Thread aThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// --- Set the executed flag to true ----------------
				NetworkModelAnalyzer.this.setExecuted(true);
				// --- Reset local NetworkModel ---------------------
				NetworkModelAnalyzer.this.resetLocalNetworkModel();
				// --- Inform listener ------------------------------
				NetworkModelAnalyzer.this.informListener(NetworkModelAnalyzerEvent.AnalysisStarted);
				
				// --- Do the actual analysis -----------------------
				NetworkModelAnalyzer.this.doAnalyze();
				
				// --- if done, set the executed flag to false ------
				NetworkModelAnalyzer.this.setExecuted(false);
				// --- Inform listener ------------------------------
				NetworkModelAnalyzer.this.informListener(NetworkModelAnalyzerEvent.AnalysisFinalized);

			}
		}, "NetworkModel-Analyzer");
		aThread.start();
	}
	
	/**
	 * Returns the current working Network Model.
	 * @return the network model
	 */
	private NetworkModel getNetworkModel() {
		if (networkModel==null) {
			networkModel = this.graphController.getNetworkModel(); 
		}
		return networkModel;
	}
	private void resetLocalNetworkModel() {
		this.networkModel = null;
	}
	
	/**
	 * Do analyze.
	 */
	private void doAnalyze() {
		
		int noNetComps = this.getNetworkModel().getNetworkComponents().size();
		
		
		
		
	}
	
	// --------------------------------------------------------------
	// --- From here methods for listeners are located --------------
	// --------------------------------------------------------------
	/**
	 * Returns the listener of the Analyzer.
	 * @return the listener
	 */
	private List<NetworkModelAnalyzerListener> getListener() {
		if (listener==null) {
			listener = new ArrayList<>();
		}
		return listener;
	}
	/**
	 * Adds the network model analyzer listener.
	 * @param newListener the new listener
	 */
	public void addNetworkModelAnalyzerListener(NetworkModelAnalyzerListener newListener) {
		if (this.getListener().contains(newListener)==false) {
			this.getListener().add(newListener);
		}
	}
	/**
	 * Removes the network model analyzer listener.
	 * @param newListener the new listener
	 */
	public void removeNetworkModelAnalyzerListener(NetworkModelAnalyzerListener newListener) {
		this.getListener().remove(newListener);
	}
	/**
	 * Inform listener about specific events.
	 * @param event the NetworkModelAnalyzerEvent to inform about
	 */
	private void informListener(NetworkModelAnalyzerEvent event) {
		for (int i = 0; i < this.getListener().size(); i++) {
			NetworkModelAnalyzerListener listener = this.getListener().get(i);
			switch (event) {
			case AnalysisStarted:
				listener.onNetworkModelAnalysisStarted();
				break;

			case AnalysisFinalized:
				listener.onNetworkModelAnalysisFinalized();
				break;
			}
		}
	}

	
}
