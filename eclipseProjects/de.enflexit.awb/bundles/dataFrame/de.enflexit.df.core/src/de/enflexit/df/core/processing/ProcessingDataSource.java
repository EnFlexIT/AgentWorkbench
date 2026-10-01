package de.enflexit.df.core.processing;

import de.enflexit.common.NumberHelper;
import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * This data source implementation represents data that is generated from other data sources using data transformations. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingDataSource extends DefaultDataSource {

	private static final long serialVersionUID = 5845214876005739979L;
	
	private ProcessingDataSourceIntegration dataSourceIntegration;
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#newInstance()
	 */
	@Override
	public DefaultDataSource newInstance() {
		return new ProcessingDataSource();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#getDataSourceIntegration(de.enflexit.df.core.model.DataController, de.enflexit.df.core.workbook.DataWorkbook)
	 */
	@Override
	public AbstractDataSourceIntegration<?> getDataSourceIntegration(DataController dataController,	DataWorkbook dataWorkbook) {
		if (dataSourceIntegration==null) {
			dataSourceIntegration = new ProcessingDataSourceIntegration(dataController, dataWorkbook, this);
		}
		return dataSourceIntegration;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#toConfigurationString()
	 */
	@Override
	public String toConfigurationString() {
		
		String config = new String();
		
		config = ProcessingDataSource.addConfigValue(config, KEY_ID, String.valueOf(this.getId()));
		config = ProcessingDataSource.addConfigValue(config, KEY_NAME, this.getName());
		config = ProcessingDataSource.addConfigValue(config, KEY_DESCRIPTION, this.getDescription());
		
		return config;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#fromConfigurationString(java.lang.String)
	 */
	@Override
	public ProcessingDataSource fromConfigurationString(String configurationString) {
		
		if (configurationString==null || configurationString.isBlank()==true) return this;
		
		String[] keyValuePairs = configurationString.split("\\|");
		if (keyValuePairs.length==0) return this;
		
		// --- Create new instance ----------------------------------
		for (String keyValuePair : keyValuePairs) {
			
			int idxTagOpen  = keyValuePair.indexOf("[");
			int idxTagClose = keyValuePair.indexOf("]");
			
			String key   = keyValuePair.substring(0, idxTagOpen);
			String value = keyValuePair.substring(idxTagOpen + 1, idxTagClose);
			if (value.isBlank()==true) continue;
			
			switch (key) {
			case KEY_ID:
				this.setId(NumberHelper.parseInteger(value));
				break;
			case KEY_NAME:
				this.setName(value);
				break;
			case KEY_DESCRIPTION:
				this.setDescription(value);
				break;
			}
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#requiresSubConfiguration()
	 */
	@Override
	public boolean requiresSubConfiguration() {
		return true;
	}

}
