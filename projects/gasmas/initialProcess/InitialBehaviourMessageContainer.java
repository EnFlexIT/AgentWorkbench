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
package gasmas.initialProcess;

import java.io.Serializable;

/**
 * The Class InitialBehaviourMessageContainer is used for organizatoric issues as a message class of the initial process
 * 
 * @author Benjamin Schwartz - University of Duisburg - Essen
 */
public class InitialBehaviourMessageContainer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1326135126924738223L;
	
	/** The data. */
	protected Object data;
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Instantiates a new initial behaviour message container.
	 *
	 * @param data the data
	 */
	public InitialBehaviourMessageContainer(Object data) {
		this.data = data;
	}
	
	
}

