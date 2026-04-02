package de.enflexit.df.core.model;

import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationCsv;

/**
 * The Class DataTreeNodeDataSourceCsv.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceCsv extends DataTreeNodeDataSource<CsvDataSource> {

	private JPanelDataSourceConfigurationCsv jPanelDataSourceConfigurationCsv;
	
	/**
	 * Instantiates a new data tree node data source csv.
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceCsv(CsvDataSource dataSource) {
		super(dataSource);
		this.setImageIcon(BundleHelper.getThemedIcon("CsvFileBlack.png", "CsvFileGrey.png"));
		this.setCaption("New CSV data source");
		this.setTooltipText("Please, configure the CSV File settings ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeDataSource#getJPanelConfiguration()
	 */
	@Override
	public JPanelDataSourceConfigurationCsv getJPanelConfiguration() {
		if (jPanelDataSourceConfigurationCsv==null) {
			jPanelDataSourceConfigurationCsv = new JPanelDataSourceConfigurationCsv(this);
		}
		return jPanelDataSourceConfigurationCsv;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeBase#setCaption(java.lang.String)
	 */
	@Override
	public void setCaption(String caption) {
		super.setCaption(caption);
		if (this.getDataSource()!=null) {
			this.getDataSource().setName(caption);
		}
	}
	
	
	
}
