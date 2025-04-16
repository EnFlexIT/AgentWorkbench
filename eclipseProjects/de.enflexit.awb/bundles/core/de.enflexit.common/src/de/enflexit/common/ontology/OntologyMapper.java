package de.enflexit.common.ontology;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class OntologyMapper allows to define application wide mappings for Ontologies.
 * This is typically needed, if ontologies are moved from one package to another (after refactoring).
 * To define a mapping, define an {@link OntologyMapping} and register it by using the method
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyMapper {

	private static List<OntologyMapping> ontoMappingList; 
	
	/**
	 * Returns the registered {@link OntologyMapping}s as list.
	 * @return the ontology mapping list
	 */
	private static List<OntologyMapping> getOntologyMappingList() {
		if (ontoMappingList==null) {
			ontoMappingList = new ArrayList<>();
		}
		return ontoMappingList;
	}
	/**
	 * Can be used to register an application wide ontology mapping.
	 * @param om the OntologyMapping to register (null is not allowed)
	 */
	public static void registerOntologyMapping(OntologyMapping om) {
		// --- Do some validation checks ------------------
		if (om==null) {
			throw new IllegalArgumentException("The specified OntologyMapping is null!");
		} else if (om.isValidMapping()==false) {
			throw new IllegalArgumentException("The specified OntologyMapping is invalid: " + om.toString());
		}
		// --- Add to the mapping list -------------------- 
		if (getOntologyMappingList().contains(om)==false) {
			getOntologyMappingList().add(om);
		}
		
	}
	/**
	 * Can be used to unregister an application wide ontology mapping.
	 * @param om the OntologyMapping to unregister
	 */
	public static void unregisterOntologyMapping(OntologyMapping om) {
		if (om!=null) {
			getOntologyMappingList().remove(om);
		}
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here some operational methods -------------------------------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Returns the mapped ontology class name (if a class specific mapping is defined).
	 *
	 * @param ontologyClassNameToCheck the ontology class name to check
	 * @return the original or mapped ontology class name
	 */
	public static String getMappedOntologyClassName(String ontologyClassNameToCheck) {
		
		String ontologyClassNameNew = ontologyClassNameToCheck;
		if (ontologyClassNameNew!=null && ontologyClassNameNew.isEmpty()==false && getOntologyMappingList().size()>0) {
			// --- Loop over defined OntologyMapping's ------------------------ 
			for (int i = 0; i < getOntologyMappingList().size(); i++) {
				ontologyClassNameNew = getOntologyMappingList().get(i).applyMapping(ontologyClassNameToCheck);
			}
		}
		return ontologyClassNameNew;
	}
	
}
