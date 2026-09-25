package de.enflexit.df.core.processing.transformationGraph;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import tech.tablesaw.api.Table;

/**
 * A node in the transformation graph, that represents a data table which can be input and/or output of a transformation. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTableNode {
	
	private AbstractDataSourceDTNO<?> dataSourceDTNO;
	private Table dataTable;

	/**
	 * Gets the data table.
	 * @return the data table
	 */
	public Table getDataTable() {
		return dataTable;
	}

	/**
	 * Sets the data table.
	 * @param dataTable the new data table
	 */
	public void setDataTable(Table dataTable) {
		this.dataTable = dataTable;
	}

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
		this.setDataTable(dataSourceDTNO.getTable());
	}
	
	
}
