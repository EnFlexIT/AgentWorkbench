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

import agentgui.core.application.Application;
import agentgui.core.config.BundleProperties;
import agentgui.core.project.Project;
import agentgui.core.project.transfer.ProjectExportSettings;

/**
 * The Class ProjectRepositoryExport manages the export 
 * to the globally specified project repository directory.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryExport extends Thread {

	private Project currProject; 
	private String repositoryLocationPath;
	private ProjectExportSettings projectExportSettings;
	private RepositoryEntry repositoryEntry;
	
	/**
	 * Instantiates a new project updater.
	 * @param projectToUpdate the project to update
	 */
	public ProjectRepositoryExport(Project projectToUpdate) {
		this.currProject = projectToUpdate;
		this.setName(this.getClass().getSimpleName() + " " + this.currProject.getProjectName());
	}
	
	/**
	 * Returns the repository location path.
	 * @return the repository location path
	 */
	public String getRepositoryLocationPath() {
		if (repositoryLocationPath==null || repositoryLocationPath.isEmpty()==true) {
			// --- Try to get the globally configured location ------
			repositoryLocationPath = Application.getGlobalInfo().getStringFromConfiguration(BundleProperties.DEF_LOCAL_PROJECT_REPOSITORY, null);
		}
		return repositoryLocationPath;
	}
	/**
	 * Sets the repository location path.
	 * @param repositoryLocationPath the new repository location path
	 */
	public void setRepositoryLocationPath(String repositoryLocationPath) {
		this.repositoryLocationPath = repositoryLocationPath;
	}
	
	/**
	 * Returns the project export settings.
	 * @return the project export settings
	 */
	public ProjectExportSettings getProjectExportSettings() {
		return projectExportSettings;
	}
	/**
	 * Sets the project export settings.
	 * @param projectExportSettings the new project export settings
	 */
	public void setProjectExportSettings(ProjectExportSettings projectExportSettings) {
		this.projectExportSettings = projectExportSettings;
	}
	
	/**
	 * Returns the {@link RepositoryEntry}.
	 * @return the repository entry
	 */
	public RepositoryEntry getRepositoryEntry() {
		return repositoryEntry;
	}
	/**
	 * Sets the {@link RepositoryEntry}.
	 * @param repositoryEntry the new repository entry
	 */
	public void setRepositoryEntry(RepositoryEntry repositoryEntry) {
		this.repositoryEntry = repositoryEntry;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		// -- TODO ------------
		System.out.println("TODO - Project Export!");
		
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
