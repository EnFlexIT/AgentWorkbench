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
package agentgui.envModel.graph.controller.ui.messaging;

/**
 * The Class GraphUIStateMessage can be used to define and change the
 * visual state of the state panel within the Messaging widget {@link MessagingJInternalFrame}.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class GraphUIStateMessage extends GraphUIMessage {

	private static final long serialVersionUID = 3878309177802048286L;

	/**
	 * Has to return the class for the visualization of states.
	 * @return the visualization class
	 */
	public abstract Class<? extends GraphUIStateMessagePanel> getVisualizationClass();
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.messaging.GraphUIMessage#getMessage()
	 */
	@Override
	public abstract String getMessage();
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.messaging.GraphUIMessage#getMessageType()
	 */
	@Override
	public GraphUIMessageType getMessageType() {
		return GraphUIMessageType.Information;
	}
	
}
