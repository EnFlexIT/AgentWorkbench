package de.enflexit.awb.timeSeriesDataProvider;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import de.enflexit.awb.timeSeriesDataProvider.csv.CsvDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.csv.CsvDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataScourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.jdbc.JDBCDataSeriesConfiguration;

@XmlRootElement
public class TimeSeriesDataProviderConfiguration implements Serializable {
	
	private static final long serialVersionUID = -7283966338745638834L;
	
	private ArrayList<AbstractDataSourceConfiguration> dataSourceConfigurations;
	
	private static Class<?>[] contextClasses;
	
	@XmlTransient
	private ArrayList<PropertyChangeListener> listeners;

	/**
	 * Gets the data source configurations.
	 * @return the data source configurations
	 */
	public ArrayList<AbstractDataSourceConfiguration> getDataSourceConfigurations() {
		if (dataSourceConfigurations==null) {
			dataSourceConfigurations = new ArrayList<>();
		}
		return dataSourceConfigurations;
	}

	/**
	 * Sets the data source configurations.
	 * @param dataSourceCOnfigurations the new data source configurations
	 */
	public void setDataSourceConfigurations(ArrayList<AbstractDataSourceConfiguration> dataSourceCOnfigurations) {
		this.dataSourceConfigurations = dataSourceCOnfigurations;
	}

	/**
	 * Stores the data source configurations list to the configuration file.
	 * @param configurationFile the configuration file
	 */
	public void storeDataSourceConfigurationsList(File configurationFile) {
		try {
			
			// --- Make sure the parent directory exists, if saving for the first time -- 
			if (configurationFile.getParentFile().exists()==false) {
				configurationFile.getParentFile().mkdir();
			}
			
			JAXBContext context = JAXBContext.newInstance(getContextClasses());
			Marshaller marshaller = context.createMarshaller(); 
			marshaller.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 
			
			FileWriter fileWriter = new FileWriter(configurationFile);
			marshaller.marshal(this, fileWriter);
			fileWriter.close();
			
		} catch (JAXBException ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error storing configuration data in xml format!");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error writing the configuration file!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Load the data source configurations list from the configuration file.
	 * @param configurationFile the configuration file
	 * @return the time series data provider configuration
	 */
	public static TimeSeriesDataProviderConfiguration loadDataSourceConfigurationsList(File configurationFile) {
		TimeSeriesDataProviderConfiguration configuration = null;
		if (configurationFile.exists()) {
			try {
				JAXBContext context = JAXBContext.newInstance(getContextClasses());
				Unmarshaller unmarshaller = context.createUnmarshaller();
				
				FileReader reader = new FileReader(configurationFile);
				configuration = (TimeSeriesDataProviderConfiguration) unmarshaller.unmarshal(reader);
				reader.close();
				
				registerListeners(configuration);
				
			} catch (JAXBException ex) {
				System.err.println("[" + TimeSeriesDataProviderConfiguration.class.getSimpleName() + "] Error loading configuration data from xml format!");
				ex.printStackTrace();
			} catch (FileNotFoundException ex) {
				System.err.println("[" + TimeSeriesDataProviderConfiguration.class.getSimpleName() + "] Configuration file not found!");
				ex.printStackTrace();
			} catch (IOException ex) {
				System.err.println("[" + TimeSeriesDataProviderConfiguration.class.getSimpleName() + "] Error reading the configuration file!");
				ex.printStackTrace();
			}
		}
		return configuration;
	}
	
	/**
	 * Registers the required listeners to all data sources and series.
	 * @param configuration the configuration
	 */
	private static void registerListeners(TimeSeriesDataProviderConfiguration configuration) {
		for (int i=0; i<configuration.getDataSourceConfigurations().size(); i++) {
			AbstractDataSourceConfiguration sourceConfig = configuration.getDataSourceConfigurations().get(i);
			sourceConfig.addPropertyChamgeListener(TimeSeriesDataProvider.getInstance());
			for (int j=0; j<sourceConfig.getDataSeriesConfigurations().size(); j++) {
				sourceConfig.getDataSeriesConfigurations().get(j).addPropertyChamgeListener(sourceConfig);
			}
		}
	}

	/**
	 * Checks if the list of known data source configurations contains the specified configuration.
	 * @param dataSourceConfiguration the data source configuration
	 * @return true, if successful
	 */
	public boolean contains(AbstractDataSourceConfiguration dataSourceConfiguration) {
		return this.getDataSourceConfigurations().contains(dataSourceConfiguration);
	}

	/**
	 * Adds the provided data source configuration to the list.
	 * @param dataSourceConfiguration the data source configuration
	 */
	public void add(AbstractDataSourceConfiguration dataSourceConfiguration) {
		if (this.getDataSourceConfigurations().contains(dataSourceConfiguration)==false) {
			this.getDataSourceConfigurations().add(dataSourceConfiguration);
		}
	}

	/**
	 * Removes the provided data source configuration from the list.
	 * @param dataSourceConfiguration the data source configuration
	 */
	public void remove(AbstractDataSourceConfiguration dataSourceConfiguration) {
		if (this.getDataSourceConfigurations().contains(dataSourceConfiguration)==true) {
			this.getDataSourceConfigurations().remove(dataSourceConfiguration);
		}
	}

	/**
	 * Gets the necessary context classes for loading or saving the configuration.
	 * @return the context classes
	 */
	private static Class<?>[] getContextClasses() {
		if (contextClasses==null) {
			ArrayList<Class<?>>contextClassesList = new ArrayList<>();
			contextClassesList.add(TimeSeriesDataProviderConfiguration.class);
			contextClassesList.add(AbstractDataSourceConfiguration.class);
			contextClassesList.add(AbstractDataSeriesConfiguration.class);
			contextClassesList.add(CsvDataSourceConfiguration.class);
			contextClassesList.add(CsvDataSeriesConfiguration.class);
			contextClassesList.add(JDBCDataScourceConfiguration.class);
			contextClassesList.add(JDBCDataSeriesConfiguration.class);
			
			contextClasses = contextClassesList.toArray(new Class<?>[contextClassesList.size()]);
		}
		return contextClasses;
	}
	
}
