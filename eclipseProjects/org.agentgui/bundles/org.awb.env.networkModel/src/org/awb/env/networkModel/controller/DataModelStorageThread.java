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
package org.awb.env.networkModel.controller;

import java.awt.Cursor;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.agentgui.gui.AwbProgressMonitor;
import org.agentgui.gui.UiBridge;
import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

/**
 * The Class DataModelStorageThread encodes or decodes the data model 
 * of {@link NetworkComponent}'s or {@link GraphNode}'s in or from a Base64 string.
 * 
 * @see NetworkComponent#setDataModel(Object)
 * @see NetworkComponent#setDataModelBase64(java.util.Vector)
 * @see GraphNode#getDataModel()
 * @see GraphNode#getDataModelBase64()
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class DataModelStorageThread extends Thread {

	public enum OrganizerAction {
		ORGANIZE_SAVING,
		ORGANIZE_LOADING
	}

	private enum WorkerAction {
		SAVE,
		LOAD
	}

	// --- Variables for all ------------------------------
	private GraphEnvironmentController graphController;

	// --- Variables for the organizer --------------------
	private DataModelStorageThread organizerThread;
	private OrganizerAction organizerAction;
	private int elementsToConvert;
	private int elementsConverted;
	private int percentProgressOld = -1;
	private Object finalizer;
	
	private boolean isHeadlessOperation = Application.isOperatingHeadless();
	private AwbProgressMonitor progressMonitor;
	private long firstDisplayWaitTime = 0;			// ms
	private long firstDisplayTime;
	
	private long vectorDividerBytesPerThread = 450000; 
	
	// --- Variables for the worker -----------------------
	private WorkerAction workerAction;
	private Vector<DataModelNetworkElement> componentsToWorkOn;
	private HashMap<String, NetworkComponentAdapter> networkComponentAdapterHash;
	
	
	/**
	 * Instantiates a new organizer thread for the data model encoding or decoding .
	 *
	 * @param graphController the graph controller
	 * @param action the action that is either {@link OrganizerAction#ORGANIZE_LOADING} or {@link OrganizerAction#ORGANIZE_SAVING}
	 */
	public DataModelStorageThread(GraphEnvironmentController graphController, OrganizerAction action) {
		this.graphController = graphController;
		this.organizerAction = action;
		if (action==OrganizerAction.ORGANIZE_SAVING) {
			this.setName("Encoding-Manager");
		} else if (action==OrganizerAction.ORGANIZE_LOADING) {
			this.setName("Decoding-Manager");
		}
	}
	
	/**
	 * Instantiates a new sub thread for the data model encoding or decoding.
	 *
	 * @param organizerThread the parent organizer thread
	 * @param graphController the graph controller
	 * @param componentsToWorkOn the components to work on
	 * @param action the action
	 * @param name the designated name for the Thread
	 */
	private DataModelStorageThread(DataModelStorageThread organizerThread, GraphEnvironmentController graphController, Vector<DataModelNetworkElement> componentsToWorkOn, WorkerAction action, int threadNo) {
		this.organizerThread = organizerThread;
		this.graphController = graphController;
		this.componentsToWorkOn = componentsToWorkOn;
		this.workerAction = action;
		if (action==WorkerAction.SAVE) {
			this.setName("Encoding-Worker-" + threadNo);
		} else if (action==WorkerAction.LOAD) {
			this.setName("Decoding-Worker-" + threadNo);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		try {
			// --- Organizer action -----------------------
			if (this.organizerAction!=null) {
				switch (this.organizerAction) {
				case ORGANIZE_LOADING:
				case ORGANIZE_SAVING:
					this.organizePersitenceAction();
					break;
				}
			}
			// --- Worker action --------------------------
			if (this.workerAction!=null) {
				switch (this.workerAction) {
				case LOAD:
					this.loadDataModel();
					break;
					
				case SAVE:
					this.saveDataModel();
					break;
				}			
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Organizes the decoding or the encoding of the Base64 data models.
	 */
	private void organizePersitenceAction() {
		
    	this.finalizer = new Object();
		this.firstDisplayTime = System.currentTimeMillis() + this.firstDisplayWaitTime;

		// --- Summarize NetworkComponent's and GraphNode's --------- 
		Vector<DataModelNetworkElement> sumCompVector = this.getNetworkObjectsToLoadOrSave();
		this.elementsToConvert = sumCompVector.size();
		if (this.elementsToConvert==0) return;
		
		// --- Summarize the file sizes -----------------------------
		long fileSizeXML = this.graphController.getFileXML().length();
		long fileSizeGraphML = this.graphController.getFileGraphML().length();
		long fileSize = fileSizeXML + fileSizeGraphML; 
		
		// --- Split component vector -------------------------------
		int noOfVector = 1;
		try {
			noOfVector = ((int) (fileSize / this.vectorDividerBytesPerThread))+1;
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		// --- Finally, adjust the number of Threads ----------------
		if (noOfVector>this.elementsToConvert) noOfVector=this.elementsToConvert;
		if (noOfVector>15) noOfVector=15;
		if (noOfVector<=0) noOfVector=1;
		
		// --- Define separation Vector -----------------------------
		Vector<Vector<DataModelNetworkElement>> splitVector = new Vector<>();
		
		// --- Distribute components on separated Vector ------------
		int roundTripIndex = 0;
		for (int i = 0; i < sumCompVector.size(); i++) {
			
			DataModelNetworkElement component = sumCompVector.get(i);
			// --- Make sure that a part Vector is available --------
			if (splitVector.size()<roundTripIndex+1) {
				splitVector.add(new Vector<DataModelNetworkElement>());
			}
			splitVector.get(roundTripIndex).add(component);
			roundTripIndex++;
			if (roundTripIndex>=noOfVector) {
				roundTripIndex=0;
			}
		}

		// --- Set progress to 0 ------------------------------------
		this.setProgressToProgressMonitor(0);
		
		// --- Start thread for each element vector -----------------
		for (int i = 0; i < splitVector.size(); i++) {
			Vector<DataModelNetworkElement> elementVector = splitVector.get(i);
			switch (this.organizerAction) {
			case ORGANIZE_LOADING:
				new DataModelStorageThread(this, this.graphController, elementVector, WorkerAction.LOAD, i+1).start();
				break;

			case ORGANIZE_SAVING:
				new DataModelStorageThread(this, this.graphController, elementVector, WorkerAction.SAVE, i+1).start();	
				break;
			}
		}
		
    	// --- Wait to be finalized ---------------------------------
    	try {
    		synchronized (this.finalizer) {
    			this.finalizer.wait();		
    		}
    	} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

    	// --- Finally, close progress monitor ---------------------- 
    	if (this.isHeadlessOperation==false) {
    		SwingUtilities.invokeLater(new Runnable() {
    			@Override
    			public void run() {
    		    	getProgressMonitor().setVisible(false);
    		    	getProgressMonitor().dispose();
    		    	graphController.setBasicGraphGuiVisViewerActionOnTop(false);
    		    	if (Application.getMainWindow()!=null) {
    		    		Application.getMainWindow().setCursor(Cursor.getDefaultCursor());
    		    	}
					Application.setStatusBarMessageReady();
    			}
    		});    		
    	}
		
	}
	
	/**
	 * Returns the network objects where data model have to be to saved or loaded.
	 * @return the network objects to en- decode
	 */
	private Vector<DataModelNetworkElement> getNetworkObjectsToLoadOrSave() {

		if (this.organizerAction==null) return new Vector<>();
		
		// --- Define the result vector -----------------------------
		Vector<DataModelNetworkElement> reducedNetElementVector = new Vector<>();

		// --- Work on the NetworkComponents ------------------------
		Object[] netComps = this.graphController.getNetworkModel().getNetworkComponents().values().toArray();
		for (int i = 0; i < netComps.length; i++) {
			
			NetworkComponent netComp = (NetworkComponent) netComps[i];
			switch (this.organizerAction) {
			case ORGANIZE_SAVING:
				if (netComp.getDataModel()!=null) {
					reducedNetElementVector.add(netComp);
				}
				break;

			case ORGANIZE_LOADING:
				reducedNetElementVector.add(netComp);
				break;
			}
		}

		// --- Work on the GrphNodes --------------------------------
		Object[] graphNodes = this.graphController.getNetworkModel().getGraph().getVertices().toArray();
		for (int i = 0; i < graphNodes.length; i++) {
			
			GraphNode graphNode = (GraphNode) graphNodes[i];
			switch (this.organizerAction) {
			case ORGANIZE_SAVING:
				if (graphNode.getDataModel()!=null) {
					reducedNetElementVector.add(graphNode);
				}
				break;

			case ORGANIZE_LOADING:
				reducedNetElementVector.add(graphNode);
				break;
			}
		}
		return reducedNetElementVector;
	}
	
	/**
	 * Increases the component counter that count converted elements.
	 */
	public synchronized void increaseComponentCounter() {
		
		this.elementsConverted++;
		if (this.elementsConverted>=this.elementsToConvert) {
			// --- Conversion is done -------------------------------
    		synchronized (this.finalizer) {
    			this.finalizer.notify();		
    		}
			return;
		}

		// --- Calculate Progress -----------------------------------
		float progressCalc = (float) (((float)this.elementsConverted/(float)this.elementsToConvert) * 100.0);
		int percentProgressNew = Math.round(progressCalc);

		// --- Only display progress, if procedure is too long ------
		if (System.currentTimeMillis()>this.firstDisplayTime && percentProgressNew!=percentProgressOld) {
			this.setProgressToProgressMonitor(percentProgressNew);
		}
		
	}
	
	/**
	 * Sets the specified progress to the progress monitor.
	 * @param percentProgressNew the new progress to progress monitor
	 */
	private void setProgressToProgressMonitor(final int percentProgressNew) {
		
		if (this.isHeadlessOperation==true) return;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// --- Set Progress monitor ---------------------
				getProgressMonitor().setProgress(percentProgressNew);
				percentProgressOld = percentProgressNew;
				// --- Show progress monitor if not visible ----- 
				if (getProgressMonitor().isVisible()==false) {
					getProgressMonitor().setVisible(true);
				}
			}
		});
	}
	
	/**
	 * Returns the progress monitor for the current action.
	 * @return the progress monitor
	 */
	private AwbProgressMonitor getProgressMonitor() {
		if (progressMonitor==null && this.isHeadlessOperation==false) {
			// --- Set title and header for the ProgressMonitor -----
			String title = null;
			String header = null;
			String progress = null;
			switch (this.organizerAction) {
			case ORGANIZE_LOADING:
				title = Language.translate("Initiating network components", Language.EN);
		    	header = Language.translate("Initiating network components and setting data model", Language.EN);
		    	progress = Language.translate("Reading", Language.EN) + "...";
				break;
				
			case ORGANIZE_SAVING:
				title = Language.translate("Preparing network components", Language.EN);
		    	header = Language.translate("Preparing and encoding network components for saving", Language.EN);
		    	progress = Language.translate("Writing", Language.EN) + "...";
				break;

			}
	    	
			// --- Initiate ProgressMonitor -------------------------
			progressMonitor = UiBridge.getInstance().getProgressMonitor(title, header, progress);
			progressMonitor.setOwner(Application.getGlobalInfo().getOwnerFrameForComponent(this.graphController.getGraphEnvironmentControllerGUI()));
			progressMonitor.setAllow2Cancel(false);
			
		}
		return progressMonitor;
	}
	
	// --------------------------------------------------------------------------------------------
	// --- From here, the worker methods can be found ---------------------------------------------
	// --------------------------------------------------------------------------------------------
	/**
	 * Loads the data models of all {@link DataModelNetworkElement}s (e.g decode from Base64 string).
	 */
	private void loadDataModel() {
		
		for (int i = 0; i < this.componentsToWorkOn.size(); i++) {
		
			try {
				// --- Set the components data model instance -----------------
				DataModelNetworkElement networkElement =  this.componentsToWorkOn.get(i);
				NetworkComponentAdapter netCompAdapter = this.getNetworkComponentAdapter(networkElement);
				if (netCompAdapter!=null) {
					// --- Get DataModelAdapter -------------------------------
					NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.getStoredDataModelAdapter();
					if (netCompDataModelAdapter!=null) {
						// --- Load data model instance ------------------------
						AbstractDataModelStorageHandler storageHandler = netCompDataModelAdapter.getDataModelStorageHandlerInternal();
						Object dataModel = storageHandler.loadDataModel(networkElement);
						networkElement.setDataModel(dataModel);
    					// --- Requires persistence update? -------------------
    					if (storageHandler.isRequiresPersistenceUpdate()==true) {
    						TreeMap<String, String> settings = storageHandler.saveDataModel(networkElement);
    						networkElement.setDataModelStorageSettings(settings);
    						if (this.graphController.getProject()!=null) {
    							this.graphController.getProject().setUnsaved(true);
    						}
    					}
					}
				} 
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.organizerThread.increaseComponentCounter();
		}// end for
	}

	
	/**
	 * Saves the data models of all {@link DataModelNetworkElement}s (e.g encode as Base64 string).
	 */
	private void saveDataModel() {
		
		for (int i = 0; i < this.componentsToWorkOn.size(); i++) {
			
			try {

				// --- Set the components data model as Base64 ----------------
				DataModelNetworkElement dmNetElement = this.componentsToWorkOn.get(i);
				NetworkComponentAdapter netCompAdapter = this.getNetworkComponentAdapter(dmNetElement);
				if (netCompAdapter!=null) {
					// --- Get DataModelAdapter -------------------------------
					NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.getStoredDataModelAdapter();
					if (netCompDataModelAdapter!=null) {
						TreeMap<String, String> storageSettings = netCompDataModelAdapter.getDataModelStorageHandlerInternal().saveDataModel(dmNetElement);
						if (storageSettings==null || storageSettings.size()==0) {
							dmNetElement.setDataModelStorageSettings(null);
						} else {
							dmNetElement.setDataModelStorageSettings(storageSettings);
						}
					}
				}					
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.organizerThread.increaseComponentCounter();
		}// end for
	}
	
	
	/**
	 * Returns the NetworkComponentAdapter HashMap that serves as reminder for known adapter.
	 * @return the network component adapter hash
	 */
	private HashMap<String, NetworkComponentAdapter> getNetworkComponentAdapterHash() {
		if (this.networkComponentAdapterHash==null) {
			this.networkComponentAdapterHash = new HashMap<String, NetworkComponentAdapter>();
		}
		return networkComponentAdapterHash;
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified DataModelNetworkElement.
	 *
	 * @param networkComponent the NetworkComponent
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(DataModelNetworkElement networkElement) {
		if (networkElement instanceof NetworkComponent) {
			return this.getNetworkComponentAdapter((NetworkComponent)networkElement);
		} else if (networkElement instanceof GraphNode) {
			return this.getNetworkComponentAdapter((GraphNode)networkElement);
		}
		return null;
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified NetworkComponent.
	 *
	 * @param networkComponent the NetworkComponent
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(NetworkComponent networkComponent) {
		return this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, networkComponent, false, this.getNetworkComponentAdapterHash());
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified GraphNode.
	 *
	 * @param graphNode the graph node
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphNode graphNode) {
		return this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, graphNode, false, this.getNetworkComponentAdapterHash());
	}
	
}
