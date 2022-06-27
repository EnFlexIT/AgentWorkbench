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
package agentgui.core.update.repositoryModel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.osgi.framework.Version;

import agentgui.core.project.Project;
import agentgui.core.update.ProjectRepositoryExport;


/**
 * The Class RepositoryEntry is used as descriptor for single installable projects.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlType(name = "RepositoryEntry", propOrder = {"projectID", "projectName", "projectDescription", "versionTag", "version", "fileName"})
public class RepositoryEntry implements Serializable {

	private static final long serialVersionUID = -2543921945180740987L;
	
	private String projectID;
	private String projectName;
	private String projectDescription;
	private String version;
	private String versionTag;
	private String fileName;
	
	/**
	 * Instantiates a new repository entry (default constructor).
	 */
	public RepositoryEntry() { }
	
	/**
	 * Instantiates a new repository entry based on the available {@link Project}.
	 * @param project the project for which a RepositoryEntry has to created 
	 */
	public RepositoryEntry(Project project) {
		this.setProjectID(project.getProjectFolder());
		this.setProjectName(project.getProjectName());
		this.setProjectDescription(project.getProjectDescription());
		this.setVersion(project.getVersion().toString());
		this.setVersionTag(project.getVersionTag());
		this.setFileName(ProjectRepositoryExport.getRepositoryFileName(project));
	}
	/**
	 * Instantiates a new repository entry.
	 *
	 * @param projectID the project ID
	 * @param projectName the project name
	 * @param projectDescriptioon the project descriptioon
	 * @param version the version as string
	 * @param versionTag the version tag
	 * @param fileName the file name
	 */
	public RepositoryEntry(String projectID, String projectName, String projectDescriptioon, String version, String versionTag, String fileName) { 
		this.setProjectID(projectID);
		this.setProjectName(projectName);
		this.setProjectDescription(projectDescriptioon);
		this.setVersion(version);
		this.setVersionTag(versionTag);
		this.setFileName(fileName);
	}
	/**
	 * Instantiates a new repository entry.
	 *
	 * @param projectID the project ID
	 * @param projectName the project name
	 * @param projectDescriptioon the project descriptioon
	 * @param version the version as {@link Version}
	 * @param versionTag the version tag
	 * @param fileName the file name
	 */
	public RepositoryEntry(String projectID, String projectName, String projectDescriptioon, Version version, String versionTag, String fileName) {
		this.setProjectID(projectID);
		this.setProjectName(projectName);
		this.setProjectDescription(projectDescriptioon);
		this.setOsgiFrameworkVersion(version);
		this.setVersionTag(versionTag);
		this.setFileName(fileName);
	}

	
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		if (projectID==null || projectID.isEmpty()) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The project ID is not allowed to be null.");
		}
		this.projectID = projectID;
	}
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		if (projectName==null || projectName.isEmpty()) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The project name is not allowed to be null.");
		}
		this.projectName = projectName;
	}
	
	
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	
	
	@XmlTransient
	public Version getOsgiFrameworkVersion() {
		return Version.parseVersion(this.getVersion());
	}
	public void setOsgiFrameworkVersion(Version version) {
		this.setVersion(version.toString());
	}
	

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		if (version==null || version.isEmpty()) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The version is not allowed to be null.");
		}
		this.version = version;
	}
	
	
	public String getVersionTag() {
		return versionTag;
	}
	public void setVersionTag(String versionTag) {
		if (versionTag==null || versionTag.isEmpty()) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The version tag is not allowed to be null.");
		}
		this.versionTag = versionTag;
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		if (fileName==null || fileName.isEmpty()) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The file name is not allowed to be null.");
		}
		this.fileName = fileName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Project: '" + this.getProjectName() + "', #" + this.getVersionTag() + ", Version: " + this.getVersion() + "";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==this) return true;
		if (! (compareObject instanceof RepositoryEntry)) return false;
		
		// --- Do comparison without project name and description --- 
		RepositoryEntry compareRE = (RepositoryEntry) compareObject;
		if (compareRE.getProjectID().equals(this.getProjectID())==true &&
			compareRE.getOsgiFrameworkVersion().equals(this.getOsgiFrameworkVersion())==true && 
			compareRE.getVersionTag().equals(this.getVersionTag())==true  && 
			compareRE.getFileName().equals(this.getFileName())==true ) {
			return true;
		}
		return false;
	}
	
}