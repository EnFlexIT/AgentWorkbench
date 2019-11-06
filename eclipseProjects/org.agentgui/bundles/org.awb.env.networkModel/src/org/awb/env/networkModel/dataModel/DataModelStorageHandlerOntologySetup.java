package org.awb.env.networkModel.dataModel;

import org.awb.env.networkModel.controller.SetupDataModelStorageService;

/**
 * The Class DataModelStorageHandlerOntologySetup is used to save or load all setup instances 
 * of ontologies in one file. Thus it should be possible to have a proper XML structure and avoid
 * to double convert from instance to xml and subsequently in a Base64 encoded string.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DataModelStorageHandlerOntologySetup implements SetupDataModelStorageService {

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.dataModel.SetupDataModelStorageService#getDataModelStorageHandlerClass()
	 */
	@Override
	public Class<? extends AbstractDataModelStorageHandler> getDataModelStorageHandlerClass() {
		return DataModelStorageHandlerBase64.class;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.dataModel.SetupDataModelStorageService#loadNetworkElementDataModels()
	 */
	@Override
	public void loadNetworkElementDataModels() {
		
		System.out.println("[" + this.getClass().getSimpleName() + "]: load network element data model!");
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.dataModel.SetupDataModelStorageService#saveNetworkElementDataModels()
	 */
	@Override
	public void saveNetworkElementDataModels() {
		
		System.out.println("[" + this.getClass().getSimpleName() + "]: save network element data model!");
	}


}
