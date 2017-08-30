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
package de.enflexit.oidc;

import de.enflexit.oidc.OIDCAuthorization.URLProcessor;

/**
 * Implement this interface e.g. anonymously on the fly to react on the availability (after login) of a resource protected by OIDC to realize a two-step process.
 * Pass it to @see {@link de.enflexit.oidc.OIDCAuthorization#setAvailabilityHandler(OIDCResourceAvailabilityHandler)}
 */
public interface OIDCResourceAvailabilityHandler {
	
	/**
	 * This method is called by the @see {@link de.enflexit.oidc.OIDCAuthorization#authorizeByUserAndPW(String, String)} method, as soon as the resource becomes available (directly or after login etc.). It has to be implemented by a descendant class.
	 *
	 * @param urlProcessor the URLProcessor which was used to establish the connection and from where the return values can be acquired.
	 */
	public void onResourceAvailable(URLProcessor urlProcessor);

	/**
	 * This method is called by the @see {@link de.enflexit.oidc.OIDCAuthorization#authorizeByUserAndPW(String, String)} method, if the resource is not available without authorization.
	 *
	 * @param oidcAuthorization the OIDCAuthorization object from where the return values can be acquired.
	 * @return standard handling defines, whether the authorization should be handled via the default method, i.e. showing a login panel
	 */
	public boolean onAuthorizationNecessary(OIDCAuthorization oidcAuthorization);
}
