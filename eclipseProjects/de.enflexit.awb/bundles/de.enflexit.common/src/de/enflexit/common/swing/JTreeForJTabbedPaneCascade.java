package de.enflexit.common.swing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * The Class JTreeForJTabbedPaneCascade.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JTreeForJTabbedPaneCascade extends JTree implements ChangeListener, TreeSelectionListener {

	private static final long serialVersionUID = -4074597995956877608L;

	private boolean isDebugJComponent = false;
	private boolean isDebugJTabbedPane = false;
	
	private JComponent mainJComponent;
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	
	private boolean isPauseChangeListener;
	private boolean isPauseTreeSelectionListener;
	
	/**
	 * Instantiates a new JTree for a cascade of tabs within JTabbedPane
	 * @param mainJComponent the main J component
	 */
	public JTreeForJTabbedPaneCascade(JComponent mainJComponent) {
		this.mainJComponent = mainJComponent;
		this.initialize();
	}
	/**
	 * Initializes the JTree.
	 */
	private void initialize() {
		this.setModel(this.getTreeModel());
		this.addTreeSelectionListener(this);
		this.reBuildView();
	}
	/**
	 * ReBuilds the Jtree and thus its view.
	 */
	public void reBuildView() {
		
		this.removeAllChangeListener();
		this.getRootNode().removeAllChildren();
		
		this.searchJComponent(this.mainJComponent, this.getRootNode());
		this.getTreeModel().reload();
		this.expandAll();
		this.setRootVisible(false);
	}
	
	/**
	 * Returns the root node of the tree.
	 * @return the root node
	 */
	public DefaultMutableTreeNode getRootNode() {
		if (rootNode==null) {
			rootNode = new DefaultMutableTreeNode(new TabbedPaneTreeNodeObject("Root", null));
		}
		return rootNode;
	}
	/**
	 * Returns the tree model.
	 * @return the tree model
	 */
	private DefaultTreeModel getTreeModel() {
		if (treeModel==null) {
			treeModel = new DefaultTreeModel(this.getRootNode());
		}
		return treeModel;
	}
	/**
	 * Expand all nodes.
	 */
	public void expandAll() {
		for (int i = 0; i < this.getRowCount(); i++) {
	         this.expandRow(i);
		}
	}
	
	/**
	 * Search on the specified JComponent for JTabbedPanes.
	 *
	 * @param jComp the JComponent to search on
	 * @param parentNode the parent node
	 */
	private void searchJComponent(Component comp, DefaultMutableTreeNode parentNode) {
		
		// --- Do we have a JComponent? -----------------------------
		if (comp instanceof JComponent == false) return;
		JComponent jComp = (JComponent) comp;
		
		// --- Search sub components --------------------------------
		if (this.isDebugJComponent==true) System.out.println("[" + this.getClass().getSimpleName() + "] Search on parent component of type " + jComp.getClass().getSimpleName());
		for (Component subComp : jComp.getComponents()) {
			
			if (this.isDebugJComponent==true) System.out.println("[" + this.getClass().getSimpleName() + "] => " + subComp.getClass().getSimpleName());
			if (subComp instanceof JTabbedPane) {
				// --- Found a JTabbedPane --------------------------
				this.analyzeJTabbedPane((JTabbedPane) subComp, parentNode);
				
			} else if (subComp instanceof JComponent) {
				// --- Search for subComponents ---------------------
				this.searchJComponent((JComponent) subComp, parentNode);
			}
		}
	}
	/**
	 * Analyzes the specified JTabbedPane and creates tree nodes accordingly
	 *
	 * @param jTabbedPane the JTabbedPane
	 * @param parentNode the parent node
	 */
	private void analyzeJTabbedPane(JTabbedPane jTabbedPane, DefaultMutableTreeNode parentNode) {
		
		if (this.isDebugJTabbedPane==true) System.out.println("[" + this.getClass().getSimpleName() + "] Search on JTabbedPane");
		
		// --- Add ChangeListener to JTabbedPane to react on changes ----------
		jTabbedPane.addChangeListener(this);
		TabbedPaneTreeNodeObject tno = (TabbedPaneTreeNodeObject) parentNode.getUserObject();
		tno.setJTabbedPane(jTabbedPane);
		
		// --- Search all tabs ------------------------------------------------
		for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
			
			// --- Create node for each tab -----------------------------------
			Component tabComp = jTabbedPane.getComponentAt(i);
			String tabTitle = jTabbedPane.getTitleAt(i).trim();
			DefaultMutableTreeNode tabTreeNode = new DefaultMutableTreeNode(new TabbedPaneTreeNodeObject(tabTitle, null));
			parentNode.add(tabTreeNode);
			if (this.isDebugJTabbedPane==true)  System.out.println("[" + this.getClass().getSimpleName() + "] => TabComp '" + parentNode.toString() + "/" + tabTitle + "': " + tabComp.getClass().getSimpleName());
			
			// --- Further search for JTabbedPanes ----------------------------
			if (tabComp instanceof JTabbedPane) {
				JTabbedPane subJTabbedPane = (JTabbedPane) tabComp;
				this.analyzeJTabbedPane(subJTabbedPane, tabTreeNode);
			} else if (tabComp instanceof JComponent) {
				this.searchJComponent((JComponent) tabComp, tabTreeNode);
			}
		}
	}
	/**
	 * Removes all change listener.
	 */
	private void removeAllChangeListener() {
		this.getTabbedPaneList().forEach(tbbPane -> tbbPane.removeChangeListener(this));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent ce) {
		
		if (this.isPauseChangeListener==true) return;
		
		// --- ChangeEvent from an JTabbedPane => Focus TreeNode ----
		this.isPauseTreeSelectionListener = true;

		JTabbedPane tabSelected = (JTabbedPane) ce.getSource();
		DefaultMutableTreeNode parentTreeNode = this.getTreeNodeOfJTabbedPane(tabSelected);
		if (parentTreeNode!=null) {
			// -- Get node that corresponds to tab selection --------
			DefaultMutableTreeNode selectedTreeNode =  parentTreeNode;
			if (parentTreeNode.getChildCount()>0 && tabSelected.getSelectedIndex()!=-1 && tabSelected.getSelectedIndex()<parentTreeNode.getChildCount()) {
				selectedTreeNode = (DefaultMutableTreeNode) parentTreeNode.getChildAt(tabSelected.getSelectedIndex());
			}
			// --- Set tree node selection --------------------------
			TreePath tp = new TreePath(selectedTreeNode.getPath());
			this.setSelectionPath(tp);
		}
		this.isPauseTreeSelectionListener = false;
	}
	/**
	 * Return the tree node of the specified JTabbedPane.
	 *
	 * @param jTabbedPane the JTabbedPane
	 * @return the tree node of JTabbedPane
	 */
	public DefaultMutableTreeNode getTreeNodeOfJTabbedPane(JTabbedPane jTabbedPane) {
		
		if (jTabbedPane== null) return null;
		for (DefaultMutableTreeNode treeNode : this.getTreeNodeList()) {
			TabbedPaneTreeNodeObject tno = (TabbedPaneTreeNodeObject) treeNode.getUserObject();
			if (tno.getJTabbedPane()==jTabbedPane) return treeNode;
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {

		if (this.isPauseTreeSelectionListener==true) return;
		
		// --- Tree selection changed => Focus right tab ------------
		this.isPauseChangeListener = true;
		this.setFocusToTabbedPane((DefaultMutableTreeNode) this.getLastSelectedPathComponent());
		this.isPauseChangeListener = false;
	}
	
	/**
	 * Sets the focus to the specified tab title.
	 * @param tabTitle the new focus to tab
	 */
	public void setFocusToTab(String tabTitle) {
		
		if (tabTitle==null || tabTitle.isBlank()==true) return;
		
		// -- Find the node for the specified tab ---------
		for (DefaultMutableTreeNode treeNode :  this.getTreeNodeList()) {
			TabbedPaneTreeNodeObject tno = (TabbedPaneTreeNodeObject) treeNode.getUserObject();
			if (tno.getTabTitle().trim().equals(tabTitle.trim())==true) {
				try {
					// --- Pause local listener -----------
					this.isPauseTreeSelectionListener = true;
					this.isPauseChangeListener = true;
					// --- Set tree selection -------------
					TreePath tp = new TreePath(treeNode.getPath());
					this.setSelectionPath(tp);
					// --- Focus corresponding tab --------
					this.setFocusToTabbedPane(treeNode);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					this.isPauseChangeListener = false;
					this.isPauseTreeSelectionListener = false;
				}
				break;
			}
		}
	}
	
	/**
	 * Sets the focus to the JTabbed pane selected.
	 * @param nodeSelected the new focus to tabbed pane
	 */
	private void setFocusToTabbedPane(DefaultMutableTreeNode nodeSelected) {
		
		if (nodeSelected==null) return;
		
		JTabbedPane currTPane = null;
		TreeNode[] tnQueue = nodeSelected.getPath();
		for (TreeNode tn : tnQueue) {
			// --- Get user object ------------------------
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tn; 
			TabbedPaneTreeNodeObject tno = (TabbedPaneTreeNodeObject) node.getUserObject();
			// --- Switch selected tab? -------------------
			int currIndexPos = node.getParent()==null ? -1 : node.getParent().getIndex(node);
			if (currTPane!=null && currIndexPos!=-1) {
				currTPane.setSelectedIndex(currIndexPos);
			}
			// --- Remind as current JTabbedPane? ---------
			if (tno.getJTabbedPane()!=null) {
				currTPane = tno.getJTabbedPane();
			}
		}
	}
	
	
	/**
	 * Returns all known JTabbedPanes in a list.
	 * @return the JTabbedPane list
	 */
	private List<JTabbedPane> getTabbedPaneList() {
		List<JTabbedPane> tabbedPaneList = new ArrayList<>();
		for (TabbedPaneTreeNodeObject tno  : this.getTabbedPaneTreeNodeObjectList()) {
			if (tno.getJTabbedPane()!=null) tabbedPaneList.add(tno.getJTabbedPane());
		}
		return tabbedPaneList;
	}
	/**
	 * Returns all known TabbedPaneTreeNodeObject in a list.
	 * @return the TabbedPaneTreeNodeObject list
	 */
	private List<TabbedPaneTreeNodeObject> getTabbedPaneTreeNodeObjectList() {
		List<TabbedPaneTreeNodeObject> tnoList = new ArrayList<>();
		this.getTreeNodeList().forEach(treeNode -> tnoList.add((TabbedPaneTreeNodeObject)treeNode.getUserObject()));
		return tnoList;
	}
	/**
	 * Returns the current tree node list, excluding the root node.
	 * @return the tree node list
	 */
	public List<DefaultMutableTreeNode> getTreeNodeList() {
		List<DefaultMutableTreeNode> treeNodeList = new ArrayList<>();
		treeNodeList.add(this.getRootNode());
		treeNodeList.addAll(this.getTreeNodeList(this.getRootNode()));
		return treeNodeList;
	}
	/**
	 * Returns all tree nodes and sub tree nodes that belong to the specified parent node.
	 *
	 * @param parentNode the parent node
	 * @return the tree node list
	 */
	public List<DefaultMutableTreeNode> getTreeNodeList(DefaultMutableTreeNode parentNode) {
		
		List<DefaultMutableTreeNode> treeNodeList = new ArrayList<>();
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			treeNodeList.add(treeNode);
			if (treeNode.getChildCount()>0) {
				treeNodeList.addAll(this.getTreeNodeList(treeNode));
			}
		}
		return treeNodeList;
	}
	
	
	/**
	 * The Class TabbedPaneTreeNodeObject.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class TabbedPaneTreeNodeObject {
		
		private String tabTitle;
		private JTabbedPane jTabbedPane;
		
		/**
		 * Instantiates a new JTtabbedPane tree node object.
		 *
		 * @param tabTitle the tab title
		 * @param hostingJTabbedPane the hosting J tabbed pane
		 */
		public TabbedPaneTreeNodeObject(String tabTitle, JTabbedPane hostingJTabbedPane) {
			this.tabTitle = tabTitle;
			this.jTabbedPane = hostingJTabbedPane;
		}
		
		/**
		 * Sets the tab title.
		 * @param tabTitle the new tab title
		 */
		public void setTabTitle(String tabTitle) {
			this.tabTitle = tabTitle;
		}
		/**
		 * Returns the tab title.
		 * @return the tab title
		 */
		public String getTabTitle() {
			return tabTitle;
		}
		
		/**
		 * Sets the hosting / super ordinate JTabbed pane.
		 * @param jTabbedPane the new JTabbedPane
		 */
		public void setJTabbedPane(JTabbedPane jTabbedPane) {
			this.jTabbedPane = jTabbedPane;
		}
		/**
		 * Returns the hosting / super ordinate JTabbed pane.
		 * @param jTabbedPane the new JTabbedPane
		 */
		public JTabbedPane getJTabbedPane() {
			return jTabbedPane;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.getTabTitle();
		}
	}
	
}
