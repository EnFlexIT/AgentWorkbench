package de.enflexit.common.bundleEvaluation;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The Class CacheClassFilterResult contains classes that were found 
 * by one filter within a single bundle during the evaluation.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
@XmlRootElement(name = "FilterClassList")
@XmlType(name = "FilterClassList", propOrder = {
    "filterScope",
    "filteredClasses"
})
public class CacheClassFilterResult {

	@XmlAttribute(name = "FilterScope")
	private String filterScope;

	@XmlElementWrapper(name = "FilteredClasses")
	@XmlElement(name = "FilteredClass")
	private ArrayList<String> filteredClasses;

	
	/**
	 * Instantiates a new cache class filter result.
	 */
	public CacheClassFilterResult() {}
	/**
	 * Instantiates a new cache class filter result.
	 * @param filterScope the filter scope
	 */
	public CacheClassFilterResult(String filterScope) {
		this.setFilterScope(filterScope);
	}
	
	/**
	 * Returns the filter scope.
	 * @return the filter scope
	 */
	@XmlTransient
	public String getFilterScope() {
		return filterScope;
	}
	/**
	 * Sets the filter scope.
	 * @param filterScope the new filter scope
	 */
	public void setFilterScope(String filterScope) {
		this.filterScope = filterScope;
	}
	
	/**
	 * Returns the filtered classes.
	 * @return the filtered classes
	 */
	@XmlTransient
	public ArrayList<String> getFilteredClasses() {
		return filteredClasses;
	}
	/**
	 * Returns the filtered classes and ensures that the return value will never be Null.
	 * @return the filtered classes not null
	 */
	public ArrayList<String> getFilteredClassesNotNull() {
		if (filteredClasses==null) {
			filteredClasses = new ArrayList<>();
		}
		return filteredClasses;
	}
	/**
	 * Sets the filtered classes.
	 * @param filteredClasses the new filtered classes
	 */
	public void setFilteredClasses(ArrayList<String> filteredClasses) {
		this.filteredClasses = filteredClasses;
	}
	/**
	 * Sets the filtered classes to null if empty.
	 */
	public void setFilteredClassesToNullIfEmpty() {
		if (filteredClasses!=null && filteredClasses.size()==0) {
			filteredClasses=null;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {
		
		if (compObject==null) return false;
		
		if (compObject instanceof CacheClassFilterResult) {
			CacheClassFilterResult cfrComp = (CacheClassFilterResult) compObject;
			return cfrComp.getFilterScope().equals(this.getFilterScope());
		}
		return false;
	}
}
