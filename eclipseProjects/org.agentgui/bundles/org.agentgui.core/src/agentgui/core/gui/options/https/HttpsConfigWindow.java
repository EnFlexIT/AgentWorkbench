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
package agentgui.core.gui.options.https;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.crypto.CertificateProperties;
import de.enflexit.common.crypto.KeyStoreController;

/**
 * This class allows the user to configure the HTTPS Message Transport protocol.
 * He can Create or edit the KeyStore and TrustStore which are necessary to run
 * the HTTPS protocol.
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */
public class HttpsConfigWindow extends JDialog implements ActionListener {

	private static final long serialVersionUID = -4676410555112580221L;

	private String preferredDirector; 
	
	private KeyStoreConfigPanel keyStoreConfigPanel;
	private TrustStoreConfigPanel trustStoreConfigPanel;

	private JLabel jLabelKeyStoreFile;
	private JLabel jLabelTrustStoreFile;
	private JTextField jTextFieldTrustStoreLocationPath;
	private JTextField jTextFieldKeyStoreLocationPath;

	private JButton jButtonOpenKeyStore;
	private JButton jButtonCreateKeyStore;
	private JButton jButtonOpenTrustStore;
	private JButton jButtonCreateTrustStore;
	private JButton jButtonOK;
	private JButton jButtonCancel;

	private JFileChooser jFileChooser;

	private File trustStoreFile;
	private File keyStoreFile;
	private String keyAlias;
	private String keyStorePassword;
	private String trustStorePassword;
	private String keyStoreButtonPressed;
	private String trustStoreButtonPressed;

	public static final String KEYSTORE_FILENAME = "KeyStore.jks";
	public static final String TRUSTSTORE_FILENAME = "TrustStore.jks";

