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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.osgi.framework.Version;

/**
 * The Class ProjectRepository describes the structure of a project repository.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement
public class ProjectRepository implements Serializable {

	private static final long serialVersionUID = -1753468067006537985L;

	public static String REPOSITORY_FILE_NAME = "ProjectRepository.xml"; 
	
	private TreeMap<String, ProjectRepositoryEntries> projectRepositories;
	
	/**
	 * Return the project repositories, where the key represents the project 
	 * and the value the Tree the corresponding ProjectRepositoryEntries.
	 * @return the project repositories
	 */
	public TreeMap<String, ProjectRepositoryEntries> getProjectRepositories() {
		if (projectRepositories==null) {
			projectRepositories = new TreeMap<>();
		}
		return projectRepositories;
	}

	/**
	 * Returns the list of projects located in the repository.
	 * @return the repository project list
	 */
	public List<String> getRepositoryProjectList() {
		return new ArrayList<>(this.getProjectRepositories().keySet());
	}
	
	/**
	 * Returns the ProjectRepositoryEntries for the specified project.
	 *
	 * @param projectName the project name
	 * @return the project repository entries
	 */
	public ProjectRepositoryEntries getProjectRepositoryEntries(String projectName) {
		return this.getProjectRepositories().get(projectName);
	}
	
	/**
	 * Adds the repository entry.
	 *
	 * @param projectName the project name
	 * @param repositoryEntry the repository entry
	 */
	public void addRepositoryEntry(String projectName, RepositoryEntry repositoryEntry) {
		ProjectRepositoryEntries pre = this.getProjectRepositoryEntries(projectName);
		if (pre==null) {
			pre = new ProjectRepositoryEntries();
			this.getProjectRepositories().put(projectName, pre);
		}
		pre.addRepositoryEntry(repositoryEntry);
	}
	
	
	// ------------------------------------------------------------------------
	// --- Sub class ProjectRepositoryEntries ---------------------------------
	// ------------------------------------------------------------------------
	/**
	 * The Class ProjectRepositoryEntries is used to describe different 
	 * {@link ProjectRepositoryEntries} of a single project.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class ProjectRepositoryEntries implements Serializable {

		private static final long serialVersionUID = -7440175985393423620L;
		
		private TreeMap<String, RepositoryEntry> repositoryEntries;

		/**
		 * Returns the TreeMap of repository entries, where the key represents
		 * the version tag, while the value is the corresponding RepositoryEntry.
		 * @return the repository entries
		 */
		public TreeMap<String, RepositoryEntry> getRepositoryEntries() {
			if (repositoryEntries==null) {
				repositoryEntries = new TreeMap<>();
			}
			return repositoryEntries;
		}
		/**
		 * Adds the specified repository entry to the repository entries.
		 *
		 * @param repositoryEntry the repository entry
		 * @return the previous RepositoryEntry, or null if there was no mapping for the specified RepositoryEntry.
		 */
		public RepositoryEntry addRepositoryEntry(RepositoryEntry repositoryEntry) {
			return this.getRepositoryEntries().put(repositoryEntry.getVersionTag(), repositoryEntry);
		}
	}
	
	
	// ------------------------------------------------------------------------
	// --- Sub class RepositoryEntry ------------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * The Class RepositoryEntry is used as descriptor for single installable projects.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class RepositoryEntry implements Serializable {

		private static final long serialVersionUID = -2543921945180740987L;
		
		private String version;
		private String versionTag;
		private String fileName;
		
		/**
		 * Instantiates a new repository entry (default constructor).
		 */
		public RepositoryEntry() { }
		
		/**
		 * Instantiates a new repository entry.
		 *
		 * @param version the version as string
		 * @param versionTag the version tag
		 * @param fileName the file name
		 */
		public RepositoryEntry(String version, String versionTag, String fileName) { 
			this.setVersion(version);
			this.setVersionTag(versionTag);
			this.setFileName(fileName);
		}
		/**
		 * Instantiates a new repository entry.
		 *
		 * @param version the version as {@link Version}
		 * @param versionTag the version tag
		 * @param fileName the file name
		 */
		public RepositoryEntry(Version version, String versionTag, String fileName) { 
			this.setVersion(version);
			this.setVersionTag(versionTag);
			this.setFileName(fileName);
		}

		
		public Version getVersion() {
			return Version.parseVersion(this.version);
		}
		public void setVersion(Version version) {
			this.setVersion(version.toString());
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
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object compareObject) {
			
			if (compareObject==this) return true;
			if (! (compareObject instanceof RepositoryEntry)) return false;
			
			RepositoryEntry compareRE = (RepositoryEntry) compareObject;
			if (compareRE.getVersion().equals(this.getVersion())==true && 
				compareRE.getVersionTag().equals(this.getVersionTag())==true  && 
				compareRE.getFileName().equals(this.getFileName())==true ) {
				return true;
			}
			return false;
		}
	}
	

	// ------------------------------------------------------------------------
	// --- Methods for loading and saving a ProjectRepository -----------------  
	// ------------------------------------------------------------------------	
	/**
	 * Saves the current instance to the specified directory.
	 * @param localRepositoryDirectory the local repository directory
	 */
	public void save(File localRepositoryDirectory) {
	
		// --- Check for the right destination file ----------------- 
		String destinationFilePath = getLocationPathIncludingRepositoryFile(localRepositoryDirectory.getAbsolutePath(), false);
		File destinationFile = new File(destinationFilePath);
		
		Writer fileWriter = null;
		try {
			JAXBContext pc = JAXBContext.newInstance(this.getClass());
			Marshaller marshaller = pc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// --- Write values to xml-File -------------------------
			fileWriter = new FileWriter(destinationFile);
			marshaller.marshal(this, fileWriter);
			
		} catch (JAXBException | IOException jaxbEx) {
			jaxbEx.printStackTrace();
		} finally {
			if (fileWriter!=null) {
				try {
					fileWriter.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Loads a project repository from the specified URL.
	 *
	 * @param url the URL to load from
	 * @return the project repository
	 */
	public static ProjectRepository loadProjectRepository(URL webURL) {
		
		ProjectRepository projectRepository = null;
		
		// --- Check for the right destination file ----------------- 
		String sourceURLPath = getLocationPathIncludingRepositoryFile(webURL.getPath(), true);
		
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		try {
			URL sourceURL = new URL(sourceURLPath);
			// --- Define a HTTP request ----------------------------
			httpConnection = (HttpURLConnection) sourceURL.openConnection(); 
			httpConnection.addRequestProperty("User-Agent", "Mozilla/4.76"); 
			is = httpConnection.getInputStream();
			// --- Define JAXB stuff -------------------------------- 
			JAXBContext pc = JAXBContext.newInstance(ProjectRepository.class);
			Unmarshaller um = pc.createUnmarshaller();
			projectRepository = (ProjectRepository) um.unmarshal(is);
			
		} catch (IOException | JAXBException ex) {
			ex.printStackTrace();
		} finally {
			if (is!=null) {
				try {
					is.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				if (httpConnection!=null) {
					try {
						httpConnection.disconnect();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return projectRepository;
	}
	/**
	 * Loads a project repository from the specified URL.
	 *
	 * @param localRepositoryDirectory the local repository directory
	 * @return the project repository
	 */
	public static ProjectRepository loadProjectRepository(File localRepositoryDirectory) {
		
		ProjectRepository projectRepository = null;

		// --- Check for the right destination file ----------------- 
		String sourceFilePath = getLocationPathIncludingRepositoryFile(localRepositoryDirectory.getAbsolutePath(), false);
		File sourceFile = new File(sourceFilePath);

		FileReader fileReader = null;
		try {
			JAXBContext pc = JAXBContext.newInstance(ProjectRepository.class);
			Unmarshaller um = pc.createUnmarshaller();
			fileReader = new FileReader(sourceFile);
			projectRepository = (ProjectRepository) um.unmarshal(fileReader);

		} catch (FileNotFoundException | JAXBException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (fileReader!=null) fileReader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return projectRepository;
	}
	
	/**
	 * Returns the location path including the repository file if not already there.
	 *
	 * @param initialLocation the initial location
	 * @param isWebLink indicates if the specified initial location is a web link. if not a file is assumed
	 * @return the location including repository file
	 */
	private static String getLocationPathIncludingRepositoryFile(String initialLocation, boolean isWebLink) {
		String location = initialLocation;
		if (location.endsWith(REPOSITORY_FILE_NAME)==false) {
			String fileSeparator = File.separator;
			if (isWebLink==true)  fileSeparator = "/";
			if (location.endsWith(fileSeparator)==false) location+=fileSeparator; 
			location+=REPOSITORY_FILE_NAME;
		}
		return location; 
	}
	
}
