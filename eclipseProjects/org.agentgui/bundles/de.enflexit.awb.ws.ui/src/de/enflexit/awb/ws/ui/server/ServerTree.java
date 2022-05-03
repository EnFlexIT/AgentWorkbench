package de.enflexit.awb.ws.ui.server;

import java.util.List;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.AwbWebRegistry;
import de.enflexit.awb.ws.AwbWebServerService;
import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.core.JettyServerInstances;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject;
import de.enflexit.awb.ws.core.model.HandlerHelper;
import de.enflexit.awb.ws.core.model.ServerTreeNodeHandler;
import de.enflexit.awb.ws.core.model.ServerTreeNodeRoot;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;

/**
 * The Class ServerTree is used in {@link JPanelServerConfiguration} to show the
 * composition of all AWB-registered server.
 * 
 * @see AwbWebServerService
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTree extends JTree {

	private static final long serialVersionUID = 3075920850705647191L;
	
	private DefaultMutableTreeNode rootTreeNode;
	private DefaultTreeModel treeModelServer;
	
	private ServerTreeCellRenderer serverTreeCellRenderer;
	
	
	/**
	 * Instantiates a new server tree.
	 */
	public ServerTree() {
		this.setModel(this.getTreeModelServer());
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setCellRenderer(this.getServerTreeCellRenderer());
		ToolTipManager.sharedInstance().registerComponent(this);
		this.refreshTreeModel();
	}
	
	private DefaultMutableTreeNode getRootTreeNode() {
		if (rootTreeNode==null) {
			rootTreeNode = new DefaultMutableTreeNode(new ServerTreeNodeRoot());
		}
		return rootTreeNode;
	}
	private DefaultTreeModel getTreeModelServer() {
		if (treeModelServer==null) {
			treeModelServer = new DefaultTreeModel(this.getRootTreeNode());
		}
		return treeModelServer;
	}

	private ServerTreeCellRenderer getServerTreeCellRenderer() {
		if (serverTreeCellRenderer==null) {
			serverTreeCellRenderer = new ServerTreeCellRenderer();
		}
		return serverTreeCellRenderer;
	}
	
	
	/**
	 * Returns the current {@link JettyServerManager} instance.
	 * @return the server manager
	 */
	private JettyServerManager getServerManager() {
		return JettyServerManager.getInstance();
	}
	/**
	 * Returns the current {@link AwbWebRegistry} of the {@link JettyServerManager}.
	 * @return the AwbWebRegistry
	 */
	private AwbWebRegistry getAwbWebRegistry() {
		return this.getServerManager().getAwbWebRegistry();
	}
	
	
	/**
	 * Will refresh / rebuild the current TreeModel.
	 */
	private void refreshTreeModel() {
		this.getRootTreeNode().removeAllChildren();
		this.fillTreeModel();
	}
	/**
	 * Fills the tree model.
	 */
	private void fillTreeModel() {

		// --- Get all registered server services, sorted by name ---
		List<AwbWebServerServiceWrapper> serverServices = this.getAwbWebRegistry().getRegisteredWebServerSorted();
		for (int i = 0; i < serverServices.size(); i++) {
			// --- Get the service entry ----------------------------
			AwbWebServerServiceWrapper serverService = serverServices.get(i);
			String serverName = serverService.getJettyConfiguration().getServerName();
			// --- Get the registered handler for the server --------
			List<AwbWebHandlerService> handlerServices = this.getAwbWebRegistry().getAwbWebHandlerServiceSorted(serverName);
			// --- If started, get all relevant server instances ----
			JettyServerInstances serverInstances = this.getServerManager().getServerInstances(serverName);
			
			// --- Add to tree model --------------------------------
			this.addServerToTreeModel(serverService, handlerServices, serverInstances);
		}
		this.expandToContextPath();
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}
	/**
	 * Adds the specified server elements to the tree model.
	 *
	 * @param serverService the server service
	 * @param handlerServiceList the handler services
	 * @param serverInstances the server instances
	 */
	private void addServerToTreeModel(AwbWebServerServiceWrapper serverService, List<AwbWebHandlerService> handlerServiceList, JettyServerInstances serverInstances) {
		
		// --- Add a server node --------------------------
		DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(new ServerTreeNodeServer(serverService, serverInstances)); 
		this.getRootTreeNode().add(serverNode);
		
		// --- Join the list of services and handler ------ 
		List<DefaultMutableTreeNode> handlerNodes = HandlerHelper.getHandlerTrees(serverInstances, handlerServiceList);
		for (DefaultMutableTreeNode handlerNode : handlerNodes) {
			serverNode.add(handlerNode);
		}
	}
	
	
	/**
	 * Expands all tree nodes.
	 */
	private void expandAll() {
		for (int i = 0; i < this.getRowCount(); i++) {
	         this.expandRow(i);
		}
	}
	/**
	 * Expand to context path.
	 */
	private void expandToContextPath() {

		// --- Expand all nodes -------------------------------------
		this.expandAll();
		
		// --- Check if current node has a context path -------------
		for (int i=this.getRowCount()-1; i>=0; i--) {
			TreePath treePath = this.getPathForRow(i);
			// --- Get node instance and user object ---------------- 
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			AbstractServerTreeNodeObject nodeObject = (AbstractServerTreeNodeObject) treeNode.getUserObject();
			if (nodeObject instanceof ServerTreeNodeHandler) {
				ServerTreeNodeHandler stnHandler = (ServerTreeNodeHandler) nodeObject;
				// --- Check to collapse the current tree path ------
				if (stnHandler.getContextPath().isEmpty()==false ) {
					this.collapsePath(treePath);
				}
			}
		}
	}
	
	
}
