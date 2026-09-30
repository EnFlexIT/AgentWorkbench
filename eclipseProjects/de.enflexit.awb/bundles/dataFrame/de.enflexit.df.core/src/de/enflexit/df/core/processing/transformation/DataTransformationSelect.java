package de.enflexit.df.core.processing.transformation;

import tech.tablesaw.api.Table;

/**
 * This {@link AbstractDataTransformation} selects a set of columns from the input table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationSelect extends AbstractDataTransformation {
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation()
	 */
	@Override
	public Table performTransformation() {
		
		final String TRANSFORMATION_PARAMETER_COLUMNS_TO_SELECT = "columnsToSelect";
		
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
		if (paramObject instanceof String == false) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Invalid parameter Type for " + TRANSFORMATION_PARAMETER_COLUMNS_TO_SELECT + ", must be String!");
			return null;
		}
		
		String[] columns = ((String)paramObject).split(",");
		if (columns.length==0) {
			System.err.println("[" + this.getClass().getSimpleName() + "] No columns specified for selection!");
			return null;
		}
		
		// --- If passed, return a table containing only the selected columns
		Table inputTable = this.getInputNodes().get(0).getDataTable();
		return inputTable.selectColumns(columns);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceSelectImpl.TRANSFORMATION_NAME;
	}

}