	private boolean canceled;
	private JPanel jPanelKeystoreControl;
	private JPanel jPanelTrustStoreControl;

	
	/**
	 * Instantiates a new httpsConfigWindow. Set the dialog visible in order to show it.
	 * @param owner the owner frame or window
	 * @wbp.parser.constructor
	 */
	public HttpsConfigWindow(Window owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Instantiates a new HttpsConfigWindow. Set the dialog visible in order to show it.
	 * @param owner the owner frame or dialog
	 * @param keystore the KeyStore
	 * @param keystorePassword the KeyStore password
	 * @param truststore the TrustStore
	 * @param truststorePassword the TrustStore password
	 */
	public HttpsConfigWindow(Window owner, File keystore, String keystorePassword, File truststore, String truststorePassword) {
		super(owner);
		this.setKeyStoreFile(keystore);
		this.setKeyStorePassword(keystorePassword);
		this.setTrustStoreFile(truststore);
		this.setTrustStorePassword(truststorePassword);
		this.setKeyStoreDataToVisualization();
		this.setTrustStoreDataToVisualization();
		this.initialize();
	}
	/**
	 * Initialize the contents of the dialog.
	 */
	private void initialize() {
		
		this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": " + Language.translate("HTTPS Configuration", Language.EN));
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		
		this.setBounds(100, 100, 820, 590);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelKeystoreControl = new GridBagConstraints();
		gbc_jPanelKeystoreControl.insets = new Insets(10, 10, 15, 5);
		gbc_jPanelKeystoreControl.fill = GridBagConstraints.BOTH;
		gbc_jPanelKeystoreControl.gridx = 0;
		gbc_jPanelKeystoreControl.gridy = 0;
		getContentPane().add(getJPanelKeystoreControl(), gbc_jPanelKeystoreControl);
		GridBagConstraints gbc_jPanelTrustStoreControl = new GridBagConstraints();
		gbc_jPanelTrustStoreControl.insets = new Insets(10, 5, 15, 10);
		gbc_jPanelTrustStoreControl.fill = GridBagConstraints.BOTH;
		gbc_jPanelTrustStoreControl.gridx = 1;
		gbc_jPanelTrustStoreControl.gridy = 0;
		getContentPane().add(getJPanelTrustStoreControl(), gbc_jPanelTrustStoreControl);
	}
	private JPanel getJPanelKeystoreControl() {
		if (jPanelKeystoreControl == null) {
			jPanelKeystoreControl = new JPanel();
			GridBagLayout gbl_jPanelKeystoreControl = new GridBagLayout();
			gbl_jPanelKeystoreControl.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelKeystoreControl.rowHeights = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelKeystoreControl.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelKeystoreControl.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
			jPanelKeystoreControl.setLayout(gbl_jPanelKeystoreControl);
			GridBagConstraints gbc_jLabelKeyStoreFile = new GridBagConstraints();
			gbc_jLabelKeyStoreFile.gridx = 0;
			gbc_jLabelKeyStoreFile.gridy = 0;
			jPanelKeystoreControl.add(getJLabelKeyStoreFile(), gbc_jLabelKeyStoreFile);
			GridBagConstraints gbc_jButtonOpenKeyStore = new GridBagConstraints();
			gbc_jButtonOpenKeyStore.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonOpenKeyStore.anchor = GridBagConstraints.EAST;
			gbc_jButtonOpenKeyStore.gridx = 1;
			gbc_jButtonOpenKeyStore.gridy = 0;
			jPanelKeystoreControl.add(getJButtonOpenKeyStore(), gbc_jButtonOpenKeyStore);
			GridBagConstraints gbc_jButtonCreateKeyStore = new GridBagConstraints();
			gbc_jButtonCreateKeyStore.gridx = 2;
			gbc_jButtonCreateKeyStore.gridy = 0;
			jPanelKeystoreControl.add(getJButtonCreateKeyStore(), gbc_jButtonCreateKeyStore);
			GridBagConstraints gbc_jLabelKeyStoreLocationPath = new GridBagConstraints();
			gbc_jLabelKeyStoreLocationPath.insets = new Insets(5, 0, 0, 0);
			gbc_jLabelKeyStoreLocationPath.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelKeyStoreLocationPath.gridwidth = 3;
			gbc_jLabelKeyStoreLocationPath.gridx = 0;
			gbc_jLabelKeyStoreLocationPath.gridy = 1;
			jPanelKeystoreControl.add(getJTextFieldKeyStoreLocationPath(), gbc_jLabelKeyStoreLocationPath);
			GridBagConstraints gbc_keyStoreConfigPanel = new GridBagConstraints();
			gbc_keyStoreConfigPanel.insets = new Insets(5, 0, 0, 0);
			gbc_keyStoreConfigPanel.fill = GridBagConstraints.BOTH;
			gbc_keyStoreConfigPanel.gridwidth = 3;
			gbc_keyStoreConfigPanel.gridx = 0;
			gbc_keyStoreConfigPanel.gridy = 2;
			jPanelKeystoreControl.add(getKeyStoreConfigPanel(), gbc_keyStoreConfigPanel);
			GridBagConstraints gbc_jButtonOK = new GridBagConstraints();
			gbc_jButtonOK.insets = new Insets(10, 0, 0, 20);
			gbc_jButtonOK.anchor = GridBagConstraints.EAST;
			gbc_jButtonOK.gridwidth = 3;
			gbc_jButtonOK.gridx = 0;
			gbc_jButtonOK.gridy = 3;
			jPanelKeystoreControl.add(getJButtonOK(), gbc_jButtonOK);
		}
		return jPanelKeystoreControl;
	}
	private JPanel getJPanelTrustStoreControl() {
		if (jPanelTrustStoreControl == null) {
			jPanelTrustStoreControl = new JPanel();
			GridBagLayout gbl_jPanelTrustStoreControl = new GridBagLayout();
			gbl_jPanelTrustStoreControl.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelTrustStoreControl.rowHeights = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelTrustStoreControl.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelTrustStoreControl.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
			jPanelTrustStoreControl.setLayout(gbl_jPanelTrustStoreControl);
			GridBagConstraints gbc_jLabelTrustStoreFile = new GridBagConstraints();
			gbc_jLabelTrustStoreFile.gridx = 0;
			gbc_jLabelTrustStoreFile.gridy = 0;
			jPanelTrustStoreControl.add(getJLabelTrustStoreFile(), gbc_jLabelTrustStoreFile);
			GridBagConstraints gbc_jButtonOpenTrustStore = new GridBagConstraints();
			gbc_jButtonOpenTrustStore.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonOpenTrustStore.anchor = GridBagConstraints.EAST;
			gbc_jButtonOpenTrustStore.gridx = 1;
			gbc_jButtonOpenTrustStore.gridy = 0;
			jPanelTrustStoreControl.add(getJButtonOpenTrustStore(), gbc_jButtonOpenTrustStore);
			GridBagConstraints gbc_jButtonCreateTrustStore = new GridBagConstraints();
			gbc_jButtonCreateTrustStore.gridx = 2;
			gbc_jButtonCreateTrustStore.gridy = 0;
			jPanelTrustStoreControl.add(getJButtonCreateTrustStore(), gbc_jButtonCreateTrustStore);
			GridBagConstraints gbc_jLabelTrustStoreLocationPath = new GridBagConstraints();
			gbc_jLabelTrustStoreLocationPath.insets = new Insets(5, 0, 0, 0);
			gbc_jLabelTrustStoreLocationPath.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelTrustStoreLocationPath.gridwidth = 3;
			gbc_jLabelTrustStoreLocationPath.gridx = 0;
			gbc_jLabelTrustStoreLocationPath.gridy = 1;
			jPanelTrustStoreControl.add(getJTextFieldTrustStoreLocationPath(), gbc_jLabelTrustStoreLocationPath);
			GridBagConstraints gbc_trustStoreConfigPanel = new GridBagConstraints();
			gbc_trustStoreConfigPanel.insets = new Insets(5, 0, 0, 0);
			gbc_trustStoreConfigPanel.fill = GridBagConstraints.BOTH;
			gbc_trustStoreConfigPanel.gridwidth = 3;
			gbc_trustStoreConfigPanel.gridx = 0;
			gbc_trustStoreConfigPanel.gridy = 2;
			jPanelTrustStoreControl.add(getTrustStoreConfigPanel(), gbc_trustStoreConfigPanel);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(10, 20, 0, 0);
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.gridwidth = 3;
			gbc_jButtonCancel.gridx = 0;
			gbc_jButtonCancel.gridy = 3;
			jPanelTrustStoreControl.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelTrustStoreControl;
	}

	/**
	 * Specifies the preferred director for files.
	 * @param preferredFileDirector the new preferred file director
	 */
	public void setPreferredDirector(String preferredFileDirector) {
		this.preferredDirector = preferredFileDirector;
	}
	/**
	 * Return the preferred director for files.
	 * @return the preferred file director
	 */
	public String getPreferredDirector() {
		return preferredDirector;
	}
	/**
	 * Checks if the specified file is in the preferred director.
	 *
	 * @param fileToCheck the file to check
	 * @return true, if is within preferred file director
	 */
	private boolean isInPreferredDirector(File fileToCheck) {
		return this.isInPreferredDirector(fileToCheck.getAbsolutePath());
	}
	/**
	 * Checks if the specified file is in the preferred director.
	 *
	 * @param filePathToCheck the file path to check
	 * @return true, if is within preferred file director
	 */
	private boolean isInPreferredDirector(String filePathToCheck) {
		if (this.getPreferredDirector()==null) return true;
		if (filePathToCheck==null || filePathToCheck.isEmpty()) return false;
		if (filePathToCheck.startsWith(this.getPreferredDirector())==false) {
			return false;	
		} 
		return true;
	}
	
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return this.canceled;
	}
	/**
	 * Sets if the dialog action was canceled.
	 * @param isCanceled the new canceled
	 */
	private void setCanceled(boolean isCanceled) {
		this.canceled = isCanceled;
	}

