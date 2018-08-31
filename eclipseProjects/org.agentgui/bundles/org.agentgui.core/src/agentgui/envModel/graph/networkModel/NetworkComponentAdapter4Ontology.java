/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.graph.networkModel;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.JComponent;

import org.apache.commons.codec.binary.Base64;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.envModel.graph.controller.BasicGraphGuiProperties;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import de.enflexit.common.ExceptionHandling;
import de.enflexit.common.ontology.OntologyClassTreeObject;
import de.enflexit.common.ontology.OntologyVisualisationHelper;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

/**
 * The Class NetworkComponentAdapter4Ontology.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter4Ontology extends NetworkComponentAdapter4DataModel {

	private OntologyVisualisationHelper ovh;
	private OntologyInstanceViewer oiv;
	
	/**
	 * Instantiates a new network component adapter for ontologies.
	 *
	 * @param graphController the graph controller
	 */
	public NetworkComponentAdapter4Ontology(GraphEnvironmentController graphController) {
		super(graphController);
	}

	/**
	 * Define the Vector of the needed Ontologies for this type of NetworkComponent.
	 * @return the Vector of ontology classes 
	 */
	public abstract Vector<Class<? extends Ontology>> getOntologyBaseClasses();
	
	/**
	 * Returns the ontology base class references.
	 * @return the ontology base class references
	 */
	public Vector<String> getOntologyBaseClassReferences() {
		Vector<String> classReferences = null;
		if (this.getOntologyBaseClasses()!=null) {
			classReferences = new Vector<String>();
			for (Class<? extends Ontology> ontoClass : this.getOntologyBaseClasses()) {
				classReferences.add(ontoClass.getName());
			}
		}
		return classReferences;
	}
	
	/**
	 * Gets the ontology class references.
	 * @return the ontology class references
	 */
	public abstract String[] getOntologyClassReferences();
	
	
	/**
	 * Sets the ontology visualisation helper.
	 * @param ontologyVisualisationHelper the new ontology visualisation helper
	 */
	public void setOntologyVisualisationHelper(OntologyVisualisationHelper ontologyVisualisationHelper) {
		this.ovh = ontologyVisualisationHelper;
	}
	/**
	 * Returns the ontology visualisation helper.
	 * @return the ontology visualisation helper
	 */
	public OntologyVisualisationHelper getOntologyVisualisationHelper() {
		if (this.ovh==null) {
			if (this.getOntologyBaseClasses()==null) {
				throw new NullPointerException("The ontology base classes of the NetworkComponentAdapter were not defined!");
			} else {
				this.ovh = new OntologyVisualisationHelper(this.getOntologyBaseClassReferences());	
			}
		}
		return this.ovh;
	}
	
	/**
	 * Returns the ontology instance viewer.
	 * @return the ontology instance viewer
	 */
	private OntologyInstanceViewer getOntologyInstanceViewer() {
		if (this.oiv==null) {
			if (this.getOntologyClassReferences()==null) {
				throw new NullPointerException("The references to the classes out of the configured ontologies are not set!");
			} else {
				this.oiv = new OntologyInstanceViewer(this.getOntologyVisualisationHelper(), this.getOntologyClassReferences());
				this.oiv.setAllowViewEnlargement(false);	
			}
		}
		return this.oiv;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#save()
	 */
	@Override
	public void save() {
		this.getOntologyInstanceViewer().save();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#setVisualizationComponent(javax.swing.JComponent)
	 */
	@Override
	public final void setVisualizationComponent(JComponent visualizationComponent) {
		// --- Nothing to do in case of an Ontology -------
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#getVisualizationComponent(agentgui.envModel.graph.controller.BasicGraphGuiProperties)
	 */
	@Override
	public JComponent getVisualizationComponent(BasicGraphGuiProperties internalPropertyFrame) {
		return this.getOntologyInstanceViewer();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#setDataModel(java.lang.Object)
	 */
	@Override
	public void setDataModel(Object dataModel) {
		Object[] dataModelArray = null;
		if (dataModel!=null) {
			dataModelArray = (Object[]) dataModel;	
		}
		this.getOntologyInstanceViewer().setConfigurationInstances(dataModelArray);	
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#getDataModel()
	 */
	@Override
	public Object getDataModel() {
		return this.getOntologyInstanceViewer().getConfigurationInstances();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#getDataModelBase64Encoded(java.lang.Object)
	 */
	@Override
	public Vector<String> getDataModelBase64Encoded(Object dataModel) {
		Vector<String> base64Vector = this.getXML64FromInstances((Object[]) dataModel);
		if (base64Vector.size()==0) {
			base64Vector=null;
		}
		return base64Vector;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#getDataModelBase64Decoded(java.util.Vector)
	 */
	@Override
	public Object getDataModelBase64Decoded(Vector<String> dataModel) {
		if (dataModel==null) {
			throw new NullPointerException("Vector<String> 'dataModel' must not be null!");
		}
		return this.getInstancesFromXML64(dataModel);
	}

	
	// ----------------------------------------------------------------------------------
	// --- From here, conversion from XML64 to instances --------------------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Gets the instances from XML 64.
	 *
	 * @param ontoXML64Vector the onto XML 64 vector
	 * @return the instances from XML 64
	 */
	private Object[] getInstancesFromXML64(Vector<String> ontoXML64Vector) {
		
		// --- Decode Base64 first ----------------------------------
		Vector<String> ontoXMLVector = new Vector<>();
		for (String ontoXML64 : ontoXML64Vector) {
			String ontoXML = null;
			if (ontoXML64!=null && ontoXML64.equals("")==false) {
				try {
					ontoXML = new String(Base64.decodeBase64(ontoXML64.getBytes()), "UTF8");	
				} catch (UnsupportedEncodingException uee) {
					uee.printStackTrace();
				}
			}
			ontoXMLVector.add(ontoXML);
		}
		
		// --- Create instances -------------------------------------
		Object[] instances = new Object[this.getOntologyClassReferences().length];
		String[] classReferences = this.getOntologyClassReferences();
		for (int i = 0; i < classReferences.length; i++) {
			// --- Get the corresponding Ontology-Instance ----------
			OntologyClassTreeObject octo = this.getOntologyVisualisationHelper().getClassTreeObject(classReferences[i]);
			Ontology ontology = octo.getOntologyClass().getOntologyInstance();
			
			String xml = null;
			if (i<=(ontoXMLVector.size()-1)) {
				xml = ontoXMLVector.get(i);
			}
			instances[i] = this.getInstanceOfXML(xml, classReferences[i], ontology);
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
	private Object getInstanceOfXML(String xmlString, String className, Ontology ontology) {
		
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
	private Object getNewClassInstance(String className) {
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
	/**
	 * This method creates the XML form from the instances.
	 *
	 * @param ontoInstances the ontology instances as object array
	 * @return the XML from instances
	 */
	private Vector<String> getXML64FromInstances(Object[] ontoInstances) {
	
		Vector<String> xml64Vector = new Vector<>();
		String[] classReferences = this.getOntologyClassReferences();
		for (int i = 0; i < classReferences.length; i++) {
			
			// --- Get the corresponding Ontology-Instance ----------
			OntologyClassTreeObject octo = this.getOntologyVisualisationHelper().getClassTreeObject(classReferences[i]);
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
	private String getXMLOfInstance(Object ontologyObject, Ontology ontology) {
		
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
	
}