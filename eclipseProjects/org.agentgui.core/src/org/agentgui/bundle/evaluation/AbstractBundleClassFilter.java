package org.agentgui.bundle.evaluation;

import java.rmi.server.UID;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * The Class AbstractBundleClassFilter can be extended in order to find classes
 * that satisfy the filter-internal, specific condition.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public abstract class AbstractBundleClassFilter {

	private HashMap<String, String> bundleClassesFound; 
	private Vector<UID> busyMarker;
	private Vector<BundleClassFilterListener> filterListener;
	
	
	// ----------------------------------------------------
	// --- Required methods for filter ---------- Start ---
	// ----------------------------------------------------
	/**
	 * Has to return the unique filter scope as String (e.g. the super class reference that is used for the filter).
	 * @return the unique filter scope as String
	 */
	public abstract String getFilterScope();

	/**
	 * Has to check if the specified class represents the current filter criteria.
	 *
	 * @param clazz the class to check
	 * @return true, if the specified is equal to the filter filter
	 */
	public abstract boolean isFilterCriteria(Class<?> clazz);
	
	/**
	 * Has to return <code>true</code> in case that the specified class fulfills the individual filter requirements.
	 *
	 * @param clazz the actual class to check by the current filter
	 * @return true, if the class is in the filter scope
	 */
	public abstract boolean isInFilterScope(Class<?> clazz);
	// ----------------------------------------------------
	// --- Required methods for filter ---------- End -----
	// ----------------------------------------------------

	
	// ----------------------------------------------------
	// --- Indicator, if filter is used --------- Start ---
	// ----------------------------------------------------
	/**
	 * Returns the vector of busy markers.
	 * @return the busy marker
	 */
	private Vector<UID> getBusyMarker() {
		if (busyMarker==null) {
			busyMarker = new Vector<>();	
		}
		return busyMarker;
	}
	/**
	 * Adds a marker that this filter is in use by an evaluation process .
	 * @param busyMarker the busy marker
	 */
	public void addBusyMarker(UID busyMarker) {
		int nMarkerOld = this.getBusyMarker().size();
		if (this.getBusyMarker().contains(busyMarker)==false) {
			this.getBusyMarker().add(busyMarker);
		}
		if (this.getBusyMarker().size()!=nMarkerOld) {
			this.setBusyToListener(this.isUsedInSearch());
		}
	}
	/**
	 * Removes the busy marker.
	 * @param busyMarker the busy marker
	 */
	public void removeBusyMarker(UID busyMarker) {
		int nMarkerOld = this.getBusyMarker().size();
		this.getBusyMarker().remove(busyMarker);
		if (this.getBusyMarker().size()!=nMarkerOld) {
			this.setBusyToListener(this.isUsedInSearch());
		}
	}
	/**
	 * Return if this filter is currently used by an evaluation process.
	 * @return true, if this filter is currently used
	 * @see BundleEvaluator
	 */
	public boolean isUsedInSearch() {
		return (this.getBusyMarker().size()>0);
	}
	/**
	 * Sets if this filter is currently used by a search process to the listener.
	 * @param isBusy the new busy indicator for the listener. 
	 */
	private void setBusyToListener(boolean isBusy) {
		Vector<BundleClassFilterListener> listenerVector = new Vector<>(this.getFilterListener());
		for (int i = 0; i < listenerVector.size(); i++) {
			listenerVector.get(i).setBusy(isBusy);
		}
	}
	// ----------------------------------------------------
	// --- Indicator, if filter is used --------- End -----
	// ----------------------------------------------------

	
	// ----------------------------------------------------
	// --- Filter Listener ---------------------- Start ---
	// ----------------------------------------------------
	/**
	 * Returns the current filter listener.
	 * @return the filter listener
	 */
	private Vector<BundleClassFilterListener> getFilterListener() {
		if (filterListener==null) {
			filterListener = new Vector<>();
		}
		return filterListener;
	}
	/**
	 * Adds the bundle class filter listener.
	 * @param classListener the class listener
	 */
	public void addBundleClassFilterListener(BundleClassFilterListener classListener) {
		synchronized(this) {
			if (this.getFilterListener().contains(classListener)==false) {
				// --- Add to the list of listener for this filter ----------------------
				this.getFilterListener().add(classListener);
				// --- Temporary, set busy to true --------------------------------------
				classListener.setBusy(true);
				// --- Provide the classes already found to the new listener ------------
				Vector<String> classVector = new Vector<>(this.getBundleClassesFound().keySet());
				for (int i = 0; i < classVector.size(); i++) {
					String className = classVector.get(i);
					String symbolicBundleName = this.getBundleClassesFound().get(className);
					classListener.addClassFound(className, symbolicBundleName);
				}
				// --- Add the final, right  busy marker --------------------------------
				classListener.setBusy(this.isUsedInSearch());
			}
		}
	}
	/**
	 * Removes the specified bundle class filter listener.
	 * @param classListener the class listener
	 */
	public void removeBundleClassFilterListener(BundleClassFilterListener classListener) {
		this.getFilterListener().remove(classListener);
	}
	// ----------------------------------------------------
	// --- Filter Listener ---------------------- End -----
	// ----------------------------------------------------

	
	/**
	 * Returns the classes found in the corresponding bundle (used the symbolic name here).
	 * @return the bundle classes found
	 */
	private HashMap<String, String> getBundleClassesFound() {
		if (bundleClassesFound==null) {
			bundleClassesFound = new HashMap<>();
		}
		return bundleClassesFound;
	}
	/**
	 * Adds the specified class to the vector of classes found, if not already there.
	 *
	 * @param symbolicBundleName the symbolic bundle name
	 * @param className the class name
	 */
	public void addClassFound(String className, String symbolicBundleName) {
		this.getBundleClassesFound().put(className, symbolicBundleName);
		// --- Inform listener ----------------------------
		for (BundleClassFilterListener listener : this.getFilterListener()) {
			listener.addClassFound(className, symbolicBundleName);
		}
	}
	
	/**
	 * Returns the ordered vector of all classes found.
	 * @return the classes found
	 */
	public Vector<String> getClassesFound() {
		Vector<String> classesFound = new Vector<String>(this.getBundleClassesFound().keySet()); 
		if (classesFound.size()>0) Collections.sort(classesFound);
		return classesFound; 
	}
	/**
	 * Returns the ordered vector of classes found for the specified bundle or null in case that the symbolic bundle name is null.
	 * @param symbolicBundleName the symbolic bundle name
	 * @return the classes found
	 */
	public Vector<String> getClassesFound(String symbolicBundleName) {
		
		if (symbolicBundleName==null) return null;

		Vector<String> classesFound = new Vector<String>();
		HashMap<String,String> classesFoundHash = new HashMap<>(this.getBundleClassesFound());
		for (String className : classesFoundHash.keySet()) {
			String bundlename = classesFoundHash.get(className);
			if (bundlename.equals(symbolicBundleName)) {
				classesFound.add(className);
			}
		}

		if (classesFound.size()>0) Collections.sort(classesFound);
		return classesFound;
	}
	
	/**
	 * Returns the ordered vector of bundles filtered.
	 * @return the bundles filtered
	 */
	public Vector<String> getBundlesFiltered() {
		// --- Create HashSet first to uniquely find bundle names --
		HashSet<String> bundleHashSet = new HashSet<String>();
		bundleHashSet.addAll(this.getBundleClassesFound().values());
		// --- Convert into a sorted vector -------------------------
		Vector<String> bundleFiltered = new Vector<>(bundleHashSet);
		Collections.sort(bundleFiltered);
		return bundleFiltered;
	}
	
	/**
	 * Removes all classes found for the specified bundle.
	 * @param symbolicBundleName the symbolic bundle name
	 */
	public void removeBundleClasses(String symbolicBundleName) {
		HashMap<String,String> classesFoundHash = new HashMap<>(this.getBundleClassesFound());
		for (String className : classesFoundHash.keySet()) {
			String bundlename = classesFoundHash.get(className);
			if (bundlename.equals(symbolicBundleName)) {
				this.getBundleClassesFound().remove(className);
				// --- Inform listener -------------------- 
				for (BundleClassFilterListener listener : this.getFilterListener()) {
					listener.removeClassFound(className, symbolicBundleName);
				}
			}
		}
	}
	
	/**
	 * Returns the class location of the specified class name.
	 * @param className the class name
	 * @return the class location or null, if the class could not be found
	 */
	public ClassLocaton getClassLocation(String className) {
		ClassLocaton classLocation = null;
		String bundleName = this.getBundleClassesFound().get(className);
		if (bundleName!=null) {
			classLocation = new ClassLocaton(className, bundleName, this);
		}
		return classLocation;
	}
	
	/**
	 * Prints the filter result.
	 */
	public void printFilterResult() {
		System.out.println("Filter for: '" + this + "'");
		for (String symbolicBundleName : this.getBundlesFiltered()) {
			Vector<String> classeNamesFound = this.getClassesFound(symbolicBundleName);
			System.out.println("=> " + classeNamesFound.size() + " found in " + symbolicBundleName);
			for (int i = 0; i < classeNamesFound.size(); i++) {
				System.out.println(classeNamesFound.get(i));
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getFilterScope();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj==null) return false;
		if (obj==this) return true;
		if (obj instanceof AbstractBundleClassFilter) {
			AbstractBundleClassFilter abcf = (AbstractBundleClassFilter) obj;
			return abcf.toString().equals(this.toString());
		}
		return super.equals(obj);
	}

}
