package de.enflexit.df.core;

import javax.swing.JMenu;

import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;

/**
 * The Class DataFrameMainWindowExtension.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameMainWindowExtension extends MainWindowExtension {

	private JMenu jMenuDataAnalytics;
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.mainWindow.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {
	
		this.addJMenu(this.getJMenuDataAnalytics(), 2);
		
	}

	private JMenu getJMenuDataAnalytics() {
		if (jMenuDataAnalytics==null) {
			jMenuDataAnalytics = new JMenu("Data Analytics");
		}
		return jMenuDataAnalytics;
	}
	
	
}
