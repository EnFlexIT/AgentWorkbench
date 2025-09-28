package de.enflexit.awb.timeSeriesDataProvider;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.ApplicationListener;

/**
 * The {@link TimeSeriesDataProvider} can be used to provide time series based data, like 
 * weather data or market prices, that is required by several agents in one central location.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataProvider implements PropertyChangeListener, ApplicationListener{
	
	/**
	 * The different scopes/levels, on which data source configurations can be managed. 
	 */
	public static enum ConfigurationScope{
		APPLICATION, PROJECT
	}
	
	private static TimeSeriesDataProvider instance;
	
	public static final String DATA_SOURCE_ADDED = "dataSourceAdded";
	public static final String DATA_SOURCE_REMOVED = "dataSourceRemoved";
	
	private static final String DATA_DIRECTORY = "timeSeriesProvider";
	private static final String CONFIG_FILE_NAME = "DataSourceConfiguration.xml";
	
	private ArrayList<PropertyChangeListener> listeners;
	
	private HashMap<String, AbstractDataSourceConfiguration> dataSourceConfigurations;
	
	private HashMap<String, AbstractDataSource> dataSourcesByName;
	
	/**
	 * Instantiates a new time series data provider.
	 */
	private TimeSeriesDataProvider() {
		// private constructor due to singleton
	}
	
	/**
	 * Gets the single instance of TimeSeriesDataProvider.
	 * @return single instance of TimeSeriesDataProvider
	 */
	public static TimeSeriesDataProvider getInstance() {
		if (instance==null) {
			instance = new TimeSeriesDataProvider();
			instance.loadProviderConfigurations();
			Application.addApplicationListener(instance);
		}
		return instance;
	}
	
	/**
	 * Gets the data source configurations.
	 * @return the data source configurations
	 */
	public HashMap<String, AbstractDataSourceConfiguration> getDataSourceConfigurations() {
		if (dataSourceConfigurations==null) {
			dataSourceConfigurations = new HashMap<String, AbstractDataSourceConfiguration>();
		}
		return dataSourceConfigurations;
	}
	
	/**
	 * Loads all provider configurations.
	 */
	private void loadProviderConfigurations() {
		for(ConfigurationScope configScope : ConfigurationScope.values()) {
			this.loadProviderConfiguration(configScope);
		}
	}
	
	/**
	 * Gets the provider configuration for the specified configuration scope.
	 * @param configurationScope the configuration scope
	 * @return the provider configuration
	 */
	public void loadProviderConfiguration(ConfigurationScope configurationScope) {
		File providerConfigFile = this.getProviderConfigurationFile(configurationScope);
		TimeSeriesDataProviderConfiguration providerConfig = TimeSeriesDataProviderConfiguration.loadDataSourceConfigurationsList(providerConfigFile);
		if (providerConfig!=null) {
			for (AbstractDataSourceConfiguration sourceConfig : providerConfig.getDataSourceConfigurations()) {
				sourceConfig.setConfigurationScope(configurationScope);
				this.addDataSourceConfiguration(sourceConfig);
			}
		}
	}

	/**
	 * Adds a data source configuration.
	 * @param dataSourceConfiguration the data source configuration
	 */
	public void addDataSourceConfiguration(AbstractDataSourceConfiguration dataSourceConfiguration) {
			dataSourceConfiguration.addPropertyChamgeListener(this);
			this.getDataSourceConfigurations().put(dataSourceConfiguration.getName(), dataSourceConfiguration);
			PropertyChangeEvent sourceAdded = new PropertyChangeEvent(this, DATA_SOURCE_ADDED, null, dataSourceConfiguration);
			this.notifyListeners(sourceAdded);
	}
	
	/**
	 * Removes a data source configuration.
	 * @param dataSourceConfiguration the data source configuration
	 */
	public void removeDataSourceConfiguration(AbstractDataSourceConfiguration dataSourceConfiguration) {
		if (this.getDataSourceConfigurations().containsKey(dataSourceConfiguration.getName())) {
			dataSourceConfiguration.removePropertyChangeListener(this);
			this.getDataSourcesByName().remove(dataSourceConfiguration.getName());
			this.getDataSourceConfigurations().remove(dataSourceConfiguration.getName());
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
	public File getProjectScopeConfigurationFile() {
		
		if (Application.getProjectFocused()!=null) {

			// --- Determine the name and location for the config file from the project
			Path projectFolderPath = new File(Application.getProjectFocused().getProjectFolderFullPath()).toPath();
			Path configFilePath = projectFolderPath.resolve(DATA_DIRECTORY).resolve(CONFIG_FILE_NAME);
			File configFile = configFilePath.toFile();
			
			return configFile;
			
		} else {
//			System.err.println("[" + this.getClass().getSimpleName() + "] No project loaded, unable load project-specific time series configuration.");
			return null;
		}
	}
	
	public File getApplicationScopeConfigurationFile() {
		File configFile = null;
		Path propertiesPath = Application.getGlobalInfo().getPathProperty(true);
		configFile = propertiesPath.resolve(CONFIG_FILE_NAME).toFile();
		return configFile;
	}
	
	public void storeProviderConfigurations() {
		for (ConfigurationScope configScope : ConfigurationScope.values()) {
			
			ArrayList<AbstractDataSourceConfiguration> configsForScope = new ArrayList<AbstractDataSourceConfiguration>();
			for(AbstractDataSourceConfiguration sourceConfig : this.getDataSourceConfigurations().values()) {
				if (sourceConfig.getConfigurationScope()==configScope) {
					configsForScope.add(sourceConfig);
				}
			}
			
			File configFile = this.getProviderConfigurationFile(configScope);
			if (configsForScope.size()>0) {
				if (configFile!=null) {
					TimeSeriesDataProviderConfiguration scopeConfig = new TimeSeriesDataProviderConfiguration();
					scopeConfig.getDataSourceConfigurations().addAll(configsForScope);
					scopeConfig.storeDataSourceConfigurationsList(configFile);
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] No config file specified for configuration scope " + configScope);
				}
			} else if (configsForScope.size()==0 && configFile.exists()) {
				// --- If there are no configs but an existing config file, delete the file 
				configFile.delete();
			}
			
		}
	}
	
	/**
	 * Gets the configuration file for the specified {@link ConfigurationScope}.
	 * @param configurationScope the configuration scope
	 * @return the configuration file
	 */
	private File getProviderConfigurationFile(ConfigurationScope configurationScope) {
		File configFile = null;
		if (configurationScope==ConfigurationScope.APPLICATION) {
			configFile = this.getApplicationScopeConfigurationFile();
		} else if (configurationScope==ConfigurationScope.PROJECT) {
			configFile = this.getProjectScopeConfigurationFile();
		}
		return configFile;
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
			AbstractDataSourceConfiguration sourceConfig = this.getDataSourceConfigurations().get(sourceName);
			if (sourceConfig.getName().equals(sourceName)) {
				dataSource = sourceConfig.createDataSource();
				if (dataSource!=null) {
					this.getDataSourcesByName().put(sourceConfig.getName(), dataSource);
				}
			}
		}
		return dataSource;
	}
	
	
	/**
	 * Resets all data sources.
	 */
	public void resetDataSources() {
		this.dataSourceConfigurations = null;
		this.dataSourcesByName = null;
	}
	
	/**
	 * Gets a list of the configured data sources.
	 * @return the data source names
	 */
	public List<String> getAvailableDataSourceNames(){
		return new ArrayList<String>(this.getDataSourceConfigurations().keySet());
	}
	
	/**
	 * Removes the data source.
	 * @param dataSourceToDelete the data source to delete
	 */
	public void removeDataSource(String dataSourceName) {
		this.getDataSourceConfigurations().remove(dataSourceName);
		this.getDataSourcesByName().remove(dataSourceName);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ApplicationListener#onApplicationEvent(de.enflexit.awb.core.ApplicationListener.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent ae) {
		if (ae.getApplicationEvent().equals(ApplicationEvent.PROJECT_FOCUSED)) {
			// --- Remove data sources related to the previous project, if any
			this.removeProjectScopeDataSources();
			// --- Load the data sources related to the current project
			if (ae.getEventObject()!=null) {
				this.loadProviderConfiguration(ConfigurationScope.PROJECT);
			}
		}
	}
	
	/**
	 * Removes all data sources with project scope from the lists.
	 */
	private void removeProjectScopeDataSources() {
		// --- First, collect all data sources with project scope
		ArrayList<AbstractDataSourceConfiguration> configsToRemove = new ArrayList<AbstractDataSourceConfiguration>();
		for (String sourceName : this.getDataSourceConfigurations().keySet()) {
			AbstractDataSourceConfiguration sourceConfig = this.getDataSourceConfigurations().get(sourceName);
			if (sourceConfig.getConfigurationScope()==ConfigurationScope.PROJECT) {
				configsToRemove.add(sourceConfig);
			}
		}
		// --- Next, remove the corresponding sources and configurations
		for (AbstractDataSourceConfiguration sourceConfig: configsToRemove) {
			this.removeDataSourceConfiguration(sourceConfig);
		}
	}
	
}
