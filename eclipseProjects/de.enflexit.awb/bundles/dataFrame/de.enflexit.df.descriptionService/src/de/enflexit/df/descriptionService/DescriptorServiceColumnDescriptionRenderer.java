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
	
	/**
	 * Gets a HTML representation of the column description details.
	 * @param columnDescription the {@link ColumnDescription} for the column of interest.
	 * @return the HTML representation of the column details. May be null if no details are specified for the column.
	 */
	private String getColumnDetailsHTML(DataColumnDescription colDescFromDB) {
		
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
	public String getDescription(ColumnDescription columnDescription, DescriptionUseCase descriptionUseCase) {
		
		DataColumnDescription colDescFromDB = this.descriptionController.getColumnDescription(columnDescription.getColumnName(), columnDescription.getTableName());
		
		String descText = null;
		switch (descriptionUseCase) {
		case DescriptionPanel:
			descText = this.getColumnDetailsHTML(colDescFromDB);
			if (descText==null) {
				descText = PLACEHOLDER_NO_DECSRIPTION_FOUND;
			}
			break;
			
		case Tooltip:
			descText = this.getColumnDetailsHTML(colDescFromDB);
			if (descText==null) {
				descText = columnDescription.getDefaultDescription();
			}
			break;
		case ColumnSelection:
			descText = this.getColumnDescriptionForSelection(colDescFromDB);
			if (descText==null) {
				descText = columnDescription.getDefaultDescription();
			}
			break;
		}
		return descText;
	}
	
	/**
	 * Gets a description string for column selection lists.
	 * @param columnDescription the column description
	 * @return the description string
	 */
	private String getColumnDescriptionForSelection(DataColumnDescription columnDescription) {
		
		if (columnDescription==null) return null;
		
		String descriptionString = columnDescription.getColumnName() + "(" + columnDescription.getColumnType() + ")";
		if (columnDescription.getDescription()!=null && columnDescription.getDescription().isBlank()==false) {
			descriptionString += " " + columnDescription.getDescription();
		}
		
		return descriptionString;
	}

}
