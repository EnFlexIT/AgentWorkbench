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
package agentgui.core.plugin;

import agentgui.core.project.Project;

/**
 * The Class PlugInError serves as an error instance in case that a PlugIn class can not be found or loaded.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PlugInError extends PlugIn {

	private Exception exception;
	
	/**
	 * Instantiates a new plug in error.
	 * @param currProject the current project instance
	 */
	public PlugInError(Project currProject) {
		super(currProject);
	}
	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#getName()
	 */
	@Override
	public String getName() {
		return "ERROR-PlugIn";
	}

	/**
	 * Sets the exception.
	 * @param ex the new exception
	 */
	public void setException(Exception ex) {
		exception = ex;
	}
	/**
	 * Returns the exception that was thrown during load operation.
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}
	
}
