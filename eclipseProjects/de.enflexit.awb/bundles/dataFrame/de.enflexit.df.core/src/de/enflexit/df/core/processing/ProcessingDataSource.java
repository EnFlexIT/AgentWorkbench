package de.enflexit.df.core.processing;

import de.enflexit.common.NumberHelper;
import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.transformationGraph.PersistentTransformationGraph;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * This data source implementation represents data that is generated from other data sources using data transformations. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingDataSource extends DefaultDataSource {

	private static final long serialVersionUID = 5845214876005739979L;
	
	private static final String KEY_TRANSFORMATION_GRAPH = "TransformationGraph";
	
	private static final String OPENING_BRACKET_REPLACER = "$%&";
	private static final String CLOSING_BRACKET_REPLACER = "&%$";
	
	private ProcessingDataSourceIntegration dataSourceIntegration;
	
	private PersistentTransformationGraph persistentTransformationGraph;
	
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
		config = ProcessingDataSource.addConfigValue(config, KEY_SHORT_UUID, this.getShortUUID());
		config = ProcessingDataSource.addConfigValue(config, KEY_NAME, this.getName());
		config = ProcessingDataSource.addConfigValue(config, KEY_DESCRIPTION, this.getDescription());
		
		if (this.getPersistentTransformationGraph()!=null) {
			String persGraphConfigString = this.replaceBrackets(this.getPersistentTransformationGraph().toJsonString());
			config = ProcessingDataSource.addConfigValue(config, KEY_TRANSFORMATION_GRAPH, persGraphConfigString);
		}
		
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
			case KEY_SHORT_UUID:
				this.setShortUUID(value);
				break;
			case KEY_NAME:
				this.setName(value);
				break;
			case KEY_DESCRIPTION:
				this.setDescription(value);
				break;
			case KEY_TRANSFORMATION_GRAPH:
				String persGraphJsonString = this.restoreBrackets(value);
				this.setPersistentTransformationGraph(PersistentTransformationGraph.fromJsonString(persGraphJsonString));
				break;
			}
		}
		return this;
	}
	
	/**
	 * Replaces brackets with strings that will most likely never occur, because brackets are used by
	 * the superclass method to mark configuration values.
	 * @param originalString the original string
	 * @return the string
	 */
	private String replaceBrackets(String originalString) {
		String intermediaeString = originalString.replace("[", OPENING_BRACKET_REPLACER);
		return intermediaeString.replace("]", CLOSING_BRACKET_REPLACER);
	}
	
	/**
	 * Restores the brackets that have been replaced by the defined replacer strings.
	 * @param originalString the original string
	 * @return the string
	 */
	private String restoreBrackets(String originalString) {
		String intermediaeString = originalString.replace(OPENING_BRACKET_REPLACER, "[");
		return intermediaeString.replace(CLOSING_BRACKET_REPLACER, "]");
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DefaultDataSource#requiresSubConfiguration()
	 */
	@Override
	public boolean requiresSubConfiguration() {
		return true;
	}

	/**
	 * Gets the persistent transformation graph.
	 * @return the persistent transformation graph
	 */
	public PersistentTransformationGraph getPersistentTransformationGraph() {
		return persistentTransformationGraph;
	}
	/**
	 * Sets the persistent transformation graph.
	 * @param persistentTransformationGraph the new persistent transformation graph
	 */
	public void setPersistentTransformationGraph(PersistentTransformationGraph persistentTransformationGraph) {
		this.persistentTransformationGraph = persistentTransformationGraph;
	}
	
	

}
