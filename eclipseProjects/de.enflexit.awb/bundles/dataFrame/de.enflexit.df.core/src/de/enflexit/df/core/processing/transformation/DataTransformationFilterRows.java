package de.enflexit.df.core.processing.transformation;

import java.time.LocalDate;
import java.time.ZoneId;

import de.enflexit.df.core.processing.transformation.Filters.FilterExpression;
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
		FilterExpression filterExpression = Filters.between("time_utc", LocalDate.of(2024, 6, 10).atStartOfDay(ZoneId.of("Europe/Berlin")).toInstant(), LocalDate.of(2024, 6, 20).plusDays(1).atStartOfDay(ZoneId.of("Europe/Berlin")).toInstant());
		Table inputTable = this.getInputNodes().get(0).getDataTable();
		return TableSawFilter.filter(inputTable, filterExpression);
	}

}
