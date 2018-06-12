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

	/**
	 * Returns the {@link RepositoryTagVersions} for the specified tag.
	 *
	 * @param versionTag the version tag
	 * @return the repository tag versions
	 */
	public RepositoryTagVersions getRepositoryTagVersions(String versionTag) {
		return this.getRepositoryEntries().get(versionTag);
	}
	
}
