package de.enflexit.df.core.extension;

/**
 * The Class ColumnDescriptionRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param columnDescription the column description
 * @return the description
 */
public interface ColumnDescriptionRenderer {

	/**
	 * Returns the description.
	 *
	 * @param columnDescription the column description
	 * @return the description
	 */
	public String getDescription(ColumnDescription columnDescription);
	
	/**
	 * Returns the tool tip.
	 *
	 * @param columnDescription the column description
	 * @return the tool tip
	 */
	public String getToolTip(ColumnDescription columnDescription);
	
}
