package de.enflexit.common.featureEvaluation;

import java.net.URI;
import java.util.List;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import de.enflexit.common.p2.P2OperationsHandler;

/**
 * This class stores the relevant information about a feature.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class FeatureInfo implements Comparable<FeatureInfo> {

	private String name;
	private String id;
	private String version;
	private URI repositoryURI;
	private String repositoryName;

		
	/**
	 * Creates a new {@link FeatureInfo} instance from the specified feature ID
	 *
	 * @param featureID the feature ID
	 * @return the feature info
	 */
	public static FeatureInfo createFeatureInfoFromFeatureID(String featureID) {
		
		if (featureID==null || featureID.isEmpty()==true) return null; 

		FeatureInfo featureInfo = null;
		try {
			
			List<IInstallableUnit> iuList = P2OperationsHandler.getInstance().getInstalledFeatures();
			for (int i = 0; i < iuList.size(); i++) {
				IInstallableUnit iu = iuList.get(i); 
				if (iu.getId().startsWith(featureID)==true) {
					featureInfo = createFeatureInfoFromIU(iu);
					break;
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return featureInfo;
	}
	
	/**
	 * Creates a new {@link FeatureInfo} instance from the provided {@link IInstallableUnit}.
	 *
	 * @param featureIU the {@link IInstallableUnit}
	 * @return the feature info
	 */
	public static FeatureInfo createFeatureInfoFromIU(IInstallableUnit featureIU) {
		
		FeatureInfo featureInfo = new FeatureInfo();

		featureInfo.setId(featureIU.getId());
		featureInfo.setName(featureIU.getProperty(IInstallableUnit.PROP_NAME));
		featureInfo.setVersion(featureIU.getVersion().toString());

		P2OperationsHandler p2handler = P2OperationsHandler.getInstance();
		featureInfo.setRepositoryURI(p2handler.getRepositoryForInstallableUnit(featureInfo.getId()));
		if (featureInfo.getRepositoryURI()!=null) {
			featureInfo.setRepositoryName(p2handler.getRepositoryName(featureInfo.getRepositoryURI()));
		}
		return featureInfo;
	}

	
	/**
	 * Returns the feature name.
	 * @return the feature name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the feature name.
	 * @param name the new feature name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the feature ID.
	 * @return the feature ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the feature ID.
	 * @param id the new feature ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the feature version.
	 * @return the feature version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * Sets the feature version.
	 * @param version the new feature version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the repository URI.
	 * @return the repository URI
	 */
	public URI getRepositoryURI() {
		return repositoryURI;
	}
	/**
	 * Sets the repository URI.
	 * @param repositoryURI the new repository URI
	 */
	public void setRepositoryURI(URI repositoryURI) {
		this.repositoryURI = repositoryURI;
	}

	/**
	 * Gets the repository name.
	 * @return the repository name
	 */
	public String getRepositoryName() {
		return repositoryName;
	}
	/**
	 * Sets the repository name.
	 * @param repositoryName the new repository name
	 */
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// --- Try to find the feature file info ---------- 
		String featureLabel = this.getName();
		if (featureLabel==null || featureLabel.equals("%featureName")==true) {
			featureLabel = this.getId();
		}
		return featureLabel + " [" + this.getId() + " " + this.getVersion() + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {

		if (compareObject==null) return false; 

		if (compareObject instanceof FeatureInfo) {
			// --- Compare the FeatureInfo objects --------
			FeatureInfo fiCompare = (FeatureInfo) compareObject;
			if (fiCompare.getId().equals(this.getId())==true && 
				fiCompare.getVersion().equals(this.getVersion())==true) return true; 
		} 
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(FeatureInfo o) {
		return this.toString().compareTo(o.toString());
	}

}
