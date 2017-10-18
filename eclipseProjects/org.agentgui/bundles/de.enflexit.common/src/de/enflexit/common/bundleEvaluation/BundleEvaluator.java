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
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
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
	
	private HashSet<String> bundleExcludeHashSet;
	private EvaluationFilterResults evaluationFilterResults;

	private HashMap<String, ClassRequest> classRequestHash;
	private long requestMaxStayTime = 1000 * 10; // 10 seconds
	
	
	/** The singleton bundle evaluator instance. */
	private static BundleEvaluator bundleEvaluatorInstance;
	/** 
	 * Private, singleton constructor. 
	 */
	private BundleEvaluator() {
	}
	/**
	 * Gets the single instance of BundleEvaluator.
	 * @return single instance of BundleEvaluator
	 */
	public static BundleEvaluator getInstance() {
		if (bundleEvaluatorInstance==null) {
			bundleEvaluatorInstance = new BundleEvaluator();
		}
		return bundleEvaluatorInstance;
	}
	
	/**
	 * Returns the description of excluded bundles as HashSet.
	 * @return the excluded bundle hash set
	 */
	private HashSet<String> getBundleExcludeHashSet() {
		if (bundleExcludeHashSet==null) {
			bundleExcludeHashSet = new HashSet<>();
			bundleExcludeHashSet.add("com.ibm.icu");
			bundleExcludeHashSet.add("javax.inject");
			bundleExcludeHashSet.add("javax.servlet");
			bundleExcludeHashSet.add("javax.xml");
			bundleExcludeHashSet.add("org.eclipse");
			bundleExcludeHashSet.add("org.w3c");
			bundleExcludeHashSet.add("org.apache");
			
			bundleExcludeHashSet.add("org.agentgui.lib.bouncyCastle");
			bundleExcludeHashSet.add("org.agentgui.lib.googleTranslate");
			bundleExcludeHashSet.add("org.agentgui.lib.hypericSigar");
			bundleExcludeHashSet.add("org.agentgui.lib.jep");
			bundleExcludeHashSet.add("org.agentgui.lib.jFreeChart");
			bundleExcludeHashSet.add("org.agentgui.lib.jung");
			bundleExcludeHashSet.add("org.agentgui.lib.mySQL");
			bundleExcludeHashSet.add("org.agentgui.lib.oidc");
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
	
		boolean excluded = false;
		
		String[] nameParts = symbolicBundleName.split("\\.");
		String name2Check = nameParts[0]; 
		for (int i=0; i<nameParts.length; i++) {
			// --- Check if the part is already part of the exclude list ------
			if (this.getBundleExcludeHashSet().contains(name2Check)==true) return true;
			if ((i+1)>=nameParts.length) break;
			name2Check += "." + nameParts[i+1];
		}
		return excluded;
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
		for (AbstractBundleClassFilter filter : this.getEvaluationFilterResults()) {
			filter.addBusyMarker(busyMarker);
		}
	}
	/**
	 * Removes the specified busy marker from all filter.
	 * @param busyMarker the busy marker
	 */
	public void removeBusyMarkerFromAllFilter(UID busyMarker) {
		for (AbstractBundleClassFilter filter : this.getEvaluationFilterResults()) {
			filter.removeBusyMarker(busyMarker);
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
		return this.getBundleContext().getBundles();
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
		Bundle[] bundles = this.getBundles();
		for (int i = 0; i < bundles.length; i++) {
			this.evaluateBundleInThread(bundles[i], bundleClassFilterToUse);
		}
	}

	/**
	 * Searches / filters in the specified bundle for the classes that are specified by the bundle class filter.
	 *
	 * @param bundle the bundle to search in (<code>null</code> is NOT permitted)
	 * @param bundleClassFilterToUse the bundle class filter to exclusively search with (<code>null</code> IS permitted).
	 * @see #getEvaluationFilterResults()
	 */
	public void evaluateBundleInThread(final Bundle bundle, final AbstractBundleClassFilter bundleClassFilterToUse) {

		if (bundle==null) return;
		if (this.isExcludedBundle(bundle.getSymbolicName())==true) return;
		
		// --- Define search thread for the bundle ------------------
		Thread searchThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					evaluateBundle(bundle, bundleClassFilterToUse);
				} catch (IllegalStateException isEx) {
					System.err.println(Thread.currentThread().getName() + ": " + isEx.getMessage());
				} catch (Exception ex) {
					System.err.println(Thread.currentThread().getName() + ": " + ex.getMessage());
				}
			}
		});
		searchThread.setName("BundleSearch_" + bundle.getSymbolicName());
		searchThread.start();
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
		
		long starTime = System.nanoTime(); 
		if (this.debug==true) {
			System.out.println("Evaluate bundle " + bundle.getSymbolicName());
		}

		// ----------------------------------------------------------
		// --- Set a busy marker for the used bundle filter ---------
		// ----------------------------------------------------------
		UID busyMarker = new UID();
		if (bundleClassFilterToUse==null) {
			this.addBusyMarkerToAllFilter(busyMarker);
		} else {
			bundleClassFilterToUse.addBusyMarker(busyMarker);
		}
		// ----------------------------------------------------------
		
		try {
			// --- Get all classes from the bundle ------------------
			List<Class<?>> classes = this.getClasses(bundle);
			for (int i = 0; i < classes.size(); i++) {
				// --------------------------------------------------
				// --- Check single class ---------------------------
				Class<?> clazz = classes.get(i);
				if (this.getSourceBundleOfClass(clazz)!=bundle) continue;
				
				if (bundleClassFilterToUse==null) {
					// ----------------------------------------------
					// --- Use all known filter ---------------------
					// ----------------------------------------------
					for (int j = 0; j < getEvaluationFilterResults().size(); j++) {
						AbstractBundleClassFilter classFilter = this.getEvaluationFilterResults().get(j);
						if (classFilter.isInFilterScope(clazz)==true) {
							classFilter.addClassFound(clazz.getName(), bundle.getSymbolicName());
						}
					}
					
				} else {
					// ----------------------------------------------
					// --- Use the specified class filter -----------
					// ----------------------------------------------
					if (bundleClassFilterToUse.isInFilterScope(clazz)==true) {
						bundleClassFilterToUse.addClassFound(clazz.getName(), bundle.getSymbolicName());
					}
				}
				
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
	 * @param options the options according to the {@link BundleWiring} that are {@link BundleWiring#LISTRESOURCES_RECURSE}, {@link BundleWiring#LISTRESOURCES_LOCAL} or {@link BundleWiring#FINDENTRIES_RECURSE}
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
		if (bundleWiring!=null) {
			Collection<String> resources = bundleWiring.listResources(packagePath, "*.class", options);
			for (String resource : resources) {
				// --- Get a suitable class name --------------------
				String className = this.getClassName(resource);
				if (className!=null) {
					
					try {
						// ---- Load the class into the bundle ------ 
						Class<?> clazz = bundle.loadClass(className);
						if (this.getSourceBundleOfClass(clazz)==bundle) {
							bundleClasses.add(clazz);
						}
						
					} catch (ClassNotFoundException cnfEx) {
						//ex.printStackTrace();
					} catch (NoClassDefFoundError ncDefEx) {
						//ex.printStackTrace();
					} catch (IllegalStateException isfEx) {
						//ex.printStackTrace();
					} catch (IllegalAccessError iae) {
						//ex.printStackTrace();
					}
				}
			}
			
			// --- If bundle is packed in a jar, we're done ---------
			if (this.isJarBundle(bundle)==false) {
				// ------------------------------------------------------
				// --- If the bundle wiring worked, check for jars ------ 
				// ------------------------------------------------------			
				// --- Does the bundle has a "Bundle-ClassPath" entry ---
				Vector<String> bundleClassPathEntries  = this.getBundleClassPathEntries(bundle);
				if (bundle.getState()==Bundle.ACTIVE && bundleClassPathEntries!=null && bundleClassPathEntries.size()>0) {
					// --- Check for available jars in the bundle--------
					Enumeration<URL> bundleJars = bundle.findEntries("", "*.jar", true);
					if (bundleJars!=null) {
						while (bundle.getState()==Bundle.ACTIVE && bundleJars.hasMoreElements()) {
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
							if (debug) System.out.println(debugText); 
							
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
	public List<String> getPackages(List<Class<?>> classesList) {
		List<String> packagesFound = new ArrayList<String>();
		for (int i = 0; i < classesList.size(); i++) {
			Class<?> classFound = classesList.get(i);
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
	 * @param classList the class list
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
	 * @param file the file
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
	 * @param file the file
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
			// --- Check the jar file entries -----------------------
			Enumeration<JarEntry> jarFileEntries = jarFile.entries();
			while (jarFileEntries.hasMoreElements()) {
				
				JarEntry entry = (JarEntry)jarFileEntries.nextElement();
				String className = entry.getName();
				if (entry.isDirectory()==false && className.endsWith(".class") && className.contains("$")==false) {
					// --- Correct the class name -------------------
					className = className.substring(0, className.length()-6);
					if (className.startsWith("/")) {
						className = className.substring(1);
					}
					className = className.replace('/', '.');
					classesOfJarFile.add(className);
				}
			} // end while

			// --- Close the jar file -------------------------------
			try {
				jarFile.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
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
			bundleURL = FileLocator.resolve(bundle.getEntry("/"));
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

		// --- Corrections, if we are under windows -------
		if (Platform.getOS().compareTo(Platform.OS_WIN32) == 0) {
			//pluginInstallDir = pluginInstallDir.substring(1);
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
		Vector<ClassRequest> classRequests = new Vector<>(this.getClassRequestHash().values());
		for (int i = 0; i < classRequests.size(); i++) {
			ClassRequest classRequest = classRequests.get(i);
			if (System.currentTimeMillis()>=classRequest.getRequestTime() + this.requestMaxStayTime) {
				this.getClassRequestHash().remove(classRequest.getRequestID());
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
	
}
