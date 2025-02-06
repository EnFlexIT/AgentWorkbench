package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.agentgui.gui.swing.MainWindowExtension;

import agentgui.core.application.Application;
import agentgui.core.application.ApplicationListener;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;

/**
 * This class is responsible for the integration of the {@link TimeSeriesDataProvider} into the Agent.Workbench UI.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataProviderUiIntegration extends MainWindowExtension implements ActionListener, ApplicationListener {
	
private static final String ICON_PATH = "/icons/TSDProvider.png";
	
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
		Application.addApplicationListener(this);
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
			toolbarButton.setEnabled(this.isProjectLoaded());
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

	/* (non-Javadoc)
	 * @see agentgui.core.application.ApplicationListener#onApplicationEvent(agentgui.core.application.ApplicationListener.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		switch (event.getApplicationEvent()) {
		case ApplicationEvent.PROJECT_LOADED:
		case ApplicationEvent.PROJECT_CLOSED:
			this.getToolbarButton().setEnabled(this.isProjectLoaded());
		}
	}
	
	/**
	 * Checks if a project is currently loaded.
	 * @return true, if is project loaded
	 */
	private boolean isProjectLoaded() {
		return Application.getProjectFocused()!=null;
	}

}
