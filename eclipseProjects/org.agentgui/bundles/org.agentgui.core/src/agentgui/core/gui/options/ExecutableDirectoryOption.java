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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.BundleProperties;
import agentgui.core.config.GlobalInfo;

/**
 * The Class OIDCOptions extends an {@link AbstractOptionTab} and is
 * used in order to visually configure the OpenID Connect settings of Agent.GUI.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ExecutableDirectoryOption extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel jPanelApplicationDirectory;
	private JPanel jPanelDummy;
	
	private JLabel jLabelApplicationDirectoryLabel;	
	private JTextField jTextFieldApplicationdirectory;
	
	private JButton jButtonApplicationDirectroySelect;	
	private JButton jButtonAppliationDirectorySave;

	/**
	 * This is the default constructor
	 */
	public ExecutableDirectoryOption() {
		super();
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Configure translation ----------------------
		this.getJButtonApplicationDirectorySave().setText(Language.translate("Speichern"));
		this.getJButtonApplicationDirectorySelect().setToolTipText(Language.translate("Verzeichnis auswählen"));
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Produktverzeichnis");
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Produktverzeichnis");
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 0;
		gridBagConstraints22.anchor = GridBagConstraints.WEST;
		gridBagConstraints22.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints22.gridy = 6;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.anchor = GridBagConstraints.WEST;
		gridBagConstraints12.insets = new Insets(10, 20, 0, 0);
		gridBagConstraints12.gridy = 5;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.insets = new Insets(0, 20, 20, 20);
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.weighty = 1.0;
		gridBagConstraints21.gridy = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(10, 20, 0, 0);
		gridBagConstraints11.gridy = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.insets = new Insets(20, 20, 10, 20);
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints2.gridy = 4;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints1.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 20, 0, 0);
		gridBagConstraints.gridy = 2;
		
		this.setSize(555, 307);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setLayout(new GridBagLayout());
		this.add(getJPanelApplicationDirectory(), gridBagConstraints4);
		this.add(getJPanelDummy(), gridBagConstraints21);		
	}

	/**
	 * Gets the JPanel for the application directory.
	 * @return the JPanel application directory
	 */
	private JPanel getJPanelApplicationDirectory() {
		if (jPanelApplicationDirectory == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 3;
			gridBagConstraints6.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			
			jLabelApplicationDirectoryLabel = new JLabel();
			jLabelApplicationDirectoryLabel.setText("Application Directory");
			jLabelApplicationDirectoryLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelApplicationDirectory = new JPanel();
			jPanelApplicationDirectory.setLayout(new GridBagLayout());
			jPanelApplicationDirectory.add(jLabelApplicationDirectoryLabel, gridBagConstraints3);
			jPanelApplicationDirectory.add(getJTextFieldApplicationDirectory(), gridBagConstraints5);
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints7.gridy = 0;
			jPanelApplicationDirectory.add(getJButtonApplicationDirectorySelect(), gridBagConstraints7);
			jPanelApplicationDirectory.add(getJButtonApplicationDirectorySave(), gridBagConstraints6);
		}
		return jPanelApplicationDirectory;
	}
	
	/**
	 * Gets the JTextField application directory.
	 * @return the JTextField application directory
	 */
	private JTextField getJTextFieldApplicationDirectory() {
		if (jTextFieldApplicationdirectory == null) {
			jTextFieldApplicationdirectory = new JTextField();
		}
		return jTextFieldApplicationdirectory;
	}
	
	/**
	 * Gets the JButton application directory save.
	 * @return the j button application directory save
	 */
	private JButton getJButtonApplicationDirectorySave() {
		if (jButtonAppliationDirectorySave == null) {
			jButtonAppliationDirectorySave = new JButton();
			jButtonAppliationDirectorySave.setText("Speichern");
			jButtonAppliationDirectorySave.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonAppliationDirectorySave.setForeground(new Color(0, 153, 0));
			jButtonAppliationDirectorySave.setPreferredSize(new Dimension(100, 26));
			jButtonAppliationDirectorySave.addActionListener(this);
		}
		return jButtonAppliationDirectorySave;
	}
	
	/**
	 * Gets the j button application directory select.
	 * @return the j button application directory select
	 */
	private JButton getJButtonApplicationDirectorySelect() {
		if (jButtonApplicationDirectroySelect == null) {
			jButtonApplicationDirectroySelect = new JButton();
			jButtonApplicationDirectroySelect.setToolTipText(Language.translate("Verzeichnis auswählen"));
			jButtonApplicationDirectroySelect.setIcon(GlobalInfo.getInternalImageIcon("MBopen.png"));
			jButtonApplicationDirectroySelect.setPreferredSize(new Dimension(45, 26));
			jButtonApplicationDirectroySelect.addActionListener(this);
		}
		return jButtonApplicationDirectroySelect;
	}

	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
		}
		return jPanelDummy;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object actionFrom = ae.getSource();
		if (actionFrom==this.getJButtonApplicationDirectorySave()) {
			this.setFormData2Global();
			
		} else if (actionFrom==this.getJButtonApplicationDirectorySelect()) {
			// --- Select the directory ---------------------------
			String currentDirPath = this.getJTextFieldApplicationDirectory().getText();
			File currentDir = null;
			if (currentDirPath!=null && currentDirPath.equals("")==false) {
				currentDir = new File(currentDirPath);
			}
			if (currentDir==null || currentDir.exists()==false) {
				currentDir = Application.getGlobalInfo().getLastSelectedFolder();
			}
			
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(Language.translate("Verzeichnis eines installierten Produkts auswählen"));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setCurrentDirectory(currentDir);
			chooser.setMultiSelectionEnabled(false);
			
			int answerChooser = chooser.showDialog(this, Language.translate("Verzeichnis auswählen"));
			if (answerChooser==JFileChooser.CANCEL_OPTION) return;
			
			String selectedDirectory = chooser.getSelectedFile().getAbsolutePath();
			this.getJTextFieldApplicationDirectory().setText(selectedDirectory);
			this.getJTextFieldApplicationDirectory().setToolTipText(selectedDirectory);
			Application.getGlobalInfo().setLastSelectedFolder(chooser.getSelectedFile());
			
		}
	}
	
	/**
	 * Fills the form with data from the properties file.
	 */
	private void setGlobalData2Form() {
		String productPath = Application.getGlobalInfo().getStringFromPersistedConfiguration(BundleProperties.DEF_PRODUCT_INSTALLATION_DIRECTORY, null);
		this.getJTextFieldApplicationDirectory().setText(productPath);
		this.getJTextFieldApplicationDirectory().setToolTipText(productPath);
	}
	/**
	 * Writes the form data to the properties file.
	 */
	private void setFormData2Global() {
		Application.getGlobalInfo().putStringToPersistedConfiguration(BundleProperties.DEF_PRODUCT_INSTALLATION_DIRECTORY, this.getJTextFieldApplicationDirectory().getText().trim());
	}
	
}  
