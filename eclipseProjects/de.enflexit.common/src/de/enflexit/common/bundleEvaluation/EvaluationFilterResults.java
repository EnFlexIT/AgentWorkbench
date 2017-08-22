package de.enflexit.common.bundleEvaluation;

import java.util.Vector;

import org.osgi.framework.Bundle;

/**
 * The Class EvaluationFilterResults holds all filter 
 * and their search results for the {@link BundleEvaluator}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
class EvaluationFilterResults extends Vector<AbstractBundleClassFilter> {

	private static final long serialVersionUID = -470312383603184190L;

	private BundleEvaluator bundleEvaluator;
	
	/**
	 * Instantiates a new evaluation filter results.
	 * @param bundleEvaluator the bundle evaluator
	 */
	public EvaluationFilterResults(BundleEvaluator bundleEvaluator) {
		this.bundleEvaluator = bundleEvaluator;
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.Vector#addElement(java.lang.Object)
	 */
	@Override
	public synchronized void addElement(AbstractBundleClassFilter bundleClassFilter) {
		this.add(bundleClassFilter);
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(AbstractBundleClassFilter bundleClassFilter) {
		return this.add(bundleClassFilter, true);
	}
	/**
	 * Adds the specified {@link AbstractBundleClassFilter} to the list of active filter and starts the evaluation with this filter. 
	 *
	 * @param bundleClassFilter the bundle class filter
	 * @param doBundleEvaluation set true, if you want to directly evaluate all bundles with the current filter
	 * @return true, if successful
	 */
	public synchronized boolean add(AbstractBundleClassFilter bundleClassFilter, boolean doBundleEvaluation) {
		boolean added = false;
		if (bundleClassFilter==null) return false;
		if (this.contains(bundleClassFilter)==false) {
			added = super.add(bundleClassFilter);
			if (doBundleEvaluation==true) {
				this.bundleEvaluator.evaluateAllBundles(bundleClassFilter);
			}
		}
		return added;
	}

	/**
	 * Removes the evaluation filter results of the specified bundle.
	 * @param bundle the bundle from which the results have to be removed
	 */
	public void removeEvaluationFilterResults(Bundle bundle) {
		if (bundle==null) return;
		for (int i = 0; i < this.size(); i++) {
			AbstractBundleClassFilter abcf = this.get(i);
			if (abcf!=null) abcf.removeBundleClasses(bundle.getSymbolicName());
		}
	}
	
	/**
	 * Returns the AbstractBundleClassFilter by the specified search class.
	 * @param searchClassOfBundleFilter the search class of the bundle filter
	 * @return the bundle class filter by search class
	 */
	public AbstractBundleClassFilter getBundleClassFilterBySearchClass(Class<?> searchClassOfBundleFilter) {
		for (int i = 0; i < this.size(); i++) {
			AbstractBundleClassFilter filter =  this.get(i);
			if (filter!=null) {
				if (filter.isFilterCriteria(searchClassOfBundleFilter)==true) {
					return filter;
				}
			}
		}
		return null;
	}
	
		
	/**
	 * Returns the ClassLocaton for the specified class name.
	 * @param className the class name
	 * @return the class location or null, if the class was not found
	 */
	public ClassLocaton getClassLocation(String className)  {
		
		if (className==null || className.equals("")==true) return null;
		
		// --- Plan A: Try to find class in the AbstractBundleClassFilter -----
		ClassLocaton classLocation = null;
		for (int i = 0; i < this.size(); i++) {
			AbstractBundleClassFilter abcf = this.get(i);
			if (abcf!=null) {
				classLocation = abcf.getClassLocation(className);
				if (classLocation!=null) break;
			}
		}
		
		// --- Plan B: If not found yet, try using direct bundle access -------
		if (classLocation==null) {
			Bundle[] bundles = this.bundleEvaluator.getBundles();
			for (int i = 0; i < bundles.length; i++) {
				Bundle bundle = bundles[i];
				try {
					Class<?> clazz = bundle.loadClass(className);
					if (clazz!=null) {
						classLocation = new ClassLocaton(className, bundle.getSymbolicName(), null);
						break;
					}
				} catch (ClassNotFoundException cnEx) {
					// cnEx.printStackTrace();
				}
			}
		}
		return classLocation;
	}
	
	
}
