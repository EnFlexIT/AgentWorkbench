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
package agentgui.core.gui.components;

/**
 * This class is used in several JList elements in order to display selected classes
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassElement2Display {

	private Class<?> currClass = null;
	private String additionalText = null;
	
	/**
	 * @param clazz
	 */
	public ClassElement2Display (Class<?> clazz){
		this.currClass=clazz;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		if (this.additionalText!=null) {
			return currClass.getName() + " (" + this.additionalText + ")";
		} else {
			return currClass.getName();	
		}
	}
	/**
	 * Provides the Class
	 * @return the Class
	 */
	public Class<?> getElementClass(){
		return currClass;
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
