package org.awb.env.networkModel.dataModel;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.awb.env.networkModel.DataModelNetworkElement;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.adapter.NetworkComponentAdapter4Ontology;
import org.awb.env.networkModel.controller.SetupDataModelStorageService;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import de.enflexit.common.ExceptionHandling;
import de.enflexit.common.ontology.OntologyClassTreeObject;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

/**
 * The Class DataModelStorageHandlerOntology handles {@link NetworkComponent}s data models 
 * that are based on ontologies.
 * 
 * @see NetworkComponent#getDataModelBase64()
 * @see NetworkComponent#setDataModelBase64(java.util.Vector)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DataModelStorageHandlerOntology extends AbstractDataModelStorageHandler {
	
	public  static final String ONTO_SETTING_STORAGE_LOCATION = "StorageLocation";
	private static final OntologyStorageLocation DESTINATION = OntologyStorageLocation.Both;
	
	private enum OntologyStorageLocation {
		Base64InNetworkElements,
		OntologyFile,
		Both
	}
	
	
	protected NetworkComponentAdapter4Ontology ontologyAdapter;
	
	/**
	 * Instantiates a new data model storage handler ontology.
	 * @param ontologyAdapter the ontology adapter
	 */
	public DataModelStorageHandlerOntology(NetworkComponentAdapter4Ontology ontologyAdapter) {
		this.ontologyAdapter = ontologyAdapter;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here, conversion from XML64 to instances --------------------------------
	// ----------------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.dataModel.AbstractDataModelStorageHandler#loadDataModel(org.awb.env.networkModel.DataModelNetworkElement)
	 */
	@Override
	public Object loadDataModel(DataModelNetworkElement networkElement) {
		
		if (networkElement==null) return null;
		
		// ----------------------------------------------------------
		// --- Identify storage location ----------------------------
		// ----------------------------------------------------------
		OntologyStorageLocation storageLocation = OntologyStorageLocation.Base64InNetworkElements;
		if (networkElement.getDataModelStorageSettings()==null) {
			this.setRequiresPersistenceUpdate(true);
		} else {
			String storageLocationString = networkElement.getDataModelStorageSettings().get(ONTO_SETTING_STORAGE_LOCATION);
			if (storageLocationString!=null && storageLocationString.isEmpty()==false) {
				storageLocation = OntologyStorageLocation.valueOf(storageLocationString);
			} else {
				this.setRequiresPersistenceUpdate(true);
			}
		}

		// ----------------------------------------------------------
		// --- Get the actual data model ----------------------------
		// ----------------------------------------------------------
		Object dataModel = null;
		
		// --- Try loading from Base64 (also for old versions) ------   
		try {
			Vector<String> dataModelBase64 = networkElement.getDataModelBase64();
			if (dataModelBase64!=null) {
				dataModel = this.getInstancesFromXML64(dataModelBase64);
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --- Load from central setup file -------------------------
		if (storageLocation==OntologyStorageLocation.OntologyFile) {
			// --- Try to get the data model from central file ------
			dataModel = this.getSetupDataModelStorageServiceOntology().getDataModel(networkElement);
		}
		return dataModel;
	}
	
	/**
	 * Gets the instances from XML 64.
	 *
	 * @param ontoXML64Vector the onto XML 64 vector
	 * @return the instances from XML 64
	 */
	protected Object[] getInstancesFromXML64(Vector<String> ontoXML64Vector) {
		// --- Decode Base64 first ----------------------------------
		Vector<String> ontoXMLVector = this.decodeBase64StringVector(ontoXML64Vector);
		// --- Create instances -------------------------------------
		return this.createInstancesFromOntologyXmlVector(ontoXMLVector);
	}

	/**
	 * Decodes a vector of Base64-encoded strings.
	 * @param base64StringVector the Base64-encoded strings
	 * @return the decoded strings
	 */
	protected Vector<String> decodeBase64StringVector(Vector<String>base64StringVector){
		Vector<String> ontoXMLVector = new Vector<>();
		for (String base64String : base64StringVector) {
			String decodedString = null;
			if (base64String!=null && base64String.equals("")==false) {
				try {
					decodedString = new String(Base64.decodeBase64(base64String.getBytes()), "UTF8");	
				} catch (UnsupportedEncodingException uee) {
					uee.printStackTrace();
				}
			}
			ontoXMLVector.add(decodedString);
		}
		return ontoXMLVector;
	}
	
	/**
	 * Creates ontology instances for a vector of XML strings
	 * @param ontologyXmlVector the XML strings
	 * @return the ontology instances
	 */
	protected Object[] createInstancesFromOntologyXmlVector(Vector<String> ontologyXmlVector) {
		Object[] instances = new Object[this.ontologyAdapter.getOntologyClassReferences().length];
		String[] classReferences = this.ontologyAdapter.getOntologyClassReferences();
		for (int i = 0; i < classReferences.length; i++) {
			// --- Get the corresponding Ontology-Instance ----------
			OntologyClassTreeObject octo = this.ontologyAdapter.getOntologyVisualizationHelper().getClassTreeObject(classReferences[i]);
			if (octo!=null) {
				Ontology ontology = octo.getOntologyClass().getOntologyInstance();
				
				String xml = null;
				if (i<=(ontologyXmlVector.size()-1)) {
					xml = ontologyXmlVector.get(i);
				}
				instances[i] = this.getInstanceOfXML(xml, classReferences[i], ontology);	
			}
		}
		return instances;
	}

	/**
	 * This method translates an XML String to an object instance by the
	 * given instance of the current ontology. The translated object must
	 * be a part of this ontology.
	 *
	 * @param xmlString the xml string
	 * @param ontology the ontology
	 * @return the instance of xml
	 */
	protected Object getInstanceOfXML(String xmlString, String className, Ontology ontology) {
		
		Object objectInstance = null;

		if (xmlString!=null && xmlString.equals("")==false) {
			try {
				XMLCodec codec = new XMLCodec();
				objectInstance = codec.decodeObject(ontology, xmlString);
				
			} catch (CodecException ce) {
				ce.printStackTrace();
				
			} catch (OntologyException oe) {
				//oe.printStackTrace();
				// --- No object was created from XML ---------------
				// --- -> Try to create an empty instance -----------
				System.err.println("Ontology '" + ontology.getName() + "' -> XML to Object: " + ExceptionHandling.getFirstTextLineOfException(oe));
				objectInstance = this.getNewClassInstance(className);
				System.err.println("Ontology '" + ontology.getName() + "' -> XML to Object: Created an empty instance for class '" + className + "' !");
				
			}		
		}
		return objectInstance;
	}
	/**
	 * This method returns a new instance of a given class given by its full class name.
	 * @param className the class name
	 * @return the new class instance
	 */
	protected Object getNewClassInstance(String className) {
		Object instance = null;
		try {
			// --- OntologyBeanGenerator for Protege 3.3.1 ----------
			instance = ClassLoadServiceUtility.newInstance(className);	
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here, conversion from instance to XML64 ---------------------------------
	// ----------------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.dataModel.AbstractDataModelStorageHandler#saveDataModel(org.awb.env.networkModel.DataModelNetworkElement)
	 */
	@Override
	public TreeMap<String, String> saveDataModel(DataModelNetworkElement networkElement) {

		if (networkElement==null) return null;
			
		TreeMap<String, String> settings = new TreeMap<>();
		settings.put(ONTO_SETTING_STORAGE_LOCATION, DESTINATION.toString());
		
		Object dataModel = networkElement.getDataModel(); 
		// ----------------------------------------------------------
		if (DESTINATION==OntologyStorageLocation.Both || DESTINATION==OntologyStorageLocation.Base64InNetworkElements) {
			if (dataModel==null) {
				networkElement.setDataModelBase64(null);
			} else {
				if (dataModel.getClass().isArray()==true) {
					Object[] dmArray = (Object[]) dataModel;
					Vector<String> dataModelBase64 = this.getXML64FromInstances(dmArray);
					networkElement.setDataModelBase64(dataModelBase64);
				} else {
					networkElement.setDataModelBase64(null);
				}
			}
		}
		
		// ----------------------------------------------------------
		if (DESTINATION==OntologyStorageLocation.Both || DESTINATION==OntologyStorageLocation.OntologyFile) {
			this.getSetupDataModelStorageServiceOntology().setDataModel(networkElement, this.ontologyAdapter.getOntologyBaseClasses());
			if (DESTINATION==OntologyStorageLocation.OntologyFile) {
				// --- Clean up old style model ---------------------
				networkElement.setDataModelBase64(null);
			}
		}
		return settings;
	}
	
	/**
	 * This method creates the XML form from the instances.
	 *
	 * @param ontoInstances the ontology instances as object array
	 * @return the XML from instances
	 */
	protected Vector<String> getXML64FromInstances(Object[] ontoInstances) {
	
		Vector<String> xml64Vector = new Vector<>();
		String[] classReferences = this.ontologyAdapter.getOntologyClassReferences();
		for (int i = 0; i < classReferences.length; i++) {
			
			// --- Get the corresponding Ontology-Instance ----------
			OntologyClassTreeObject octo = this.ontologyAdapter.getOntologyVisualizationHelper().getClassTreeObject(classReferences[i]);
			Ontology ontology = octo.getOntologyClass().getOntologyInstance();
						
			// --- Generate XML of this object ----------------------
			String xml = null;
			String xml64 = null;
			if (i<ontoInstances.length) {
				xml = this.getXMLOfInstance(ontoInstances[i], ontology);
			}
			if (xml!=null && xml.equals("")==false) {
				try {
					xml64 = new String(Base64.encodeBase64(xml.getBytes("UTF8")));
				} catch (UnsupportedEncodingException uee) {
					uee.printStackTrace();
				}	
			}
			xml64Vector.add(xml64);
		}
		return xml64Vector;
	}
	/**
	 * This method translates an object instance to a String by the
	 * given instance of the current ontology. The object must be a
	 * part of this ontology.
	 *
	 * @param ontologyObject the ontology object
	 * @param ontology the ontology
	 * @return the xML of instance
	 */
	protected String getXMLOfInstance(Object ontologyObject, Ontology ontology) {
		
		XMLCodec codec = new XMLCodec();
		String xmlRepresentation = null;
		try {
			// --- OntologyBeanGenerator for Protege 3.3.1 ---------------
			xmlRepresentation = codec.encodeObject(ontology, ontologyObject, true);
			
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		if (xmlRepresentation!=null) xmlRepresentation.trim();
		return xmlRepresentation;
	}

	
	// ----------------------------------------------------------------------------------
	// --- From here, help methods for saving in an extra file can be found -------------
	// ----------------------------------------------------------------------------------
	/**
	 * Gets the setup data model storage service for ontology instances.
	 * @return the setup data model storage service ontology
	 */
	private SetupDataModelStorageServiceOntology getSetupDataModelStorageServiceOntology() {
		SetupDataModelStorageService sdmService = super.getSetupDataModelStorageService();
		if (sdmService!=null && sdmService instanceof SetupDataModelStorageServiceOntology) {
			return (SetupDataModelStorageServiceOntology) sdmService;
		}
		return null;
	}
	
	
}
