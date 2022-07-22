package de.enflexit.awb.ws.ui.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.credential.AbstractCredential;
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
	private JPanelAssignCredential panelAssignCredential;
	private JPanelAssignedCredentials panelAssignedCredentials;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{230, 224, 230, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_panelClientBundle = new GridBagConstraints();
		gbc_panelClientBundle.gridheight = 2;
		gbc_panelClientBundle.insets = new Insets(10, 20, 10, 5);
		gbc_panelClientBundle.fill = GridBagConstraints.BOTH;
		gbc_panelClientBundle.gridx = 0;
		gbc_panelClientBundle.gridy = 0;
		add(getPanelClientBundle(), gbc_panelClientBundle);
		GridBagConstraints gbc_panelAssignCredential = new GridBagConstraints();
		gbc_panelAssignCredential.insets = new Insets(10, 0, 5, 5);
		gbc_panelAssignCredential.fill = GridBagConstraints.BOTH;
		gbc_panelAssignCredential.gridx = 1;
		gbc_panelAssignCredential.gridy = 0;
		add(getPanelAssignCredential_1(), gbc_panelAssignCredential);
		GridBagConstraints gbc_panelCredentials = new GridBagConstraints();
		gbc_panelCredentials.gridheight = 2;
		gbc_panelCredentials.insets = new Insets(10, 0, 10, 20);
		gbc_panelCredentials.fill = GridBagConstraints.BOTH;
		gbc_panelCredentials.gridx = 2;
		gbc_panelCredentials.gridy = 0;
		add(getPanelCredentials(), gbc_panelCredentials);
		GridBagConstraints gbc_panelAssignedCredentials = new GridBagConstraints();
		gbc_panelAssignedCredentials.insets = new Insets(0, 0, 5, 5);
		gbc_panelAssignedCredentials.fill = GridBagConstraints.BOTH;
		gbc_panelAssignedCredentials.gridx = 1;
		gbc_panelAssignedCredentials.gridy = 1;
		add(getPanelAssignedCredentials(), gbc_panelAssignedCredentials);
	}
	
	public JPanelClientBundle getPanelClientBundle() {
		if (panelClientBundle == null) {
			panelClientBundle = new JPanelClientBundle();
			panelClientBundle.getJListBundles().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ApiRegistration awbServices=panelClientBundle.getJListBundles().getSelectedValue();
					panelAssignCredential.getJtextFieldClientBundle().setText(awbServices.getClientBundleName());;
				}
			});
		}
		return panelClientBundle;
	}

	public JPanelCredentials getPanelCredentials() {
		if (panelCredentials == null) {
			panelCredentials = new JPanelCredentials();
			panelCredentials.getJListCredentials().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					AbstractCredential cred=panelCredentials.getJListCredentials().getSelectedValue();
					panelAssignCredential.getJtextFieldClientBundle().setText(cred.getName());;
				}
			});
		}		
		return panelCredentials;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#hasUnsavedChanges()
	 */
	@Override
	public boolean hasUnsavedChanges() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.WsConfigurationInterface#userConfirmedToChangeView()
	 */
	@Override
	public boolean userConfirmedToChangeView() {
		return false;
	}
	private JPanelAssignCredential getPanelAssignCredential_1() {
		if (panelAssignCredential == null) {
			panelAssignCredential = new JPanelAssignCredential();
			GridBagLayout gridBagLayout = (GridBagLayout) panelAssignCredential.getLayout();
			gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
			gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
			gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0};
			gridBagLayout.columnWidths = new int[]{0, 0, 0};
		}
		return panelAssignCredential;
	}
	private JPanelAssignedCredentials getPanelAssignedCredentials() {
		if (panelAssignedCredentials == null) {
			panelAssignedCredentials = new JPanelAssignedCredentials();
		}
		return panelAssignedCredentials;
	}
}
