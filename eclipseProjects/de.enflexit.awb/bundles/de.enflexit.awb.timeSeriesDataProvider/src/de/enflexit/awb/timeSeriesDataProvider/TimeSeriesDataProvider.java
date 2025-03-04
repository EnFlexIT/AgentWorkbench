package de.enflexit.awb.timeSeriesDataProvider;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.dataModel.CsvDataSourceConfiguration;

/**
 * The {@link TimeSeriesDataProvider} can be used to provide time series based data, like 
 * weather data or market prices, that is required by several agents in one central location.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataProvider implements PropertyChangeListener{
	
	private static final TimeSeriesDataProvider instance = new TimeSeriesDataProvider();
	
	public static final String DATA_SOURCE_ADDED = "dataSourceAdded";
	public static final String DATA_SOURCE_REMOVED = "dataSourceRemoved";
	
	private static final String DATA_DIRECTORY = "timeSeriesProvider";
	private static final String CONFIG_FILE_NAME = "DataSourceConfiguration.xml";
	
	private File configFile;

	private TimeSeriesDataProviderConfiguration configuration;
	
	private ArrayList<PropertyChangeListener> listeners;
	
	private HashMap<String, AbstractDataSource> dataSourcesByName;
	
	private HashMap<String, AbstractDataSource> dataSourcesBySeriesName;

	private TimeSeriesDataProvider() {
		// private constructor due to singleton
	}
	
	/**
	 * Gets the single instance of TimeSeriesDataProvider.
	 * @return single instance of TimeSeriesDataProvider
	 */
	public static TimeSeriesDataProvider getInstance() {
		return instance;
	}

	/**
	 * Gets the configuration.
	 * @return the configuration
	 */
	public TimeSeriesDataProviderConfiguration getConfiguration() {
		if (configuration==null) {
			// --- Try to load from file ----------------------------
			configuration = this.loadConfigurationsList();
			if (configuration==null) {
				// --- If not successful, create a new instance -----
				configuration = new TimeSeriesDataProviderConfiguration();
			}
		}
		return configuration;
	}
	/**
	 * Adds a data source configuration.
	 * @param dataSourceConfiguration the data source configuration
	 */
	public void addDataSourceConfiguration(AbstractDataSourceConfiguration dataSourceConfiguration) {
		if (this.getConfiguration().contains(dataSourceConfiguration)==false) {
			dataSourceConfiguration.addPropertyChamgeListener(this);
			this.getConfiguration().add(dataSourceConfiguration);
			PropertyChangeEvent sourceAdded = new PropertyChangeEvent(this, DATA_SOURCE_ADDED, null, dataSourceConfiguration);
			this.notifyListeners(sourceAdded);
		}
	}
	
	/**
	 * Removes a data source configuration.
	 * @param dataSourceConfiguration the data source configuration
	 */
	public void removeDataSourceConfiguration(AbstractDataSourceConfiguration dataSourceConfiguration) {
		if (this.getConfiguration().contains(dataSourceConfiguration)==true) {
			dataSourceConfiguration.removePropertyChangeListener(this);
			this.getConfiguration().remove(dataSourceConfiguration);
			PropertyChangeEvent sourceRemoved = new PropertyChangeEvent(this, DATA_SOURCE_REMOVED, dataSourceConfiguration, null);
			this.notifyListeners(sourceRemoved);
		}
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
	 * Adds a new {@link PropertyChangeListener}.
	 * @param listener the listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
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
	
	/**
	 * Notifies the currently registered {@link PropertyChangeListener}s about an update.
	 * @param changeEvent the change event
	 */
	private void notifyListeners(PropertyChangeEvent changeEvent) {
		for (int i=0; i<this.getListeners().size(); i++) {
			this.getListeners().get(i).propertyChange(changeEvent);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// --- Pass rename events on to the listeners -----
		this.notifyListeners(evt);
	}
	
	/**
	 * Gets the configuration file for the {@link TimeSeriesDataProvider}.
	 * @return the configuration file
	 */
	public File getConfigurationFile() {
		
		if (Application.getProjectFocused()!=null) {

			// --- Determine the name and location for the config file from the project
			Path projectFolderPath = new File(Application.getProjectFocused().getProjectFolderFullPath()).toPath();
			Path configFilePath = projectFolderPath.resolve(DATA_DIRECTORY).resolve(CONFIG_FILE_NAME);
			configFile = configFilePath.toFile();
			
			return configFile;
			
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No project loaded, unable load project-specific time series configuration.");
			return null;
		}
	}

	/**
	 * Stores the current data source configurations to an XML file.
	 */
	public void storeConfigurationsList() {
		this.getConfiguration().storeDataSourceConfigurationsList(this.getConfigurationFile());
	}
	
	/**
	 * Loads the data source configurations from the configuraiton file.
	 * @return the weather data provider configuration
	 */
	public TimeSeriesDataProviderConfiguration loadConfigurationsList() {
		TimeSeriesDataProviderConfiguration configuration = TimeSeriesDataProviderConfiguration.loadDataSourceConfigurationsList(this.getConfigurationFile());
		return configuration;
	}
	
	/**
	 * Gets a HashMap to look up the data source for the specified data series. 
	 * @return the sources by series
	 */
	private HashMap<String, AbstractDataSource> getDataSourcesBySeries() {
		if (dataSourcesBySeriesName==null) {
			dataSourcesBySeriesName = new HashMap<>();
			for (int i=0; i<this.getConfiguration().getDataSourceConfigurations().size(); i++) {
				AbstractDataSourceConfiguration sourceConfig = this.getConfiguration().getDataSourceConfigurations().get(i);
				for (int j=0; j<sourceConfig.getDataSeriesConfigurations().size(); j++) {
					AbstractDataSeriesConfiguration seriesConfig = sourceConfig.getDataSeriesConfigurations().get(j);
					this.getDataSourcesBySeries().put(seriesConfig.getName(), this.getDataSourcesByName().get(sourceConfig.getName()));
				}
			}
		}
		return dataSourcesBySeriesName;
	}
	
	/**
	 * Gets a HashMap to look up the data sources by their name.
	 * @return the data sources
	 */
	private HashMap<String, AbstractDataSource> getDataSourcesByName() {
		if (dataSourcesByName==null) {
			dataSourcesByName = new HashMap<>();
		}
		return dataSourcesByName;
	}
	
	/**
	 * Gets the data source with the specified name. May be null if not found.
	 * @param sourceName the source name
	 * @return the data source
	 */
	public AbstractDataSource getDataSource(String sourceName) {
		
		// --- Check if the requested data source is already available --------
		AbstractDataSource dataSource = this.getDataSourcesByName().get(sourceName);
		
		// --- If not, look for a matching configuration and initialize the source
		if (dataSource==null) {
			for (AbstractDataSourceConfiguration sourceConfig : this.getConfiguration().getDataSourceConfigurations()) {
				if (sourceConfig.getName().equals(sourceName)) {
					dataSource = this.initializeDataSource(sourceConfig);
					this.getDataSourcesByName().put(sourceConfig.getName(), dataSource);
					break;
				}
			}
		}
		
		return dataSource;
	}
	
	/**
	 * Initializes a data source.
	 * @param sourceConfig the data source configuration
	 * @return the abstract data source
	 */
	private AbstractDataSource initializeDataSource(AbstractDataSourceConfiguration sourceConfig) {
		AbstractDataSource dataSource = null;
		if (sourceConfig instanceof CsvDataSourceConfiguration) {
			dataSource = new CsvDataSource((CsvDataSourceConfiguration) sourceConfig);
		}
		return dataSource;
	}
	
	/**
	 * Gets the value for the specified time from the specified series. May return null if either 
	 * there is no series with this name, or the timestamp is outside the series time range
	 * @param seriesName the series name
	 * @param timestamp the timestamp
	 * @return the value 
	 */
	public Double getValue(String seriesName, long timestamp) {
		Double value = null;
		AbstractDataSource dataSource = this.getDataSourcesBySeries().get(seriesName);
		if (dataSource!=null) {
			value = dataSource.getValue(seriesName, timestamp);
		}
		return value;
	}
	
	/**
	 * Gets all values from the specified data series.
	 * @param seriesName the series name
	 * @return the data series values
	 */
	public List<Double> getDataSeriesValues(String seriesName){
		List<Double> valuesList = null;
		AbstractDataSource dataSource = this.getDataSourcesBySeries().get(seriesName);
		if (dataSource!=null) {
			valuesList = dataSource.getDataSeriesValues(seriesName);
		}
		return valuesList;
	}
	
	/**
	 * Resets all data sources.
	 */
	public void resetDataSources() {
		this.dataSourcesByName = null;
		this.dataSourcesBySeriesName = null;
	}
	
}
