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

import java.text.SimpleDateFormat;
import java.util.Date;

import agentgui.core.project.Project;

/**
 * The Class ProjectUpdater does what the name promises.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectUpdater extends Thread {

	private Project currProject; 
	
	/**
	 * Instantiates a new project updater.
	 * @param projectToUpdate the project to update
	 */
	public ProjectUpdater(Project projectToUpdate) {
		this.currProject = projectToUpdate;
		this.setName(this.getClass().getSimpleName() + this.currProject.getProjectName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		// -- TODO ------------
		
	}
	
	
	/**
	 * Return a version qualifier for a given time stamp.
	 * @param timeStamp the time stamp
	 * @return the version qualifier for time stamp
	 */
	public static String getVersionQualifierForTimeStamp(long timeStamp) {
		return getVersionQualifierForDate(new Date(timeStamp));
	}
	/**
	 * Return the version qualifier for the given time.
	 *
	 * @param currTime the curr time
	 * @return the version qualifier for time stamp
	 */
	public static String getVersionQualifierForDate(Date currTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
		return sdf.format(currTime);
	}
	
}
