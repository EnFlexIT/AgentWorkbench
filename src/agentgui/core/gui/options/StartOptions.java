package agentgui.core.gui.options;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

public class StartOptions extends JPanel implements ActionListener {

	private static final long serialVersionUID = -5837814050254569584L;
	
	private JPanel jPanelTop = null;
	private JPanel jPanelMiddle = null;
	private JPanel jPanelBottom = null;

	private JLabel jLabelServerHeader = null;
	private JLabel jLabelRunsAs = null;
	private JLabel jLabelJadeConfig = null;
	private JLabel jLabelMasterURL = null;
	private JLabel jLabelMasterPort = null;
	private JLabel jLabelMasterPort4MTP = null;
	private JLabel jLabelPort4MTP = null;
	private JLabel jLabelPort = null;
	
	private JLabel jLabelDBtitle = null;
	private JLabel jLabelDBHost = null;
	private JLabel jLabelDB = null;
	private JLabel jLabelDBUser = null;
	private JLabel jLabelDBpswd = null;
	private JLabel jLabelDummy = null;
	
	public JRadioButton jRadioButtonRunAsApplication = null;
	public JRadioButton jRadioButtonRunAsServer = null;
	public JCheckBox jCheckBoxAutoStart = null;
	public JTextField jTextFieldMasterURL = null;
	public JTextField jTextFieldMasterPort = null;
	public JTextField jTextFieldMasterPort4MTP = null;

	public JTextField jTextFieldDBHost = null;
	public JTextField jTextFieldDB = null;
	public JTextField jTextFieldDBUser = null;
	public JTextField jTextFieldDBPswd = null;

