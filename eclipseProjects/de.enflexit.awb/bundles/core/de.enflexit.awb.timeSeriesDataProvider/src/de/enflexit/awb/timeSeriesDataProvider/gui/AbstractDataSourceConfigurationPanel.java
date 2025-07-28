package de.enflexit.awb.timeSeriesDataProvider.gui;

import javax.swing.JPanel;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;

/**
 * Abstract superclass for data source configuration panels.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDataSourceConfigurationPanel extends JPanel {
	
	private static final long serialVersionUID = 7653253507163831612L;

	/**
	 * Gets the data source configuraiton.
	 * @return the data source configuraiton
	 */
	public abstract AbstractDataSourceConfiguration getDataSourceConfiguraiton();
	
	/**
	 * Sets the data source configuration.
	 * @param sourceConfiguration the new data source configuration
	 */
	public abstract void setDataSourceConfiguration(AbstractDataSourceConfiguration sourceConfiguration);
	
	public abstract void setDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguraiton);
}
