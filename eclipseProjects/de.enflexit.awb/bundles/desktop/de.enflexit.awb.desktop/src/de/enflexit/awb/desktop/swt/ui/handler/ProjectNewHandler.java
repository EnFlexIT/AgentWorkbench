package de.enflexit.awb.desktop.swt.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

/**
 * Handler class for the New Project command
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ProjectNewHandler {
	@Execute
	public void execute(MApplication application, EPartService partService, EModelService modelService) {
//		Application.getProjectsLoaded().add(true, null, application, partService, modelService);
	}
}