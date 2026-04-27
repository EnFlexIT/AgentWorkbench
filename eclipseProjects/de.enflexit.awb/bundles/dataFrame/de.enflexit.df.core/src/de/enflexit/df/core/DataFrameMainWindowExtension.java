package de.enflexit.df.core;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.ui.JFrameDataViewer;

/**
 * The Class DataFrameMainWindowExtension.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameMainWindowExtension extends MainWindowExtension implements ActionListener {

	private JMenu jMenuDataAnalytics;
	
	private JMenuItem jMenuItemDataViews;
	private JMenuItem jMenuItemDataSettings;
	
	private JFrameDataViewer dvDialog;
	
	
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
			jMenuDataAnalytics.add(this.getJMenuItemDataViews());
			jMenuDataAnalytics.addSeparator();
			jMenuDataAnalytics.add(this.getJMenuItemDataSettings());
		}
		return jMenuDataAnalytics;
	}
	/**
	 * Return the JMenuItem data views.
	 * @return the j menu item data views
	 */
	private JMenuItem getJMenuItemDataViews() {
		if (jMenuItemDataViews==null) {
			jMenuItemDataViews = new JMenuItem("Data Viewer");
			jMenuItemDataViews.addActionListener(this);
		}
		return jMenuItemDataViews;
	}
	/**
	 * Return the JMenuItem data settings.
	 * @return the j menu item data settings
	 */
	private JMenuItem getJMenuItemDataSettings() {
		if (jMenuItemDataSettings==null) {
			jMenuItemDataSettings = new JMenuItem("Settings");
			jMenuItemDataSettings.addActionListener(this);
		}
		return jMenuItemDataSettings;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == this.getJMenuItemDataViews()) {
			this.showDataViewDialog();
			
		} else if (ae.getSource() == this.getJMenuItemDataSettings()) {
			
			
		}
		
	}
	
	/**
	 * Show data view dialog.
	 */
	private void showDataViewDialog() {
		
		if (dvDialog==null) {
			// --- Open the dialog, if not already open ---
			Window owner = OwnerDetection.getOwnerWindowForComponent(this.getJMenuItemDataViews());
			dvDialog = new JFrameDataViewer(owner);
			dvDialog.setVisible(true);
			dvDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent we) {
					DataFrameMainWindowExtension.this.dvDialog=null;
				}
			});
			
		} else {
			// --- Focus to dialog ------------------------
			dvDialog.toFront();
			dvDialog.requestFocus();
			
		}
	}
	
	
}
