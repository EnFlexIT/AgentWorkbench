package de.enflexit.df.descriptionService;

import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.extension.ColumnDescriptionRenderer;
import de.enflexit.df.descriptionService.db.DataColumnAlternativeID;
import de.enflexit.df.descriptionService.db.DataColumnDescription;
import tech.tablesaw.api.ColumnType;

/**
 * The Class DescriptorServiceColumnDescriptionRenderer.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DescriptorServiceColumnDescriptionRenderer implements ColumnDescriptionRenderer {
	
	private DescriptionsController descriptionController;
	
	/**
	 * Instantiates a new descriptor service column description renderer.
	 * @param descriptionController the description controller
	 */
	public DescriptorServiceColumnDescriptionRenderer(DescriptionsController descriptionController) {
		this.descriptionController = descriptionController;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.ColumnDescriptionRenderer#getDescription(de.enflexit.df.core.extension.ColumnDescription)
	 */
	@Override
	public String getDescription(ColumnDescription columnDescription) {
		
		// --- If there is a description stored in the DB, build a customized description string based on the specified information. 
		DataColumnDescription colDescFromDB = this.descriptionController.getColumnDescription(columnDescription.getColumnName(), columnDescription.getTableName());
		if (colDescFromDB!=null) {
			StringBuffer descriptionText = new StringBuffer();
			
			if(colDescFromDB.getTableName()!=null && colDescFromDB.getTableName().isBlank()==false) {
				descriptionText.append("DB Table:\t" + colDescFromDB.getTableName() + "\n");
			}
			if (colDescFromDB.getColumnName()!=null && colDescFromDB.getColumnName().isBlank()==false) {
				descriptionText.append("Column:\t" + colDescFromDB.getColumnName() + "\n");
			}
			descriptionText.append("Description:\t" + (colDescFromDB.getDescription()!=null ? colDescFromDB.getDescription() : "Not specified") + "\n");
			descriptionText.append("Data Type:\t" + (colDescFromDB.getColumnType()!=null ? ColumnType.valueOf(colDescFromDB.getColumnType()).getPrinterFriendlyName() : "Not specified") + "\n");
			descriptionText.append("Unit:\t" + (colDescFromDB.getUnit()!=null&&colDescFromDB.getUnit().isBlank()==false ? colDescFromDB.getUnit() : "Not specified") + "\n");
			descriptionText.append("Min. Value:\t" + (colDescFromDB.getMinValue()!=null ? colDescFromDB.getMinValue() : "Not specified") + "\n");
			descriptionText.append("Max. Value:\t" + (colDescFromDB.getMaxValue()!=null ? colDescFromDB.getMaxValue() : "Not specified") + "\n");
			
			if (colDescFromDB.getAlternativeIDs().size()>0) {
				descriptionText.append("Alternative IDs:\n");
				for (DataColumnAlternativeID altID : colDescFromDB.getAlternativeIDs()) {
					descriptionText.append("- " + altID.getAlternativeIdentifier() + "\n");
				}
			}
			
			return descriptionText.toString();
			
		} else {
			return "No Column details specified!\nUse the description editor to change that!";
		}
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.ColumnDescriptionRenderer#getToolTip(de.enflexit.df.core.extension.ColumnDescription)
	 */
	@Override
	public String getToolTip(ColumnDescription columnDescription) {
		return columnDescription.getDefaultDescription();
	}

}
