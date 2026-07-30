package de.enflexit.df.impl.excel;

import java.util.List;

import javax.swing.JComponent;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class CsvDataSourceIntegration.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExcelDataSourceIntegration extends AbstractDataSourceIntegration<ExcelDataSource> {

	private ExcelDataSourceDTNO dtnoExcelDataSource;
	private JPanelDataSourceConfigurationExcel jPanelDataSourceConfigurationExcel;
	private JPanelExcelTableConfiguration jPanelExcelTableConfiguration;
	
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
	public AbstractDataSourceDTNO<ExcelDataSource> getDTNO() {
		if (dtnoExcelDataSource==null) {
			dtnoExcelDataSource = new ExcelDataSourceDTNO(this);
		}
		return dtnoExcelDataSource;
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
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getDetailViewPanel()
	 */
	@Override
	public JComponent getDetailViewPanel() {
		if (jPanelExcelTableConfiguration==null) {
			jPanelExcelTableConfiguration = new JPanelExcelTableConfiguration();
		}
		return jPanelExcelTableConfiguration;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetDetailViewPanel()
	 */
	@Override
	public void resetDetailViewPanel() {
		jPanelExcelTableConfiguration = null;
	}

}
