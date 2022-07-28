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
package de.enflexit.common.bundleEvaluation;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.State;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.UID;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

/**
 * The singleton class BundleEvaluator provides help methods to check bundle contents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BundleEvaluator {
	
	private boolean debug = false;
	private HashMap<Bundle, Boolean> debugDetailsInBundel;
	
	private HashSet<String> bundleExcludeHashSet;
	private EvaluationFilterResults evaluationFilterResults;

	private int maxSearchThreads = 5;
	private Vector<BundleEvaluatorThread> bundleEvaluatorThreadVectorWaiting;
	private Vector<BundleEvaluatorThread> bundleEvaluatorThreadVectorRunning;
	
	private HashMap<String, ClassRequest> classRequestHash;
	private long requestMaxStayTime = 1000 * 10; // 10 seconds
	
	private Cache cache;
	private Object cacheSynchronizerForInitialization = new Object();
	
	
	
	/** The singleton bundle evaluator instance. */
	private static BundleEvaluator bundleEvaluatorInstance;
	/** 
	 * Private, singleton constructor. 
	 */
	private BundleEvaluator() {
		this.getBundleExcludeHashSet();
	}
	/**
	 * Gets the single instance of BundleEvaluator.
	 * @return single instance of BundleEvaluator
	 */
	public static BundleEvaluator getInstance() {
		if (bundleEvaluatorInstance==null) {
			bundleEvaluatorInstance = new BundleEvaluator();
			bundleEvaluatorInstance.getCache();
		}
		return bundleEvaluatorInstance;
	}
	
	/**
	 * Returns the Cache that stores the evaluation results in a configuration file.
	 * @return the cache
	 */
	protected Cache getCache() {
		if (cache==null) {
			synchronized (cacheSynchronizerForInitialization) {
				if (cache==null) {
					cache = new Cache().load();
				}
			}
		}
		return cache;
	}
	
	/**
	 * Returns the description of excluded bundles as HashSet.
	 * @return the excluded bundle hash set
	 */
	private HashSet<String> getBundleExcludeHashSet() {
		if (bundleExcludeHashSet==null) {
			bundleExcludeHashSet = new HashSet<>();
			
			bundleExcludeHashSet.add("ch.qos.logback.core");
			bundleExcludeHashSet.add("ch.qos.logback.classic");
			bundleExcludeHashSet.add("ch.qos.logback.slf4j");
			
			bundleExcludeHashSet.add("com.ibm.icu");
			bundleExcludeHashSet.add("com.github.luben");
			bundleExcludeHashSet.add("com.github.oshi");
			
			bundleExcludeHashSet.add("com.google.gson");
			bundleExcludeHashSet.add("com.google.guava");
			bundleExcludeHashSet.add("com.google.protobuf");
			bundleExcludeHashSet.add("com.jcraft.jsch");
			bundleExcludeHashSet.add("com.mysql.cj");
			bundleExcludeHashSet.add("com.nimbusds");
			bundleExcludeHashSet.add("com.sun.activation");
			bundleExcludeHashSet.add("com.sun.jna");
			bundleExcludeHashSet.add("com.sun.xml");
			
			bundleExcludeHashSet.add("com.fasterxml");
			
			bundleExcludeHashSet.add("jakarta.activation-api");
			bundleExcludeHashSet.add("jakarta.annotation");
			bundleExcludeHashSet.add("jakarta.annotation-api");
			bundleExcludeHashSet.add("jakarta.inject");
			bundleExcludeHashSet.add("jakarta.servlet");
			bundleExcludeHashSet.add("jakarta.servlet-api");
			bundleExcludeHashSet.add("jakarta.validation");
			bundleExcludeHashSet.add("jakarta.ws.rs-api");
			bundleExcludeHashSet.add("jakarta.xml");
			
			bundleExcludeHashSet.add("javax.annotation");
			bundleExcludeHashSet.add("javax.inject");
			bundleExcludeHashSet.add("javax.servlet");
			bundleExcludeHashSet.add("javax.xml");
			
			bundleExcludeHashSet.add("lang-tag");
			bundleExcludeHashSet.add("javassist");
			bundleExcludeHashSet.add("joda-time");
			bundleExcludeHashSet.add("mariadb-java-client");
			bundleExcludeHashSet.add("net.minidev");
			bundleExcludeHashSet.add("oauth2-oidc-sdk");
			
			bundleExcludeHashSet.add("org.apache");
			bundleExcludeHashSet.add("org.bouncycastle");
			bundleExcludeHashSet.add("org.brotli.dec");
			bundleExcludeHashSet.add("org.cryptacular");
			bundleExcludeHashSet.add("org.eclipse");
			bundleExcludeHashSet.add("org.glassfish.jersey");
			bundleExcludeHashSet.add("org.glassfish.hk2");
			bundleExcludeHashSet.add("org.hamcrest.core");
			bundleExcludeHashSet.add("org.jsr-305");
			bundleExcludeHashSet.add("org.junit");
			bundleExcludeHashSet.add("org.jvnet");
			bundleExcludeHashSet.add("org.objectweb.asm");
			bundleExcludeHashSet.add("org.opentest4j");
			bundleExcludeHashSet.add("org.osgi");
			bundleExcludeHashSet.add("org.postgresql.jdbc");
			bundleExcludeHashSet.add("org.sat4j");
			bundleExcludeHashSet.add("org.slf4j.api");
			bundleExcludeHashSet.add("org.tukaani");
			bundleExcludeHashSet.add("org.w3c");
			bundleExcludeHashSet.add("org.yaml");
			
			bundleExcludeHashSet.add("slf4j.api");
			
			bundleExcludeHashSet.add("wrapped");
			
			bundleExcludeHashSet.add("de.enflexit.api");
			bundleExcludeHashSet.add("de.enflexit.awb.ws.swagger1x");
			bundleExcludeHashSet.add("de.enflexit.awb.ws.swagger2x");
			bundleExcludeHashSet.add("de.enflexit.common");
			bundleExcludeHashSet.add("de.enflexit.db.hibernate");
			bundleExcludeHashSet.add("de.enflexit.db.mariaDB");
			bundleExcludeHashSet.add("de.enflexit.db.mySQL");
			bundleExcludeHashSet.add("de.enflexit.db.postgres");
			bundleExcludeHashSet.add("de.enflexit.jaxb.impl.binding");
			bundleExcludeHashSet.add("de.enflexit.oidc");
			bundleExcludeHashSet.add("de.enflexit.oshi");
			
			bundleExcludeHashSet.add("org.agentgui.lib.jung");
			bundleExcludeHashSet.add("org.agentgui.lib.scimark");
			
		}
		return bundleExcludeHashSet;
	}
	/**
	 * Checks if the specified symbolic bundle name belongs to the list of excluded bundles.
	 *
	 * @param symbolicBundleName the symbolic bundle name
	 * @return true, if is excluded
	 */
	private boolean isExcludedBundle(String symbolicBundleName) {
	
		// --- Early exit, because of exclude bundle? -------------------------
		if (this.getBundleExcludeHashSet().contains(symbolicBundleName)==true) return true;
		
		String[] nameParts = symbolicBundleName.split("\\.");
		String name2Check = nameParts[0]; 
		for (int i=0; i<nameParts.length; i++) {
			// --- Check if the part is already part of the exclude list ------
			if (this.getBundleExcludeHashSet().contains(name2Check)==true) return true;
			if ((i+1)>=nameParts.length) break;
			name2Check += "." + nameParts[i+1];
		}
		return false;
	}
	
	// --------------------------------------------------------------
	// --- Methods to inform that bundles were added/removed --------
	// --------------------------------------------------------------
	/**
	 * Sets that the specified bundle was added.
	 * @param bundle the new bundle added
	 */
	public void setBundleAdded(Bundle bundle) {
		this.evaluateBundleInThread(bundle, null);
	}
	/**
	 * Sets that the specified bundle was removed.
	 * @param bundle the new bundle removed
	 */
	public void setBundleRemoved(Bundle bundle) {
		this.removeEvaluationFilterResults(bundle);
	}
	// --------------------------------------------------------------
	
	
	// --------------------------------------------------------------
	// --- Methods to provide access to the search results ---------- 
	// --------------------------------------------------------------
	/**
	 * Returns the filter and the filter results of the bundle evaluation.
	 * @return the bundle class filters
	 */
	private EvaluationFilterResults getEvaluationFilterResults() {
		if (evaluationFilterResults==null) {
			evaluationFilterResults = new EvaluationFilterResults(this);
		}
		return evaluationFilterResults;
	}
	/**
	 * Adds the specified bundle class filter and executes the needed, filter-specific search.
	 *
	 * @param bundleClassFilter the bundle class filter
	 * @return true, if the filter was added<br> false, if a filter with the same filter scope was already available. 
	 */
	public boolean addBundleClassFilter(AbstractBundleClassFilter bundleClassFilter) {
		return this.getEvaluationFilterResults().add(bundleClassFilter);
	}
	/**
	 * Adds the specified bundle class filter and executes the needed, filter-specific search.
	 *
	 * @param bundleClassFilter the bundle class filter
	 * @param doBundleEvaluation set true, if you want to directly evaluate all bundles with the current filter
	 * @return true, if the filter was added<br> false, if a filter with the same filter scope was already available.
	 */
	public boolean addBundleClassFilter(AbstractBundleClassFilter bundleClassFilter, boolean doBundleEvaluation) {
		return this.getEvaluationFilterResults().add(bundleClassFilter, doBundleEvaluation);
	}

	/**
	 * Removes the specified bundle class filter.
	 *
	 * @param bundleClassFilter the bundle class filter
	 * @return true, if successful
	 */
	public boolean removeBundleClassFilter(AbstractBundleClassFilter bundleClassFilter) {
		return this.getEvaluationFilterResults().remove(bundleClassFilter);
	}
	/**
	 * Returns the bundle class filter by the class to search for.
	 *
	 * @param classOfBundleFilter the class of bundle filter
	 * @return the bundle class filter by class
	 */
	public AbstractBundleClassFilter getBundleClassFilterByClass(Class<?> classOfBundleFilter) {
		return this.getEvaluationFilterResults().getBundleClassFilterBySearchClass(classOfBundleFilter);
	}
	
	/**
	 * Removes the evaluation filter results of the specified bundle.
	 * @param bundle the bundle from which the results have to be removed
	 */
	public void removeEvaluationFilterResults(Bundle bundle) {
		this.getEvaluationFilterResults().removeEvaluationFilterResults(bundle);
	}
	/**
	 * Returns the ClassLocaton for the specified class name.
	 * @param className the class name
	 * @return the class location
	 */
	public ClassLocaton getClassLocation(String className)  {
		return this.getEvaluationFilterResults().getClassLocation(className);
	}
	// --------------------------------------------------------------
	
	
	// --------------------------------------------------------------
	// --- Methods to declare the search process as busy ------------ 
	// --------------------------------------------------------------
	/**
	 * Adds the specified busy marker to all filter.
	 * @param busyMarker the busy marker
	 */
	public void addBusyMarkerToAllFilter(UID busyMarker) {
		synchronized (this.getEvaluationFilterResults()) {
			for (AbstractBundleClassFilter filter : this.getEvaluationFilterResults()) {
				filter.addBusyMarker(busyMarker);
			}
		}
	}
	/**
	 * Removes the specified busy marker from all filter.
	 * @param busyMarker the busy marker
	 */
	public void removeBusyMarkerFromAllFilter(UID busyMarker) {
		synchronized (this.getEvaluationFilterResults()) {
			for (AbstractBundleClassFilter filter : this.getEvaluationFilterResults()) {
				filter.removeBusyMarker(busyMarker);
			}
		}
	}
	// --------------------------------------------------------------
	
	
	// --------------------------------------------------------------
	// --- Methods to access the current BundleContext --------------
	// --------------------------------------------------------------
	/**
	 * Returns the current bundle context.
	 * @return the bundle context
	 */
	public BundleContext getBundleContext() {
		return FrameworkUtil.getBundle(BundleEvaluator.class).getBundleContext();
	}
	/**
	 * Returns all bundles that are currently loaded.
	 * @return the bundle array
	 */
	public Bundle[] getBundles() {
		BundleContext bc = this.getBundleContext(); 
		if (bc==null) return null;
		return bc.getBundles();
	}
	// --------------------------------------------------------------	
	
	
	// --------------------------------------------------------------
	// --- Methods to evaluate bundles, jars and other start here ---  
	// --------------------------------------------------------------
	/**
	 * Searches in all current bundles with all class filters defined in {@link #evaluationFilterResults}.
	 */
	public void evaluateAllBundles() {
		this.evaluateAllBundles(null);
	}
	/**
	 * Searches all current bundles for specific classes.
	 * If the the parameter <code>bundleClassFilterToUse</code> is specified, only this 
	 * filter will be used. If this parameter is <code>null</code>, all class filters
	 * defined in {@link #evaluationFilterResults} will be applied.
	 * 	
	 * @param bundleClassFilterToUse the bundle class filter to exclusively search with (<code>null</code> IS permitted).
	 */
	public void evaluateAllBundles(AbstractBundleClassFilter bundleClassFilterToUse) {
		Bundle[] bundleArray = this.getBundles();
		if (bundleArray!=null) {
			// --- Create a sorted list of the array ----------------
			List<Bundle> bundleList = Arrays.asList(bundleArray); 
			Collections.sort(bundleList, new Comparator<Bundle>() {
				@Override
				public int compare(Bundle b1, Bundle b2) {
					return b1.getSymbolicName().compareTo(b2.getSymbolicName());
				}
			});
			// --- Start evaluation for each bundle -----------------
			for (int i = 0; i < bundleList.size(); i++) {
				this.evaluateBundleInThread(bundleList.get(i), bundleClassFilterToUse);
			}
		}
	}

	/**
	 * Searches / filters in the specified bundle for the classes that are specified by the bundle class filter.
	 *
	 * @param bundle the bundle to search in (<code>null</code> is NOT permitted)
	 * @param bundleClassFilterToUse the bundle class filter to exclusively search with (<code>null</code> IS permitted).
	 * @see #getEvaluationFilterResults()
	 */
	public void evaluateBundleInThread(Bundle bundle, AbstractBundleClassFilter bundleClassFilterToUse) {

		if (bundle==null) return;
		boolean isExcludeBundleFromSearch = this.isExcludedBundle(bundle.getSymbolicName());
		boolean isNoBundleClassFilterAndNoFilterResultDefined = (bundleClassFilterToUse==null && this.getEvaluationFilterResults().size()==0);

		// ----------------------------------------------------------
		// --- Introduction debug area ------------------------------
		// ----------------------------------------------------------
		boolean debugShowBundleSelection = false;
		boolean debugShowBundlesIncluded = true;
		if (this.debug==true && debugShowBundleSelection==true) {
			if (debugShowBundlesIncluded==true) {
				if (isExcludeBundleFromSearch==false) System.out.println("Included bundle " + bundle.getSymbolicName() + " - isNoBundleClassFilterAndNoFilterResultDefined=" + isNoBundleClassFilterAndNoFilterResultDefined);
			} else {
				if (isExcludeBundleFromSearch==true ) System.out.println("Excluded bundle " + bundle.getSymbolicName()  + " - isNoBundleClassFilterAndNoFilterResultDefined=" + isNoBundleClassFilterAndNoFilterResultDefined);
			}
		}
		// ----------------------------------------------------------
		
		
		// --- Check for exit search for bundle ---------------------
		if (isExcludeBundleFromSearch==true) return;
		if (isNoBundleClassFilterAndNoFilterResultDefined==true) return;
		
		// --- Evaluate bundle details ------------------------------
		String classFilterDescription = "Null";
		if (bundleClassFilterToUse!=null) {
			classFilterDescription = bundleClassFilterToUse.getFilterScope(); 
		}
		
		// ----------------------------------------------------------
		// --- Debug area for the search thread start ---------------
		// ----------------------------------------------------------
		boolean debugThreadStart = false;
		if (this.debug==true && debugThreadStart==true) {
			String lastModifiedDisplay = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(bundle.getLastModified()));
			System.out.println("Start evaluation Thread for: " + bundle.getSymbolicName() + " (Vers. " + bundle.getVersion().toString() + " / " + lastModifiedDisplay + ") - filter scope: " + classFilterDescription + "");
		}
		// ----------------------------------------------------------
		
		
		// --- Start the evaluation of the bundle -------------------
		BundleEvaluatorThread searchThread = new BundleEvaluatorThread(bundle, bundleClassFilterToUse);
		// --- If not already available register and start search ---
		if (this.registerSearchThread(searchThread)==true) {
			this.executeWaitingSearchThreads();
		} else {
			if (this.debug==true) {
				System.err.println("Search was already active for '" + bundle.getSymbolicName() + " - filter scope: " + classFilterDescription);
			}
		}
	}
	/**
	 * Returns the vector of bundle evaluator threads waiting for execution.
	 * @return the vector of bundle evaluator threads
	 */
	private Vector<BundleEvaluatorThread> getVectorOfBundleEvaluatorThreadsWaiting() {
		if (bundleEvaluatorThreadVectorWaiting==null) {
			bundleEvaluatorThreadVectorWaiting = new Vector<>();
		}
		return bundleEvaluatorThreadVectorWaiting;
	}
	/**
	 * Returns the vector of bundle evaluator threads that are currently running.
	 * @return the vector of bundle evaluator threads
	 */
	private Vector<BundleEvaluatorThread> getVectorOfBundleEvaluatorThreadsRunning() {
		if (bundleEvaluatorThreadVectorRunning==null) {
			bundleEvaluatorThreadVectorRunning= new Vector<>();
		}
		return bundleEvaluatorThreadVectorRunning;
	}
	
	/**
	 * Registers the specified BundleEvaluatorThread.
	 *
	 * @param bundleEvaluatorThread the bundle evaluator thread
	 * @return true, if successful
	 */
	public boolean registerSearchThread(BundleEvaluatorThread bundleEvaluatorThread) {
		if (this.getVectorOfBundleEvaluatorThreadsWaiting().contains(bundleEvaluatorThread)==false) {
			this.getVectorOfBundleEvaluatorThreadsWaiting().addElement(bundleEvaluatorThread);
			return true;
		}
		return false;
	}
	/**
	 * Unregister the specified BundleEvaluatorThread.
	 * @param bundleEvaluatorThread the bundle evaluator thread
	 */
	public void unregisterSearchThread(BundleEvaluatorThread bundleEvaluatorThread) {
		this.getVectorOfBundleEvaluatorThreadsWaiting().remove(bundleEvaluatorThread);
		this.getVectorOfBundleEvaluatorThreadsRunning().remove(bundleEvaluatorThread);
		this.executeWaitingSearchThreads();
	}
	
	/**
	 * Execute waiting search threads.
	 */
	private synchronized void executeWaitingSearchThreads() {
		
		// --- If no further thread is waiting for execution, save cache ------
		if (this.getVectorOfBundleEvaluatorThreadsWaiting().size()==0) {
			this.getCache().save();
		}
		
		// --- Check how many thread are currently running --------------------
		int noOfRunningThreads = this.getVectorOfBundleEvaluatorThreadsRunning().size();

		boolean isShowNoOfRunningThreads = false;
		if (this.debug==true && isShowNoOfRunningThreads==true) System.out.println("# Number of executed search threads: " + noOfRunningThreads);
		if (noOfRunningThreads>=this.maxSearchThreads) return;
		
		// --- Execute one of the waiting threads -----------------------------
		for (int i=0; i < this.getVectorOfBundleEvaluatorThreadsWaiting().size(); i++) {
			BundleEvaluatorThread searchThread = this.getVectorOfBundleEvaluatorThreadsWaiting().get(i);
			synchronized (searchThread) {
				if (searchThread.getState()==State.NEW) {
					try {
						this.getVectorOfBundleEvaluatorThreadsRunning().add(searchThread);
						searchThread.start();
					} catch (Exception ex) {
						System.err.println("[" + this.getClass().getSimpleName() + "] Error with BundleEvaluatorThread '" + searchThread.getName() + "', State: " + searchThread.getState() + ":");
						ex.printStackTrace();
					}
				}
			}
			if (this.getVectorOfBundleEvaluatorThreadsRunning().size()>=this.maxSearchThreads) return;
		}
	}
	
	
	/**
	 * Gets the debug detail.
	 * @return the debug detail
	 */
	private HashMap<Bundle, Boolean> getDebugDetailsInBundle() {
		if (debugDetailsInBundel==null) {
			debugDetailsInBundel = new HashMap<>();
		}
		return debugDetailsInBundel;
	}
	
	/**
	 * Searches / filters in the specified bundle for specific classes.<br> 
	 * If the the parameter <code>bundleClassFilterToUse</code> is specified, only this 
	 * filter will be used. If this parameter is <code>null</code>, all class filters
	 * defined in {@link #evaluationFilterResults} will be applied.	
	 *
	 * @param bundle the bundle to search in (<code>null</code> is NOT permitted)
	 * @param bundleClassFilterToUse the bundle class filter to exclusively search with (<code>null</code> IS permitted).
	 * @see #getEvaluationFilterResults()
	 */
	public void evaluateBundle(Bundle bundle, AbstractBundleClassFilter bundleClassFilterToUse) {

		if (bundle==null) return;
		if (this.isExcludedBundle(bundle.getSymbolicName())==true) return;
		if (bundleClassFilterToUse==null && this.getEvaluationFilterResults().size()==0) return;
		
		// --- Remind start time of the search ----------------------
		long starTime = System.nanoTime(); 

		// ----------------------------------------------------------
		// --- Debug area for the search start ----------------------
		// ----------------------------------------------------------
		boolean debugSearchStart = false;
		if (this.debug==true && debugSearchStart==true) {
			String sbn = bundle.getSymbolicName();
			String classFilterDescription = "";
			if (bundleClassFilterToUse!=null) {
				classFilterDescription = bundleClassFilterToUse.getFilterScope(); 
			}
			System.out.println("Evaluate bundle " + sbn + " - filter scope: " + classFilterDescription);
			
			boolean debugDetails = false;
			if (debugDetails==true && sbn.equals("org.agentgui.lib.jade")==true) {
				this.getDebugDetailsInBundle().put(bundle, true);
				System.out.println("Debug stop here - found '" + sbn + "'");
			}
		}
		// ----------------------------------------------------------

		
		// ----------------------------------------------------------
		// --- Set a busy marker for the used bundle filter ---------
		// ----------------------------------------------------------
		UID busyMarker = new UID();
		if (bundleClassFilterToUse==null) {
			this.addBusyMarkerToAllFilter(busyMarker);
		} else {
			bundleClassFilterToUse.addBusyMarker(busyMarker);
		}
		// --- Define the filter to apply in this search ------------
		Vector<AbstractBundleClassFilter> filterToApply = this.getEvaluationFilterResults();
		if (bundleClassFilterToUse!=null) {
			filterToApply = new Vector<>();
			filterToApply.add(bundleClassFilterToUse);
		}
		
		// ----------------------------------------------------------
		// --- Check if the cache does know the results already -----
		// ----------------------------------------------------------
		CacheBundleResult cachedBundleResult = this.getCache().getOrReCreateBundleResult(bundle);
		filterToApply = cachedBundleResult.updateBundleClassFilterFromCache(filterToApply);
		
		// ----------------------------------------------------------
		// --- Get classes and execute the filter checks ------------
		// ----------------------------------------------------------
		try {
			
			if (filterToApply!=null && filterToApply.size()>0) {
				// --- Get all classes from the bundle --------------
				List<Class<?>> classes = this.getClasses(bundle);
				for (int i = 0; i < classes.size(); i++) {
					// --- Check single class -----------------------
					Class<?> clazz = classes.get(i);
					if (this.getSourceBundleOfClass(clazz)!=bundle) continue;
					
					// --- Use all defined filter -------------------
					for (int j = 0; j < filterToApply.size(); j++) {
						AbstractBundleClassFilter classFilter = filterToApply.get(j);
						cachedBundleResult.addClassFilterResult(classFilter.getFilterScope(), null);
						if (classFilter.isInFilterScope(clazz)==true) {
							// --- Write to results -----------------
							classFilter.addClassFound(clazz.getName(), bundle.getSymbolicName());
							// --- Write to cache -------------------
							cachedBundleResult.addClassFilterResult(classFilter.getFilterScope(), clazz.getName());
						}
					}
				}
				// --- Save the current state of the cache ----------
				this.getCache().save();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// ------------------------------------------------------
			// --- Remove busy marker for the used bundle filter ---- 
			// ------------------------------------------------------
			if (bundleClassFilterToUse==null) {
				this.removeBusyMarkerFromAllFilter(busyMarker);
			} else {
				bundleClassFilterToUse.removeBusyMarker(busyMarker);
			}
			// ------------------------------------------------------
			
		}

		if (this.debug==true) {
			long timeProceedingMilis = TimeUnit.MILLISECONDS.convert((System.nanoTime() - starTime), TimeUnit.NANOSECONDS);
			System.out.println("Bundle evaluation for '" + bundle.getSymbolicName() + "' required " + timeProceedingMilis + " ms (" + (Math.round(timeProceedingMilis / 100.0)/10.0) + " s)");
		}
	}
	
	/**
	 * Returns the class references of the specified bundle (maybe quite time consuming).
	 * @param bundle the bundle to evaluate
	 * @return the list of classes
	 */
	public List<Class<?>> getClasses(Bundle bundle) {
		return this.getClasses(bundle, null);
	}
	/**
	 * Returns the class references of the specified bundle (may be quite time consuming, especially without package filter).
	 *
	 * @param bundle the bundle to evaluate
	 * @param packageFilter the package filter; maybe <code>null</code>, which will return all classes from the bundle
	 * @return the list of classes
	 */
	public List<Class<?>> getClasses(Bundle bundle, String packageFilter) {
		return getClasses(bundle, packageFilter, BundleWiring.LISTRESOURCES_RECURSE);
	}
	/**
	 * Returns the class references of the specified bundle (may be quite time consuming, especially without package filter).
	 *
	 * @param bundle the bundle to evaluate
	 * @param packageFilter the package filter; maybe <code>null</code>, which will return all classes from the bundle
	 * @param options the options according to the {@link BundleWiring} that are {@link BundleWiring#LISTRESOURCES_RECURSE}, {@link BundleWiring#LISTRESOURCES_LOCAL} or {@link BundleWiring#FINDENTRIES_RECURSE}
	 * @return the list of classes
	 */
	public List<Class<?>> getClasses(Bundle bundle, String packageFilter, int options) {

		// --- Define the result list -------------------------------
		List<Class<?>> bundleClasses = new ArrayList<Class<?>>();
		if (bundle==null) return bundleClasses;
		
		// ----------------------------------------------------------
		// --- Define request reminder ------------------------------
		// ----------------------------------------------------------
		String classRequestID = bundle.getSymbolicName() + "@" + packageFilter + "@" + options;
		ClassRequest classRequest = this.getOrAddNewClassRequest(classRequestID);
		if (classRequest.getResult()!=null) {
			// --- Just return the result already there -------------
			return classRequest.getResult();
		}
		// ----------------------------------------------------------

		
		// --- Adjust the package filter ----------------------------
		String packagePath = "/";
		if (packageFilter!=null) {
			packagePath = packageFilter.replace(".", "/");
			if (packagePath.startsWith("/")==false) packagePath = "/" + packagePath;
			if (packagePath.endsWith("/")  ==false) packagePath = packagePath + "/";
		}
		
		// --- Checking class files ---------------------------------
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		if (bundleWiring!=null && this.isBundleStartingResolvedOrActive(bundle)==true) {
			
			try {
				// --- Try to read the resources --------------------
				Vector<String> resources = new Vector<>(bundleWiring.listResources(packagePath, "*.class", options));
				// --- get the classes found ------------------------
				for (int i=0; i < resources.size(); i++) {
					
					// --- Get a suitable class name ----------------
					String resource = resources.get(i);
					String className = this.getClassName(resource);
					if (className!=null) {
						
						try {
							// ---- Load the class into the bundle --
							Class<?> clazz = bundle.loadClass(className);
							if (this.getSourceBundleOfClass(clazz)==bundle) {
								bundleClasses.add(clazz);
							}
							
						} catch (ClassNotFoundException | NoClassDefFoundError | IllegalStateException | IllegalAccessError | UnsupportedClassVersionError cnfEx) {
							//ex.printStackTrace();
						}
					}
				}

			} catch (Exception ex) {
				if (!(ex instanceof NullPointerException)) {
					ex.printStackTrace();
				}
			}
			
			// --- If bundle is packed in a jar, we're done ---------
			if (this.isJarBundle(bundle)==false) {
				// ------------------------------------------------------
				// --- If the bundle wiring worked, check for jars ------ 
				// ------------------------------------------------------			
				// --- Does the bundle has a "Bundle-ClassPath" entry ---
				Vector<String> bundleClassPathEntries  = this.getBundleClassPathEntries(bundle);
				if (this.isBundleStartingResolvedOrActive(bundle)==true && bundleClassPathEntries!=null && bundleClassPathEntries.size()>0) {
					// --- Check for available jars in the bundle--------
					Enumeration<URL> bundleJars = bundle.findEntries("", "*.jar", true);
					if (bundleJars!=null) {
						while (this.isBundleStartingResolvedOrActive(bundle)==true && bundleJars.hasMoreElements()) {
							URL url = (URL) bundleJars.nextElement();
							String debugText = "Found jar-File of '" + bundle.getSymbolicName() + "' (" + url.getPath() + ")";
							if (bundleClassPathEntries.contains(url.getPath())) {
								debugText += "\t=> Will be checked ... ";
								List<Class<?>> jarClasses = this.getJarClasses(bundle, url);
								if (packageFilter==null) {
									// --- Add all classes found  -------
									bundleClasses.addAll(jarClasses);
								} else {
									// --- Check if the in package ------
									for (int i = 0; i < jarClasses.size(); i++) {
										Class<?> classFound = jarClasses.get(i);
										String className = classFound.getName();
										if (className.startsWith(packageFilter)) {
											bundleClasses.add(classFound);		
										}
									}
								}
								debugText += "Done!";
							}
							if (this.getDebugDetailsInBundle().get(bundle)!=null && this.getDebugDetailsInBundle().get(bundle)==true) {
								System.out.println(debugText); 
							}
							
						}
					}	
				}
			}
		}
		
		// ----------------------------------------------------------
		// --- Remind the result of the search ----------------------
		// ----------------------------------------------------------
		classRequest.setResult(bundleClasses);
		classRequest.setInProgress(false);
		this.cleanClassRequests();
		// ----------------------------------------------------------
		
		return bundleClasses;
	}
	/**
	 * Returns the bundle class path entries conform to the URL pattern (starting with a slash).
	 * @param bundle the bundle
	 * @return the bundle class path entries
	 */
	private Vector<String> getBundleClassPathEntries(Bundle bundle) {
		
		String bundleClassPath = bundle.getHeaders().get(Constants.BUNDLE_CLASSPATH);
		if (bundleClassPath==null) return null;
		
		Vector<String> bundleClassPathEntries = new Vector<>(Arrays.asList(bundleClassPath .split(",")));
		for (int i = 0; i < bundleClassPathEntries.size(); i++) {
			String bundleClassPathEntry = bundleClassPathEntries.get(i);
			if (bundleClassPathEntry.startsWith("/")==false) {
				bundleClassPathEntries.set(i, "/" + bundleClassPathEntry);
			}
		}
		return bundleClassPathEntries;
	}

	/**
	 * Checks if the specified bundle is resolved or active.
	 *
	 * @param bundle the bundle
	 * @return true, if the bundle is resolved or active
	 */
	private boolean isBundleStartingResolvedOrActive(Bundle bundle) {
		return (bundle.getState()==Bundle.STARTING || bundle.getState()==Bundle.RESOLVED || bundle.getState()==Bundle.ACTIVE); 
	}
	
	/**
	 * Returns the class name of a bundle resource returned by the bundle wiring.
	 * @param resource the resource string
	 * @return the class name of bundle resource
	 */
	private String getClassName(String resource) {
		
		if (resource.contains("$")) return null;
		
		String className = resource.replaceAll("/", ".");
        // --- For the development environment --------------
        if (className.startsWith("bin.")==true) {
        	className = className.substring(4, className.length());
        }
        // --- Cut file extension ---------------------------
        if (className.endsWith(".class")==true) {
        	className = className.substring(0, className.length()-6);
        }
		return className;
	}
	
	/**
	 * Returns the class references of the specified bundle (maybe quite time consuming).
	 * @param bundle the bundle
	 * @return the list of class references
	 */
	public List<String> getClassReferences(Bundle bundle) {
		return this.getClassReferences(bundle, null);
	}
	/**
	 * Returns the class references of the specified bundle (maybe quite time consuming, especially without package filter).
	 * @param bundle the bundle
	 * @param packageFilter the package filter; maybe <code>null</code>, which will return all classes from the bundle
	 * @return the list of class references
	 */
	public List<String> getClassReferences(Bundle bundle, String packageFilter) {
		
		List<String> classReferencesFound = new ArrayList<String>();
		List<Class<?>> classesFound = this.getClasses(bundle, packageFilter);
		for (int i = 0; i < classesFound.size(); i++) {
			Class<?> classFound = classesFound.get(i);
			if (classesFound!=null) {
				classReferencesFound.add(classFound.getName());
			}
		}
		return classReferencesFound;
	}
	/**
	 * Returns the package names of the specified bundle.
	 * @param bundle the bundle
	 * @return the packages
	 */
	public List<String> getPackages(Bundle bundle) {
		return this.getPackages(this.getClasses(bundle));
	}
	/**
	 * Return the packages from the specified class list.
	 *
	 * @param classList the class list
	 * @return the packages
	 */
	public List<String> getPackages(List<Class<?>> classList) {
		List<String> packagesFound = new ArrayList<String>();
		for (int i = 0; i < classList.size(); i++) {
			Class<?> classFound = classList.get(i);
			if (classFound!=null) {
				if (classFound.getPackage()!=null) {
					String packageName = classFound.getPackage().getName();
					if (packagesFound.contains(packageName)==false) {
						packagesFound.add(packageName);
					}	
				}
			}
		}
		return packagesFound;
	}
	/**
	 * Return the packages from the specified class name list.
	 *
	 * @param classNameList the list of the class names
	 * @return the packages
	 */
	public List<String> getPackagesOfClassNames(List<String> classNameList) {
		List<String> packagesFound = new ArrayList<String>();
		for (int i = 0; i < classNameList.size(); i++) {
			String classNameFound = classNameList.get(i);
			if (classNameList!=null) {
				int dotIndex = classNameFound.lastIndexOf(".");
				if (dotIndex>-1) {
					String packageName = classNameFound.substring(0, dotIndex);
					if (packagesFound.contains(packageName)==false) {
						packagesFound.add(packageName);
					}	

				}
				
			}
		}
		return packagesFound;
	}
	/**
	 * Loads and return the classes of a jar file that contains the specified class.
	 *
	 * @param bundle the bundle
	 * @param classInstance the instance
	 * @return the jar classes by object instance
	 */
	public List<Class<?>> getJarClassesByClassInstance(Bundle bundle, Class<?> classInstance) {
		
		if (classInstance==null) return null;
		if (bundle==null) return null;
		
		List<Class<?>> classesFound = new ArrayList<Class<?>>();
		
		// --- Get jar file from class instance ---------------------
		File jarFile = new File(classInstance.getProtectionDomain().getCodeSource().getLocation().getPath());
		if (jarFile.getAbsolutePath().contains("%20")==true) {
			try {
				jarFile = new File(classInstance.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			} catch (URISyntaxException uriEx) {
				uriEx.printStackTrace();
			}
		}
		
		// --- Try to load classes into the current bundle ----------
		if (jarFile.exists()==true) {
			List<Class<?>> jadeJarClasses = this.getJarClasses(bundle, jarFile);
			if (jadeJarClasses!=null && jadeJarClasses.size()>0) {
				classesFound.addAll(jadeJarClasses);
			}
		}
		return classesFound;
	}

	
	/**
	 * Loads and returns the classes from a jar file into the specified bundle.
	 *
	 * @param bundle the bundle
	 * @param file the file
	 * @return the jar classes
	 */
	public List<Class<?>> getJarClasses(Bundle bundle, File file) {
		
		if (file.exists()==false) return null; 

		// --- Define the URL of the file -----------------
		URL jarFileURL = this.getUrlFromFile(file);
		if (jarFileURL!=null) {
			// --- Return the regulars method results -----
			return this.getJarClasses(bundle, jarFileURL);
		}
		// --- Return empty list in case of an error ------
		return new ArrayList<>();
	}
	
	/**
	 * Loads and returns the classes from a jar file into the specified bundle.
	 *
	 * @param bundle the bundle
	 * @param jarFileURL the jar file URL
	 * @return the jar classes
	 */
	public List<Class<?>> getJarClasses(Bundle bundle, URL jarFileURL) {
		List<Class<?>> classesOfJarFile = new ArrayList<Class<?>>();
		if (bundle!=null) {
			List<String> classNames = this.getJarClassReferences(bundle, jarFileURL);
			for (int i = 0; i < classNames.size(); i++) {
				String className = classNames.get(i);
				try {
					Class<?> classFound = bundle.loadClass(className);
					classesOfJarFile.add(classFound);
				} catch (ClassNotFoundException | NoClassDefFoundError cnfEx) {
					// No print of the stack trace here
					// cnfEx.printStackTrace();
				}
			}
		}
		
		return classesOfJarFile;
	}
	/**
	 * Loads and returns the class references from a jar file into the specified bundle.
	 *
	 * @param bundle the bundle
	 * @param file the file
	 * @return the jar classes
	 */
	public List<String> getJarClassReferences(Bundle bundle, File file) {
		
		if (file.exists()==false) return null;
		
		// --- Define the URL of the file -----------------
		URL jarFileURL = this.getUrlFromFile(file);
		if (jarFileURL!=null) {
			// --- Return the regulars method results -----
			return this.getJarClassReferences(bundle, jarFileURL);
		}
		// --- Return empty list in case of an error ------
		return new ArrayList<>();
	}
	
	/**
	 * Loads and returns the class references from a jar file into the specified bundle.
	 *
	 * @param bundle the bundle
	 * @param jarFileURL the jar file URL
	 * @return the jar classes
	 */
	public List<String> getJarClassReferences(Bundle bundle, URL jarFileURL) {

		boolean debug = false;
		
		// --- Define the result list -------------------------------
		List<String> classesOfJarFile = new ArrayList<String>();
		
		// ----------------------------------------------------------
		// --- Establish connection to jar file --------------------- 
		// ----------------------------------------------------------
		JarFile jarFile = null;
		try {
			// --- Get the file description of the URL --------------
			String jarFilePath = this.getFilePathFromURL(jarFileURL);
			if (debug) System.out.println("=> Check: " + jarFilePath);
			
			// --- Check, if this file is available -----------------
			File checkFile = new File(jarFilePath);
			if (checkFile.exists()==false && bundle!=null) {
				// --- Rebuild URL with the bundle location ---------
				String bundleDirectory = this.getBundleDirectory(bundle);
				jarFilePath = bundleDirectory + jarFilePath;
				jarFilePath = jarFilePath.replaceAll("//", "/");
				// --- Create new URL -------------------------------
				jarFileURL = new URL("jar:file:" + jarFilePath + "!/");
				if (debug) System.out.println("=> ...corrected to: " + jarFileURL);
			}
			
			// --- Establish connection to the JarFile -------------- 
			URLConnection urlConnection = jarFileURL.openConnection();
			JarURLConnection conn = (JarURLConnection) urlConnection;
			jarFile = conn.getJarFile();

		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} catch (ClassCastException ccEx) {
			ccEx.printStackTrace();
		}
		
		// ----------------------------------------------------------
		// --- Read classes from jar file ---------------------------
		// ----------------------------------------------------------
		if (jarFile!=null && jarFileURL!=null) {
			try {
				// --- Check the jar file entries -------------------
				Enumeration<JarEntry> jarFileEntries = jarFile.entries();
				while (jarFileEntries.hasMoreElements()) {
					
					JarEntry entry = (JarEntry)jarFileEntries.nextElement();
					String className = entry.getName();
					if (entry.isDirectory()==false && className.endsWith(".class") && className.contains("$")==false) {
						// --- Correct the class name ---------------
						className = className.substring(0, className.length()-6);
						if (className.startsWith("/")) {
							className = className.substring(1);
						}
						className = className.replace('/', '.');
						classesOfJarFile.add(className);
					}
				} // end while
				
			} catch (Exception ex) {
				System.err.println(Thread.currentThread().getName() + ": Error while evaluating jar file entries.");
				ex.printStackTrace();
				
			} finally {
				if (jarFile!=null) {
					// --- Close the jar file -----------------------
					try {
						jarFile.close();
					} catch (IOException ioEx) {
						ioEx.printStackTrace();
					}
				}
			}
		
		}
		return classesOfJarFile;
	}
	/**
	 * Returns the URL from the specified file.
	 *
	 * @param file the file
	 * @return the URL from file
	 */
	private URL getUrlFromFile(File file) {
		
		try {
			// --- Define the URL of the file -------------
			String canonicalPath = file.getCanonicalPath();
			if (canonicalPath.startsWith("/")==false) {
				canonicalPath = "/"+canonicalPath;
			}
			URL jarFileURL = new URL("file:" + canonicalPath);
			return jarFileURL = new URL("jar:" + jarFileURL.toExternalForm() + "!/");
			
		} catch (MalformedURLException mUrlEx) {
			mUrlEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the file path from the URL.
	 * @param url the URL
	 * @return the file path from URL
	 */
	public String getFilePathFromURL(URL url) {
		
		if (url==null) return null;

		String jarFilePath = url.getFile();
		jarFilePath = jarFilePath.replaceAll("file:", "");
		jarFilePath = jarFilePath.replaceAll("!/", "");
		return jarFilePath;
	}
	
	
	/**
	 * Returns the bundle URL resolved by the {@link FileLocator}.
	 * @param bundle the bundle
	 * @return the bundle URL
	 */
	public URL getBundleURL(Bundle bundle) {
		URL bundleURL = null;
		try {
			if (this.isBundleStartingResolvedOrActive(bundle)==true) {
				bundleURL = FileLocator.resolve(bundle.getEntry("/"));
			}
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return bundleURL;
	}
	/**
	 * Checks if the specified {@link Bundle} is packed in a jar.
	 * @param bundle the bundle
	 * @return true, if is jar bundle
	 */
	public boolean isJarBundle(Bundle bundle) {
		
		URL pluginURL = this.getBundleURL(bundle);
		if (pluginURL==null) return false;
		
		String filePath = this.getFilePathFromURL(pluginURL);
		File pluginfile = new File(filePath);
		if (pluginfile.exists()) {
			if (pluginfile.isDirectory()) {
				return false;
			} else {
				return true;
			}
		}
		return pluginURL.getFile().endsWith(".jar");
	}
	
	/**
	 * Return the bundle directory.
	 *
	 * @param bundle the bundle
	 * @return the bundle directory
	 */
	public String getBundleDirectory(Bundle bundle) {
		
		if (bundle == null) return null;

		// --- Get File URL of bundle --------------------- 
		URL pluginURL = null;
		try {
			pluginURL = FileLocator.resolve(bundle.getEntry("/"));
		} catch (IOException e) {
			throw new RuntimeException("Could not get installation directory of the plugin: " + bundle.getSymbolicName());
		}
		
		// --- Clean up the directory path ----------------
		String pluginInstallDir = pluginURL.getFile().trim();
		if (pluginInstallDir.length()==0) {
			throw new RuntimeException("Could not get installation directory of the plugin: " + bundle.getSymbolicName());
		}
		return pluginInstallDir;
	}
	
	/**
	 * Returns the source bundle that loaded the specified class by using <code>FrameworkUtil.getBundle(clazz);</code>.
	 * @param clazz the class instance 
	 * @return the bundle from class
	 */
	public Bundle getSourceBundleOfClass(Class<?> clazz) {
		return FrameworkUtil.getBundle(clazz);
	}
	
	/**
	 * Tries to find the specified class in the bundle. If the class could not be found, the method returns <code>null</code>.
	 *
	 * @param bundle the bundle in which the class should be located
	 * @param className the class name
	 * @return the class or <code>null</code>
	 */
	public Class<?> findClass(Bundle bundle, String className) {
	    if (bundle!=null) {
    		try {
    			Class<?> c = bundle.loadClass(className);
    			return c;
    		} catch (ClassNotFoundException e) {
    			//cnfEx.printStackTrace();
				if (this.debug==true) {
					System.err.println(this.getClass().getSimpleName() + "#findClass(bundle, className) Could not find class '" + className + "'");
				}
    		}
	    }
	    return null;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, methods for storing class search results can be found --- 
	// ------------------------------------------------------------------------
	/**
	 * Returns the class request HashMap.
	 * @return the class request HashMap
	 */
	private HashMap<String, ClassRequest> getClassRequestHash() {
		if (classRequestHash==null) {
			classRequestHash = new HashMap<>();
		}
		return classRequestHash;
	}
	/**
	 * Returns or creates a new {@link ClassRequest} for the specified classRequestID.
	 *
	 * @param classRequestID the class request ID
	 * @return the class request
	 */
	private ClassRequest getOrAddNewClassRequest(String classRequestID) {

		// --- Get request from the stored requests -----------------
		ClassRequest crFound = this.getClassRequestHash().get(classRequestID);
		if (crFound==null) {
			// --- Create a new class request -----------------------
			crFound = new ClassRequest(classRequestID, System.currentTimeMillis(), true);
			this.getClassRequestHash().put(classRequestID, crFound);
			
		} else {
			// --- Check the class request --------------------------
			while (crFound.isInProgress()) {
				try {
					Thread.sleep(30);
				} catch (InterruptedException iEx) {
					iEx.printStackTrace();
				}
			}
			// --- Not busy (anymore) -------------------------------
			
			// --- Check if the request is too old ------------------
			if (System.currentTimeMillis()>=crFound.getRequestTime() + this.requestMaxStayTime) {
				// --- Overwrite old request ------------------------
				crFound = new ClassRequest(classRequestID, System.currentTimeMillis(), true);
				this.getClassRequestHash().put(classRequestID, crFound);
			}
			
		}
		return crFound;
	}
	/**
	 * Cleans the reminded {@link ClassRequest}s.
	 */
	private void cleanClassRequests() {
		if (this.getClassRequestHash().size()<=0) return;
		synchronized (this.getClassRequestHash()) {
			Vector<ClassRequest> classRequests = new Vector<>(this.getClassRequestHash().values());
			for (int i = 0; i < classRequests.size(); i++) {
				ClassRequest classRequest = classRequests.get(i);
				if (System.currentTimeMillis()>=classRequest.getRequestTime() + this.requestMaxStayTime) {
					this.getClassRequestHash().remove(classRequest.getRequestID());
				}
			}
		}
	}
	
	/**
	 * The Class ClassRequest stores information about a request about classes within a bundle.
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
	 */
	private class ClassRequest {
		
		private String requestID;
		private long requestTime;
		private Boolean inProgress;
		private List<Class<?>> result;

		/**
		 * Instantiates a new class request.
		 *
		 * @param requestID the request ID
		 * @param requestTime the request time
		 * @param isBusy the is busy
		 */
		public ClassRequest(String requestID, long requestTime, boolean isBusy) {
			this.setRequestID(requestID);
			this.setRequestTime(requestTime);
			this.setInProgress(isBusy);
		}
		
		public String getRequestID() {
			return requestID;
		}
		public void setRequestID(String requestID) {
			this.requestID = requestID;
		}
		
		public long getRequestTime() {
			return requestTime;
		}
		public void setRequestTime(long requestTime) {
			this.requestTime = requestTime;
		}
		
		public Boolean isInProgress() {
			return inProgress;
		}
		public void setInProgress(boolean inProgress) {
			this.inProgress = inProgress;
		}
		
		public List<Class<?>> getResult() {
			return result;
		}
		public void setResult(List<Class<?>> result) {
			this.result = result;
		}
	}
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------


	 /**
	 * Returns the specified bundle event as string.
	 * 
	 * @param event the bundle event
	 * @return the bundle event as string
	 */
	public static String getBundleEventAsString(BundleEvent event) {
		
		if (event == null) return "null";
		
		int type = event.getType();
		switch (type) {
		case BundleEvent.INSTALLED:
			return "INSTALLED";
		case BundleEvent.LAZY_ACTIVATION:
			return "LAZY_ACTIVATION";
		case BundleEvent.RESOLVED:
			return "RESOLVED";
		case BundleEvent.STARTED:
			return "STARTED";
		case BundleEvent.STARTING:
			return "STARTING";
		case BundleEvent.STOPPED:
			return "STOPPED";
		case BundleEvent.UNINSTALLED:
			return "UNINSTALLED";
		case BundleEvent.UNRESOLVED:
			return "UNRESOLVED";
		case BundleEvent.UPDATED:
			return "UPDATED";
		default:
			return "unknown event type: " + type;
		}
	} 
	
}
