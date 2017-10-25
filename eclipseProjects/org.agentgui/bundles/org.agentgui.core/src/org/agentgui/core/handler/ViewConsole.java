 
package org.agentgui.core.handler;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler class for the About command
 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
 */
public class ViewConsole {

	@Execute public void execute(MApplication application, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell, EModelService modelService) {
		System.out.println((this.getClass().getSimpleName() + " called"));
	}
	
}