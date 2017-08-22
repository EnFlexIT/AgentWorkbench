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
package agentgui.envModel.graph.controller;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.envModel.graph.controller.BasicGraphGui.ToolBarSurrounding;
import agentgui.envModel.graph.controller.BasicGraphGui.ToolBarType;


/**
 * The Class CustomToolbarComponentDescription can be used in order to 
 * describe a customised JButton that is to be integrated into the toolbars
 * of the {@link BasicGraphGui}.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class CustomToolbarComponentDescription implements Serializable {

	private static final long serialVersionUID = -4520407479539600469L;

	private ToolBarType toolBarType;
	private ToolBarSurrounding toolBarSurrounding;
	private String customComponentClassName;
	private Integer indexPosition;
	private boolean addJSeparatorFirst;
	
	private transient AbstractCustomToolbarComponent customComponent;
	
	
	/**
	 * Instantiates a new CustomToolbarComponentDescription that describes a custom toolbar element for the {@link BasicGraphGui}.
	 * To build your actual component build a new class that extends the class {@link AbstractCustomToolbarComponent} and specify 
	 * it with the parameter customComponentClass
	 *
	 * @param toolBarType the tool bar type
	 * @param toolBarSurrounding the tool bar surrounding
	 * @param customComponentClass the actual class that extends the class {@link AbstractCustomToolbarComponent}
	 * 
	 * @see AbstractCustomToolbarComponent
	 * @see #getCustomComponentClass()
	 */
	public CustomToolbarComponentDescription(ToolBarType toolBarType, ToolBarSurrounding toolBarSurrounding, Class<? extends AbstractCustomToolbarComponent> customComponentClass) {
		if (toolBarType==ToolBarType.EditControl && toolBarSurrounding==ToolBarSurrounding.RuntimeOnly) {
			try {
				String errMsg = "A cutomized JComponent for the BasicGraphGui can not be defined as an edit control element at runtime!";
				throw new CustomToolbarComponentException(errMsg);
			} catch (CustomToolbarComponentException ex) {
				ex.printStackTrace();
			}
			return;
		}
		this.toolBarType = toolBarType;
		this.toolBarSurrounding = toolBarSurrounding;
		this.customComponentClassName = customComponentClass.getName();
	}
	
	/**
	 * Instantiates a new CustomToolbarComponentDescription that describes a custom toolbar element for the {@link BasicGraphGui}.
	 * To build your actual component build a new class that extends the class {@link AbstractCustomToolbarComponent} and specify 
	 * it with the parameter customComponentClass
	 *
	 * @param toolBarType the tool bar type
	 * @param toolBarSurrounding the tool bar surrounding
	 * @param customComponentClass the actual class that extends the class {@link AbstractCustomToolbarComponent}
	 * @param indexPosition the index position for the customised component (leave null if you just want to add at the end)
	 * @param addSeparatorFirst set true, if you want to add JSeparator first
	 * 
	 * @see AbstractCustomToolbarComponent
	 * @see #getCustomComponentClass()
	 */
	public CustomToolbarComponentDescription(ToolBarType toolBarType, ToolBarSurrounding toolBarSurrounding, Class<? extends AbstractCustomToolbarComponent> customComponentClass, Integer indexPosition, boolean addSeparatorFirst) {
		this(toolBarType, toolBarSurrounding, customComponentClass);
		if (indexPosition!=null && indexPosition<0) {
			try {
				String errMsg = "Index has to be null or equal or graeter than 0!";
				throw new CustomToolbarComponentException(errMsg);
			} catch (CustomToolbarComponentException ex) {
				ex.printStackTrace();
			}
			return;
		}
		this.indexPosition = indexPosition;
		this.addJSeparatorFirst = addSeparatorFirst;
	}

	
	/**
	 * Returns the tool bar type.
	 * @return the tool bar type
	 */
	public ToolBarType getToolBarType() {
		return toolBarType;
	}
	/**
	 * Sets the tool bar type.
	 * @param toolBarType the new tool bar type
	 */
	public void setToolBarType(ToolBarType toolBarType) {
		this.toolBarType = toolBarType;
	}
	
	/**
	 * Returns the tool bar surrounding.
	 * @return the tool bar surrounding
	 */
	public ToolBarSurrounding getToolBarSurrounding() {
		return toolBarSurrounding;
	}
	/**
	 * Sets the tool bar surrounding.
	 * @param toolBarSurrounding the new tool bar surrounding
	 */
	public void setToolBarSurrounding(ToolBarSurrounding toolBarSurrounding) {
		this.toolBarSurrounding = toolBarSurrounding;
	}
	
	/**
	 * Returns the class for the customised JButton for the {@link BasicGraphGui}.
	 * @return the class for the customised JButton 
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends AbstractCustomToolbarComponent> getCustomComponentClass() {
		
		Class<? extends AbstractCustomToolbarComponent> clazz = null;
		try {
			clazz = (Class<? extends AbstractCustomToolbarComponent>) ClassLoadServiceUtility.forName(this.customComponentClassName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clazz;
	}
	/**
	 * Sets he class for the customised component for the {@link BasicGraphGui}.
	 * @param newCustomComponentClass the new class for the customised Component
	 */
	public void setCustomComponentClass(Class<? extends AbstractCustomToolbarComponent> newCustomComponentClass) {
		this.customComponentClassName = newCustomComponentClass.getName();
	}
	
	/**
	 * Returns the index position for the JButton.
	 * @return the index position
	 */
	public Integer getIndexPosition() {
		return indexPosition;
	}
	/**
	 * Sets the index position for JButton.
	 * @param indexPosition the new index position
	 */
	public void setIndexPosition(Integer indexPosition) {
		this.indexPosition = indexPosition;
	}
	
	/**
	 * Checks if a subsequent JSeparator should be added after the component.
	 * @return true, if a subsequent JSeparator is to add
	 */
	public boolean isAddSeparatorFirst() {
		return addJSeparatorFirst;
	}
	/**
	 * Sets to add (or not) a subsequent JSeparator after the actual component.
	 * @param addSeparatorFirst the new adds the separator first
	 */
	public void setAddSeparatorFirst(boolean addSeparatorFirst) {
		this.addJSeparatorFirst = addSeparatorFirst;
	}
	

	/**
	 * Returns a copy of the current instance.
	 * @return the copy of the current CustomToolbarComponentDescription
	 */
	public CustomToolbarComponentDescription getCopy() {
		return new CustomToolbarComponentDescription(this.getToolBarType(), this.getToolBarSurrounding(), this.getCustomComponentClass(), this.getIndexPosition(), this.isAddSeparatorFirst());
	}
	
	
	/**
	 * Returns the instance of the custom tool bar component (extended {@link AbstractCustomToolbarComponent}).
	 *
	 * @param graphController the current {@link GraphEnvironmentController}
	 * @return the extended {@link AbstractCustomToolbarComponent} that can return the actual component
	 */
	public AbstractCustomToolbarComponent getToolBarComponent(GraphEnvironmentController graphController) {
		
		Class<? extends AbstractCustomToolbarComponent> customCompClass = this.getCustomComponentClass();
		if (customComponent==null && customCompClass!=null) {
			
			// --- Initiate this class --------------------------				
			try {
				customComponent = customCompClass.getDeclaredConstructor( new Class[] { graphController.getClass() }).newInstance( new Object[] { graphController });
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return customComponent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {

		if (compareObject==null) return false;
		if (!(compareObject instanceof CustomToolbarComponentDescription)) return false;
		
		// --- Compare the objects ------------------------
		CustomToolbarComponentDescription compObject = (CustomToolbarComponentDescription) compareObject;
		
		if (compObject.getToolBarType()!=this.getToolBarType()) return false;
		if (compObject.getToolBarSurrounding()!=this.getToolBarSurrounding()) return false;
		if (this.isEqualString(compObject.getCustomComponentClass().getName(), this.getCustomComponentClass().getName())==false) return false;
		if (compObject.getIndexPosition()!=this.getIndexPosition()) return false;
		if (compObject.isAddSeparatorFirst()!=this.isAddSeparatorFirst()) return false;
		
		return true;
	}
	/**
	 * Checks if is equal string setting.
	 *
	 * @param string1 the string 1
	 * @param string2 the string 2
	 * @return true, if is equal string
	 */
	private boolean isEqualString(String string1, String string2) {
		boolean isEqual = true;
		if (string1==null & string2==null) {
			isEqual = true;
		} else if (string1==null & string2!=null) {
			isEqual = false;
		} else if (string1!=null & string2==null) {
			isEqual = false;
		} else {
			isEqual = string1.equals(string2);
		}
		return isEqual;
	}
	
}
