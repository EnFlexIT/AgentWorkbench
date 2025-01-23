package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.agentgui.gui.swing.MainWindowExtension;

public class TimeSeriesDataProviderUiIntegration extends MainWindowExtension implements ActionListener {
	
private static final String ICON_PATH = "/icons/WeatherDataButton.png";
	
	private ImageIcon imageIcon;
	private JButton toolbarButton;
	private TimeSeriesDataProviderConfigurationDialog configurationDialog;

	@Override
	public void actionPerformed(ActionEvent e) {
		this.getConfigurationDialog().setVisible(true);
	}

	@Override
	public void initialize() {
		this.addToolbarComponent(this.getToolbarButton(), 8, SeparatorPosition.NoSeparator);
	}
	
	/**
	 * Gets the toolbar button.
	 * @return the toolbar button
	 */
	private JButton getToolbarButton() {
		if (toolbarButton==null) {
			toolbarButton = new JButton(this.getImageIcon());
			toolbarButton.setToolTipText("Configure global time series data soources.");
			toolbarButton.addActionListener(this);
		}
		return toolbarButton;
	}
	
	/**
	 * Gets the image icon.
	 * @return the image icon
	 */
	private ImageIcon getImageIcon() {
		if (imageIcon==null) {
			imageIcon = new ImageIcon(this.getClass().getResource(ICON_PATH));
		}
		return imageIcon;
	}
	
	/**
	 * Gets the configuration dialog.
	 * @return the configuration dialog
	 */
	private TimeSeriesDataProviderConfigurationDialog getConfigurationDialog() {
		if (configurationDialog==null) {
			configurationDialog = new TimeSeriesDataProviderConfigurationDialog();
		}
		return configurationDialog;
	}

}
