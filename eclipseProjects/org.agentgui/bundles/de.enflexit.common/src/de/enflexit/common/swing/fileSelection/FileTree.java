package de.enflexit.common.swing.fileSelection;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
	 * Sets all children nodes selected.
	 *
	 * @param paraentNode the parent node
	 * @param isSelected the selected
	 */
	public void setChildrenNodesSelected(DefaultMutableTreeNode paraentNode, boolean isSelected) {
		
		if (paraentNode==null) return;
		for (int i = 0; i < paraentNode.getChildCount(); i++) {
			DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) paraentNode.getChildAt(i);
			FileDescriptor fd = (FileDescriptor) subNode.getUserObject(); 
			fd.setSelected(isSelected);
			this.fileTreeModel.reload(subNode);
			if (subNode.getChildCount()>0) {
				this.setChildrenNodesSelected(subNode, isSelected);
			}
		}
	}
	
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
	protected void fireFileSelectionChanged() {
		for (int i = 0; i < this.getFileTreeListener().size(); i++) {
			this.getFileTreeListener().get(i).onFileSelectionChanged();
		}
	}

}
