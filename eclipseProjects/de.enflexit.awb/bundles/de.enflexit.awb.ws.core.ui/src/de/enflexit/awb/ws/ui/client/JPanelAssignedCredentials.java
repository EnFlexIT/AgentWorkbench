package de.enflexit.awb.ws.ui.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.enflexit.awb.ws.client.ApiRegistration;
import de.enflexit.awb.ws.client.CredentialAssignment;
import de.enflexit.awb.ws.client.WsCredentialStore;
import de.enflexit.awb.ws.credential.AbstractCredential;

public class JPanelAssignedCredentials extends JPanel {

	private static final long serialVersionUID = 4478895396272005153L;
	private JLabel jLableAssigneCredentials;
	private JScrollPane jScrollPaneAssigneCredentials;
	private JList<AbstractCredential> jListAssignedCredentials;
	public JPanelAssignedCredentials() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{265, 0};
		gridBagLayout.rowHeights = new int[]{0, 138, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLableAssigneCredentials = new GridBagConstraints();
		gbc_jLableAssigneCredentials.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLableAssigneCredentials.insets = new Insets(5, 5, 5, 0);
		gbc_jLableAssigneCredentials.gridx = 0;
		gbc_jLableAssigneCredentials.gridy = 0;
		add(getJLableAssigneCredentials(), gbc_jLableAssigneCredentials);
		GridBagConstraints gbc_jScrollPaneAssigneCredentials = new GridBagConstraints();
		gbc_jScrollPaneAssigneCredentials.insets = new Insets(5, 5, 5, 5);
		gbc_jScrollPaneAssigneCredentials.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneAssigneCredentials.gridx = 0;
		gbc_jScrollPaneAssigneCredentials.gridy = 1;
		add(getJScrollPaneAssigneCredentials(), gbc_jScrollPaneAssigneCredentials);
	}

	private JLabel getJLableAssigneCredentials() {
		if (jLableAssigneCredentials == null) {
			jLableAssigneCredentials = new JLabel("Assigned Credentials");
			jLableAssigneCredentials.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLableAssigneCredentials;
	}
	private JScrollPane getJScrollPaneAssigneCredentials() {
		if (jScrollPaneAssigneCredentials == null) {
			jScrollPaneAssigneCredentials = new JScrollPane();
			jScrollPaneAssigneCredentials.setViewportView(getJListAssignedCredentials());
		}
		return jScrollPaneAssigneCredentials;
	}
	private JList<AbstractCredential> getJListAssignedCredentials() {
		if (jListAssignedCredentials == null) {
			jListAssignedCredentials = new JList<AbstractCredential>();
			jListAssignedCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jListAssignedCredentials;
	}
	
	public void fillAssignedCredentialJList(ApiRegistration awbRegService) {

		List<CredentialAssignment> credAssgnList = WsCredentialStore.getInstance().getCredentialAssignmentList();
		List<CredentialAssignment> credAssgnOfSelectedApi = new ArrayList<CredentialAssignment>();
		List<AbstractCredential> assgnCredentials = new ArrayList<>();

		for (Iterator<CredentialAssignment> iterator = credAssgnList.iterator(); iterator.hasNext();) {
			CredentialAssignment credentialAssignment = (CredentialAssignment) iterator.next();
			String apiId = credentialAssignment.getIdApiRegistrationDefaultBundleName();
			if (awbRegService.getClientBundleName() == apiId) {
				credAssgnOfSelectedApi.add(credentialAssignment);
				AbstractCredential abstractCred = WsCredentialStore.getInstance().getCredential(credentialAssignment.getIdCredential());
				assgnCredentials.add(abstractCred);
			}
		}
	}
}
