package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import de.enflexit.awb.baseUI.ToolBarGroup;
import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.common.swing.AwbThemeImageIcon;

/**
 * This class is responsible for the integration of the {@link TimeSeriesDataProvider} into the Agent.Workbench UI.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataProviderUiIntegration extends MainWindowExtension implements ActionListener {
	
	private static final String ICON_PATH_LIGHT_MODE = "/icons/TimeSeriesChartBlack.png";
	private static final String ICON_PATH_DARK_MODE = "/icons/TimeSeriesChartGrey.png";
	
	private AwbThemeImageIcon imageIcon;
	private JButton toolbarButton;
	private TimeSeriesDataProviderConfigurationDialog configurationDialog;

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.getConfigurationDialog().setVisible(true);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.mainWindow.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {
		this.addToolbarComponent(this.getToolbarButton(), null, false, ToolBarGroup.ExtraTools);
	}
	
	/**
	 * Gets the toolbar button.
	 * @return the toolbar button
	 */
	private JButton getToolbarButton() {
		if (toolbarButton==null) {
			toolbarButton = new JButton(this.getImageIcon());
			toolbarButton.setToolTipText("Configure global time series data sources.");
			toolbarButton.addActionListener(this);
		}
		return toolbarButton;
	}
	
	/**
	 * Gets the image icon.
	 * @return the image icon
	 */
	private AwbThemeImageIcon getImageIcon() {
		if (imageIcon==null) {
			ImageIcon lightModeIcon = new ImageIcon(this.getClass().getResource(ICON_PATH_LIGHT_MODE));
			ImageIcon darkModeIcon = new ImageIcon(this.getClass().getResource(ICON_PATH_DARK_MODE));
			imageIcon = new AwbThemeImageIcon(lightModeIcon, darkModeIcon);
		}
		return imageIcon;
	}
	
	/**
	 * Gets the configuration dialog.
	 * @return the configuration dialog
	 */
	private TimeSeriesDataProviderConfigurationDialog getConfigurationDialog() {
		if (configurationDialog==null) {
			if (Application.getMainWindow()!=null) {
				configurationDialog = new TimeSeriesDataProviderConfigurationDialog((Window) Application.getMainWindow());
			} else {
				configurationDialog = new TimeSeriesDataProviderConfigurationDialog(null);
			}
		}
		return configurationDialog;
	}

}
