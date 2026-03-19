package de.enflexit.awb.samples.perspective;

import de.enflexit.awb.core.ui.AwbMainWindowMenu;
import de.enflexit.awb.core.ui.AwbMainWindowToolBarGroup;
import de.enflexit.awb.core.ui.AwbPerspectiveService;
import de.enflexit.awb.core.ui.AwbProjectTab;
import de.enflexit.awb.core.ui.AwbUiConfiguration;
import de.enflexit.awb.core.ui.AwbUiExtension;


/**
 * The Class ExamplePerspectiveService.
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class ExamplePerspectiveService implements AwbPerspectiveService{
	
	public static final String SERVICE_NAME = "Example Perspective";
	private AwbUiConfiguration uiConfig;
	
	/* (non-Javadoc)
	* @see de.enflexit.awb.core.ui.perspective.AwbPerspectiveService#getName()
	*/
	@Override
	public String getName() {
		return SERVICE_NAME;
	}

	/* (non-Javadoc)
	* @see de.enflexit.awb.core.ui.perspective.AwbPerspectiveService#getAwbUiConfiguration()
	*/
	@Override
	public AwbUiConfiguration getAwbUiConfiguration() {
		if (uiConfig==null) {
			uiConfig = new AwbUiConfiguration();
			
			uiConfig.setApplicationTitle(SERVICE_NAME);
			
			uiConfig.getHiddenMenus().add(AwbMainWindowMenu.MenuJade);
			uiConfig.getHiddenMenus().add(AwbMainWindowMenu.MenuSimulation);
			
			uiConfig.getHiddenToolBarGroups().add(AwbMainWindowToolBarGroup.JadeControls);
			uiConfig.getHiddenToolBarGroups().add(AwbMainWindowToolBarGroup.MAS_Control);
			uiConfig.getHiddenToolBarGroups().add(AwbMainWindowToolBarGroup.MAS_Monitoring);
			
			uiConfig.getHiddenProjectTabs().add(AwbProjectTab.Configuration_Agent_Load_Metric);
			uiConfig.getHiddenProjectTabs().add(AwbProjectTab.Runtime_Visualisation);
			uiConfig.getHiddenProjectTabs().add(AwbProjectTab.Project_Desktop);
			
			
		}
		return uiConfig;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPerspectiveService#getAwbUiExtension()
	 */
	@Override
	public AwbUiExtension getAwbUiExtension() {
		return null;
	}

}
