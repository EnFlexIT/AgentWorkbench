package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.ExcelDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationExcel;

/**
 * The Class DataTreeNodeDataSourceExcel.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceExcel extends DataTreeNodeDataSource<ExcelDataSource> {

	private JPanelDataSourceConfigurationExcel jPanelDataSourceConfigurationExcel;
	
	/**
	 * Instantiates a new data tree node data source excel.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceExcel(ExcelDataSource dataSource) {
		super(dataSource);
		this.setImageIcon(BundleHelper.getThemedIcon("MS-Excel-Light.png", "MS-Excel-Dark.png"));
		this.setCaption("New Excel data source");
		this.setTooltipText("Please, configure the Excel file settings ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeDataSource#getJPanelConfiguration()
	 */
	@Override
	public JPanelDataSourceConfigurationExcel getJPanelConfiguration() {
		if (jPanelDataSourceConfigurationExcel==null) {
			jPanelDataSourceConfigurationExcel = new JPanelDataSourceConfigurationExcel(this);
		}
		return jPanelDataSourceConfigurationExcel;
	}

}
