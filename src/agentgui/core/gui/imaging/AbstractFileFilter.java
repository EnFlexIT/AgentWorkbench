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
package agentgui.core.gui.imaging;

import javax.swing.filechooser.FileFilter;

/**
 * The Class ImageFilter that allows to write an easy FierFilter just
 * by setting the allowed file extensions to the constructor.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractFileFilter extends FileFilter {

	private String[] fileExtension = null;
	private String fileDescription = null;
	
	
	/**
	 * Instantiates a new file filter.
	 *
	 * @param allowedFileExtension the allowed file extension
	 * @param fileDescription the file description
	 */
	public AbstractFileFilter(String allowedFileExtension, String fileDescription) {
		super();
		String[] allowed = new String[1];
		allowed[0] = allowedFileExtension;
		this.fileExtension = allowed;
		this.fileDescription = fileDescription;
	}
	
	/**
	 * Instantiates a new file filter.
	 *
	 * @param allowedFileExtension the allowed file extension
	 * @param fileDescription the file description
	 */
	public AbstractFileFilter(String[] allowedFileExtension, String fileDescription) {
		super();
		this.fileExtension = allowedFileExtension;
		this.fileDescription = fileDescription;
	}
	
	/**
	 * Sets the file extension.
	 * @param fileExtension the new file extension
	 */
	public void setFileExtension(String[] fileExtension) {
		this.fileExtension = fileExtension;
	}
	/**
	 * Gets the current file extension.
	 * @return the file extension
	 */
	public String[] getFileExtension() {
		return fileExtension;
	}

	/**
	 * Sets the file description.
	 * @param fileDescription the new file description
	 */
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	/**
	 * Returns the current file description.
	 * @return the file description
	 */
	public String getFileDescription() {
		return fileDescription;
	}
	
	
	
}
