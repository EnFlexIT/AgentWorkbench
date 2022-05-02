package de.enflexit.awb.ws.ui.server;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.AwbWebRegistry;
import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.core.JettyServerInstances;
import de.enflexit.awb.ws.core.JettyServerManager;

/**
 * The Class ServerTree.
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
			rootTreeNode = this.createTreeNode(new ServerTreeNodeRoot());
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
		this.expandAll();
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
		
		// --- Add a server node ----------------------------
		DefaultMutableTreeNode serverNode = this.createTreeNode(new ServerTreeNodeServerService(serverService, serverInstances)); 
		this.getRootTreeNode().add(serverNode);
		
		// --- Add the Handler to the server node -----------
		for (int i = 0; i < handlerServiceList.size(); i++) {
			AwbWebHandlerService handlerService = handlerServiceList.get(i);
			serverNode.add(this.createTreeNode(new ServerTreeNodeHandlerService(handlerService)));
		}
	}
	private DefaultMutableTreeNode createTreeNode(ServerTreeNodeObject nodeObject) {
		return new DefaultMutableTreeNode(nodeObject);
	}
	
	/**
	 * Expand all nodes.
	 */
	private void expandAll() {
		for (int i = 0; i < this.getRowCount(); i++) {
	         this.expandRow(i);
		}
	}
	/**
	 * Expand.
	 *
	 * @param node the node
	 * @param childLevels the child levels
	 */
	private void expand(DefaultMutableTreeNode node, int childLevels) {
        this.expandAll();
        if (childLevels == 0) return;
        if (childLevels != -1) childLevels--;
        for (int x = 0; x < this.getTreeModelServer().getChildCount(node); x++) {
        	DefaultMutableTreeNode child = (DefaultMutableTreeNode) this.getTreeModelServer().getChild(node, x);
            this.expand(child, childLevels);
        }
    }
	/**
	 * Returns a TreePath for the specified node.
	 * @param nodeToSelect the node to select
	 * @return the tree path to node or null
	 */
	private TreePath getTreePathToNode(DefaultMutableTreeNode nodeToSelect) {

		if (nodeToSelect==null) return null;
		
		ArrayList<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
		DefaultMutableTreeNode nodeWork = nodeToSelect;
		while (nodeWork!=this.getRootTreeNode()) {
			list.add(0, nodeWork);
			nodeWork = (DefaultMutableTreeNode) nodeWork.getParent();
		}
		list.add(0, nodeWork);
		return new TreePath(list.toArray());
	}
	
}
