package de.enflexit.df.core.extension;

import javax.swing.JPanel;

/**
 * The Interface ColumnDescriptionPanel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface ColumnDescriptionPanel {

	/**
	 * Has to returns the JComponent thats serves as column description panel.
	 * @return the column description panel
	 */
	public JPanel getActualColumnDescriptionPanel();
	
	/**
	 * Has to receive the {@link ColumnDescription} that is to be displayed.
	 * @param columnDescription the new column description
	 */
	public void setColumnDescription(ColumnDescription columnDescription);
	
}
