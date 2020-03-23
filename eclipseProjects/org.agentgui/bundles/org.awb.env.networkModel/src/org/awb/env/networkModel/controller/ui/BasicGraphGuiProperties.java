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
package org.awb.env.networkModel.controller.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.adapter.BundlingNetworkComponentAdapter4DataModel;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;
import org.awb.env.networkModel.controller.DataModelEnDecoder64;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.visualisation.notifications.DataModelNotification;
import org.awb.env.networkModel.visualisation.notifications.UpdateDataSeries;
import org.awb.env.networkModel.visualisation.notifications.UpdateDataSeriesException;

import agentgui.core.application.Language;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;
import de.enflexit.eom.api.EomVisualizationHostContainer;
import jade.core.AID;

/**
 * The Class BasicGraphGuiProperties is used as dialog in order to configure
 * properties of {@link NetworkComponent}'s or {@link GraphNode}'s. Therefore, this 
 * Dialog searches first for the corresponding {@link NetworkComponentAdapter}.
 * As a second step it reads the data model instance from the GraphNode or 
 * NetworkComponent and passes this instance to the component for visualising.
 * In the end the changed instance will be transfered back to the specific
 * GraphNode or NetworkComponent.
 * 
 * @see NetworkComponent
 * @see NetworkComponent#getDataModel()
 * @see GraphNode
 * @see GraphNode#getDataModel()
 * @see NetworkComponentAdapter
 * @see NetworkComponentAdapter4DataModel
 * @see NetworkComponentAdapter4DataModel#getVisualizationComponent(BasicGraphGuiProperties)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BasicGraphGuiProperties extends BasicGraphGuiJInternalFrame implements Observer, ActionListener, EomVisualizationHostContainer {

	private static final long serialVersionUID = -868257113588339559L;

	private final String pathImage = GraphGlobals.getPathImages();
	
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT= 450;
	
	private Object selectedGraphObject;
	private GraphNode graphNode;
	private NetworkComponent networkComponent;
	
	private NetworkComponentAdapter networkComponentAdapter;
	private NetworkComponentAdapter4DataModel adapter4DataModel;
	
	private JPanel jContentPane;  
	private JToolBar jToolBarProperties;
	private JToolBarButton jToolBarButtonSave;
	private JToolBarButton jToolBarButtonSaveAndExit;
	private JToolBarButton jToolBarButtonDisableRuntimeUpdates;
	private JComponent jComponentContent;

	private TreeMap<String, String> dataModelInitialStorageSettings;
	private Vector<Integer> dataModelInitialHashCodes;
	private Object newDataModel;
	
	private boolean dataModelNotificationEnabled = true; 
	private DataModelNotification dataModelNotificationLast;
	
	
	/**
	 * Instantiates a new properties dialog for GraphNodes or NetworkComponents.
	 *
	 * @param graphController the current {@link GraphEnvironmentController}
	 * @param selectedGraphObject the selected graph object
	 */
	public BasicGraphGuiProperties(GraphEnvironmentController graphController, Object selectedGraphObject) {
		super(graphController);
		this.setSelectedGraphObject(selectedGraphObject);
		this.initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setAutoscrolls(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setIconifiable(true);

		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		this.setTitle("Component");
		
		// --- Remove Frame menu ----------------
		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
		ui.getNorthPane().remove(0);
		
		this.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent ife) {
				BasicGraphGuiProperties.this.doClose();
			}
		});
		
		this.configureForGraphObject();
		this.setContentPane(this.getJContentPane());
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setInitialSizeAndPosition();
		
		// --- Call to the super-class ----------
		this.registerAtDesktopAndSetVisible();
		this.graphController.addObserver(this);
	}
	
	/**
	 * Sets the initial size and position of the frame.
	 */
	private void setInitialSizeAndPosition() {

		// --- Get the initial x-position of the property window --------------
		int posBasicGraphGui = this.graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui().getLocation().x;
		int posVisViewOnBasicGraphGui = this.basicGraphGui.getVisualizationViewer().getParent().getLocation().x; 
		int initialX = posBasicGraphGui + posVisViewOnBasicGraphGui;
		
		// --- Initial size value ----------------------------------------- 
		Dimension newSize = this.getDefaultSize();
		if (this.graphDesktop!=null) {

			// --- Possibly, a user defined size is to be considered ----------
			if (this.getNetworkComponentAdapter4DataModel()!=null) {
				Dimension userSize = this.getNetworkComponentAdapter4DataModel().getSizeOfVisualisation(this.graphDesktop); 
				if (userSize!=null) {
					newSize = userSize;
				}
			}
			
			if (this.graphDesktop.getLastOpenedEditor()==null) {
				this.setLocation(initialX, 0);
			} else {
				int movementX = 10;
				int movementY = 22;
				Point lastEditorPosition = this.graphDesktop.getLastOpenedEditor().getLocation();
				this.setLocation(lastEditorPosition.x + movementX, lastEditorPosition.y + movementY);
			}
			
		} else {
			this.setLocation(initialX, 0);
			
		}
		this.setSize(newSize);
		
	}
	
	/**
	 * Returns the default size of this dialog, which depends on the size of the {@link BasicGraphGuiJDesktopPane}.
	 * @return the default size
	 */
	public Dimension getDefaultSize() {
		Dimension defaultSize = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		if (this.graphDesktop!=null) {
			Dimension desktopSize = this.graphDesktop.getSize();
			defaultSize = new Dimension(DEFAULT_WIDTH, (int) (desktopSize.getHeight()*(2.0/3.0)));
		}
		return defaultSize;
	}
	
	/**
	 * Sets the selected graph object that is either a NetworkComponent or a GraphNode.
	 * @param selectedGraphObject the new selected graph object
	 */
	public void setSelectedGraphObject(Object selectedGraphObject) {
		this.selectedGraphObject = selectedGraphObject;
	}
	/**
	 * Returns the selected graph object for this editor.
	 * @return the graphObject
	 */
	public Object getSelectedGraphObject() {
		return selectedGraphObject;
	}
	
	/**
	 * Configure for graph object.
	 */
	private void configureForGraphObject() {

		String title2Set = null;
		if (this.getSelectedGraphObject()==null) {
			title2Set = "No valid selection!";
			
		} else if (this.getSelectedGraphObject() instanceof GraphNode) {
			// --- Set the local variable ---------------------------
			this.graphNode = (GraphNode) this.getSelectedGraphObject();
			// --- Get the corresponding NetworkComponentAdapter ----			
			this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, this.graphNode, true, null);
			title2Set = "Vertex: " + this.graphNode.getId();
			
		} else if (this.getSelectedGraphObject() instanceof GraphEdge) {
			// --- Just get the corresponding NetworkComponent ------ 
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent((GraphEdge) this.getSelectedGraphObject());
			// --- Get the corresponding NetworkComponentAdapter ---- 
			this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, this.networkComponent, true, null);
			title2Set = "Comp.: " + this.networkComponent.getId() + " (" +  this.networkComponent.getType() + ")";
			
		} else if (this.getSelectedGraphObject() instanceof NetworkComponent) {
			// --- Cast to NetworkComponent -------------------------
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent(((NetworkComponent) this.getSelectedGraphObject()).getId());
			// --- Get the corresponding NetworkComponentAdapter ---- 
			this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, this.networkComponent, true, null);
			title2Set = "Comp.: " + this.networkComponent.getId() + " (" +  this.networkComponent.getType() + ")";
		}

		if (this.networkComponent!=null) {
			// --- Mark / Select NetworkComponent for user ----------
			NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Selected);
			nmn.setInfoObject(this.networkComponent);
			this.graphController.notifyObservers(nmn);
		}

		// --- Some layout stuff ----------------------------------- 
		if (title2Set!=null) {
			this.setTitle(title2Set);	
		}
		
	}
	
	/**
	 * Returns the current {@link NetworkComponentAdapter}.
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter() {
		return this.networkComponentAdapter;
	}
	/**
	 * Returns the current adapter for the data model of the {@link NetworkComponent}.
	 * @return the network component adapter4 data model
	 */
	public NetworkComponentAdapter4DataModel getNetworkComponentAdapter4DataModel() {
		if (this.adapter4DataModel==null) {
			if (this.getNetworkComponentAdapter()!=null) {
				// --- Ensure to get a new adapter4DataModel --------
				this.getNetworkComponentAdapter().resetStoredDataModelAdapter();
				this.adapter4DataModel = this.getNetworkComponentAdapter().getStoredDataModelAdapter();
			}	
		}
		return this.adapter4DataModel;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.api.EomHostContainer#exchangeJPanelContent(javax.swing.JPanel)
	 */
	@Override
	public void exchangeJPanelContent(JPanel newJPanelContent) {
		if (newJPanelContent==null) return;
		// --- Set visualization component to NetworkComponentAdapter --------- 
		this.getNetworkComponentAdapter4DataModel().setVisualizationComponent(newJPanelContent);
		this.jComponentContent = newJPanelContent;
	}
	
	/**
	 * This method initializes jPanelContent	
	 * @return javax.swing.JPanel	
	 */
	private JComponent getJPanelContent() {
		if (this.jComponentContent==null) {
			
			if (this.getNetworkComponentAdapter()==null) {
				// --- No network component adapter was defined -------------------------
				this.getJToolBarButtonSave().setEnabled(false);
				this.getJToolBarButtonSaveAndExit().setEnabled(false);
				this.jComponentContent = this.getContentForEmptyNetworkComponentAdapter();
				
			} else {
				
				// --- Define the DataModelNetworkElement ------------------------------- 
				DataModelNetworkElement networkElement = null;
				if (this.graphNode!=null) {
					networkElement = this.graphNode;
				} else {
					networkElement = this.networkComponent;
				}
				
				if (this.getNetworkComponentAdapter4DataModel()==null) {
					// --- No DataModelAdapter was defined -------------------------
					networkElement.setDataModel(null);
					networkElement.setDataModelStorageSettings(null);
					networkElement.setDataModelBase64(null);
					// --- Disable save-actions ------------------------------------
					this.getJToolBarButtonSave().setEnabled(false);
					this.getJToolBarButtonSaveAndExit().setEnabled(false);
					
				} else {
					// --- There was a DataModelAdapter defined --------------------
					Object dataModel = networkElement.getDataModel();
					if (dataModel==null) {
    					// --- Check if data model is really null ------------------
						AbstractDataModelStorageHandler storageHandler = this.getNetworkComponentAdapter4DataModel().getDataModelStorageHandlerInternal(); 
    					dataModel = storageHandler.loadDataModel(networkElement);
    					networkElement.setDataModel(dataModel);
    					// --- Requires persistence update? ------------------------
    					if (storageHandler.isRequiresPersistenceUpdate()==true) {
    						TreeMap<String, String> settings = storageHandler.saveDataModel(networkElement);
    						networkElement.setDataModelStorageSettings(settings);
    						if (this.graphController.getProject()!=null) {
    							this.graphController.getProject().setUnsaved(true);
    						}
    					}
	    			}
					
					// --- Set model to visualization ------------------------------
					this.getNetworkComponentAdapter4DataModel().setDataModel(dataModel);

					// --- Remind initial HashCodes of Base64 data model vector ----
					this.setDataModelBase64InitialHashCodes(networkElement);

					// --- Get the visualization component -------------------------
					JComponent visualisation = this.getNetworkComponentAdapter4DataModel().getVisualizationComponent(this);
					if (visualisation instanceof OntologyInstanceViewer) {
						((OntologyInstanceViewer)visualisation).setJToolBar4UserFunctions(this.getJToolBarProperties());
					}
					
					// --- If Available, add individual tool bar elements ----------
					this.addCustomToolBarElements();
					
					if (visualisation==null) {
						this.getJToolBarButtonSave().setEnabled(false);
						this.getJToolBarButtonSaveAndExit().setEnabled(false);
						String displayText = "<html><center>" + Language.translate("No visualisation component could be found", Language.EN) + "!<br><br> </center></html>";
						visualisation = this.getContentForEmptyNetworkComponentAdapter(displayText);
					} else {
						visualisation.validate();
					}
					this.jComponentContent = visualisation;
				}
			}
		}
		return this.jComponentContent;
	}
	
	
	/**
	 * Gets the content for an empty {@link NetworkComponentAdapter}.
	 * @return the content for empty network component adapter
	 */
	private JComponent getContentForEmptyNetworkComponentAdapter() {
		return this.getContentForEmptyNetworkComponentAdapter(null);
	}
	/**
	 * Gets the content for an empty {@link NetworkComponentAdapter}.
	 * @param displayText the display text to show
	 * @return the content for empty network component adapter
	 */
	private JComponent getContentForEmptyNetworkComponentAdapter(String displayText) {
		
		if (displayText==null || displayText.isEmpty()) {
			if (this.networkComponent!=null) {
				displayText = "<html><center>" + Language.translate("No NetworkComponentAdapter\nwas defined for the\n NetworkComponent", Language.EN) + " " + this.networkComponent.getId() + " (" +  this.networkComponent.getType() + ")!<br><br> </center></html>";
			} else if (this.graphNode!=null) {
				
				List<String> domainList = this.graphController.getNetworkModel().getDomain(this.graphNode);
				if (domainList.size()==0) {
					displayText = "<html><center>" + Language.translate("No domain information could be found for GraphNode '", Language.EN) + " '" + this.graphNode.getId() + "'!<br><br> </center></html>";
							
				} else {
					String domainText = ""; 
					if (domainList.size()==1) {
						domainText = domainList.get(0); 
					} else {
						for (int i = 0; i < domainList.size(); i++) {
							if (i==0) {
								domainText = domainList.get(i);
							} else {
								if (i < (domainList.size()-1)) {
									domainText += ", " + domainList.get(i);
								} else {
									domainText += " " + Language.translate("and", Language.EN) + " " + domainList.get(i);
								}
							}
						}
					}
					displayText = "<html><center>" + Language.translate("No NetworkComponentAdapter\nwas defined for\n the GraphNodes of the Domain", Language.EN) + " '" + domainText + "'!<br><br> </center></html>";
				}
			}
		}
		
		JLabel jLabelNoAdapter = new JLabel();
		jLabelNoAdapter.setText(displayText);
		jLabelNoAdapter.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelNoAdapter.setHorizontalAlignment(JLabel.CENTER);
		jLabelNoAdapter.setSize(new Dimension(200, 260));
		return jLabelNoAdapter; 
	}
	
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	public JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(this.getJToolBarProperties(), BorderLayout.NORTH);
			JComponent dataContent = this.getJPanelContent();
			if (dataContent!=null) {
				jContentPane.add(dataContent, BorderLayout.CENTER);	
			}
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jToolBarProperties	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBarProperties() {
		if (jToolBarProperties == null) {
			jToolBarProperties = new JToolBar("Properties Bar");
			jToolBarProperties.setFloatable(false);
			jToolBarProperties.setRollover(true);
			jToolBarProperties.add(this.getJToolBarButtonSave());
			jToolBarProperties.add(this.getJToolBarButtonSaveAndExit());
			if (this.graphController.getProject()==null) {
				// --- During runtime ---------------------
				jToolBarProperties.addSeparator();
				jToolBarProperties.add(this.getJToolBarButtonDisableRuntimeUpdates());
			}
		}
		return jToolBarProperties;
	}
	/**
	 * Returns the JToolBarButton for the save action.
	 * @return the JToolBarButton for the save action
	 */
	private JToolBarButton getJToolBarButtonSave() {
		if (this.jToolBarButtonSave==null) {
			this.jToolBarButtonSave=new JToolBarButton("Save", Language.translate("Save", Language.EN), null, "MBsave.png", this);
		}
		return this.jToolBarButtonSave;
	}
	/**
	 * Returns the JToolBarButton for the save and exit action.
	 * @return the JToolBarButton for the save and exit action
	 */
	private JToolBarButton getJToolBarButtonSaveAndExit() {
		if (this.jToolBarButtonSaveAndExit==null) {
			this.jToolBarButtonSaveAndExit=new JToolBarButton("SaveAndExit", Language.translate("Save and Exit", Language.EN), null, "MBsaveAndExit.png", this);
		}
		return this.jToolBarButtonSaveAndExit;
	}
	/**
	 * Gets the JToolBarButton prevent runtime updates.
	 * @return the JToolBarButton prevent runtime updates
	 */
	private JToolBarButton getJToolBarButtonDisableRuntimeUpdates() {
		if (this.jToolBarButtonDisableRuntimeUpdates==null) {
			this.jToolBarButtonDisableRuntimeUpdates=new JToolBarButton("DisableRuntimeUpdates", Language.translate("Disable / Enable runtime updates", Language.EN), null, "Refresh.png", this);
		}
		return this.jToolBarButtonDisableRuntimeUpdates;
	}
	/**
	 * Adds the custom tool bar elements, if specified .
	 */
	private void addCustomToolBarElements() {
		
		if (this.getNetworkComponentAdapter4DataModel()==null || this.getNetworkComponentAdapter4DataModel().getToolBarElements()==null) return;
		
		Vector<JComponent> toolBarElements = this.getNetworkComponentAdapter4DataModel().getToolBarElements();
		for (int i = 0; i < toolBarElements.size(); i++) {
			JComponent comp = toolBarElements.get(i);
			if (this.getJToolBarProperties().getComponentIndex(comp)==-1) {
				this.getJToolBarProperties().add(comp);
			}
		}
	}
	
	/**
	 * Returns a hash code vector derived from a specified data model.
	 * 
	 * @param dataModel the data model
	 * @return the hash code vector from data model
	 */
	private Vector<Integer> getHashCodeVectorFromDataModel(Object dataModel) {
		
		if (dataModel==null) return null;
		
		// --- Create a Base64 version of the data model ------------ 
		Vector<String> dataModelBase64 = DataModelEnDecoder64.getDataModelBase64Encoded(dataModel);
		
		Vector<Integer> hashCodeVector = new Vector<Integer>();
		// --- Data model is an object array ----------
		for (int i = 0; i < dataModelBase64.size(); i++) {
			String singleDataModel = dataModelBase64.get(i);
			int singleDataModelHashCode = 0;
			if (singleDataModel!=null) {
				singleDataModelHashCode = singleDataModel.hashCode();	
			}
			hashCodeVector.add(singleDataModelHashCode);	
		}
		return hashCodeVector;
	}
	
	/**
	 * Sets the initial hash codes for a given data model.
	 * @param dataModelBase64 the new data model base64 initial hash codes
	 */
	private void setDataModelBase64InitialHashCodes(DataModelNetworkElement networkElement) {
		this.dataModelInitialStorageSettings = networkElement.getDataModelStorageSettings();
		this.dataModelInitialHashCodes = this.getHashCodeVectorFromDataModel(networkElement.getDataModel());
	}
	/**
	 * Checks if the storage settings or the data model have changed.
	 * @return true, if the storage settings or the data model has changed
	 */
	private boolean isChangedDataModel() {
		
		boolean changed = false;
		NetworkComponentAdapter4DataModel nca4dm = this.getNetworkComponentAdapter4DataModel();
		if (nca4dm!=null) {
			// --- Get the edited data model ------------------------
			Object newDataModel = this.getReviewedDataModel(nca4dm.getDataModel());
			Vector<Integer> newHashCodes = this.getHashCodeVectorFromDataModel(newDataModel);
			// --- Check for changes --------------------------------
			if (! (newHashCodes==null & this.dataModelInitialHashCodes==null)) {
				if ( (newHashCodes==null & this.dataModelInitialHashCodes!=null) || (newHashCodes!=null & this.dataModelInitialHashCodes==null) ) {
					changed = true;
				} else {
					// --- Compare the two vector instances ---------
					if (newHashCodes.size()!=this.dataModelInitialHashCodes.size()) {
						changed = true;
					} else {
						// --- Compare the elements of the Vector ---
						for (int i = 0; i < newHashCodes.size(); i++) {
							Integer hashElementOld = this.dataModelInitialHashCodes.get(i);
							Integer hashElementNew = newHashCodes.get(i);
							if (! (hashElementOld==null & hashElementNew==null)) {
								if ( (hashElementOld==null & hashElementNew!=null) || (hashElementOld!=null & hashElementNew==null)) {
									changed = true;
									break;
								} else {
									if (hashElementOld.equals(hashElementNew)==false) {
										changed = true;
										break;
									}
								}
							}
						} // end for
					}
				}
			}
		}
		return changed;
	}
	
	/**
	 * Have storage settings changed.
	 * @return true, if the storage settings were changed
	 */
	private boolean isChangedStorageSettings() {
		
		boolean changedSettings = false;
		
		NetworkComponentAdapter4DataModel nca4dm = this.getNetworkComponentAdapter4DataModel();
		if (nca4dm!=null) {
			
			// --- Get the current / edited data model --------------
			Object newDataModel = this.getReviewedDataModel(nca4dm.getDataModel());
			
			// --- Define the current edit element ------------------
			DataModelNetworkElement orgNetworkElement = null;
			if (this.graphNode!=null) {
				orgNetworkElement = this.graphNode;
			} else {
				orgNetworkElement = this.networkComponent;
			}
			
			// --- Get storage handler to invoke save simulated -----
			AbstractDataModelStorageHandler storageHandler = nca4dm.getDataModelStorageHandlerInternal();
			
			// --- Create temporary element -------------------------
			DataModelNetworkElement tempNetworkElement = storageHandler.createTemporaryNetworkElement(orgNetworkElement);
			tempNetworkElement.setDataModel(newDataModel);
			
			// --- Invoke to store that element ---------------------
			TreeMap<String, String> newStorageSettings = storageHandler.saveDataModelSimulated(tempNetworkElement);
			TreeMap<String, String> oldStorageSettings = this.dataModelInitialStorageSettings; 
			
			// --- Compare the storage settings ---------------------
			if (newStorageSettings==null && oldStorageSettings==null) {
				changedSettings = false;
			} else if ((newStorageSettings==null && oldStorageSettings!=null) || newStorageSettings!=null && oldStorageSettings==null) {
				changedSettings = true;
			} else {
				changedSettings = (newStorageSettings.equals(oldStorageSettings)==false);
			}
		}
		return changedSettings;
	}
	
	/**
	 * Checks if the current data model is savable.
	 * @return true, if the current data model can be saved
	 */
	private boolean isSavableModel() {
		NetworkComponentAdapter4DataModel nca4dm = this.getNetworkComponentAdapter4DataModel();
		if (nca4dm!=null) {
			return nca4dm.save();
		}
		return true;
	}
	
	/**
	 * Do the actual save operation.
	 * @return true, if successful
	 */
	private boolean doSave() {
		if (this.isSavableModel()==true) {
			this.saveToNetworkComponentOrGraphNode();
			if (this.graphController.getProject()==null) {
				this.sendChangesToAgent();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Does the close action.
	 */
	private void doClose() {
		
		int diaAnswer = 0;
		String diaTitle = Language.translate("Close Properties", Language.EN);
		String diaQuestion = null;
		
		// --- Check if we have changes -----------------------------
		if (this.isChangedDataModel()==true || this.isChangedStorageSettings()==true) {
		
			if (this.graphController.getProject()!=null) {
				// --- Setup case -------------------------
				diaQuestion = Language.translate("Save changes to network model?", Language.EN);
			} else {
				// --- Execution case ---------------------
				diaQuestion = Language.translate("Save and send data model changes to agent(s)?", Language.EN);
			}

			// --- User request ---------------------------
			diaAnswer = JOptionPane.showConfirmDialog(this, diaQuestion, diaTitle, JOptionPane.YES_NO_CANCEL_OPTION);
			if (diaAnswer==JOptionPane.YES_OPTION) {
				// --- Save to node or component ----------
				if (this.doSave()==true) {
					this.setInvisibleAndDispose();
				}
				
			} else if (diaAnswer==JOptionPane.NO_OPTION){
				this.setInvisibleAndDispose();
				
			} else if (diaAnswer==JOptionPane.CANCEL_OPTION){
				// --- Do nothing ---- 
			}
			
		} else {
			// --- Data model has NOT changed ! ---
			this.setInvisibleAndDispose();
		}
	}
	
	/**
	 * Sets property dialog invisible and disposes it.
	 */
	private void setInvisibleAndDispose() {
		this.graphController.deleteObserver(this);
		this.setVisible(false);
		this.dispose();
	}
	
	/**
	 * Saves the current data model to its corresponding NetworkComponent or GraphNode.
	 * If used in the setup environment, the project will be set to dirty (unsaved).
	 */
	private void saveToNetworkComponentOrGraphNode() {
		
		NetworkComponentAdapter4DataModel nca4dm = this.getNetworkComponentAdapter4DataModel();
		if (nca4dm!=null) {
			
			// --- Define the current edit element ------------------
			DataModelNetworkElement networkElement = null;
			if (this.graphNode!=null) {
				networkElement = this.graphNode;
			} else {
				networkElement = this.networkComponent;
			}
			
			// --- Set the new data model to that element -----------
			this.newDataModel = this.getReviewedDataModel(nca4dm.getDataModel());
			networkElement.setDataModel(this.newDataModel);
			
			// --- Invoke to store that element ---------------------
			TreeMap<String, String> storageSettings = nca4dm.getDataModelStorageHandlerInternal().saveDataModel(networkElement);
			if (storageSettings==null || storageSettings.size()==0) {
				networkElement.setDataModelStorageSettings(null);
			} else {
				networkElement.setDataModelStorageSettings(storageSettings);
			}
	
			// --- Store this a new initial hash reminder -----------
			this.setDataModelBase64InitialHashCodes(networkElement);
			
			if (this.graphController.getProject()!=null) {
				// --- Setup case -------------------
				this.graphController.setProjectUnsaved();
			}			
		}
		
	}

	/**
	 * Will review and possibly correct the specified data model instance.
	 *
	 * @param dataModel the data model
	 * @return the reviewed data model
	 */
	private Object getReviewedDataModel(Object dataModel) {
		
		if (dataModel!=null) {
			if (dataModel.getClass().isArray()==true) {
				Object[] dataModelArray = (Object[]) dataModel;
				for (int i = 0; i < dataModelArray.length; i++) {
					if (dataModelArray[i]!=null) {
						return dataModel;
					}
				}
				return null;
				
			} else {
				return dataModel;
			}
		}
		return null;
	}
	
	
	/**
	 * Sends the data model changes to the corresponding agent.
	 */
	private void sendChangesToAgent() {
		
		DataModelNotification dmNote = null;
		List<NetworkComponent> comps = new ArrayList<>();
		if (this.graphNode!=null) {
			dmNote = new DataModelNotification(this.graphNode);
			// --- Get the NetworkComponent's connected to this GraphNode -----
			comps = this.graphController.getNetworkModel().getNetworkComponents(this.graphNode);
			
		} else {
			dmNote = new DataModelNotification(this.networkComponent);
			comps.add(this.networkComponent);
		}
		
		if (dmNote!=null) {
			for (int i = 0; i < comps.size(); i++) {
				NetworkComponent netComp = comps.get(i);
				// --- Send notifications -------
				boolean done = this.getNetworkComponentAdapter().sendAgentNotification(new AID(netComp.getId(), AID.ISLOCALNAME), dmNote);
				if (done==false) {
					System.err.println("DataModelNotification to agent '" + netComp.getId() + "' could not be send.");
				}
			}
		}
		
	}
	
	/**
	 * Checks if is data model notification enabled.
	 * @return true, if is data model notification enabled
	 */
	private boolean isDataModelNotificationEnabled(){
		return this.dataModelNotificationEnabled;
	}
	/**
	 * Sets the data model notification enabled.
	 * @param enable the new data model notification enabled
	 */
	private void setDataModelNotificationEnabled(boolean enable) {

		String imgName = null;
		if (enable==true) {
			imgName = "Refresh.png";
		} else {
			imgName = "RefreshNot.png";
		}
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(this.pathImage + imgName), null);
		this.getJToolBarButtonDisableRuntimeUpdates().setIcon(imageIcon);
		this.dataModelNotificationEnabled = enable;
	}
	
	/**
	 * Puts a data model notification into this property window.
	 *
	 * @param dmn the DataModelNotification
	 * @return true, if successful
	 */
	public boolean setDataModelNotification(DataModelNotification dmn) {
		
		if (dmn==null) return true;
		
		// --- Is the notification relevant for this view ? ---------------
		if (dmn.isForGraphNode(this.graphNode)==false && dmn.isForNetworkComponent(this.networkComponent)==false) {
			return false;
		}

		// --- Updates allowed? -------------------------------------------
		if (this.isDataModelNotificationEnabled()==true) {
			this.dataModelNotificationLast = null;
		} else {
			this.dataModelNotificationLast = dmn;
			return true;
		}
		
		if (dmn.isForGraphNode(this.graphNode)==true) {
			Object dataModel = dmn.getGraphNode().getDataModel();
			// -- Update GraphNode data model -----------------------
			this.getNetworkComponentAdapter4DataModel().setDataModel(dataModel);
			this.setDataModelBase64InitialHashCodes(this.graphNode);
		}
		
		if (dmn.isForNetworkComponent(this.networkComponent)==true) {
			Object dataModel = dmn.getNetworkComponent().getDataModel();
			// -- Update NetworkComponent data model ----------------
			this.getNetworkComponentAdapter4DataModel().setDataModel(dataModel);
			this.setDataModelBase64InitialHashCodes(this.networkComponent);
		}
		return true;
	}
	
	/**
	 * Sets an update of a data series.
	 *
	 * @param uds the actual update of the data series
	 * @return true, if the update was for this property window
	 */
	public boolean setUpdateDataSeries(UpdateDataSeries uds) {
		
		if (uds==null) return true;
		
		// --- Check if the update is for the current property window ---------
		boolean applyUpdate = false;
		switch (uds.getComponentType()) {
		case GraphNode:
			if (this.graphNode!=null && this.graphNode.getId().equals(uds.getComponentID())) {
				applyUpdate = true;
			}
			break;
		case NetworkComponent:
			if (this.networkComponent!=null && this.networkComponent.getId().equals(uds.getComponentID())) {
				applyUpdate = true;
			}
			break;
		}
		
		// --- Apply the Update -----------------------------------------------
		if (applyUpdate==true) {
			try {
				// --- Update the corresponding OntologyInstanceViewer --------
				OntologyInstanceViewer ontoViewer = null;
				NetworkComponentAdapter4DataModel nca2DataModel = this.getNetworkComponentAdapter4DataModel();
				if (nca2DataModel instanceof BundlingNetworkComponentAdapter4DataModel) {
					BundlingNetworkComponentAdapter4DataModel bnca2DataModel = (BundlingNetworkComponentAdapter4DataModel) nca2DataModel;
					ontoViewer = (OntologyInstanceViewer) bnca2DataModel.getVisualizationComponent(uds.getDomain());
				} else {
					ontoViewer = (OntologyInstanceViewer) nca2DataModel.getVisualizationComponent(this);
				}
				
				if (ontoViewer!=null) {
					uds.applyToOntologyInstanceViewer(ontoViewer);
				}
				
			} catch (UpdateDataSeriesException udse) {
				System.err.println("=> Error in Property Dialog!");
				udse.printStackTrace();
			}
		}
		return applyUpdate;
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actionCommand = ae.getActionCommand();
		if (actionCommand.equals("Save") || actionCommand.equals("SaveAndExit")) {
			// --- Actions for 'Save' and 'Save and Exit' -----------
			if (this.doSave()==true) {
				// --- Close, if 'Save and Exit' --------------------
				if (actionCommand.equals("SaveAndExit")) {
					this.setVisible(false);
					this.dispose();
				
				}	
			}
		
		} else if (actionCommand.equals("DisableRuntimeUpdates")) {
			// --- Disable runtime updates --------------------------
			if (this.isDataModelNotificationEnabled()==false) {
				this.setDataModelNotificationEnabled(true);
				this.setDataModelNotification(this.dataModelNotificationLast);
			} else {
				this.setDataModelNotificationEnabled(false);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		if (updateObject instanceof NetworkModelNotification) {
			NetworkModelNotification nmNote = (NetworkModelNotification) updateObject;
			switch (nmNote.getReason()) {
			case NetworkModelNotification.NETWORK_MODEL_NetworkElementDataModelReLoaded:
				// --- Create an internal DataModelNotification to reload the data model ---------- 
				DataModelNotification dmNote = null;
				if (this.graphNode!=null) {
					dmNote = new DataModelNotification(this.graphNode);
				} else {
					dmNote = new DataModelNotification(this.networkComponent);
				}
				this.setDataModelNotification(dmNote);
				break;
			}
		}
		
	}
	
	/**
	 * The Class JToolBarButton.
	 */
	private class JToolBarButton extends JButton {

		private static final long serialVersionUID = 1L;
 
		/**
		 * Instantiates a new j tool bar button.
		 *
		 * @param actionCommand the action command
		 * @param toolTipText the tool tip text
		 * @param altText the alt text
		 * @param imgName the image name
		 * @param actionListener the ActionListener
		 */
		private JToolBarButton(String actionCommand, String toolTipText, String altText, String imgName, ActionListener actionListener) {
				
			this.setText(altText);
			this.setToolTipText(toolTipText);
			this.setSize(36, 36);
			
			if (imgName!=null) {
				this.setPreferredSize(new Dimension(26,26));
			} else {
				this.setPreferredSize(null);	
			}

			if (imgName!=null) {
				try {
					ImageIcon imageIcon = new ImageIcon( this.getClass().getResource(pathImage + imgName), altText);
					this.setIcon(imageIcon);
					
				} catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(actionListener);	
			this.setActionCommand(actionCommand);
		}
		
	}

	
}
