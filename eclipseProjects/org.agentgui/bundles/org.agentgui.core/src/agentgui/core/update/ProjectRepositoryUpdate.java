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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import agentgui.core.project.Project;
import agentgui.core.update.repositoryModel.ProjectRepository;
import agentgui.core.update.repositoryModel.RepositoryEntry;

/**
 * The Class ProjectUpdater does what the name promises.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryUpdate extends Thread {

	private Project currProject; 
	private ProjectRepository projectRepository;
	
	/**
	 * Instantiates a new project updater.
	 * @param projectToUpdate the project to update
	 */
	public ProjectRepositoryUpdate(Project projectToUpdate) {
		this.currProject = projectToUpdate;
		this.setName(this.getClass().getSimpleName()  + " " + this.currProject.getProjectName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		// --- Check if the setting are complete --------------------
		String configError = this.getConfigurationError();
		if (configError!=null) {
			String errMsg = "[" + this.getClass().getSimpleName() + "] " + configError + " - Cancel update check.";
			throw new IllegalArgumentException(errMsg);
		}

		// -- Check for the configure update site -------------------
		if (this.currProject.getUpdateSite()==null) {
			System.err.println("No update-site was specified for the project '" + this.currProject.getProjectName() + "'!");
			return;
		}
		
		// --- Check if the repository can be loaded ----------------
		if (this.getProjectRepository()==null) return;
		
		// --- Check if an update is available ----------------------
		RepositoryEntry update = this.getProjectRepository().getProjectUpdate(this.currProject);
		if (update!=null) {
			// --- An update is available ---------------------------
			System.out.println("An Update is available => Do the update");
			
		}
		
	}

	/**
	 * Returns the project repository from the projects update site.
	 * @return the project repository
	 */
	public ProjectRepository getProjectRepository() {
		if (projectRepository==null && this.currProject.getUpdateSite()!=null) {
			// --- Check if the update site is a web site URL -------
			try {
				URL updateURL = new URL(this.currProject.getUpdateSite());
				projectRepository = ProjectRepository.loadProjectRepository(updateURL);
			} catch (MalformedURLException urlEx) {
				//urlEx.printStackTrace();
				this.printSystemError("URL access action says " + urlEx.getLocalizedMessage());
			}
			if (projectRepository==null) {
				// --- Check if update site is a local directory ----
				File localRepo = new File(this.currProject.getUpdateSite());
				if (localRepo.exists()==true) {
					projectRepository = ProjectRepository.loadProjectRepository(localRepo);
				}
			}
		}
		return projectRepository;
	}
	
	/**
	 * Return the configuration error as string, if there is an error.
	 * @return the configuration error
	 */
	private String getConfigurationError() {
		if (this.currProject==null) {
			return "No project was specified for an update!";
		}
		return null;
	}
	
	/**
	 * Prints the specified system error.
	 * @param message the message
	 */
	private void printSystemError(String message) {
		System.err.println("[" + this.getClass().getSimpleName() + "] " + message);
	}
	
}
