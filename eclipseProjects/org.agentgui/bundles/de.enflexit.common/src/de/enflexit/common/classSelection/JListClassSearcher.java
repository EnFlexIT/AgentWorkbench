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
	private SortedListModel<ClassElement2Display> currListModel;
	
	private ArrayList<JListClassSearcherListener> listenerList;
	
	private String textSearchFor;
	private String textExcludePackage;
	
	
	/**
	 * This constructor was only build to enable the use of the Visual Editor  
	 */
	@Deprecated
	public JListClassSearcher() {
		super();
	}
	
	/**
	 * Instantiates a new JList that presents or searches for specific classes.
	 * @param class2Search4 the class to search for
	 */
	public JListClassSearcher(Class<?> classToSearchFor) {
		this(classToSearchFor, null);
	}
	/**
	 * Instantiates a new JList that presents or searches for specific classes.
	 * @param classToSearchFor the class to search for
	 * @param exclusiveBundleNames the exclusive bundle names
	 * @param listener the listener for the classes found
	 */
	public JListClassSearcher(Class<?> classToSearchFor, Vector<String> exclusiveBundleNames) {
		super();
		this.class2Search4 = classToSearchFor;
		this.exclusiveBundleNames = exclusiveBundleNames;
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
	private Vector<String> getExclusiveBundleNames() {
		if (exclusiveBundleNames==null) {
			exclusiveBundleNames = new Vector<>();
		}
		return exclusiveBundleNames;
	}
	
	/**
	 * Returns the bundle class filter for the current class to search for.
	 * @return the bundle class filter
	 */
	public AbstractBundleClassFilter getBundleClassFilter() {
		if (bundleClassFilter==null) {
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
	 * Sets the DefaultListModel for the current JList to display
	 */
	public synchronized SortedListModel<ClassElement2Display> getListModel() {
		if (currListModel==null) {
			currListModel = new SortedListModel<>();
			this.getBundleClassFilter().addBundleClassFilterListener(this);			
		}
		return currListModel;
	}
	/**
	 * Adds the specified element to the list model considering the Swing environment.
	 * @param ce2d the ClassElement2Display to add
	 */
	private void addToListModel(final ClassElement2Display ce2d) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getListModel().add(ce2d);
				if (textSearchFor!=null || textExcludePackage!=null) {
					setModelFiltered(textSearchFor, textExcludePackage);
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
				getListModel().removeElement(ce2d);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.BundleClassFilterListener#addClassFound(java.lang.String, java.lang.String)
	 */
	@Override
	public void addClassFound(String className, String symbolicBundleName) {
		
		ClassElement2Display ce2d = new ClassElement2Display(className, symbolicBundleName);
		boolean isInformListener = false;
		
		if (this.getExclusiveBundleNames().size()==0) {
			// --- Simply add the class found in the list model ----- 
			this.addToListModel(ce2d);
			isInformListener = true;
		} else {
			// --- Check, if the class is out of the exclusive bundle -------
			for (int i = 0; i < this.getExclusiveBundleNames().size(); i++) {
				String exBundleName = this.getExclusiveBundleNames().get(i);
				if (exBundleName!=null && exBundleName.trim().equals("")==false) {
					if (symbolicBundleName.equals(exBundleName)) {
						this.addToListModel(ce2d);
						isInformListener = true;
					}
				}
			}
		}
		// --- Inform listener --------------------------------------
		if (isInformListener==true) {
			for (JListClassSearcherListener listener : this.getListenerList()) {
				listener.addClassFound(ce2d);
			}
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
		if (this.textSearchFor==null && this.textExcludePackage==null) {
			// --- No filter => use full list -------------
			this.setModel(this.getListModel());
		
		} else {
			// --- Start filtering the Classes ------------
			SortedListModel<ClassElement2Display> tmpListModel = new SortedListModel<ClassElement2Display>();
			for (int i =0; i<this.getListModel().getSize();i++) {
				
				ClassElement2Display ce2d = this.getListModel().get(i);
				String compareString = ce2d.toString().toLowerCase();
				boolean addForClassDescription = true;
				if (this.textSearchFor!=null) {
					addForClassDescription = compareString.contains(this.textSearchFor.toLowerCase());
				}
				
				if (this.textExcludePackage==null) {
					if (addForClassDescription==true) {
						tmpListModel.addElement(ce2d);
					}
					
				} else {
					boolean addForPackageExclude = ce2d.getClassElement().startsWith(this.textExcludePackage.toLowerCase())==false;
					if (addForClassDescription==true && addForPackageExclude==true) {
						tmpListModel.addElement(ce2d);
					}
					
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
	 * Adds the specified JListClassSearcherListener to the list of listener
	 * @param listener the listener to add
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
	 * removes the specified JListClassSearcherListener to the list of listener
	 * @param listener the listener to remove
	 */
	public boolean removeClassSearcherListListener(JListClassSearcherListener listener) {
		if (listener==null) return false;
		return this.getListenerList().remove(listener);
	}
	// ------------------------------------------------------------------------
	
	
} 
