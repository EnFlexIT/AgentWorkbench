package de.enflexit.awb.ws.ui.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientConfiguration extends JPanel implements WsConfigurationInterface {
	
	private static final long serialVersionUID = 7987858783733542296L;
	
	private JPanelClientBundle panelClientBundle;
	private JPanelCredentials panelCredentials;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_panelClientBundle = new GridBagConstraints();
		gbc_panelClientBundle.insets = new Insets(10, 10, 10, 0);
		gbc_panelClientBundle.fill = GridBagConstraints.BOTH;
		gbc_panelClientBundle.gridx = 0;
		gbc_panelClientBundle.gridy = 0;
		add(getPanelClientBundle(), gbc_panelClientBundle);
		GridBagConstraints gbc_panelCredentials = new GridBagConstraints();
		gbc_panelCredentials.insets = new Insets(10, 0, 10, 10);
		gbc_panelCredentials.fill = GridBagConstraints.BOTH;
		gbc_panelCredentials.gridx = 2;
		gbc_panelCredentials.gridy = 0;
		add(getPanelCredentials(), gbc_panelCredentials);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private JPanelClientBundle getPanelClientBundle() {
		if (panelClientBundle == null) {
			panelClientBundle = new JPanelClientBundle();
		}
		return panelClientBundle;
	}
	private JPanelCredentials getPanelCredentials() {
		if (panelCredentials == null) {
			panelCredentials = new JPanelCredentials();
		}
		return panelCredentials;
	}
}
