package de.enflexit.df.core.dataSources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.enflexit.common.ServiceFinder;
import de.enflexit.df.impl.csv.CsvDataSource;
import de.enflexit.df.impl.db.DatabaseDataSource;
import de.enflexit.df.impl.excel.ExcelDataSource;

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
	 * Returns external data source services only, which means every {@link DataSource} that 
	 * is NOT located in the local bundle 'de.enflexit.df.core'.
	 * 
	 * @return the external data source services
	 */
	public static List<DataSource> getExternalDataSourceServices() {
		
		// --- Remove local DataSources from DataSourceServiceHashMap ---------
		HashMap<String, DataSource> dsHashMap = DataSourceHelper.getDataSourceServiceHashMap();
		dsHashMap.remove(CsvDataSource.class.getSimpleName());
		dsHashMap.remove(ExcelDataSource.class.getSimpleName());
		dsHashMap.remove(DatabaseDataSource.class.getSimpleName());
		
		List<DataSource> extDataSourceList = new ArrayList<>(); 
		extDataSourceList.addAll(dsHashMap.values());
		
		if (extDataSourceList.size()>0) {
			Collections.sort(extDataSourceList, new Comparator<DataSource>() {
				@Override
				public int compare(DataSource ds1, DataSource ds2) {
					return ds1.getDataSourceIdentifier().compareTo(ds2.getDataSourceIdentifier());
				}
			});
		}
		
		return extDataSourceList;
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
		
		// --- Transfer the sub configurations ------------
		ds.updateSubConfigurations();
		dDataSource.getDataSourceSubConfigurations().addAll(ds.getDataSourceSubConfigurations());
		
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
			// --- Call the from configuration string method --------
			actDS.fromConfigurationString(configurationString);
			
			// --- Add sub configuration to the actual instance ----- 
			actDS.getDataSourceSubConfigurations().addAll(dDS.getDataSourceSubConfigurations());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return actDS;
	}
	
	
}
