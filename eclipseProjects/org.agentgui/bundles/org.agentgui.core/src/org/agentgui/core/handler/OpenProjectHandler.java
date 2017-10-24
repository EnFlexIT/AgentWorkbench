 
package org.agentgui.core.handler;

import org.eclipse.e4.core.di.annotations.Execute;

import agentgui.core.application.Application;

/**
 * Handler class for the Open Project command
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class OpenProjectHandler {
	@Execute
	public void execute() {
		Application.getProjectsLoaded().add(false);
	}
		
}