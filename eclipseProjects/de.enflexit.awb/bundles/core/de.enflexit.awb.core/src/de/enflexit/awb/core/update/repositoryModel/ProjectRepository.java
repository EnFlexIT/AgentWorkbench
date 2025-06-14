package de.enflexit.awb.core.update.repositoryModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.osgi.framework.Version;

import de.enflexit.awb.core.project.Project;
import de.enflexit.common.http.HttpURLConnector;
import de.enflexit.common.http.HttpURLConnectorException;
import de.enflexit.common.http.WebResourcesAuthorization;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * The Class ProjectRepository describes the structure of a project repository.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement(name ="awbProjectsRepository")
public class ProjectRepository implements Serializable {

	private static final long serialVersionUID = -1753468067006537985L;

	public static String REPOSITORY_FILE_NAME = "awbProjectsRepository.xml"; 
	
	private transient boolean webRepository;
	
	@XmlElementWrapper(name = "ProjectRepositories")
	@XmlElement(name = "ProjectRepository")
	private TreeMap<String, ProjectRepositoryEntries> projectRepositories;
	
	/**
	 * Return the project repositories, where the key represents the project ID
	 * and the value the corresponding ProjectRepositoryEntries.
	 * @return the project repositories
	 */
	public TreeMap<String, ProjectRepositoryEntries> getProjectRepositories() {
		if (projectRepositories==null) {
			projectRepositories = new TreeMap<>();
		}
		return projectRepositories;
	}

