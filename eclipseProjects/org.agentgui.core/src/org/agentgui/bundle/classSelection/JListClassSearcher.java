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
package org.agentgui.bundle.classSelection;

import java.util.Vector;

import agentgui.core.gui.components.JListWithProgressBar;
import agentgui.core.gui.components.SortedListModel;
import agentgui.core.project.PlatformJadeConfig;
import de.enflexit.common.bundleEvaluation.AbstractBundleClassFilter;
import de.enflexit.common.bundleEvaluation.BundleClassFilterListener;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import jade.core.BaseService;

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
		super();
		this.class2Search4 = classToSearchFor;
		this.setModel(this.getListModel());
	}
//	/**
//	 * Instantiates a new JList that presents or searches for specific classes.
//	 *
//	 * @param classToSearchFor the class to search for
//	 * @param exclusiveBundleName the exclusive bundle name to take search results from
//	 */
//	public JListClassSearcher(Class<?> classToSearchFor, String exclusiveBundleName) {
//		super();
//		this.class2Search4 = classToSearchFor;
//		if (exclusiveBundleName!=null && exclusiveBundleName.trim().equals("")==false) {
//			this.getExclusiveBundleNames().add(exclusiveBundleName.trim());
//		}
//		this.setModel(this.getListModel());
//	}
	/**
	 * Instantiates a new JList that presents or searches for specific classes.
	 *
	 * @param classToSearchFor the class to search for
	 * @param exclusiveBundleName the vectr of exclusive bundle names to take search results from
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
	private AbstractBundleClassFilter getBundleClassFilter() {
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
	public SortedListModel<ClassElement2Display> getListModel() {
		if (currListModel==null) {
			currListModel = new SortedListModel<>();
			this.getBundleClassFilter().addBundleClassFilterListener(this);			
		}
		return currListModel;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.BundleClassFilterListener#addClassFound(java.lang.String, java.lang.String)
	 */
	@Override
	public void addClassFound(String className, String symbolicBundleName) {
		
		ClassElement2Display ce2d = new ClassElement2Display(className, symbolicBundleName);
		
		// ----------------------------------------------------------
		// --- Check if we have to displays additional text --------- 
		// ----------------------------------------------------------
		// --- For a BaseService ----------------
		if (this.getBundleClassFilter().isFilterCriteria(BaseService.class)==true) {
			if (PlatformJadeConfig.isAutoService(className)==true) {
				ce2d.setAdditionalText(PlatformJadeConfig.getAutoServiceTextAddition());
			}
		}
		// ----------------------------------------------------------
		
		if (this.getExclusiveBundleNames().size()==0) {
			// --- Simply add the class found in the list model ----- 
			this.getListModel().addElement(ce2d);
		} else {
			// --- Check, if the class is out of the exclusive bundle -------
			for (int i = 0; i < this.getExclusiveBundleNames().size(); i++) {
				String exBundleName = this.getExclusiveBundleNames().get(i);
				if (exBundleName!=null && exBundleName.trim().equals("")==false) {
					if (symbolicBundleName.equals(exBundleName)) {
						this.getListModel().addElement(ce2d);
					}
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.bundleEvaluation.BundleClassFilterListener#removeClassFound(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeClassFound(String className, String symbolicBundleName) {
		this.getListModel().removeElement(new ClassElement2Display(className, symbolicBundleName));
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

} 
