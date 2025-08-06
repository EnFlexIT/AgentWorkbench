package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;

/**
 * Custom tree cell renderer, showing individual icons for the different data source types.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesConfigTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = -4437238641552771937L;

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		Component customRenderer = this.getCustomRendererComponent((DefaultMutableTreeNode) value, sel);
		
		if (customRenderer!=null) {
			// --- Use custom renderer if available -----------------
			return customRenderer;
		} else {
			// --- Use default renderer otherwise -------------------
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
	}
	
	/**
	 * Gets a custom renderer component, if the tree node contains a data source configuration that specifies individual icons.
	 * @param treeNode the tree node
	 * @param isSelected the is selected
	 * @return the custom renderer component
	 */
	private JLabel getCustomRendererComponent(DefaultMutableTreeNode treeNode, boolean isSelected) {
		JLabel rendererLabel = null;
		if (treeNode.getUserObject() instanceof AbstractDataSourceConfiguration) {
			AbstractDataSourceConfiguration dsConfig = (AbstractDataSourceConfiguration) treeNode.getUserObject();
			ImageIcon dsIcon = dsConfig.getImageIcon(isSelected, AwbLookAndFeelAdjustments.isDarkLookAndFeel());
			if (dsIcon!=null) {
				rendererLabel = new JLabel();
				rendererLabel.setIcon(dsIcon);
				rendererLabel.setText(dsConfig.getName());
				if (isSelected==true) {
					rendererLabel.setForeground(Color.WHITE);
				} else {
					rendererLabel.setForeground(this.getForegroundColor());
				}
			}
		}
		return rendererLabel;
	}
	
	/**
	 * Gets the foreground color for light or dark mode.
	 * @return the foreground color
	 */
	private Color getForegroundColor() {
		if (AwbLookAndFeelAdjustments.isDarkLookAndFeel()==true) {
			return Color.LIGHT_GRAY;
		} else {
			return Color.BLACK;
		}
	}

}
