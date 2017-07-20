package org.agentgui.bundle.evaluation;

/**
 * The listener interface for receiving bundleClassFilter events.
 * The class that is interested in processing a bundleClassFilter
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addBundleClassFilterListener</code> method. When
 * the bundleClassFilter event occurs, that object's appropriate
 * method is invoked.
 *
 * @see BundleClassFilterEvent
 */
public interface BundleClassFilterListener {

	/**
	 * Will be invoked, if a class was found in a bundle.
	 *
	 * @param className the class name
	 * @param symbolicBundleName the symbolic bundle name
	 */
	public void addClassFound(String className, String symbolicBundleName);
	
	/**
	 * Will be invoked if a class was removed from the classes found.
	 *
	 * @param className the class name
	 * @param symbolicBundleName the symbolic bundle name
	 */
	public void removeClassFound(String className, String symbolicBundleName);
	
	/**
	 * Sets if the current {@link AbstractBundleClassFilter} is busy or not.
	 * 
	 * @param isBusy set <code>true</code> if the class filter is currently busy
	 */
	public void setBusy(boolean isBusy);
	
}
