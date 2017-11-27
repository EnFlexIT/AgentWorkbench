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

import java.net.URI;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;

import agentgui.core.common.CommonComponentFactory;
import de.enflexit.common.p2.P2OperationsHandler;

/**
 * This class stores the relevant information about a feature.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class FeatureInfo {

	private String featureName;
	private String featureID;
	private Version featureVersion;
	private URI repositoryURI;
	private String repositoryName;

	/**
	 * Creates a new {@link FeatureInfo} instance from the provided {@link IInstallableUnit}.
	 *
	 * @param featureIU the {@link IInstallableUnit}
	 * @param p2handler A {@link P2OperationsHandler} instance. If null, a new instance is created
	 * @return the feature info
	 */
	public static FeatureInfo createFeatureInfoFromIU(IInstallableUnit featureIU, P2OperationsHandler p2handler) {
		FeatureInfo featureInfo = new FeatureInfo();

		featureInfo.setFeatureID(featureIU.getId());
		featureInfo.setFeatureName(featureIU.getProperty(IInstallableUnit.PROP_NAME));
		featureInfo.setFeatureVersion(featureIU.getVersion());

		if (p2handler == null) {
			p2handler = CommonComponentFactory.getNewP2OperationsHandler();
		}

		featureInfo.setRepositoryURI(p2handler.getRepositoryForInstallableUnit(featureInfo.getFeatureID()));
		if (featureInfo.getRepositoryURI() != null) {
			featureInfo.setRepositoryName(p2handler.getRepositoryName(featureInfo.getRepositoryURI()));
		}

		return featureInfo;
	}

	/**
	 * Gets the feature name.
	 *
	 * @return the feature name
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * Sets the feature name.
	 *
	 * @param featureName the new feature name
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	/**
	 * Gets the feature ID.
	 *
	 * @return the feature ID
	 */
	public String getFeatureID() {
		return featureID;
	}

	/**
	 * Sets the feature ID.
	 *
	 * @param featureID the new feature ID
	 */
	public void setFeatureID(String featureID) {
		this.featureID = featureID;
	}

	/**
	 * Gets the feature version.
	 *
	 * @return the feature version
	 */
	public Version getFeatureVersion() {
		return featureVersion;
	}

	/**
	 * Sets the feature version.
	 *
	 * @param featureVersion the new feature version
	 */
	public void setFeatureVersion(Version featureVersion) {
		this.featureVersion = featureVersion;
	}

	/**
	 * Gets the repository URI.
	 *
	 * @return the repository URI
	 */
	public URI getRepositoryURI() {
		return repositoryURI;
	}

	/**
	 * Sets the repository URI.
	 *
	 * @param repositoryURI the new repository URI
	 */
	public void setRepositoryURI(URI repositoryURI) {
		this.repositoryURI = repositoryURI;
	}

	/**
	 * Gets the repository name.
	 *
	 * @return the repository name
	 */
	public String getRepositoryName() {
		return repositoryName;
	}

	/**
	 * Sets the repository name.
	 *
	 * @param repositoryName the new repository name
	 */
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.featureName.equals("%featureName")) {
			// --- Feature name not defined, return ID instead ------
			return this.featureID;
		} else {
			// --- Return the feature name --------------------------
			return this.featureName;
		}
	}

}
