package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfiguration;

/**
 * The Class DataTreeNodeDataSource.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class DataTreeNodeDataSource<DS extends AbstractDataSource> extends DataTreeNodeBase {

	private DS dataSource;

	/**
	 * Instantiates a new data tree node data source.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSource(DS dataSource) {
		this.setDataSource(dataSource);
	}
	
	/**
	 * Returns the data source description.
	 * @return the data source
	 */
	public DS getDataSource() {
		return dataSource;
	}
	/**
	 * Sets the data source.
	 * @param dataSource the new data source
	 */
	public void setDataSource(DS dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Has to return the JPanel for the configuration of the data source.
	 * @return the JPanel for the configuration
	 */
	public abstract JPanelDataSourceConfiguration<?> getJPanelConfiguration();
	
}
