package de.enflexit.df.core.processing.transformation.filter;

import java.util.List;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterExpression;
import tech.tablesaw.api.Table;

/**
 * This {@link AbstractDataTransformation} implementation allows to
 * filter the rows of a table by the values of one or more columns.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationFilterRows extends AbstractDataTransformation {
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceFilterRows.TRANSFORMATION_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation()
	 */
	@Override
	public Table performTransformation() {
		
		Table resultTable = null;
		
		System.out.println("Starting filter transformation...");
		long timestampStart = System.currentTimeMillis();
		
		Table inputTable = this.getInputNodes().get(0).getDataTable();
		
		@SuppressWarnings("unchecked")
		List<FilterExpression> filtersList = (List<FilterExpression>) this.getTransformationParameters().get(DataTransformationServiceFilterRows.REQUIRED_PARAMETER);
		
		if (filtersList==null || filtersList.isEmpty()) {
			// --- No filters defined -> pass the original table
			resultTable = this.getInputNodes().get(0).getDataTable();
		} else {
			FilterExpression combinedFilters = Filters.and(filtersList);
			resultTable = TableSawFilter.filter(inputTable, combinedFilters);
		}
		
		long timestampEnd = System.currentTimeMillis();
		
		System.out.println("Done after " + (timestampEnd-timestampStart)/1000 + " seconds");
		return resultTable;
		
	}
	

}
