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

import org.osgi.framework.Bundle;

/**
 * This class is used in several JList elements in order to display selected classes
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassElement2Display implements Comparable<ClassElement2Display> {

	private String className;
	private String bundleName;
	private String additionalText;

	
	/**
	 * Instantiates a new class element to display in a JList.
	 * @param clazz the class
	 */
	public ClassElement2Display(Class<?> clazz, Bundle bundle){
		this.className = clazz.getName();
		this.bundleName = bundle.getSymbolicName();
	}
	/**
	 * Instantiates a new class element to display in a JList.
	 * @param className the class name
	 */
	public ClassElement2Display(String className, String bundleName){
		this.className = className;
		this.bundleName = bundleName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ClassElement2Display ce2Compare) {
		return this.toString().compareTo(ce2Compare.toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj!=null && obj instanceof ClassElement2Display) {
			ClassElement2Display ce2Check = (ClassElement2Display) obj;
			return ce2Check.toString().equals(this.toString());
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String displayText = ""; 
		if (this.getClassElement()==null || this.getClassElement().trim().equals("")==true) {
			// --- No class defined => display error ------
			displayText = this.getClass().getSimpleName() + ": No class defined error.s";
		} else {
			// --- Symbolic bundle name -------------------
			displayText += "(";
			if (this.getBundleName()==null || this.getBundleName().trim().equals("")==true) {
				displayText += "?";
			} else {
				displayText += this.getBundleName();
			}
			displayText += ") ";

			// --- Class name -----------------------------
			displayText += this.getClassElement();
			
			// --- Additional Text -----------------------
			if (this.getAdditionalText()!=null && this.getAdditionalText().trim().equals("")==false) {
				displayText += " (" + this.getAdditionalText() + ")";
			}
		}
		return displayText;
	}
	/**
	 * Provides the class name
	 * @return the class name
	 */
	public String getClassElement(){
		return className;
	}
	/**
	 * Sets the class element.
	 * @param className the new class element
	 */
	public void setClassElement(String className) {
		this.className = className;
	}
	/**
	 * Returns the simple class name of the class found.
	 * @return the class element simple name
	 */
	public String getClassElementSimpleName() {
		int cutAt = this.getClassElement().lastIndexOf(".");
		return this.getClassElement().substring(cutAt+1);
	}
	
	/**
	 * Returns the bundle name of the current class.
	 * @return the bundle name
	 */
	public String getBundleName() {
		return bundleName;
	}
	/**
	 * Sets the bundle name of the current class.
	 * @param bundleName the new bundle name
	 */
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	
	/**
	 * Can be used in order to set text, which can be stored as an additional information
	 * @param additionalText the additionalText to set
	 */
	public void setAdditionalText(String additionalText) {
		this.additionalText = additionalText;
	}
	/**
	 * Returns text, which will be stored as an additional information
	 * @return the additionalText
	 */
	public String getAdditionalText() {
		return additionalText;
	}
	
	
}
