package de.enflexit.common.bundleEvaluation;

import org.osgi.framework.Bundle;

/**
 * The Class BundleEvaluatorThread is used as search thread within the bundle evaluation.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BundleEvaluatorThread extends Thread {

	private static int THREAD_PRIORITY = Thread.MIN_PRIORITY;
	
	private Bundle bundle;
	private AbstractBundleClassFilter bundleClassFilter;
	
	/**
	 * Instantiates a new bundle evaluator thread.
	 */
	public BundleEvaluatorThread() {
		super();
	}
	/**
	 * Instantiates a new bundle evaluator thread.
	 * @param runnable the runnable
	 */
	public BundleEvaluatorThread(Runnable runnable) {
		super(runnable);
	}
	
	/**
	 * Instantiates a new bundle evaluator thread.
	 *
	 * @param bundleToSearchIn the bundle to search in
	 * @param bundleClassFilter the bundle class filter
	 */
	public BundleEvaluatorThread(Bundle bundleToSearchIn, AbstractBundleClassFilter bundleClassFilter) {
		super();
		if (bundleToSearchIn==null) {
			throw new NullPointerException("The bundle in which the search has to be executed is not allowed to null!");
		}
		this.setBundle(bundleToSearchIn);
		this.setBundleClassFilter(bundleClassFilter);
	}
	
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
		this.setName("BundleSearch_" + this.bundle.getSymbolicName());
	}
	
	public AbstractBundleClassFilter getBundleClassFilter() {
		return bundleClassFilter;
	}
	public void setBundleClassFilter(AbstractBundleClassFilter bundleClassFilter) {
		this.bundleClassFilter = bundleClassFilter;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		if (this.getBundle()==null) {
			System.err.println("[" + this.getClass().getName() + "] No bundle was specified for the evaluation of the bundle - terminate thread!");
			return;
		}
		
		try {
			// --- priority of the thread ---------------------------
			this.setPriority(THREAD_PRIORITY);

			// --- Execute the search -------------------------------
			BundleEvaluator.getInstance().evaluateBundle(this.getBundle(), this.getBundleClassFilter());
			
		} catch (Exception ex) {
			System.err.println(Thread.currentThread().getName() + ": " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			// --- Unregister as search thread ----------------------
			BundleEvaluator.getInstance().unregisterSearchThread(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null) return false;
		if (! (compareObject instanceof BundleEvaluatorThread)) return false;
		
		BundleEvaluatorThread  compareEvaluator = (BundleEvaluatorThread) compareObject;
		
		if (compareEvaluator.getBundle()==this.getBundle() || compareEvaluator.getBundle().getSymbolicName().equals(this.getBundle().getSymbolicName())==true) {
			// --- Identical or equal bundle --------------
			AbstractBundleClassFilter compareFilter = compareEvaluator.getBundleClassFilter();
			AbstractBundleClassFilter thisFilter = this.getBundleClassFilter();
			if (compareFilter==null & thisFilter==null) {
				return true;
			} else if ( (compareFilter==null & thisFilter!=null) || (compareFilter!=null & thisFilter==null) ) {
				// Nothing to do here 
			} else {
				if (compareFilter.getFilterScope().equals(thisFilter.getFilterScope())==true) {
					return true;
				}
			}
		}
		return false;
	}
	
	
}
