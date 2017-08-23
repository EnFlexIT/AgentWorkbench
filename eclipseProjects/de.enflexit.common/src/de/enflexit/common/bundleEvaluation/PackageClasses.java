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

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;

/**
 * The Class PackageClasses extends an ArrayList and 
 * will determine all classes within a package.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PackageClasses extends ArrayList<String> {

	private static final long serialVersionUID = -256361698681180954L;

	private Class<?> classInPackage;
	private String searchInPackage;
	
	/**
	 * Instantiates a new instance package classes.
	 * @param classInPackage the class in package
	 */
	public PackageClasses(Class<?> classInPackage) {
		this.classInPackage = classInPackage;
		this.searchInPackage = this.classInPackage.getPackage().getName();
		this.resolvePackageClasses();
	}
	/**
	 * Instantiates a new package classes.
	 * @param packageName the package name where 
	 */
	public PackageClasses(String packageName) {
		this.searchInPackage = packageName;
		this.resolvePackageClasses();
	}
	/**
	 * Initial detection of the available classes by using the 'SearchReference'.
	 */
	private void resolvePackageClasses() {
		
		// --- Get the BundleEvaluator -------------------
		BundleEvaluator be = BundleEvaluator.getInstance();
		
		// ------------------------------------------------
		// --- Case that a class is already known ---------
		// ------------------------------------------------
		if (this.classInPackage!=null) {
			// --- Find the required bundle ---------------
			Bundle bundle = be.getSourceBundleOfClass(this.classInPackage);
			// --- Get class list of the package ----------
			List<String> classesFound = be.getClassReferences(bundle, this.searchInPackage);
			this.addAll(classesFound);
			return;
		}
		// ------------------------------------------------
		// --- Case that only the package is known --------
		// ------------------------------------------------
		Bundle[] bundles = be.getBundles();
		for (int i = 0; i < bundles.length; i++) {
			Bundle bundle = bundles[i];
			List<String> classesFound = be.getClassReferences(bundle, this.searchInPackage);
			if (classesFound!=null && classesFound.size()>0) {
				this.addAll(classesFound);
				return;
			}
		}
	}
	
	
}
