package de.enflexit.awb.timeSeriesDataProvider;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

/**
 * Abstract superclass for the configuration of a data source, which can provide data for multiple data series.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDataSourceConfiguration implements Serializable, PropertyChangeListener{
	
	private static final long serialVersionUID = -3510318054321072622L;
	
	public static final String DATA_SOURCE_RENAMED = "dataSourceRenamed";
	public static final String DATA_SERIES_ADDED = "dataSeriesAdded";
	public static final String DATA_SERIES_REMOVED = "dataSeriesRemoved";
	public static final String DATA_SERIES_CLEARED = "dataSeriesCleared";
	
	private String name;
	
	@XmlElementWrapper(name = "seriesConfigurations")
	@XmlElement(name = "seriesConfiguration")
	private ArrayList<AbstractDataSeriesConfiguration> dataSeriesConfigurations;
	
	private transient ArrayList<PropertyChangeListener> listeners;
	
	/**
	 * Gets the name of this data source.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name for this data source.
	 * @param name the new name
	 */
	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		PropertyChangeEvent sourceRenamed = new PropertyChangeEvent(this, DATA_SOURCE_RENAMED, oldName, name);
		this.notifyListeners(sourceRenamed);
	}

	/**
	 * Provides a list of data series defined for this data source. 
	 * @return the list
	 */
	public ArrayList<AbstractDataSeriesConfiguration> getDataSeriesConfigurations(){
		if (dataSeriesConfigurations==null) {
			dataSeriesConfigurations = new ArrayList<>();
		}
		return dataSeriesConfigurations;
	};
	
	/**
	 * Adds a data series configuration.
	 * @param seriesConfiguration the series configuration
	 */
	public void addDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguration) {
		// --- Notify by default ----------------
		this.addDataSeriesConfiguration(seriesConfiguration, true);
	}
	
	/**
	 * Adds the data series configuration, allows to disable property change notification.
	 * @param seriesConfiguration the series configuration
	 * @param notify specifies if registered {@link PropertyChangeListener}s should be notified
	 */
	public void addDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguration, boolean notify) {
		if (this.getDataSeriesConfigurations().contains(seriesConfiguration)==false) {
			seriesConfiguration.addPropertyChamgeListener(this);
			this.getDataSeriesConfigurations().add(seriesConfiguration);
			if (notify==true) {
				PropertyChangeEvent seriesAdded = new PropertyChangeEvent(this, DATA_SERIES_ADDED, this, seriesConfiguration);
				this.notifyListeners(seriesAdded);
			}
		}
	}
	
	/**
	 * Removes a data series configuration.
	 * @param seriesConfiguration the series configuration
	 */
	public void removeDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguration) {
		if (this.getDataSeriesConfigurations().contains(seriesConfiguration)==true) {
			seriesConfiguration.removePropertyChangeListener(this);
			this.getDataSeriesConfigurations().remove(seriesConfiguration);
			PropertyChangeEvent seriesRemoved = new PropertyChangeEvent(this, DATA_SERIES_REMOVED, null, seriesConfiguration);
			this.notifyListeners(seriesRemoved);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	
	/**
	 * Gets the list of currently registered {@link PropertyChangeListener}s.
	 * @return the listeners
	 */
	private ArrayList<PropertyChangeListener> getListeners() {
		if (listeners==null) {
			listeners = new ArrayList<>();
		}
		return listeners;
	}
	
	/**
	 * Notifies the currently registered {@link PropertyChangeListener}s about an update.
	 * @param changeEvent the change event
	 */
	protected void notifyListeners(PropertyChangeEvent changeEvent) {
		for (int i=0; i<this.getListeners().size(); i++) {
			this.getListeners().get(i).propertyChange(changeEvent);
		}
	}
	
	/**
	 * Adds a new {@link PropertyChangeListener}.
	 * @param listener the listener
	 */
	public void addPropertyChamgeListener(PropertyChangeListener listener) {
		if (this.getListeners().contains(listener)==false) {
			this.getListeners().add(listener);
		}
	}
	
	/**
	 * Removes a {@link PropertyChangeListener}.
	 * @param listener the listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (this.getListeners().contains(listener)==true) {
			this.getListeners().remove(listener);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(AbstractDataSeriesConfiguration.DATA_SERIES_RENAMED)) {
			// --- Pass series rename events on to the listeners ----
			this.notifyListeners(evt);
		}
	}
	
	/**
	 * Override this method to provide a custom icon for the data source tree. If returning null, the default icons will be used.
	 * @param isSelected specifies if the data source is currently selected
	 * @return the image icon
	 */
	public abstract ImageIcon getImageIcon(boolean isSelected, boolean isDarkMode);
	
	/**
	 * Creates a data source according to this configuration
	 * @return the abstract data source
	 */
	public abstract AbstractDataSource createDataSource();
}
