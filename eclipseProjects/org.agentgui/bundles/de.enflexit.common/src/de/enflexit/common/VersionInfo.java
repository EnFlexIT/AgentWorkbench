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
package de.enflexit.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * This class manages the local version info of the EOM, based on the file located in 
 * the package 'energy/version.properties'.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class VersionInfo {

	private boolean debug = false;
	
	private String plugInID;
	private String applicationTitle;
	
	private Version version;
	
	/**
	 * Instantiates a new version info.
	 * @param plugInID the plug in ID
	 * @param applicationTitle the application title
	 */
	public VersionInfo(String plugInID, String applicationTitle) {
		this.plugInID = plugInID;
		this.applicationTitle = applicationTitle;
		this.printVersionInfo();
	}

	/**
	 * Loads the version information to this instance.
	 * @return the version
	 */
	public Version getVersion() {
		if (version==null) {
			Bundle bundle = Platform.getBundle(this.plugInID);
			version = bundle.getVersion();
		}
		return version;
	}
	
	/**
	 * Prints out the current version and build information.
	 */
	public void printVersionInfo() {
		System.out.println(this.getVersionInfo());
		if (debug==true) {
			System.out.println("Version.Mayor " + this.getVersionMajor());
			System.out.println("Version.Minor " + this.getVersionMinor());
			System.out.println("Version.Micro " + this.getVersionMicro());
			System.out.println("Qualifier " + this.getVersion().getQualifier());
		}
	}

	/**
	 * Returns the version, build and Java information as String.
	 * @return the version info
	 */
	public String getVersionInfo() {
		return this.getFullVersionInfo(true, " ") + " on " + this.getJavaInfo() + ""; 
	}
	
	/**
	 * This method returns the full version information of Agent.GUI.
	 *
	 * @param includeApplicationTitle the include application title
	 * @param newLineString the new line string
	 * @return the full version info
	 */
	public String getFullVersionInfo(boolean includeApplicationTitle, String newLineString) {
		
		String versionInfo = "";
		String dateOrQualifier = this.getVersion().getQualifier(); 
		Date versionDate =  this.getVersionDate();
		if (versionDate!=null) {
			dateOrQualifier = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(this.getVersionDate());
		}
		
		if (newLineString==null) {
			newLineString="";
		}
		
		if (includeApplicationTitle) {
			versionInfo += this.applicationTitle + " " + newLineString;	
		}
		
		versionInfo += this.getVersionMajor() + "." + this.getVersionMinor() + "." + this.getVersionMicro() + newLineString;
		versionInfo += " (" + dateOrQualifier + ")"; 
		
		return versionInfo.replaceAll("  ", " ");
	}
	
	/**
	 * Provides the major version number.
	 * @return the version major
	 */
	public Integer getVersionMajor() {
		return this.getVersion().getMajor();
	}
	/**
	 * Provides the minor version number.
	 * @return the version minor
	 */
	public Integer getVersionMinor() {
		return this.getVersion().getMinor();
	}
	
	/**
	 * Provides the build number of the version.
	 * @return the version build
	 */
	public Integer getVersionMicro() {
		return this.getVersion().getMicro();
	}
	/**
	 * Provides the date where this version was build as Date.
	 * @return the version date
	 */
	public Date getVersionDate() {
		Date date=null;
		try {
			String qualifier = this.getVersion().getQualifier();
			date = new SimpleDateFormat("yyyyMMddHHmm").parse(qualifier);
		} catch (ParseException paEx) {
			if (this.debug) paEx.printStackTrace();
		}
		return date;
	}
	/**
	 * Provides the date where this version was build as a String.
	 * @return the version date as string
	 */
	public String getVersionDateAsString() {
		return this.getVersion().getQualifier();
	}

	/**
	 * Returns the current java version and vendor.
	 * @return the java info
	 */
	public String getJavaInfo() {
		String javaVersion = System.getProperty("java.version");
		String javaVendor = System.getProperty("java.vendor");
		String javaInfo = "Java " + javaVersion + " of " + javaVendor;
		return javaInfo;
	}
	
	/**
	 * Checks if a foreign is up to date.
	 *
	 * @param foreignMajorRevision the major revision to compare
	 * @param foreignMinorRevision the minor revision to compare
	 * @param foreignMicroRevision the micro revision number to compare
	 * @return true, if is newer version
	 */
	public boolean isUpToDate(Integer foreignMajorRevision, Integer foreignMinorRevision, Integer foreignMicroRevision) {
		
		if (this.getVersionMajor()>foreignMajorRevision) {
			return false;
		} else {
			if (this.getVersionMinor()>foreignMinorRevision) {
				return false;
			} else {
				if (this.getVersionMicro()>foreignMicroRevision) {
					return false;
				}
			}
		}
		return true;
	}
	
}
