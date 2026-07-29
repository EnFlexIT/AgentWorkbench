package de.enflexit.df.impl.csv;

import de.enflexit.df.core.dataSources.integration.AbstractDTNO_DataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class CsvDataSourceIntegration.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class CsvDataSourceIntegration extends AbstractDataSourceIntegration<CsvDataSource> {

	/**
	 * Instantiates a new csv data source integration.
	 */
	public CsvDataSourceIntegration() {
		super();
	}
	/**
	 * Instantiates a new csv data source integration.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public CsvDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, CsvDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDataTreeNodeObject()
	 */
	@Override
	public AbstractDTNO_DataSource<CsvDataSource> getDataTreeNodeObject() {
		return new DTNO_CsvDataSource(this.getDataController(), this.getDataWorkbook(), this.getDataSource());
	}

}
