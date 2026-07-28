package de.enflexit.df.impl.excel;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDTNO_DataSource;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class DTNO_ExcelDataSource.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DTNO_ExcelDataSource extends AbstractDTNO_DataSource<ExcelDataSource> {

	private JPanelDataSourceConfigurationExcel jPanelDataSourceConfigurationExcel;

	private PaginationDataLoader4Excel paginationDataLoader4Excel;
	
	/**
	 * Instantiates a new data tree node data source excel.
	 *
	 * @param dataController the data controller
	 * @param dataSource the data source
	 */
	public DTNO_ExcelDataSource(DataController dataController, DataWorkbook dataWorkbook, ExcelDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
		if (dataSource.getName()==null) {
			dataSource.setName("New Excel data source");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("MS-Excel-Light.png", "MS-Excel-Dark.png"));
		this.setTooltipText("Please, configure the Excel file settings ...");
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		if (jPanelDataSourceConfigurationExcel==null) {
			jPanelDataSourceConfigurationExcel = new JPanelDataSourceConfigurationExcel(this);
		}
		return jPanelDataSourceConfigurationExcel;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {
		this.jPanelDataSourceConfigurationExcel = null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.AbstractDTNO_DataSource#getPaginationDataLoader()
	 */
	@Override
	public AbstractPaginationDataLoader<ExcelDataSource> getPaginationDataLoader() {
		if (paginationDataLoader4Excel==null) {
			paginationDataLoader4Excel = new PaginationDataLoader4Excel(this.getDataSource());
		}
		return paginationDataLoader4Excel;
	}
	

}
