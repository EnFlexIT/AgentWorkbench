package de.enflexit.common.bundleEvaluation;


/**
 * The Class ClassLocaton is used to describe the bundle location 
 * and the filter that found the class.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ClassLocaton {
	
	private String className;
	private String bundleName;
	private AbstractBundleClassFilter filter;
	
	/**
	 * Instantiates a new class locator.
	 *
	 * @param className the class name
	 * @param bundleName the symbolic bundle name
	 * @param filter the filter that found the class
	 */
	public ClassLocaton(String className, String bundleName, AbstractBundleClassFilter filter) {
		this.setClassName(className);
		this.setBundleSymbolicName(bundleName);
		this.setBundleClassFilter(filter);
	}

	/**
	 * Returns the class name.
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * Sets the class name.
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Returns the symbolic bundle name.
	 * @return the bundle name
	 */
	public String getBundlSymboliceName() {
		return bundleName;
	}
	/**
	 * Sets the symbolic bundle name.
	 * @param bundleName the new bundle name
	 */
	public void setBundleSymbolicName(String bundleName) {
		this.bundleName = bundleName;
	}

	/**
	 * Returns the filter that found the class.
	 * @return the filter
	 */
	public AbstractBundleClassFilter getBundleClassFilter() {
		return filter;
	}
	/**
	 * Sets the filter.
	 * @param filter the new filter
	 */
	public void setBundleClassFilter(AbstractBundleClassFilter filter) {
		this.filter = filter;
	}
	
}
