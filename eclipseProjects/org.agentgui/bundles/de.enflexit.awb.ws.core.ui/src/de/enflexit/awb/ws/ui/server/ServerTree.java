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
import de.enflexit.awb.ws.core.model.ServerTreeNodeServerSecurity;

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
	public void refreshTreeModel() {
		this.clearSelection();
		this.getRootTreeNode().removeAllChildren();
		this.getTreeModelServer().reload();
		this.fillTreeModel();
	}
	/**
	 * Fills the tree model.
	 */
	private void fillTreeModel() {

		// --- Initially show root node -----------------------------
		this.setRootVisible(true);
		
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
		
		// --- Hide root node ---------------------------------------
		this.setRootVisible(false);
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
		ServerTreeNodeServer stnServer = new ServerTreeNodeServer(serverService, serverInstances);
		DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(stnServer); 
		this.getTreeModelServer().insertNodeInto(serverNode, this.getRootTreeNode(), this.getRootTreeNode().getChildCount());
		// --- Add server security node -------------------
		serverNode.add(new DefaultMutableTreeNode(new ServerTreeNodeServerSecurity(stnServer)));
		// --- Add the handler to the server node --------- 
		HandlerHelper.getHandlerTrees(serverInstances, handlerServiceList).forEach((DefaultMutableTreeNode treeNode) -> serverNode.add(treeNode));
	}
	
	// --------------------------------------------------------------
	// --- From here some helper methods ----------------------------
	// --------------------------------------------------------------
	/**
	 * Returns the currently selected server tree node object.
	 * @return the server tree node object {@link AbstractServerTreeNodeObject}
	 */
	public AbstractServerTreeNodeObject getServerTreeNodeObjectSelected() {
		return this.getServerTreeNodeObject(this.getSelectionPath());
	}
	/**
	 * Returns the server tree node object for the specified TreePath.
	 *
	 * @param path the TreePath
	 * @return the server tree node object {@link AbstractServerTreeNodeObject}
	 */
	public AbstractServerTreeNodeObject getServerTreeNodeObject(TreePath path) {
		if (path==null) return null;
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		return (AbstractServerTreeNodeObject) treeNode.getUserObject();
	}
	/**
	 * Returns the TreePath for the specified tree node object (see {@link AbstractServerTreeNodeObject}).
	 *
	 * @param stnoSearch the server tree node object to search for
	 * @return the tree path for server tree node object
	 */
	public TreePath getTreePathForServerTreeNodeObject(AbstractServerTreeNodeObject stnoSearch) {
		
		for (int i = 0; i < this.getRowCount(); i++) {
			TreePath treePath = this.getPathForRow(i);
			AbstractServerTreeNodeObject nodeObject = this.getServerTreeNodeObject(treePath);
			if (nodeObject==stnoSearch) {
				return treePath;
			}
		}
		return null;
	}
	/**
	 * Returns the parent server node for the specified ServerTreeNodeHandler.
	 *
	 * @param serverTreeNodeHandler the server tree node handler
	 * @return the parent server node
	 */
	public ServerTreeNodeServer getParentServerNode(ServerTreeNodeHandler serverTreeNodeHandler) {
		
		TreePath treePathNode = this.getTreePathForServerTreeNodeObject(serverTreeNodeHandler);
		while (treePathNode!=null && (! (this.getServerTreeNodeObject(treePathNode) instanceof ServerTreeNodeServer))) {
			treePathNode = treePathNode.getParentPath();
		}
		if (treePathNode!=null) {
			return (ServerTreeNodeServer) this.getServerTreeNodeObject(treePathNode);
		}
		return null;
	}
	/**
	 * Returns the parent server node for the specified ServerTreeNodeServerSecurity.
	 *
	 * @param serverTreeNodeServerSecurity the server tree node server security
	 * @return the parent server node
	 */
	public ServerTreeNodeServer getParentServerNode(ServerTreeNodeServerSecurity serverTreeNodeServerSecurity) {
		
		TreePath treePathNode = this.getTreePathForServerTreeNodeObject(serverTreeNodeServerSecurity);
		while (treePathNode!=null && (! (this.getServerTreeNodeObject(treePathNode) instanceof ServerTreeNodeServer))) {
			treePathNode = treePathNode.getParentPath();
		}
		if (treePathNode!=null) {
			return (ServerTreeNodeServer) this.getServerTreeNodeObject(treePathNode);
		}
		return null;
	}
	
	/**
	 * Select first server node.
	 */
	public void selectFirstServerNode() {
		TreePath fsnServer = this.getFirstServerNode();
		if (fsnServer!=null) {
			this.setSelectionPath(fsnServer);
		}
	}
	/**
	 * Selects the specified server node.
	 * @param serverName the server name
	 */
	public void selectServerNode(String serverName) {
		if (serverName==null) return;
		TreePath fsnServer = this.getServerNode(serverName);
		if (fsnServer!=null) {
			this.setSelectionPath(fsnServer);
		}
	}
	/**
	 * Selects the specified handler, specified by the service class name.
	 * @param serviceClassName the service class name
	 */
	public void selectHandlerNode(String serviceClassName) {
		if (serviceClassName==null) return;
		TreePath fsnServer = this.getHandlerNode(serviceClassName);
		if (fsnServer!=null) {
			this.setSelectionPath(fsnServer);
		}
	}
	/**
	 * Select security node that belongs to the specified server.
	 * @param serverName the server name
	 */
	public void selectSecurityNode(String serverName) {
		if (serverName==null) return;
		TreePath fsnServer = this.getSecurityNode(serverName);
		if (fsnServer!=null) {
			this.setSelectionPath(fsnServer);
		}
	}
	
	/**
	 * Returns the first server node.
	 * @return the first server node
	 */
	public TreePath getFirstServerNode() {
		TreePath fsnServerTreePath = null;
		for (int i = 0; i < this.getRowCount(); i++) {
			TreePath treePath = this.getPathForRow(i);
			AbstractServerTreeNodeObject nodeObject = this.getServerTreeNodeObject(treePath);
			if (nodeObject instanceof ServerTreeNodeServer) {
				fsnServerTreePath = treePath;
				break;
			}
		}
		return fsnServerTreePath;
	}
	/**
	 * Returns the TreePath of the specified server.
	 *
	 * @param serverName the server name
	 * @return the server node
	 */
	public TreePath getServerNode(String serverName) {
		TreePath fsnServerTreePath = null;
		for (int i = 0; i < this.getRowCount(); i++) {
			TreePath treePath = this.getPathForRow(i);
			AbstractServerTreeNodeObject nodeObject = this.getServerTreeNodeObject(treePath);
			if (nodeObject instanceof ServerTreeNodeServer) {
				ServerTreeNodeServer stnServer = (ServerTreeNodeServer) nodeObject;
				if (stnServer.getJettyConfiguration().getServerName().equals(serverName)==true) {
					fsnServerTreePath = treePath;
					break;
				}
			}
		}
		return fsnServerTreePath;
	}
	/**
	 * Returns the TreePath of the specified handler.
	 *
	 * @param serviceClassName the service class name
	 * @return the handler node
	 */
	public TreePath getHandlerNode(String serviceClassName) {
		TreePath fsnServerTreePath = null;
		for (int i = 0; i < this.getRowCount(); i++) {
			TreePath treePath = this.getPathForRow(i);
			AbstractServerTreeNodeObject nodeObject = this.getServerTreeNodeObject(treePath);
			if (nodeObject instanceof ServerTreeNodeHandler) {
				ServerTreeNodeHandler stnServer = (ServerTreeNodeHandler) nodeObject;
				if (stnServer.getServiceClassName().equals(serviceClassName)==true) {
					fsnServerTreePath = treePath;
					break;
				}
			}
		}
		return fsnServerTreePath;
	}
	/**
	 * Returns the TreePath of the security node of the specified server.
	 *
	 * @param serverName the server name
	 * @return the security node
	 */
	public TreePath getSecurityNode(String serverName) {
		
		TreePath fsnServerTreePath = this.getServerNode(serverName);
		if (fsnServerTreePath!=null) {
			// --- Check child nodes of server node -------
			DefaultMutableTreeNode serverNode = (DefaultMutableTreeNode) fsnServerTreePath.getLastPathComponent();
			for (int i = 0; i < serverNode.getChildCount(); i++) {
				DefaultMutableTreeNode serverChild = (DefaultMutableTreeNode) serverNode.getChildAt(i);
				if (serverChild.getUserObject() instanceof ServerTreeNodeServerSecurity) {
					return this.getTreePathForServerTreeNodeObject((AbstractServerTreeNodeObject) serverChild.getUserObject());
				}
			}
		}
		return null;
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
