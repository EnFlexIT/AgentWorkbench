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
package org.awb.env.networkModel.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

/**
 * The Class NetworkModelAnalyzer does what the name promises.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkModelAnalyzer {

	private enum NetworkModelAnalyzerEvent {
		AnalysisStarted,
		AnalysisFinalized,
		AppendedMessage
	}
	
	public enum CountingType {
		DomainElement,
		NetworkComponent,
		GraphElementPrototype
	}

	private boolean isExecuted;
	private boolean isInterruptAnalyzer;
	
	private GraphEnvironmentController graphController;
	private List<NetworkModelAnalyzerListener> listener;

	private NetworkModel networkModel;
	private HashMap<ElementCounter, Integer> elementCountingHashMap;
	private List<String> notConnectedNetworkComponents;
	
	private List<String> resultList;
	
	
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
				NetworkModelAnalyzer.this.resetLocalDataModel();
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
	/**
	 * Resets the local data model.
	 */
	private void resetLocalDataModel() {
		this.networkModel = null;
		this.elementCountingHashMap = null;
		this.notConnectedNetworkComponents = null;
		this.resultList = null;
	}
	
	/**
	 * Do analyze.
	 */
	private void doAnalyze() {
		
		// --- Simply count Network Components ----------------------
		int noNetComps = this.getNetworkModel().getNetworkComponents().size();
		this.addResultMessage(noNetComps + " " + this.getStringNetworkComponentS(noNetComps) + " found in the current NetworkModel");

		// --- Count single elements -------------------------------- 
		this.doElementCounting();
		this.addResultMessagesForElementCounting();
		
	}
	
	/**
	 * Adds the result messages for the element counting.
	 */
	private void addResultMessagesForElementCounting() {
		
		GeneralGraphSettings4MAS graphSettings = this.getNetworkModel().getGeneralGraphSettings4MAS();
		
		// --- Print domain results ---------------------------------
		this.addResultMessage("Differentiated by Domain");
		List<String> domains = new ArrayList<>(graphSettings.getDomainSettings().keySet());
		for (int i = 0; i < domains.size(); i++) {
			String domain = domains.get(i);
			Integer noOfDomainElements = this.getElementCountingHashMap().get(new ElementCounter(CountingType.DomainElement, domain));
			if (noOfDomainElements!=null) {
				this.addResultMessage("- Domain '" + domain + "': Found " + noOfDomainElements + " " + this.getStringNetworkComponentS(noOfDomainElements));
			}
		}
		this.addResultMessage("");
		
		// --- Print NetworkComponent results -----------------------
		this.addResultMessage("Type of Network Components");
		List<String> ctsList = new ArrayList<>(graphSettings.getCurrentCTS().keySet());
		for (int i = 0; i < ctsList.size(); i++) {
			String componentType = ctsList.get(i);
			Integer noOfComponents = this.getElementCountingHashMap().get(new ElementCounter(CountingType.NetworkComponent, componentType));
			if (noOfComponents!=null) {
				this.addResultMessage("- " + noOfComponents + " x " + componentType + "");
			}
		}
		this.addResultMessage("");
		
		// --- Print NetworkComponent results -----------------------
		this.addResultMessage("Type of used GraphElementProtottypes");
		List<ElementCounter> geList = this.getElementCounterByType(CountingType.GraphElementPrototype);
		for (int i = 0; i < geList.size(); i++) {
			ElementCounter elementCounter = geList.get(i);
			Integer noOfGraphElementTypes = this.getElementCountingHashMap().get(elementCounter);
			this.addResultMessage("- " + noOfGraphElementTypes  + " x " + elementCounter.getElementID() + "");
		}
		this.addResultMessage("");
		
		// --- Print not connected NetworkComponents ----------------
		int noOfFreeNetComps = this.getNotConnectedNetworkComponentList().size(); 
		if (noOfFreeNetComps==0) {
			this.addResultMessage("All NetworkComponents are connected to other NetworkComponents!");
		} else {
			this.addResultMessage(noOfFreeNetComps + " single free " + this.getStringNetworkComponentS(noOfFreeNetComps) + " (not connected to other):");
			for (int i = 0; i < noOfFreeNetComps; i++) {
				this.addResultMessage("- " + this.getNotConnectedNetworkComponentList().get(i));
			}
		}
		this.addResultMessage("");
		
	}
	
	/**
	 * Do element counting.
	 */
	private void doElementCounting() {
		
		GeneralGraphSettings4MAS graphSettings = this.getNetworkModel().getGeneralGraphSettings4MAS(); 
		
		List<NetworkComponent> netCompList = new ArrayList<>(this.getNetworkModel().getNetworkComponents().values());
		for (int i = 0; i < netCompList.size(); i++) {

			NetworkComponent netComp = netCompList.get(i);

			// --- Increase counting for domain, NetworkComponent and GraphElement ------
			ComponentTypeSettings cts = graphSettings.getCurrentCTS().get(netComp.getType());
			if (cts!=null) {
				this.increaseElementCounter(CountingType.DomainElement, cts.getDomain());
			}
			this.increaseElementCounter(CountingType.NetworkComponent, netComp.getType());
			this.increaseElementCounter(CountingType.GraphElementPrototype, cts.getGraphPrototype());
			
			// --- Check for connection to other components -----------------------------
			if (this.getNetworkModel().getNeighbourNetworkComponents(netComp).size()==0) {
				this.getNotConnectedNetworkComponentList().add(netComp.toString());
			}
		}
	}
	
	/**
	 * Returns the list of not connected NetworkComponents.
	 * @return the not connected network component list
	 */
	private List<String> getNotConnectedNetworkComponentList() {
		if (notConnectedNetworkComponents==null) {
			notConnectedNetworkComponents = new ArrayList<>();
		}
		return notConnectedNetworkComponents;
	}
	
	// --------------------------------------------------------------
	// --- From here methods for the element counting can be found --
	// --------------------------------------------------------------
	/**
	 * Returns the element counting hash map.
	 * @return the element counting hash map
	 */
	private HashMap<ElementCounter, Integer> getElementCountingHashMap() {
		if (elementCountingHashMap==null) {
			elementCountingHashMap = new HashMap<>();
		}
		return elementCountingHashMap;
	}
	/**
	 * The Class ElementCounter.
	 */
	public class ElementCounter {
		
		private CountingType countingType;
		private String elementID;
		
		/**
		 * Instantiates a new element counter.
		 *
		 * @param countingType the counting type
		 * @param elementID the element ID
		 */
		public ElementCounter(CountingType countingType, String elementID) {
			this.setCountingType(countingType);
			this.setElementID(elementID);
		}
		
		/**
		 * Gets the counting type.
		 * @return the counting type
		 */
		public CountingType getCountingType() {
			return countingType;
		}
		/**
		 * Sets the counting type.
		 * @param countingType the new counting type
		 */
		public void setCountingType(CountingType countingType) {
			this.countingType = countingType;
		}

		/**
		 * Gets the element ID.
		 * @return the element ID
		 */
		public String getElementID() {
			return elementID;
		}
		/**
		 * Sets the element ID.
		 * @param elementID the new element ID
		 */
		public void setElementID(String elementID) {
			this.elementID = elementID;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.countingType.name() + "-" + this.elementID;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object compareObject) {
			if (compareObject instanceof ElementCounter) {
				ElementCounter ecCompare = (ElementCounter) compareObject;
				if (ecCompare.getCountingType()==this.getCountingType()) {
					if (ecCompare.getElementID().equals(this.getElementID())) {
						return true;
					}
				}
			}
			return false;
		}
	}
	/**
	 * Increase the specified element counter.
	 *
	 * @param countingType the counting type
	 * @param elementID the element ID
	 */
	private void increaseElementCounter(CountingType countingType, String elementID) {
 		ElementCounter ec = new ElementCounter(countingType, elementID);
		Integer elementsFound = this.getElementCountingHashMap().get(ec);
		if (elementsFound==null) {
			elementsFound = this.getElementCountingHashMap().put(ec, Integer.valueOf(1));
		} else {
			this.getElementCountingHashMap().put(ec,  Integer.valueOf(elementsFound+1));
		}
	}
	/**
	 * Returns the {@link ElementCounter} of the specified {@link CountingType}.
	 *
	 * @param type the CountingType to filter for
	 * @return the element counter by type
	 */
	private List<ElementCounter> getElementCounterByType(CountingType type) {
		List<ElementCounter> typedElementCounter = new ArrayList<>();
		List<ElementCounter> elementCounterList = new ArrayList<>(this.getElementCountingHashMap().keySet());
		for (int i = 0; i < elementCounterList.size(); i++) {
			ElementCounter elementCounter = elementCounterList.get(i); 
			if (elementCounter.getCountingType()==type) {
				typedElementCounter.add(elementCounter);
			}
		}
		return typedElementCounter;
	}
	

	// --------------------------------------------------------------
	// --- From here methods for the analysis results can be found --
	// --------------------------------------------------------------
	private String getStringNetworkComponentS(int noOf) {
		if (noOf==1) {
			return "NetworkComponent";	
		}
		return "NetworkComponents";
	}
	/**
	 * Returns the overall result message list with its single results.
	 * @return the result message list
	 */
	public List<String> getResultMessageList() {
		if (resultList==null) {
			resultList = new ArrayList<>();
		}
		return resultList;
	}
	/**
	 * Adds the specified single result message to the result mesage list.
	 * @param newMessage the new message
	 */
	private void addResultMessage(String newMessage) {
		this.getResultMessageList().add(newMessage);
		this.informListener(NetworkModelAnalyzerEvent.AppendedMessage);
	}
	
	/**
	 * Gets the result message string.
	 * @return the result message string
	 */
	public String getResultMessageString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.getResultMessageList().size(); i++) {
			if (builder.length()>=0) {
				builder.append(System.lineSeparator());
			}
			builder.append(this.getResultMessageList().get(i));
		}
		return builder.toString();
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
				listener.onNetworkModelAnalysStarted();
				break;

			case AnalysisFinalized:
				listener.onNetworkModelAnalysFinalized();
				break;
				
			case AppendedMessage:
				listener.onAppendedMessage(this.getResultMessageList().get(this.getResultMessageList().size()-1));
				break;
			}
		}
	}

	
}
