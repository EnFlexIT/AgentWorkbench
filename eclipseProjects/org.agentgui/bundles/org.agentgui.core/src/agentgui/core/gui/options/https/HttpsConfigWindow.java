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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
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

	private JPanel jPanelHeader;
	private JPanel jPanelFooter;
	private JPanel jPanelKeyStoreButtons;
	private JPanel jPanelTrustStoreButtons;

	private JLabel jLabelKeyStoreFile;
	private JLabel jLabelKeyStoreLocation;
	private JLabel jLabelTrustStoreFile;
	private JLabel jLabelTrustStoreLocation;
	private JLabel jLabelTrustStoreLocationPath;
	private JLabel jLabelKeyStoreLocationPath;

	private JButton jButtonOpenKeyStore;
	private JButton jButtonCreateKeyStore;
	private JButton jButtonOpenTrustStore;
	private JButton jButtonCreateTrustStore;
	private JButton jButtonOK;
	private JButton jButtonCancel;

	private JFileChooser jFileChooser;
	private JScrollPane jScrollPaneKeyStore;
	private JScrollPane jScrollPaneTrustStore;

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

	
	/**
	 * Instantiates a new httpsConfigWindow. Set the dialog visible in order to show it.
	 * @param owner the owner
	 */
	public HttpsConfigWindow(Dialog owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Instantiates a new httpsConfigWindow. Set the dialog visible in order to show it.
	 * @param owner the owner
	 */
	public HttpsConfigWindow(Frame owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Instantiates a new HttpsConfigWindow. Set the dialog visible in order to show it.
	 * @param owner the owner
	 * @param keystore the KeyStore
	 * @param keystorePassword the KeyStore password
	 * @param truststore the TrustStore
	 * @param truststorePassword the TrustStore password
	 */
	public HttpsConfigWindow(Dialog owner, File keystore, String keystorePassword, File truststore, String truststorePassword) {
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
	 * Instantiates a new httpsConfigWindow. Set the dialog visible in order to show it.
	 * @param owner the owner
	 * @param keystore the KeyStore
	 * @param keystorePassword the KeyStore password
	 * @param truststore the TrustStore
	 * @param truststorePassword the TrustStore password
	 */
	public HttpsConfigWindow(Frame owner, File keystore, String keystorePassword, File truststore, String truststorePassword) {
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
		this.setIconImage(GlobalInfo.getInternalImage("AgentGUI.png"));
		
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
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelHeader = new GridBagConstraints();
		gbc_jPanelHeader.gridwidth = 2;
		gbc_jPanelHeader.insets = new Insets(10, 10, 5, 10);
		gbc_jPanelHeader.anchor = GridBagConstraints.NORTH;
		gbc_jPanelHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelHeader.gridx = 0;
		gbc_jPanelHeader.gridy = 0;
		this.getContentPane().add(getJPanelHeader(), gbc_jPanelHeader);
		GridBagConstraints gbc_jScrollPaneKeyStore = new GridBagConstraints();
		gbc_jScrollPaneKeyStore.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneKeyStore.insets = new Insets(10, 10, 0, 10);
		gbc_jScrollPaneKeyStore.gridx = 0;
		gbc_jScrollPaneKeyStore.gridy = 1;
		this.getContentPane().add(getJPanelBody(), gbc_jScrollPaneKeyStore);
		GridBagConstraints gbc_jScrollPaneTrustStore = new GridBagConstraints();
		gbc_jScrollPaneTrustStore.insets = new Insets(10, 0, 0, 10);
		gbc_jScrollPaneTrustStore.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneTrustStore.gridx = 1;
		gbc_jScrollPaneTrustStore.gridy = 1;
		getContentPane().add(getJScrollPaneTrustStore(), gbc_jScrollPaneTrustStore);
		GridBagConstraints gbc_jPanelFooter = new GridBagConstraints();
		gbc_jPanelFooter.gridwidth = 2;
		gbc_jPanelFooter.insets = new Insets(10, 10, 15, 10);
		gbc_jPanelFooter.anchor = GridBagConstraints.NORTH;
		gbc_jPanelFooter.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelFooter.gridx = 0;
		gbc_jPanelFooter.gridy = 2;
		this.getContentPane().add(getJPanelFooter(), gbc_jPanelFooter);
		
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
			// --- Space for user messages --------------------------
			
			
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
	private JLabel getJLabelKeyStoreLocation() {
		if (jLabelKeyStoreLocation == null) {
			jLabelKeyStoreLocation = new JLabel(Language.translate("Location", Language.EN) + ":");
			jLabelKeyStoreLocation.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelKeyStoreLocation;
	}
	private JLabel getJLabelTrustStoreFile() {
		if (jLabelTrustStoreFile == null) {
			jLabelTrustStoreFile = new JLabel(Language.translate("TrustStore File", Language.EN) + ":");
			jLabelTrustStoreFile.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelTrustStoreFile;
	}
	protected JLabel getJLabelTrustStoreLocationPath() {
		if (jLabelTrustStoreLocationPath == null) {
			jLabelTrustStoreLocationPath = new JLabel("");
			jLabelTrustStoreLocationPath.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelTrustStoreLocationPath;
	}
	private JLabel getLabelTrustStoreLocation() {
		if (jLabelTrustStoreLocation == null) {
			jLabelTrustStoreLocation = new JLabel(Language.translate("Location", Language.EN) + ":");
			jLabelTrustStoreLocation.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelTrustStoreLocation;
	}
	protected JLabel getJLabelKeyStoreLocationPath() {
		if (jLabelKeyStoreLocationPath == null) {
			jLabelKeyStoreLocationPath = new JLabel("");
			jLabelKeyStoreLocationPath.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelKeyStoreLocationPath;
	}

	private JPanel getJPanelHeader() {
		if (jPanelHeader == null) {
			jPanelHeader = new JPanel();
			GridBagLayout gbl_jPanelHeader = new GridBagLayout();
			gbl_jPanelHeader.columnWidths = new int[] { 0, 300, 0, 300, 0 };
			gbl_jPanelHeader.rowHeights = new int[] { 0, 0, 0 };
			gbl_jPanelHeader.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
			gbl_jPanelHeader.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
			jPanelHeader.setLayout(gbl_jPanelHeader);
			GridBagConstraints gbc_jLabelKeyStoreFile = new GridBagConstraints();
			gbc_jLabelKeyStoreFile.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelKeyStoreFile.gridx = 0;
			gbc_jLabelKeyStoreFile.gridy = 0;
			jPanelHeader.add(getJLabelKeyStoreFile(), gbc_jLabelKeyStoreFile);
			GridBagConstraints gbc_jPanelKeyStoreButtons = new GridBagConstraints();
			gbc_jPanelKeyStoreButtons.insets = new Insets(0, 0, 5, 5);
			gbc_jPanelKeyStoreButtons.fill = GridBagConstraints.BOTH;
			gbc_jPanelKeyStoreButtons.gridx = 1;
			gbc_jPanelKeyStoreButtons.gridy = 0;
			jPanelHeader.add(getJPanelKeyStoreButtons(), gbc_jPanelKeyStoreButtons);
			GridBagConstraints gbc_jLabelTrustStoreFile = new GridBagConstraints();
			gbc_jLabelTrustStoreFile.insets = new Insets(0, 17, 5, 5);
			gbc_jLabelTrustStoreFile.gridx = 2;
			gbc_jLabelTrustStoreFile.gridy = 0;
			jPanelHeader.add(getJLabelTrustStoreFile(), gbc_jLabelTrustStoreFile);
			GridBagConstraints gbc_jPanelTrustStoreButtons = new GridBagConstraints();
			gbc_jPanelTrustStoreButtons.ipadx = 1;
			gbc_jPanelTrustStoreButtons.insets = new Insets(0, 0, 5, 0);
			gbc_jPanelTrustStoreButtons.fill = GridBagConstraints.BOTH;
			gbc_jPanelTrustStoreButtons.gridx = 3;
			gbc_jPanelTrustStoreButtons.gridy = 0;
			jPanelHeader.add(getJPanelTrustStoreButtons(), gbc_jPanelTrustStoreButtons);
			GridBagConstraints gbc_jLabelKeyStoreLocation = new GridBagConstraints();
			gbc_jLabelKeyStoreLocation.anchor = GridBagConstraints.WEST;
			gbc_jLabelKeyStoreLocation.insets = new Insets(0, 5, 0, 5);
			gbc_jLabelKeyStoreLocation.gridx = 0;
			gbc_jLabelKeyStoreLocation.gridy = 1;
			jPanelHeader.add(getJLabelKeyStoreLocation(), gbc_jLabelKeyStoreLocation);
			GridBagConstraints gbc_jLabelKeyStoreLocationPath = new GridBagConstraints();
			gbc_jLabelKeyStoreLocationPath.anchor = GridBagConstraints.WEST;
			gbc_jLabelKeyStoreLocationPath.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelKeyStoreLocationPath.gridx = 1;
			gbc_jLabelKeyStoreLocationPath.gridy = 1;
			jPanelHeader.add(getJLabelKeyStoreLocationPath(), gbc_jLabelKeyStoreLocationPath);
			GridBagConstraints gbc_jLabelTrustStoreLocation = new GridBagConstraints();
			gbc_jLabelTrustStoreLocation.anchor = GridBagConstraints.WEST;
			gbc_jLabelTrustStoreLocation.insets = new Insets(0, 17, 0, 5);
			gbc_jLabelTrustStoreLocation.gridx = 2;
			gbc_jLabelTrustStoreLocation.gridy = 1;
			jPanelHeader.add(getLabelTrustStoreLocation(), gbc_jLabelTrustStoreLocation);
			GridBagConstraints gbc_jLabelTrustStoreLocationPath = new GridBagConstraints();
			gbc_jLabelTrustStoreLocationPath.anchor = GridBagConstraints.WEST;
			gbc_jLabelTrustStoreLocationPath.gridx = 3;
			gbc_jLabelTrustStoreLocationPath.gridy = 1;
			jPanelHeader.add(getJLabelTrustStoreLocationPath(), gbc_jLabelTrustStoreLocationPath);
		}
		return jPanelHeader;
	}

	private JPanel getJPanelKeyStoreButtons() {
		if (jPanelKeyStoreButtons == null) {
			jPanelKeyStoreButtons = new JPanel();
			GridBagLayout gbl_jPanelKeyStoreButtons = new GridBagLayout();
			gbl_jPanelKeyStoreButtons.columnWidths = new int[] { 0, 0, 0 };
			gbl_jPanelKeyStoreButtons.rowHeights = new int[] { 0, 0 };
			gbl_jPanelKeyStoreButtons.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_jPanelKeyStoreButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			jPanelKeyStoreButtons.setLayout(gbl_jPanelKeyStoreButtons);
			GridBagConstraints gbc_jButtonOpenKeyStore = new GridBagConstraints();
			gbc_jButtonOpenKeyStore.insets = new Insets(0, 5, 0, 5);
			gbc_jButtonOpenKeyStore.gridx = 0;
			gbc_jButtonOpenKeyStore.gridy = 0;
			jPanelKeyStoreButtons.add(getJButtonOpenKeyStore(), gbc_jButtonOpenKeyStore);
			GridBagConstraints gbc_jButtonCreateKeyStore = new GridBagConstraints();
			gbc_jButtonCreateKeyStore.gridx = 1;
			gbc_jButtonCreateKeyStore.gridy = 0;
			jPanelKeyStoreButtons.add(getJButtonCreateKeyStore(), gbc_jButtonCreateKeyStore);
		}
		return jPanelKeyStoreButtons;
	}

	private JPanel getJPanelTrustStoreButtons() {
		if (jPanelTrustStoreButtons == null) {
			jPanelTrustStoreButtons = new JPanel();
			GridBagLayout gbl_jPanelTrustStoreButtons = new GridBagLayout();
			gbl_jPanelTrustStoreButtons.columnWidths = new int[] { 0, 0, 0 };
			gbl_jPanelTrustStoreButtons.rowHeights = new int[] { 0, 0 };
			gbl_jPanelTrustStoreButtons.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_jPanelTrustStoreButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			jPanelTrustStoreButtons.setLayout(gbl_jPanelTrustStoreButtons);
			GridBagConstraints gbc_jButtonOpenTrustStore = new GridBagConstraints();
			gbc_jButtonOpenTrustStore.insets = new Insets(0, 5, 0, 5);
			gbc_jButtonOpenTrustStore.gridx = 0;
			gbc_jButtonOpenTrustStore.gridy = 0;
			jPanelTrustStoreButtons.add(getJButtonOpenTrustStore(), gbc_jButtonOpenTrustStore);
			GridBagConstraints gbc_jButtonCreateTrustStore = new GridBagConstraints();
			gbc_jButtonCreateTrustStore.gridx = 1;
			gbc_jButtonCreateTrustStore.gridy = 0;
			jPanelTrustStoreButtons.add(getJButtonCreateTrustStore(), gbc_jButtonCreateTrustStore);
		}
		return jPanelTrustStoreButtons;
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

	private JPanel getJPanelFooter() {
		if (jPanelFooter == null) {
			jPanelFooter = new JPanel();
			FlowLayout fl_jPanelFooter = (FlowLayout) jPanelFooter.getLayout();
			fl_jPanelFooter.setHgap(10);
			fl_jPanelFooter.setVgap(10);
			jPanelFooter.add(getJButtonOK());
			jPanelFooter.add(getJButtonCancel());
		}
		return jPanelFooter;
	}
	private JScrollPane getJPanelBody() {
		if (jScrollPaneKeyStore == null) {
			jScrollPaneKeyStore = new JScrollPane();
			jScrollPaneKeyStore.setViewportView(this.getKeyStoreConfigPanel());
			jScrollPaneKeyStore.setBorder(null);
		}
		return jScrollPaneKeyStore;
	}
	private JScrollPane getJScrollPaneTrustStore() {
		if (jScrollPaneTrustStore == null) {
			jScrollPaneTrustStore = new JScrollPane();
			jScrollPaneTrustStore.setViewportView(this.getTrustStoreConfigPanel());
			jScrollPaneTrustStore.setBorder(null);
		}
		return jScrollPaneTrustStore;
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
		KeyStoreController controller = getKeyStoreConfigPanel().getKeyStoreController();
		controller.openTrustStore(getKeyStoreFile(), getKeyStorePassword());
		if (controller.isInitialized()) {
			certificateProperties = getKeyStoreConfigPanel().getKeyStoreController().getFirstCertificateProperties();
		} 
		if (certificateProperties!=null) {

			this.getKeyStoreConfigPanel().fillFields(certificateProperties);
			this.setKeyAlias(certificateProperties.getAlias());

			this.getJLabelKeyStoreLocationPath().setText(getKeyStoreFile().getAbsolutePath());
			this.getJLabelKeyStoreLocationPath().setToolTipText(getKeyStoreFile().getAbsolutePath());
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
		this.getJLabelTrustStoreLocationPath().setText(getTrustStoreFile().getAbsolutePath());
		this.getJLabelTrustStoreLocationPath().setToolTipText(getTrustStoreFile().getAbsolutePath());
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
			this.jLabelKeyStoreLocationPath.setText(null);
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
					String msg = Language.translate("Please, enter the password for  ", Language.EN);
					JLabel jLabelEnterPassword = new JLabel(msg + " " + keyStoreName + "  :");
					jLabelEnterPassword.setFont(new Font("Dialog", Font.BOLD, 11));
					
					JPasswordField jPasswordField = new JPasswordField(10);
					jPasswordField.addAncestorListener(new RequestFocusListener());

					JPanel jPanelPassword = new JPanel();
					jPanelPassword.add(jLabelEnterPassword);
					jPanelPassword.add(jPasswordField);

					String title = Language.translate("Password", Language.EN);
					int option = JOptionPane.showOptionDialog(null, jPanelPassword, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, jPasswordField);
					if (option==JOptionPane.OK_OPTION) {
						// ------------ Press OK --------------------------------
						keyStorePassword = new String(jPasswordField.getPassword());

						if (getKeyStoreConfigPanel().getKeyStoreController().openTrustStore(keyStoreFile, keyStorePassword)==true) {
							this.setKeyAlias(this.getKeyStoreConfigPanel().getKeyStoreController().getFirstCertificateProperties().getAlias());
						}
						if (this.getKeyAlias()==null) {
							this.jLabelKeyStoreLocationPath.setText(null);
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
			jLabelTrustStoreLocationPath.setText(null);
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
					String msg = Language.translate("Please enter the password for  ", Language.EN);
					JLabel jLabelEnterPassword = new JLabel(msg + " " + truststorename + "  :");
					jLabelEnterPassword.setFont(new Font("Dialog", Font.BOLD, 11));
					
					JPasswordField jPasswordFiled = new JPasswordField(10);
					jPasswordFiled.addAncestorListener(new RequestFocusListener());

					JPanel jPanelEnterPassword = new JPanel();
					jPanelEnterPassword.add(jLabelEnterPassword);
					jPanelEnterPassword.add(jPasswordFiled);
					
					String title = Language.translate("Password", Language.EN);
					int option = JOptionPane.showOptionDialog(null, jPanelEnterPassword, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, jPasswordFiled);
					trustStorePassword = new String(jPasswordFiled.getPassword());
					if (option == 0) {
						// ---- Press OK Button ------------------------------
						boolean openingSuccessful = getTrustStoreConfigPanel().getTrustStoreController().openTrustStore(trustStoreFile, trustStorePassword);
						if (openingSuccessful == false) {
							jLabelTrustStoreLocationPath.setText(null);
							trustStoreFile = null;
							getTrustStoreConfigPanel().getJTextFieldTrustStoreName().setText(null);
							getTrustStoreConfigPanel().getJPasswordFieldPassword().setText(null);
							getTrustStoreConfigPanel().getJPasswordFieldConfirmPassword().setText(null);
							getTrustStoreConfigPanel().getTrustStoreController().clearTableModel();
						} else {
							this.setTrustStoreDataToVisualization();
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
	 * Taken from https://tips4java.wordpress.com/2010/03/14/dialog-focus/
	 * 
	 * Convenience class to request focus on a component.
	 *
	 * When the component is added to a realized Window then component will
	 * request focus immediately, since the ancestorAdded event is fired
	 * immediately.
	 *
	 * When the component is added to a non realized Window, then the focus
	 * request will be made once the window is realized, since the
	 * ancestorAdded event will not be fired until then.
	 *
	 * Using the default constructor will cause the listener to be removed
	 * from the component once the AncestorEvent is generated. A second constructor
	 * allows you to specify a boolean value of false to prevent the
	 * AncestorListener from being removed when the event is generated. This will
	 * allow you to reuse the listener each time the event is generated.
	 */
	private class RequestFocusListener implements AncestorListener {

		private boolean removeListener;

		/**
		 *  Convenience constructor. The listener is only used once and then it is removed from the component.
		 */
		public RequestFocusListener() {
			this(true);
		}
		/**
		 *  Constructor that controls whether this listen can be used once or multiple times.
		 *  @param removeListener when true this listener is only invoked once otherwise it can be invoked multiple times.
		 */
		public RequestFocusListener(boolean removeListener) {
			this.removeListener = removeListener;
		}
		@Override
		public void ancestorAdded(AncestorEvent e) {
			JComponent component = e.getComponent();
			component.requestFocusInWindow();
			if (removeListener)
				component.removeAncestorListener(this);
		}
		@Override
		public void ancestorMoved(AncestorEvent e) {
		}
		@Override
		public void ancestorRemoved(AncestorEvent e) {
		}
	}
	
}