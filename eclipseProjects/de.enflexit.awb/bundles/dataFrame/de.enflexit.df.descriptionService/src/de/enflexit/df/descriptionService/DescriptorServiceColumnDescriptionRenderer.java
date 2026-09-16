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
	
	private static final String PLACEHOLDER_NO_DECSRIPTION_FOUND = "No Column details specified!\nUse the description editor to change that!";
	
	private DescriptionsController descriptionController;
	
	private String cssStylesHeader = """
			<head>
				<style>
					table {
						border-collapse: collapse;
					}
					td {
						align: left;
						vertical-align: top;
						padding: 2px 6px;
					}
					td.key {
						font-weight: bold;
					}
					li {
						line-height: 80%;
					}
					p {
						margin: 5px;
					}
					ul {
						margin-top: 0;
						padding-top: 0;
					}
				</style>
			</head>
			""";
	
	/**
	 * Instantiates a new descriptor service column description renderer.
	 * @param descriptionController the description controller
	 */
	public DescriptorServiceColumnDescriptionRenderer(DescriptionsController descriptionController) {
		this.descriptionController = descriptionController;
	}
	
//	/**
//	 * Gets the description as plain text, using escape sequences (\n, \t) for formatting.
//	 * @param columnDescription the {@link ColumnDescription} for the column of interest.
//	 * @return the column details text. May be null if no details are specified for the column.
//	 */
//	private String getColumnDetailsPlainText(ColumnDescription columnDescription) {
//		// --- If there is a description stored in the DB, build a customized description string based on the specified information. 
//		DataColumnDescription colDescFromDB = this.descriptionController.getColumnDescription(columnDescription.getColumnName(), columnDescription.getTableName());
//		if (colDescFromDB!=null) {
//			StringBuffer descriptionText = new StringBuffer();
//			
//			if(colDescFromDB.getTableName()!=null && colDescFromDB.getTableName().isBlank()==false) {
//				descriptionText.append("DB Table:\t" + colDescFromDB.getTableName() + "\n");
//			}
//			if (colDescFromDB.getColumnName()!=null && colDescFromDB.getColumnName().isBlank()==false) {
//				descriptionText.append("Column:\t" + colDescFromDB.getColumnName() + "\n");
//			}
//			descriptionText.append("Description:\t" + (colDescFromDB.getDescription()!=null ? colDescFromDB.getDescription() : "Not specified") + "\n");
//			descriptionText.append("Data Type:\t" + (colDescFromDB.getColumnType()!=null ? ColumnType.valueOf(colDescFromDB.getColumnType()).getPrinterFriendlyName() : "Not specified") + "\n");
//			descriptionText.append("Unit:\t" + (colDescFromDB.getUnit()!=null&&colDescFromDB.getUnit().isBlank()==false ? colDescFromDB.getUnit() : "Not specified") + "\n");
//			descriptionText.append("Min. Value:\t" + (colDescFromDB.getMinValue()!=null ? colDescFromDB.getMinValue() : "Not specified") + "\n");
//			descriptionText.append("Max. Value:\t" + (colDescFromDB.getMaxValue()!=null ? colDescFromDB.getMaxValue() : "Not specified") + "\n");
//			
//			if (colDescFromDB.getAlternativeIDs().size()>0) {
//				descriptionText.append("\nAlternative IDs:\n");
//				for (DataColumnAlternativeID altID : colDescFromDB.getAlternativeIDs()) {
//					descriptionText.append("- " + altID.getAlternativeIdentifier() + "\n");
//				}
//			}
//			
//			return descriptionText.toString();
//			
//		} else {
//			return null;
//		}
//	}
	
	/**
	 * Gets a HTML representation of the column description details.
	 * @param columnDescription the {@link ColumnDescription} for the column of interest.
	 * @return the HTML representation of the column details. May be null if no details are specified for the column.
	 */
	private String getColumnDetailsHTML(ColumnDescription columnDescription) {
		DataColumnDescription colDescFromDB = this.descriptionController.getColumnDescription(columnDescription.getColumnName(), columnDescription.getTableName());
		if (colDescFromDB!=null) {
			StringBuffer columnDetailsHTML = new StringBuffer();
			columnDetailsHTML.append("<html>" + cssStylesHeader + "<table>");
			
			if(colDescFromDB.getTableName()!=null && colDescFromDB.getTableName().isBlank()==false) {
				columnDetailsHTML.append(this.getKeyValueTableRow("DB Table", colDescFromDB.getTableName()));
			}
			if (colDescFromDB.getColumnName()!=null && colDescFromDB.getColumnName().isBlank()==false) {
				columnDetailsHTML.append(this.getKeyValueTableRow("Column", colDescFromDB.getColumnName()));
			}
			
			columnDetailsHTML.append(this.getKeyValueTableRow("Description", colDescFromDB.getDescription()));
			columnDetailsHTML.append(this.getKeyValueTableRow("Data Type", ColumnType.valueOf(colDescFromDB.getColumnType())));
			columnDetailsHTML.append(this.getKeyValueTableRow("Unit", colDescFromDB.getUnit()));
			columnDetailsHTML.append(this.getKeyValueTableRow("Min. Value", colDescFromDB.getMinValue()));
			columnDetailsHTML.append(this.getKeyValueTableRow("Max. Value", colDescFromDB.getMaxValue()));
			
			columnDetailsHTML.append("</table>");
			
			if (colDescFromDB.getAlternativeIDs().size()>0) {
				columnDetailsHTML.append("<p>Alternative IDs:</p><ul>");
				for (DataColumnAlternativeID altID : colDescFromDB.getAlternativeIDs()) {
					columnDetailsHTML.append("<li>" + altID.getAlternativeIdentifier() + "</li>");
				}
				columnDetailsHTML.append("</ul>");
			}
			
			columnDetailsHTML.append("</html>");
			return columnDetailsHTML.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Creates a HTML table row for a key value combination
	 * @param key the key
	 * @param value the value
	 * @return the table row
	 */
	private String getKeyValueTableRow(String key, Number value) {
		return this.getKeyValueTableRow(key, (value!=null ? String.valueOf(value) : null));
	}
	
	/**
	 * Creates a HTML table row for a key value combination
	 * @param key the key
	 * @param value the value
	 * @return the table row
	 */
	private String getKeyValueTableRow(String key, ColumnType value) {
		return this.getKeyValueTableRow(key, (value!=null ? value.getPrinterFriendlyName() : null));
	}
	
	/**
	 * Creates a HTML table row for a key value combination
	 * @param key the key
	 * @param value the value
	 * @return the table row
	 */
	private String getKeyValueTableRow(String key, String value) {
		return "<tr><td class=\"key\">" + key + ":</td><td>" + (value!=null ? value : "Not specified" + "</td></tr>");
	}
	

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.ColumnDescriptionRenderer#getDescription(de.enflexit.df.core.extension.ColumnDescription)
	 */
	@Override
	public String getDescription(ColumnDescription columnDescription) {
		String columnDetailsText = this.getColumnDetailsHTML(columnDescription);
		return (columnDetailsText!=null ? columnDetailsText : PLACEHOLDER_NO_DECSRIPTION_FOUND);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.extension.ColumnDescriptionRenderer#getToolTip(de.enflexit.df.core.extension.ColumnDescription)
	 */
	@Override
	public String getToolTip(ColumnDescription columnDescription) {
		String columnDetailsText = this.getColumnDetailsHTML(columnDescription);
		return (columnDetailsText!=null ? columnDetailsText : columnDescription.getDefaultDescription());
	}

}
