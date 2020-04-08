package de.enflexit.common.swing.fileSelection;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The listener interface for {@link FileTree}.
 *
 * @see FileTree
 * @see FileTree#addFileTreeListener(FileTreeListener)
 * @see FileTree#removeFileTreeListener(FileTreeListener)
 */
public interface FileTreeListener {

	/**
	 * Will be invoked if the file selection changed.
	 */
	public void onFileSelectionChanged();

	/**
	 * Will be invoked, if a tree node was edited.
	 * @param treeNodeEdited the tree node edited
	 */
	public void onFileTreeElementEdited(DefaultMutableTreeNode treeNodeEdited);
	
}
