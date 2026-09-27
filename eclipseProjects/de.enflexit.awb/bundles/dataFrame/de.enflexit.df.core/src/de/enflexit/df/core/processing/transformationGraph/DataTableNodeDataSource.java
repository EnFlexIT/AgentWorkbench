package de.enflexit.df.core.processing.transformationGraph;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import tech.tablesaw.api.Table;

/**
 * This data table node represents an original data source from the workbook.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTableNodeDataSource extends DataTableNode {
	
	private AbstractDataSourceDTNO<?> dataSourceDTNO;
	
	/**
	 * Gets the DTNO of the data source providing the table. Might be null if  
	 * the table is not related to a data source, i.e. for intermediate steps.
	 * @return the data source DTNO
	 */
	public AbstractDataSourceDTNO<?> getDataSourceDTNO() {
		return dataSourceDTNO;
	}

	/**
	 * Sets the data source DTNO.
	 * @param dataSourceDTNO the new data source DTNO
	 */
	public void setDataSourceDTNO(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		this.dataSourceDTNO = dataSourceDTNO;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getDataTable()
	 */
	@Override
	public Table getDataTable() {
		return this.dataSourceDTNO!=null ? this.dataSourceDTNO.getTable() : null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getLabelText()
	 */
	@Override
	public String getLabelText() {
		return this.dataSourceDTNO!=null ? this.dataSourceDTNO.getCaption() : "Data source not set!";
	}
	
}
