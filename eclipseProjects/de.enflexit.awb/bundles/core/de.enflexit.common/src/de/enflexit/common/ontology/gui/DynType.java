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
package de.enflexit.common.ontology.gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.enflexit.common.ontology.OntologySingleClassSlotDescription;

/**
 * This class is used as user object in the tree model of the {@link DynForm} class.
 * 
 * @see DynForm
 *  
 * @author Marvin Steinberg - University of Duisburg - Essen
 */
public class DynType {
	
	public static final String typeClass = "class";
	public static final String typeCyclic = "cyclicType";
	public static final String typeRawType = "rawType";
	public static final String typeInnerClassType = "innerClassType";
	
	private OntologySingleClassSlotDescription oscsd = null;

	private String typeName = "";
	private String className = "";
	private String fieldName = "";
	
	private JPanel panel = null;
	private JComponent fieldDisplay = null;
	
	private boolean visibleInTableView = true;
	
	/**
	 * Instantiates a new DynType.
	 *
	 * @param curOSCSD the current OntologySingleClassSlotDescription
	 * @param typeName the type name
	 * @param className the class name
	 * @param locatedOnPanel the located on panel
	 * @param fieldOrClassName the field or class name
	 */
	public DynType(OntologySingleClassSlotDescription curOSCSD, String typeName, String className, JPanel locatedOnPanel, String fieldOrClassName){
		this.oscsd = curOSCSD;
		this.typeName = typeName;
		this.className = className;
		this.panel = locatedOnPanel;
		if(typeName.equalsIgnoreCase(typeRawType) || typeName.equalsIgnoreCase(typeInnerClassType)) {
			this.fieldName = fieldOrClassName;
		}			
	}
	
	/**
	 * Instantiates a new DynType.
	 *
	 * @param curOSCSD the current OntologySingleClassSlotDescription
	 * @param typeName the type name
	 * @param className the class name
	 * @param locatedOnPanel the located on panel
	 * @param fieldName the field name
	 * @param fieldValue the field value
	 */
	public DynType(OntologySingleClassSlotDescription curOSCSD, String typeName, String className, JPanel locatedOnPanel, String fieldName, JComponent fieldValue) {
		this.oscsd = curOSCSD;
		this.typeName = typeName;
		this.className = className;
		this.panel = locatedOnPanel;
		this.fieldName = fieldName;
		this.fieldDisplay = fieldValue;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retVal = null;
		if (this.oscsd==null) {
			retVal = "[base] " + this.typeName + " - " + this.className + " - " + this.fieldName;
		} else {
			retVal = "[" + this.oscsd.getSlotCardinality() + "] " + this.typeName + " - " + this.className + " - " + this.fieldName;	
		}
		return retVal;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
	
		if (object instanceof DynType) {
			
			DynType compare = (DynType) object;
			if ((this.oscsd==null) != (compare.oscsd==null)) {
				return false;
			} 
			if (this.oscsd!=null) {
				if (this.oscsd.equals(compare.oscsd)==false) {
					return false;
				}	
			}
			if (this.typeName.equals(compare.typeName)==false) {
				return false;
			}
			if (this.className.equals(compare.className)==false) {
				return false;
			}
			if (this.fieldName.equals(compare.fieldName)==false) {
				return false;
			}			
			return true;
			
		} else {
			return false;
		}
	}
	
	/**
	 * Provides the OntologySingleClassSlotDescription.
	 * @return the oSCSD
	 */
	public OntologySingleClassSlotDescription getOntologySingleClassSlotDescription() {
		return oscsd;
	}
	/**
	 * Sets the OntologySingleClassSlotDescription.
	 * @param curOSCSD the new oSCSD
	 */
	public void setOntologySingleClassSlotDescription(OntologySingleClassSlotDescription curOSCSD) {
		this.oscsd = curOSCSD;
	}

	/**
	 * Gets the class name.
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * Sets the class name.
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Gets the type name.
	 * @return the type name
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * Sets the type name.
	 * @param typeName the new type name
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Gets the field name.
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * Sets the field name.
	 * @param fieldName the new field name
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Sets the panel.
	 * @param panel the new panel
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	/**
	 * Gets the panel.
	 * @return the panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Gets the field display.
	 * @return the field display
	 */
	public JComponent getFieldDisplay() {
		return fieldDisplay;
	}
	/**
	 * Sets the field display.
	 * @param fieldValue the new field display
	 */
	public void setFieldDisplay(JComponent fieldValue) {
		this.fieldDisplay = fieldValue;
	}

	/**
	 * Checks if is class.
	 * @return true, if is class
	 */
	public boolean isClass(){
		return typeName.equalsIgnoreCase(typeClass);
	}
	/**
	 * Checks if is inner class type.
	 * @return true, if is inner class type
	 */
	public boolean isInnerClassType(){
		return typeName.equalsIgnoreCase(typeInnerClassType);
	}
	/**
	 * Checks if is raw type.
	 * @return true, if is raw type
	 */
	public boolean isRawType(){
		return typeName.equalsIgnoreCase(typeRawType);
	}

	/**
	 * Sets the visibility for the table view.
	 * @param visible the new visible in table view
	 */
	public void setVisibleInTableView(boolean visible) {
		this.visibleInTableView = visible;
	}
	/**
	 * Checks if is visible in table view.
	 * @return true, if is visible in table view
	 */
	public boolean isVisibleInTableView() {
		return visibleInTableView;
	}

	/**
	 * Returns, if exists the multiple (+|-) JButton multiple of the DynForm.
	 * @return the JButton multiple of the DynForm
	 */
	public JButton getJButtonMultipleOnDynFormPanel() {
		JButton jButtonMultipleOnDynForm = null;
		JPanel dynFormPanel = this.getPanel();
		if (dynFormPanel!=null) {
			for (int i=0; i < dynFormPanel.getComponentCount(); i++) {
				if (dynFormPanel.getComponent(i) instanceof JButton) {
					jButtonMultipleOnDynForm = (JButton) dynFormPanel.getComponent(i);
					break;
				}
			}	
		}
		return jButtonMultipleOnDynForm;
	}
	
}
