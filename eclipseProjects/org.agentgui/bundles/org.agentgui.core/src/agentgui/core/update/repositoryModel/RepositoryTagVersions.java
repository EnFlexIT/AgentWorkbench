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
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * The Class TagVersions is used to store different versions of an AWB project file
 * identified by the project ID and the versionTag of a {@link RepositoryEntry}.
 */
public class RepositoryTagVersions implements Serializable {

	private static final long serialVersionUID = 6487025081925973020L;

	@XmlElementWrapper(name = "RepositoryTagVector")
	@XmlElement(name = "RepositoryEntry")
	private Vector<RepositoryEntry> repositoryTagVector;
	
	
	/**
	 * Returns the repository entry vector.
	 * @return the repository entry vector
	 */
	public Vector<RepositoryEntry> getRepositoryTagVector() {
		if (repositoryTagVector==null) {
			repositoryTagVector = new Vector<>();
		}
		return repositoryTagVector;
	}
	/**
	 * Gets the repository vector sorted.
	 * @param ascending set true if the sorting should be don ascending, otherwise false
	 * @return the repository vector sorted
	 */
	public Vector<RepositoryEntry> getRepositoryTagVectorSorted(boolean ascending) {
		this.sortRepositoryTagVector(ascending);
		return this.getRepositoryTagVector();
	}
	/**
	 * Sorts the local repository vector.
	 * @param ascending set true if the sorting should be don ascending, otherwise false
	 */
	public void sortRepositoryTagVector(final boolean ascending) {
		Collections.sort(this.getRepositoryTagVector(), new Comparator<RepositoryEntry>() {
			@Override
			public int compare(RepositoryEntry re1, RepositoryEntry re2) {
				int comparator = 0;
				if (ascending==true) {
					comparator = re1.getOsgiFrameworkVersion().compareTo(re2.getOsgiFrameworkVersion());
				} else {
					comparator = re2.getOsgiFrameworkVersion().compareTo(re1.getOsgiFrameworkVersion());
				}
				return comparator;
			}
		});
	}
	
	
	/**
	 * Returns the latest version from the vector of RepositoryEntries.
	 * @return the latest version
	 */
	public RepositoryEntry getLatestVersion() {

		RepositoryEntry latestRE = null;
		for (int i = 0; i < this.getRepositoryTagVector().size(); i++) {
			// --- Get the version to check ---------------
			RepositoryEntry checkRE = this.getRepositoryTagVector().get(i);	
			if (latestRE==null) {
				latestRE = checkRE;
				continue;
			}
			if (checkRE.getOsgiFrameworkVersion().compareTo(latestRE.getOsgiFrameworkVersion())>0) {
				latestRE = checkRE;
			}
		}
		return latestRE;
	}
	
}
