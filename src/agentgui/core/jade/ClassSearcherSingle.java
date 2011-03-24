package agentgui.core.jade;

import jade.core.Agent;
import jade.util.ClassFinderFilter;
import jade.util.ClassFinderListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import agentgui.core.agents.AgentClassElement;
import agentgui.core.application.Application;
import agentgui.core.application.Project;
import agentgui.core.common.ClassLoaderUtil;
import agentgui.core.gui.components.ClassElement2Display;
import agentgui.core.gui.components.JListClassSearcher;

public class ClassSearcherSingle {

	public static final int ACC_INTERFACE = 0x0200;
	public static final int ACC_ABSTRACT = 0x0400;
	
	private Project currProject = null;
	private Vector<String> packagesInProject = new Vector<String>();
	
	private ClassUpdater cu = null;
	private Class<?> class2Search4;
	private String classname;
	private ClassFinderFilter classfilter;
	
	private boolean busy = false;
	private boolean classesLoaded = false;
	private Vector<Class<?>> classesFound = new Vector<Class<?>>();
	
	private DefaultListModel jListModelClassesFound = new DefaultListModel();
	private DefaultListModel jListModelClassesFoundProject = new DefaultListModel();
	private Vector<JListClassSearcher> jListProgress = new Vector<JListClassSearcher>();
	
	/**
	 * Constructor of this class. Executes the search for the class
	 * @param search4Class
	 */
	public ClassSearcherSingle(Class<?> clazz2Search4) {
		this.class2Search4 =  clazz2Search4;
		this.classname = clazz2Search4.getName();
	}
	
	public void startSearch() {
		
		classesFound.removeAllElements();
		jListModelClassesFound.removeAllElements();
		jListModelClassesFoundProject.removeAllElements();
		
		this.setBusy(true);
		
		// --- Start the search of classes ----------------
		cu = new ClassUpdater(classname, classfilter == null ? new ClassFilter() : classfilter);
		if (classname.equals(Object.class.getName())) {
			cu.setUpdateEvery(10);	
		}
		new Thread(cu).start();		
	}
	public void reStartSearch() {
		this.stopSearch();
		this.startSearch();
	}
	public void stopSearch() {
		if (cu!=null) {
			synchronized (cu) {
				cu.stopSearch();
			}
		}
	}
	public void registerJListWithProgress(JListClassSearcher jListClassSearcher) {
		jListProgress.addElement(jListClassSearcher);	
		jListClassSearcher.setBusy(this.busy);
	}
	private void setBusy(boolean isBusy) {
		this.busy = isBusy;
		Vector<JListClassSearcher> jList2Display = new Vector<JListClassSearcher>(jListProgress);
		for (JListClassSearcher jListWP : jList2Display) {
			jListWP.setBusy(this.busy);
		}
	}

	/**
	 * Thi will set the current project and evaluate the package-names in the project
	 * @param project
	 */
	public void setProject(Project project) {
		
		this.currProject = project;
		this.packagesInProject.removeAllElements();
		
		// ------------------------------------------------
		// --- Search for packages in project -------------
		if (this.currProject!=null) {
			// --- We are using our IDE in the moment -----			
			if (Application.RunInfo.AppExecutedOver().equalsIgnoreCase("IDE")) {
				packagesInProject.addElement(currProject.getProjectFolder());
			} 
			// --- If we have external resources ----------
			if (currProject.projectResources!=null && currProject.projectResources.size()>0) {
				Vector<String> extResources = currProject.projectResources;
				String absProPath = currProject.getProjectFolderFullPath();
				try {
					packagesInProject.addAll(ClassLoaderUtil.getPackageNames(extResources, absProPath)) ;
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}
		// ------------------------------------------------
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
	public Vector<Class<?>> getClassesFound(boolean classesFromProjectOnly) {
		
		if (classesFromProjectOnly==true) {
			// -------------------------------------------------
			// --- Filter packages from the current project ----
			// --- and define the result-Vector 			----
			// -------------------------------------------------
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
			
			if (list.size() > 0) {
				
				this.setBusy(true);
				classesFound.addAll(list);
				
				// ----------------------------------------
				// --- add results to the display-lists ---
				for (int i = 0; i < list.size(); i++) {
					Class<?> clazz = list.get(i);
					String clazzName = clazz.getName();
					
					// add to jListModelClassesFound ------
					this.add2ListModel(jListModelClassesFound, clazz);

					// add to jListModelClassesFoundProject
					if (this.currProject!=null) {
						for (String projectPackage : packagesInProject) {
							if (clazzName.startsWith(projectPackage)) {
								this.add2ListModel(jListModelClassesFoundProject, clazz);
								break;
							}
						}
					}
				}// end for
				
				// --- notify JListClassSearcher ---------- 
				for (JListClassSearcher jListWP : jListProgress) {
					jListWP.refreshModel();
				}
				// ----------------------------------------
			}
		}//end synchronized
	}
	
	@SuppressWarnings("unchecked")
	public void add2ListModel(final DefaultListModel listModel, final Class<?> clazz) {
	
		if (clazz.getName().contains("$")==false) {
			// ------------------------------------------------------
			// --- Swing is not Thread Safe ... ---------------------
			// ------------------------------------------------------
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {

					if (class2Search4 == ClassSearcher.CLASSES_AGENTS) {
						Class<? extends Agent> agentClass = (Class<? extends Agent>) clazz;
						listModel.addElement(new AgentClassElement(agentClass));
						
					} else if (class2Search4 == ClassSearcher.CLASSES_ONTOLOGIES) {
						listModel.addElement(new ClassElement2Display(clazz));
						
					} else if (class2Search4 == ClassSearcher.CLASSES_BASESERVICE) {
						listModel.addElement(new ClassElement2Display(clazz));
						
					} else {
						listModel.addElement(new ClassElement2Display(clazz));
					}
				
			}});
			// ------------------------------------------------------
		}
	}
	
	/**
	 * @return the jListModelClassesFound
	 */
	public DefaultListModel getjListModelClassesFound() {
		if (this.jListModelClassesFound==null) {
			return new DefaultListModel();
		} else {
			return jListModelClassesFound;	
		}
	}
	/**
	 * @return the jListModelClassesFoundProject
	 */
	public DefaultListModel getjListModelClassesFoundProject() {
		if (this.jListModelClassesFoundProject==null) {
			return new DefaultListModel();
		} else {
			return jListModelClassesFoundProject;	
		}
	}

	
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassUpdater' --- S T A R T ---------------------------
	// ------------------------------------------------------------------------
	/**
	 * This will be the running thread, searching for the classes
	 * @author derksen
	 */
	private class ClassUpdater extends Thread implements ClassFinderListener {

		private int updateEvery = 1;
		private int numberOfClasses;

		private List<Class<?>> classNamesCache;
		private String classname;
		private ClassFinder cf;
		private ClassFinderFilter classfilter;

		
		public ClassUpdater(String classname, ClassFinderFilter classfilter) {
			this.classname = classname;
			this.classfilter = classfilter;
		}

		public void setUpdateEvery(int updateEvery) {
			this.updateEvery = updateEvery;
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
			if ((numberOfClasses % this.updateEvery) == 0) {
				appendToList(classNamesCache);
				classNamesCache.clear();
			}
		}

		public void run() {
			
			Thread.currentThread().setName("ClassSearch-" + class2Search4.getSimpleName());
			classNamesCache = new ArrayList<Class<?>>(updateEvery);
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
			setBusy(false);
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
