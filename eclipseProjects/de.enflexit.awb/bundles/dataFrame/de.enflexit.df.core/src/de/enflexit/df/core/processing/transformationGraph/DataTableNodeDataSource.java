package de.enflexit.df.core.processing.transformationGraph;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import tech.tablesaw.api.Table;

/**
 * This data table node represents an original data source from the workbook.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTableNodeDataSource extends DataTableNode {
	
	private AbstractDataSourceDTNO<?> dataSourceDTNO;
	
	private String uuid;
	
	private static boolean debug = false;
	
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
		if (this.dataSourceDTNO==null) return null;
		
		Table table = this.dataSourceDTNO.getTable();
		int tries = 0;
		while (table==null && tries < 10) {
			try {
				Thread.sleep(1000);
				tries++;
				table = this.dataSourceDTNO.getTable();
				if (debug==true) {
					System.out.println("[" + this.getClass().getSimpleName() + "] Table not available for " + this.getLabelText() + "! Retrying...");
				}
			} catch (InterruptedException e) {
			}
		}
		if (debug==true) {
			System.out.println("[" + this.getClass().getSimpleName() + "] Table found for " + this.getLabelText() + " after " + tries + " retries");
		}
		
		return this.dataSourceDTNO!=null ? this.dataSourceDTNO.getTable() : null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getLabelText()
	 */
	@Override
	public String getLabelText() {
		return this.dataSourceDTNO!=null ? this.dataSourceDTNO.getCaption() : "Data source not set!";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getLongText()
	 */
	@Override
	public String getLongText() {
		// --- No separate long text ------------
		return this.getLabelText();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getDataSourceUUID()
	 */
	@Override
	public String getDataSourceUUID() {
		return this.dataSourceDTNO!=null ? this.dataSourceDTNO.getShortUUID() : this.uuid;
	}
	
	/**
	 * Sets the uuid.
	 * @param uuid the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
