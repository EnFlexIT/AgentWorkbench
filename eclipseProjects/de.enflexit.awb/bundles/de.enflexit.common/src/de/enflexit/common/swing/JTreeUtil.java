package de.enflexit.common.swing;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * The Class JTreeUtil provides help methods to work on JTree's.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JTreeUtil {

	/**
	 * Sets the tree expanded state.
	 *
	 * @param jTree the JTree to work on
	 * @param expanded set <code>true</code>to expand, <code>false</code> to collapse
	 */
	public static void setTreeExpandedState(JTree jTree, boolean expanded) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getModel().getRoot();
		setNodeExpandedState(jTree, node, expanded);
	}

	/**
	 * Sets the node expanded state.
	 *
	 * @param jTree the JTree to work on
	 * @param node the node to expand or collapse
	 * @param expanded set <code>true</code>to expand, <code>false</code> to collapse
	 */
	public static void setNodeExpandedState(JTree jTree, DefaultMutableTreeNode node, boolean expanded) {
		
		for (int i = 0; i < node.getChildCount(); i++) {
			setNodeExpandedState(jTree, (DefaultMutableTreeNode) node.getChildAt(i), expanded);
		}
		
		if (!expanded && node.isRoot()) return;
		
		TreePath path = new TreePath(node.getPath());
		if (expanded) {
			jTree.expandPath(path);
		} else {
			jTree.collapsePath(path);
		}
	}
	
}
