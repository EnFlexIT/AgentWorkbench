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
 * The Class TestStateMessage.
 */
public class TestStateMessage extends GraphUIStateMessage {

	private static final long serialVersionUID = -2691054613382139693L;

	
	/**
	 * Instantiates a new test state message.
	 * @param message the message
	 */
	public TestStateMessage(String message) {
		this.setMessage(message);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.messaging.GraphUIStateMessage#getMessage()
	 */
	@Override
	public String getMessage() {
		return null;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.messaging.GraphUIStateMessage#getVisualizationClass()
	 */
	@Override
	public Class<? extends GraphUIStateMessagePanel> getVisualizationClass() {
		return TestStateMessageVisualization.class;
	}

}
