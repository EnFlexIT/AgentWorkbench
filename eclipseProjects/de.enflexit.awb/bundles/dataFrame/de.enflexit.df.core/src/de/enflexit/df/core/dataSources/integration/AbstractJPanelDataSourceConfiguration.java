package de.enflexit.df.core.dataSources.integration;

import javax.swing.JPanel;

import de.enflexit.df.core.dataSources.DataSource;
import de.enflexit.df.core.dataSources.DataSourceIntegration;
import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class AbstractJPanelDataSourceConfiguration.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractJPanelDataSourceConfiguration<DS extends DefaultDataSource, DSI extends DataSourceIntegration<DS>> extends JPanel {

	private static final long serialVersionUID = 6426491391209948791L;

	private DSI dsIntegration;
	
	/**
	 * Instantiates a new AbstractJPanelDataSourceConfiguration.
	 *
	 * @param dataController the current data controller
	 * @param dsTreeNode the ds tree node
	 */
	public AbstractJPanelDataSourceConfiguration(DSI dsIntegration) {
		this.setDataSourceIntegration(dsIntegration);
	}
	
	/**
	 * Returns the data source integration.
	 * @return the data source integration
	 */
	public DSI getDataSourceIntegration() {
		return dsIntegration;
	}
	/**
	 * Sets the data source integration.
	 * @param dsIntegration the new data source integration
	 */
	public void setDataSourceIntegration(DSI dsIntegration) {
		this.dsIntegration = dsIntegration;
	}
	
	
	// --- Derived from the DataSourceIntegration -----------------------------
	/**
	 * Gets the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return getDataSourceIntegration().getDataController();
	}
	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	protected DataWorkbook getDataWorkbook() {
		return this.getDataSourceIntegration().getDataWorkbook();
	}
	/**
	 * Has to return the current {@link DataSource}.
	 * @return the data source
	 */
	protected DS getDataSource() {
		return this.getDataSourceIntegration().getDataSource();
	}
	
	/**
	 * Returns the 'Data Tree Node Object' (DTNO) for the current {@link DataSource}.
	 * @return the dtno
	 */
	protected AbstractDataSourceDTNO<DS> getDTNO() {
		return this.getDataSourceIntegration().getDTNO();
	}
	
	/**
	 * Informs the data controller about settings changes.
	 */
	protected void informDataSourceSettingChanged(String changedValue) {
		if (this.getDataController()!=null) {
			this.getDataController().firePropertyChange(DataController.DC_DATA_SOURCE_CONFIGURATION_CHANGED, changedValue, this.getDataSource());	
		}
	}
	
	
}
