package de.enflexit.df.core.extension;

/**
 * The Class ColumnDescriptionRenderer.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface ColumnDescriptionRenderer {

	public enum DescriptionUseCase {
		DescriptionPanel,
		ColumnSelection,
		Tooltip
	}
	
	/**
	 * Returns the description.
	 *
	 * @param columnDescription the column description
	 * @param descriptionUseCase the description use case
	 * @return the description
	 */
	public String getDescription(ColumnDescription columnDescription, DescriptionUseCase descriptionUseCase);
	
}
