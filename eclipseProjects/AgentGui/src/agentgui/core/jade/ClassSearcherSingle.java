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
import agentgui.core.common.ClassLoaderUtil;
import agentgui.core.config.GlobalInfo.ExecutionEnvironment;
import agentgui.core.gui.components.ClassElement2Display;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.gui.components.JListWithProgressBar;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.Project;

/**
 * This class will start a search process for classes that are extending a specified super class. 
 * 
 * @see ClassFinder
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassSearcherSingle {

	public static final int ACC_INTERFACE = 0x0200;
	public static final int ACC_ABSTRACT = 0x0400;
	
	private Project currProject = null;
	
	/** The packages in the project. */
	private Vector<String> packagesInProject = new Vector<String>();
	
	private ClassUpdater cu = null;
	private Class<?> class2Search4;
	private String classname;
	private ClassFinderFilter classfilter;
	private List<Object> searchErrorStack = null;
		
	private boolean busy = false;

	private boolean classesLoaded = false;
	private Vector<Class<?>> classesFound = new Vector<Class<?>>();
	private DefaultListModel jListModelClassesFound = new DefaultListModel();
	private DefaultListModel jListModelClassesFoundProject = new DefaultListModel();
	
	private Vector<JListClassSearcher> jListProgress = new Vector<JListClassSearcher>();
	
	/**
	 * Constructor of this class. Executes the search for the class
	 * @param clazz2Search4 The super class to search for
	 */
	public ClassSearcherSingle(Class<?> clazz2Search4) {
		this.class2Search4 =  clazz2Search4;
		this.classname = clazz2Search4.getName();
	}
	
	/** Start the search process. */
	public void startSearch() {
		
		classesFound = new Vector<Class<?>>();
		jListModelClassesFound = new DefaultListModel();
		jListModelClassesFoundProject = new DefaultListModel();
		
		this.setBusy(true);

		// --- If there is a running search stop it -------
		if (cu!=null) {
			this.stopSearch();
		}
		
		// --- Start the search of classes ----------------
		cu = new ClassUpdater(classname, classfilter == null ? new ClassFilter() : classfilter);
		if (classname.equals(Object.class.getName())) {
			cu.setUpdateEvery(10);	
		}
		cu.startSearch();
				
	}
	/** Stop the search process. */
	public void stopSearch() {
		if (cu!=null) {
			cu.interruptSearch();
			cu.stopSearch();
			cu = null;
		}
	}
	/** Re start search. */
	public void reStartSearch() {
		this.stopSearch();
		this.startSearch();
	}
	
	/**
	 * Registers a JListWithProgress to this search process
	 * 
	 * @see JListWithProgressBar
	 * @param jListClassSearcher the JListWithProgress
	 */
	public void registerJListWithProgress(JListClassSearcher jListClassSearcher) {
		if (this.jListProgress.contains(jListClassSearcher)==false) {
			jListProgress.addElement(jListClassSearcher);	
		}
		jListClassSearcher.setBusy(this.busy);
	}
	
	/**
	 * Sets the this search process to busy or not.
	 *
	 * @param isBusy the new busy
	 */
	private void setBusy(boolean isBusy) {
		this.busy = isBusy;
		Vector<JListClassSearcher> jList2Display = new Vector<JListClassSearcher>(jListProgress);
		for (JListClassSearcher jListWP : jList2Display) {
			jListWP.setBusy(this.busy);
		}
	}

	/**
	 * This will set the current project and evaluate the package-names in the project.
	 *
	 * @param project the new project
	 */
	public void setProject(Project project) {
		
		this.currProject = project;
		this.packagesInProject.removeAllElements();
		
		// ------------------------------------------------
		// --- Search for packages in project -------------
		if (this.currProject!=null) {
			// --- We are using our IDE in the moment -----			
			if (Application.getGlobalInfo().getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverIDE) {
				packagesInProject.addElement(currProject.getProjectFolder());
			} 
			// --- If we have external resources ----------
			if (currProject.getProjectResources()!=null && currProject.getProjectResources().size()>0) {
				Vector<String> extResources = currProject.getProjectResources();
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
	 * Checks if is classes loaded.
	 *
	 * @return the classesLoaded
	 */
	public boolean isClassesLoaded() {
		return classesLoaded;
	}
	
	/**
	 * Gets the classes found.
	 *
	 * @param classesFromProjectOnly the classes from project only
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
	 * Allows the running Thread to add the latest results.
	 *
	 * @param list the list
	 */
	private void appendToList(List<Class<?>> list) {
		
		synchronized (this.classesFound) {
			
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
	
	/**
	 * Add2 list model.
	 *
	 * @param listModel the list model
	 * @param clazz the clazz
	 */
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
						ClassElement2Display ce2d = new ClassElement2Display(clazz);
						if (PlatformJadeConfig.isAutoService(clazz.getName())==true) {
							ce2d.setAdditionalText(PlatformJadeConfig.getAutoServiceTextAddition());
						}
						listModel.addElement(ce2d);
						
					} else {
						listModel.addElement(new ClassElement2Display(clazz));
					}
				
			}});
			// ------------------------------------------------------
		}
	}
	
	/**
	 * Returns a DefaultListModel of the classes found.
	 *
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
	 * Returns a DefaultListModel of classes found for the current project.
	 *
	 * @return the jListModelClassesFoundProject
	 */
	public DefaultListModel getjListModelClassesFoundProject() {
		if (this.jListModelClassesFoundProject==null) {
			return new DefaultListModel();
		} else {
			return jListModelClassesFoundProject;	
		}
	}

	/**
	 * Returns the errors of the search process.
	 * @return the errors
	 */
	public List<Object> getErrors() {
		return this.searchErrorStack;
	}
	/**
	 * Sets the errors of the search process. Errors of the kind 
	 * NoClassDefFoundError are eliminated, because they happen naturally 
	 * while searching classes. 
	 * @param errorStack the new errors
	 */
	public void setErrors(List<Object> errorStack) {
		if (errorStack==null) {
			this.searchErrorStack = null;
		} else {
			this.searchErrorStack = new ArrayList<Object>();
			for (Object error : errorStack) {
				if (error instanceof NoClassDefFoundError) {
					// --- Skip ---
				} else {
					this.searchErrorStack.add(error);
				}
			}
		}
	}
	
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassUpdater' --- S T A R T ---------------------------
	// ------------------------------------------------------------------------
	/**
	 * This will be the running thread, searching for the classes.
	 *
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class ClassUpdater implements Runnable, ClassFinderListener {

		private int updateEvery = 1;
		private int numberOfClasses;
		
		private List<Class<?>> classNamesCache;
		private String classname;
		
		private ClassFinder cf;
		private ClassFinderFilter classfilter;

		private Thread searchThread = null;
		
		/**
		 * Instantiates a new class updater.
		 *
		 * @param classname the classname
		 * @param classfilter the classfilter
		 */
		public ClassUpdater(String classname, ClassFinderFilter classfilter) {
			this.classname = classname;
			this.classfilter = classfilter;
		}

		/**
		 * Start search.
		 */
		public synchronized void startSearch(){
		    if (searchThread == null){
		    	searchThread = new Thread(this);
		    	searchThread.start();
		    }
		}
		/**
		 * Interrupt search.
		 */
		public synchronized void interruptSearch() {
		    if (searchThread != null) {
		    	searchThread.interrupt();
		    }
		}   
		/**
		 * Stop search.
		 */
		public synchronized void stopSearch() {
			if (cf!=null) {
				cf.setStopSearch(true);
				if (searchThread!=null) {
					searchThread =null;
				}
			}
		}
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@SuppressWarnings("unchecked")
		public void run() {
			
			Thread.currentThread().setName("ClassSearch-" + class2Search4.getSimpleName());
			
			classNamesCache = new ArrayList<Class<?>>(updateEvery);
			numberOfClasses = 0;
			
			try {
				// Reset error Stack
				setErrors(null);
				// Start the ClassFinder
				cf = new ClassFinder();
				cf.findSubclasses(classname, this, classfilter);
				// in case of remaining results
				if (classNamesCache.size() > 0) {
					appendToList(classNamesCache);
					classNamesCache.clear();
				}
				// last call, with empty list, to update status message
				appendToList(classNamesCache);
				
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			
			setErrors(cf.getErrors());
			classNamesCache = null;
			classesLoaded = true;
			setBusy(false);
			this.stopSearch();
			
		}
		
		/**
		 * Sets the update every.
		 *
		 * @param updateEvery the new update every
		 */
		public void setUpdateEvery(int updateEvery) {
			this.updateEvery = updateEvery;
		}
		
		/* (non-Javadoc)
		 * @see jade.util.ClassFinderListener#add(java.lang.Class, java.net.URL)
		 */
		@SuppressWarnings("rawtypes")
		public void add(Class clazz, URL location) {
			numberOfClasses++;
			classNamesCache.add(clazz);
			if ((numberOfClasses % this.updateEvery) == 0) {
				appendToList(classNamesCache);
				classNamesCache.clear();
			}
		}

	}
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassUpdater' --- S T O P -----------------------------
	// ------------------------------------------------------------------------

		
	// ------------------------------------------------------------------------
	// --- Sub-Class - 'ClassFilter' --- S T A R T ----------------------------
	// ------------------------------------------------------------------------
	/**
	 * Filter-Object to find the right classes.
	 *
	 * @author derksen
	 */
	private class ClassFilter implements ClassFinderFilter {
		
		/* (non-Javadoc)
		 * @see jade.util.ClassFinderFilter#include(java.lang.Class, java.lang.Class)
		 */
		@SuppressWarnings("rawtypes")
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
