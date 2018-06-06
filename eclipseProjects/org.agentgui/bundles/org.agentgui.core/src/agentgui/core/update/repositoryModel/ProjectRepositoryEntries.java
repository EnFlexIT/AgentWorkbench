package agentgui.core.update.repositoryModel;

import java.io.Serializable;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * The Class ProjectRepositoryEntries is used to describe different 
 * {@link ProjectRepositoryEntries} of a single project.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryEntries implements Serializable {

	private static final long serialVersionUID = -7440175985393423620L;
	
	@XmlElementWrapper(name = "ProjectRepositoryEntries")
	@XmlElement(name = "RepositoryEntry")
	private TreeMap<String, RepositoryTagVersions> repositoryEntries;

	/**
	 * Returns the TreeMap of repository entries, where the key represents
	 * the version tag, while the value is the corresponding RepositoryEntry.
	 * @return the repository entries
	 */
	public TreeMap<String, RepositoryTagVersions> getRepositoryEntries() {
		if (repositoryEntries==null) {
			repositoryEntries = new TreeMap<>();
		}
		return repositoryEntries;
	}
	
	/**
	 * Adds the specified repository entry to the repository entries available for the corresponding versionTag.
	 *
	 * @param repositoryEntry the repository entry
	 * @return the previous RepositoryEntry, or null if there was nothing found.
	 */
	public RepositoryEntry addRepositoryEntry(RepositoryEntry repositoryEntry) {
		
		RepositoryEntry oldEntry = null;
		// --- Get the tag versions -------------------
		RepositoryTagVersions tagVersions = this.getRepositoryEntries().get(repositoryEntry.getVersionTag());
		if (tagVersions==null) {
			tagVersions = new RepositoryTagVersions();
			this.getRepositoryEntries().put(repositoryEntry.getVersionTag(), tagVersions);
		}
		// -- Check if the entry was already there ----
		if (tagVersions.getRepositoryTagVector().contains(repositoryEntry)==true) {
			int idxOldVersion = tagVersions.getRepositoryTagVector().indexOf(repositoryEntry);
			oldEntry = tagVersions.getRepositoryTagVector().remove(idxOldVersion);
		}
		// --- Add the Repository Entry ---------------
		tagVersions.getRepositoryTagVector().add(repositoryEntry);
		
		return oldEntry;
	}
}
