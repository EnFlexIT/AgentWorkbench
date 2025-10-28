package org.awb.env.networkModel.controller.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import de.enflexit.common.Observable;
import de.enflexit.common.Observer;
import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.commands.RenamedNetworkComponent;

/**
 * The GUI for a GraphEnvironmentController. This contains a pane showing the NetworkComponents table and the BasicGraphGUI. 
 * The main class which associates with the components table, the environment model and the Basic Graph GUI.
 * 
 * @see GraphEnvironmentController
 * @see BasicGraphGui
 * @see org.awb.env.networkModel.helper
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology - Guwahati
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class BasicGraphGuiRootJSplitPane extends JInternalFrame implements ListSelectionListener, Observer {

    private static final long serialVersionUID = 7376906096627051173L;

    private GraphEnvironmentController graphController;
    
    private JSplitPane jSplitPaneRoot;
    private NetworkComponentTablePanel networkComponentTablePanel;
    private BasicGraphGui graphGUI;
    
    
    /**
     * This is the default constructor for just displaying the current environment model during a running simulation.
     * @param graphController the current {@link GraphEnvironmentController}
     */
    public BasicGraphGuiRootJSplitPane(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		this.graphController.addObserver(this);
		this.initialize();
    }
    
    /**
     * This method initializes this
     * @return void
     */
    private void initialize() {
	
		this.setLayout(new BorderLayout());
		
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setContentPane(this.getJSplitPaneRoot());
		
		this.setVisible(true);
		
    }
    /**
     * Returns the graph environment controller.
     * @return the graph environment controller
     */
    private GraphEnvironmentController getGraphController() {
    	return this.graphController;
    }

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentPanel#dispose()
	 */
	@Override
	public void dispose() {
		if (this.graphGUI!=null) {
			// --- Destroy the current GraphGui -----
			this.graphGUI.dispose();
			this.graphGUI = null;
		}
	}
    
    /**
     * This method initializes jSplitPaneRoot
     * @return javax.swing.JSplitPane
     */
    public JSplitPane getJSplitPaneRoot() {
		if (jSplitPaneRoot == null) {
		    jSplitPaneRoot = new JSplitPane();
		    jSplitPaneRoot.setOneTouchExpandable(true);
		    jSplitPaneRoot.setLeftComponent(this.getNetworkComponentTablePanel());
		    jSplitPaneRoot.setRightComponent(this.getBasicGraphGui());
		    jSplitPaneRoot.setDividerLocation(230);
		}
		return jSplitPaneRoot;
    }
    /**
     * Gets the BasicGraphGui, which is the visualization component for the graph.
     * @return the basic graph GUI that contains the graph visualization component
     */
    public BasicGraphGui getBasicGraphGui() {
		if (graphGUI == null) {
		    graphGUI = new BasicGraphGui(this.getGraphController());
		}
		return graphGUI;
    }
    
    /**
     * Gets the network component table panel.
     * @return the network component table panel
     */
    private NetworkComponentTablePanel getNetworkComponentTablePanel() {
    	if (networkComponentTablePanel==null) {
    		networkComponentTablePanel = new NetworkComponentTablePanel(this.getGraphController(), true, false);
    		networkComponentTablePanel.addListSelectionListener(this);
    		networkComponentTablePanel.addMouseListener(new MouseAdapter() {
		    	@Override
		    	public void mouseClicked(MouseEvent me) {
		    		if (me.getClickCount()==2) {
		    			NetworkComponent netCompSelected = getNetworkComponentTablePanel().getSelectedNetworkComponent();
		    			if (netCompSelected!=null) {
		    				getGraphController().getNetworkModelUndoManager().selectNetworkComponent(netCompSelected);
		    				getGraphController().getNetworkModelUndoManager().zoomNetworkComponent();
		    			}
		    			
		    		}
		    	}
			});
    	}
		return networkComponentTablePanel;
	}
    /* (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent le) {
    	NetworkComponent netCompSelected = this.getNetworkComponentTablePanel().getSelectedNetworkComponent();
		if (netCompSelected!=null) {
			this.getGraphController().getNetworkModelUndoManager().selectNetworkComponent(netCompSelected);	
		}
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
    		NetworkComponent networkComponent = null;
    		
    		switch (reason) {
    		case NetworkModelNotification.NETWORK_MODEL_ComponentTypeSettingsChanged:
    		case NetworkModelNotification.NETWORK_MODEL_Reload:
    		case NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel:
    			this.getNetworkComponentTablePanel().reLoadNetworkComponents();
    			this.getNetworkComponentTablePanel().applyTableSorter();
    			break;
				
    		case NetworkModelNotification.NETWORK_MODEL_Component_Added:
    			if (infoObject instanceof NetworkComponent) {
    				networkComponent = (NetworkComponent) infoObject;
    				this.getNetworkComponentTablePanel().addNetworkComponent(networkComponent);	
    			} else if (infoObject instanceof HashSet<?>) {
					@SuppressWarnings("unchecked")
					HashSet<NetworkComponent> networkComponentHash = (HashSet<NetworkComponent>) infoObject;
					this.getNetworkComponentTablePanel().addNetworkComponents(new ArrayList<>(networkComponentHash));
    			}
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Component_Removed:
				if (infoObject instanceof NetworkComponent) {
					networkComponent = (NetworkComponent) infoObject;
					this.getNetworkComponentTablePanel().removeNetworkComponent(networkComponent);
				
				} else if (infoObject instanceof List<?>) {
					@SuppressWarnings("unchecked")
					List<NetworkComponent> networkComponentList = (List<NetworkComponent>) infoObject;
					this.getNetworkComponentTablePanel().removeNetworkComponents(networkComponentList);
				}
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_Component_Selected:
				networkComponent = (NetworkComponent) infoObject;
				this.getNetworkComponentTablePanel().setSelectedNetworkComponent(networkComponent);
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_EditComponentSettings:
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_Component_Renamed:
				RenamedNetworkComponent renamed = (RenamedNetworkComponent) infoObject;
				if (renamed!=null) {
					this.getNetworkComponentTablePanel().renameNetworkComponent(renamed.getOldNetworkComponentID(), renamed.getNewNetworkComponentID());
				}
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_EdgeEditing:
				// --- Disable to edit elements in the table --------
				this.getNetworkComponentTablePanel().setRenamingEnabled(false);
				break;
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
				// --- Enable to edit elements in the table ---------
				this.getNetworkComponentTablePanel().setRenamingEnabled(true);
				break;
				
			default:
				break;
			}
    	}
    	
    }

}  
