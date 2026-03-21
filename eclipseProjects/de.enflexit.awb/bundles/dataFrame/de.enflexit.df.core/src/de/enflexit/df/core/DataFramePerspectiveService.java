package de.enflexit.df.core;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.ui.AwbMainWindowMenu;
import de.enflexit.awb.core.ui.AwbMainWindowToolBarGroup;
import de.enflexit.awb.core.ui.AwbPerspectiveService;
import de.enflexit.awb.core.ui.AwbProjectTab;
import de.enflexit.awb.core.ui.AwbUiConfiguration;
import de.enflexit.awb.core.ui.AwbUiExtension;

/**
 * The Class DataFramePerspectiveService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFramePerspectiveService implements AwbPerspectiveService {

	public static final String DATA_ANALYTICS_PERSPECTIVE_NAME = "Data Analytics Perspective";

	public AwbUiConfiguration uiConfiguration;
	public DataFrameMainWindowExtension dfMainWindowExtension;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPerspectiveService#getName()
	 */
	@Override
	public String getName() {
		return DATA_ANALYTICS_PERSPECTIVE_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPerspectiveService#getAwbUiConfiguration()
	 */
	@Override
	public AwbUiConfiguration getAwbUiConfiguration() {
		if (uiConfiguration==null) {
			uiConfiguration = new AwbUiConfiguration();
			
			uiConfiguration.setApplicationTitle(Application.getGlobalInfo().getApplicationTitle() + " - Data Analytics");
			
			uiConfiguration.getHiddenMenus().add(AwbMainWindowMenu.MenuJade);
			uiConfiguration.getHiddenMenus().add(AwbMainWindowMenu.MenuSimulation);
			
			uiConfiguration.getHiddenToolBarGroups().add(AwbMainWindowToolBarGroup.SetupHandling);
			
			uiConfiguration.getHiddenToolBarGroups().add(AwbMainWindowToolBarGroup.JadeControls);
			uiConfiguration.getHiddenToolBarGroups().add(AwbMainWindowToolBarGroup.MAS_Control);
			uiConfiguration.getHiddenToolBarGroups().add(AwbMainWindowToolBarGroup.MAS_Monitoring);
		
			
			uiConfiguration.setHideTabHeader(true);
			uiConfiguration.setHideProjectTree(false);
			
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Configuration_Agents);
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Configuration_Ontologies);
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Configuration_JADE_Settings);
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Configuration_JADE_Services);
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Configuration_Distribution_Thresholds);
			
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Configuration_Agent_Load_Metric);
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Setup);
			
			uiConfiguration.getHiddenProjectTabs().add(AwbProjectTab.Project_Desktop);
			
		}
		return uiConfiguration;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbPerspectiveService#getAwbUiExtension()
	 */
	@Override
	public AwbUiExtension getAwbUiExtension() {
		if (dfMainWindowExtension==null) {
			dfMainWindowExtension = new DataFrameMainWindowExtension();
		}
		return dfMainWindowExtension;
	}
	
}
