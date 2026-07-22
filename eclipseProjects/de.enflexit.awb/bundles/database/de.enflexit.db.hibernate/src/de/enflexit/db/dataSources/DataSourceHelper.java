package de.enflexit.db.dataSources;

import java.util.HashMap;
import java.util.List;

import de.enflexit.common.ServiceFinder;

/**
 * The Class DataSourceHelper.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataSourceHelper {

	/**
	 * Returns the data source services.
	 * @return the data source services
	 */
	public static List<DataSource> getDataSourceServices() {
		return ServiceFinder.findServices(DataSource.class);
	}
	/**
	 * Returns a HashMap containing the data source identifier and its DataSource .
	 * @return the data source service hash map
	 */
	public static HashMap<String, DataSource> getDataSourceServiceHashMap() {
		HashMap<String, DataSource> dsHashMap = new HashMap<>();
		DataSourceHelper.getDataSourceServices().forEach(ds -> dsHashMap.put(ds.getDataSourceIdentifier(), ds));
		return dsHashMap;
	}
	
	
	
	/**
	 * Converts the specified data source to a DefaultDataSource.
	 * @param ds the data source to convert
	 */
	public static DefaultDataSource toDefaultDataSource(DefaultDataSource ds) {

		DefaultDataSource dDataSource = new DefaultDataSource();
		dDataSource.setId(ds.getId());
		dDataSource.setName(ds.getName());
		dDataSource.setDescription(ds.getDescription());
		dDataSource.setRowsPerPage(ds.getRowsPerPage());
		dDataSource.setStorageConfiguration(ds.getStorageConfiguration());
		return dDataSource;
	}
	
	/**
	 * Converts the specified DefaultDataSource to a specific DataSource by using the storage configuration.
	 *
	 * @param absDS the abs DS
	 * @return the default data source
	 */
	public static DefaultDataSource toSpecificDataSource(DefaultDataSource absDS) {
		return DataSourceHelper.toSpecificDataSource(DataSourceHelper.getDataSourceServiceHashMap(), absDS);
	}
	/**
	 * Converts the specified DefaultDataSource to a specific DataSource by using the storage configuration.
	 *
	 * @param dsHashMap the data source HashMap
	 * @param dDS the DefaultDataSource instance to convert 
	 * @return the specific data source derived from the storage configuration
	 */
	public static DefaultDataSource toSpecificDataSource(HashMap<String, DataSource> dsHashMap, DefaultDataSource dDS) {

		if (dDS==null || dDS.getStorageConfiguration()==null) return null;
		
		// --- Get data source ID and configuration -----------------
		int cutIdent = dDS.getStorageConfiguration().indexOf("::"); 
		String dataSourceIdentifier = dDS.getStorageConfiguration().substring(0, cutIdent); 
		String configurationString = dDS.getStorageConfiguration().substring(cutIdent + 2);
		
		// --- Try to find the corresponding service ----------------
		DataSource ds = dsHashMap.get(dataSourceIdentifier);
		if (ds==null) return null;
		
		// --- Load the configuration to the actual DataSource ------
		DefaultDataSource actDS = ds.newInstance();
		try {
			actDS.fromConfigurationString(configurationString);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return actDS;
	}
	
	
}
