package de.enflexit.common.swing.fileSelection;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.enflexit.common.swing.fileSelection.DirectoryEvaluator.FileDescriptor;

/**
 * The Class FileTree extends an JTree and is customized wit respect to a
 * file and directory selection.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class FileTree extends JTree {

	private static final long serialVersionUID = -3918400184177276606L;

	private DefaultTreeModel fileTreeModel;
	private ArrayList<FileTreeListener> fileTreeListener;
	
	/**
	 * Instantiates a new file tree.
	 * @param directoryEvaluator the directory evaluator
	 */
	public FileTree(DefaultTreeModel fileTreeModel) {
		super(fileTreeModel);
		this.fileTreeModel = fileTreeModel;
		this.setCellRenderer(new FileTreeCellRenderer());
		this.setCellEditor(new FileTreeCellEditor(this));
		this.setEditable(true);
	}
	
	/**
	 * Returns the tree path for the specified node.
	 *
	 * @param treeNode the tree node
	 * @return the tree path from node
	 */
	public TreePath getTreePathFromNode(TreeNode treeNode) {
		ArrayList<Object> nodePath = new ArrayList<Object>();
		if (treeNode!=null) {
			nodePath.add(treeNode);
			treeNode = treeNode.getParent();
			while (treeNode!=null) {
				nodePath.add(0, treeNode);
				treeNode = treeNode.getParent();
			}
		}
		return nodePath.isEmpty() ? null : new TreePath(nodePath.toArray());
	}
	
	/**
	 * Expands all nodes starting from the specified node.
	 *
	 * @param baseNode the node
	 * @param childLevels the child levels
	 */
	public void expand(TreeNode baseNode, int childLevels) {
        
        this.expandPath(this.getTreePathFromNode(baseNode));
        this.fileTreeModel.reload(baseNode);
        
        if (childLevels == 0) return;
        if (childLevels != -1) childLevels--;

        for (int x = 0; x < this.fileTreeModel.getChildCount(baseNode); x++) {
        	DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) this.fileTreeModel.getChild(baseNode, x);
        	this.expandPath(this.getTreePathFromNode(childNode));
            this.fileTreeModel.reload(childNode);
            this.expand(childNode, childLevels);
        }
    }
	
	/**
	 * Sets the nodes selected.
	 *
	 * @param nodeToSetSelected the node to set selected
	 * @param isSelected the is selected
	 */
	public void setNodesSelected(final DefaultMutableTreeNode nodeToSetSelected, final boolean isSelected) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				FileDescriptor fd = (FileDescriptor) nodeToSetSelected.getUserObject();
				fd.setSelected(false);
				fileTreeModel.reload(nodeToSetSelected);
			}
		});
	}
	/**
	 * Sets all children nodes selected.
	 *
	 * @param paraentNode the parent node
	 * @param isSelected the selected
	 */
	public void setChildrenNodesSelected(final DefaultMutableTreeNode paraentNode, final boolean isSelected) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				FileTree.this.setChildrenNodesSelectedInternal(paraentNode, isSelected);
				FileTree.this.fireFileTreeElementEdited(paraentNode);
				FileTree.this.fireFileSelectionChanged();
			}
		});
	}
	/**
	 * Sets all children nodes selected.
	 *
	 * @param paraentNode the parent node
	 * @param isSelected the selected
	 */
	private void setChildrenNodesSelectedInternal(DefaultMutableTreeNode paraentNode, boolean isSelected) {
		
		if (paraentNode==null) return;
		this.fileTreeModel.reload(paraentNode);
		for (int i = 0; i < paraentNode.getChildCount(); i++) {
			DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) paraentNode.getChildAt(i);
			FileDescriptor fd = (FileDescriptor) subNode.getUserObject(); 
			fd.setSelected(isSelected);
			this.fileTreeModel.reload(subNode);
			if (subNode.getChildCount()>0) {
				this.setChildrenNodesSelectedInternal(subNode, isSelected);
			}
		}
	}
	
	
	// --------------------------------------------------------------
	// --- From here methods for the FileTreeListener can be found --
	// --------------------------------------------------------------
	/**
	 * Return the registered file tree listener.
	 * @return the file tree listener
	 */
	public ArrayList<FileTreeListener> getFileTreeListener() {
		if (fileTreeListener==null) {
			fileTreeListener = new ArrayList<>();
		}
		return fileTreeListener;
	}
	/**
	 * Adds the specified FileTreeListener.
	 * @param fileTreeListener the file tree listener
	 */
	public void addFileTreeListener(FileTreeListener fileTreeListener) {
		if (this.getFileTreeListener().contains(fileTreeListener)==false) {
			this.getFileTreeListener().add(fileTreeListener);
		}
	}
	/**
	 * Removes the specified FileTreeListener.
	 * @param fileTreeListener the file tree listener
	 */
	public void removeFileTreeListener(FileTreeListener fileTreeListener) {
		this.getFileTreeListener().remove(fileTreeListener);
	}
	/**
	 * Fire file selection changed.
	 */
	private void fireFileSelectionChanged() {
		for (int i = 0; i < this.getFileTreeListener().size(); i++) {
			this.getFileTreeListener().get(i).onFileSelectionChanged();
		}
	}
	/**
	 * Fire file tree element edited.
	 * @param treeNodeEdited the tree node edited
	 */
	private void fireFileTreeElementEdited(DefaultMutableTreeNode treeNodeEdited) {
		for (int i = 0; i < this.getFileTreeListener().size(); i++) {
			this.getFileTreeListener().get(i).onFileTreeElementEdited(treeNodeEdited);
		}
	}
	
}
