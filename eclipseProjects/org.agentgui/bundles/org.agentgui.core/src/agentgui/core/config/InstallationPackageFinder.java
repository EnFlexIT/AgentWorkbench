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
package agentgui.core.config;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

/**
 * The Class InstallationPackageFinder can be used to evaluate a directory in order to.
 * find installation packages for Windows, Linux or Mac. Therefore, the class provides
 * several help functions.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class InstallationPackageFinder {

	private String searchDirectory;
	private File searchDirectoryFile;
	
	private File installationPackageWindows32;
	private File installationPackageWindows64;
	private File installationPackageLinux32;
	private File installationPackageLinux64;
	private File installationPackageMacOS;
	
	private String searchResultDescription;
	
	private Vector<InstallationPackageDescription> installationPackageVector;
	
	/**
	 * Instantiates a new installation package finder that uses the setting
	 * of the {@link BundleProperties}.
	 * 
	 * @see BundleProperties#DEF_PRODUCT_DIRECTORY_FOR_INSTALLATION_PACKAGES
	 */
	public InstallationPackageFinder() {
		this.setSearchDirectory(Application.getGlobalInfo().getStringFromConfiguration(BundleProperties.DEF_PRODUCT_DIRECTORY_FOR_INSTALLATION_PACKAGES, null));
	}
	/**
	 * Instantiates a new installation package finder that uses the specified search directory.
	 * @param searchDirectory the search directory
	 */
	public InstallationPackageFinder(String searchDirectory) {
		this.setSearchDirectory(searchDirectory);
	}
	
	/**
	 * Sets the search directory and starts the search for the installation packages.
	 * @param searchDirectory the new search directory
	 */
	public void setSearchDirectory(String searchDirectory) {
		
		String errNullSearchDirectory = Language.translate("Directory for installation packages is empty.", Language.EN);
		if (searchDirectory==null || searchDirectory.trim().isEmpty()) {
			this.setSearchResultDescription(errNullSearchDirectory);
			return;
		}
		
		if (this.searchDirectory==null || searchDirectory.equals(this.searchDirectory)==false) {
			this.setSearchResultDescription(null);
			this.searchDirectory = searchDirectory;
			this.getInstallationPackageVector().clear();
			this.startSearch();
		}
	}
	
	/**
	 * Starts the search for installation packages.
	 */
	private void startSearch() {
	
		// --- Check the specified path -------------------
		if (this.searchDirectory==null) {
			String errNoDirectoryDefined = Language.translate("No directory for the installation packages were defined.", Language.EN);
			this.setSearchResultDescription(errNoDirectoryDefined);
			return;
		}

		// --- Check if the search directory exists -------
		this.searchDirectoryFile = new File(this.searchDirectory);
		if (this.searchDirectoryFile.isDirectory()==false || this.searchDirectoryFile.exists()==false) {
			String errDirectoryNotFound = Language.translate("The specified directory could not be found.", Language.EN);
			this.setSearchResultDescription(errDirectoryNotFound);
			return;
		}
		
		// --- Start searching the required files ---------
		this.startInstallationPackageSearch();
	}

	/**
	 * Starts the search for the required installation packages.
	 */
	private void startInstallationPackageSearch() {
		
		if (this.searchDirectoryFile==null) {
			String errDirectoryNotDefine = Language.translate("No search directory is defined.", Language.EN);
			this.setSearchResultDescription(errDirectoryNotDefine);
			return;
		}
		
		// --- Filter for the required files --------------
		File[] searchResult =  this.searchDirectoryFile.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()==false && file.getName().startsWith("org.agentgui")) return true;
				return false;
			}
		});
		
		// --- Separate results for os and architecture ---
		String fileInfo = "";
		String packageDescription = Application.getGlobalInfo().getApplicationTitle();
		for (int i = 0; i < searchResult.length; i++) {
			
			File instPack = searchResult[i];
			String instPackName = instPack.getName();
			if (instPackName.contains("-win")) {
				// --- Found Windows package --------------
				if (instPackName.contains("x86_64")) {
					this.installationPackageWindows64 = instPack;
					fileInfo += Language.translate("Found installation package for Windows 64-Bit", Language.EN);
					fileInfo += " (" + instPackName + ")\n";
					this.getInstallationPackageVector().addElement(new InstallationPackageDescription(InstallationPackageDescription.OS_WINDOWS_64, instPack, packageDescription + " for Windows 64-Bit"));
				} else {
					this.installationPackageWindows32 = instPack;
					fileInfo += Language.translate("Found installation package for Windows 32-Bit", Language.EN);
					fileInfo += " (" + instPackName + ")\n";
					this.getInstallationPackageVector().addElement(new InstallationPackageDescription(InstallationPackageDescription.OS_WINDOWS_32, instPack, packageDescription + " for Windows 32-Bit"));
				}
				
			} else if (instPackName.contains("-linux")) {
				// --- Found Linux package ----------------
				if (instPackName.contains("x86_64")) {
					this.installationPackageLinux64 = instPack;
					fileInfo += Language.translate("Found installation package for Linux 64-Bit", Language.EN);
					fileInfo += " (" + instPackName + ")\n";
					this.getInstallationPackageVector().addElement(new InstallationPackageDescription(InstallationPackageDescription.OS_LINUX_64, instPack, packageDescription + " for Linux 64-Bit"));
				} else {
					this.installationPackageLinux32 = instPack;
					fileInfo += Language.translate("Found installation package for Linux 32-Bit", Language.EN);
					fileInfo += " (" + instPackName + ")\n";
					this.getInstallationPackageVector().addElement(new InstallationPackageDescription(InstallationPackageDescription.OS_LINUX_32, instPack, packageDescription + " for Linux 32-Bit"));
				}
				
			} else if (instPackName.contains("-mac")) {
				// --- Found MacOS package ----------------
				this.installationPackageMacOS = instPack;
				fileInfo += Language.translate("Found installation package for macOS", Language.EN);
				fileInfo += " (" + instPackName + ")\n";
				this.getInstallationPackageVector().addElement(new InstallationPackageDescription(InstallationPackageDescription.OS_MAC_OS, instPack, packageDescription + " for macOS"));
			}
		}
		
		if (fileInfo.isEmpty()) {
			fileInfo = fileInfo += Language.translate("No installation packages were found.", Language.EN);
		}
		this.setSearchResultDescription(fileInfo);
		
	}

	
	/**
	 * Returns the installation package windows 32.
	 * @return the installation package windows 32
	 */
	public File getInstallationPackageWindows32() {
		return installationPackageWindows32;
	}
	/**
	 * Returns the installation package windows 64.
	 * @return the installation package windows 64
	 */
	public File getInstallationPackageWindows64() {
		return installationPackageWindows64;
	}
	/**
	 * Returns the installation package Linux 32.
	 * @return the installation package Linux 32
	 */
	public File getInstallationPackageLinux32() {
		return installationPackageLinux32;
	}
	/**
	 * Returns the installation package Linux 64.
	 * @return the installation package Linux 64
	 */
	public File getInstallationPackageLinux64() {
		return installationPackageLinux64;
	}
	/**
	 * Returns the installation package mac OS.
	 * @return the installation package mac OS
	 */
	public File getInstallationPackageMacOS() {
		return installationPackageMacOS;
	}

	
	/**
	 * Sets the search result description.
	 * @param searchResultDescription the new search result description
	 */
	public void setSearchResultDescription(String searchResultDescription) {
		this.searchResultDescription = searchResultDescription;
	}
	/**
	 * Returns the search result description.
	 *
	 * @param asHTML set true, if the text should be encapsulated as HTML
	 * @return the search result description
	 */
	public String getSearchResultDescription(boolean asHTML) {
		if (asHTML==true) {
			return "<html>" + searchResultDescription.replace("\n", "<br>") + "</html>"; 
		}
		return searchResultDescription;
	}
	

	/**
	 * Returns the available installation packages as list model.
	 * @return the installation package list model
	 */
	public Vector<InstallationPackageDescription> getInstallationPackageVector() {
		if (installationPackageVector==null) {
			installationPackageVector = new Vector<>();
		}
		return installationPackageVector;
	}
	
	public InstallationPackageDescription getInstallationPackageForOperatingSystem(String operatingSystem) {
		for(InstallationPackageDescription installationPackage : installationPackageVector) {
			if(installationPackage.getOperatingSystem().equals(operatingSystem)) {
				return installationPackage;
			}
		}
		return null;
	}
	
	
	/**
	 * The Class InstallationPackageDescription can be used within list model.
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class InstallationPackageDescription {
		
		public static final String OS_WINDOWS_32 = "win32";
		public static final String OS_WINDOWS_64 = "win64";
		public static final String OS_LINUX_32 = "linux32";
		public static final String OS_LINUX_64 = "linux64";
		public static final String OS_MAC_OS = "macOs";
		
		
		private String operatingSystem;
		private File pacakgeFile;
		private String packageDescription;
		
		/**
		 * Instantiates a new installation package description.
		 *
		 * @param packageFile the package file
		 * @param packageDescription the description
		 */
		public InstallationPackageDescription(String operatingSystem, File packageFile, String packageDescription) {
			this.setOperatingSystem(operatingSystem);
			this.setPacakgeFile(packageFile);
			this.setPacakgeDescription(packageDescription);
		}
		
		public String getOperatingSystem() {
			return operatingSystem;
		}

		public void setOperatingSystem(String operatingSystem) {
			this.operatingSystem = operatingSystem;
		}

		public void setPacakgeFile(File pacakgeFile) {
			this.pacakgeFile = pacakgeFile;
		}
		public File getPacakgeFile() {
			return pacakgeFile;
		}
		
		public void setPacakgeDescription(String packageDescription) {
			this.packageDescription = packageDescription;
		}
		public String getPacakgeDescription() {
			return packageDescription;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.toString(true);
		}
		/**
		 * Extension of the regular .toString() method.
		 * @param includeFilename the include filename flag
		 * @return the description string for this instance
		 */
		public String toString(boolean includeFilename) {
			if (includeFilename==true) {
				return this.getPacakgeDescription() + " (" + this.getPacakgeFile().getName() + ")";
			}
			return this.getPacakgeDescription();
		}
		
		/**
		 * Checks if this installation package is for windows.
		 * @return true, if is for windows
		 */
		public boolean isForWindows() {
			return (operatingSystem.equals(OS_WINDOWS_32) || operatingSystem.equals(OS_WINDOWS_64));
		}
		/**
		 * Checks if this installation package is for Linux.
		 * @return true, if is for Linux
		 */
		public boolean isForLinux() {
			return (operatingSystem.equals(OS_LINUX_32) || operatingSystem.equals(OS_LINUX_64));
		}
		/**
		 * Checks if this installation package is for MAC.
		 * @return true, if is for MAC
		 */
		public boolean isForMac() {
			return (operatingSystem.equals(OS_MAC_OS));
		}
	}

	
}
