package de.enflexit.awb.core.project.transfer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlTransient;

import de.enflexit.awb.core.config.InstallationPackageFinder;
import de.enflexit.awb.core.config.InstallationPackageFinder.InstallationPackageDescription;
import de.enflexit.awb.core.project.Project;

/**
 * The Class ProjectExportSettings.
 * @author Nils Loose 
 */
public class ProjectExportSettings implements Serializable{
	
	private static final long serialVersionUID = -1048613565176959102L;

	private File targetFile;
	
	private boolean includeInstallationPackage;
	private transient InstallationPackageDescription installationPackage;
	private String targetOS;
	
	private boolean includeAuthorizationSettings = false;
	
	private List<String> simSetups;
	private List<String> fileExcludeListInternal;

	
	/**
	 * Returns the target file.
	 * @return the target path
	 */
	public File getTargetFile() {
		return targetFile;
	}
	/**
	 * Sets the target file.
	 * @param targetPath the new target path
	 */
	public void setTargetFile(File targetPath) {
		this.targetFile = targetPath;
	}
	
	/**
	 * Checks if is include product.
	 * @return true, if is include product
	 */
	public boolean isIncludeInstallationPackage() {
		return includeInstallationPackage;
	}
	/**
	 * Sets the include product.
	 * @param includeProduct the new include product
	 */
	public void setIncludeInstallationPackage(boolean includeProduct) {
		this.includeInstallationPackage = includeProduct;
	}
	
	/**
	 * Returns the installation package.
	 * @return the installation package
	 */
	@XmlTransient
	public InstallationPackageDescription getInstallationPackage() {
		if (installationPackage==null) {
			installationPackage = new InstallationPackageFinder().getInstallationPackageForOperatingSystem(this.getTargetOS());
		}
		return installationPackage;
	}
	/**
	 * Sets the installation package.
	 * @param installationPackage the new installation package
	 */
	public void setInstallationPackage(InstallationPackageDescription installationPackage) {
		this.installationPackage = installationPackage;
		if (installationPackage!=null) {
			this.setTargetOS(installationPackage.getOperatingSystem());
		} else {
			this.setTargetOS(null);
		}
	}
	
	/**
	 * Gets the target OS.
	 * @return the target OS
	 */
	public String getTargetOS() {
		return targetOS;
	}
	/**
	 * Sets the target OS.
	 * @param targetOS the new target OS
	 */
	public void setTargetOS(String targetOS) {
		this.targetOS = targetOS;
	}
	
	/**
	 * Checks if authorization settings should be included.
	 * @return true, if is include authorization settings
	 */
	public boolean isIncludeAuthorizationSettings() {
		return includeAuthorizationSettings;
	}
	/**
	 * Include authorization settings.
	 * @param includeAuthorizationSettings the new include authorization settings
	 */
	public void setIncludeAuthorizationSettings(boolean includeAuthorizationSettings) {
		this.includeAuthorizationSettings = includeAuthorizationSettings;
	}
	
	/**
	 * Returns the simulation setup names.
	 * @return the simulation setup names
	 */
	public List<String> getSimSetups() {
		if (simSetups==null) {
			simSetups = new ArrayList<>();
		}
		return simSetups;
	}
	/**
	 * Sets the simulation setup name to exports.
	 * @param simSetups the new simulation setup names 
	 */
	public void setSimSetups(List<String> simSetups) {
		this.simSetups = simSetups;
	}

	/**
	 * Gets the file exclude list internal.
	 * @return the file exclude list internal
	 */
	protected List<String> getFileExcludeListInternal() {
		 if (fileExcludeListInternal==null) {
			 fileExcludeListInternal = new ArrayList<>();
		 }
		return fileExcludeListInternal;
	}
	
	/**
	 * Gets a project export settings controller that is initialized with this settings instance.
	 * @return the project export settings controller
	 */
	public ProjectExportSettingsController getProjectExportSettingsController() {
		return this.getProjectExportSettingsController(null);
	}
	/**
	 * Gets the project export settings controller.
	 * @param project the project
	 * @return the project export settings controller
	 */
	public ProjectExportSettingsController getProjectExportSettingsController(Project project) {
		return new ProjectExportSettingsController(project, this, null);
	}

}
