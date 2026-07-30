package de.enflexit.df.impl.csv;

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
public class CsvDataSourceIntegration extends AbstractDataSourceIntegration<CsvDataSource> {

	private CsvDataSourceDTNO dtnoCsvDataSource;
	private JPanelDataSourceConfigurationCsv jPanelDataSourceConfigurationCsv;

	
	/**
	 * Instantiates a new csv data source integration.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public CsvDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, CsvDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDTNO()
	 */
	@Override
	public AbstractDataSourceDTNO<CsvDataSource> getDTNO() {
		if (dtnoCsvDataSource==null) {
			dtnoCsvDataSource =new CsvDataSourceDTNO(this); 
		}
		return dtnoCsvDataSource;
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
		if (jPanelDataSourceConfigurationCsv==null) {
			jPanelDataSourceConfigurationCsv = new JPanelDataSourceConfigurationCsv(this);
		}
		return jPanelDataSourceConfigurationCsv;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {
		this.jPanelDataSourceConfigurationCsv = null;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getDetailViewPanel()
	 */
	@Override
	public JComponent getDetailViewPanel() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetDetailViewPanel()
	 */
	@Override
	public void resetDetailViewPanel() { }

}
