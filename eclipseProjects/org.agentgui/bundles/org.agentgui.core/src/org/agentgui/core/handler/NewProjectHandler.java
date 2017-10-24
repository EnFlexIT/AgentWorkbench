 
package org.agentgui.core.handler;

import org.eclipse.e4.core.di.annotations.Execute;

import agentgui.core.application.Application;

/**
 * Handler class for the New Project command
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class NewProjectHandler {
	@Execute
	public void execute() {
		Application.getProjectsLoaded().add(true);
	}
}