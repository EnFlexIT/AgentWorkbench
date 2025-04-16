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
package agentgui.simulationService.load.threading;

import agentgui.simulationService.load.LoadMeasureThread;

/**
 * The Interface ThreadProtocolReceiver can be used in order to register
 * as a receiver for the thread measurements of the {@link LoadMeasureThread}.
 * 
 * @see LoadMeasureThread
 * @see LoadMeasureThread#doThreadMeasurement(long, ThreadProtocolReceiver)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ThreadProtocolReceiver {

	/**
	 * Receives a thread measurement by a {@link ThreadProtocol}.
	 * @param threadProtocol the protocol the was determined
	 */
	public void receiveThreadProtocol(ThreadProtocol threadProtocol);
}
