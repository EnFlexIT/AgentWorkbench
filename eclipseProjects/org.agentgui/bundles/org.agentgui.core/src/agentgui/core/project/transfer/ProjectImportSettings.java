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
package agentgui.core.project.transfer;

import java.io.File;
import java.io.Serializable;

/**
 * The Class ProjectImportSettings is used as configuration instance
 * within the {@link ProjectImportController}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectImportSettings implements Serializable {

	private static final long serialVersionUID = -2616516163411105035L;

	private File projectArchiveFile;
	private boolean extractInThread;
	
	
	/**
	 * Instantiates a new project import settings (default constructor).
	 */
	public ProjectImportSettings() { }
	/**
	 * Instantiates a new project import settings.
	 * @param projectArchiveFile the project archive file
	 */
	public ProjectImportSettings(File projectArchiveFile) {
		this.setProjectArchiveFile(projectArchiveFile);
	}

	
	/**
	 * Returns the project archive file.
	 * @return the project archive file
	 */
	public File getProjectArchiveFile() {
		return this.projectArchiveFile;
	}
	/**
	 * Sets the project archive file.
	 * @param projectArchiveFile the new project archive file
	 */
	public void setProjectArchiveFile(File projectArchiveFile) {
		this.projectArchiveFile = projectArchiveFile;
	}
	
	/**
	 * Sets to execute the extraction process in an own thread or not.
	 * @param isExtractInThread the new extract in thread
	 */
	public void setExtractInThread(boolean isExtractInThread) {
		this.extractInThread = isExtractInThread;
	}
	/**
	 * Checks if is extract in thread.
	 * @return true, if is extract in thread
	 */
	public boolean isExtractInThread() {
		return extractInThread;
	}
	
}
