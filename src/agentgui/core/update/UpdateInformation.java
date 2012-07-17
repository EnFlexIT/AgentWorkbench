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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class UpdateInformation.
 */
@XmlRootElement
public class UpdateInformation {

	private Integer majorRevision = null;
	private Integer minorRevision = null;
	private Integer build = null;

	private String downloadLink = null; 
	private String downloadFile = null;
	private Integer downloadSize = null;
	
	
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
	
	/**
	 * Gets the download link.
	 * @return the downloadLink
	 */
	public String getDownloadLink() {
		return downloadLink;
	}
	/**
	 * Sets the download link.
	 * @param downloadLink the downloadLink to set
	 */
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
	
	/**
	 * Gets the download file.
	 * @return the downloadFile
	 */
	public String getDownloadFile() {
		return downloadFile;
	}
	/**
	 * Sets the download file.
	 * @param downloadFile the downloadFile to set
	 */
	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

	/**
	 * Sets the download size.
	 * @param downloadSize the new download size
	 */
	public void setDownloadSize(Integer downloadSize) {
		this.downloadSize = downloadSize;
	}
	/**
	 * Gets the download size.
	 * @return the download size
	 */
	public Integer getDownloadSize() {
		return downloadSize;
	}
	
}
