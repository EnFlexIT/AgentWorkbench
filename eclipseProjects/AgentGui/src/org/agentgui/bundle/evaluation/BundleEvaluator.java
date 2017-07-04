package org.agentgui.bundle.evaluation;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

import jade.core.Agent;
import jade.core.migration.InterPlatformMobilityService;


/**
 * The singleton class BundleEvaluator provides help methods to check bundle contents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BundleEvaluator {
	
	public static final String JADE_BUNDLE_NAME = "org.agentgui.lib.jade";
	
	private boolean debug = false;
	
	private HashSet<String> bundleExcludeHashSet;
	private EvaluationFilterResults evaluationFilterResults;

	
	/** The singleton bundle evaluator instance. */
	private static BundleEvaluator bundleEvaluatorInstance;
	/** 
	 * Private, singleton constructor. 
	 */
	private BundleEvaluator() {
		this.getEvaluationFilterResults().add(new FilterForAgent(), false);
		this.getEvaluationFilterResults().add(new FilterForOntology(), false);
		this.getEvaluationFilterResults().add(new FilterForBaseService(), false);
		this.evaluateAllBundles();
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
	 * @return true, if successful
	 */
	public boolean addBundleClassFilter(AbstractBundleClassFilter bundleClassFilter) {
		return this.getEvaluationFilterResults().add(bundleClassFilter);
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
				evaluateBundle(bundle, bundleClassFilterToUse);
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
	 * Returns the classes of the specified bundle.
	 *
	 * @param bundle the bundle
	 * @return the list of classes
	 */
	public List<Class<?>> getClasses(Bundle bundle) {

		List<Class<?>> classesOfCurrentBundle = new ArrayList<Class<?>>();
		
		// --- Checking class files ---------------------------------
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		Collection<String> resources = bundleWiring.listResources("/", "*.class", BundleWiring.LISTRESOURCES_RECURSE);
		for (String resource : resources) {

			URL localResource = bundle.getEntry(resource);
		    if (localResource!=null) {
		        String className = resource.replaceAll("/", ".");
		        // --- For the development environment --------------
		        if (className.startsWith("bin.")==true) {
		        	className = className.substring(4, className.length());
		        }
		        // --- Cut file extension ---------------------------
		        if (className.endsWith(".class")==true) {
		        	className = className.substring(0, className.length()-6);
		        }
		        // ---- Try to load the class into the bundle ------- 
		        try {
					Class<?> clazz = bundle.loadClass(className);
					classesOfCurrentBundle.add(clazz);
					
				} catch (ClassNotFoundException cnfEx) {
					System.err.println("Could not find class '" + className + "'");
					//cnfEx.printStackTrace();
				}
		    }
		}
		
		// --- Load JADE classes? -----------------------------------
		if (bundle.getSymbolicName().equals(JADE_BUNDLE_NAME)==true && this.findClass(bundle, Agent.class.getName())!=null) {
			List<Class<?>> jadeClasses = this.getJadeClasses(bundle);
			if (jadeClasses!=null && jadeClasses.size()>0) {
				classesOfCurrentBundle.addAll(jadeClasses);
			}
		}
		return classesOfCurrentBundle;
	}
	
	/**
	 * Loads and returns the jade classes that are part of the jade.jar or the migration.jar into the specified bundle.
	 *
	 * @param bundle the bundle
	 * @return the jade classes
	 */
	private List<Class<?>> getJadeClasses(Bundle bundle) {
		
		List<Class<?>> classesOfCurrentBundle = new ArrayList<Class<?>>();
		// --- Load classes from jade.jar ---------------------------
		List<Class<?>> jadeJarClasses = this.getJarClassesByClassInstance(bundle, Agent.class);
		if (jadeJarClasses!=null && jadeJarClasses.size()>0) {
			classesOfCurrentBundle.addAll(jadeJarClasses);
		}
		// --- Load classes from migration.jar ----------------------
		List<Class<?>> impsJarClasses = this.getJarClassesByClassInstance(bundle, InterPlatformMobilityService.class);
		if (impsJarClasses!=null && impsJarClasses.size()>0) {
			classesOfCurrentBundle.addAll(impsJarClasses);
		}
		return classesOfCurrentBundle;
	}
	
	/**
	 * Loads and return the classes of the jar file that contains the specified class.
	 *
	 * @param bundle the bundle
	 * @param classInstance the instance
	 * @return the jar classes by object instance
	 */
	private List<Class<?>> getJarClassesByClassInstance(Bundle bundle, Class<?> classInstance) {
		
		if (classInstance==null) return null;
		if (bundle==null) return null;
		
		List<Class<?>> classesFound = new ArrayList<Class<?>>();
		
		// --- Get jar file from class instance ---------------------
		File jarFile = new File(classInstance.getProtectionDomain().getCodeSource().getLocation().getPath());
		if (jarFile.getAbsolutePath().contains("%20")==true) {
			try {
				jarFile = new File(Agent.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
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
	private List<Class<?>> getJarClasses(Bundle bundle, File file) {
		
		if (file.exists()==false) return null; 
		
		// --- Define the result list -------------------------------
		List<Class<?>> classesOfJarFile = new ArrayList<Class<?>>();
			
		// --- Establish connection to jar file --------------------- 
		URL jarFileURL = null;
		JarFile jarFile = null;
		try {
			
			String canonicalPath = file.getCanonicalPath();
			if (canonicalPath.startsWith("/")==false) {
				canonicalPath = "/"+canonicalPath;
			}

			jarFileURL = new URL("file:" + canonicalPath);
			jarFileURL = new URL("jar:" + jarFileURL.toExternalForm() + "!/");
			
			URLConnection urlConnection = jarFileURL.openConnection();
			JarURLConnection conn = (JarURLConnection) urlConnection;
			jarFile = conn.getJarFile();

		} catch (MalformedURLException mUrlEx) {
			mUrlEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		
		// --- Read classes from jar file ---------------------------
		if (jarFile!=null && jarFileURL!=null) {
			// --- Check the jar file entries -----------------------
			Enumeration<JarEntry> e = jarFile.entries();
			while (e.hasMoreElements()) {
				
				JarEntry entry = (JarEntry)e.nextElement();
				String classname = entry.getName();
				if (entry.isDirectory()==false && classname.endsWith(".class") && classname.contains("$")==false) {
					// --- Correct the class name -------------------
					classname = classname.substring(0, classname.length()-6);
					if (classname.startsWith("/")) {
						classname = classname.substring(1);
					}
					classname = classname.replace('/', '.');
					
					// --- Try to load the class into the bundle ----
					try {
						Class<?> classFound = bundle.loadClass(classname);
						classesOfJarFile.add(classFound);
						
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				
			} // end while
		}
		return classesOfJarFile;
	}

	
	/**
	 * Tries to find the specified class in the bundle.
	 *
	 * @param bundle the bundle in which the class is located
	 * @param className the class name
	 * @return the class
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
	
	/**
	 * Returns the class references of the specified bundle.
	 *
	 * @param bundle the bundle
	 * @return the list of class references
	 */
	public List<String> getClassReferences(Bundle bundle) {
		
		List<String> classNamesOfCurrentBundle = new ArrayList<String>();

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		Collection<String> resources = bundleWiring.listResources("/", "*.class", BundleWiring.LISTRESOURCES_RECURSE);
		for (String resource : resources) {
		    URL localResource = bundle.getEntry(resource);
		    if (localResource != null) {
		        String className = resource.replaceAll("/", ".");
		        classNamesOfCurrentBundle.add(className);
		    }
		}
		return classNamesOfCurrentBundle;
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
	
}
