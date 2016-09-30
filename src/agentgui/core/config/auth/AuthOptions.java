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
package agentgui.core.config.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.options.AbstractOptionTab;
import agentgui.core.gui.options.OptionDialog;
import agentgui.core.gui.options.https.HttpsConfigWindow;

/**
 * On this JPanel the starting options of AgentGUI can be set.
 * 
 * @author Hanno - Felix Wagner - DAWIS - ICB - University of Duisburg - Essen
 */
public class AuthOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = -6953230375139010927L;

	private JButton btnSave;
	private JButton btnConfigTrust;

	private JScrollPane spLog;
	private JTextArea taLog;

	public static final String COMMAND_CONFIGURETRUST = "configureTrust";
	public static final String COMMAND_SAVE = "save";
	private OIDCPanel panel;

	/**
	 * This is the Constructor
	 */
	public AuthOptions(OptionDialog optionDialog) {
		super(optionDialog);

		this.initialize();
		this.setGlobalData2Form();

	}

	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Authorisierung");
	}

	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Authorisierung");
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.insets = new Insets(20, 20, 5, 20);
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.weighty = 0.0;
		gridBagConstraints21.gridy = 0;

		this.setSize(770, 440);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		this.setLayout(gridBagLayout);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.anchor = GridBagConstraints.EAST;
		gbc_btnSave.insets = new Insets(10, 10, 10, 10);
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 0;
		this.add(getBtnSave(), gbc_btnSave);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(getPanel(), gbc_panel);

		GridBagConstraints gbc_btnConfigTrust = new GridBagConstraints();
		gbc_btnConfigTrust.anchor = GridBagConstraints.EAST;
		gbc_btnConfigTrust.insets = new Insets(0, 0, 5, 10);
		gbc_btnConfigTrust.gridx = 0;
		gbc_btnConfigTrust.gridy = 2;
		this.add(getBtnConfigTrust(), gbc_btnConfigTrust);

		GridBagConstraints gbc_spLog = new GridBagConstraints();
		gbc_spLog.fill = GridBagConstraints.BOTH;
		gbc_spLog.insets = new Insets(10, 10, 10, 10);

		gbc_spLog.gridx = 0;
		gbc_spLog.gridy = 3;
		add(getSpLog(), gbc_spLog);

	}

	private JScrollPane getSpLog() {
		if (spLog == null) {
			spLog = new JScrollPane();
			spLog.setViewportView(getTaLog()); // ColumnHeaderView();
		}
		return spLog;
	}

	private JTextArea getTaLog() {
		if (taLog == null) {
			taLog = new JTextArea();
		}
		return taLog;
	}

	private JButton getBtnConfigTrust() {
		if (btnConfigTrust == null) {
			btnConfigTrust = new JButton(Language.translate("Vertrauen konfigurieren"));
			btnConfigTrust.setActionCommand(COMMAND_CONFIGURETRUST);
			btnConfigTrust.addActionListener(this);
		}
		return btnConfigTrust;
	}

	private JButton getBtnSave() {
		if (btnSave == null) {
			final ImageIcon iconSave = new ImageIcon( this.getClass().getResource( Application.getGlobalInfo().getPathImageIntern() + "MBsave.png") );

			btnSave = new JButton();
			btnSave.setActionCommand(COMMAND_SAVE);
			btnSave.addActionListener(this);
			btnSave.setToolTipText(Language.translate("Speichern"));
			btnSave.setForeground(new Color(0, 0, 255));
			btnSave.setFont(new Font("Dialog", Font.BOLD, 12));
			btnSave.setPreferredSize(new Dimension(26, 26));
			btnSave.setIcon(iconSave);
		}
		return btnSave;
	}

	/**
	 * This method sets the Data from the global Area to the Form.
	 */
	private void setGlobalData2Form() {
		panel.getTfUsername().setText(Application.getGlobalInfo().getOIDCUsername());
	}

	public void setFormData2Global() {
		Application.getGlobalInfo().setOIDCUsername(panel.getTfUsername().getText());
	}

	public void log(String s) {
		try {
			Document doc = getTaLog().getDocument();
			doc.insertString(doc.getLength(), s + "\n", null);
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		String actCMD = ae.getActionCommand();
		if (actCMD.equalsIgnoreCase(COMMAND_SAVE)) {
			setFormData2Global();
		} else if (actCMD.equalsIgnoreCase(COMMAND_CONFIGURETRUST)) {
			// --- Open the HttpsConfigWindow ----------------------------------
//			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(this.optionDialog, getKeyStore(), getKeyStorePassword(), getTrustStore(), getTrustStorePassword());
			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(this.optionDialog);

			// --- Wait for the user -------------------------------------------
			if (httpsConfigWindow.isCanceled() == false) {
				// ---- Return the TrustStore chosen by the user --
//				this.setTrustStore(httpsConfigWindow.getTrustStorefilepath());
//				this.setTrustStorePassword(httpsConfigWindow.getTrustStorePassword());
//				this.getJTextFieldTrustStorePath().setText(this.getTrustStore());
			}
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = OIDCAuthorization.getInstance().getPanel(this);
		}
		return panel;
	}
}  // @jve:decl-index=0:visual-constraint="-3,8"
