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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.network.NetworkAddresses;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.PlatformJadeConfig.MTP_Creation;

/**
 * Represents the JPanel/Tab 'Configuration' - 'JADE-Configuration'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelOwnMTP extends AbstractJPanelForOptions implements ActionListener {

	private static final long serialVersionUID = -7016775471452161527L;
	
	private JRadioButton jRadioButtonMtpAutoConfig;
	private JRadioButton jRadioButtonMtpIP;
	
	private JTextField jTextFieldDefaultPortMTP;
	private JButton jButtonSetPortMTPDefault;
	private JLabel jLabelMTPport;
	
	private JLabel jLabelIP;
	private JTextField jTextFieldIPAddress;
	private JButton jButtonIPedit;
	private JSeparator jSeparator2;
	private JLabel jLabelLocaleMtpAddress;
	
	
	/**
	 * Constructor of this class.
	 *
	 * @param optionDialog the option dialog
	 * @param startOptions the start options
	 */
	public JPanelOwnMTP(OptionDialog optionDialog, StartOptions startOptions) {
		super(optionDialog, startOptions);
		this.initialize();
		// --- Translate ----------------------------------
		jLabelLocaleMtpAddress.setText(Language.translate("Eigene lokale MTP-Adresse"));
		jRadioButtonMtpAutoConfig.setText(Language.translate("JADE-Automatik verwenden"));
		jRadioButtonMtpIP.setText(Language.translate("IP-Adresse verwenden"));
		jButtonSetPortMTPDefault.setToolTipText(Language.translate("Standard verwenden"));
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
	
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(500, 80);

		GridBagConstraints gbc_jRadioButtonMtpAutoConfig = new GridBagConstraints();
		gbc_jRadioButtonMtpAutoConfig.gridwidth = 2;
		gbc_jRadioButtonMtpAutoConfig.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonMtpAutoConfig.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonMtpAutoConfig.gridx = 0;
		gbc_jRadioButtonMtpAutoConfig.gridy = 1;

		GridBagConstraints gbc_jRadioButtonMtpIP = new GridBagConstraints();
		gbc_jRadioButtonMtpIP.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonMtpIP.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonMtpIP.gridx = 2;
		gbc_jRadioButtonMtpIP.gridy = 1;
		
		GridBagConstraints gbc_jButtonIPedit = new GridBagConstraints();
		gbc_jButtonIPedit.anchor = GridBagConstraints.EAST;
		gbc_jButtonIPedit.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonIPedit.gridx = 3;
		gbc_jButtonIPedit.gridy = 1;
		
		GridBagConstraints gbc_jSeparator2 = new GridBagConstraints();
		gbc_jSeparator2.fill = GridBagConstraints.VERTICAL;
		gbc_jSeparator2.gridheight = 2;
		gbc_jSeparator2.insets = new Insets(0, 10, 0, 10);
		gbc_jSeparator2.gridx = 4;
		gbc_jSeparator2.gridy = 1;

		GridBagConstraints gbc_jLabelMTPport = new GridBagConstraints();
		gbc_jLabelMTPport.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelMTPport.gridx = 5;
		gbc_jLabelMTPport.gridy = 1;
		
		GridBagConstraints gbc_jTextFieldDefaultPortMTP = new GridBagConstraints();
		gbc_jTextFieldDefaultPortMTP.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldDefaultPortMTP.gridx = 6;
		gbc_jTextFieldDefaultPortMTP.gridy = 1;

		GridBagConstraints gbc_jButtonSetPortMTPDefault = new GridBagConstraints();
		gbc_jButtonSetPortMTPDefault.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonSetPortMTPDefault.gridx = 7;
		gbc_jButtonSetPortMTPDefault.gridy = 1;
		
		GridBagConstraints gbc_jLabelIP = new GridBagConstraints();
		gbc_jLabelIP.anchor = GridBagConstraints.EAST;
		gbc_jLabelIP.insets = new Insets(0, 0, 0, 5);
		gbc_jLabelIP.gridx = 0;
		gbc_jLabelIP.gridy = 2;

		GridBagConstraints gbc_jTextFieldIPAddress = new GridBagConstraints();
		gbc_jTextFieldIPAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldIPAddress.gridwidth = 3;
		gbc_jTextFieldIPAddress.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldIPAddress.gridx = 1;
		gbc_jTextFieldIPAddress.gridy = 2;
		
		GridBagConstraints gbc_lblLokaleMtpanschrift = new GridBagConstraints();
		gbc_lblLokaleMtpanschrift.anchor = GridBagConstraints.WEST;
		gbc_lblLokaleMtpanschrift.gridwidth = 4;
		gbc_lblLokaleMtpanschrift.insets = new Insets(0, 0, 5, 5);
		gbc_lblLokaleMtpanschrift.gridx = 0;
		gbc_lblLokaleMtpanschrift.gridy = 0;
		
		this.add(getJLabelLocaleMtpAddress(), gbc_lblLokaleMtpanschrift);
		this.add(getJRadioButtonMtpAutoConfig(), gbc_jRadioButtonMtpAutoConfig);
		this.add(getJRadioButtonMtpIP(), gbc_jRadioButtonMtpIP);
		this.add(getJButtonIPedit(), gbc_jButtonIPedit);
		this.add(getSeparator_1(), gbc_jSeparator2);
		this.add(getJLabelMTPport(), gbc_jLabelMTPport);
		this.add(getJTextFieldDefaultPortMTP(), gbc_jTextFieldDefaultPortMTP);
		this.add(getJButtonSetPortMTPDefault(), gbc_jButtonSetPortMTPDefault);
		this.add(getJLabelIP(), gbc_jLabelIP);
		this.add(getJTextFieldIPAddress(), gbc_jTextFieldIPAddress);
		
		// --- Create the ButtonGroup for the radio buttons ---------
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(this.getJRadioButtonMtpAutoConfig());
		bGroup.add(this.getJRadioButtonMtpIP());
		
	}

	/**
	 * Gets the JLabel locale MTP address.
	 * @return the JLabel locale MTP address
	 */
	private JLabel getJLabelLocaleMtpAddress() {
		if (jLabelLocaleMtpAddress == null) {
			jLabelLocaleMtpAddress = new JLabel("Eigene lokale MTP-Adresse");
			jLabelLocaleMtpAddress.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelLocaleMtpAddress;
	}
	/**
	 * Gets the JRadioButton auto configuration.
	 * @return the JRadioButton auto configuration
	 */
	private JRadioButton getJRadioButtonMtpAutoConfig() {
		if (jRadioButtonMtpAutoConfig == null) {
			jRadioButtonMtpAutoConfig = new JRadioButton("JADE-Automatik verwenden");
			jRadioButtonMtpAutoConfig.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonMtpAutoConfig.addActionListener(this);
		}
		return jRadioButtonMtpAutoConfig;
	}
	/**
	 * Gets the JRadioButton for IP usage.
	 * @return the JRadioButton IP usage 
	 */
	private JRadioButton getJRadioButtonMtpIP() {
		if (jRadioButtonMtpIP == null) {
			jRadioButtonMtpIP = new JRadioButton("Use IP-Address");
			jRadioButtonMtpIP.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonMtpIP.addActionListener(this);
		}
		return jRadioButtonMtpIP;
	}
	private JLabel getJLabelIP() {
		if (jLabelIP == null) {
			jLabelIP = new JLabel("IP:");
			jLabelIP.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelIP;
	}
	private JTextField getJTextFieldIPAddress() {
		if (jTextFieldIPAddress==null) {
			jTextFieldIPAddress = new JTextField();
			jTextFieldIPAddress.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldIPAddress.setPreferredSize(new Dimension(250, 26));
		}
		return jTextFieldIPAddress;
	}
	private JButton getJButtonIPedit() {
		if (jButtonIPedit == null) {
			jButtonIPedit = new JButton();
			jButtonIPedit.setToolTipText("IP auswählen");
			jButtonIPedit.setPreferredSize(new Dimension(45, 26));
			jButtonIPedit.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonIPedit.addActionListener(this);
		}
		return jButtonIPedit;
	}
	private JSeparator getSeparator_1() {
		if (jSeparator2 == null) {
			jSeparator2 = new JSeparator();
			jSeparator2.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparator2;
	}
	/**
	 * Gets the JLabel mtp port.
	 * @return the JLabel mtp port
	 */
	private JLabel getJLabelMTPport() {
		if (jLabelMTPport == null) {
			jLabelMTPport = new JLabel("MTP-Port:");
			jLabelMTPport.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelMTPport;
	}
	/**
	 * Gets the JTextFielddefault port mtp.
	 * @return the JTextField default port mtp
	 */
	private JTextField getJTextFieldDefaultPortMTP() {
		if (jTextFieldDefaultPortMTP == null) {
			jTextFieldDefaultPortMTP = new JTextField();
			jTextFieldDefaultPortMTP.setText((String) null);
			jTextFieldDefaultPortMTP.setPreferredSize(new Dimension(71, 26));
			jTextFieldDefaultPortMTP.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldDefaultPortMTP.addKeyListener( new KeyAdapter() {
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
		return jTextFieldDefaultPortMTP;
	}
	/**
	 * Gets the JButton set port mtp default.
	 * @return the JButton set port mtp default
	 */
	private JButton getJButtonSetPortMTPDefault() {
		if (jButtonSetPortMTPDefault == null) {
			jButtonSetPortMTPDefault = new JButton();
			jButtonSetPortMTPDefault.setToolTipText("Standard verwenden");
			jButtonSetPortMTPDefault.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortMTPDefault.setActionCommand("SetPortDefault");
			jButtonSetPortMTPDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonSetPortMTPDefault.addActionListener(this);
		}
		return jButtonSetPortMTPDefault;
	}
	
	/**
	 * Returns the currently selected MTP creation method.
	 * @return the current MTP_Creation of the view
	 */
	private MTP_Creation getMtpCreation() {
		if (this.getJRadioButtonMtpIP().isSelected()) {
			return MTP_Creation.ConfiguredByIPandPort;
		}
		return MTP_Creation.ConfiguredByJADE;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setGlobalData2Form()
	 */
	@Override
	public void setGlobalData2Form() {

		MTP_Creation mTP_Creation = this.getGlobalInfo().getOwnMtpCreation();
		switch (mTP_Creation) {
		case ConfiguredByJADE:
			this.getJRadioButtonMtpAutoConfig().setSelected(true);
			this.getJRadioButtonMtpIP().setSelected(false);
			break;

		case ConfiguredByIPandPort:
			this.getJRadioButtonMtpAutoConfig().setSelected(false);
			this.getJRadioButtonMtpIP().setSelected(true);
			break;
		}
		this.getJTextFieldIPAddress().setText(this.getGlobalInfo().getOwnMtpIP());
		this.getJTextFieldDefaultPortMTP().setText(this.getGlobalInfo().getOwnMtpPort().toString());
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setFormData2Global()
	 */
	@Override
	public void setFormData2Global() {

		this.getGlobalInfo().setOwnMtpCreation(this.getMtpCreation());
		this.getGlobalInfo().setOwnMtpIP(this.getJTextFieldIPAddress().getText());
		Integer ownMtpPort = Integer.parseInt(this.getJTextFieldDefaultPortMTP().getText().trim());
		this.getGlobalInfo().setOwnMtpPort(ownMtpPort);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#refreshView()
	 */
	@Override
	public void refreshView() {
		
		MTP_Creation mTP_Creation = this.getMtpCreation();
		switch (mTP_Creation) {
		case ConfiguredByJADE:
			this.getJLabelMTPport().setEnabled(false);
			this.getJTextFieldDefaultPortMTP().setEnabled(false);
			this.getJButtonSetPortMTPDefault().setEnabled(false);
			this.getJLabelIP().setEnabled(false);
			this.getJTextFieldIPAddress().setEnabled(false);
			this.getJButtonIPedit().setEnabled(false);
			this.getJTextFieldIPAddress().setText(PlatformJadeConfig.MTP_IP_AUTO_Config);
			break;

		case ConfiguredByIPandPort:
			this.getJLabelMTPport().setEnabled(true);
			this.getJTextFieldDefaultPortMTP().setEnabled(true);
			this.getJButtonSetPortMTPDefault().setEnabled(true);
			this.getJLabelIP().setEnabled(true);
			this.getJTextFieldIPAddress().setEnabled(true);
			this.getJButtonIPedit().setEnabled(true);
			break;
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
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		if (trigger==this.getJRadioButtonMtpAutoConfig()) {
			// --- Switch to MTP-auto configuration -------
			this.refreshView();
		} else if (trigger==this.getJRadioButtonMtpIP()) {
			// --- Switch to MTP-IP usage -----------------			
			this.refreshView();
			
		} else if (trigger==this.getJButtonIPedit()) {
			NetworkAddresses netAddresses = new NetworkAddresses();
			JPopupMenu popUp = netAddresses.getJPopupMenu4NetworkAddresses(this);
			popUp.show(this.getJTextFieldIPAddress(), 0, this.getJTextFieldIPAddress().getHeight());
			
		} else if (trigger instanceof JMenuItem) {
			// --- Trigger from JPopoupMenue for the IP-Addresses ---
			JMenuItem menuItem = (JMenuItem) trigger;
			this.getJTextFieldIPAddress().setText(menuItem.getActionCommand());
			
		} else if (trigger==this.getJButtonSetPortMTPDefault()) {
			// --- Set default MTP port -------------------
			this.getJTextFieldDefaultPortMTP().setText("7778");
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
