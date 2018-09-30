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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
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

import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.visualisation.notifications.DataModelNotification;
import agentgui.envModel.graph.visualisation.notifications.UpdateDataSeries;
import agentgui.envModel.graph.visualisation.notifications.UpdateDataSeriesException;
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
 * @see NetworkComponentAdapter4DataModel#getVisualisationComponent()
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BasicGraphGuiProperties extends BasicGraphGuiJInternalFrame implements EomVisualizationHostContainer, ActionListener {

	private static final long serialVersionUID = -868257113588339559L;

	private final String pathImage = GraphGlobals.getPathImages();
	
	private int defaultWidth = 300;
	private int defaultHeight= 450;
	
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

	private Vector<Integer> dataModelInitialHashCodes;
	private Object newDataModel;
	private Vector<String> newDataModelBase64;
	
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
		this.selectedGraphObject = selectedGraphObject;
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
				doClose();
			}
		});
		
		this.configureForGraphObject();
		this.setContentPane(this.getJContentPane());
		this.setSize(this.defaultWidth, this.defaultHeight);
		this.setInitialSizeAndPosition();
		
		// --- Call to the super-class ----------
		this.registerAtDesktopAndSetVisible();
		
	}
	
	/**
	 * Sets the initial size and position of the frame.
	 */
	private void setInitialSizeAndPosition() {

		// --- Get the initial x-position of the property window --------------
		int posBasicGraphGui = this.graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui().getLocation().x;
		int posVisViewOnBasicGraphGui = this.basicGraphGui.getVisualizationViewer().getParent().getLocation().x; 
		int initialX = posBasicGraphGui + posVisViewOnBasicGraphGui;
		
		if (this.graphDesktop!=null) {
			Dimension desktopSize = this.graphDesktop.getSize();
			Dimension newSize = new Dimension(this.defaultWidth, (int) (desktopSize.getHeight()*(2.0/3.0)));
			// --- Possibly, a user defined size is to be considered ----------
			if (this.getNetworkComponentAdapter4DataModel()!=null) {
				Dimension userSize = this.getNetworkComponentAdapter4DataModel().getSizeOfVisualisation(this.graphDesktop); 
				if (userSize!=null) newSize = userSize;	
			}
			
			if (this.graphDesktop.getLastOpenedEditor()==null) {
				this.setLocation(initialX, 0);
				this.setSize(newSize);
			} else {
				int movementX = 10;
				int movementY = 22;
				Point lastEditorPosition = this.graphDesktop.getLastOpenedEditor().getLocation();
				this.setLocation(lastEditorPosition.x+movementX, lastEditorPosition.y+movementY);
				this.setSize(newSize);
			}
		} else {
			this.setLocation(initialX, 0);
		}
	}
	
	/**
	 * Returns the graph object.
	 * @return the graphObject
	 */
	public Object getGraphObject() {
		return selectedGraphObject;
	}
	
	/**
	 * Configure for graph object.
	 */
	private void configureForGraphObject() {

		String title2Set = null;
		if (this.getGraphObject()==null) {
			title2Set = "No valid selection!";
			
		} else if (this.getGraphObject() instanceof GraphNode) {
			// --- Set the local variable ---------------------------
			this.graphNode = (GraphNode) this.getGraphObject();
			// --- Get the corresponding NetworkComponentAdapter ----			
			this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, this.graphNode);
			title2Set = "Vertex: " + this.graphNode.getId();
			
		} else if (this.getGraphObject() instanceof GraphEdge) {
			// --- Just get the corresponding NetworkComponent ------ 
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent((GraphEdge) this.getGraphObject());
			// --- Get the corresponding NetworkComponentAdapter ---- 
			this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, this.networkComponent);
			title2Set = "Comp.: " + this.networkComponent.getId() + " (" +  this.networkComponent.getType() + ")";
			
		} else if (this.getGraphObject() instanceof NetworkComponent) {
			// --- Cast to NetworkComponent -------------------------
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent(((NetworkComponent) this.getGraphObject()).getId());
			// --- Get the corresponding NetworkComponentAdapter ---- 
			this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.graphController, this.networkComponent);
			title2Set = "Comp.: " + this.networkComponent.getId() + " (" +  this.networkComponent.getType() + ")";
		}

		if (this.networkComponent!=null) {
			// --- Mark / Select NetworkComponent for user --------------
			NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Select);
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
				this.adapter4DataModel = this.getNetworkComponentAdapter().getNewDataModelAdapter();
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
		
		// --- Remove the old content -----------------------------------------
		this.getJContentPane().remove(this.getJPanelContent());
		// --- Add the new content --------------------------------------------
		this.jComponentContent = newJPanelContent;
		this.getJContentPane().add(newJPanelContent, BorderLayout.CENTER);
		this.validate();
		this.repaint();
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
				// --- There is a network component adapter available -------------------
				if (this.getNetworkComponentAdapter4DataModel()==null) {
					// --- No DataModelAdapter was defined -------------------------
					if (this.graphNode!=null) {
						this.graphNode.setDataModel(null);
						this.graphNode.setDataModelBase64(null);
					} else {
						this.networkComponent.setDataModel(null);
						this.networkComponent.setDataModelBase64(null);	
					}
					// --- Disable save-actions ------------------------------------
					this.getJToolBarButtonSave().setEnabled(false);
					this.getJToolBarButtonSaveAndExit().setEnabled(false);
					
				} else {
					// --- There was a DataModelAdapter defined --------------------
					Object dataModel = null;
					Vector<String> dataModelBase64 = null;
					// --- Get the Base64 encoded Vector<String> -------------- 
					if (this.graphNode!=null) {
						dataModel = this.graphNode.getDataModel();
						dataModelBase64 = this.graphNode.getDataModelBase64();
					} else {
						dataModel = this.networkComponent.getDataModel();
						dataModelBase64 = this.networkComponent.getDataModelBase64();	
					}
					
					if (dataModel==null && dataModelBase64!=null) {
    					// --- Convert Base64 decoded Object ------------------
    					dataModel = this.getNetworkComponentAdapter4DataModel().getDataModelBase64Decoded(dataModelBase64);
    					if (this.graphNode!=null) {
    						this.graphNode.setDataModel(dataModel);
    					} else {
    						this.networkComponent.setDataModel(dataModel);
    					}
	    			}
					
					// --- Set model to visualization ------------------------------
					this.getNetworkComponentAdapter4DataModel().setDataModel(dataModel);

					// --- Remind initial HashCodes of Base64 data model vector ----
					this.setDataModelBase64InitialHashCodes(dataModelBase64);

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
				String domain = this.graphController.getNetworkModel().getDomain(this.graphNode); 
				displayText = "<html><center>" + Language.translate("No NetworkComponentAdapter\nwas defined for\n the GraphNodes of the Domain", Language.EN) + " '" + domain + "'!<br><br> </center></html>";
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
	private JPanel getJContentPane() {
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
	 * @param dataModelBase64 the data model
	 * @return the hash code vector from data model
	 */
	private Vector<Integer> getHashCodeVectorFromDataModel(Vector<String> dataModelBase64) {
		
		if (dataModelBase64==null) return null;
		
		Vector<Integer> hashCodeVector = new Vector<Integer>();
		// --- Data model is an object array ----------
		for (String singleDataModel : dataModelBase64) {
			int singleDataModelHashCode = 0;
			if (singleDataModel!=null) {
				singleDataModelHashCode = singleDataModel.hashCode();	
			}
			hashCodeVector.add(singleDataModelHashCode);	
		}
		return hashCodeVector;
	}
	
	/**
	 * Sets the initial hash codes for a given DataModel that is Base64 encoded.
	 * @param dataModelBase64 the new data model base64 initial hash codes
	 */
	private void setDataModelBase64InitialHashCodes(Vector<String> dataModelBase64) {
		this.dataModelInitialHashCodes = this.getHashCodeVectorFromDataModel(dataModelBase64);
	}
	/**
	 * Checks if the current settings have changed.
	 * @return true, if the data model was changed
	 */
	private boolean hasChanged() {
		
		boolean changed = false;
		NetworkComponentAdapter4DataModel nca4dm = this.getNetworkComponentAdapter4DataModel();
		if (nca4dm!=null) {
			nca4dm.save();
			this.newDataModel = nca4dm.getDataModel();
			this.newDataModelBase64 = nca4dm.getDataModelBase64Encoded(this.newDataModel);
			Vector<Integer> newHashCodes = this.getHashCodeVectorFromDataModel(newDataModelBase64);
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
	 * Does the close action.
	 */
	private void doClose() {
		
		if (this.hasChanged()==true) {
			// --- Data model has changed ! ---------------------------
			String diaTitle = Language.translate("Close Properties", Language.EN);
			String diaQuestion = null;
			if (this.graphController.getProject()!=null) {
				// --- Setup case -------------
				diaQuestion = Language.translate("Save changes to network model?", Language.EN);
			} else {
				// --- Execution case ---------
				diaQuestion = Language.translate("Save and send data model changes to agent(s)?", Language.EN);
			}

			// --- User request ---------------------------------------
			int diaAnswer = JOptionPane.showConfirmDialog(this, diaQuestion, diaTitle, JOptionPane.YES_NO_CANCEL_OPTION);
			if (diaAnswer==JOptionPane.YES_OPTION) {
				if (this.graphController.getProject()!=null) {
					// --- Setup case -------------
					this.save();	
				} else {
					// --- Execution case ---------
					this.save(true);
				}
				this.setVisible(false);
				this.dispose();
				
			} else if (diaAnswer==JOptionPane.NO_OPTION){
				this.setVisible(false);
				this.dispose();
				
			} else if (diaAnswer==JOptionPane.CANCEL_OPTION){
				// --- Do nothing ---- 
			}
			
		} else {
			// --- Data model has NOT changed ! ---
			this.setVisible(false);
			this.dispose();
		}
	}
	
	/**
	 * Saves the current settings without sending them to the agent(s).
	 */
	private void save() {
		this.save(false);
	}
	/**
	 * Saves the current settings.
	 * @param sendChangesToAgent set true, if you want to send the changes to the agent(s) during execution
	 */
	private void save(boolean sendChangesToAgent) {
		
		NetworkComponentAdapter4DataModel nca4dm = this.getNetworkComponentAdapter4DataModel();
		if (nca4dm!=null) {
			nca4dm.save();
			this.newDataModel = nca4dm.getDataModel();
			this.newDataModelBase64 = nca4dm.getDataModelBase64Encoded(this.newDataModel);
			
			if (this.graphNode!=null) {
				this.graphNode.setDataModel(this.newDataModel);
				this.graphNode.setDataModelBase64(this.newDataModelBase64);
			} else {
				this.networkComponent.setDataModel(this.newDataModel);
				this.networkComponent.setDataModelBase64(this.newDataModelBase64);
			}
			
			if (this.graphController.getProject()!=null) {
				// --- Setup case -------------------
				this.graphController.setProjectUnsaved();
				
			} else if (this.graphController.getProject()==null && sendChangesToAgent==true) {
				// --- Execution case ---------------			
				this.sendChangesToAgent();
			}			
		}
		
	}

	/**
	 * Send changes to agent.
	 */
	private void sendChangesToAgent() {
		
		DataModelNotification dmNote = null;
		HashSet<NetworkComponent> comps = new HashSet<NetworkComponent>();
		if (this.graphNode!=null) {
			dmNote = new DataModelNotification(this.graphNode, this.graphController.getNetworkModel());
			// --- Get the NetworkComponent's connected to this GraphNode -----
			comps = this.graphController.getNetworkModel().getNetworkComponents(this.graphNode);
			
		} else {
			dmNote = new DataModelNotification(this.networkComponent, this.graphController.getNetworkModel());
			comps.add(this.networkComponent);
		}
		
		if (dmNote!=null) {
			for (NetworkComponent netComp : comps) {
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
	 * @param dmn the DataModelNotification
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
			// -- Update the model of the current GraphNode ? -------------
			if (dmn.isUseDataModelBase64Encoded()==true) {
				dataModel = this.getNetworkComponentAdapter4DataModel().getDataModelBase64Decoded(dmn.getGraphNode().getDataModelBase64());
			}
			this.getNetworkComponentAdapter4DataModel().setDataModel(dataModel);
			this.setDataModelBase64InitialHashCodes(getNetworkComponentAdapter4DataModel().getDataModelBase64Encoded(getNetworkComponentAdapter4DataModel().getDataModel()));
		}
		
		if (dmn.isForNetworkComponent(this.networkComponent)==true) {
			Object dataModel = dmn.getNetworkComponent().getDataModel();
			// -- Update the model of the current NetworkComponent ? ------
			if (dmn.isUseDataModelBase64Encoded()==true) {
				dataModel = this.getNetworkComponentAdapter4DataModel().getDataModelBase64Decoded(dmn.getNetworkComponent().getDataModelBase64());
			}
			this.getNetworkComponentAdapter4DataModel().setDataModel(dataModel);
			this.setDataModelBase64InitialHashCodes(getNetworkComponentAdapter4DataModel().getDataModelBase64Encoded(getNetworkComponentAdapter4DataModel().getDataModel()));
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
				// --- Update the view --------------------
				OntologyInstanceViewer ontoViewer = (OntologyInstanceViewer) this.getNetworkComponentAdapter4DataModel().getVisualizationComponent(this);
				uds.applyToOntologyInstanceViewer(ontoViewer);
				
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
			if (this.graphController.getProject()!=null) {
				this.save();	
			} else {
				this.save(true);
			}
			// --- Close, if 'Save and Exit' ------------------------
			if (actionCommand.equals("SaveAndExit")) {
				this.setVisible(false);
				this.dispose();
			
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

	
	/**
	 * The Class JToolBarButton.
	 */
	private class JToolBarButton extends JButton {

		/** The Constant serialVersionUID. */
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
