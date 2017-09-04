 
package org.agentgui.core.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;

/**
 * Handler class for the Delete Project command
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class DeleteProjectHandler {
	@Execute
	public void execute() {
		//TODO Placeholder method, replace with actual implementation
		System.out.println((this.getClass().getSimpleName() + " called"));
	}
	
	
	@CanExecute
	public boolean canExecute() {
		//TODO Check if a project is loaded
		return true;
	}
		
}