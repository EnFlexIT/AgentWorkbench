package de.enflexit.df.core.plugin;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.plugins.PlugIn;
import de.enflexit.awb.core.ui.AwbProjectWindowTab;
import de.enflexit.awb.desktop.project.ProjectWindowTab;
import de.enflexit.df.core.ui.JPanelDataViewer;

/**
 * The Class DataFramePlugin.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFramePlugin extends PlugIn {

	private JPanelDataViewer jPanelDataViewer;
	
	/**
	 * Instantiates a new data frame plugin.
	 * @param currProject the curr project
	 */
	public DataFramePlugin(Project currProject) {
		super(currProject);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.project.plugins.AbstractPlugIn#getName()
	 */
	@Override
	public String getName() {
		return "Project-PlugIn of the Data Framework";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.project.plugins.AbstractPlugIn#onPlugIn()
	 */
	@Override
	public void onPlugIn() {
		this.addDataFrameworkTab();
		super.onPlugIn();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.project.plugins.AbstractPlugIn#onPlugOut()
	 */
	@Override
	public void onPlugOut() {
		super.onPlugOut();
		this.jPanelDataViewer = null;
	}
	/**
	 * Adds the data frame tab.
	 */
	private void addDataFrameworkTab() {
		
		ProjectWindowTab pwt = new ProjectWindowTab(this.project, AwbProjectWindowTab.DISPLAY_4_END_USER, "Data Framework", null, null, this.getJPanelDataViewer(), null);
		this.addProjectWindowTab(pwt, 2);
	}
	/**
	 * Returns the JPanelDataViewer.
	 * @return the j panel data viewer
	 */
	private JPanelDataViewer getJPanelDataViewer() {
		if (jPanelDataViewer==null) {
			jPanelDataViewer = new JPanelDataViewer();
		}
		return jPanelDataViewer;
	}
	
	
}
