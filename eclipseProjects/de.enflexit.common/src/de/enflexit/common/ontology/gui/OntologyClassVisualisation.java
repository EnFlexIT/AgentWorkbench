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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * The Class OntologyClassVisualisation can be used in order to name a 
 * class out of an Ontology that can be configured with customized 
 * widgets or dialog.<br>
 * In order to register your own OntologyClassVisualisation use the register method
 * in the GlobalInfo of the Application (see links below).
 * 
 * @see GlobalInfo#registerOntologyClassVisualisation(String)
 * @see GlobalInfo#getKnownOntologyClassVisualisations()
 * @see GlobalInfo#isOntologyClassVisualisation(Class)
 * @see GlobalInfo#getOntologyClassVisualisation(Class)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class OntologyClassVisualisation {

	
	/**
	 * Returns the class out of an ontology that can be visualized .
	 * @return the ontology class
	 */
	public abstract Class<?> getOntologyClass();

	/**
	 * Returns the class of the widget that extends a OntologyClassWidget that extends a JPanel.<br>
	 * Should be a small JPanel with a dimension of approx width=315 and height=250. 
	 * @return the widget class
	 */
	public abstract Class<? extends OntologyClassWidget> getWidgetClass();

	/**
	 * Returns the OntologyClassEditorJPanel class that extends JPanel.
	 * @return the editor OntologyClassEditorJPanel class
	 */
	public abstract Class<? extends OntologyClassEditorJPanel> getEditorJPanelClass();
	
		
	
	/**
	 * Returns the widget instance from the configured class .
	 *
	 * @param dynForm the current DynForm
	 * @param startArgIndex the index of the start argument 
	 * @return the OntologyClassWidget
	 */
	public OntologyClassWidget getWidget(DynForm dynForm, int startArgIndex) {

		OntologyClassWidget widget = null;
		Class<? extends OntologyClassWidget> widgetClass = this.getWidgetClass();
		if (widgetClass!=null) {
			
			try {
				Class<?>[] conParameter = new Class[2];
				conParameter[0] = DynForm.class;
				conParameter[1] = int.class;
				
				// --- Get the constructor ------------------------------	
				Constructor<?> widgetConstructor = widgetClass.getConstructor(conParameter);
	
				// --- Define the argument for the newInstance call ----- 
				Object[] args = new Object[2];
				args[0] = dynForm;
				args[1] = startArgIndex;
				
				widget = (OntologyClassWidget) widgetConstructor.newInstance(args);
				widget.invokeSetOntologyClassInstance(this.getOntologyClassInstance(dynForm, startArgIndex));

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		return widget;
	}
	/**
	 * Returns the Editor-JPanel for instance of the configured class .
	 * 
	 * @param dynForm the current DynForm
	 * @param startArgIndex the index of the start argument 
	 * @return the OntologyClassEditorJPanel
	 */
	public OntologyClassEditorJPanel getEditorJPanel(DynForm dynForm, int startArgIndex) {
		
		OntologyClassEditorJPanel editor = null;
		Class<? extends OntologyClassEditorJPanel> editorClass = this.getEditorJPanelClass();
		if (editorClass!=null) {

			try {
				Class<?>[] conParameter = new Class[2];
				conParameter[0] = DynForm.class;
				conParameter[1] = int.class;
				
				// --- Get the constructor ------------------------------	
				Constructor<?> editorConstructor = editorClass.getConstructor(conParameter);
	
				// --- Define the argument for the newInstance call ----- 
				Object[] args = new Object[2];
				args[0] = dynForm;
				args[1] = startArgIndex;
				
				editor = (OntologyClassEditorJPanel) editorConstructor.newInstance(args);
				editor.invokeSetOntologyClassInstance(this.getOntologyClassInstance(dynForm, startArgIndex));
				
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		return editor;
	}
		
	/**
	 * Returns the concrete ontology class instance.
	 *
	 * @param dynForm the current DynForm
	 * @param startArgIndex the start argument index
	 * @return the instance of the corresponding ontology class 
	 */
	public Object getOntologyClassInstance(DynForm dynForm, int startArgIndex) {
		Object ontologyClassInstance = null;
		Object[] startArgs = dynForm.getOntoArgsInstance();
		if (startArgs!=null) {
			ontologyClassInstance =  startArgs[startArgIndex];
		}
		return ontologyClassInstance;
	}
	
}
