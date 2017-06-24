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
package agentgui.core.ontologies;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.codec.binary.Base64;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

/**
 * The Class OntologyInstanceHelper provides static methods that enable to convert an object instance 
 * out of an ontology from instance to XML-string to Base64 encoded XML-string.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyInstanceHelper {

	
	/**
	 * Returns the ontology object instance from the specified base 64 encoded XML string.
	 *
	 * @param base64EncodedXML the base 64 encoded XML
	 * @param ontologyMainClassReference the main class reference of the ontology 
	 * @return the ontology instance from XML bas 64
	 */
	public static Object getOntologyObjectInstanceFromBase64EncodedXMLString(String base64EncodedXML, String ontologyMainClassReference) {
		
		Object ontologyInstance = null;
		try {
			String xmlString = new String(Base64.decodeBase64(base64EncodedXML.getBytes()), "UTF8");
			ontologyInstance = getOntologyObjectInstanceFromXMLString(xmlString, ontologyMainClassReference);
		
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		return ontologyInstance;
	}
	/**
	 * Returns the ontology object instance from XML string.
	 *
	 * @param xmlString the XML string of the instance to create
	 * @param ontologyMainClassReference the main class reference of the ontology 
	 * @return the ontology object instance from XML string
	 */
	public static Object getOntologyObjectInstanceFromXMLString(String xmlString, String ontologyMainClassReference) {
		
		Object ontologyInstance = null;
		Ontology onto = getOntology(ontologyMainClassReference);
		if (onto!=null) {
			ontologyInstance = getOntologyObjectInstanceFromXMLString(xmlString, onto);
		}
		return ontologyInstance;
	}
	/**
	 * Returns the ontology object instance from XML string.
	 *
	 * @param xmlString the XML string of the instance to create
	 * @param ontology the ontology, where the actual class of the XML string is defined
	 * @return the ontology object instance from XML string
	 */
	public static Object getOntologyObjectInstanceFromXMLString(String xmlString, Ontology ontology) {
		
		Object ontologyInstance = null;
		
		if (xmlString!=null && xmlString.equals("")==false) {
			try {
				XMLCodec codec = new XMLCodec();
				ontologyInstance = codec.decodeObject(ontology, xmlString);
				
			} catch (CodecException ce) {
				ce.printStackTrace();
			} catch (OntologyException oe) {
				oe.printStackTrace();
			}		
		}
		return ontologyInstance;
	}
	
	
	/**
	 * Returns the base 64 encoded XML string from ontology object instance.
	 *
	 * @param ontologyInstance the ontology instance
	 * @param ontologyMainClassReference the ontology main class reference
	 * @return the 64 encoded XML string from ontology object instance
	 */
	public static String getBase64EncodedXMLStringFromOntologyObjectInstance(Object ontologyInstance, String ontologyMainClassReference) {
	
		String bas64EncodedXML = null; 
		if (ontologyInstance!=null) {
			String xmlString = getXMLStringFromOntologyObjectInstance(ontologyInstance, ontologyMainClassReference);
			if (xmlString!=null && xmlString.equals("")==false) {
				try {
					bas64EncodedXML = new String(Base64.encodeBase64(xmlString.getBytes("UTF8")));	
					
				} catch (UnsupportedEncodingException uex) {
					uex.printStackTrace();
				}
			}
		}
		return bas64EncodedXML;
	}
	/**
	 * Returns the XML string from the ontology object instance.
	 *
	 * @param ontologyInstance the ontology instance
	 * @param ontologyMainClassReference the ontology main class reference
	 * @return the XML string from the ontology object instance
	 */
	public static String getXMLStringFromOntologyObjectInstance(Object ontologyInstance, String ontologyMainClassReference) {
		
		String xmlRepresentation = null;
		Ontology onto = getOntology(ontologyMainClassReference);
		if (onto!=null) {
			xmlRepresentation = getXMLStringFromOntologyObjectInstance(ontologyInstance, onto);
		}
		return xmlRepresentation;
	}
	/**
	 * Returns the XML string from the ontology object instance.
	 *
	 * @param ontologyInstance the ontology instance
	 * @param ontology the ontology, where the actual class of the XML string is defined
	 * @return the XML string from the ontology object instance 
	 */
	public static String getXMLStringFromOntologyObjectInstance(Object ontologyInstance, Ontology ontology) {
		
		String xmlRepresentation = null;
		try {
			XMLCodec codec = new XMLCodec();
			xmlRepresentation = codec.encodeObject(ontology, ontologyInstance, true);
			if (xmlRepresentation!=null) {
				xmlRepresentation.trim();
			}
			
		} catch (CodecException ce) {
			ce.printStackTrace();
		} catch (OntologyException oe) {
			oe.printStackTrace();
		}
		return xmlRepresentation;
	}
	
	
	/**
	 * This Method returns the instance of the Ontology, specified by it's class reference.
	 *
	 * @param ontologyClassReference the ontology class reference
	 * @return The instance of the ontology
	 */
	public static Ontology getOntology(String ontologyClassReference) {
		
		Ontology ontology = null;
		if (ontologyClassReference!=null) {
			try {
				// --- Try to get an instance of the current Ontology ---------
				ontology = ClassLoadServiceUtility.getOntologyInstance(ontologyClassReference);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return ontology;
	}
	
}
