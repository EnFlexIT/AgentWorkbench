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
package agentgui.core.update;

import agentgui.core.update.MirrorTool.MirrorToolsJob;

/**
 * The listener interface for receiving mirror events.
 * The class that is interested in processing a mirror
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMirrorListener<code> method. When
 * the mirror event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MirrorTool
 */
public interface MirrorToolListener {

	/**
	 * On mirroring finalized will be invoked if a mirroring is done.
	 *
	 * @param job the job that was executed
	 * @param successful true, if the mirroring was successful
	 */
	public void onMirroringFinaliized(MirrorToolsJob job, boolean successful); 
	
	
}
