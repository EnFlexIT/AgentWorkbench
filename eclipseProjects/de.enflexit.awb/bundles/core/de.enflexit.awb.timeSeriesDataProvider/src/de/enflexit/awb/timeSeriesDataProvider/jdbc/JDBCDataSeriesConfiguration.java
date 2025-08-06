package de.enflexit.awb.timeSeriesDataProvider.jdbc;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;

/**
 * {@link AbstractDataSeriesConfiguration} implementation for JDBC-based data series.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDBCDataSeriesConfiguration extends AbstractDataSeriesConfiguration {

	private static final long serialVersionUID = 5032523307681657830L;
	
	private String dataColumn;
	
	/**
	 * Gets the data column.
	 * @return the data column
	 */
	public String getDataColumn() {
		return dataColumn;
	}
	/**
	 * Sets the data column.
	 * @param dataColumn the new data column
	 */
	public void setDataColumn(String dataColumn) {
		this.dataColumn = dataColumn;
	}

}
