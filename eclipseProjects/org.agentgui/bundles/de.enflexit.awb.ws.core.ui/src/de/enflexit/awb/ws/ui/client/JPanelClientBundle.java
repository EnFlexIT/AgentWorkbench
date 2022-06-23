package de.enflexit.awb.ws.ui.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.enflexit.awb.ws.client.AwbApiRegistrationService;

/**
 * The Class JPanelClientConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelClientBundle extends JPanel {
	
	private static final long serialVersionUID = 7987858783733542296L;
	
	private JLabel jLabelBundleList;
	private JScrollPane jScrollPaneBundleList;
	private JList<AwbApiRegistrationService> jListBundles;
	private DefaultListModel<AwbApiRegistrationService> listModelRegisteredApis;
	
	private JPanel jPanelInfo;
		private JLabel jLabelCredentialType;
		private JLabel jLabelDescription;
		private JLabel jLabelCredentialTypeDefined;
		private JScrollPane jScrollPaneDescription;
		private JTextArea jTextAreaDescription;
	
	/**
	 * Instantiates a new j panel client configuration.
	 */
	public JPanelClientBundle() {
		this.initialize();
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 100, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelBundleList = new GridBagConstraints();
		gbc_jLabelBundleList.anchor = GridBagConstraints.WEST;
		gbc_jLabelBundleList.gridx = 0;
		gbc_jLabelBundleList.gridy = 0;
		add(getJLabelBundleList(), gbc_jLabelBundleList);
		GridBagConstraints gbc_jScrollPaneBundleList = new GridBagConstraints();
		gbc_jScrollPaneBundleList.insets = new Insets(5, 0, 0, 0);
		gbc_jScrollPaneBundleList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneBundleList.gridx = 0;
		gbc_jScrollPaneBundleList.gridy = 1;
		add(getJScrollPaneBundleList(), gbc_jScrollPaneBundleList);
		GridBagConstraints gbc_jPanelInfo = new GridBagConstraints();
		gbc_jPanelInfo.insets = new Insets(10, 0, 0, 0);
		gbc_jPanelInfo.fill = GridBagConstraints.BOTH;
		gbc_jPanelInfo.gridx = 0;
		gbc_jPanelInfo.gridy = 2;
		add(getJPanelInfo(), gbc_jPanelInfo);
	}
	
	private JLabel getJLabelBundleList() {
		if (jLabelBundleList == null) {
			jLabelBundleList = new JLabel("Server - API / Client Bundle");
			jLabelBundleList.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelBundleList;
	}
	private JScrollPane getJScrollPaneBundleList() {
		if (jScrollPaneBundleList == null) {
			jScrollPaneBundleList = new JScrollPane();
			jScrollPaneBundleList.setViewportView(getJListBundles());
		}
		return jScrollPaneBundleList;
	}
	
	private DefaultListModel<AwbApiRegistrationService> getListModelRegisteredApis() {
		if (listModelRegisteredApis==null) {
			listModelRegisteredApis = new DefaultListModel<AwbApiRegistrationService>();
			// --- Fill the list model ------------------------------
			// TODO Guck mal
		}
		return listModelRegisteredApis;
	}
	private JList<AwbApiRegistrationService> getJListBundles() {
		if (jListBundles == null) {
			jListBundles = new JList<>(this.getListModelRegisteredApis());
			jListBundles.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jListBundles;
	}
	
	
	private JPanel getJPanelInfo() {
		if (jPanelInfo == null) {
			jPanelInfo = new JPanel();
			
			GridBagLayout gbl_jPanelInfo = new GridBagLayout();
			gbl_jPanelInfo.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelInfo.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelInfo.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelInfo.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
			jPanelInfo.setLayout(gbl_jPanelInfo);
			
			GridBagConstraints gbc_jLabelCredentialType = new GridBagConstraints();
			gbc_jLabelCredentialType.anchor = GridBagConstraints.WEST;
			gbc_jLabelCredentialType.gridx = 0;
			gbc_jLabelCredentialType.gridy = 0;
			jPanelInfo.add(getJLabelCredentialType(), gbc_jLabelCredentialType);
			
			GridBagConstraints gbc_jLabelCredentialTypeDefined = new GridBagConstraints();
			gbc_jLabelCredentialTypeDefined.anchor = GridBagConstraints.WEST;
			gbc_jLabelCredentialTypeDefined.gridx = 1;
			gbc_jLabelCredentialTypeDefined.gridy = 0;
			jPanelInfo.add(getJLabelCredentialTypeDefined(), gbc_jLabelCredentialTypeDefined);
			
			GridBagConstraints gbc_jLabelDescription = new GridBagConstraints();
			gbc_jLabelDescription.insets = new Insets(5, 0, 0, 0);
			gbc_jLabelDescription.anchor = GridBagConstraints.WEST;
			gbc_jLabelDescription.gridx = 0;
			gbc_jLabelDescription.gridy = 1;
			jPanelInfo.add(getJLabelDescription(), gbc_jLabelDescription);
			
			GridBagConstraints gbc_jTextAreaDescription = new GridBagConstraints();
			gbc_jTextAreaDescription.insets = new Insets(5, 0, 0, 0);
			gbc_jTextAreaDescription.gridwidth = 2;
			gbc_jTextAreaDescription.fill = GridBagConstraints.BOTH;
			gbc_jTextAreaDescription.gridx = 0;
			gbc_jTextAreaDescription.gridy = 2;
			jPanelInfo.add(getJScrollPaneDescription(), gbc_jTextAreaDescription);
		}
		return jPanelInfo;
	}
	private JLabel getJLabelCredentialType() {
		if (jLabelCredentialType == null) {
			jLabelCredentialType = new JLabel("Credential Type:");
			jLabelCredentialType.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCredentialType;
	}
	private JLabel getJLabelDescription() {
		if (jLabelDescription == null) {
			jLabelDescription = new JLabel("Description:");
			jLabelDescription.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelDescription;
	}
	private JLabel getJLabelCredentialTypeDefined() {
		if (jLabelCredentialTypeDefined == null) {
			jLabelCredentialTypeDefined = new JLabel("-");
			jLabelCredentialTypeDefined.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCredentialTypeDefined;
	}
	
	private JScrollPane getJScrollPaneDescription() {
		if (jScrollPaneDescription==null) {
			jScrollPaneDescription = new JScrollPane();
			jScrollPaneDescription.setViewportView(this.getJTextAreaDescription());
		}
		return jScrollPaneDescription;
	}
	private JTextArea getJTextAreaDescription() {
		if (jTextAreaDescription == null) {
			jTextAreaDescription = new JTextArea();
			jTextAreaDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jTextAreaDescription;
	}
}
