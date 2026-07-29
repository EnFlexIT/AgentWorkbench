package de.enflexit.df.impl.excel;

import de.enflexit.df.core.dataSources.integration.AbstractDTNO_DataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class CsvDataSourceIntegration.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExcelDataSourceIntegration extends AbstractDataSourceIntegration<ExcelDataSource> {

	/**
	 * Instantiates a new database data source integration.
	 */
	public ExcelDataSourceIntegration() {
		super();
	}
	/**
	 * Instantiates a new database data source integration.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public ExcelDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, ExcelDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDataTreeNodeObject()
	 */
	@Override
	public AbstractDTNO_DataSource<ExcelDataSource> getDataTreeNodeObject() {
		return new DTNO_ExcelDataSource(this.getDataController(), this.getDataWorkbook(), this.getDataSource());
	}

}