	/**
	 * This is the Constructor
	 */
	public StartOptions() {
		super();
		initialize();
		
		// --- Übersetzungen einstellen ----------------------------------------
		jLabelRunsAs.setText(Language.translate("Starte Agent.GUI als:"));
		jRadioButtonRunAsApplication.setText(Language.translate("Anwendung"));
		jRadioButtonRunAsServer.setText(Language.translate("Server-Dienst (Master / Slave)"));
		jLabelServerHeader.setText(Language.translate("Server-Konfiguration"));
		jCheckBoxAutoStart.setText(" " + Language.translate("Server-Dienst beim Programmstart automatisch initialisieren"));
		jLabelJadeConfig.setText( Application.RunInfo.getApplicationTitle() + " " + Language.translate("Hauptserver (Server.Master)") );
		jLabelDBtitle.setText(Language.translate("Datenbank für das Hauptserver-Modul"));
		
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints7.weightx = 0.0;
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.weighty = 1.0;
		gridBagConstraints7.gridy = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.fill = GridBagConstraints.NONE;
		gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints3.weighty = 0.0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.weightx = 0.0;
		gridBagConstraints3.gridy = 2;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints21.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.gridy = 0;
		jLabelRunsAs = new JLabel();
		jLabelRunsAs.setText("Starte Agent.GUI als:");
		jLabelRunsAs.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelRunsAs.setSize(new Dimension(122, 16));
		jLabelRunsAs.setLocation(new Point(0, 4));
		jLabelRunsAs.setToolTipText("");
		this.setSize(540, 400);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1));
		this.add(getJPanelTop(), gridBagConstraints21);
		this.add(getJPanelMiddle(), gridBagConstraints3);
		this.add(getJPanelBottom(), gridBagConstraints7);
	}

	/**
	 * This method initializes jRadioButtonRunAsApplication	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRunAsApplication() {
		if (jRadioButtonRunAsApplication == null) {
			jRadioButtonRunAsApplication = new JRadioButton();
			jRadioButtonRunAsApplication.setText("Anwendung");
			jRadioButtonRunAsApplication.setSelected(true);
			jRadioButtonRunAsApplication.setSize(new Dimension(121, 24));
			jRadioButtonRunAsApplication.setLocation(new Point(150, 0));
			jRadioButtonRunAsApplication.setPreferredSize(new Dimension(150, 24));
			jRadioButtonRunAsApplication.setActionCommand("runAsApplication");
			jRadioButtonRunAsApplication.addActionListener(this);
		}
		return jRadioButtonRunAsApplication;
	}

	/**
	 * This method initializes jRadioButtonRunAsServer	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRunAsServer() {
		if (jRadioButtonRunAsServer == null) {
			jRadioButtonRunAsServer = new JRadioButton();
			jRadioButtonRunAsServer.setPreferredSize(new Dimension(200, 24));
			jRadioButtonRunAsServer.setSize(new Dimension(216, 24));
			jRadioButtonRunAsServer.setLocation(new Point(280, 0));
			jRadioButtonRunAsServer.setText("Server-Dienst (Master / Slave)");
			jRadioButtonRunAsServer.setActionCommand("runAsServer");
			jRadioButtonRunAsServer.addActionListener(this);

		}
		return jRadioButtonRunAsServer;
	}

	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			jPanelTop = new JPanel();
			jPanelTop.setLayout(null);
			jPanelTop.setPreferredSize(new Dimension(500, 25));
			jPanelTop.add(jLabelRunsAs, null);
			jPanelTop.add(getJRadioButtonRunAsApplication(), null);
			jPanelTop.add(getJRadioButtonRunAsServer(), null);
			
			ButtonGroup runAsGroup = new ButtonGroup();
			runAsGroup.add(jRadioButtonRunAsApplication);
			runAsGroup.add(jRadioButtonRunAsServer);
			
		}
		return jPanelTop;
	}

	/**
	 * This method initializes jPanelMiddle	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelMiddle() {
		if (jPanelMiddle == null) {
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 2;
			gridBagConstraints41.anchor = GridBagConstraints.WEST;
			gridBagConstraints41.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints41.gridy = 5;
			jLabelPort = new JLabel();
			jLabelPort.setText("1099 = \"myServer:1099/JADE\"");
			jLabelPort.setPreferredSize(new Dimension(280, 16));
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 2;
			gridBagConstraints31.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridy = 6;
			jLabelPort4MTP = new JLabel();
			jLabelPort4MTP.setText("7778 = \"http://myServer:7778/acc\"");
			jLabelPort4MTP.setPreferredSize(new Dimension(280, 16));
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints22.gridy = 6;
			jLabelMasterPort4MTP = new JLabel();
			jLabelMasterPort4MTP.setText("Port-MTP");
			jLabelMasterPort4MTP.setPreferredSize(new Dimension(55, 16));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 6;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints6.gridy = 5;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.NONE;
			gridBagConstraints51.gridy = 4;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints51.anchor = GridBagConstraints.WEST;
			gridBagConstraints51.gridwidth = 2;
			gridBagConstraints51.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints4.gridy = 5;
			jLabelMasterPort = new JLabel();
			jLabelMasterPort.setText("Port");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 4;
			jLabelMasterURL = new JLabel();
			jLabelMasterURL.setText("URL / IP");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(20, 0, 0, 0);
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.gridy = 3;
			jLabelJadeConfig = new JLabel();
			jLabelJadeConfig.setText("Agent.GUI Hauptserver (Server.Master)");
			jLabelJadeConfig.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(10, 0, 0, 10);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 3;
			gridBagConstraints.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints5.gridy = 0;
			jLabelServerHeader = new JLabel();
			jLabelServerHeader.setText("Server-Konfiguration");
			jLabelServerHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			jPanelMiddle = new JPanel();
			jPanelMiddle.setLayout(new GridBagLayout());
			jPanelMiddle.add(jLabelServerHeader, gridBagConstraints5);
			jPanelMiddle.add(getJCheckBoxAutoStart(), gridBagConstraints);
			jPanelMiddle.add(jLabelJadeConfig, gridBagConstraints1);
			jPanelMiddle.add(jLabelMasterURL, gridBagConstraints2);
			jPanelMiddle.add(jLabelMasterPort, gridBagConstraints4);
			jPanelMiddle.add(getJTextFieldMasterURL(), gridBagConstraints51);
			jPanelMiddle.add(getJTextFieldMasterPort(), gridBagConstraints6);
			jPanelMiddle.add(getJTextFieldMasterPort4MTP(), gridBagConstraints11);
			jPanelMiddle.add(jLabelMasterPort4MTP, gridBagConstraints22);
			jPanelMiddle.add(jLabelPort4MTP, gridBagConstraints31);
			jPanelMiddle.add(jLabelPort, gridBagConstraints41);
		}
		return jPanelMiddle;
	}

	/**
	 * This method initializes jCheckBoxAutoStart	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxAutoStart() {
		if (jCheckBoxAutoStart == null) {
			jCheckBoxAutoStart = new JCheckBox();
			jCheckBoxAutoStart.setText("Server-Dienst beim Programmstart automatisch initialisieren");
		}
		return jCheckBoxAutoStart;
	}

	/**
	 * This method initialises jTextFieldMasterURL	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMasterURL() {
		if (jTextFieldMasterURL == null) {
			jTextFieldMasterURL = new JTextField();
			jTextFieldMasterURL.setPreferredSize(new Dimension(400, 26));
		}
		return jTextFieldMasterURL;
	}

	/**
	 * This method initializes jTextFieldMasterPort	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMasterPort() {
		if (jTextFieldMasterPort == null) {
			jTextFieldMasterPort = new JTextField();
			jTextFieldMasterPort.setPreferredSize(new Dimension(100, 26));
			jTextFieldMasterPort.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldMasterPort;
	}

	/**
	 * This method initializes jTextFieldMasterPort4MTP	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMasterPort4MTP() {
		if (jTextFieldMasterPort4MTP == null) {
			jTextFieldMasterPort4MTP = new JTextField();
			jTextFieldMasterPort4MTP.setPreferredSize(new Dimension(100, 26));
			jTextFieldMasterPort4MTP.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldMasterPort4MTP;
	}

	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints18.weighty = 1.0;
			gridBagConstraints18.gridy = 3;
			jLabelDummy = new JLabel();
			jLabelDummy.setText(" ");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints16.gridy = 2;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints16.anchor = GridBagConstraints.EAST;
			gridBagConstraints16.gridx = 4;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridwidth = 3;
			gridBagConstraints17.gridy = 0;
			jLabelDBtitle = new JLabel();
			jLabelDBtitle.setText("Datenbank für das Hauptserver-Modul");
			jLabelDBtitle.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints15.anchor = GridBagConstraints.EAST;
			gridBagConstraints15.gridx = 4;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 3;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints14.gridy = 2;
			jLabelDBpswd = new JLabel();
			jLabelDBpswd.setText("DB-Pswd");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 3;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints13.gridy = 1;
			jLabelDBUser = new JLabel();
			jLabelDBUser.setText("DB-User");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridx = 2;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints8.gridy = 2;
			jLabelDB = new JLabel();
			jLabelDB.setText("DB");
			jLabelDBHost = new JLabel();
			jLabelDBHost.setText("DB-Host");
			jLabelDBHost.setPreferredSize(new Dimension(55, 16));
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.setPreferredSize(new Dimension(465, 104));
			jPanelBottom.add(jLabelDBHost, gridBagConstraints12);
			jPanelBottom.add(jLabelDB, gridBagConstraints8);
			jPanelBottom.add(getJTextFieldDBHost(), gridBagConstraints9);
			jPanelBottom.add(getJTextFieldDB(), gridBagConstraints10);
			jPanelBottom.add(jLabelDBUser, gridBagConstraints13);
			jPanelBottom.add(jLabelDBpswd, gridBagConstraints14);
			jPanelBottom.add(getJTextFieldDBUser(), gridBagConstraints15);
			jPanelBottom.add(jLabelDBtitle, gridBagConstraints17);
			jPanelBottom.add(getjTextFieldDBPswd(), gridBagConstraints16);
			jPanelBottom.add(jLabelDummy, gridBagConstraints18);
		}
		return jPanelBottom;
	}

	/**
	 * This method initializes jTextFieldDBHost	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDBHost() {
		if (jTextFieldDBHost == null) {
			jTextFieldDBHost = new JTextField();
			jTextFieldDBHost.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDBHost;
	}

	/**
	 * This method initializes jTextFieldDB	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDB() {
		if (jTextFieldDB == null) {
			jTextFieldDB = new JTextField();
			jTextFieldDB.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDB;
	}

	/**
	 * This method initializes jTextFieldDBUser	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDBUser() {
		if (jTextFieldDBUser == null) {
			jTextFieldDBUser = new JTextField();
			jTextFieldDBUser.setPreferredSize(new Dimension(120, 26));
		}
		return jTextFieldDBUser;
	}

	/**
	 * This method initializes jPasswordFieldDBpswd	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JTextField getjTextFieldDBPswd() {
		if (jTextFieldDBPswd == null) {
			jTextFieldDBPswd = new JPasswordField();
			jTextFieldDBPswd.setPreferredSize(new Dimension(120, 26));
		}
		return jTextFieldDBPswd;
	}

	/**
	 * This method handles all Action-Events from this class
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		if (actCMD.equalsIgnoreCase("runAsApplication")) {
			
		} else if (actCMD.equalsIgnoreCase("runAsServer")) {
		
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
		this.refreshView();
		
	}
	/**
	 * This methode handles the view 
	 */
	public void refreshView() {
		
		if ( jRadioButtonRunAsServer.isSelected() ) {
			jCheckBoxAutoStart.setEnabled(true);
			jTextFieldDBHost.setEnabled(true);
			jTextFieldDB.setEnabled(true);
			jTextFieldDBUser.setEnabled(true);
			jTextFieldDBPswd.setEditable(true);
		} else {
			jCheckBoxAutoStart.setEnabled(false);
			jTextFieldDBHost.setEnabled(false);
			jTextFieldDB.setEnabled(false);
			jTextFieldDBUser.setEnabled(false);
			jTextFieldDBPswd.setEditable(false);
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
