package de.enflexit.common.swing.fileSelection;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Class FileTreeAdapter implements all methods from the interface {@link FileTreeListener}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class FileTreeAdapter implements FileTreeListener {

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.fileSelection.FileTreeListener#onFileSelectionChanged()
	 */
	@Override
	public void onFileSelectionChanged() { }

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.fileSelection.FileTreeListener#onFileTreeElementEdited(javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public void onFileTreeElementEdited(DefaultMutableTreeNode treeNodeEdited) { }

}
