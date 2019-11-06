package org.awb.env.networkModel.dataModel;

import java.io.File;
import java.util.TreeMap;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.SetupDataModelStorageService;

/**
 * The Class SetupDataModelStorageServiceOntology is used to save or load all setup instances 
 * of ontologies in one file. Thus it should be possible to have a proper XML structure and avoid
 * to double convert from instance to xml and subsequently in a Base64 encoded string.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SetupDataModelStorageServiceOntology implements SetupDataModelStorageService {

	private GraphEnvironmentController graphController;
	
	private TreeMap<String, Object> ontologyInstanceTreeMap;
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.SetupDataModelStorageService#setGraphEnvironmentController(org.awb.env.networkModel.controller.GraphEnvironmentController)
	 */
	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.dataModel.SetupDataModelStorageService#getDataModelStorageHandlerClass()
	 */
	@Override
	public Class<? extends AbstractDataModelStorageHandler> getDataModelStorageHandlerClass() {
		return DataModelStorageHandlerOntology.class;
	}

	/**
	 * Gets the ontology instance tree map.
	 * @return the ontology instance tree map
	 */
	private TreeMap<String, Object> getOntologyInstanceTreeMap() {
		if (ontologyInstanceTreeMap==null) {
			ontologyInstanceTreeMap = new TreeMap<>();
		}
		return ontologyInstanceTreeMap;
	}
	
	/**
	 * Returns the local TreeMap key for the specified data model network element.
	 *
	 * @param networkElement the network element
	 * @return the key of data model network element
	 */
	private String getKeyOfDataModelNetworkElement(DataModelNetworkElement networkElement) {
		if (networkElement==null) return null;
		String id = networkElement.getId();
		String type = networkElement.getClass().getSimpleName();
		String key = type + "," + id;
		return key;
	}
	/**
	 * Sets the data model for the specified network element to the local storage.
	 *
	 * @param networkElement the network element
	 * @param dataModel the data model
	 */
	public void setDataModel(DataModelNetworkElement networkElement, Object dataModel) {
		String key = this.getKeyOfDataModelNetworkElement(networkElement);
		if (key!=null && dataModel!=null) {
			this.getOntologyInstanceTreeMap().put(key, dataModel);
		}
	}
	/**
	 * Return the local stored data model for the specified NetworkElement.
	 *
	 * @param networkElement the network element
	 * @return the data model
	 */
	public Object getDataModel(DataModelNetworkElement networkElement) {
		String key = this.getKeyOfDataModelNetworkElement(networkElement);
		if (key!=null) {
			return this.getOntologyInstanceTreeMap().get(key);
		}
		return null;
	}
	
	/**
	 * Returns the file for the current setup.
	 *
	 * @param destinationDirectory the destination directory
	 * @param setupName the setup name
	 * @return the file for setup
	 */
	private File getSetupFile(String destinationDirectory, String setupName) {
		if (destinationDirectory==null || destinationDirectory.isEmpty()==true || setupName==null || setupName.isEmpty()==true) return null;
		return new File(destinationDirectory + setupName + "-Ontology.xml");
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.SetupDataModelStorageService#saveNetworkElementDataModels(java.lang.String, java.lang.String)
	 */
	@Override
	public void saveNetworkElementDataModels(String destinationDirectory, String setupName) {
		
		File setupFile = this.getSetupFile(destinationDirectory, setupName);
		if (setupFile==null) return;
		
		System.out.println("[" + this.getClass().getSimpleName() + "]: save network element data model to file " + setupFile.getAbsolutePath() + "!");
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.SetupDataModelStorageService#loadNetworkElementDataModels(java.lang.String, java.lang.String)
	 */
	@Override
	public void loadNetworkElementDataModels(String destinationDirectory, String setupName) {
		
		File setupFile = this.getSetupFile(destinationDirectory, setupName);
		if (setupFile==null || setupFile.exists()==false) return;
		
		
		
		System.out.println("[" + this.getClass().getSimpleName() + "]: load network element data model from flle " + setupFile.getAbsolutePath() + "!");
	}
	
	

}
