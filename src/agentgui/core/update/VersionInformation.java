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
package agentgui.core.update;

import java.io.Serializable;

import agentgui.core.config.VersionInfo;

/**
 * The Class VersionInformation contains the general information about the major, minor and the build number.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class VersionInformation implements Serializable {

	private static final long serialVersionUID = 802676604343473147L;

	private Integer majorRevision = 0;
	private Integer minorRevision = 0;
	private Integer build = 0;
	
	
	/**
	 * Checks if this version is an older version than the specified version.
	 *
	 * @param versionInformation the version information
	 * @return true, if this instance describes an older version
	 */
	public boolean isOlderVersion(VersionInformation versionInformation) {
		return this.isOlderVersion(versionInformation.getMajorRevision(), versionInformation.getMinorRevision(), versionInformation.getBuild());
	}
	/**
	 * Checks if this version is an older version than the specified version..
	 *
	 * @param majorRevision the major revision to compare
	 * @param minorRevision the minor revision to compare
	 * @param build the build number to compare
	 * @return true, if this instance describes an older version
	 */
	public boolean isOlderVersion(Integer majorRevision, Integer minorRevision, Integer build) {
		return ! this.isNewerVersion(majorRevision, minorRevision, build);
	}
	
	/**
	 * Checks if this version is a newer version than the specified version.
	 *
	 * @param versionInfo the local version info to compare
	 * @return true, if this instance describes a newer version
	 */
	public boolean isNewerVersion(VersionInfo versionInfo) {
		return this.isNewerVersion(versionInfo.getVersionMajor(), versionInfo.getVersionMinor(), versionInfo.getVersionBuild());
	}
	/**
	 * Checks if this version is a newer version than the specified version.
	 *
	 * @param versionInformation the local version info to compare
	 * @return true, if this instance describes a newer version
	 */
	public boolean isNewerVersion(VersionInformation versionInformation) {
		return this.isNewerVersion(versionInformation.getMajorRevision(), versionInformation.getMinorRevision(), versionInformation.getBuild());
	}
	/**
	 * Checks if this version is a newer version than the specified version..
	 *
	 * @param majorRevision the major revision to compare
	 * @param minorRevision the minor revision to compare
	 * @param build the build number to compare
	 * @return true, if this instance describes a newer version
	 */
	public boolean isNewerVersion(Integer majorRevision, Integer minorRevision, Integer build) {
		
		if (this.majorRevision>majorRevision) {
			return true;
		} else if (this.majorRevision==majorRevision) {
			if (this.minorRevision>minorRevision) {
				return true;
			} else if (this.minorRevision==minorRevision) {
				if (this.build>build) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * Gets the major revision.
	 * @return the majorRevision
	 */
	public Integer getMajorRevision() {
		return majorRevision;
	}
	
	/**
	 * Sets the major revision.
	 * @param majorRevision the majorRevision to set
	 */
	public void setMajorRevision(Integer majorRevision) {
		this.majorRevision = majorRevision;
	}
	
	/**
	 * Gets the minor revision.
	 * @return the minorRevision
	 */
	public Integer getMinorRevision() {
		return minorRevision;
	}
	/**
	 * Sets the minor revision.
	 * @param minorRevision the minorRevision to set
	 */
	public void setMinorRevision(Integer minorRevision) {
		this.minorRevision = minorRevision;
	}
	
	/**
	 * Gets the builds the.
	 * @return the build
	 */
	public Integer getBuild() {
		return build;
	}
	/**
	 * Sets the builds the.
	 * @param build the build to set
	 */
	public void setBuild(Integer build) {
		this.build = build;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.majorRevision==null || this.minorRevision==null || this.build==null) {
			return "Error version " + super.toString();
		} 
		return this.majorRevision + "." + this.minorRevision + " revision " + this.build; 
	}
	/**
	 * Returns a copy of the current instance.
	 * @return the copy of the current instance
	 */
	public VersionInformation getCopy() {
		VersionInformation copy = new VersionInformation();
		copy.setMajorRevision(new Integer(this.majorRevision));
		copy.setMinorRevision(new Integer(this.minorRevision));
		copy.setBuild(new Integer(this.build));
		return copy;
	}
	
}
