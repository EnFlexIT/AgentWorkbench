/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.gui.options;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.MtpProtocol;
import agentgui.core.gui.components.JComboBoxMtpProtocol;
import agentgui.core.gui.options.https.HttpsConfigWindow;

/**
 * The Class JPanelMTPConfig is used to set the Message Transport Protocol. The
 * user has the possibility to start JADE with the HTTP MTP or HTTPS MTP.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 14-04-2016
 */
public class JPanelMTPConfig extends AbstractJPanelForOptions implements ActionListener, ItemListener {

	private static final long serialVersionUID = -7016775471452161527L;

	private JLabel jLabelKeystorePath;
	private JLabel jLabelTruststorePath;
	private JLabel jLabelMTPProtocol;

	private JTextField jTextFieldKeyStorePath;
	private JTextField jTextFieldTrustStorePath;

	private JButton jButtonEditMTP;
	private JComboBoxMtpProtocol jComboBoxMtpProtocol;

	private File keyStore;
	private String keyStorePassword;
	private File trustStore;
	private String trustStorePassword;

	
	/**
	 * Constructor of this class.
	 * @param optionDialog the option dialog
	 * @param startOptions the start options
	 */
	public JPanelMTPConfig(OptionDialog optionDialog, StartOptions startOptions) {
		super(optionDialog, startOptions);
		this.initialize();
	}
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		this.setLayout(gridBagLayout);
		this.setSize(429, 102);
		GridBagConstraints gbc_jLabelMTPProtocol = new GridBagConstraints();
		gbc_jLabelMTPProtocol.anchor = GridBagConstraints.WEST;
		gbc_jLabelMTPProtocol.insets = new Insets(0, 0, 3, 5);
		gbc_jLabelMTPProtocol.gridx = 0;
		gbc_jLabelMTPProtocol.gridy = 0;
		add(getJLabelMTPProtocol(), gbc_jLabelMTPProtocol);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.SOUTHWEST;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		add(getJComboBoxMtpProtocol(), gbc_comboBox);
		GridBagConstraints gbc_jButtonEditMTP = new GridBagConstraints();
		gbc_jButtonEditMTP.anchor = GridBagConstraints.SOUTH;
		gbc_jButtonEditMTP.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonEditMTP.gridx = 2;
		gbc_jButtonEditMTP.gridy = 0;
		add(getJButtonEditMTP(), gbc_jButtonEditMTP);
		GridBagConstraints gbc_jLabelKeystorePath = new GridBagConstraints();
		gbc_jLabelKeystorePath.anchor = GridBagConstraints.WEST;
		gbc_jLabelKeystorePath.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelKeystorePath.gridx = 0;
		gbc_jLabelKeystorePath.gridy = 1;
		add(getJLabelKeystorePath(), gbc_jLabelKeystorePath);
		GridBagConstraints gbc_jTextFieldKeyStorePath = new GridBagConstraints();
		gbc_jTextFieldKeyStorePath.gridwidth = 2;
		gbc_jTextFieldKeyStorePath.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldKeyStorePath.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldKeyStorePath.gridx = 1;
		gbc_jTextFieldKeyStorePath.gridy = 1;
		add(getJTextFieldKeyStorePath(), gbc_jTextFieldKeyStorePath);
		GridBagConstraints gbc_jLabelTruststorePath = new GridBagConstraints();
		gbc_jLabelTruststorePath.anchor = GridBagConstraints.WEST;
		gbc_jLabelTruststorePath.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelTruststorePath.gridx = 0;
		gbc_jLabelTruststorePath.gridy = 2;
		add(getJLabelTruststorePath(), gbc_jLabelTruststorePath);
		GridBagConstraints gbc_jTextFieldTrustStorePath = new GridBagConstraints();
		gbc_jTextFieldTrustStorePath.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldTrustStorePath.gridwidth = 2;
		gbc_jTextFieldTrustStorePath.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldTrustStorePath.gridx = 1;
		gbc_jTextFieldTrustStorePath.gridy = 2;
		add(getJTextFieldTrustStorePath(), gbc_jTextFieldTrustStorePath);
	}
	/**
	 * This method initializes jLabelMTPConfiguration.
	 */
	private JLabel getJLabelMTPProtocol() {
		if (jLabelMTPProtocol == null) {
			jLabelMTPProtocol = new JLabel(Language.translate("MTP-Protocol", Language.EN)+":");
			jLabelMTPProtocol.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMTPProtocol;
	}
	/**
	 * This method initializes jComboBoxMtpProtocol.
	 */
	protected JComboBoxMtpProtocol getJComboBoxMtpProtocol() {
		if (jComboBoxMtpProtocol == null) {
			jComboBoxMtpProtocol = new JComboBoxMtpProtocol();
			jComboBoxMtpProtocol.setPreferredSize(new Dimension(90, 26));
			jComboBoxMtpProtocol.addItemListener(this);
		}
		return jComboBoxMtpProtocol;
	}
	/**
	 * This method initializes jButtonEditMTP.
	 */
	private JButton getJButtonEditMTP() {
		if (jButtonEditMTP == null) {
			jButtonEditMTP = new JButton();
			jButtonEditMTP.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonEditMTP.setPreferredSize(new Dimension(45, 26));
			jButtonEditMTP.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonEditMTP.addActionListener(this);
		}
		return jButtonEditMTP;
	}
	/**
	 * This method initializes jLabelKeystorePath.
	 */
	private JLabel getJLabelKeystorePath() {
		if (jLabelKeystorePath == null) {
			jLabelKeystorePath = new JLabel("KeyStore:");
			jLabelKeystorePath.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelKeystorePath;
	}
	/**
	 * This method initializes jLabelTruststorePath.
	 */
	private JLabel getJLabelTruststorePath() {
		if (jLabelTruststorePath == null) {
			jLabelTruststorePath = new JLabel("TrustStore:");
			jLabelTruststorePath.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelTruststorePath;
	}
	/**
	 * This method initializes jTextFieldKeyStorePath.
	 */
	private JTextField getJTextFieldKeyStorePath() {
		if (jTextFieldKeyStorePath == null) {
			jTextFieldKeyStorePath = new JTextField();
			jTextFieldKeyStorePath.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldKeyStorePath.setPreferredSize(new Dimension(468, 26));
		}
		return jTextFieldKeyStorePath;
	}
	/**
	 * This method initializes jTextFieldTrustStorePath.
	 */
	private JTextField getJTextFieldTrustStorePath() {
		if (jTextFieldTrustStorePath == null) {
			jTextFieldTrustStorePath = new JTextField();
			jTextFieldTrustStorePath.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldTrustStorePath.setPreferredSize(new Dimension(468, 26));
		}
		return jTextFieldTrustStorePath;
	}
	/**
	 * This method initializes keyStore.
	 * @return keyStore
	 */
	private File getKeyStore() {
		return keyStore;
	}
	/**
	 * Sets keyStore.
	 * @param keyStore
	 */
	private void setKeyStore(File keyStore) {
		this.keyStore = keyStore;
	}
	/**
	 * This method initializes keyStorePassword.
	 * @return keyStorePassword
	 */
	private String getKeyStorePassword() {
		return keyStorePassword;
	}
	/**
	 * Sets keyStorePassword.
	 * @param keyStorePassword
	 */
	private void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	/**
	 * This method initializes trustStore.
	 * @return trustStore
	 */
	private File getTrustStore() {
		return trustStore;
	}
	/**
	 * Sets trustStore.
	 * @param trustStore
	 */
	private void setTrustStore(File trustStore) {
		this.trustStore = trustStore;
	}
	/**
	 * This method initializes trustStorePassword.
	 * @return trustStorePassword
	 */
	private String getTrustStorePassword() {
		return trustStorePassword;
	}
	/**
	 * Sets trustStorePasswords.
	 * @param trustStorePassword
	 */
	private void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	/**
	 * Sets JLabels and JTextFields visible true in case choosing HTTPS MTP.
	 */
	private void showCertificateSettings(){
		this.getJLabelKeystorePath().setVisible(true);
		this.getJLabelTruststorePath().setVisible(true);
		this.getJTextFieldKeyStorePath().setVisible(true);
		this.getJTextFieldTrustStorePath().setVisible(true);
		this.getJButtonEditMTP().setVisible(true);
	}
	/**
	 * Sets JLabels and JTextFields visible false in case choosing HTTP MTP.
	 */
	protected void hideCertificateSettings(){
		this.getJLabelKeystorePath().setVisible(false);
		this.getJLabelTruststorePath().setVisible(false);
		this.getJTextFieldTrustStorePath().setVisible(false);
		this.getJTextFieldKeyStorePath().setVisible(false);
		this.getJButtonEditMTP().setVisible(false);
	}
	
	/**
	 * Opens the HttpsCinfogWindow to configure the HTTPS MTP.
	 * @param trigger the trigger for the call
	 */
	private void editHTTPSsettings(Object trigger) {
		
		if (trigger==this.getJButtonEditMTP()) {
			// --- In case that the user choose to edit the HTTPS MTP ----------
			// --- Open the HttpsConfigWindow ----------------------------------
			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(this.optionDialog, getKeyStore(), getKeyStorePassword(), getTrustStore(), getTrustStorePassword());
			httpsConfigWindow.setVisible(true);
			// --- Wait for the user -------------------------------------------
			if (httpsConfigWindow.isCanceled() == false) {
				// ---- Return the KeyStore and TrustStore chosen by the user --
				this.setKeyStore(httpsConfigWindow.getKeyStoreFile());
				this.setTrustStore(httpsConfigWindow.getTrustStoreFile());
				this.setKeyStorePassword(httpsConfigWindow.getKeyStorePassword());
				this.setTrustStorePassword(httpsConfigWindow.getTrustStorePassword());
				this.getJTextFieldKeyStorePath().setText(this.getKeyStore().getAbsolutePath());
				this.getJTextFieldTrustStorePath().setText(this.getTrustStore().getAbsolutePath());
			} 
			
		} else if (trigger==this.getJComboBoxMtpProtocol()) {
			// --- In case that the user choose to configure new HTTPS MTP ------
			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(this.optionDialog);
			httpsConfigWindow.setVisible(true);
			// --- Wait for the user --------------------------------------------
			if (httpsConfigWindow.isCanceled() == false) {
				// ---- Return the KeyStore and TrustStore chosen by the user ---
				this.setKeyStore(httpsConfigWindow.getKeyStoreFile());
				this.setTrustStore(httpsConfigWindow.getTrustStoreFile());
				this.setKeyStorePassword(httpsConfigWindow.getKeyStorePassword());
				this.setTrustStorePassword(httpsConfigWindow.getTrustStorePassword());
				this.getJTextFieldKeyStorePath().setText(this.getKeyStore().getAbsolutePath());
				this.getJTextFieldTrustStorePath().setText(this.getTrustStore().getAbsolutePath());
			} else {
				// ---- Button Cancel is pressed -------------------------------
				getJComboBoxMtpProtocol().setSelectedProtocol(MtpProtocol.HTTP);
				this.hideCertificateSettings();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent event) {
		
		if (event.getSource()==this.getJComboBoxMtpProtocol()) {
			MtpProtocol currentMTP = this.getJComboBoxMtpProtocol().getSelectedProtocol();
			if (currentMTP==MtpProtocol.HTTP) {
				// ---- switch from HTTPS to HTTP ----------------------------------
				this.hideCertificateSettings();
				
			} else if (currentMTP==MtpProtocol.HTTPS) {
				// ---- switch between HTTP and HTTPS ------------------------------
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Object item = event.getItem();
					if (item.equals(MtpProtocol.HTTPS)) {
						// ---- If the user choose HTTPS ---------------------------
						this.showCertificateSettings();
						// --- If the stores are not defined or do not exist, open the configuration dialog ----------
						if(this.keyStore == null || this.trustStore == null || this.keyStore.exists() == false || this.trustStore.exists() == false){
							this.editHTTPSsettings(this.getJComboBoxMtpProtocol());
						}
					} else {
						// ---- If the user choose HTTP ----------------------------
						this.hideCertificateSettings();
					}
				}
				
			} else if (currentMTP == MtpProtocol.PROXIEDHTTPS){
				System.err.println("NOT YET IMPLEMENTED");
				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonEditMTP()){
			// --- Open the HttpsConfigWindow -------------------------------------
			this.editHTTPSsettings(this.getJButtonEditMTP());
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setGlobalData2Form()
	 */
	@Override
	public void setGlobalData2Form() {
		MtpProtocol mtpProtocol = this.getGlobalInfo().getMtpProtocol();
		this.getJComboBoxMtpProtocol().setSelectedProtocol(mtpProtocol);
		if (mtpProtocol.equals(MtpProtocol.HTTPS)) {
			this.setKeyStore(new File(this.getGlobalInfo().getKeyStoreFile()));
			this.setTrustStore(new File(this.getGlobalInfo().getTrustStoreFile()));
			this.setKeyStorePassword(this.getGlobalInfo().getKeyStorePassword());
			this.setTrustStorePassword(this.getGlobalInfo().getTrustStorePassword());
			this.getJTextFieldKeyStorePath().setText(this.getKeyStore().getAbsolutePath());
			this.getJTextFieldTrustStorePath().setText(this.getTrustStore().getAbsolutePath());
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setFormData2Global()
	 */
	@Override
	public void setFormData2Global() {
		this.getGlobalInfo().setMtpProtocol(this.getJComboBoxMtpProtocol().getSelectedProtocol());
		if (this.getJComboBoxMtpProtocol().getSelectedProtocol()==MtpProtocol.HTTPS) {
			this.getGlobalInfo().setKeyStoreFile(this.getKeyStore().getAbsolutePath());
			this.getGlobalInfo().setTrustStoreFile(this.getTrustStore().getAbsolutePath());
			this.getGlobalInfo().setKeyStorePassword(this.getKeyStorePassword());
			this.getGlobalInfo().setTrustStorePassword(this.getTrustStorePassword());
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#errorFound()
	 */
	@Override
	public boolean errorFound() {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#refreshView()
	 */
	@Override
	public void refreshView() {
		MtpProtocol mtpProtocol = this.getGlobalInfo().getMtpProtocol();
		this.getJComboBoxMtpProtocol().setSelectedProtocol(mtpProtocol);
		if (mtpProtocol.equals(MtpProtocol.HTTPS)) {
			this.showCertificateSettings();
		} else {
			this.hideCertificateSettings();
		}
	}
}
