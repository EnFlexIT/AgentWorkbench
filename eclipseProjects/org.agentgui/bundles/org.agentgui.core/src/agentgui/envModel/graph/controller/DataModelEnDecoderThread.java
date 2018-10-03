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
package agentgui.envModel.graph.controller;

import java.awt.Cursor;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.agentgui.gui.AwbProgressMonitor;
import org.agentgui.gui.UiBridge;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;

/**
 * The Class DataModelEnDecoderThread encodes or decodes the data model 
 * of {@link NetworkComponent}'s or {@link GraphNode}'s in or from a Base64 string.
 * 
 * @see NetworkComponent#setDataModel(Object)
 * @see NetworkComponent#setDataModelBase64(java.util.Vector)
 * @see GraphNode#getDataModel()
 * @see GraphNode#getDataModelBase64()
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class DataModelEnDecoderThread extends Thread {

	public enum OrganizerAction {
		ORGANIZE_ENCODE_64,
		ORGANIZE_DECODE_64,
	}

	private enum WorkerAction {
		ENCODE_64,
		DECODE_64
	}

	// --- Variables for all ------------------------------
	private GraphEnvironmentController graphController;

	// --- Variables for the organizer --------------------
	private DataModelEnDecoderThread organizerThread;
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
	private Vector<Object> componentsToWorkOn;
	private HashMap<String, NetworkComponentAdapter> networkComponentAdapterHash;
	
	
	/**
	 * Instantiates a new organizer thread for the data model encoding or decoding .
	 *
	 * @param graphController the graph controller
	 * @param action the action that is either {@link OrganizerAction#ORGANIZE_DECODE_64} or {@link OrganizerAction#ORGANIZE_ENCODE_64}
	 */
	public DataModelEnDecoderThread(GraphEnvironmentController graphController, OrganizerAction action) {
		this.graphController = graphController;
		this.organizerAction = action;
		if (action==OrganizerAction.ORGANIZE_ENCODE_64) {
			this.setName("Encoding-Manager");
		} else if (action==OrganizerAction.ORGANIZE_DECODE_64) {
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
	private DataModelEnDecoderThread(DataModelEnDecoderThread organizerThread, GraphEnvironmentController graphController, Vector<Object> componentsToWorkOn, WorkerAction action, int threadNo) {
		this.organizerThread = organizerThread;
		this.graphController = graphController;
		this.componentsToWorkOn = componentsToWorkOn;
		this.workerAction = action;
		if (action==WorkerAction.ENCODE_64) {
			this.setName("Encoding-Worker-" + threadNo);
		} else if (action==WorkerAction.DECODE_64) {
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
				case ORGANIZE_DECODE_64:
				case ORGANIZE_ENCODE_64:
					this.organizeEnDecoding();
					break;
				}
			}
			// --- Worker action --------------------------
			if (this.workerAction!=null) {
				switch (this.workerAction) {
				case DECODE_64:
					this.decode64();
					break;
					
				case ENCODE_64:
					this.encode64();
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
	private void organizeEnDecoding() {
		
    	this.finalizer = new Object();
		this.firstDisplayTime = System.currentTimeMillis() + this.firstDisplayWaitTime;

		// --- Summarize NetworkComponent's and GraphNode's --------- 
		Vector<Object> sumCompVector = this.getNetworkObjectsToEnDecode();
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
		Vector<Vector<Object>> splitVector = new Vector<>();
		
		// --- Distribute components on separated Vector ------------
		int roundTripIndex = 0;
		for (int i = 0; i < sumCompVector.size(); i++) {
			
			Object component = sumCompVector.get(i);
			// --- Make sure that a part Vector is available --------
			if (splitVector.size()<roundTripIndex+1) {
				splitVector.add(new Vector<Object>());
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
			Vector<Object> elementVector = splitVector.get(i);
			switch (this.organizerAction) {
			case ORGANIZE_DECODE_64:
				new DataModelEnDecoderThread(this, this.graphController, elementVector, WorkerAction.DECODE_64, i+1).start();
				break;

			case ORGANIZE_ENCODE_64:
				new DataModelEnDecoderThread(this, this.graphController, elementVector, WorkerAction.ENCODE_64, i+1).start();	
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
	 * Returns the network objects where data model have to be to en- or decoded.
	 * @return the network objects to en- decode
	 */
	private Vector<Object> getNetworkObjectsToEnDecode() {

		if (this.organizerAction==null) return new Vector<>();
		
		// --- Define the result vector -----------------------------
		Vector<Object> reducedNetElementVector = new Vector<>();

		// --- Work on the NetworkComponents ------------------------
		Object[] netComps = this.graphController.getNetworkModel().getNetworkComponents().values().toArray();
		for (int i = 0; i < netComps.length; i++) {
			
			NetworkComponent netComp = (NetworkComponent) netComps[i];
			switch (this.organizerAction) {
			case ORGANIZE_ENCODE_64:
				if (netComp.getDataModel()!=null) {
					reducedNetElementVector.add(netComp);
				}
				break;

			case ORGANIZE_DECODE_64:
				if (netComp.getDataModelBase64()!=null && netComp.getDataModelBase64().size()>0) {
					reducedNetElementVector.add(netComp);
				}
				break;
			}
		}

		// --- Work on the GrphNodes --------------------------------
		Object[] graphNodes = this.graphController.getNetworkModel().getGraph().getVertices().toArray();
		for (int i = 0; i < graphNodes.length; i++) {
			
			GraphNode graphNode = (GraphNode) graphNodes[i];
			switch (this.organizerAction) {
			case ORGANIZE_ENCODE_64:
				if (graphNode.getDataModel()!=null) {
					reducedNetElementVector.add(graphNode);
				}
				break;

			case ORGANIZE_DECODE_64:
				if (graphNode.getDataModelBase64()!=null && graphNode.getDataModelBase64().size()>0) {
					reducedNetElementVector.add(graphNode);
				}
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
			case ORGANIZE_DECODE_64:
				title = Language.translate("Initiating network components", Language.EN);
		    	header = Language.translate("Initiating network components and setting data model", Language.EN);
		    	progress = Language.translate("Reading", Language.EN) + "...";
				break;
				
			case ORGANIZE_ENCODE_64:
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
	 * Decodes the data models from a Base64 string.
	 */
	private void decode64() {
		
		for (int i = 0; i < this.componentsToWorkOn.size(); i++) {
		
			try {

				// --- Find the corresponding NetworkComponentAdapter ---------
				NetworkComponentAdapter netCompAdapter=null;
				NetworkComponent netComp = null;
				GraphNode graphNode = null;
				
				Object objectToWorkOn = this.componentsToWorkOn.get(i);
				if (objectToWorkOn instanceof NetworkComponent) {
					netComp = (NetworkComponent) objectToWorkOn;		
					netCompAdapter = this.getNetworkComponentAdapter(this.graphController, netComp);
				} else if (objectToWorkOn instanceof GraphNode) {
					graphNode = (GraphNode) objectToWorkOn;
					netCompAdapter = this.getNetworkComponentAdapter(this.graphController, graphNode);	
				}
				
				// --- Set the components data model instance -----------------
				if (netCompAdapter!=null) {
					
					Vector<String> dataModelBase64 = null;
					if (graphNode!=null) {
						dataModelBase64 = graphNode.getDataModelBase64();
					} else {
						dataModelBase64 = netComp.getDataModelBase64();
					}
					
					if (dataModelBase64!=null) {
						// --- Get DataModelAdapter ---------------------------
						NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.getStoredDataModelAdapter();
						if (netCompDataModelAdapter!=null) {
							// --- Get Base64 decoded Object ------------------
							Object dataModel = netCompDataModelAdapter.getDataModelBase64Decoded(dataModelBase64);
							if (graphNode!=null) {
			    				graphNode.setDataModel(dataModel);
			    			} else {
			    				netComp.setDataModel(dataModel);
			    			}
						}
					}
					
				}// end found adapter
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.organizerThread.increaseComponentCounter();
		}// end for
	}

	
	/**
	 * Encode the data models into a Base64 string.
	 */
	private void encode64() {
		
		for (Object objectToWorkOn : this.componentsToWorkOn) {
			
			try {
				
				// --- Find the corresponding NetworkComponentAdapter ---------
				NetworkComponentAdapter netCompAdapter=null;
				NetworkComponent netComp = null;
				GraphNode graphNode = null;
				
				if (objectToWorkOn instanceof NetworkComponent) {
					netComp = (NetworkComponent) objectToWorkOn;		
					netCompAdapter = this.getNetworkComponentAdapter(this.graphController, netComp);
				} else if (objectToWorkOn instanceof GraphNode) {
					graphNode = (GraphNode) objectToWorkOn;
					netCompAdapter = this.getNetworkComponentAdapter(this.graphController, graphNode);	
				}
				// --- Set the components data model as Base64 ----------------
				if (netCompAdapter!=null) {
					
					Object dataModel = null;
					if (graphNode!=null) {
						dataModel = graphNode.getDataModel();
					} else {
						dataModel = netComp.getDataModel();
					}
					
					if (dataModel==null) {
						// --- No data model ----------------------------------
						if (graphNode!=null) {
		    				graphNode.setDataModelBase64(null);
		    			} else {
		    				netComp.setDataModelBase64(null);;
		    			}
					} else {
						// --- Get DataModelAdapter ---------------------------
						NetworkComponentAdapter4DataModel netCompDataModelAdapter = netCompAdapter.getStoredDataModelAdapter();
						if (netCompDataModelAdapter==null) {
							// --- No DataModelAdapter found ------------------
							if (graphNode!=null) {
			    				graphNode.setDataModelBase64(null);
			    			} else {
			    				netComp.setDataModelBase64(null);
			    			}
								
						} else {
							// --- Get Base64 encoded String ----------------
							Vector<String> dataModelBase64 = netCompDataModelAdapter.getDataModelBase64Encoded(dataModel);
							if (graphNode!=null) {
			    				graphNode.setDataModelBase64(dataModelBase64);
			    			} else {
			    				netComp.setDataModelBase64(dataModelBase64);
			    			}
						}
					}

				}// end found adapter
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.organizerThread.increaseComponentCounter();
		}// end for
	}
	

	/**
	 * Returns the NetworkComponentAdapter for the specified NetworkComponent.
	 *
	 * @param networkComponent the NetworkComponent
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, NetworkComponent networkComponent) {
		return this.getNetworkComponentAdapter(graphController, networkComponent.getType());
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified GraphNode.
	 *
	 * @param graphController the current graph controller, if there is one. Can also be null.
	 * @param graphNode the graph node
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, GraphNode graphNode) {
		String domain = this.graphController.getNetworkModel().getDomain(graphNode);
		if (domain!=null) {
			String searchFor = GeneralGraphSettings4MAS.GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX + domain;
			return this.getNetworkComponentAdapter(graphController, searchFor);
		}
		return null;
	}
	/**
	 * Returns the NetworkComponentAdapter for the specified type of component.
	 *
	 * @param componentTypeName the component type name
	 * @return the network component adapter
	 */
	private NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, String componentTypeName) {
		
		if (this.networkComponentAdapterHash==null) {
			this.networkComponentAdapterHash = new HashMap<String, NetworkComponentAdapter>();
		}
		NetworkComponentAdapter netCompAdapter = this.networkComponentAdapterHash.get(componentTypeName);
		if (netCompAdapter==null) {
			// --- Create corresponding NetworkComponentAdapter -----
			netCompAdapter = this.graphController.getNetworkModel().createNetworkComponentAdapter(graphController, componentTypeName);
			this.networkComponentAdapterHash.put(componentTypeName, netCompAdapter);
		}
		return netCompAdapter;
	}
	
}
