package de.enflexit.awb.ws.ui.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;

import de.enflexit.awb.ws.ui.WsConfigurationInterface;

/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientConfiguration extends JPanel implements WsConfigurationInterface {
	
	private static final long serialVersionUID = 7987858783733542296L;
	private JList credentialList;
	private JTree serverTree;
	private JSplitPane splitPane;
	private JTextField textField_serverID;
	private JLabel lbl_ServerID;
	private JSplitPane splitPane_1;
	private JSplitPane splitPane_2;
	private JSplitPane splitPane_3;
	private JLabel lbl_ClientBundleName;
	private JTextField textField_ClientBundleName;
	private JTextField textField_3;
	private JLabel lbl_CredentialType;
	private JLabel lblNewLabel_1;
	private JComboBox comboBox_CredType;
	private JSplitPane splitPane_4;
	private JSplitPane splitPane_5;
	private JLabel lblServerID;
	private JLabel lblCredentialID;
	private JTextField textFieldServerID;
	private JTextField textFieldCredentialID;
	private JSplitPane splitPane_6;
	private JSplitPane splitPane_7;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_2;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientConfiguration() {
		this.initialize();
	}
	private void initialize() {
		
		this.setBorder(BorderFactory.createEmptyBorder());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{229, 172, 0, 0};
		gridBagLayout.rowHeights = new int[]{211, 0, -20, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_serverTree = new GridBagConstraints();
		gbc_serverTree.insets = new Insets(0, 0, 5, 5);
		gbc_serverTree.fill = GridBagConstraints.BOTH;
		gbc_serverTree.gridx = 0;
		gbc_serverTree.gridy = 0;
		add(getTree_1(), gbc_serverTree);
		GridBagConstraints gbc_credentialList = new GridBagConstraints();
		gbc_credentialList.anchor = GridBagConstraints.EAST;
		gbc_credentialList.insets = new Insets(0, 0, 5, 0);
		gbc_credentialList.fill = GridBagConstraints.VERTICAL;
		gbc_credentialList.gridx = 2;
		gbc_credentialList.gridy = 0;
		add(getCredentialList(), gbc_credentialList);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.insets = new Insets(0, 0, 5, 5);
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		add(getSplitPane(), gbc_splitPane);
		GridBagConstraints gbc_splitPane_4 = new GridBagConstraints();
		gbc_splitPane_4.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane_4.fill = GridBagConstraints.BOTH;
		gbc_splitPane_4.gridx = 2;
		gbc_splitPane_4.gridy = 1;
		add(getSplitPane_4(), gbc_splitPane_4);
		GridBagConstraints gbc_splitPane_2 = new GridBagConstraints();
		gbc_splitPane_2.insets = new Insets(0, 0, 5, 5);
		gbc_splitPane_2.fill = GridBagConstraints.BOTH;
		gbc_splitPane_2.gridx = 0;
		gbc_splitPane_2.gridy = 2;
		add(getSplitPane_2(), gbc_splitPane_2);
		GridBagConstraints gbc_splitPane_5 = new GridBagConstraints();
		gbc_splitPane_5.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane_5.fill = GridBagConstraints.BOTH;
		gbc_splitPane_5.gridx = 2;
		gbc_splitPane_5.gridy = 2;
		add(getSplitPane_5(), gbc_splitPane_5);
		GridBagConstraints gbc_splitPane_1 = new GridBagConstraints();
		gbc_splitPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_splitPane_1.fill = GridBagConstraints.BOTH;
		gbc_splitPane_1.gridx = 0;
		gbc_splitPane_1.gridy = 3;
		add(getSplitPane_1(), gbc_splitPane_1);
		GridBagConstraints gbc_splitPane_6 = new GridBagConstraints();
		gbc_splitPane_6.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane_6.fill = GridBagConstraints.BOTH;
		gbc_splitPane_6.gridx = 2;
		gbc_splitPane_6.gridy = 3;
		add(getSplitPane_6(), gbc_splitPane_6);
		GridBagConstraints gbc_splitPane_3 = new GridBagConstraints();
		gbc_splitPane_3.insets = new Insets(0, 0, 0, 5);
		gbc_splitPane_3.fill = GridBagConstraints.BOTH;
		gbc_splitPane_3.gridx = 0;
		gbc_splitPane_3.gridy = 4;
		add(getSplitPane_3(), gbc_splitPane_3);
		GridBagConstraints gbc_splitPane_7 = new GridBagConstraints();
		gbc_splitPane_7.fill = GridBagConstraints.BOTH;
		gbc_splitPane_7.gridx = 2;
		gbc_splitPane_7.gridy = 4;
		add(getSplitPane_7(), gbc_splitPane_7);
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
	private JList getCredentialList() {
		if (credentialList == null) {
			credentialList = new JList();
		}
		return credentialList;
	}
	private JTree getTree_1() {
		if (serverTree == null) {
			serverTree = new JTree();
		}
		return serverTree;
	}
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setRightComponent(getTextField_serverID());
			splitPane.setLeftComponent(getLbl_ServerID());
		}
		return splitPane;
	}
	private JTextField getTextField_serverID() {
		if (textField_serverID == null) {
			textField_serverID = new JTextField();
			textField_serverID.setColumns(10);
		}
		return textField_serverID;
	}
	private JLabel getLbl_ServerID() {
		if (lbl_ServerID == null) {
			lbl_ServerID = new JLabel("Server-ID");
		}
		return lbl_ServerID;
	}
	private JSplitPane getSplitPane_1() {
		if (splitPane_1 == null) {
			splitPane_1 = new JSplitPane();
			splitPane_1.setLeftComponent(getLabel_3());
			splitPane_1.setRightComponent(getTextField_ClientBundleName());
		}
		return splitPane_1;
	}
	private JSplitPane getSplitPane_2() {
		if (splitPane_2 == null) {
			splitPane_2 = new JSplitPane();
			splitPane_2.setLeftComponent(getLabel_4());
			splitPane_2.setRightComponent(getComboBox_CredType());
		}
		return splitPane_2;
	}
	private JSplitPane getSplitPane_3() {
		if (splitPane_3 == null) {
			splitPane_3 = new JSplitPane();
			splitPane_3.setRightComponent(getTextField_3());
			splitPane_3.setLeftComponent(getLabel_5());
		}
		return splitPane_3;
	}
	private JLabel getLabel_3() {
		if (lbl_ClientBundleName == null) {
			lbl_ClientBundleName = new JLabel("Client-Bundlename");
		}
		return lbl_ClientBundleName;
	}
	private JTextField getTextField_ClientBundleName() {
		if (textField_ClientBundleName == null) {
			textField_ClientBundleName = new JTextField();
			textField_ClientBundleName.setColumns(10);
		}
		return textField_ClientBundleName;
	}
	private JTextField getTextField_3() {
		if (textField_3 == null) {
			textField_3 = new JTextField();
			textField_3.setColumns(10);
		}
		return textField_3;
	}
	private JLabel getLabel_4() {
		if (lbl_CredentialType == null) {
			lbl_CredentialType = new JLabel("Credential-Type");
		}
		return lbl_CredentialType;
	}
	private JLabel getLabel_5() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("API-URL");
		}
		return lblNewLabel_1;
	}
	private JComboBox getComboBox_CredType() {
		if (comboBox_CredType == null) {
			comboBox_CredType = new JComboBox();
		}
		return comboBox_CredType;
	}
	private JSplitPane getSplitPane_4() {
		if (splitPane_4 == null) {
			splitPane_4 = new JSplitPane();
			splitPane_4.setLeftComponent(getLblServerID());
			splitPane_4.setRightComponent(getTextFieldServerID());
		}
		return splitPane_4;
	}
	private JSplitPane getSplitPane_5() {
		if (splitPane_5 == null) {
			splitPane_5 = new JSplitPane();
			splitPane_5.setLeftComponent(getLblCredentialID());
			splitPane_5.setRightComponent(getTextFieldCredentialID());
		}
		return splitPane_5;
	}
	private JLabel getLblServerID() {
		if (lblServerID == null) {
			lblServerID = new JLabel("Server ID");
		}
		return lblServerID;
	}
	private JLabel getLblCredentialID() {
		if (lblCredentialID == null) {
			lblCredentialID = new JLabel("Credential ID");
		}
		return lblCredentialID;
	}
	private JTextField getTextFieldServerID() {
		if (textFieldServerID == null) {
			textFieldServerID = new JTextField();
			textFieldServerID.setColumns(10);
		}
		return textFieldServerID;
	}
	private JTextField getTextFieldCredentialID() {
		if (textFieldCredentialID == null) {
			textFieldCredentialID = new JTextField();
			textFieldCredentialID.setColumns(10);
		}
		return textFieldCredentialID;
	}
	private JSplitPane getSplitPane_6() {
		if (splitPane_6 == null) {
			splitPane_6 = new JSplitPane();
			splitPane_6.setLeftComponent(getLblNewLabel());
		}
		return splitPane_6;
	}
	private JSplitPane getSplitPane_7() {
		if (splitPane_7 == null) {
			splitPane_7 = new JSplitPane();
			splitPane_7.setLeftComponent(getLblNewLabel_2());
		}
		return splitPane_7;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("New label");
		}
		return lblNewLabel;
	}
	private JLabel getLblNewLabel_2() {
		if (lblNewLabel_2 == null) {
			lblNewLabel_2 = new JLabel("New label");
		}
		return lblNewLabel_2;
	}
}
