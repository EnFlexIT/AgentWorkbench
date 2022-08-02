package de.enflexit.awb.ws.ui.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
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
	
	private JPanelClientBundle jPanelClientBundle;
	private JPanelCredentials jPanelCredentials;
	private JPanelAssignCredential jPanelAssignCredential;
	private JPanelAssignedCredentials jPanelAssignedCredentials;
	private JSplitPane jSplitPaneLeft;
	private JSplitPane jSplitPaneMiddleRight;
	private JPanel jPanelMiddle;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{230, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jSplitPaneLeft = new GridBagConstraints();
		gbc_jSplitPaneLeft.insets = new Insets(10, 10, 10, 10);
		gbc_jSplitPaneLeft.fill = GridBagConstraints.BOTH;
		gbc_jSplitPaneLeft.gridx = 0;
		gbc_jSplitPaneLeft.gridy = 0;
		add(getJSplitPaneLeft(), gbc_jSplitPaneLeft);
	}
	
	public JPanelClientBundle getJPanelClientBundle() {
		if (jPanelClientBundle == null) {
			jPanelClientBundle = new JPanelClientBundle();
			jPanelClientBundle.getJListApiRegistration().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ApiRegistration awbServices=jPanelClientBundle.getJListApiRegistration().getSelectedValue();
					jPanelAssignCredential.getJtextFieldClientBundle().setText(awbServices.getClientBundleName());;
				}
			});
		}
		return jPanelClientBundle;
	}

	public JPanelCredentials getJPanelCredentials() {
		if (jPanelCredentials == null) {
			jPanelCredentials = new JPanelCredentials();
			jPanelCredentials.getJListCredentials().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					AbstractCredential cred = jPanelCredentials.getJListCredentials().getSelectedValue();
					if (cred!=null) {
						getJPanelAssignCredential_1().getJtextFieldCredential().setText(cred.toString());
					}
				}
			});
		}		
		return jPanelCredentials;
	}

	private JPanelAssignCredential getJPanelAssignCredential_1() {
		if (jPanelAssignCredential == null) {
			jPanelAssignCredential = new JPanelAssignCredential();
		}
		return jPanelAssignCredential;
	}
	private JPanelAssignedCredentials getJPanelAssignedCredentials() {
		if (jPanelAssignedCredentials == null) {
			jPanelAssignedCredentials = new JPanelAssignedCredentials();
		}
		return jPanelAssignedCredentials;
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
	private JSplitPane getJSplitPaneLeft() {
		if (jSplitPaneLeft == null) {
			jSplitPaneLeft = new JSplitPane();
			jSplitPaneLeft.setResizeWeight(0.3);
			jSplitPaneLeft.setLeftComponent(this.getJPanelClientBundle());
			jSplitPaneLeft.setRightComponent(getJSplitPaneMiddleRight());
		}
		return jSplitPaneLeft;
	}

	private JSplitPane getJSplitPaneMiddleRight() {
		if (jSplitPaneMiddleRight == null) {
			jSplitPaneMiddleRight = new JSplitPane();
			jSplitPaneMiddleRight.setResizeWeight(0.5);
			jSplitPaneMiddleRight.setRightComponent(this.getJPanelCredentials());
			jSplitPaneMiddleRight.setLeftComponent(getJPanelMiddle());
		}
		return jSplitPaneMiddleRight;
	}
	private JPanel getJPanelMiddle() {
		if (jPanelMiddle == null) {
			jPanelMiddle = new JPanel();
			GridBagLayout gbl_jPanelMiddle = new GridBagLayout();
			gbl_jPanelMiddle.columnWidths = new int[]{0, 0};
			gbl_jPanelMiddle.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelMiddle.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelMiddle.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			jPanelMiddle.setLayout(gbl_jPanelMiddle);
			GridBagConstraints gbc_panelAssignCredential = new GridBagConstraints();
			gbc_panelAssignCredential.fill = GridBagConstraints.HORIZONTAL;
			gbc_panelAssignCredential.insets = new Insets(0, 0, 5, 0);
			gbc_panelAssignCredential.gridx = 0;
			gbc_panelAssignCredential.gridy = 0;
			jPanelMiddle.add(getJPanelAssignCredential_1(), gbc_panelAssignCredential);
			GridBagConstraints gbc_panelAssignedCredentials = new GridBagConstraints();
			gbc_panelAssignedCredentials.fill = GridBagConstraints.HORIZONTAL;
			gbc_panelAssignedCredentials.gridx = 0;
			gbc_panelAssignedCredentials.gridy = 1;
			jPanelMiddle.add(getJPanelAssignedCredentials(), gbc_panelAssignedCredentials);
		}
		return jPanelMiddle;
	}
}
