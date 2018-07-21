package de.enflexit.common.swing.fileSelection;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.enflexit.common.swing.fileSelection.DirectoryEvaluator.FileDescriptor;

/**
 * The Class FileTreeCellRenderer is used as renderer in the {@link FileTree}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -8466756399038136275L;

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		// --- Get current FileDescriptor ---------------------------
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		FileDescriptor fd = (FileDescriptor) node.getUserObject();
		
		// --- Get the default Render Component (extended JLabel)  --
		DefaultTreeCellRenderer jLabelDefault = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		// --- Get the check box ------------------------------------
		fd.setSelected(fd.isSelected());

		// --- Place it on a new JPanel -----------------------------
		JPanel jPanelReturn = new JPanel();
		jPanelReturn.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		jPanelReturn.setOpaque(false);
		jPanelReturn.setBackground(new Color(0, 0, 0, 0));
		jPanelReturn.add(fd.getCheckBox());
		jPanelReturn.add(jLabelDefault);
		return jPanelReturn;
	}
	
}
