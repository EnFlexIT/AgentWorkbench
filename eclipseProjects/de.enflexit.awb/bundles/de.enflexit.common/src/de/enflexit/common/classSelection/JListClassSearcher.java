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
package de.enflexit.common.classSelection;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.SwingUtilities;

import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;
import de.enflexit.common.bundleEvaluation.BundleClassFilterListener;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import de.enflexit.common.swing.JListWithProgressBar;
import de.enflexit.common.swing.SortedListModel;

/**
 * This class can be use like a JList, but it will show a progress, if
 * the corresponding search process is still running
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JListClassSearcher extends JListWithProgressBar<ClassElement2Display> implements BundleClassFilterListener {

	private static final long serialVersionUID = 6613469481292382381L;

	private Class<?> class2Search4;
	private Vector<String> exclusiveBundleNames;

	private AbstractBundleClassFilter bundleClassFilter;
	private SortedListModel<ClassElement2Display> listModel;
	
	private ArrayList<JListClassSearcherListener> listenerList;
	
	private String textSearchFor;
	private String textExcludePackage;
	
	
	/**
	 * This constructor is only used by the WindowBBuilder  
	 */
	@Deprecated
	public JListClassSearcher() {
		super();
	}
	/**
	 * Instantiates a new JList that presents or searches for specific classes.
	 * @param classToSearchFor the class to search for
	 */
	public JListClassSearcher(Class<?> classToSearchFor) {
		this(classToSearchFor, null);
	}
	/**
	 * Instantiates a new JList that presents or searches for specific classes.
	 *
	 * @param classToSearchFor the class to search for
	 * @param exclusiveBundleNames the exclusive bundle names
	 */
	public JListClassSearcher(Class<?> classToSearchFor, Vector<String> exclusiveBundleNames) {
		super();
		this.class2Search4 = classToSearchFor;
		this.setExclusiveBundleNames(exclusiveBundleNames);
		this.setModel(this.getListModel());
	}
	
	/**
	 * Returns the class after that is being searched for.
	 * @return the class after that is being searched for.
	 */
	public Class<?> getClass2SearchFor() {
		return this.class2Search4;
	}
	
	/**
	 * Returns the exclusive bundle names, if any.
	 * @return the exclusive bundle names
	 */
	public Vector<String> getExclusiveBundleNames() {
		if (exclusiveBundleNames==null) {
			exclusiveBundleNames = new Vector<>();
		}
		return exclusiveBundleNames;
	}
	/**
	 * Sets the exclusive bundle names.
	 * @param exclusiveBundleNames the new exclusive bundle names of <code>null</code>
	 */
	public void setExclusiveBundleNames(Vector<String> exclusiveBundleNames) {
		this.exclusiveBundleNames = exclusiveBundleNames;
		this.setModelFiltered(textSearchFor, textExcludePackage);
	}
	
	/**
	 * Returns the bundle class filter for the current class to search for.
	 * @return the bundle class filter
	 */
	public AbstractBundleClassFilter getBundleClassFilter() {
		if (bundleClassFilter==null && this.getClass2SearchFor()!=null) {
			// --- Get bundle evaluator and try to find the corresponding filter --------
			BundleEvaluator bEvaluator = BundleEvaluator.getInstance();
			bundleClassFilter = bEvaluator.getBundleClassFilterByClass(this.getClass2SearchFor());
			if (bundleClassFilter==null) {
				// --- Filter not found - create a new filter ---------------------------
				bundleClassFilter = new AbstractBundleClassFilter() {
					@Override
					public boolean isInFilterScope(Class<?> clazz) {
						return getClass2SearchFor().isAssignableFrom(clazz) && getClass2SearchFor().equals(clazz)==false;
					}
					@Override
					public boolean isFilterCriteria(Class<?> clazz) {
						return (clazz==getClass2SearchFor());
					}
					@Override
					public String getFilterScope() {
						return getClass2SearchFor().getName();
					}
				};
				// --- Add the filter to the BundleEvaluator ----------------------------
				bEvaluator.addBundleClassFilter(bundleClassFilter);
			}
		}
		return bundleClassFilter;
	}
	
	/**
	 * Sets the DefaultListModel for the current JList to display.
	 * @return the list model of {@link ClassElement2Display}
	 */
	public synchronized SortedListModel<ClassElement2Display> getListModel() {
		if (listModel==null) {
			listModel = new SortedListModel<>();
			AbstractBundleClassFilter bcf = this.getBundleClassFilter();
			if (bcf!=null) {
				bcf.addBundleClassFilterListener(this);			
			}
		}
		return listModel;
	}
	/**
	 * Adds the specified element to the list model considering the Swing environment.
	 * @param ce2d the ClassElement2Display to add
	 */
	private void addToListModel(final ClassElement2Display ce2d) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JListClassSearcher.this.getListModel().add(ce2d);
				if (textSearchFor!=null || textExcludePackage!=null) {
					JListClassSearcher.this.setModelFiltered(textSearchFor, textExcludePackage);
				}
			}
		});
	}
	/**
	 * Removes the specified element to the list model considering the Swing environment.
	 * @param ce2d the ClassElement2Display to add
	 */
	private void removefromListModel(final ClassElement2Display ce2d) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JListClassSearcher.this.getListModel().removeElement(ce2d);
				if (textSearchFor!=null || textExcludePackage!=null) {
					JListClassSearcher.this.setModelFiltered(textSearchFor, textExcludePackage);
				}
			}
		});
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.BundleClassFilterListener#addClassFound(java.lang.String, java.lang.String)
	 */
	@Override
	public void addClassFound(String className, String symbolicBundleName) {
		ClassElement2Display ce2d = new ClassElement2Display(className, symbolicBundleName);
		this.addToListModel(ce2d);
		for (JListClassSearcherListener listener : this.getListenerList()) {
			listener.addClassFound(ce2d);
		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.BundleClassFilterListener#removeClassFound(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeClassFound(String className, String symbolicBundleName) {
		ClassElement2Display ce2d = new ClassElement2Display(className, symbolicBundleName);
		this.removefromListModel(ce2d);
		// --- Inform listener -------------------------------------- 
		for (JListClassSearcherListener listener : this.getListenerList()) {
			listener.removeClassFound(ce2d);
		}
	}
	
	/**
	 * Filters the displayed list, depending on the search String.
	 * @param search4 the new model filtered
	 */
	public void setModelFiltered(String search4) {
		this.setModelFiltered(search4, null);
	}
	/**
	 * Filters the displayed list, depending on the search String
	 * while excluding the alsoExclude-variable .
	 *
	 * @param searchFor the search parameter
	 * @param excludePackage the exclude package
	 */
	public void setModelFiltered(String searchFor, String excludePackage) {
		
		// --- Adjust search text -------------------------
		if (searchFor==null || searchFor.trim().equals("")==true) {
			this.textSearchFor = null;
		} else {
			this.textSearchFor = searchFor.trim();
		}
		// --- Adjust excluded package --------------------
		if (excludePackage==null || excludePackage.trim().equals("")==true) {
			this.textExcludePackage = null;
		} else {
			this.textExcludePackage = excludePackage.trim();
		}
		
		// --- Set required list model -------------------- 
		if (this.textSearchFor==null && this.textExcludePackage==null && this.getExclusiveBundleNames().size()==0) {
			// --- No filter => use full list -------------
			this.setModel(this.getListModel());
		
		} else {
			// --- Start filtering the Classes ------------
			SortedListModel<ClassElement2Display> tmpListModel = new SortedListModel<ClassElement2Display>();
			for (int i =0; i<this.getListModel().getSize();i++) {
				
				boolean addToTmpList = true;
				ClassElement2Display ce2d = this.getListModel().get(i);
				String compareString = ce2d.toString().toLowerCase();
				String bundleName = ce2d.getBundleName();
				
				// --- Check for 'textSearchFor' --------------------
				if (this.textSearchFor!=null && this.textSearchFor.isBlank()==false) {
					addToTmpList = compareString.contains(this.textSearchFor.toLowerCase());
				}
				// --- Check for 'textExcludePackage' ---------------
				if (addToTmpList==true && this.textExcludePackage!=null && this.textExcludePackage.isBlank()==false) {
					addToTmpList = (ce2d.getClassElement().toLowerCase().startsWith(this.textExcludePackage.toLowerCase())==false);
				}
				// --- Check to exclude bundles ---------------------
				if (addToTmpList==true && this.getExclusiveBundleNames().size()>0) {
					addToTmpList = this.getExclusiveBundleNames().contains(bundleName);
				}
				
				// --- Finally: add to tmp list model --------------- 
				if (addToTmpList==true) {
					tmpListModel.addElement(ce2d);
				}
				
			}
			// --- Set filtered model as current list -----
			this.setModel(tmpListModel);
		}
	}

	// ------------------------------------------------------------------------
	// --- From here, listener to the list will be handled --------------------
	// ------------------------------------------------------------------------	
	/**
	 * Returns the listener list.
	 * @return the listener list
	 */
	private ArrayList<JListClassSearcherListener> getListenerList() {
		if (listenerList==null) {
			listenerList = new ArrayList<>();
		}
		return listenerList;
	}
	
	/**
	 * Adds the specified JListClassSearcherListener to the list of listener.
	 *
	 * @param listener the listener to add
	 * @return true, if the listener was added successfully
	 */
	public boolean addClassSearcherListListener(JListClassSearcherListener listener) {
		if (listener!=null && this.getListenerList().contains(listener)==false) {
			boolean added = this.getListenerList().add(listener);
			if (added==true) {
				// --- Provide already known results to the new listener ------
				for (int i = 0; i < this.getListModel().getSize(); i++) {
					listener.addClassFound(this.getListModel().get(i));
				}
			}
			return added;
		}
		return false;
	}
	
	/**
	 * removes the specified JListClassSearcherListener to the list of listener.
	 *
	 * @param listener the listener to remove
	 * @return true, if the listener was successfully removed
	 */
	public boolean removeClassSearcherListListener(JListClassSearcherListener listener) {
		if (listener==null) return false;
		return this.getListenerList().remove(listener);
	}
	// ------------------------------------------------------------------------
	
	
} 
