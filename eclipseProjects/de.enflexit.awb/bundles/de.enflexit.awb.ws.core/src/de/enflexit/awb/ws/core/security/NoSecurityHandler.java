package de.enflexit.awb.ws.core.security;

import org.eclipse.jetty.ee10.servlet.security.ConstraintSecurityHandler;

/**
 * The Class NoSecurityHandler does no security checks and is internally only used 
 * to replace a real (working) SecurtiyHandler by a non securing SecurityHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class NoSecurityHandler extends ConstraintSecurityHandler {

}
