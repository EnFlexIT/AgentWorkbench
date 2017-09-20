 
package org.agentgui.core.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbench;

/**
 * Handler class for the Exit command
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class ExitHandler {
	@Execute
	public void execute(IWorkbench workbench) {
		System.err.println("=> Gefunden !!");
		workbench.close();
	}
		
}