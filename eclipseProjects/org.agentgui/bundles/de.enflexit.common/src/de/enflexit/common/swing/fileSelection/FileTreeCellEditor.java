package de.enflexit.common.swing.fileSelection;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
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

	private FileTree fileTree;

	private DefaultMutableTreeNode treeNode;
	private FileDescriptor fileDescriptor;
	private FileTreeCellRenderer fileTreeRenderer;
	private ActionListener aListener;
	
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
		return this.fileDescriptor;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		
		// --- Get current FileDescriptor ---------------------------
		this.treeNode = (DefaultMutableTreeNode)value;
		this.fileDescriptor = (FileDescriptor) this.treeNode.getUserObject();
		// --- Add an action listener to the check box --------------
		this.addActionListener(this.fileDescriptor.getCheckBox());
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

	/**
	 * Adds the local action listener if not already there.
	 * @param jCheckBoxToAddTo the j check box to add to
	 */
	private void addActionListener(JCheckBox jCheckBoxToAddTo) {
		ArrayList<ActionListener> listener = new ArrayList<>(Arrays.asList(jCheckBoxToAddTo.getActionListeners()));
		if (listener.contains(this.getCheckBoxActionListener())==false) {
			jCheckBoxToAddTo.addActionListener(this.getCheckBoxActionListener());
		}
	}
	/**
	 * Returns the check box action listener.
	 * @return the check box action listener
	 */
	private ActionListener getCheckBoxActionListener() {
		if (aListener==null) {
			aListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					boolean isSelected = ((JCheckBox)ae.getSource()).isSelected(); 
					fileDescriptor.setSelected(isSelected);
					fileTree.setChildrenNodesSelected(treeNode, isSelected);
					fireEditingStopped();
				}
			};
		}
		return aListener;
	}
	
}
