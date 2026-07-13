package de.enflexit.awb.desktop.swt;

import jakarta.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * The Class MainMenuModelProcessor re-integrates the main
 * menu entry 'org.eclipse.ui.main.menu' to the e4 application model.
 * Thus, menu will not be disappeared, if the application is restarted.   
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ApplicationModelMainMenuProcessor {

	/** The Constant ID_TRIMMED_WINDOW. */
	private static final String ID_TRIMMED_WINDOW = "de.enflexit.awb.desktop.swt.trimmedwindow.agentworkbench";
	private static final String ID_MAIN_MENUE_ELEMENT = "org.eclipse.ui.main.menu";
	
	@Inject private MApplication application;
	@Inject private EModelService modelService;

	/**
	 * Executes the model processor.
	 */
	@Execute public void execute() {
		MTrimmedWindow window = (MTrimmedWindow) this.modelService.find(ID_TRIMMED_WINDOW, this.application);
		if (window==null) {
			System.err.println(this.getClass().getSimpleName() + ": Could not find Trimmed Window '" + ID_TRIMMED_WINDOW + "' - The menus could be missed.");
			return;
		} else if (window.getMainMenu()!=null) {
			if (window.getMainMenu().getElementId().equals(ID_MAIN_MENUE_ELEMENT)==false) {
				System.err.println(this.getClass().getSimpleName() + ": Main menu of window '" + ID_TRIMMED_WINDOW + "' was not '" + ID_MAIN_MENUE_ELEMENT + "'.");
			} else {
				return;
			}
		}
		window.setMainMenu(this.createEmptyMainMenu());
	}
	/**
	 * Creates the empty main menu.
	 * @return the m menu
	 */
	private MMenu createEmptyMainMenu() {
		final MMenu mainMenu = this.modelService.createModelElement(MMenu.class);
		mainMenu.setElementId(ID_MAIN_MENUE_ELEMENT);
		return mainMenu;
	}

}
