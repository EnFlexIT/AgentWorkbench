package org.awb.env.networkModel.controller.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.environment.EnvironmentPanel;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.awb.env.networkModel.ClusterNetworkComponent;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.visualisation.notifications.DataModelOpenViewNotification;

import de.enflexit.language.Language;

/**
 * The GUI for a GraphEnvironmentController. This contains a pane showing the NetworkComponents table and the BasicGraphGUI. The main class which associates with the components table, the environment
 * model and the Basic Graph GUI.
 * 
 * @see GraphEnvironmentController
 * @see BasicGraphGui
 * @see NetworkModel
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology - Guwahati
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphEnvironmentControllerGUI extends EnvironmentPanel implements Observer {

    private static final long serialVersionUID = 7376906096627051173L;

    private final String sourceTopologyTabName = "Topologie";
    
    private GraphEnvironmentController graphController;
    private Timer initVisTimer;
    
    private JComponent mainDisplayComponent;
    private boolean useTabs = false;
    private JTabbedPane jTabbedPaneAltNetModels;
    private HashMap<String, GraphEnvironmentControllerGUI> networkModelTabs;
    
    private BasicGraphGuiJDesktopPane basicGraphGuiJDesktopPane;
    private BasicGraphGuiRootJSplitPane graphGUI;

    
    /**
     * This is the default constructor for just displaying the current environment model during a running simulation.
     * @param graphController the current EnvironmentController {here a @link GraphEnvironmentController}
     */
    public GraphEnvironmentControllerGUI(GraphEnvironmentController graphController) {
		super(graphController);
		this.graphController = graphController;
		this.initialize();
    }
    
    /**
     * This method initializes this
     * @return void
     */
    private void initialize() {
		
    	this.setLayout(new BorderLayout());
		this.add(this.getJComponentMainDisplay(), BorderLayout.CENTER);
		this.setAlternativeNetworkModels();
		
		// --- Start the initial visualization timer? -----
		if (this.graphController.getNetworkModel().getNetworkComponents().size()>0) {
    		this.getInitVisTimer().start();
    	}
    }
    /**
     * Returns the initial visualization timer.
     * @return the initial visualization timer.
     */
    public Timer getInitVisTimer() {
    	if (initVisTimer==null) {
    		initVisTimer = new Timer(50, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					GraphEnvironmentControllerGUI.this.invokeNetworkModelReload();
				}
			});
    	}
		return initVisTimer;
	}
    /**
     * Invokes the network model reload or restart the local timer.
     */
    private void invokeNetworkModelReload() {
    	
    	if (this.getParent()!=null && this.isVisible()==true) {
    		// --- Refresh the view and terminate the timer ---------
			this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Reload));
			this.getInitVisTimer().stop();
			this.initVisTimer = null;
    	} else {
    		// --- Not visible yet - restart timer ------------------
    		this.getInitVisTimer().restart();
    	}
    }

    
    
    /**
     * ReLoads the network model.
     */
    private void reLoad() {
    	// --- Close all property windows -----------------
    	this.getBasicGraphGuiJDesktopPane().closeAllBasicGraphGuiProperties();
    	// --- Set alternative NetworkModel's -------------
    	this.setAlternativeNetworkModels();
    }
    
    /**
     * Returns the graph environment controller.
     * @return the graph environment controller
     */
    private GraphEnvironmentController getGraphController() {
    	return (GraphEnvironmentController) this.environmentController;
    }

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentPanel#dispose()
	 */
	@Override
	public void dispose() {
		
		if (this.networkModelTabs!=null) {
			// --- Dispose / Remove sub tabs ----------------------------------
			HashSet<String> tabNames = new HashSet<String>(this.networkModelTabs.keySet()); 
			for (String tabName : tabNames) {
				GraphEnvironmentControllerGUI graphGUI = this.networkModelTabs.get(tabName);
				if (graphGUI!=this) {
					graphGUI.dispose();
					this.getJTabbedPaneAltNetModels().remove(graphGUI);
					this.networkModelTabs.remove(tabName);	
				}
			}
		}

		// --- Kill the current GraphGui --------------------------------------
		if (this.graphGUI!=null) {
			this.graphGUI.dispose();	
		}
		
		// --- Remove from the parent component -------------------------------
		if (this.getParent()!=null) {
			this.getParent().remove(this);
		}
		
	}
    
    /**
     * Gets the JTabbedPane for alternative network models.
     * @return the JTabbedPane for alternative network models
     */
    private JTabbedPane getJTabbedPaneAltNetModels() {
		if (jTabbedPaneAltNetModels == null) {
		    jTabbedPaneAltNetModels = new JTabbedPane();
		    jTabbedPaneAltNetModels.setFont(new Font("Dialog", Font.BOLD, 13));
		    
		    // --- Initialize the Tab-Reminder ------------
		    this.networkModelTabs = new HashMap<String, GraphEnvironmentControllerGUI>();
	
		    // --- Display the normal topology ------------
		    this.jTabbedPaneAltNetModels.addTab(Language.translate(this.sourceTopologyTabName), this.getBasicGraphGuiJDesktopPane());
		    this.networkModelTabs.put(Language.translate(this.sourceTopologyTabName), this);
	
		}
		return jTabbedPaneAltNetModels;
    }
   
    /**
     * Returns the main display component.
     * @return the main display component
     */
    private JComponent getJComponentMainDisplay() {
    
    	if (this.mainDisplayComponent==null) {
    		if (this.getGraphController().getNetworkModel()==null || this.getGraphController().getNetworkModel().getAlternativeNetworkModel().size()==0) {
    			this.mainDisplayComponent = this.getBasicGraphGuiJDesktopPane();
    		    this.useTabs = false;
    		} else {
    		    this.mainDisplayComponent = this.getJTabbedPaneAltNetModels();
    		    this.useTabs = true;
    		}
    	}
    	return this.mainDisplayComponent;
    }
    /**
     * Sets to use tabs or not.
     * @param use the new use
     */
    public void setUseTabs(boolean use) {

		if (this.useTabs!=use) {
		    // --- Change the appearance of this tab ----------------
		    this.useTabs = use;
		    this.remove(this.mainDisplayComponent);
		    if (this.useTabs==true) {
				// --- NO tabs were displayed yet => show them now --
				this.mainDisplayComponent = this.getJTabbedPaneAltNetModels();
				
		    } else {
				// --- Tabs are displayed => remove them now --------
		    	if (this.jTabbedPaneAltNetModels!=null) {
		    		this.jTabbedPaneAltNetModels = null;
		    	}
		    	this.mainDisplayComponent = this.getBasicGraphGuiJDesktopPane();
		    	
		    }
		    
		    // --- Finally ------------------------------------------
		    this.add(this.mainDisplayComponent, null);
		    this.validate();
		    this.repaint();
		}
		
    }

    /**
     * Sets the focus on the specified alternative tab.
     * @param tabTitle2Focus the title, where the focus has to set on
     */
    private void setFocusOnAlternativeTab(String tabTitle2Focus) {
    	
    	// --- Try parent element -------------------------
    	if (this.getParent() instanceof JTabbedPane) {
    		JTabbedPane tabs = (JTabbedPane) this.getParent();
    		for (int i = 0; i < tabs.getTabCount(); i++) {
    			if (tabs.getTitleAt(i).equals(tabTitle2Focus)) {
    				tabs.setSelectedIndex(i);
    				return;
    			}
			}
    	}
    	
    	// --- Try current JTabbedPane -------------------- 
    	if (this.jTabbedPaneAltNetModels!=null) {
    		JTabbedPane tabs = this.getJTabbedPaneAltNetModels();
    		for (int i = 0; i < tabs.getTabCount(); i++) {
    			if (tabs.getTitleAt(i).equals(tabTitle2Focus)) {
    				tabs.setSelectedIndex(i);
    				return;
    			}
    		}	
    	}
    }
    
    /**
     * Returns the BasicGraphGuiJDesktopPane
     * @return the BasicGraphGuiJDesktopPane
     */
    public BasicGraphGuiJDesktopPane getBasicGraphGuiJDesktopPane() {
    	if (basicGraphGuiJDesktopPane==null) {
    		basicGraphGuiJDesktopPane = new BasicGraphGuiJDesktopPane((GraphEnvironmentController) this.environmentController);
    		basicGraphGuiJDesktopPane.add(this.getBasicGraphGuiRootJSplitPane(), JLayeredPane.DEFAULT_LAYER);
    		
    		// --- Set internal frame size ----------------
    		Dimension internalFrameSize = new Dimension(800, 600);
    		if (Application.isOperatingHeadless()==false && Application.isMainWindowInitiated()==true) {
    			Dimension projectDesktopSize = Application.getMainWindow().getProjectDesktop().getSize();
    			internalFrameSize.width  = (int) (projectDesktopSize.getWidth()  - 230.0);
    			internalFrameSize.height = (int) (projectDesktopSize.getHeight() - 60.0);
    		}
    		this.getBasicGraphGuiRootJSplitPane().setLocation(0, 0);
    		this.getBasicGraphGuiRootJSplitPane().setSize(internalFrameSize);
    		
    		// --- Maximize the internal frame ------------ 
    		SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
		    		try {
						GraphEnvironmentControllerGUI.this.getBasicGraphGuiRootJSplitPane().setMaximum(true);
					} catch (PropertyVetoException pvex) {
						pvex.printStackTrace();
					}
				}
			});
    		
    	}
    	return basicGraphGuiJDesktopPane;
    }
    
    /**
     * Returns the BasicGraphGuiRootJSplitPane.
     * @return the BasicGraphGuiRootJSplitPane
     */
    public BasicGraphGuiRootJSplitPane getBasicGraphGuiRootJSplitPane() {
		if (this.graphGUI == null) {
			this.graphGUI = new BasicGraphGuiRootJSplitPane(this.getGraphController());
		}
		return this.graphGUI;
    }

    /**
     * Sets the network model to the display.
     * @param nm the new NetworkModel
     */
    private void setAlternativeNetworkModels() {
	
    	NetworkModel nm = this.getGraphController().getNetworkModel();
		if (nm!=null && nm.getAlternativeNetworkModel().size() > 0) {
			
			this.setUseTabs(true);
			// --------------------------------------------------------------------------
		    // --- Alternative NetworkModel's has to be displayed -----------------------
		    // --------------------------------------------------------------------------
			HashMap<String, NetworkModel> altNetModelHash = nm.getAlternativeNetworkModel();
		    // --- Get the model names ordered ------------------------------------------
		    Vector<String> altNetModelTabsToDelete = new Vector<String>(this.networkModelTabs.keySet());
			Vector<String> altNetModels = new Vector<String>(altNetModelHash.keySet());
		    Collections.sort(altNetModels);
	
		    for (int i = 0; i < altNetModels.size(); i++) {
	
				String altNetModelName = altNetModels.get(i);
				NetworkModel altNetModel = altNetModelHash.get(altNetModelName);
		
				GraphEnvironmentControllerGUI graphControllerGUI = null;
				GraphEnvironmentController graphController = null;
		
				if (this.networkModelTabs.get(altNetModelName) == null) {
				    // --- Create new controller for alternative NetworkModel -----------
			    	graphController = new GraphEnvironmentController();
			    	graphController.setDisplayEnvironmentModel(altNetModel);
			    	graphControllerGUI = (GraphEnvironmentControllerGUI) graphController.getOrCreateEnvironmentPanel();
			    	
			    	this.getJTabbedPaneAltNetModels().addTab(altNetModelName, graphControllerGUI);
			    	this.networkModelTabs.put(altNetModelName, graphControllerGUI);
			    	
				} else {
				    // --- Get the Controller for the alternative NetworkModel ----------
				    graphControllerGUI = this.networkModelTabs.get(altNetModelName);
				    graphController = graphControllerGUI.getGraphController();
				    graphController.setDisplayEnvironmentModel(altNetModel);

				    // --- Set the appearance of the GUI to use or not use a JTabbedPane ----
					if (altNetModel.getAlternativeNetworkModel().size()==0) {
					    graphControllerGUI.setUseTabs(false);
					} else {
					    graphControllerGUI.setUseTabs(true);
					}
				    
				}
		
				// --- Remove the current tab name from the list of Tabs to delete ------
				altNetModelTabsToDelete.remove(altNetModelName);
				
		    }//end for
		    
		    // --- Delete Tabs that are not longer element of the alternative Models ---- 
		    for (String tabToDelete : altNetModelTabsToDelete) {
		    	if (tabToDelete.equals(Language.translate(this.sourceTopologyTabName))==false) {
		    		GraphEnvironmentControllerGUI graphControllerGUI = this.networkModelTabs.get(tabToDelete);
			    	if (graphControllerGUI!=null) {
			    		this.jTabbedPaneAltNetModels.remove(graphControllerGUI);
				    	this.networkModelTabs.remove(tabToDelete);	
				    	graphControllerGUI.dispose();
				    	graphControllerGUI = null;
			    	}	
		    	}
		    }
		    this.getJComponentMainDisplay().validate();
		   
		    // --------------------------------------------------------------------------
		    // --------------------------------------------------------------------------
		}
    }

    /**
     * This is the central method that allows the editing of component settings.
     * @param selectedGraphObject the object, where the settings should be edited.
     */
    private void editComponentSettings(Object selectedGraphObject) {

    	if (selectedGraphObject==null) return;
    	
    	ClusterNetworkComponent cnc = this.evaluateForClusterNetworkComponent(selectedGraphObject);
    	if (cnc!=null) {
			this.setFocusOnAlternativeTab(cnc.getId());
    	} else {
    		if (selectedGraphObject instanceof NetworkComponent) {
    			// --- Check if selection requires a switch to GraphNode ------
    			NetworkComponent netComp = (NetworkComponent) selectedGraphObject;
    			boolean isUsesNetCompToGraphNodeAdapter = this.getGraphController().getNetworkModel().isUsesNetworkComponentToGraphNodeAdapter(netComp);
    			if (isUsesNetCompToGraphNodeAdapter==true) {
    				GraphNode graphNode = this.getGraphController().getNetworkModel().getGraphNodeFromDistributionNode(netComp);
    				selectedGraphObject = graphNode;
    			}
    		}
    		// --- Open or focus the property editor --------------------------
    		BasicGraphGuiProperties propWindow = this.getBasicGraphGuiJDesktopPane().getEditorBySelectedGraphObject(selectedGraphObject);
    		if (propWindow==null) {
    			new BasicGraphGuiProperties(this.getGraphController(), selectedGraphObject);
    		} else {
    			propWindow.registerAtDesktopAndSetVisible();
    		}
    	}
    }
   
    /**
     * Evaluates a graph object to be a ClusterNetworkComponent or not.
     *
     * @param graphObject the graph object
     * @return the cluster network component
     */
    private ClusterNetworkComponent evaluateForClusterNetworkComponent(Object graphObject) {
    	
    	ClusterNetworkComponent cnc = null;
    	Object checkObject = null;
		if (graphObject instanceof GraphNode) {
			List<NetworkComponent> netComps = this.getGraphController().getNetworkModel().getNetworkComponents((GraphNode) graphObject);
			if (netComps.size()==1) {
				checkObject = netComps.iterator().next();
			}
		} else if (graphObject instanceof GraphEdge) {
			checkObject = this.getGraphController().getNetworkModel().getNetworkComponent((GraphEdge) graphObject);
		} else {
			checkObject = graphObject;
		}
		
		// --- Finally do the Cast ---------------
		if (checkObject!=null) {
			if (checkObject instanceof ClusterNetworkComponent) {
				cnc = (ClusterNetworkComponent) checkObject;
			}	
		}
    	return cnc;
    }
	
    /*
     * (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable observable, Object object) {
    	
    	if (object instanceof NetworkModelNotification) {
    		
    		NetworkModelNotification nmNotification = (NetworkModelNotification) object;
    		int reason = nmNotification.getReason();
    		Object infoObject = nmNotification.getInfoObject();
    		
    		switch (reason) {
    		case NetworkModelNotification.NETWORK_MODEL_ComponentTypeSettingsChanged:
    		case NetworkModelNotification.NETWORK_MODEL_Reload:
    		case NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel:
    			this.reLoad();
    			break;
				
    		case NetworkModelNotification.NETWORK_MODEL_Repaint:
    			if (infoObject instanceof DataModelOpenViewNotification) {
    				// --- Open the view to a specified data model --
    				DataModelOpenViewNotification dmovn = (DataModelOpenViewNotification) infoObject;
    				if (dmovn.isEmpty()==false) {
    					if (dmovn.isGraphNodeView()) {
    						GraphNode graphNode = (GraphNode) this.getGraphController().getNetworkModel().getGraphElement(dmovn.getGraphNodeID());
    						this.editComponentSettings(graphNode);
    					} else if (dmovn.isNetworkComponentView()) {
    						NetworkComponent netComp = this.getGraphController().getNetworkModel().getNetworkComponent(dmovn.getNetworkComponentID());
    						this.editComponentSettings(netComp);
    					}
    				}
    			}
    			break;
				
			case NetworkModelNotification.NETWORK_MODEL_EditComponentSettings:
				this.editComponentSettings(infoObject);
				break;
			
			default:
				break;
			}
    		
    	}
    	
    }

} // @jve:decl-index=0:visual-constraint="33,19"
