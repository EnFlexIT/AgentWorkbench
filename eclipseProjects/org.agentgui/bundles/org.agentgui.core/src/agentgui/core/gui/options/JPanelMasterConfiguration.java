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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.gui.components.JComboBoxMtpProtocol;

/**
 * On this JPanel the starting options of AgentGUI can be set.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelMasterConfiguration extends AbstractJPanelForOptions {

	private static final long serialVersionUID = -5837814050254569584L;
	
	private JLabel jLabelMasterHeader;
	private JLabel jLabelMasterURL;
	private JLabel jLabelMasterPort;
	private JLabel jLabelMasterPort4MTP;
	private JLabel jLabelPort4MTP;
	private JLabel jLabelPort;
	private JLabel jLabel;

	private JTextField jTextFieldMasterURL;
	private JTextField jTextFieldMasterPort;
	private JTextField jTextFieldMasterPort4MTP;

	private JComboBoxMtpProtocol jComboBoxMtpProtocol;
	
	/**
	 * This is the Constructor.
	 * @param optionDialog the option dialog
	 */
	public JPanelMasterConfiguration(OptionDialog optionDialog, StartOptions startOptions) {
		super(optionDialog, startOptions);
		this.initialize();
		
		// --- Translate ----------------------------------
		jLabelMasterHeader.setText(Application.getGlobalInfo().getApplicationTitle() + " " + Language.translate("Hauptserver (server.master)") );
	}
	/**
	 * Initialise.
	 */
	private void initialize() {
		this.setPreferredSize(new Dimension(640, 115));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{55, 95, 12, 460, 0};
		gridBagLayout.rowHeights = new int[]{16, 26, 26, 26, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		
		jLabelMasterHeader = new JLabel();
		jLabelMasterHeader.setText("Agent.GUI Hauptserver (server.master)");
		jLabelMasterHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		
		
		GridBagConstraints gbc_jLabelMasterHeader = new GridBagConstraints();
		gbc_jLabelMasterHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelMasterHeader.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelMasterHeader.gridwidth = 4;
		gbc_jLabelMasterHeader.gridx = 0;
		gbc_jLabelMasterHeader.gridy = 0;
		this.add(jLabelMasterHeader, gbc_jLabelMasterHeader);
		
		jLabelMasterURL = new JLabel();
		jLabelMasterURL.setText("URL / IP");
		GridBagConstraints gbc_jLabelMasterURL = new GridBagConstraints();
		gbc_jLabelMasterURL.anchor = GridBagConstraints.WEST;
		gbc_jLabelMasterURL.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelMasterURL.gridx = 0;
		gbc_jLabelMasterURL.gridy = 1;
		this.add(jLabelMasterURL, gbc_jLabelMasterURL);
		GridBagConstraints gbc_jComboBoxMtpProtocol = new GridBagConstraints();
		gbc_jComboBoxMtpProtocol.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxMtpProtocol.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxMtpProtocol.gridx = 1;
		gbc_jComboBoxMtpProtocol.gridy = 1;
		this.add(this.getJcomboboxMtpProtocol(), gbc_jComboBoxMtpProtocol);
		
		jLabel = new JLabel("//:");
		GridBagConstraints gbc_jLabel = new GridBagConstraints();
		gbc_jLabel.anchor = GridBagConstraints.EAST;
		gbc_jLabel.insets = new Insets(0, 0, 5, 5);
		gbc_jLabel.gridx = 2;
		gbc_jLabel.gridy = 1;
		add(jLabel, gbc_jLabel);
				GridBagConstraints gbc_jTextFieldMasterURL = new GridBagConstraints();
				gbc_jTextFieldMasterURL.fill = GridBagConstraints.HORIZONTAL;
				gbc_jTextFieldMasterURL.insets = new Insets(0, 0, 5, 0);
				gbc_jTextFieldMasterURL.gridx = 3;
				gbc_jTextFieldMasterURL.gridy = 1;
				this.add(this.getJTextFieldMasterURL(), gbc_jTextFieldMasterURL);
		
				jLabelMasterPort = new JLabel();
				jLabelMasterPort.setText("Port");
				GridBagConstraints gbc_jLabelMasterPort = new GridBagConstraints();
				gbc_jLabelMasterPort.anchor = GridBagConstraints.WEST;
				gbc_jLabelMasterPort.insets = new Insets(0, 0, 5, 5);
				gbc_jLabelMasterPort.gridx = 0;
				gbc_jLabelMasterPort.gridy = 2;
				this.add(jLabelMasterPort, gbc_jLabelMasterPort);
		GridBagConstraints gbc_jTextFieldMasterPort = new GridBagConstraints();
		gbc_jTextFieldMasterPort.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldMasterPort.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldMasterPort.gridwidth = 2;
		gbc_jTextFieldMasterPort.gridx = 1;
		gbc_jTextFieldMasterPort.gridy = 2;
		this.add(this.getJTextFieldMasterPort(), gbc_jTextFieldMasterPort);
		
		jLabelPort = new JLabel();
		jLabelPort.setText("1099 = \"myServer:1099/JADE\"");
		jLabelPort.setPreferredSize(new Dimension(220, 16));
		GridBagConstraints gbc_jLabelPort = new GridBagConstraints();
		gbc_jLabelPort.anchor = GridBagConstraints.WEST;
		gbc_jLabelPort.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelPort.gridx = 3;
		gbc_jLabelPort.gridy = 2;
		this.add(jLabelPort, gbc_jLabelPort);
		
		jLabelMasterPort4MTP = new JLabel();
		jLabelMasterPort4MTP.setText("Port-MTP");
		jLabelMasterPort4MTP.setPreferredSize(new Dimension(55, 16));
		GridBagConstraints gbc_jLabelMasterPort4MTP = new GridBagConstraints();
		gbc_jLabelMasterPort4MTP.anchor = GridBagConstraints.WEST;
		gbc_jLabelMasterPort4MTP.insets = new Insets(0, 0, 0, 5);
		gbc_jLabelMasterPort4MTP.gridx = 0;
		gbc_jLabelMasterPort4MTP.gridy = 3;
		this.add(jLabelMasterPort4MTP, gbc_jLabelMasterPort4MTP);
		GridBagConstraints gbc_jTextFieldMasterPort4MTP = new GridBagConstraints();
		gbc_jTextFieldMasterPort4MTP.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldMasterPort4MTP.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldMasterPort4MTP.gridwidth = 2;
		gbc_jTextFieldMasterPort4MTP.gridx = 1;
		gbc_jTextFieldMasterPort4MTP.gridy = 3;
		this.add(this.getJTextFieldMasterPort4MTP(), gbc_jTextFieldMasterPort4MTP);
		
		jLabelPort4MTP = new JLabel();
		jLabelPort4MTP.setText("7778 = \"http://myServer:7778/acc\"");
		jLabelPort4MTP.setPreferredSize(new Dimension(220, 16));
		GridBagConstraints gbc_jLabelPort4MTP = new GridBagConstraints();
		gbc_jLabelPort4MTP.anchor = GridBagConstraints.WEST;
		gbc_jLabelPort4MTP.gridx = 3;
		gbc_jLabelPort4MTP.gridy = 3;
		this.add(jLabelPort4MTP, gbc_jLabelPort4MTP);
	}
	
	/**
	 * This method initialises jTextFieldMasterURL	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMasterURL() {
		if (jTextFieldMasterURL == null) {
			jTextFieldMasterURL = new JTextField();
			jTextFieldMasterURL.setPreferredSize(new Dimension(460, 26));
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
	 * This method initializes jComboBoxMtpProtocol	
	 * @return JComboBoxMtpProtocol
	 */
	protected JComboBoxMtpProtocol getJcomboboxMtpProtocol(){
		if(jComboBoxMtpProtocol == null){
		   jComboBoxMtpProtocol = new JComboBoxMtpProtocol();
		   jComboBoxMtpProtocol.setPreferredSize(new Dimension(70, 26));
		}
		return jComboBoxMtpProtocol;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#refreshView()
	 */
	@Override
	public void refreshView() {
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setGlobalData2Form()
	 */
	@Override
	public void setGlobalData2Form(){
		
		this.getJTextFieldMasterURL().setText(getGlobalInfo().getServerMasterURL());
		this.getJTextFieldMasterPort().setText(getGlobalInfo().getServerMasterPort().toString());
		this.getJTextFieldMasterPort4MTP().setText(getGlobalInfo().getServerMasterPort4MTP().toString());
		this.getJcomboboxMtpProtocol().setSelectedProtocol(getGlobalInfo().getServerMasterProtocol());
	
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setFromData2Global()
	 */
	@Override
	public void setFormData2Global() {
		
		this.getGlobalInfo().setServerMasterURL(this.jTextFieldMasterURL.getText().trim());

		Integer usePort = Integer.parseInt(this.jTextFieldMasterPort.getText().trim());
		this.getGlobalInfo().setServerMasterPort(usePort);
		Integer usePort4MTP = Integer.parseInt(this.jTextFieldMasterPort4MTP.getText().trim());
		this.getGlobalInfo().setServerMasterPort4MTP(usePort4MTP);
		this.getGlobalInfo().setServerMasterProtocol(getJcomboboxMtpProtocol().getSelectedProtocol());
		
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#errorFound()
	 */
	@Override
	public boolean errorFound() {
		
		String msgHead = null;
		String msgText = null;
		boolean err = false;
		// --------------------------------------------------------------
		// --- Error Check for URL and Ports ----------------------------
		// --------------------------------------------------------------
		String testURL = this.jTextFieldMasterURL.getText();
		if (testURL!=null) {
			testURL.trim();
		}
		
		boolean checkJadeSettings = true;
		if (this.getSelectedExecutionMode()==ExecutionMode.SERVER ) {
			checkJadeSettings = true;
		} else {
			checkJadeSettings = (testURL!=null && testURL.equalsIgnoreCase("")==false);
		}
		
		// --- Testing URL and Port -------------------------------------
		if (checkJadeSettings==true) {

			// --- Prepare for tests: Parse the Port configuration ------
			String testPortAsString = this.jTextFieldMasterPort.getText().trim();
			if (testPortAsString==null || testPortAsString.equals("")==true) {
				this.jTextFieldMasterPort.setText("0");
				testPortAsString = "0";
			}
			int testPortAsInteger = Integer.parseInt( testPortAsString );
					
			String  testPort4MTPAsString = this.jTextFieldMasterPort4MTP.getText().trim();
			if (testPort4MTPAsString==null || testPort4MTPAsString.equals("")==true) {
				this.jTextFieldMasterPort4MTP.setText("0");
				testPort4MTPAsString = "0";
			}
			int testPort4MTPAsInteger = Integer.parseInt( testPort4MTPAsString );
			
			
			// --- Test URL for null value ------------------------------
			if ( testURL==null || testURL.equals("")==true) {
				msgHead = Language.translate("Fehler: URL oder IP !");
				msgText = Language.translate("Bitte geben Sie die URL oder IP des Hauptservers an!");	
				JOptionPane.showMessageDialog( this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				return true;
			}
			// --- Test URL for blanks ----------------------------------
			if ( testURL.contains(" ") ) {
				msgHead = Language.translate("Fehler: URL oder IP !");
				msgText = Language.translate("Die URL oder IP enthält unzulässige Zeichen!");	
				JOptionPane.showMessageDialog( this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				return true;
			}
			// --- Try to resolve server address --------------------
			if (isValidServerAddress(testURL)==false) {
				msgHead = Language.translate("Fehler: URL oder IP !");
				msgText = Language.translate("Die URL oder IP konnte nicht aufgelöst werden!") + "\n";
				msgText += Language.translate("Soll die aktuelle Einstellung trotzdem übernommen werden?");
				int answer = JOptionPane.showConfirmDialog( this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.YES_OPTION);
				if (answer==JOptionPane.NO_OPTION) {
					return true;
				}
			}
			// --- Testing the Port ---------------------------------
			if ( testPortAsInteger==0 ) {
				msgHead = Language.translate("Fehler: Port");
				msgText = Language.translate("Der Port muss einem Wert ungleich 0 entsprechen!");	
				JOptionPane.showMessageDialog( this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				return true;
			}
			// --- Testing the Port 4 MTP ---------------------------
			if ( testPort4MTPAsInteger==0 ) {
				msgHead = Language.translate("Fehler: Port für MTP ");
				msgText = Language.translate("Der Port für die MTP-Adresse muss einem Wert ungleich 0 entsprechen!");	
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				return true;
			}
			
		}
		return err;
	}
	
	/**
	 * Checks if is the specified server address is a valid server address.
	 *
	 * @param serverAddressToCheck the server address to check
	 * @return true, if is valid server address
	 */
	private boolean isValidServerAddress(String serverAddressToCheck) {

		boolean vaidMasterHost = false;
		try {
			InetAddress.getByName(serverAddressToCheck);
			vaidMasterHost = true;
			
		} catch (UnknownHostException uhe) {
//			uhe.printStackTrace();
		}
		return vaidMasterHost;
	}
	
	
}
