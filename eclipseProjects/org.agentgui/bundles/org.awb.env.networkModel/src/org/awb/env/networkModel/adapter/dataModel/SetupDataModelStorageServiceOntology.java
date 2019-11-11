package org.awb.env.networkModel.adapter.dataModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter4Ontology;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.persistence.SetupDataModelStorageService;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.enflexit.common.ontology.OntologyInstanceHelper;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

/**
 * The Class SetupDataModelStorageServiceOntology is used to save or load all setup instances 
 * of ontologies in one file. Thus it should be possible to have a proper XML structure and avoid
 * to double convert from instance to xml and subsequently in a Base64 encoded string.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SetupDataModelStorageServiceOntology implements SetupDataModelStorageService {

	private static final String FILE_SETUP_SUFFIX = "_Ontology.xml";
	private static final String FILE_ENCODING  = "UTF-8";
	private static final String lineBreak = "\n";
	private static final String NULL_INSTANCE = "<Null>";
	
	private static final String XML_ELEMENT_OntologyInstances = "OntologyInstances";
	private static final String XML_ELEMENT_DataModel = "DataModel";
	private static final String XML_ELEMENT_NetworkElement = "NetworkElement";
	private static final String XML_ELEMENT_Instances = "Instances";
	private static final String XML_ELEMENT_Instance = "Instance";
	private static final String XML_ATTRIBUTE_Ontology = "Ontology";
	
	private GraphEnvironmentController graphController;
	
	private XMLCodec xmlCodec;
	private TreeMap<String, OntologyInstanceConfiguration> ontologyInstanceTreeMap;
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.SetupDataModelStorageService#setGraphEnvironmentController(org.awb.env.networkModel.controller.GraphEnvironmentController)
	 */
	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.dataModel.SetupDataModelStorageService#getDataModelStorageHandlerClass()
	 */
	@Override
	public Class<? extends AbstractDataModelStorageHandler> getDataModelStorageHandlerClass() {
		return DataModelStorageHandlerOntology.class;
	}

	/**
	 * Returns the ontology instance tree map.
	 * @return the ontology instance tree map
	 */
	private TreeMap<String, OntologyInstanceConfiguration> getOntologyInstanceTreeMap() {
		if (ontologyInstanceTreeMap==null) {
			ontologyInstanceTreeMap = new TreeMap<>();
		}
		return ontologyInstanceTreeMap;
	}
	/**
	 * Sets the ontology instance tree map.
	 * @param ontologyInstanceTreeMap the ontology instance tree map
	 */
	private void setOntologyInstanceTreeMap(TreeMap<String, OntologyInstanceConfiguration> ontologyInstanceTreeMap) {
		this.ontologyInstanceTreeMap = ontologyInstanceTreeMap;
	}
	
	/**
	 * Returns the local TreeMap key for the specified data model network element.
	 *
	 * @param networkElement the network element
	 * @param partModelID the part model ID
	 * @return the key of data model network element
	 */
	private String getKeyOfDataModelNetworkElement(DataModelNetworkElement networkElement, String partModelID) {
		if (networkElement==null) return null;
		String id = networkElement.getId();
		String type = networkElement.getClass().getSimpleName();
		String key = type + "," + id;
		if (partModelID!=null && partModelID.isEmpty()==false) {
			key += "," + partModelID;
		}
		return key;
	}
	
	/**
	 * Sets the data model for the specified network element to the local storage.
	 *
	 * @param networkElement the network element
	 * @param ontologyBaseClasses the ontology base classes
	 * @param partModelID the part model ID, if the model is part of a combined data model (may be null)
	 */
	public void setDataModel(DataModelNetworkElement networkElement, Vector<Class<? extends Ontology>> ontologyBaseClasses, String partModelID) {
		String key = this.getKeyOfDataModelNetworkElement(networkElement, partModelID);
		if (key!=null) {
			Object ontologyDataModel = networkElement.getDataModel();
			if (ontologyDataModel!=null) {
				OntologyInstanceConfiguration instanceConfig = new OntologyInstanceConfiguration(ontologyBaseClasses, ontologyDataModel);
				this.getOntologyInstanceTreeMap().put(key, instanceConfig);
			} else {
				this.getOntologyInstanceTreeMap().remove(key);
			}
		}
	}
	
	/**
	 * Return the local stored data model for the specified NetworkElement.
	 *
	 * @param networkElement the network element
	 * @param partModelID the part model ID, if the model is part of a combined data model (may be null)
	 * @return the data model
	 */
	public Object getDataModel(DataModelNetworkElement networkElement, String partModelID) {
		String key = this.getKeyOfDataModelNetworkElement(networkElement, partModelID);
		if (key!=null) {
			OntologyInstanceConfiguration instanceConfig = this.getOntologyInstanceTreeMap().get(key);
			if (instanceConfig!=null) {
				return instanceConfig.getOntologyDataModel();
			}
		}
		return null;
	}
	
	
	/**
	 * Returns the destination directory for the setup file.
	 * @return the destination directory
	 */
	private String getDestinationDirectory() {
		if (this.graphController!=null) {
			return this.graphController.getEnvFolderPath();
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
	private File getSetupFile(String setupName) {
		String destinationDirectory = this.getDestinationDirectory();
		if (destinationDirectory==null || destinationDirectory.isEmpty()==true || setupName==null || setupName.isEmpty()==true) return null;
		return new File(destinationDirectory + setupName + FILE_SETUP_SUFFIX);
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.persistence.SetupDataModelStorageService#getSetupFiles(java.lang.String)
	 */
	@Override
	public List<File> getSetupFiles(String setupName) {
		List<File> setupFiles = new ArrayList<>();
		setupFiles.add(this.getSetupFile(setupName));
		return setupFiles;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.persistence.SetupDataModelStorageService#removeNetworkElementDataModels(java.lang.String)
	 */
	@Override
	public void removeNetworkElementDataModels(String setupName) {
		File setupFile = this.getSetupFile(setupName);
		if (setupFile!=null && setupFile.exists()==true) {
			setupFile.delete();
		}
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.persistence.SetupDataModelStorageService#renameNetworkElementDataModels(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameNetworkElementDataModels(String oldSetupName, String newSetupName) {
		File oldSetupFile = this.getSetupFile(oldSetupName);
		File newSetupFile = this.getSetupFile(newSetupName);
		if (oldSetupFile.exists()) {
			oldSetupFile.renameTo(newSetupFile);
		}
	}
	
	/**
	 * Checks all NetworkComponents and GraphNodes in the NetworkModel and updates the local ontology data models.
	 */
	private void updateOntologyDataModels() {
		
		// --- Clear local storage ----------------------------------
		this.setOntologyInstanceTreeMap(null);
		
		// --- Get list of DataModelNetworkElement ------------------ 
		NetworkModel networkModel = this.graphController.getNetworkModel();
		List<DataModelNetworkElement> networkElementList = networkModel.getDataModelNetworkElementList();
		for (int i = 0; i < networkElementList.size(); i++) {
			
			DataModelNetworkElement networkElement = networkElementList.get(i);
			
			NetworkComponentAdapter netCompAdapter = networkModel.getNetworkComponentAdapter(this.graphController, networkElement);
			if (netCompAdapter!=null) {
				NetworkComponentAdapter4DataModel dmNetCompAdapter = netCompAdapter.getStoredDataModelAdapter();
				// --- Check for the usage of Ontologies ------------
				if (dmNetCompAdapter!=null && dmNetCompAdapter.containsDataModelType(NetworkComponentAdapter4Ontology.DATA_MODEL_TYPE_ONTOLOGY)==true) {
					// --- Found adapter that handles ontologies ----
					TreeMap<String, String> storageSettings = dmNetCompAdapter.getDataModelStorageHandlerInternal().saveDataModel(networkElement);
					networkElement.setDataModelStorageSettings(storageSettings);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.persistence.SetupDataModelStorageService#saveNetworkElementDataModels(java.lang.String)
	 */
	@Override
	public void saveNetworkElementDataModels(String setupName) {
		
		File setupFile = this.getSetupFile(setupName);
		if (setupFile==null) return;
		
		// --- Update the local instances -----------------
		this.updateOntologyDataModels();
		
		// --- Is there something to write? ---------------
		if (this.getOntologyInstanceTreeMap().size()==0) {
			// --- Delete old file? ---
			this.removeNetworkElementDataModels(setupName);
			return;
		}
		
		// --- Write to XML file --------------------------
		OutputStream oStream = null;
		OutputStreamWriter osWriter = null;
		XMLStreamWriter xmlOut = null;
		try {
			
			oStream  = new FileOutputStream(setupFile);
			osWriter = new OutputStreamWriter(oStream, FILE_ENCODING);
			xmlOut   = XMLOutputFactory.newInstance().createXMLStreamWriter(osWriter);

			xmlOut.writeStartDocument();
			xmlOut.writeCharacters(lineBreak);
			xmlOut.writeStartElement(XML_ELEMENT_OntologyInstances);
			xmlOut.writeCharacters(lineBreak);
			
			// --- Loop over ontology instances ---------------------
			List<String> keyList = new ArrayList<>(this.getOntologyInstanceTreeMap().keySet());
			for (int i = 0; i < keyList.size(); i++) {
				
				String key = keyList.get(i);
				OntologyInstanceConfiguration instanceConfig = this.getOntologyInstanceTreeMap().get(key);
				Vector<Class<? extends Ontology>> ontologyBaseClasses = instanceConfig.getOntologyBaseClasses();
				Object ontologyObject = instanceConfig.getOntologyDataModel();
				if (ontologyObject==null) continue;
				
				// --- Get XML representation list of instances -----
				List<String> xmlRepresentationList = this.getXMLRepresentationOfDataModel(ontologyBaseClasses, ontologyObject); 
				
				// --- Write out the key / value combination --------
				xmlOut.writeStartElement(XML_ELEMENT_DataModel);
				xmlOut.writeCharacters(lineBreak + "\t");
				// --- Key: -----------------------------------------
				xmlOut.writeStartElement(XML_ELEMENT_NetworkElement);
				xmlOut.writeCharacters(key);
				xmlOut.writeEndElement();
				xmlOut.writeCharacters(lineBreak);
				
				// --- Value(s): ------------------------------------
				xmlOut.writeStartElement(XML_ELEMENT_Instances);
				xmlOut.writeCharacters(lineBreak);
				for (int j = 0; j < xmlRepresentationList.size(); j++) {

					xmlOut.writeStartElement(XML_ELEMENT_Instance);
					xmlOut.writeAttribute(XML_ATTRIBUTE_Ontology, ontologyBaseClasses.get(j).getName());
					xmlOut.writeCharacters(lineBreak);
					
					// --- Write the XML string ---------------------
					xmlOut.writeCData(xmlRepresentationList.get(j));
					xmlOut.writeCharacters(lineBreak);
					
					xmlOut.writeEndElement();	
					xmlOut.writeCharacters(lineBreak);
				}
				xmlOut.writeEndElement();
				xmlOut.writeCharacters(lineBreak);
				xmlOut.writeEndElement();
				xmlOut.writeCharacters(lineBreak);
			}
			
			xmlOut.writeEndElement();
			xmlOut.writeCharacters(lineBreak);
			xmlOut.writeEndDocument();
			xmlOut.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} finally {
			// --- Close the above opened elements --------
			if (xmlOut!=null) {
				try {
					xmlOut.close();
				} catch (XMLStreamException e) { }
			}
			if (osWriter!=null) {
				try {
					osWriter.close();
				} catch (IOException e) { }
			}
			if (oStream!=null) {
				try {
					oStream.close();
				} catch (IOException e) { }
			}
		}
	}

	/**
	 * Returns the XML representation(s) of the specified array of ontology instances.
	 *
	 * @param ontologyBaseClasses the ontology base classes
	 * @param ontologyDataModel the ontology data model
	 * @return the XML representation
	 */
	private List<String> getXMLRepresentationOfDataModel(Vector<Class<? extends Ontology>> ontologyBaseClasses, Object ontologyDataModel) {
		
		// --- Get the actual array ---------------------------------
		Object[] ontologyInstanceArray = null;
		if (ontologyDataModel!=null)  {
			if (ontologyDataModel.getClass().isArray()==true) {
				ontologyInstanceArray = (Object[]) ontologyDataModel;
			} else {
				ontologyInstanceArray = new Object[1];
				ontologyInstanceArray[0] = ontologyDataModel;
			}
		}
		
		// --- Convert to XML ---------------------------------------
		List<String> xmlRepresentationList = new ArrayList<>();
		for (int i = 0; i < ontologyBaseClasses.size(); i++) {
			
			Object ontologyInstance = null;
			String xmlRepresentation = null;
			if (ontologyInstanceArray.length>=(i+1)) {
				ontologyInstance = ontologyInstanceArray[i];
			}
			
			if (ontologyInstance!=null) {
				try {
					Ontology ontology = this.getOntology(ontologyBaseClasses.get(i));
					xmlRepresentation = this.getXMLCodec().encodeObject(ontology, ontologyInstance, true);
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
			}
			
			if (xmlRepresentation==null || xmlRepresentation.isEmpty()==true) {
				xmlRepresentation = NULL_INSTANCE;
			}
			xmlRepresentationList.add(xmlRepresentation);
		}
		return xmlRepresentationList;
	}
	
	/**
	 * Returns the ontology for the specified class.
	 *
	 * @param ontologyClass the ontology class
	 * @return the ontology
	 */
	private Ontology getOntology(Class<? extends Ontology> ontologyClass) {
		if (ontologyClass==null) return null;
		return this.getOntology(ontologyClass.getName());
	}
	/**
	 * Returns the ontology for the specified class.
	 *
	 * @param ontologyClassName the ontology class name
	 * @return the ontology
	 */
	private Ontology getOntology(String ontologyClassName) {
		if (ontologyClassName==null) return null;
		return OntologyInstanceHelper.getOntology(ontologyClassName);
	}
	
	/**
	 * Returns the local instance of the JADE XML Codec.
	 * @return the XML Codec
	 */
	private XMLCodec getXMLCodec() {
		if (xmlCodec==null) {
			xmlCodec = new XMLCodec();
		}
		return xmlCodec;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.persistence.SetupDataModelStorageService#loadNetworkElementDataModels(java.lang.String)
	 */
	@Override
	public void loadNetworkElementDataModels(String setupName) {
		
		File setupFile = this.getSetupFile(setupName);
		if (setupFile==null || setupFile.exists()==false) return;
		
		try {

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			
			XMLFileReader xmlReader = new XMLFileReader();
			saxParser.parse(setupFile, xmlReader);
			this.setOntologyInstanceTreeMap(xmlReader.getontologyInstanceTreeMap());
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The Class XMLFileReader.
	 */
	private class XMLFileReader extends DefaultHandler {
		
		private TreeMap<String, OntologyInstanceConfiguration> instanceTreeMap;
		
		private OntologyInstanceConfiguration tmpOntoConfig;
		private String tmpKey;
		private Ontology tmpOntology;
		private StringBuilder tmpStringBuilder;

		private Vector<Class<? extends Ontology>> tmpOntologyBaseClasses;
		private Vector<Object> tmpOntologyInstances;

		
		/**
		 * Returns the ontology instance tree map.
		 * @return the ontology instance tree map
		 */
		public TreeMap<String, OntologyInstanceConfiguration> getontologyInstanceTreeMap() {
			if (instanceTreeMap==null) {
				instanceTreeMap = new TreeMap<>();
			}
			return instanceTreeMap;
		}
		
		public Vector<Class<? extends Ontology>> getTmpOntologyBaseClasses() {
			if (tmpOntologyBaseClasses==null) {
				tmpOntologyBaseClasses = new Vector<>();
			}
			return tmpOntologyBaseClasses;
		}
		public Vector<Object> getTmpOntologyInstances() {
			if (tmpOntologyInstances==null) {
				tmpOntologyInstances = new Vector<>();
			}
			return tmpOntologyInstances;
		}
		
		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			if (qName.equals(XML_ELEMENT_DataModel)==true) {
				// --- Prepare for new TreeMap entry ----------------
				this.tmpOntoConfig = new OntologyInstanceConfiguration();
				this.tmpStringBuilder = null;
				
			} else if (qName.equals(XML_ELEMENT_NetworkElement)==true) {
				// --- Get ID of NetworkElement ---------------------
				this.tmpStringBuilder = new StringBuilder();
				
			} else if (qName.equals(XML_ELEMENT_Instances)==true) {
				// --- Prepare for new TreeMap entry ----------------
				this.tmpStringBuilder = null;
				
			} else if (qName.equals(XML_ELEMENT_Instance)==true) {
				// --- ReadAction attribute ontology class ---------------- 
				String ontologyClassName = attributes.getValue(XML_ATTRIBUTE_Ontology);
				this.tmpOntology = SetupDataModelStorageServiceOntology.this.getOntology(ontologyClassName);
				this.getTmpOntologyBaseClasses().add(this.tmpOntology.getClass());
				this.tmpStringBuilder = new StringBuilder();
			}
		}
		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
			if (qName.equals(XML_ELEMENT_DataModel)==true) {
				// --- Prepare for new TreeMap entry ----------------
				this.tmpOntoConfig.setOntologyBaseClasses(this.getTmpOntologyBaseClasses());
				this.tmpOntoConfig.setOntologyDataModel(this.getTmpOntologyInstances().toArray());
				this.getontologyInstanceTreeMap().put(this.tmpKey, this.tmpOntoConfig);
				// --- Reset all tmp variable -----------------------
				this.tmpOntoConfig = null;
				this.tmpKey = null;
				this.tmpOntologyBaseClasses = null;
				this.tmpOntologyInstances = null;
				
			} else if (qName.equals(XML_ELEMENT_NetworkElement)==true) {
				// --- Get ID of NetworkElement ---------------------
				this.tmpKey = this.tmpStringBuilder.toString();
				
			} else if (qName.equals(XML_ELEMENT_Instances)==true) {
				// --- Nothing to do here ---------------------------
				
			} else if (qName.equals(XML_ELEMENT_Instance)==true) {
				// --- Get the ontology instance --------------------
				Object ontologyInstance = null;
				String xmlOntologyInstance = this.tmpStringBuilder.toString().trim();
				if (xmlOntologyInstance!=null & xmlOntologyInstance.equals(NULL_INSTANCE)==false & this.tmpOntology!=null) {
					try {
						ontologyInstance = SetupDataModelStorageServiceOntology.this.getXMLCodec().decodeObject(this.tmpOntology, xmlOntologyInstance);
					} catch (CodecException | OntologyException e) {
						System.err.println("[" + this.getClass().getSimpleName() + "] Error while parsing XML string:");
						System.err.println(xmlOntologyInstance);
						e.printStackTrace();
					}
				}
				this.getTmpOntologyInstances().add(ontologyInstance);
				
			}
		}
		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (this.tmpStringBuilder==null) return;
			this.tmpStringBuilder.append(new String(ch, start, length));
		}
	}
	
	
	/**
	 * The Class OntologyInstanceConfiguration is used as part of local TreeMap (as value) to store the ontology instance configuration.
	 */
	public class OntologyInstanceConfiguration {
		
		private Vector<Class<? extends Ontology>> ontologyBaseClasses;
		private Object ontologyDataModel;
		
		
		/**
		 * Instantiates a new ontology instance configuration.
		 */
		public OntologyInstanceConfiguration() { }
		/**
		 * Instantiates a new ontology instance configuration.
		 *
		 * @param ontologyBaseClasses the ontology base classes
		 * @param ontologyDataModel the ontology data model
		 */
		public OntologyInstanceConfiguration(Vector<Class<? extends Ontology>> ontologyBaseClasses, Object ontologyDataModel) {
			this.setOntologyBaseClasses(ontologyBaseClasses);
			this.setOntologyDataModel(ontologyDataModel);
		}

		/**
		 * Gets the ontology base classes.
		 *
		 * @return the ontology base classes
		 */
		public Vector<Class<? extends Ontology>> getOntologyBaseClasses() {
			return ontologyBaseClasses;
		}
		
		/**
		 * Sets the ontology base classes.
		 *
		 * @param ontologyBaseClasses the new ontology base classes
		 */
		public void setOntologyBaseClasses(Vector<Class<? extends Ontology>> ontologyBaseClasses) {
			this.ontologyBaseClasses = ontologyBaseClasses;
		}

		/**
		 * Gets the ontology data model.
		 *
		 * @return the ontology data model
		 */
		public Object getOntologyDataModel() {
			return ontologyDataModel;
		}
		
		/**
		 * Sets the ontology data model.
		 *
		 * @param ontologyDataModel the new ontology data model
		 */
		public void setOntologyDataModel(Object ontologyDataModel) {
			this.ontologyDataModel = ontologyDataModel;
		}
	}
	

}
