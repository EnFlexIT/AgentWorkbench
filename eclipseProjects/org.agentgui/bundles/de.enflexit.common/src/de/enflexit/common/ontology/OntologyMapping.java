package de.enflexit.common.ontology;

import java.io.Serializable;

/**
 * The Class OntologyMapping allows to define an Ontology mapping. This is typically needed after refactoring
 * actions and when an Ontology is located in a new package.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyMapping implements Serializable {

	private static final long serialVersionUID = -4485684655852422892L;

	private String packageLocationOld;
	private String packageLocationNew;
	
	/**
	 * Instantiates a new ontology mapping.
	 */
	public OntologyMapping() { }
	/**
	 * Instantiates a new ontology mapping.
	 *
	 * @param packageLocationOld the package location old
	 * @param packageLocationNew the package location new
	 */
	public OntologyMapping(String packageLocationOld, String packageLocationNew) {
		this.setPackageLocationOld(packageLocationOld);
		this.setPackageLocationNew(packageLocationNew);
	}

	public String getPackageLocationOld() {
		return packageLocationOld;
	}
	public void setPackageLocationOld(String packageLocationOld) {
		this.packageLocationOld = packageLocationOld;
	}

	public String getPackageLocationNew() {
		return packageLocationNew;
	}
	public void setPackageLocationNew(String packageLocationNew) {
		this.packageLocationNew = packageLocationNew;
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here some operational methods -------------------------------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Applies this mapping to the specified ontology class name. If the mapping is valid
	 * and the class name starts with the locally defined old package location, the class name
	 * will be returned with the new package location.  
	 *
	 * @param ontologyClassNameToCheck the ontology class name to check
	 * @return the original or corrected ontology class name 
	 * 
	 * @see #isValidMapping()
	 */
	public String applyMapping(String ontologyClassNameToCheck) {
		
		if (ontologyClassNameToCheck==null || ontologyClassNameToCheck.isEmpty()==true || this.isValidMapping()==false) return ontologyClassNameToCheck;
		
		String ontologyClassNameNew = ontologyClassNameToCheck;
		if (ontologyClassNameToCheck.startsWith(this.getPackageLocationOld())==true) {
			ontologyClassNameNew = this.getPackageLocationNew() + ontologyClassNameNew.substring(this.getPackageLocationOld().length());
		}
		return ontologyClassNameNew;
	}
	
	
	/**
	 * Checks if the current mapping is valid.
	 * @return true, if is valid mapping
	 */
	public boolean isValidMapping() {
		
		if (this.getPackageLocationOld()==null || this.getPackageLocationOld().isEmpty()==true) {
			return false;
		}
		if (this.getPackageLocationNew()==null || this.getPackageLocationNew().isEmpty()==true) {
			return false;
		}
		if (this.getPackageLocationNew().equals(this.getPackageLocationOld())==true) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	
		String desc = "[" + this.getClass().getSimpleName() + "] ";
		
		desc += "old package: "; 
		if (this.getPackageLocationOld()==null || this.getPackageLocationOld().isEmpty()==true) {
			desc += "not defined";
		} else {
			desc += this.getPackageLocationOld();
		}
		
		desc += ", new package: "; 
		if (this.getPackageLocationNew()==null || this.getPackageLocationNew().isEmpty()==true) {
			desc += "not defined";
		} else {
			desc += this.getPackageLocationNew();
		}
		return desc;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {
		
		if (compObject==null || !(compObject instanceof OntologyMapping)) return false;
		
		OntologyMapping omComp = (OntologyMapping) compObject;
		return this.toString().equals(omComp.toString());
	}
	
}

