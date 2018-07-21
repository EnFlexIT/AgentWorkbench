package de.enflexit.common.swing.fileSelection;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import de.enflexit.common.swing.fileSelection.DirectoryEvaluator.FileDescriptor;

/**
 * The Class FileTreeCellEditor is used in the {@link FileTree}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class FileTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {

	private static final long serialVersionUID = 6945061461896002317L;

	private FileTreeCellRenderer fileTreeRenderer;
	private FileTree fileTree;
	
	/**
	 * Instantiates a new file tree cell editor.
	 * @param fileTree the file tree
	 */
	public FileTreeCellEditor(FileTree fileTree) {
		this.fileTree=fileTree;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.getFileTreeRenderer();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		
		// --- Get current FileDescriptor ---------------------------
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		final FileDescriptor fd = (FileDescriptor) node.getUserObject();
		
		// --- Add an action listener to the check box --------------
		fd.getCheckBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				boolean isSelected = fd.getCheckBox().isSelected(); 
				fd.setSelected(isSelected);
				fileTree.setChildrenNodesSelected(node, isSelected);
				fileTree.fireFileSelectionChanged();
			}
		});
		// --- Return renderer component ---------------------------- 
		return this.getFileTreeRenderer().getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, false);
	}
	
	/**
	 * Returns the local file tree renderer.
	 * @return the file tree renderer
	 */
	private FileTreeCellRenderer getFileTreeRenderer() {
		if (fileTreeRenderer==null) {
			fileTreeRenderer = new FileTreeCellRenderer();
		}
		return fileTreeRenderer;
	}

}
