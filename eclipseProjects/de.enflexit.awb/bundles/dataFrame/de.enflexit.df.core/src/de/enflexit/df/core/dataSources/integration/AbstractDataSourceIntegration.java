package de.enflexit.df.core.dataSources.integration;

import de.enflexit.df.core.dataSources.DataSourceIntegration;
import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class AbstractDataSourceIntegration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param <DS> the generic type
 */
public abstract class AbstractDataSourceIntegration<DS extends DefaultDataSource> implements DataSourceIntegration<DS> {

	private DataController dataController;
	private DataWorkbook dataWorkbook;
	private DS dataSource;

	/**
	 * Instantiates a new abstract data source integration.
	 */
	public AbstractDataSourceIntegration() {
		this(null, null, null);
	}
	/**
	 * Instantiates a new abstract data source integration.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public AbstractDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, DS dataSource) {
		this.setDataController(dataController);
		this.setDataWorkbook(dataWorkbook);
		this.setDataSource(dataSource);
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#setDataController(de.enflexit.df.core.model.DataController)
	 */
	@Override
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDataController()
	 */
	@Override
	public DataController getDataController() {
		return dataController;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#setDataWorkbook(de.enflexit.df.core.workbook.DataWorkbook)
	 */
	@Override
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDataWorkbook()
	 */
	@Override
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#setDataSource(de.enflexit.df.core.dataSources.DefaultDataSource)
	 */
	@Override
	public void setDataSource(DS dataSource) {
		this.dataSource = dataSource;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDataSource()
	 */
	@Override
	public DS getDataSource() {
		return dataSource;
	}


}