	private JLabel getJLabelKeyStoreFile() {
		if (jLabelKeyStoreFile == null) {
			jLabelKeyStoreFile = new JLabel(Language.translate("KeyStore File", Language.EN) + ":");
			jLabelKeyStoreFile.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelKeyStoreFile;
	}
	private JLabel getJLabelTrustStoreFile() {
		if (jLabelTrustStoreFile == null) {
			jLabelTrustStoreFile = new JLabel(Language.translate("TrustStore File", Language.EN) + ":");
			jLabelTrustStoreFile.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelTrustStoreFile;
	}
	protected JTextField getJTextFieldTrustStoreLocationPath() {
		if (jTextFieldTrustStoreLocationPath == null) {
			jTextFieldTrustStoreLocationPath = new JTextField();
			jTextFieldTrustStoreLocationPath.setEditable(false);
			jTextFieldTrustStoreLocationPath.setFont(new Font("Dialog", Font.PLAIN, 11));
			jTextFieldTrustStoreLocationPath.setOpaque(false);
			jTextFieldTrustStoreLocationPath.setBorder(BorderFactory.createEmptyBorder());
			jTextFieldTrustStoreLocationPath.setBackground(new Color(0, 0, 0, 0));
		}
		return jTextFieldTrustStoreLocationPath;
	}
	protected JTextField getJTextFieldKeyStoreLocationPath() {
		if (jTextFieldKeyStoreLocationPath == null) {
			jTextFieldKeyStoreLocationPath = new JTextField();
			jTextFieldKeyStoreLocationPath.setEditable(false);
			jTextFieldKeyStoreLocationPath.setFont(new Font("Dialog", Font.PLAIN, 11));
			jTextFieldKeyStoreLocationPath.setOpaque(false);
			jTextFieldKeyStoreLocationPath.setBorder(BorderFactory.createEmptyBorder());
			jTextFieldKeyStoreLocationPath.setBackground(new Color(0, 0, 0, 0));
		}
		return jTextFieldKeyStoreLocationPath;
	}

	protected KeyStoreConfigPanel getKeyStoreConfigPanel() {
		if (keyStoreConfigPanel == null) {
			keyStoreConfigPanel = new KeyStoreConfigPanel(this);
		}
		return keyStoreConfigPanel;
	}
	protected TrustStoreConfigPanel getTrustStoreConfigPanel() {
		if (trustStoreConfigPanel == null) {
			trustStoreConfigPanel = new TrustStoreConfigPanel(this);
		}
		return trustStoreConfigPanel;
	}
	
	private JButton getJButtonOpenKeyStore() {
		if (jButtonOpenKeyStore == null) {
			jButtonOpenKeyStore = new JButton(Language.translate("Open", Language.EN));
			jButtonOpenKeyStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonOpenKeyStore.setToolTipText(Language.translate("Open a KeyStore file", Language.EN));
			jButtonOpenKeyStore.setPreferredSize(new Dimension(85, 26));
			jButtonOpenKeyStore.addActionListener(this);
		}
		return jButtonOpenKeyStore;
	}
	private JButton getJButtonCreateKeyStore() {
		if (jButtonCreateKeyStore == null) {
			jButtonCreateKeyStore = new JButton(Language.translate("Create", Language.EN));
			jButtonCreateKeyStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonCreateKeyStore.setToolTipText(Language.translate("Create new KeyStore", Language.EN));
			jButtonCreateKeyStore.setPreferredSize(new Dimension(85, 26));
			jButtonCreateKeyStore.addActionListener(this);
		}
		return jButtonCreateKeyStore;
	}
	private JButton getJButtonOpenTrustStore() {
		if (jButtonOpenTrustStore == null) {
			jButtonOpenTrustStore = new JButton(Language.translate("Open", Language.EN));
			jButtonOpenTrustStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonOpenTrustStore.setToolTipText(Language.translate("Open a TrustStore file", Language.EN));
			jButtonOpenTrustStore.setPreferredSize(new Dimension(85, 26));
			jButtonOpenTrustStore.addActionListener(this);
		}
		return jButtonOpenTrustStore;
	}
	private JButton getJButtonCreateTrustStore() {
		if (jButtonCreateTrustStore == null) {
			jButtonCreateTrustStore = new JButton(Language.translate("Create", Language.EN));
			jButtonCreateTrustStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonCreateTrustStore.setToolTipText(Language.translate("Create new TrustStore", Language.EN));
			jButtonCreateTrustStore.setPreferredSize(new Dimension(85, 26));
			jButtonCreateTrustStore.addActionListener(this);
		}
		return jButtonCreateTrustStore;
	}
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton(Language.translate("OK", Language.EN));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setPreferredSize(new Dimension(85, 26));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton(Language.translate("Cancel", Language.EN));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setPreferredSize(new Dimension(85, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	protected JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
		}
		return jFileChooser;
	}
	
	/**
	 * This method initializes trustStorefilepath.
	 */
	public File getTrustStoreFile() {
		return trustStoreFile;
	}
	/**
	 * Sets TrustStorefilepath.
	 * @param trustStoreFile
	 */
	public void setTrustStoreFile(File trustStoreFile) {
		this.trustStoreFile = trustStoreFile;
	}
	
	/**
	 * This method initializes keyStorefilepath.
	 */
	public File getKeyStoreFile() {
		return keyStoreFile;
	}
	/**
	 * Sets KeyStorefilepath.
	 * @param keyStoreFile
	 */
	public void setKeyStoreFile(File keyStoreFile) {
		this.keyStoreFile = keyStoreFile;
	}
	
	/**
	 * This method initializes keyStorePassword.
	 */
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	/**
	 * Sets KeyStorePassword.
	 * @param keyStorePassword
	 */
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	
	/**
	 * This method initializes trustStorePassword.
	 */
	public String getTrustStorePassword() {
		return trustStorePassword;
	}
	/**
	 * Sets TrustStorePassword.
	 * @param trustStorePassword
	 */
	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	/**
	 * This method initializes keyStoreAlias.
	 */
	protected String getKeyAlias() {
		return keyAlias;
	}
	/**
	 * Sets KeyStoreAlias.
	 * @param keyAlias
	 */
	protected void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}
	
	/**
	 * This method initializes keyStoreButtonPressed.
	 */
	protected String getKeyStoreButtonPressed() {
		return keyStoreButtonPressed;
	}
	/**
	 * Sets KeyStoreButtonPressed.
	 * @param keystorebuttonPressed
	 */
	protected void setKeyStoreButtonPressed(String keystorebuttonPressed) {
		this.keyStoreButtonPressed = keystorebuttonPressed;
	}
	
	/**
	 * This method initializes trustStoreButtonPressed.
	 */
	protected String getTrustStoreButtonPressed() {
		return trustStoreButtonPressed;
	}
	/**
	 * Sets TrustStoreButtonPressed.
	 * @param truststorebuttonPressed
	 */
	protected void setTrustStoreButtonPressed(String truststorebuttonPressed) {
		this.trustStoreButtonPressed = truststorebuttonPressed;
	}
	

	/**
	 * Sets JTextField enabled true and Text null
	 * @param jTextField
	 */
	private void setFieldsEnabledTrue(JTextField jTextField) {
		jTextField.setEnabled(true);
		jTextField.setText(null);
	}

	/**
	 * Sets the key store data to visualization.
	 */
	public void setKeyStoreDataToVisualization() {

		// ---- Get the Content of the keyStore ----------------------
		CertificateProperties certificateProperties = null;
		KeyStoreController controller = this.getKeyStoreConfigPanel().getKeyStoreController();
		controller.openTrustStore(getKeyStoreFile(), getKeyStorePassword());
		if (controller.isInitialized()) {
			certificateProperties = this.getKeyStoreConfigPanel().getKeyStoreController().getFirstCertificateProperties();
		} 
		if (certificateProperties!=null) {

			this.getKeyStoreConfigPanel().fillFields(certificateProperties);
			this.setKeyAlias(certificateProperties.getAlias());

			this.getJTextFieldKeyStoreLocationPath().setText(getKeyStoreFile().getAbsolutePath());
			this.getJTextFieldKeyStoreLocationPath().setToolTipText(getKeyStoreFile().getAbsolutePath());
		}
		this.keyStoreButtonPressed = "UpdateKeyStore";
	}

	/**
	 * Sets the trust store data to the visualization.
	 */
	public void setTrustStoreDataToVisualization() {
		// ---- Prepare the TrustStore form for update -------------
		this.getTrustStoreConfigPanel().getJTextFieldTrustStoreName().setText(getTrustStoreFile().getName());
		this.getTrustStoreConfigPanel().getJTextFieldTrustStoreName().setEnabled(false);
		this.getTrustStoreConfigPanel().getJLabelCertificatesList().setVisible(true);
		this.getTrustStoreConfigPanel().getScrollPane().setVisible(true);
		this.getTrustStoreConfigPanel().getJButtonAddCertificate().setVisible(true);
		this.getTrustStoreConfigPanel().getJButtonRemoveCertificate().setVisible(true);
		this.getTrustStoreConfigPanel().getTrustStoreController().clearTableModel();
		this.getTrustStoreConfigPanel().getTrustStoreController().openTrustStore(getTrustStoreFile(), getTrustStorePassword());
		this.getTrustStoreConfigPanel().getTrustStoreController().getTrustedCertificatesList();
		this.getTrustStoreConfigPanel().getjTableTrusTedCertificates().setModel(getTrustStoreConfigPanel().getTrustStoreController().getTableModel());
		this.getTrustStoreConfigPanel().getJPasswordFieldPassword().setText(getTrustStorePassword());
		this.getTrustStoreConfigPanel().getJPasswordFieldPassword().setEnabled(true);
		this.getTrustStoreConfigPanel().getJPasswordFieldConfirmPassword().setText(null);
		this.getTrustStoreConfigPanel().getJPasswordFieldConfirmPassword().setEnabled(true);
		this.getJTextFieldTrustStoreLocationPath().setText(getTrustStoreFile().getAbsolutePath());
		this.getJTextFieldTrustStoreLocationPath().setToolTipText(getTrustStoreFile().getAbsolutePath());
		this.trustStoreButtonPressed = "EditTrustStore";
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// ---- Create new KeyStore -------------------------------
		if (ae.getSource() == this.getJButtonCreateKeyStore()) {
			// ----- Prepare the KeyStore form --------------------
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldCountryCode());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldCity());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldFullName());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldKeyStoreName());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldOrganization());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldOrganizationalUnit());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldState());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJPasswordField());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJPasswordConfirmPassword());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldAlias());
			this.setFieldsEnabledTrue(this.keyStoreConfigPanel.getJTextFieldValidity());
			this.keyStoreConfigPanel.getJTextFieldKeyStoreName().requestFocus();
			this.jTextFieldKeyStoreLocationPath.setText(null);
			this.keyStoreFile = null;
			this.keyStoreButtonPressed = "CreateKeyStore";

		} else if (ae.getSource()==this.getJButtonOpenKeyStore()) {
			// ---- Open a KeyStore -------------------------------------------
			this.getJFileChooser().setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			// --- Is there a preferred directory? ----------------------------
			if (this.getPreferredDirector()!=null) {
				this.getJFileChooser().setCurrentDirectory(new File(this.getPreferredDirector()));
			}
			// --- Open file selection ----------------------------------------
			if (this.getJFileChooser().showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				if (this.getPreferredDirector()!=null) {
					if (this.isInPreferredDirector(this.getJFileChooser().getSelectedFile())==false) {
						String msg = Language.translate("The KeyStore file should be located in directory", Language.EN) + " '" + this.getPreferredDirector() + "'!";
						String title = Language.translate("Wrong KeyStore location!", Language.EN);
						JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
						return;
					}
				} else {
					Application.getGlobalInfo().setLastSelectedFolder(this.getJFileChooser().getCurrentDirectory());
				}
				
				// -------- Get the path of selected KeyStore file -----------
				this.keyStoreFile = this.getJFileChooser().getSelectedFile();
				// ---- Get the KeyStoreName ----------------------------------
				String keyStoreName = this.keyStoreFile.getName();
				// ---- Verify if the selected file is a KeyStore -------------
				if (keyStoreName.endsWith(KEYSTORE_FILENAME)) {
					// --- Create JOptionPane to enter the KeyStore password --
					String title = Language.translate("Password", Language.EN);
					JPanelPassword jPanelEnterPassword = new JPanelPassword(Language.translate("Please enter the password for  ", Language.EN) + " " + keyStoreName + ":");
					int option = JOptionPane.showOptionDialog(null, jPanelEnterPassword, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
					if (option==JOptionPane.OK_OPTION) {
						// ---- Pressed OK Button -----------------------------
						if (this.getKeyStoreConfigPanel().getKeyStoreController().openTrustStore(keyStoreFile, jPanelEnterPassword.getPassword())==true) {
							keyStorePassword = jPanelEnterPassword.getPassword();
							this.setKeyAlias(this.getKeyStoreConfigPanel().getKeyStoreController().getFirstCertificateProperties().getAlias());
						}
						if (this.getKeyAlias()==null) {
							this.getJTextFieldKeyStoreLocationPath().setText(null);
							this.keyStoreFile = null;
						} else {
							this.setKeyStoreDataToVisualization();
						}
					}
					
				} else {
					String msg = Language.translate("Please choose a KeyStore file! Your KeyStore name should ends with 'KeyStore'!", Language.EN);
					String title = Language.translate("Warning message", Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
			}

		} else if (ae.getSource()==this.getJButtonCreateTrustStore()) {
			// ---- Create new TrustStore ----------------------------------------
			// ---- Prepared the TrusStore form ----------------------------------
			trustStoreConfigPanel.getJTextFieldTrustStoreName().requestFocus();
			setFieldsEnabledTrue(trustStoreConfigPanel.getJTextFieldTrustStoreName());
			trustStoreConfigPanel.getJLabelCertificatesList().setVisible(false);
			trustStoreConfigPanel.getScrollPane().setVisible(false);
			trustStoreConfigPanel.getJButtonAddCertificate().setVisible(false);
			trustStoreConfigPanel.getJButtonRemoveCertificate().setVisible(false);
			trustStoreConfigPanel.getJPasswordFieldConfirmPassword().setText(null);
			trustStoreConfigPanel.getJPasswordFieldPassword().setText(null);
			trustStoreConfigPanel.getJPasswordFieldConfirmPassword().setEnabled(true);
			trustStoreConfigPanel.getJPasswordFieldPassword().setEnabled(true);
			jTextFieldTrustStoreLocationPath.setText(null);
			trustStoreFile = null;
			trustStoreButtonPressed = "CreateTrustStore";

		} else if (ae.getSource() == this.getJButtonOpenTrustStore()) {
			this.getJFileChooser().setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
			// --- Is there a preferred directory? ----------------------------
			if (this.getPreferredDirector()!=null) {
				this.getJFileChooser().setCurrentDirectory(new File(this.getPreferredDirector()));
			}
			// --- Open file selection ----------------------------------------
			if (this.getJFileChooser().showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				if (this.getPreferredDirector()!=null) {
					if (this.isInPreferredDirector(this.getJFileChooser().getSelectedFile())==false) {
						String msg = Language.translate("The TrustStore file should be located in directory", Language.EN) + " '" + this.getPreferredDirector() + "'!";
						String title = Language.translate("Wrong TrustStore location!", Language.EN);
						JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
						return;
					}
				} else {
					Application.getGlobalInfo().setLastSelectedFolder(this.getJFileChooser().getCurrentDirectory());
				}
				
				// ---- Get the path of selected TrustStore file ------------
				trustStoreFile = jFileChooser.getSelectedFile();
				// ---- Get the TrustStoreName ------------------------------
				String truststorename = trustStoreFile.getName();
				// ---- Verify if the selected file is a TrustStore ---------
				if (truststorename.endsWith(TRUSTSTORE_FILENAME)) {
					// --- Create JOptionPane to enter the TrustStore password -
					String title = Language.translate("Password", Language.EN);
					JPanelPassword jPanelEnterPassword = new JPanelPassword(Language.translate("Please enter the password for  ", Language.EN) + " " + truststorename + ":");
					int option = JOptionPane.showOptionDialog(null, jPanelEnterPassword, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
					if (option==JOptionPane.OK_OPTION) {
						// ---- Pressed OK Button -----------------------------
						if (this.getTrustStoreConfigPanel().getTrustStoreController().openTrustStore(trustStoreFile, jPanelEnterPassword.getPassword())==true) {
							trustStorePassword = jPanelEnterPassword.getPassword();
							this.setTrustStoreDataToVisualization();
						} else {
							trustStoreFile = null;
							this.getJTextFieldTrustStoreLocationPath().setText(null);
							this.getTrustStoreConfigPanel().getJTextFieldTrustStoreName().setText(null);
							this.getTrustStoreConfigPanel().getJPasswordFieldPassword().setText(null);
							this.getTrustStoreConfigPanel().getJPasswordFieldConfirmPassword().setText(null);
							this.getTrustStoreConfigPanel().getTrustStoreController().clearTableModel();
						}
					}
					
				} else {
					String msg = Language.translate("Please, choose a TrustStore file! Your TrustStore name should ends with 'TrustStore'", Language.EN);
					String title = Language.translate("Warning message", Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
			}
			
		} else if (ae.getSource() == this.getJButtonCancel()) {
			// ---- Button Cancel is pressed ---------------------------------
			this.setCanceled(true);
			this.dispose();
			
		} else if (ae.getSource() == this.getJButtonOK()) {
			// ---- Button OK is pressed -------------------------------------
			if (this.getKeyStoreFile()==null) {
				String msg = Language.translate("You should open a KeyStore to run the HTTPS MTP!", Language.EN);
				String title = Language.translate("No KeyStore specified!", Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else if (this.isInPreferredDirector(this.getKeyStoreFile())==false) {
				String msg = Language.translate("The KeyStore file should be located in directory", Language.EN) + " '" + this.getPreferredDirector() + "'!";
				String title = Language.translate("Wrong KeyStore location!", Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else if (this.getTrustStoreFile()==null) {
				String msg = Language.translate("You should open a TrustStore to run the HTTPS MTP!", Language.EN);
				String title = Language.translate("No TrustStore specified!", Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else if (this.isInPreferredDirector(this.getTrustStoreFile())==false) {
				String msg = Language.translate("The TrustStore file should be located in directory", Language.EN) + " '" + this.getPreferredDirector() + "'!";
				String title = Language.translate("Wrong TrustStore location!", Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else {
				this.setCanceled(false);
				this.setVisible(false);
			}
			
		}
	}

	
	/**
	 * The Class JPanelPassword.
	 */
	private class JPanelPassword extends JPanel {
		
		private static final long serialVersionUID = 6605782615579733091L;
		
		private String displayText; 
		private JLabel jLabelDisplayText;
		private JPasswordField jPasswordField;
		
		public JPanelPassword(String displayText) {
			this.displayText = displayText;
			this.initialize();
		}
		private void initialize() {
			this.add(this.getjLabelDisplayText());
			this.add(this.getJPasswordField());
		}
		private JLabel getjLabelDisplayText() {
			if (jLabelDisplayText==null) {
				jLabelDisplayText = new JLabel(this.displayText);
				jLabelDisplayText.setFont(new Font("Dialog", Font.BOLD, 11));	
			}
			return jLabelDisplayText;
		}
		private JPasswordField getJPasswordField() {
			if (jPasswordField==null) {
				jPasswordField =  new JPasswordField(10);
				jPasswordField.addAncestorListener(new AncestorListener() {
					@Override
					public void ancestorRemoved(AncestorEvent ae) { }
					@Override
					public void ancestorMoved(AncestorEvent ae) { }
					@Override
					public void ancestorAdded(AncestorEvent ae) {
						JComponent component = ae.getComponent();
						component.requestFocusInWindow();
						component.removeAncestorListener(this);
					}
				});
			}
			return jPasswordField;
		}
		/**
		 * Returns the typed password.
		 * @return the password
		 */
		public String getPassword() {
			return new String (this.getJPasswordField().getPassword());
		}
	}
	
}