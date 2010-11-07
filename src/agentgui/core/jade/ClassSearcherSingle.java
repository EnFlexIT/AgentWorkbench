package agentgui.core.jade;

import jade.util.ClassFinderFilter;
import jade.util.ClassFinderListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.application.Project;
import agentgui.core.common.ClassLoaderUtil;

public class ClassSearcherSingle {

	public static final int ACC_INTERFACE = 0x0200;
	public static final int ACC_ABSTRACT = 0x0400;
	
	public static final String classSearcherNotify = "ClassSearcherUpdate";

	private Project currProject = null;
	
	private ClassUpdater cu = null;
	private Class<?> clazz;
	private String classname;
	private ClassFinderFilter classfilter;
	
	private boolean classesLoaded = false;
	private Vector<Class<?>> classesFound = new Vector<Class<?>>();
	
	/**
	 * Constructor of this class. Executes the search for the class
	 * @param search4Class
	 */
	public ClassSearcherSingle(Class<?> clazz2Search4, Project project) {
		this.clazz =  clazz2Search4;
		this.classname = clazz2Search4.getName();
		this.currProject = project;
		
		cu = new ClassUpdater(classname, classfilter == null ? new ClassFilter() : classfilter);
		new Thread(cu).start();
	}
	
	public void stopSearch() {
		synchronized (cu) {
			cu.stopSearch();
		}
	}
	/**
	 * @return the classesLoaded
	 */
	public boolean isClassesLoaded() {
		return classesLoaded;
	}
	/**
	 * @return the classesFound
	 */
	public Vector<Class<?>> getClassesFound(boolean filtered) {
		
		if (filtered==true) {
			// -------------------------------------------------
			// --- Filter packages from the current project ----
			Vector<String> packagesInProject = new Vector<String>();
			
			if (Application.RunInfo.AppExecutedOver().equalsIgnoreCase("IDE")) {
				// ---------------------------------------------
				// --- We are using our IDE in the moment ------
				packagesInProject.addElement(currProject.getProjectFolder());
			} 

			// -------------------------------------------------
			// --- In case that we have external resources -----
			if (currProject.projectResources!=null && currProject.projectResources.size()>0) {
				Vector<String> extResources = currProject.projectResources;
				String relProPath = currProject.getProjectFolder();
				String absProPath = currProject.getProjectFolderFullPath();
				
				try {
					packagesInProject.addAll(ClassLoaderUtil.getPackageNames(extResources, relProPath, absProPath)) ;
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
			
			// -------------------------------------------------
			// --- Define the (start) result-Vector ------------
			Vector<Class<?>> resultVec = new Vector<Class<?>>();
			for (int i = 0; i < classesFound.size(); i++) {
				Class<?> clazz = classesFound.get(i);
				String clazzName = clazz.getName();
				
				for (String projectPackage : packagesInProject) {
					if (clazzName.startsWith(projectPackage)) {
						resultVec.addElement(clazz);
					}
				}
			}
			return resultVec;
			
		} else {
			return classesFound;
		}
	}
	
	/**
	 * Allows the running Thread to add the latest results
	 * @param list
	 */
	private void appendToList(List<Class<?>> list) {
		synchronized (classesFound) {
			boolean stillSearching = list.size() > 0;
			if (stillSearching) {
				classesFound.addAll(list);
			}
			// --- notify project-window ------------------
			if (currProject!=null) {
				currProject.setNotChangedButNotify(new ClassSearcherUpdate(this.clazz));
			}
		}
	}
	
	
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassSearcherUpdate' --- S T A R T --------------------
	// ------------------------------------------------------------------------
	/**
	 * This will be used to notify the currProject about changes
	 * in the result vector of the search 
	 * @author derksen
	 */
	public class ClassSearcherUpdate {
		private Class<?> clazz2SearchFor;
		public ClassSearcherUpdate(Class<?> clazz2SearchFor) {
			this.clazz2SearchFor = clazz2SearchFor;
		}
		public String toString() {
			return classSearcherNotify;
		}
		public Class<?> getClass2SearchFor() {
			return this.clazz2SearchFor;
		}
	}
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassSearcherUpdate' --- S T O P ----------------------
	// ------------------------------------------------------------------------

	
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassUpdater' --- S T A R T ---------------------------
	// ------------------------------------------------------------------------
	/**
	 * This will be the running thread, searching for the classes
	 * @author derksen
	 */
	private class ClassUpdater extends Thread implements ClassFinderListener {

		private final static int UPDATE_EVERY = 1;
		
		private int numberOfClasses;
		private List<Class<?>> classNamesCache;
		private String classname;
		private ClassFinder cf;
		private ClassFinderFilter classfilter;

		
		public ClassUpdater(String classname, ClassFinderFilter classfilter) {
			this.classname = classname;
			this.classfilter = classfilter;
		}

		public void stopSearch() {
			if (cf!=null) {
				cf.setStopSearch(true);
			}
		}
		
		@SuppressWarnings("unchecked")
		public void add(Class clazz, URL location) {
			numberOfClasses++;
			classNamesCache.add(clazz);
			if ((numberOfClasses % UPDATE_EVERY) == 0) {
				appendToList(classNamesCache);
				classNamesCache.clear();
			}
		}

		public void run() {
			
			Thread.currentThread().setName("ClassSearch-" + clazz.getSimpleName());
			classNamesCache = new ArrayList<Class<?>>(UPDATE_EVERY);
			numberOfClasses = 0;
			cf = new ClassFinder();
			cf.findSubclasses(classname, this, classfilter);
			if (classNamesCache.size() > 0) {
				appendToList(classNamesCache);
				classNamesCache.clear();
			}
			// last call, with empty list, to update status message
			appendToList(classNamesCache);
			classNamesCache = null;
			classesLoaded = true;
		}
		
	}
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassUpdater' --- S T O P -----------------------------
	// ------------------------------------------------------------------------

		
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassFilter' --- S T A R T ----------------------------
	// ------------------------------------------------------------------------
	/**
	 * Filter-Object to find the right classes
	 * @author derksen
	 */
	private class ClassFilter implements ClassFinderFilter {
		@SuppressWarnings("unchecked")
		public boolean include(Class superClazz, Class clazz) {
			int modifiers = clazz.getModifiers();
			boolean doInclude = ((modifiers & (ACC_ABSTRACT | ACC_INTERFACE)) == 0);
			if (doInclude) {
				doInclude = !clazz.getName().equals(classname);
			}
			return doInclude;
		}
	}
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassFilter' --- S T O P ------------------------------
	// ------------------------------------------------------------------------

	
}
