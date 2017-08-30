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

import javax.swing.JComponent;

/**
 * The Class AbstractCustomToolbarComponent can be extended in order to add customised.
 * components to a toolbar of the {@link BasicGraphGui}. To register this component, create 
 * a {@link CustomToolbarComponentDescription} and add this class in the current {@link GraphEnvironmentController}. 
 * 
 * @see CustomToolbarComponentDescription
 * @see GraphEnvironmentController#addCustomToolbarComponentDescription(CustomToolbarComponentDescription)
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class AbstractCustomToolbarComponent {

	protected GraphEnvironmentController graphController;
	private JComponent createdCustomComponent;
	
	/**
	 * Instantiates a new BasicGraphGuiCustomJBotton.
	 * @param graphController the current graph controller
	 */
	public AbstractCustomToolbarComponent(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	
	/**
	 * Gets the created custom component.
	 * @return the created custom component
	 */
	public JComponent getCreatedCustomComponent() {
		return createdCustomComponent;
	}
	/**
	 * Sets the created custom component.
	 * @param createdCustomComponent the new created custom component
	 */
	public void setCreatedCustomComponent(JComponent createdCustomComponent) {
		this.createdCustomComponent = createdCustomComponent;
	}

	/**
	 * Returns the actuaal custom component.
	 * @return the custom component
	 */
	public abstract JComponent getCustomComponent();

	
	
}
