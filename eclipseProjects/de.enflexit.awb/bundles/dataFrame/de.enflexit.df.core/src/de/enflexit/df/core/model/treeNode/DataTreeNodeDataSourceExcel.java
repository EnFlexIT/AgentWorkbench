package de.enflexit.df.core.model.treeNode;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.common.dataSources.ExcelDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.data.PaginationDataLoader;
import de.enflexit.df.core.data.PaginationDataLoader4Excel;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationExcel;

/**
 * The Class DataTreeNodeDataSourceExcel.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceExcel extends AbstractDataTreeNodeDataSource<ExcelDataSource> {

	private JPanelDataSourceConfigurationExcel jPanelDataSourceConfigurationExcel;

	private PaginationDataLoader4Excel paginationDataLoader4Excel;
	
	/**
	 * Instantiates a new data tree node data source excel.
	 *
	 * @param dataController the data controller
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceExcel(DataController dataController, ExcelDataSource dataSource) {
		super(dataController, dataSource);
		if (dataSource.getName()==null) {
			dataSource.setName("New Excel data source");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("MS-Excel-Light.png", "MS-Excel-Dark.png"));
		this.setTooltipText("Please, configure the Excel file settings ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		if (jPanelDataSourceConfigurationExcel==null) {
			jPanelDataSourceConfigurationExcel = new JPanelDataSourceConfigurationExcel(this.getDataController(), this);
		}
		return jPanelDataSourceConfigurationExcel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource#getPaginationDataLoader()
	 */
	@Override
	public PaginationDataLoader<ExcelDataSource> getPaginationDataLoader() {
		if (paginationDataLoader4Excel==null) {
			paginationDataLoader4Excel = new PaginationDataLoader4Excel(this.getDataSource());
		}
		return paginationDataLoader4Excel;
	}
	

}