	/**
	 * Return if the current repository is a web repository.
	 * @return true, if is web repository
	 */
	@XmlTransient
	public boolean isWebRepository() {
		return webRepository;
	}
	/**
	 * Sets that the current repository is a web repository or not.
	 * @param webRepository the new web repository
	 */
	public void setWebRepository(boolean webRepository) {
		this.webRepository = webRepository;
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
	 * @param projectID the project ID
	 * @return the project repository entries
	 */
	public ProjectRepositoryEntries getProjectRepositoryEntries(String projectID) {
		return this.getProjectRepositories().get(projectID);
	}
	
	/**
	 * Adds the specified RepositoryEntry.
	 * @param repositoryEntry the repository entry
	 */
	public void addRepositoryEntry(RepositoryEntry repositoryEntry) {
		ProjectRepositoryEntries pre = this.getProjectRepositoryEntries(repositoryEntry.getProjectID());
		if (pre==null) {
			pre = new ProjectRepositoryEntries();
			this.getProjectRepositories().put(repositoryEntry.getProjectID(), pre);
		}
		pre.addRepositoryEntry(repositoryEntry);
	}
	
	/**
	 * Returns the project update, if such an update can be found.
	 * @param project the project for which an update has to be found
	 * @return the project update
	 */
	public RepositoryEntry getProjectUpdate(Project project) {
		if (project==null) return null;
		return this.getProjectUpdate(project.getProjectFolder(), project.getVersionTag(), project.getVersion().toString());
	}
	
	/**
	 * Return the project update, if such an update can be found.
	 *
	 * @param projectID the project ID
	 * @param versionTag the version tag
	 * @param version the version
	 * @return the project update
	 */
	public RepositoryEntry getProjectUpdate(String projectID, String versionTag, String version) {
		
		ProjectRepositoryEntries prEntries = this.getProjectRepositoryEntries(projectID);
		if (prEntries!=null) {
			RepositoryTagVersions tagVersions = prEntries.getRepositoryTagVersions(versionTag);
			if (tagVersions!=null) {
				RepositoryEntry latestVersionEntry = tagVersions.getLatestVersion();
				if (latestVersionEntry!=null) {
					Version latestVersion = latestVersionEntry.getOsgiFrameworkVersion();
					Version currentVersion = Version.parseVersion(version);
					if (latestVersion.compareTo(currentVersion)>0) {
						return latestVersionEntry;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Return a complete repository file list.
	 *
	 * @return the repository file list
	 */
	public List<String> getRepositoryFileList() {
		return this.getRepositoryFileList(null);
	}
	/**
	 * Return a complete repository file list.
	 *
	 * @param baseFileReferenceOrLink the base file reference or link to the repository (may be null)
	 * @return the repository file list
	 */
	public List<String> getRepositoryFileList(String baseFileReferenceOrLink) {
	
		// --- Finally, define the source location ------------------
		String location = "";
		if (baseFileReferenceOrLink!=null  && baseFileReferenceOrLink.isEmpty()==false) {
			location = baseFileReferenceOrLink;
			String fileSeparator = File.separator;
			if (this.isWebRepository()==true)  fileSeparator = "/";
			if (location.endsWith(fileSeparator)==false) location += fileSeparator; 
		}
		
		// --- Define the file list ---------------------------------
		ArrayList<String> fileList = new ArrayList<>();

		// --- Read the list of projects ----------------------------
		ArrayList<String> projectIDs = new ArrayList<>(this.getProjectRepositories().keySet());
		for (int i = 0; i < projectIDs.size(); i++) {
			
			String projectID = projectIDs.get(i);
			ProjectRepositoryEntries per = this.getProjectRepositoryEntries(projectID);
			
			// --- Get all tags for that project --------------------
			ArrayList<String> tags = new ArrayList<>(per.getRepositoryEntries().keySet());
			for (int j = 0; j < tags.size(); j++) {

				String tag = tags.get(j);
				RepositoryTagVersions tagVersions = per.getRepositoryEntries().get(tag);
				// --- Get all repository entries -------------------
				Vector<RepositoryEntry> repoEntries = tagVersions.getRepositoryTagVectorSorted(true);
				for (int k = 0; k < repoEntries.size(); k++) {
					fileList.add(location + repoEntries.get(k).getFileName());	
				}
			}
		}
		return fileList;
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
	 * Loads a project repository from a file reference or a link.
	 *
	 * @param fileReferenceOrLink the file reference or link to the repository
	 * @param auth the WebResourcesAuthorization
	 * @return the project repository or null
	 * @throws HttpURLConnectorException the project repository update exception
	 */
	public static ProjectRepository loadProjectRepository(String fileReferenceOrLink, WebResourcesAuthorization auth) throws HttpURLConnectorException, IllegalArgumentException {
		
		if (fileReferenceOrLink==null || fileReferenceOrLink.isBlank()==true || fileReferenceOrLink.equals("?")) return null;
		
		ProjectRepository projectRepository = null;
		// --- Check if the update site is a web site URL -------
		try {
			
			URL updateURL = new URI(fileReferenceOrLink).toURL();
			
			projectRepository = ProjectRepository.loadProjectRepository(updateURL, auth);
			if (projectRepository!=null) {
				projectRepository.setWebRepository(true);
			}
			
		} catch (MalformedURLException | URISyntaxException urlEx) {
			urlEx.printStackTrace();
		}
		
		// --- Backup, if repository comes not from an URL ------
		if (projectRepository==null) {
			// --- Check if update site is a local directory ----
			File localRepo = new File(fileReferenceOrLink);
			if (localRepo.exists()==true) {
				projectRepository = ProjectRepository.loadProjectRepository(localRepo);
				if (projectRepository!=null) {
					projectRepository.setWebRepository(false);
				}
			}
		}
		return projectRepository;
	}
	
	/**
	 * Loads a project repository from the specified URL.
	 *
	 * @param webURL the web URL
	 * @param auth the WebResourcesAuthorization
	 * @return the project repository
	 * @throws HttpURLConnectorException the project repository update exception
	 */
	public static ProjectRepository loadProjectRepository(URL webURL, WebResourcesAuthorization auth) throws HttpURLConnectorException, IllegalArgumentException {
		
		if (webURL==null) return null;
		
		ProjectRepository projectRepository = null;
		
		// --- Check for the right destination file ----------------- 
		String sourceURLPath = getLocationPathIncludingRepositoryFile(webURL.toString(), true);
		
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		try {
			// --- Open Http connection ----------------------------
			httpConnection = HttpURLConnector.openConnection(sourceURLPath, auth); 
			is = httpConnection.getInputStream();

			// --- Define JAXB stuff -------------------------------- 
			JAXBContext pc = JAXBContext.newInstance(ProjectRepository.class);
			Unmarshaller um = pc.createUnmarshaller();
			projectRepository = (ProjectRepository) um.unmarshal(is);
			
		} catch (IOException | JAXBException | URISyntaxException ex) {
			System.err.println("[" + ProjectRepository.class.getSimpleName() + "] " + ex.getLocalizedMessage());
			//ex.printStackTrace();
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
	 * @return the project repository or null, if the file could not be found
	 */
	public static ProjectRepository loadProjectRepository(File localRepositoryDirectory) {
		
		if (localRepositoryDirectory==null) return null;
		
		ProjectRepository projectRepository = null;

		// --- Check for the right destination file ----------------- 
		String sourceFilePath = getLocationPathIncludingRepositoryFile(localRepositoryDirectory.getAbsolutePath(), false);
		File sourceFile = new File(sourceFilePath);
		if (sourceFile.exists()==false) return null;
		
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
	public static String getLocationPathIncludingRepositoryFile(String initialLocation, boolean isWebLink) {
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
