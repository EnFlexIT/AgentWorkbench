package de.enflexit.awb.ws.ui.server;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import de.enflexit.awb.ws.core.model.AbstractServerTreeNodeObject;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;

/**
 * The Class ServerTreeCellRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeCellRenderer implements TreeCellRenderer {

	private JLabel jLabelDisplay;
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		AbstractServerTreeNodeObject stno = (AbstractServerTreeNodeObject) node.getUserObject();
		
		this.getJLabelDisplay().setText(stno.toString());
		this.getJLabelDisplay().setToolTipText(stno.getToolTipText());
		this.getJLabelDisplay().setIcon(stno.getNodeIcon());
		
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==false) {
			if (isSelected==true) {
				this.getJLabelDisplay().setForeground(Color.WHITE);
			} else {
				this.getJLabelDisplay().setForeground(Color.BLACK);
			}
		}
		return this.getJLabelDisplay();
	}

	/**
	 * Returns the JLabel for the display.
	 * @return the JLabel for the display
	 */
	private JLabel getJLabelDisplay() {
		if (jLabelDisplay==null) {
			jLabelDisplay = new JLabel();
		}
		return jLabelDisplay;
	}
	
}
