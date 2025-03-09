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

/**
 * The Class HttpURLConnectorAuthorizationException.
 * 
 * @author Alexander Graute - SOFTEC - University of Duisburg - Essen
 */
public class ProjectRepositoryUpdateAuthorizationException extends ProjectRepositoryUpdateException {

	private static final long serialVersionUID = 4555792668841391604L;

	/**
	 * Instantiates a new project repository update authorization exception.
	 */
	public ProjectRepositoryUpdateAuthorizationException() {
		super();
	}

	/**
	 * Instantiates a new project repository update authorization exception.
	 *
	 * @param message the message
	 */
	public ProjectRepositoryUpdateAuthorizationException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new project repository update authorization exception.
	 *
	 * @param cause the cause
	 */
	public ProjectRepositoryUpdateAuthorizationException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Instantiates a new project repository update authorization exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ProjectRepositoryUpdateAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a new project repository update authorization exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public ProjectRepositoryUpdateAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
