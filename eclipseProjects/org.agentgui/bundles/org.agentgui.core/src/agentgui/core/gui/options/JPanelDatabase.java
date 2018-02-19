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

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import agentgui.core.application.Language;

/**
 * The Class JPanelDatabase is used to set the global database settings.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelDatabase extends AbstractJPanelForOptions {

	private static final long serialVersionUID = -769973772689612392L;
	
	private JLabel jLabelDBtitle;
	private JLabel jLabelDBHost;
	private JLabel jLabelDB;
	private JLabel jLabelDBUser;
	private JLabel jLabelDBpswd;
	
	private JTextField jTextFieldDBHost;
	private JTextField jTextFieldDB;
	private JTextField jTextFieldDBUser;
	private JTextField jTextFieldDBPswd;
	
	/**
	 * This is the Constructor.
	 * @param optionDialog the option dialog
	 */
	public JPanelDatabase(OptionDialog optionDialog, StartOptions startOptions) {
		super(optionDialog, startOptions);
		this.initialize();
		// --- Translate ----------------------------------
		jLabelDBtitle.setText(Language.translate("MySQL-Datenbank für den Hauptserver (server.master)"));
	}
	
	/**
	 * Initialise.
	 */
	private void initialize() {
		this.setSize(new Dimension(550, 133));
		this.setPreferredSize(new Dimension(540, 80));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{62, 202, 42, 202, 0};
		gridBagLayout.rowHeights = new int[]{16, 20, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
				jLabelDBtitle = new JLabel();
				jLabelDBtitle.setText("MySQL-Datenbank für den Hauptserver (server.master)");
				jLabelDBtitle.setFont(new Font("Dialog", Font.BOLD, 12));
				GridBagConstraints gbc_jLabelDBtitle = new GridBagConstraints();
				gbc_jLabelDBtitle.gridwidth = 4;
				gbc_jLabelDBtitle.anchor = GridBagConstraints.WEST;
				gbc_jLabelDBtitle.insets = new Insets(0, 0, 5, 5);
				gbc_jLabelDBtitle.gridx = 0;
				gbc_jLabelDBtitle.gridy = 0;
				this.add(jLabelDBtitle, gbc_jLabelDBtitle);
		jLabelDBHost = new JLabel();
		jLabelDBHost.setText("DB-Host");
		jLabelDBHost.setPreferredSize(new Dimension(55, 16));
		GridBagConstraints gbc_jLabelDBHost = new GridBagConstraints();
		gbc_jLabelDBHost.anchor = GridBagConstraints.WEST;
		gbc_jLabelDBHost.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelDBHost.gridx = 0;
		gbc_jLabelDBHost.gridy = 1;
		this.add(jLabelDBHost, gbc_jLabelDBHost);
		GridBagConstraints gbc_jTextFieldDBHost = new GridBagConstraints();
		gbc_jTextFieldDBHost.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldDBHost.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldDBHost.gridx = 1;
		gbc_jTextFieldDBHost.gridy = 1;
		this.add(getJTextFieldDBHost(), gbc_jTextFieldDBHost);
		jLabelDBUser = new JLabel();
		jLabelDBUser.setText("DB-User");
		GridBagConstraints gbc_jLabelDBUser = new GridBagConstraints();
		gbc_jLabelDBUser.anchor = GridBagConstraints.WEST;
		gbc_jLabelDBUser.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelDBUser.gridx = 2;
		gbc_jLabelDBUser.gridy = 1;
		this.add(jLabelDBUser, gbc_jLabelDBUser);
				GridBagConstraints gbc_jTextFieldDBUser = new GridBagConstraints();
				gbc_jTextFieldDBUser.fill = GridBagConstraints.BOTH;
				gbc_jTextFieldDBUser.insets = new Insets(0, 0, 5, 0);
				gbc_jTextFieldDBUser.gridx = 3;
				gbc_jTextFieldDBUser.gridy = 1;
				this.add(getJTextFieldDBUser(), gbc_jTextFieldDBUser);
		
				jLabelDB = new JLabel();
				jLabelDB.setText("DB");
				GridBagConstraints gbc_jLabelDB = new GridBagConstraints();
				gbc_jLabelDB.anchor = GridBagConstraints.WEST;
				gbc_jLabelDB.insets = new Insets(0, 0, 0, 5);
				gbc_jLabelDB.gridx = 0;
				gbc_jLabelDB.gridy = 2;
				this.add(jLabelDB, gbc_jLabelDB);
		GridBagConstraints gbc_jTextFieldDB = new GridBagConstraints();
		gbc_jTextFieldDB.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldDB.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldDB.gridx = 1;
		gbc_jTextFieldDB.gridy = 2;
		this.add(getJTextFieldDB(), gbc_jTextFieldDB);
		
		jLabelDBpswd = new JLabel();
		jLabelDBpswd.setText("DB-Pswd");
		GridBagConstraints gbc_jLabelDBpswd = new GridBagConstraints();
		gbc_jLabelDBpswd.anchor = GridBagConstraints.WEST;
		gbc_jLabelDBpswd.insets = new Insets(0, 0, 0, 5);
		gbc_jLabelDBpswd.gridx = 2;
		gbc_jLabelDBpswd.gridy = 2;
		this.add(jLabelDBpswd, gbc_jLabelDBpswd);
		GridBagConstraints gbc_jTextFieldDBPswd = new GridBagConstraints();
		gbc_jTextFieldDBPswd.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldDBPswd.gridx = 3;
		gbc_jTextFieldDBPswd.gridy = 2;
		this.add(getJTextFieldDBPswd(), gbc_jTextFieldDBPswd);
		
	}
	
	/**
	 * This method initializes jTextFieldDBHost	
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
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDBUser() {
		if (jTextFieldDBUser == null) {
			jTextFieldDBUser = new JTextField();
			jTextFieldDBUser.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDBUser;
	}
	/**
	 * This method initializes jPasswordFieldDBpswd	
	 * @return javax.swing.JPasswordField	
	 */
	private JTextField getJTextFieldDBPswd() {
		if (jTextFieldDBPswd == null) {
			jTextFieldDBPswd = new JPasswordField();
			jTextFieldDBPswd.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDBPswd;
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
	public void setGlobalData2Form() {
		this.getJTextFieldDBHost().setText(getGlobalInfo().getServerMasterDBHost());
		this.getJTextFieldDB().setText(getGlobalInfo().getServerMasterDBName());
		this.getJTextFieldDBUser().setText(getGlobalInfo().getServerMasterDBUser());
		this.getJTextFieldDBPswd().setText(getGlobalInfo().getServerMasterDBPswd());
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setFromData2Global()
	 */
	@Override
	public void setFormData2Global() {
		this.getGlobalInfo().setServerMasterDBHost(this.jTextFieldDBHost.getText().trim());
		this.getGlobalInfo().setServerMasterDBName(this.jTextFieldDB.getText().trim());
		this.getGlobalInfo().setServerMasterDBUser(this.jTextFieldDBUser.getText().trim());
		this.getGlobalInfo().setServerMasterDBPswd(this.jTextFieldDBPswd.getText().trim());
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#errorFound()
	 */
	@Override
	public boolean errorFound() {
		return false;
	}

}
