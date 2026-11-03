package de.enflexit.df.core.processing.transformation.filter;

import de.enflexit.df.core.filter.FilterPersistenceHelper;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterExpression;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
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
	public Table performTransformation(TransformationGraphController graphController) {
		Table inputTable = this.getInputNodes().get(0).getDataTable();
		String filtersString = this.getTransformationParameters().getStringValue(DataTransformationServiceFilterRows.REQUIRED_PARAMETER);
		FilterExpression andFilter = this.createFilterExpressions(filtersString, inputTable);
		return TableSawFilter.filter(inputTable, andFilter);
	}
	
	/**
	 * Creates the filter expressions.
	 * @param filtersString the filters string
	 * @param dataController the data controller
	 * @return the array list
	 */
	private FilterExpression createFilterExpressions(String filtersString, Table table){
		return FilterPersistenceHelper.fromJsonString(filtersString, table);
	}
	

}
