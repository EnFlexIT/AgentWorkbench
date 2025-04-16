package de.enflexit.awb.timeSeriesDataProvider.dataModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;

/**
 * Abstract superclass for the configuration of a single data source for the {@link TimeSeriesDataProvider}. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDataSeriesConfiguration implements Serializable{
	
	private static final long serialVersionUID = 1276027152190975061L;
	
	public static final String DATA_SERIES_RENAMED = "dataSeriesRenamed";
	
	private transient ArrayList<PropertyChangeListener> listeners;

	private String name;
	/**
	 * Gets the identifier of this data series.
	 * @return the identifier
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of this data series.
	 * @param name the new name
	 */
	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		
		PropertyChangeEvent seriesRenamed = new PropertyChangeEvent(this, DATA_SERIES_RENAMED, oldName, name);
		this.notifyListener(seriesRenamed);
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
	protected void notifyListener(PropertyChangeEvent changeEvent) {
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
}
