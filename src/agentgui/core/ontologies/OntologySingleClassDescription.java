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
package agentgui.core.ontologies;

import java.util.ArrayList;

/**
 * This class represents the description of all slots in an ontology 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologySingleClassDescription {

	private Class<?> clazz = null;
	private String reference = null;
	private String className = null;
	private String packageName = null;
	
	/**
	 * Holds all slot descriptions for the current single ontology class  
	 */
	public ArrayList<OntologySingleClassSlotDescription> arrayList4SlotDescriptions = new ArrayList<OntologySingleClassSlotDescription>();
	
	
	/**
	 * Constructor of this class
	 */
	public OntologySingleClassDescription() {

	}
	
	/**
	 * @param clazz the class to set
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	/**
	 * @return the class
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @return the reference
	 */
	public String getClassReference() {
		return reference;
	}
	/**
	 * @param classReference the reference to set
	 */
	public void setClassReference(String classReference) {
		this.reference     = classReference;
		String packageName = classReference.substring(0, classReference.lastIndexOf("."));
		String className   = classReference.substring(classReference.lastIndexOf(".") + 1, classReference.length());
		this.setPackageName(packageName);
		this.setClassName(className);
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
