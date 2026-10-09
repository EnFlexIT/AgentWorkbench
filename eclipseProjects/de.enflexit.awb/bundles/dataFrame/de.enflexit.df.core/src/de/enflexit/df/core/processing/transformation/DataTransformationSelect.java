package de.enflexit.df.core.processing.transformation;

import java.util.List;

import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * This {@link AbstractDataTransformation} selects a set of columns from the input table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationSelect extends AbstractDataTransformation {
	
	public static final String TRANSFORMATION_PARAMETER_COLUMNS_TO_SELECT = "columnsToSelect";
	private static final String COLUMN_NAME_SEPARATOR = ",";
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation()
	 */
	@Override
	public Table performTransformation() {
		
		Table inputTable = this.getInputNodes().get(0).getDataTable();
		Table outputTable = null;
		
		// --- Perform several validity checks ----------------------
		
		if(this.getInputNodes()==null || this.getInputNodes().size()==0 || this.getInputNodes().size()>1) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Invalid input definition! This transformation requires exactly one input table!");
			return null;
		}
		
		Object paramObject = this.getTransformationParameters().get(TRANSFORMATION_PARAMETER_COLUMNS_TO_SELECT);
		if (paramObject==null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Missing required parameter " + TRANSFORMATION_PARAMETER_COLUMNS_TO_SELECT + "!");
			return null;
		}
		if (paramObject instanceof String) {
			String[] columns = ((String)paramObject).split(COLUMN_NAME_SEPARATOR);
			if (columns.length==0) {
				System.err.println("[" + this.getClass().getSimpleName() + "] No columns specified for selection!");
			} else {
				outputTable = inputTable.selectColumns(columns);
			}
		} else if (paramObject instanceof List<?> list && list.stream().allMatch(Column.class::isInstance)) {
			@SuppressWarnings("unchecked")
			List<Column<?>> columnsList = (List<Column<?>>) paramObject;
			outputTable = inputTable.selectColumns(columnsList.toArray(new Column<?>[0]));
		}
		
		
		// --- If passed, return a table containing only the selected columns
		return outputTable;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceSelectImpl.TRANSFORMATION_NAME;
	}

}
