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
package agentgui.core.jade;

import java.io.File;

import agentgui.core.application.Application;
import agentgui.core.project.Project;

/**
 * The Class ProjectFileManagerAgent simply extends the original JADE ProjectFileManagerAgent, but
 * allows to define and do some preliminary tasks within the agent and its thread.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectFileManagerAgent extends jade.misc.FileManagerAgent {

	private static final long serialVersionUID = 5145808575937759053L;

	/* (non-Javadoc)
	 * @see jade.misc.FileManagerAgent#setup()
	 */
	@Override
	protected void setup() {
	
		Project currProject = Application.getProjectFocused();
		if (currProject==null) {
			this.doDelete();
			return;
		}
		
		// --- Check if server directory exists or is empty ---------
		String serverDirPath = Application.getGlobalInfo().getResourceDistributionServerPath(false);
		File serverDir = new File(serverDirPath);
		if (serverDir.exists()==false || serverDir.listFiles().length==0) {
			this.doDelete();
			return;
		}
		
		// --- Set start arguments for the FileManager -------------- 
		Object[] fileMangerArgs = new Object[1];
		fileMangerArgs[0] = serverDirPath;
		this.setArguments(fileMangerArgs);
		super.setup();
	}
	
}
