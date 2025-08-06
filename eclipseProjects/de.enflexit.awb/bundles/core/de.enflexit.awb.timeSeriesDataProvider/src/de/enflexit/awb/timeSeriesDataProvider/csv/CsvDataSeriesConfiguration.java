package de.enflexit.awb.timeSeriesDataProvider.csv;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;

/**
 * {@link AbstractDataSeriesConfiguration} for CSV-based data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class CsvDataSeriesConfiguration extends AbstractDataSeriesConfiguration{
	private static final long serialVersionUID = -6130140646407781134L;
	
	private int dataColumn;
	
	/**
	 * Gets the data column.
	 * @return the data column
	 */
	public int getDataColumn() {
		return dataColumn;
	}
	/**
	 * Sets the data column.
	 * @param dataColumn the new data column
	 */
	public void setDataColumn(int dataColumn) {
		this.dataColumn = dataColumn;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getName();
	}
}
