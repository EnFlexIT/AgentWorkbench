package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.ExcelDataSource;
import de.enflexit.df.core.BundleHelper;

/**
 * The Class DataTreeNodeDataSourceExcel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceExcel extends DataTreeNodeDataSource<ExcelDataSource> {

	/**
	 * Instantiates a new data tree node data source excel.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceExcel(ExcelDataSource dataSource) {
		super(dataSource);
		this.setImageIcon(BundleHelper.getThemedIcon("", ""));
		this.setCaption("New Excel data source");
		this.setTooltipText("Please, configure the Excel file settings ...");
	}

}
