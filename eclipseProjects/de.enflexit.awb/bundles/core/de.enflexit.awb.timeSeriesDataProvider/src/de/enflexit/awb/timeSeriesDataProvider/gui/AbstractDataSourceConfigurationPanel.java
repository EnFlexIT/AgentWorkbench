package de.enflexit.awb.timeSeriesDataProvider.gui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;

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
	public abstract AbstractDataSourceConfiguration getDataSourceConfiguration();
	
	/**
	 * Sets the data source configuration.
	 * @param sourceConfiguration the new data source configuration
	 */
	public abstract void setDataSourceConfiguration(AbstractDataSourceConfiguration sourceConfiguration);
	
	/**
	 * Sets the data series configuration.
	 * @param seriesConfiguraiton the new data series configuration
	 */
	public abstract void setDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguraiton);
	
	
	/**
	 * Rename the current data source.
	 */
	protected boolean renameDataSource(String newName) {
		
		AbstractDataSourceConfiguration existingConfig = TimeSeriesDataProvider.getInstance().getDataSourceConfigurations().get(newName);
		if (existingConfig!=null && existingConfig!=this.getDataSourceConfiguration()) {
			JOptionPane.showMessageDialog(this, "A configuration of this name already exists, please choose a different name!", "Name already in use!", JOptionPane.WARNING_MESSAGE);
			return false;	// Indicate that the new name was rejected
		}
		
		if (this.getDataSourceConfiguration().getName().equals(newName)==false) {
			this.getDataSourceConfiguration().setName(newName);
		}
		return true;
	}
}
