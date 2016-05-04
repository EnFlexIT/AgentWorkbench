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

import java.awt.Font;
import java.awt.Frame;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.UIManager.LookAndFeelInfo;

import agentgui.core.application.Application;

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

	private KeyStoreConfigPanel keyStoreConfigPanel;
	private TrustStoreConfigPanel trustStoreConfigPanel;

	private JFrame frame;

	private JPanel jPanelHeader;
	private JPanel jPanelFooter;
	private JPanel jPanelPassword;
	private JPanel jPanelKeyStoreButtons;
	private JPanel jPanelTrustStoreButtons;

	private JLabel jLabelEnterPassword;
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

	private String trustStorefilepath;
	private String keyStorefilepath;
	private String keyStoreAlias;
	private String keyStorePassword;
	private String trustStorePassword;
	private String data;
	private String keyStoreButtonPressed;
	private String trustStoreButtonPressed;

	private final String pathImage = Application.getGlobalInfo().getPathImageIntern();
	private ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(pathImage + "AgentGUI.png"));
	private Image image = imageIcon.getImage();

	private boolean canceled;

	/**
	 * Instantiates a new httpsConfigWindow.
	 */
	public HttpsConfigWindow() {
		this.initialize();
	}
	/**
	 * Instantiates a new httpsConfigWindow.
	 * @param keystore
	 * @param keystore_password
	 * @param Truststore
	 * @param Truststore_password
	 */
	public HttpsConfigWindow(String keystore, String keystore_password, String Truststore, String Truststore_password) {
		this.setKeyStorefilepath(keystore);
		this.setKeyStorePassword(keystore_password);
		this.setTrustStorefilepath(Truststore);
		this.setTrustStorePassword(Truststore_password);
		this.getKeyStoreData();
		this.getTrustStoreData();
		this.initialize();
	}
	/**
	 * Instantiates a new httpsConfigWindow.
	 * @param owner the owner
	 */
	public HttpsConfigWindow(Dialog owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Instantiates a new httpsConfigWindow.
	 * @param owner the owner
	 */
	public HttpsConfigWindow(Frame owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Initialize the contents of the dialog.
	 */
	private void initialize() {
		this.setTitle("Agent.GUI: HTTPS Configuration");
		this.setIconImage(image);
		this.setBounds(100, 100, 820, 560);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled=true;
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
		this.setVisible(true);
	}
	/**
	 * Sets the look and feel.
	 */
	private static void setLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		new HttpsConfigWindow();
	}
	/**
	 * This method initializes jLabelKeyStoreFile.
	 */
	private JLabel getJLabelKeyStoreFile() {
		if (jLabelKeyStoreFile == null) {
			jLabelKeyStoreFile = new JLabel("KeyStore File:");
			jLabelKeyStoreFile.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelKeyStoreFile;
	}
	/**
	 * This method initializes jLabelKeyStoreLocation.
	 */
	private JLabel getJLabelKeyStoreLocation() {
		if (jLabelKeyStoreLocation == null) {
			jLabelKeyStoreLocation = new JLabel("Location:");
			jLabelKeyStoreLocation.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelKeyStoreLocation;
	}
	/**
	 * This method initializes jLabelTrustStoreFile.
	 */
	private JLabel getJLabelTrustStoreFile() {
		if (jLabelTrustStoreFile == null) {
			jLabelTrustStoreFile = new JLabel("TrustStore File:");
			jLabelTrustStoreFile.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelTrustStoreFile;
	}
	/**
	 * This method initializes JLabelTrustStoreLocationPath.
	 */
	protected JLabel getJLabelTrustStoreLocationPath() {
		if (jLabelTrustStoreLocationPath == null) {
			jLabelTrustStoreLocationPath = new JLabel("");
			jLabelTrustStoreLocationPath.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelTrustStoreLocationPath.setToolTipText(trustStorefilepath);

		}
		return jLabelTrustStoreLocationPath;
	}
	/**
	 * This method initializes jLabelTrustStoreLocation.
	 */
	private JLabel getLabelTrustStoreLocation() {
		if (jLabelTrustStoreLocation == null) {
			jLabelTrustStoreLocation = new JLabel("Location:");
			jLabelTrustStoreLocation.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelTrustStoreLocation;
	}
	/**
	 * This method initializes jLabelKeyStoreLocationPath.
	 */
	protected JLabel getJLabelKeyStoreLocationPath() {
		if (jLabelKeyStoreLocationPath == null) {
			jLabelKeyStoreLocationPath = new JLabel("");
			jLabelKeyStoreLocationPath.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelKeyStoreLocationPath;
	}
	/**
	 * This method initializes jPanelHeader.
	 */
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
	/**
	 * This method initializes jPanelKeyStoreButtons.
	 */
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
	/**
	 * This method initializes jPanelTrustStoreButtons.
	 */
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
	/**
	 * This method initializes keyStoreConfigPanel.
	 */
	protected KeyStoreConfigPanel getKeyStoreConfigPanel() {
		if (keyStoreConfigPanel == null) {
			keyStoreConfigPanel = new KeyStoreConfigPanel(this);
		}
		return keyStoreConfigPanel;
	}
	/**
	 * This method initializes trustStoreConfigPanel.
	 */
	protected TrustStoreConfigPanel getTrustStoreConfigPanel() {
		if (trustStoreConfigPanel == null) {
			trustStoreConfigPanel = new TrustStoreConfigPanel(this);
		}
		return trustStoreConfigPanel;
	}
	/**
	 * This method initializes jButtonOpenKeyStore.
	 */
	private JButton getJButtonOpenKeyStore() {
		if (jButtonOpenKeyStore == null) {
			jButtonOpenKeyStore = new JButton("Open");
			jButtonOpenKeyStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonOpenKeyStore.setToolTipText("Open a KeyStore file");
			jButtonOpenKeyStore.addActionListener(this);
		}
		return jButtonOpenKeyStore;
	}
	/**
	 * This method initializes jButtonCreateKeyStore.
	 */
	private JButton getJButtonCreateKeyStore() {
		if (jButtonCreateKeyStore == null) {
			jButtonCreateKeyStore = new JButton("Create");
			jButtonCreateKeyStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonCreateKeyStore.setToolTipText("Create new KeyStore");
			jButtonCreateKeyStore.addActionListener(this);
		}
		return jButtonCreateKeyStore;
	}
	/**
	 * This method initializes jButtonOpenTrustStore.
	 */
	private JButton getJButtonOpenTrustStore() {
		if (jButtonOpenTrustStore == null) {
			jButtonOpenTrustStore = new JButton("Open");
			jButtonOpenTrustStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonOpenTrustStore.setToolTipText("Open a TrustStore file");
			jButtonOpenTrustStore.addActionListener(this);

		}
		return jButtonOpenTrustStore;
	}
	/**
	 * This method initializes jButtonCreateTrustStore.
	 */
	private JButton getJButtonCreateTrustStore() {
		if (jButtonCreateTrustStore == null) {
			jButtonCreateTrustStore = new JButton("Create");
			jButtonCreateTrustStore.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonCreateTrustStore.setToolTipText("Create new TrustStore");
			jButtonCreateTrustStore.addActionListener(this);
		}
		return jButtonCreateTrustStore;
	}
	/**
	 * This method initializes jButtonOK.
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton("     OK     ");
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	/**
	 * This method initializes jButtonCancel.
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("   Cancel   ");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	/**
	 * This method initializes jPanelFooter.
	 */
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
	/**
	 * This method initializes jScrollPaneKeyStore.
	 */
	private JScrollPane getJPanelBody() {
		if (jScrollPaneKeyStore == null) {
			jScrollPaneKeyStore = new JScrollPane();
			jScrollPaneKeyStore.setViewportView(this.getKeyStoreConfigPanel());
			jScrollPaneKeyStore.setBorder(null);
		}
		return jScrollPaneKeyStore;
	}
	/**
	 * This method initializes jScrollPaneTrustStore.
	 */
	private JScrollPane getJScrollPaneTrustStore() {
		if (jScrollPaneTrustStore == null) {
			jScrollPaneTrustStore = new JScrollPane();
			jScrollPaneTrustStore.setViewportView(this.getTrustStoreConfigPanel());
			jScrollPaneTrustStore.setBorder(null);
		}
		return jScrollPaneTrustStore;
	}
	/**
	 * This method initializes jFileChooser.
	 */
	protected JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
		}
		return jFileChooser;
	}
	/**
	 * This method initializes trustStorefilepath.
	 */
	public String getTrustStorefilepath() {
		return trustStorefilepath;
	}
	/**
	 * This method initializes keyStorefilepath.
	 */
	public String getKeyStorefilepath() {
		return keyStorefilepath;
	}
	/**
	 * This method initializes keyStorePassword.
	 */
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	/**
	 * This method initializes trustStorePassword.
	 */
	public String getTrustStorePassword() {
		return trustStorePassword;
	}
	/**
	 * This method initializes keyStoreAlias.
	 */
	protected String getKeyStoreAlias() {
		return keyStoreAlias;
	}
	/**
	 * This method initializes keyStoreButtonPressed.
	 */
	protected String getKeyStoreButtonPressed() {
		return keyStoreButtonPressed;
	}
	/**
	 * This method initializes trustStoreButtonPressed.
	 */
	protected String getTrustStoreButtonPressed() {
		return trustStoreButtonPressed;
	}
	/**
	 * Sets KeyStorePassword.
	 * @param keyStorePassword
	 */
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	/**
	 * Sets TrustStorefilepath.
	 * @param trustStorefilepath
	 */
	public void setTrustStorefilepath(String trustStorefilepath) {
		this.trustStorefilepath = trustStorefilepath;
	}
	/**
	 * Sets KeyStorefilepath.
	 * @param keyStorefilepath
	 */
	public void setKeyStorefilepath(String keyStorefilepath) {
		this.keyStorefilepath = keyStorefilepath;
	}
	/**
	 * Sets KeyStoreAlias.
	 * @param keyStoreAlias
	 */
	protected void setKeyStoreAlias(String keyStoreAlias) {
		this.keyStoreAlias = keyStoreAlias;
	}
	/**
	 * Sets TrustStorePassword.
	 * @param trustStorePassword
	 */
	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}
	/**
	 * Sets KeyStoreButtonPressed.
	 * @param keystorebuttonPressed
	 */
	protected void setKeyStoreButtonPressed(String keystorebuttonPressed) {
		this.keyStoreButtonPressed = keystorebuttonPressed;
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
	 * Sets JTextField enabled false
	 * @param jTextField
	 */
	protected void setFieldsEnabledFalse(JTextField jTextField) {
		jTextField.setEnabled(false);
	}
	/**
	 * Get KeyStore informations and fill in fields with
	 */
	public void getKeyStoreData() {
		
		setKeyStoreAlias(KeyStoreController.GetKeyStoreAlias(getKeyStorefilepath(), getKeyStorePassword())); 
		// ---- Get the Content of the keyStore ----------------------
		data = KeyStoreController.ListKeyStoreContent(keyStorefilepath, keyStorePassword);
		// ---- Substring informations from the result ---------------
		String FullName = data.substring(data.indexOf("CN=") + 3, data.indexOf(",OU"));
		String OrganizationalUnit = data.substring(data.indexOf("OU=") + 3, data.indexOf(",O="));
		String Organization = data.substring(data.indexOf("O=") + 2, data.indexOf(",L"));
		String Locality = data.substring(data.indexOf("L=") + 2, data.indexOf(",ST"));
		String State = data.substring(data.indexOf("ST=") + 3, data.indexOf(",C"));
		String CountryCode = data.substring(data.indexOf("C=") + 2, data.indexOf("C=") + 4);

		// ----- Fill in fields with KeyStore informations ----------
		setFieldsEnabledFalse(this.getKeyStoreConfigPanel().getJTextFieldCity());
		setFieldsEnabledFalse(this.getKeyStoreConfigPanel().getJTextFieldCountryCode());
		setFieldsEnabledFalse(this.getKeyStoreConfigPanel().getJTextFieldFullName());
		setFieldsEnabledFalse(this.getKeyStoreConfigPanel().getJTextFieldOrganization());
		setFieldsEnabledFalse(this.getKeyStoreConfigPanel().getJTextFieldOrganizationalUnit());
		setFieldsEnabledFalse(this.getKeyStoreConfigPanel().getJTextFieldState());
		setFieldsEnabledFalse(this.getKeyStoreConfigPanel().getJTextFieldKeyStoreName());
		this.getKeyStoreConfigPanel().getJPasswordField().setEnabled(true);
		this.getKeyStoreConfigPanel().getJPasswordConfirmPassword().setEnabled(true);
		this.getKeyStoreConfigPanel().getJTextFieldAlias().setEnabled(true);
		this.getKeyStoreConfigPanel().getJTextFieldCity().setText(Locality);
		this.getKeyStoreConfigPanel().getJTextFieldCountryCode().setText(CountryCode);
		this.getKeyStoreConfigPanel().getJTextFieldFullName().setText(FullName);
		this.getKeyStoreConfigPanel().getJTextFieldOrganization().setText(Organization);
		this.getKeyStoreConfigPanel().getJTextFieldOrganizationalUnit().setText(OrganizationalUnit);
		this.getKeyStoreConfigPanel().getJTextFieldState().setText(State);
		this.getKeyStoreConfigPanel().getJTextFieldKeyStoreName()
				.setText(getKeyStorefilepath().substring(getKeyStorefilepath().lastIndexOf("\\") + 1));
		this.getKeyStoreConfigPanel().getJTextFieldAlias().setText(getKeyStoreAlias());
		this.getKeyStoreConfigPanel().getJPasswordField().setText(getKeyStorePassword());
		this.getKeyStoreConfigPanel().getJPasswordConfirmPassword().setText(null);
		this.getJLabelKeyStoreLocationPath().setText(getKeyStorefilepath());
		this.getJLabelKeyStoreLocationPath().setToolTipText(getKeyStorefilepath());
		keyStoreButtonPressed = "UpdateKeyStore";
	}
	/**
	 * Get TrustStore informations and fill in fields with
	 */
	public void getTrustStoreData() {

		// ---- Prepare the TrustStore form for update -------------
		this.getTrustStoreConfigPanel().getJTextFieldTrustStoreName()
				.setText(getTrustStorefilepath().substring(getTrustStorefilepath().lastIndexOf("\\") + 1));
		this.getTrustStoreConfigPanel().getJTextFieldTrustStoreName().setEnabled(false);
		this.getTrustStoreConfigPanel().getJLabelCertificatesList().setVisible(true);
		this.getTrustStoreConfigPanel().getScrollPane().setVisible(true);
		this.getTrustStoreConfigPanel().getJButtonAddCertificate().setVisible(true);
		this.getTrustStoreConfigPanel().getJButtonRemoveCertificate().setVisible(true);
		this.getTrustStoreConfigPanel().getListCertificatesAlias()
				.setModel(TrustStoreController.CertificatesAliaslist(getTrustStorefilepath(), getTrustStorePassword()));
		this.getTrustStoreConfigPanel().getJPasswordFieldTrustStorePassword().setText(getTrustStorePassword());
		this.getTrustStoreConfigPanel().getJPasswordFieldTrustStorePassword().setEnabled(true);
		this.getTrustStoreConfigPanel().getJPasswordFieldTrustStoreConfirmPassword().setText(null);
		this.getTrustStoreConfigPanel().getJPasswordFieldTrustStoreConfirmPassword().setEnabled(true);
		this.getJLabelTrustStoreLocationPath().setText(getTrustStorefilepath());
		this.getJLabelTrustStoreLocationPath().setToolTipText(getTrustStorefilepath());
		trustStoreButtonPressed = "EditTrustStore";
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// ---- Create new KeyStore -------------------------------
		if (ae.getSource() == this.getJButtonCreateKeyStore()) {
			// ----- Prepare the KeyStore form --------------------
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldCountryCode());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldCity());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldFullName());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldKeyStoreName());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldOrganization());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldOrganizationalUnit());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldState());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJPasswordField());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJPasswordConfirmPassword());
			setFieldsEnabledTrue(keyStoreConfigPanel.getJTextFieldAlias());
			keyStoreConfigPanel.getJTextFieldKeyStoreName().requestFocus();
			jLabelKeyStoreLocationPath.setText(null);
			keyStorefilepath = null;
			keyStoreButtonPressed = "CreateKeyStore";
			
		}else if (ae.getSource() == this.getJButtonOpenKeyStore()) {
			// ---- Open a KeyStore --------------------------------------------
			// ---- open JfileChooser ------------------------------------------
			getJFileChooser();
			int jfile = jFileChooser.showOpenDialog(null);
			if (jfile == JFileChooser.APPROVE_OPTION) {
				// -------- Get the path of selected KeyStore file -------------
				keyStorefilepath = jFileChooser.getSelectedFile().getPath();
				// ---- Get the KeyStoreName -----------------------------------
				String keystorename = keyStorefilepath.substring(keyStorefilepath.lastIndexOf("\\") + 1);
				// ---- Verify if the selected file is a KeyStore --------------
				if (keystorename.endsWith("KeyStore.jks")) {
					// --- Create JOptionPane to enter the KeyStore password ---
					jPanelPassword = new JPanel();
					jLabelEnterPassword = new JLabel("Please enter the password for  " + keystorename + "  :");
					jLabelEnterPassword.setFont(new Font("Dialog", Font.BOLD, 11));
					JPasswordField jPasswordField = new JPasswordField(10);
					jPasswordField.setText("123456");
					jPanelPassword.add(jLabelEnterPassword);
					jPanelPassword.add(jPasswordField);
					String[] options = new String[] { "OK", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, jPanelPassword, "Password", JOptionPane.NO_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options, jPasswordField);
					if (option == 0) {
						// ------------ Press OK --------------------------------
						keyStorePassword = new String(jPasswordField.getPassword());
						setKeyStoreAlias(KeyStoreController.GetKeyStoreAlias(keyStorefilepath, keyStorePassword)); 
						if (keyStoreAlias == null) {
							jLabelKeyStoreLocationPath.setText(null);
							keyStorefilepath = null;
						} else {
							getKeyStoreData();
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame,
							"Please choose a KeyStore file ! \nYour KeyStore name should ends with 'KeyStore' ",
							"Warning message", JOptionPane.WARNING_MESSAGE);
				}
			} else if (jfile == JFileChooser.CANCEL_OPTION) {
				// ------ Close the jFileChooser ---------------------------------
				jFileChooser.remove(jFileChooser);
			}

		} else if (ae.getSource() == this.getJButtonCreateTrustStore()) {
			// ---- Create new TrustStore ----------------------------------------
			// ---- Prepared the TrusStore form ----------------------------------
			trustStoreConfigPanel.getJTextFieldTrustStoreName().requestFocus();
			setFieldsEnabledTrue(trustStoreConfigPanel.getJTextFieldTrustStoreName());
			trustStoreConfigPanel.getJLabelCertificatesList().setVisible(false);
			trustStoreConfigPanel.getScrollPane().setVisible(false);
			trustStoreConfigPanel.getJButtonAddCertificate().setVisible(false);
			trustStoreConfigPanel.getJButtonRemoveCertificate().setVisible(false);
			trustStoreConfigPanel.getJPasswordFieldTrustStoreConfirmPassword().setText(null);
			trustStoreConfigPanel.getJPasswordFieldTrustStorePassword().setText(null);
			trustStoreConfigPanel.getJPasswordFieldTrustStoreConfirmPassword().setEnabled(true);
			trustStoreConfigPanel.getJPasswordFieldTrustStorePassword().setEnabled(true);
			jLabelTrustStoreLocationPath.setText(null);
			trustStorefilepath = null;
			trustStoreButtonPressed = "CreateTrustStore";
			
		} else if (ae.getSource() == this.getJButtonOpenTrustStore()) {
			// ---- Open a TrustStore ---------------------------------------
			// ---- open JfileChooser ---------------------------------------
			getJFileChooser();
			int result = jFileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				// ---- Get the path of selected TrustStore file ------------
				trustStorefilepath = jFileChooser.getSelectedFile().getPath();
				// ---- Get the TrustStoreName ------------------------------
				String truststorename = trustStorefilepath.substring(trustStorefilepath.lastIndexOf("\\") + 1);
				// ---- Verify if the selected file is a TrustStore ---------
				if (truststorename.endsWith("TrustStore.jks")) {
					// --- Create JOptionPane to enter the TrustStore password -
					JPanel jPanelEnterPassword = new JPanel();
					jLabelEnterPassword = new JLabel("Please enter the password for  " + truststorename + "  :");
					jLabelEnterPassword.setFont(new Font("Dialog", Font.BOLD, 11));
					JPasswordField jPasswordFiled = new JPasswordField(10);
					jPasswordFiled.setText("123456");
					jPanelEnterPassword.add(jLabelEnterPassword);
					jPanelEnterPassword.add(jPasswordFiled);
					String[] options = new String[] { "OK", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, jPanelEnterPassword, "Password",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, jPasswordFiled);
					trustStorePassword = new String(jPasswordFiled.getPassword());
					if (option == 0) {
						// ---- Press OK Button ------------------------------
						String alias = TrustStoreController.GetTrustStoreAlias(trustStorefilepath, trustStorePassword);
						if (alias == null) {
							jLabelTrustStoreLocationPath.setText(null);
							trustStorefilepath = null;
						} else {
							getTrustStoreData();
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame,
							"Please choose a TrustStore file ! \nYour TrustStore name should ends with 'TrustStore' ",
							"Warning message", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				// ---- Close jFileChosser -----------------------------------
				jFileChooser.remove(jFileChooser);
			}
		} else if (ae.getSource() == this.getJButtonCancel()) {
			// ---- Button Cancel is pressed ---------------------------------
			setCanceled(true);
			dispose();
		} else if (ae.getSource() == this.getJButtonOK()) {
			// ---- Button OK is pressed -------------------------------------
			if(getKeyStorefilepath() == null){
				JOptionPane.showMessageDialog(frame,
						"You should open a KeyStore to run the HTTPS MTP !",
						"Warning message", JOptionPane.WARNING_MESSAGE);
			}else if(getTrustStorefilepath() == null){
				JOptionPane.showMessageDialog(frame,
						"You should open a TrustStore to run the HTTPS MTP !",
						"Warning message", JOptionPane.WARNING_MESSAGE);
			}else{
				setCanceled(false);
				setVisible(false);
			}
		}
	}

}