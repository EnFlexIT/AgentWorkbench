/*
 * 
 */
package de.enflexit.common.bundleEvaluation;

import java.util.ArrayList;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.enflexit.common.classLoadService.BaseClassLoadServiceUtility;

/**
 * The Class CacheBundleResult is used within the {@link Cache} and handles
 * the results for single bundles.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
@XmlType(name = "CacheBundleResult", propOrder = {
    "classFilterResultList"
})
public class CacheBundleResult {

	@XmlAttribute(name="SymbolicBundleName")
	private String symbolicBundleName;
	@XmlAttribute(name="Version")
	private String version;
	@XmlAttribute(name="LastModified")
	private long lastModified;

	@XmlElementWrapper(name = "ClassFilterResultList")
	@XmlElement(name = "ClassFilterResult")
	private ArrayList<CacheClassFilterResult> classFilterResultList;

	
	/**
	 * Instantiates a new cache bundle result.
	 */
	public CacheBundleResult() {}
	/**
	 * Instantiates a new bundle description.
	 *
	 * @param symbolicBundleName the symbolic bundle name
	 * @param version the version
	 * @param lastModified the last modified
	 */
	public CacheBundleResult(String symbolicBundleName, String version, long lastModified) {
		this.setSymbolicBundleName(symbolicBundleName);
		this.setVersion(version);
		this.setLastModified(lastModified);
	}

	@XmlTransient
	public String getSymbolicBundleName() {
		return symbolicBundleName;
	}
	public void setSymbolicBundleName(String symbolicBundlename) {
		this.symbolicBundleName = symbolicBundlename;
	}

	@XmlTransient
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	@XmlTransient
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	/**
	 * Returns the class filter result list.
	 * @return the class filter result list
	 */
	public ArrayList<CacheClassFilterResult> getClassFilterResultList() {
		return classFilterResultList;
	}
	/**
	 * Returns the class filter result list.
	 * @return the class filter result list
	 */
	public ArrayList<CacheClassFilterResult> getClassFilterResultListNotNull() {
		if (classFilterResultList==null) {
			classFilterResultList = new ArrayList<>();
		}
		return classFilterResultList;
	}

	/**
	 * Sets the filtered classes to null if empty. Built for a maintenance call before saving.
	 */
	public void setFilteredClassesToNullIfEmpty() {
		for (int i = 0; i < this.getClassFilterResultListNotNull().size(); i++) {
			CacheClassFilterResult classFilterResult = this.getClassFilterResultListNotNull().get(i);
			classFilterResult.setFilteredClassesToNullIfEmpty();
		}
	}
	
	/**
	 * Returns the filter results (the list of classes found) for the specified filter scope.
	 *
	 * @param filterScope the filter scope
	 * @return the filter results
	 */
	public CacheClassFilterResult getClassFilterResult(String filterScope) {
		for (int i = 0; i < this.getClassFilterResultListNotNull().size(); i++) {
			CacheClassFilterResult classFilterResult = this.getClassFilterResultListNotNull().get(i);
			if (classFilterResult.getFilterScope().equals(filterScope)) {
				return classFilterResult;
			}
		}
		return null;
	}
	
	/**
	 * Adds the specified class to the specified filter. If no class is defined, just the 
	 * {@link CacheClassFilterResult} will be created and added without result class.
	 *
	 * @param filterScope the filter scope
	 * @param classFound the single class found
	 */
	public void addClassFilterResult(String filterScope, String classFound) {
		CacheClassFilterResult classFilterResult = this.getClassFilterResult(filterScope);
		if (classFilterResult==null) {
			classFilterResult = new CacheClassFilterResult(filterScope);
			this.getClassFilterResultListNotNull().add(classFilterResult);
		}
		if (classFound!=null && classFilterResult.getFilteredClassesNotNull().contains(classFound)==false) {
			classFilterResult.getFilteredClassesNotNull().add(classFound);
		}
	}

	/**
	 * Updates the specified bundle class filter from cached / known classes.
	 *
	 * @param bundleFilterToFillFromCache the filter to apply
	 * @return the vector
	 */
	public Vector<AbstractBundleClassFilter> updateBundleClassFilterFromCache(Vector<AbstractBundleClassFilter> bundleFilterToFillFromCache) {
		
		Vector<AbstractBundleClassFilter> reducedFilterList = new Vector<>();
		for (int i = 0; i < bundleFilterToFillFromCache.size(); i++) {
			
			AbstractBundleClassFilter bcFilter = bundleFilterToFillFromCache.get(i);
			CacheClassFilterResult classFilterResults = this.getClassFilterResult(bcFilter.getFilterScope());
			if (classFilterResults!=null) {
				// --- Fill filter results from cache -------------------------
				for (int j = 0; j < classFilterResults.getFilteredClassesNotNull().size(); j++) {
					try {
						String cachedClassName = classFilterResults.getFilteredClassesNotNull().get(j);
						// --- Check, if class can be found -------------------
						Class<?> clazz = BaseClassLoadServiceUtility.forName(cachedClassName);
						if (clazz!=null) {
							bcFilter.addClassFound(cachedClassName, this.getSymbolicBundleName());
						}
					} catch (ClassNotFoundException | NoClassDefFoundError e) {
						// No exceptions will be thrown
					}
				}
				
			} else {
				// --- Put back into filter vector for regular search --------- 
				reducedFilterList.add(bcFilter);
			}
		}
		return reducedFilterList;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {
		if (compObject==null) return false;
		if (compObject instanceof CacheBundleResult) {
			CacheBundleResult bdComp = (CacheBundleResult) compObject;
			// --- Compare symbolic bundle name -------
			if (bdComp.getSymbolicBundleName().equals(this.getSymbolicBundleName())==true) {
				// --- Compare version ----------------
				if (bdComp.getVersion().equals(this.getVersion())==true) {
					if (bdComp.getLastModified()==this.getLastModified()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
