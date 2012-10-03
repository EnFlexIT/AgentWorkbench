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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import agentgui.core.application.Application;
import agentgui.simulationService.ontology.AgentGuiVersion;

/**
 * This class manages the version info of Agent.GUI, based on the file located in 
 * the package 'agentgui/version.properties'.
 * The information are accessible during runtime by using Application.Version
 * 
 * @see Application#getGlobalInfo()
 * @see GlobalInfo#getFileProperties()
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class VersionInfo extends Properties {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6896459166091752107L;

	/** The debug. */
	private boolean debug = false;
	
	/** The version file. */
	private String versionFile = "agentgui/version.properties";

	/**
	 * Constructor of this class.
	 */
	public VersionInfo() {
		super();
		this.readVersionInfo();
		this.printVersionInfo();
	}


	/**
	 * Loads the version information to this instance.
	 */
	private void readVersionInfo() {
		
		try {
			this.load(this.getClass().getClassLoader().getResourceAsStream(versionFile));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints out the current version and build information.
	 */
	public void printVersionInfo() {
		System.out.println(this.getFullVersionInfo(true, " ") + " on " + this.getJavaInfo() + "");
		if (debug==true) {
			System.out.println("Version.Mayor " + this.getVersionMajor());
			System.out.println("Version.Minor " + this.getVersionMinor());
			System.out.println("Version.Build " + this.getVersionBuild());
			System.out.println("Version.Date " + this.getVersionDate().toString());
		}
	}

	/**
	 * Returns the current java version and vendor.
	 */
	public String getJavaInfo() {
		String javaVersion = System.getProperty("java.version");
		String javaVendor = System.getProperty("java.vendor");
		String javaInfo = "Java " + javaVersion + " of " + javaVendor;
		return javaInfo;
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
		String dateString = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(this.getVersionDate());
		
		if (newLineString==null) {
			newLineString="";
		}
		
		if (includeApplicationTitle) {
			versionInfo += Application.getGlobalInfo().getApplicationTitle() + newLineString;	
		}

		versionInfo += this.getVersionMajor() + "." + this.getVersionMinor() + newLineString;
		versionInfo += "revision " + this.getVersionBuild() + " (" + dateString + ")"; 
		return versionInfo;
	}
	
	/**
	 * Provides the major version number.
	 *
	 * @return the version major
	 */
	public Integer getVersionMajor() {
		Integer version = Integer.valueOf(this.getProperty("version.major"));
		return version;
	}
	
	/**
	 * Provides the minor version number.
	 *
	 * @return the version minor
	 */
	public Integer getVersionMinor() {
		Integer version = Integer.valueOf(this.getProperty("version.minor"));
		return version;
	}
	
	/**
	 * Provides the build number of the version.
	 *
	 * @return the version build
	 */
	public Integer getVersionBuild() {
		Integer version = Integer.valueOf(this.getProperty("version.build"));
		return version;
	}
	
	/**
	 * Provides the date where this version was build as Date.
	 *
	 * @return the version date
	 */
	public Date getVersionDate() {
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyyMMddhhmm").parse(this.getProperty("version.date"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * Provides the date where this version was build as a String.
	 *
	 * @return the version date as string
	 */
	public String getVersionDateAsString() {
		return this.getProperty("version.date");
	}

	/**
	 * Checks if a foreign is up to date.
	 *
	 * @param foreignVersion the foreign version
	 * @return true, if is up to date
	 */
	public boolean isUpToDate(AgentGuiVersion foreignVersion) {
		return this.isUpToDate(foreignVersion.getMajorRevision(), foreignVersion.getMinorRevision(), foreignVersion.getBuildNo());
	}
	/**
	 * Checks if a foreign is up to date.
	 *
	 * @param foreignMajorRevision the major revision to compare
	 * @param foreignMinorRevision the minor revision to compare
	 * @param foreignBuild the build number to compare
	 * @return true, if is newer version
	 */
	public boolean isUpToDate(Integer foreignMajorRevision, Integer foreignMinorRevision, Integer foreignBuild) {
		
		if (this.getVersionMajor()>foreignMajorRevision) {
			return false;
		} else {
			if (this.getVersionMinor()>foreignMinorRevision) {
				return false;
			} else {
				if (this.getVersionBuild()>foreignBuild) {
					return false;
				}
			}
		}
		return true;
	}
	
}
