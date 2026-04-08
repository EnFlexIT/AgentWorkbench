package de.enflexit.df.core.ui;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.DataTreeModel;
import de.enflexit.df.core.model.DataTreeNodeDataSource;

/**
 * The Class JTreeData.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JTreeData extends JTree implements TreeSelectionListener {

	private static final long serialVersionUID = 4514690381659493450L;

	private DataController dataController; 
	
	private JPopupMenu jPopupMenuDataTree;
	
	
	/**
	 * Instantiates a new j tree data.
	 * @param dataController the data controller
	 */
	public JTreeData(DataController dataController) {
		
		this.setDataController(dataController);
		this.setRootVisible(false);
		this.setScrollsOnExpand(true);
		
		this.setModel(this.getDataTreeModel());
		this.getDataTreeModel().addTreeModelListener(this.getTreeModelListener());
		
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addTreeSelectionListener(this);
		this.addKeyListener(this.getKeyListener());
		this.addMouseListener(this.getMouseAdapter());
		
		this.setCellRenderer(new DataTreeCellRenderer());
	}
	
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	
	/**
	 * Returns the data tree model.
	 * @return the data tree model
	 */
	private DataTreeModel getDataTreeModel() {
		return this.getDataController().getDataTreeModel();
	}
	
	/**
	 * Returns the JPopupMenu for the data tree.
	 * @return the JPopupMenu data tree
	 */
	private JPopupMenu getJPopupMenuDataTree() {
		if (jPopupMenuDataTree==null) {
			jPopupMenuDataTree = new JPopupMenu();
		}
		return jPopupMenuDataTree;
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
	 * Expands the view to the level specified.
	 *
	 * @param node the node
	 * @param childLevels the child levels
	 */
	public void expand(DefaultMutableTreeNode node, int childLevels) {
        this.expandAll();
        if (childLevels == 0) return;
        if (childLevels != -1) childLevels--;
        for (int x = 0; x < this.getDataTreeModel().getChildCount(node); x++) {
        	DefaultMutableTreeNode child = (DefaultMutableTreeNode) this.getDataTreeModel().getChild(node, x);
            this.expand(child, childLevels);
        }
    }
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		
		// --- Adjust the selection model -----------------
		this.getDataController().getSelectionModel().setSelectedTreePath(tse.getNewLeadSelectionPath());
		
		
	}
	
	/**
	 * Return the local KeyListener.
	 * @return the key listener
	 */
	private KeyListener getKeyListener() {
		
		KeyListener kl = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent ke) {

				if (ke.getKeyCode()==KeyEvent.VK_DELETE) {
					// --- Delete currently selected data source --------------
					DataController dc = JTreeData.this.getDataController();
					DataTreeNodeDataSource<?> dtnoDataSource = dc.getSelectionModel().getSelectedDataTreeNodeDataSource();
					if (dtnoDataSource!=null) {
						// --- Ask the user to delete the data source ---------
						dc.removeDataSourceAskUser(OwnerDetection.getOwnerWindowForComponent(JTreeData.this), dtnoDataSource.getDataSource(), dtnoDataSource.getCaption());
					}
					
				} else if (ke.getKeyCode()==KeyEvent.VK_CONTEXT_MENU ) {
					// --- Show context menu ----------------------------------
					Rectangle pathBounds = JTreeData.this.getPathBounds(JTreeData.this.getSelectionPath());
					JTreeData.this.getJPopupMenuDataTree().show (JTreeData.this, pathBounds.x, pathBounds.y + pathBounds.height);
					
				}
			}
		};
		return kl;
	}
	
	/**
	 * Returns the local TreeModelListener.
	 * @return the tree model listener
	 */
	private TreeModelListener getTreeModelListener() {
		
		TreeModelListener tml = new TreeModelListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
			 */
			@Override
			public void treeNodesChanged(TreeModelEvent tme) {
			}

			/* (non-Javadoc)
			 * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
			 */
			@Override
			public void treeNodesInserted(TreeModelEvent tme) {
				
				Object[] pathBase = tme.getPath();
				Object[] children = tme.getChildren();

				Object[] pathSelection = new Object [pathBase.length + 1];
				int i;
				for (i = 0; i < pathBase.length; i++) {
					pathSelection[i] = pathBase[i];
				}
				pathSelection[i] = children[0];
				JTreeData.this.setSelectionPath(new TreePath(pathSelection));
				
			}

			/* (non-Javadoc)
			 * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
			 */
			@Override
			public void treeNodesRemoved(TreeModelEvent tme) {
				
				Object[] pathBase = tme.getPath();
				
				DefaultMutableTreeNode baseNode = (DefaultMutableTreeNode) pathBase[0]; 
				if (baseNode.getChildCount()>0) {

					DefaultMutableTreeNode nextSelection = null;
					
					int childSearchIndex = tme.getChildIndices()[0];
					if (childSearchIndex > baseNode.getChildCount()-1) {
						childSearchIndex = baseNode.getChildCount()-1;
					}
					while (childSearchIndex>=0 && baseNode.getChildAt(childSearchIndex)==null) {
						childSearchIndex--;
					}
					nextSelection = (DefaultMutableTreeNode) baseNode.getChildAt(childSearchIndex);
					JTreeData.this.setSelectionPath(new TreePath(nextSelection.getPath()));
				}
			}

			/* (non-Javadoc)
			 * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
			 */
			@Override
			public void treeStructureChanged(TreeModelEvent tme) {
			}
		};
		return tml;
	}
	
	/**
	 * Returns the mouse adapter for this JTree.
	 * @return the mouse adapter
	 */
	private MouseAdapter getMouseAdapter() {
		
		MouseAdapter ma = new MouseAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 */
			@Override
			public void mousePressed(MouseEvent me) {
			
				if (SwingUtilities.isRightMouseButton(me)) {
					// --- Display the context menu -----------------
					JTree myTree = (JTree) me.getSource();
					TreePath path = myTree.getPathForLocation(me.getX(), me.getY());
					Rectangle pathBounds = myTree.getUI().getPathBounds(myTree, path);
					if (pathBounds!=null && pathBounds.contains(me.getX(), me.getY())) {
						myTree.setSelectionPath(path);
						myTree.scrollPathToVisible(path);
						JTreeData.this.getJPopupMenuDataTree().show (myTree, pathBounds.x, pathBounds.y + pathBounds.height);
					}
				}
			} // end mousePressed
			
		};
		return ma;
	}
	
	
}
