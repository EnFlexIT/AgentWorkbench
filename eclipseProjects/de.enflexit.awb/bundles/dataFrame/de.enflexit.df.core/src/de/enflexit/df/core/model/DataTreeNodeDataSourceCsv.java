package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.df.core.BundleHelper;

/**
 * The Class DataTreeNodeDataSourceCsv.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceCsv extends DataTreeNodeDataSource<CsvDataSource> {

	/**
	 * Instantiates a new data tree node data source csv.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceCsv(CsvDataSource dataSource) {
		super(dataSource);
		this.setImageIcon(BundleHelper.getThemedIcon("", ""));
		this.setCaption("New CSV data source");
		this.setTooltipText("Please, configure the CSV File settings ...");
	}
	
}
