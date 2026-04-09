package de.enflexit.df.core.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.df.core.model.DataTreeNodeObjectBase;

/**
 * The Class DataTreeCellRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -905652886390722488L;

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		Component renderComp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); 
		JLabel renderLabel = renderComp instanceof JLabel ? (JLabel) renderComp : null;
		if (value instanceof DefaultMutableTreeNode) {
			// --- Get the DataTreeNodeObjectBase -----------------------
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
			DataTreeNodeObjectBase dtno = (DataTreeNodeObjectBase) treeNode.getUserObject();
			
			// --- Set Icon and Label -------------------------------
			renderLabel.setIcon(dtno.getImageIcon());
			renderLabel.setText(dtno.toString());
			renderLabel.setToolTipText(dtno.getToolTipText());
			
			if (dtno.getErrorMessage()!=null) {
				renderLabel.setForeground(AwbThemeColor.ErrorText.getColor());
			}
			
		}
		return renderComp;
	}
	
}
