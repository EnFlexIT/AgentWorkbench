package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;

import de.enflexit.df.core.model.DataController;
import tech.tablesaw.api.Table;

/**
 * This {@link AbstractDataTransformation} selects a set of columns from the input table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationSelectColumns extends AbstractDataTransformation {
	
	public static final String PROPERTY_NAME_SELECT_COLUMNS = "columns";
	public static final String COLUMN_NAME_SEPARATOR = ",";
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation()
	 */
	@Override
	public Table performTransformation(DataController dataController) {
		
		Table inputTable = this.getInputNodes().get(0).getDataTable();
		
		// --- Perform several validity checks ----------------------
		
		if(this.getInputNodes()==null || this.getInputNodes().size()==0 || this.getInputNodes().size()>1) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Invalid input definition! This transformation requires exactly one input table!");
			this.getMessagesList().add(DataTransformationMessage.error("Invalid input definition! This transformation requires exactly one input table!"));
			return null;
		}
		
		String columnsString = this.getTransformationParameters().getStringValue(PROPERTY_NAME_SELECT_COLUMNS);
		if (columnsString==null || columnsString.isBlank()==true) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Missing required parameter " + PROPERTY_NAME_SELECT_COLUMNS + "!");
			this.getMessagesList().add(DataTransformationMessage.error("Missing required parameter " + PROPERTY_NAME_SELECT_COLUMNS + "!"));
			return null;
		}
		
		ArrayList<String> finalColumnsList = new ArrayList<String>();
		String[] columnsArray = columnsString.split(COLUMN_NAME_SEPARATOR);
		if (columnsArray.length>0) {
			// --- Check if the specified columns actually exist, skip if not -----------
			for (String columnName : columnsArray) {
				if (inputTable.containsColumn(columnName)==true) {
					finalColumnsList.add(columnName);
				} else {
					System.err.println("Column " + columnName + " not found in input table " + this.getInputNodes().get(0).getLabelText() + "!");
					this.getMessagesList().add(DataTransformationMessage.warning("Column " + columnName + " not found in input table " + this.getInputNodes().get(0).getLabelText() + ", skipping..."));
				}
			}
		}
		
		if (finalColumnsList.size()>0) {
			this.getMessagesList().add(DataTransformationMessage.warning("No selection columns defined, returning the original table"));
			return inputTable.selectColumns(finalColumnsList.toArray(new String[0]));
		} else {
			return inputTable;
		}
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceSelectColumns.TRANSFORMATION_NAME;
	}

}
