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
import javax.swing.JOptionPane;
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
public class DirectoryOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JLabel jLabelInstalledProductDirectory;	
	private JTextField jTextFieldInstalledProductDirectory;
	private JButton jButtonInstalledProductDirectroySelect;	

	private JButton jButtonSave;
	
	private JLabel jLabelProjectsDirectory;
	private JTextField jTextFieldProjectsDirectroy;
	private JButton jButtonProjectsDirectorySelect;
	
	private JLabel jLabelInstallationPackagesDirectory;
	private JTextField jTextFieldInstallationPackagesDirectroy;
	private JButton jButtonInstallationPackagesDirectorySelect;

	
	/**
	 * This is the default constructor
	 */
	public DirectoryOptions() {
		super();
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Configure translation ----------------------
		this.getJButtonSave().setText(Language.translate("Speichern"));
		this.getJButtonProjectesDirectroySelect().setToolTipText(Language.translate("Verzeichnis auswählen"));
		this.getJButtonApplicationDirectorySelect().setToolTipText(Language.translate("Verzeichnis auswählen"));
		this.getJButtonInstallationPackagesDirectorySelect().setToolTipText(Language.translate("Verzeichnis auswählen"));
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Verzeichnisse");
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Verzeichnisse");
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{130, 0, 0, 1, 0};
		gridBagLayout.rowHeights = new int[]{0, 26, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		
		GridBagConstraints gbc_jLabelProjectsDirectory = new GridBagConstraints();
		gbc_jLabelProjectsDirectory.anchor = GridBagConstraints.WEST;
		gbc_jLabelProjectsDirectory.insets = new Insets(20, 20, 0, 0);
		gbc_jLabelProjectsDirectory.gridx = 0;
		gbc_jLabelProjectsDirectory.gridy = 0;
		
		GridBagConstraints gbc_jTextFieldProjectsDirectroy = new GridBagConstraints();
		gbc_jTextFieldProjectsDirectroy.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldProjectsDirectroy.insets = new Insets(20, 5, 0, 0);
		gbc_jTextFieldProjectsDirectroy.gridx = 1;
		gbc_jTextFieldProjectsDirectroy.gridy = 0;

		GridBagConstraints gbc_jButtonProjectesDirectroySelect = new GridBagConstraints();
		gbc_jButtonProjectesDirectroySelect.insets = new Insets(20, 5, 0, 0);
		gbc_jButtonProjectesDirectroySelect.gridx = 2;
		gbc_jButtonProjectesDirectroySelect.gridy = 0;

		GridBagConstraints gbc_jButtonAppliationDirectorySave = new GridBagConstraints();
		gbc_jButtonAppliationDirectorySave.insets = new Insets(20, 20, 0, 20);
		gbc_jButtonAppliationDirectorySave.anchor = GridBagConstraints.NORTHWEST;
		gbc_jButtonAppliationDirectorySave.gridx = 3;
		gbc_jButtonAppliationDirectorySave.gridy = 0;
		
		GridBagConstraints gbc_jLabelInstalledProductDirectoryLabel = new GridBagConstraints();
		gbc_jLabelInstalledProductDirectoryLabel.anchor = GridBagConstraints.WEST;
		gbc_jLabelInstalledProductDirectoryLabel.insets = new Insets(10, 20, 0, 0);
		gbc_jLabelInstalledProductDirectoryLabel.gridx = 0;
		gbc_jLabelInstalledProductDirectoryLabel.gridy = 1;
		
		GridBagConstraints gbc_jTextFieldInstalledProductDirectory = new GridBagConstraints();
		gbc_jTextFieldInstalledProductDirectory.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldInstalledProductDirectory.insets = new Insets(10, 5, 0, 0);
		gbc_jTextFieldInstalledProductDirectory.gridx = 1;
		gbc_jTextFieldInstalledProductDirectory.gridy = 1;
		
		GridBagConstraints gbc_jButtonInstalledProductDirectroySelect = new GridBagConstraints();
		gbc_jButtonInstalledProductDirectroySelect.insets = new Insets(10, 5, 0, 0);
		gbc_jButtonInstalledProductDirectroySelect.gridx = 2;
		gbc_jButtonInstalledProductDirectroySelect.gridy = 1;
		
		GridBagConstraints gbc_jLabelInstallationPackagesDirectory = new GridBagConstraints();
		gbc_jLabelInstallationPackagesDirectory.anchor = GridBagConstraints.WEST;
		gbc_jLabelInstallationPackagesDirectory.insets = new Insets(10, 20, 0, 0);
		gbc_jLabelInstallationPackagesDirectory.gridx = 0;
		gbc_jLabelInstallationPackagesDirectory.gridy = 2;
		
		GridBagConstraints gbc_jTextFieldInstallationPackagesDirectroy = new GridBagConstraints();
		gbc_jTextFieldInstallationPackagesDirectroy.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldInstallationPackagesDirectroy.insets = new Insets(10, 5, 0, 0);
		gbc_jTextFieldInstallationPackagesDirectroy.gridx = 1;
		gbc_jTextFieldInstallationPackagesDirectroy.gridy = 2;

		GridBagConstraints gbc_jButtonInstallationPackagesDirectorySelect = new GridBagConstraints();
		gbc_jButtonInstallationPackagesDirectorySelect.insets = new Insets(10, 5, 0, 0);
		gbc_jButtonInstallationPackagesDirectorySelect.gridx = 2;
		gbc_jButtonInstallationPackagesDirectorySelect.gridy = 2;
		
		
		this.setSize(798, 306);
		this.setLayout(gridBagLayout);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		this.add(this.getJLabelProjectsDirectory(), gbc_jLabelProjectsDirectory);
		this.add(this.getJTextFieldProjectsDirectroy(), gbc_jTextFieldProjectsDirectroy);
		this.add(this.getJButtonProjectesDirectroySelect(), gbc_jButtonProjectesDirectroySelect);
		
		this.add(getJButtonSave(), gbc_jButtonAppliationDirectorySave);
		
		this.add(this.getjLabelInstalledProductDirectory(), gbc_jLabelInstalledProductDirectoryLabel);
		this.add(this.getJTextFieldInstalledProductDirectory(), gbc_jTextFieldInstalledProductDirectory);
		this.add(this.getJButtonApplicationDirectorySelect(), gbc_jButtonInstalledProductDirectroySelect);
		
		this.add(this.getJLabelInstallationPackagesDirectory(), gbc_jLabelInstallationPackagesDirectory);
		this.add(this.getJTextFieldInstallationPackagesDirectroy(), gbc_jTextFieldInstallationPackagesDirectroy);
		this.add(this.getJButtonInstallationPackagesDirectorySelect(), gbc_jButtonInstallationPackagesDirectorySelect);
		
	}
	
	private JLabel getJLabelProjectsDirectory() {
		if (jLabelProjectsDirectory == null) {
			jLabelProjectsDirectory = new JLabel();
			jLabelProjectsDirectory.setText(Language.translate("Projekt - Basisverzeichnis"));
			jLabelProjectsDirectory.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelProjectsDirectory;
	}
	private JTextField getJTextFieldProjectsDirectroy() {
		if (jTextFieldProjectsDirectroy == null) {
			jTextFieldProjectsDirectroy = new JTextField();
			jTextFieldProjectsDirectroy.setToolTipText((String) null);
			jTextFieldProjectsDirectroy.setText((String) null);
		}
		return jTextFieldProjectsDirectroy;
	}
	private JButton getJButtonProjectesDirectroySelect() {
		if (jButtonProjectsDirectorySelect == null) {
			jButtonProjectsDirectorySelect = new JButton();
			jButtonProjectsDirectorySelect.setToolTipText(Language.translate("Verzeichnis auswählen"));
			jButtonProjectsDirectorySelect.setPreferredSize(new Dimension(45, 26));
			jButtonProjectsDirectorySelect.setIcon(GlobalInfo.getInternalImageIcon("MBopen.png"));
			jButtonProjectsDirectorySelect.addActionListener(this);
		}
		return jButtonProjectsDirectorySelect;
	}
	
	
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setText("Speichern");
			jButtonSave.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSave.setForeground(new Color(0, 153, 0));
			jButtonSave.setPreferredSize(new Dimension(100, 26));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}

	public JLabel getjLabelInstalledProductDirectory() {
		if (jLabelInstalledProductDirectory==null) {
			jLabelInstalledProductDirectory = new JLabel();
			jLabelInstalledProductDirectory.setText(Language.translate("Produktinstallation"));
			jLabelInstalledProductDirectory.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelInstalledProductDirectory;
	}
	private JTextField getJTextFieldInstalledProductDirectory() {
		if (jTextFieldInstalledProductDirectory == null) {
			jTextFieldInstalledProductDirectory = new JTextField();
		}
		return jTextFieldInstalledProductDirectory;
	}
	private JButton getJButtonApplicationDirectorySelect() {
		if (jButtonInstalledProductDirectroySelect == null) {
			jButtonInstalledProductDirectroySelect = new JButton();
			jButtonInstalledProductDirectroySelect.setToolTipText(Language.translate("Verzeichnis auswählen"));
			jButtonInstalledProductDirectroySelect.setIcon(GlobalInfo.getInternalImageIcon("MBopen.png"));
			jButtonInstalledProductDirectroySelect.setPreferredSize(new Dimension(45, 26));
			jButtonInstalledProductDirectroySelect.addActionListener(this);
		}
		return jButtonInstalledProductDirectroySelect;
	}
	
	
	private JLabel getJLabelInstallationPackagesDirectory() {
		if (jLabelInstallationPackagesDirectory == null) {
			jLabelInstallationPackagesDirectory = new JLabel();
			jLabelInstallationPackagesDirectory.setText(Language.translate("Installationspakete"));
			jLabelInstallationPackagesDirectory.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelInstallationPackagesDirectory;
	}
	private JTextField getJTextFieldInstallationPackagesDirectroy() {
		if (jTextFieldInstallationPackagesDirectroy == null) {
			jTextFieldInstallationPackagesDirectroy = new JTextField();
			jTextFieldInstallationPackagesDirectroy.setToolTipText((String) null);
			jTextFieldInstallationPackagesDirectroy.setText((String) null);
		}
		return jTextFieldInstallationPackagesDirectroy;
	}
	private JButton getJButtonInstallationPackagesDirectorySelect() {
		if (jButtonInstallationPackagesDirectorySelect == null) {
			jButtonInstallationPackagesDirectorySelect = new JButton();
			jButtonInstallationPackagesDirectorySelect.setToolTipText("Verzeichnis auswählen");
			jButtonInstallationPackagesDirectorySelect.setPreferredSize(new Dimension(45, 26));
			jButtonInstallationPackagesDirectorySelect.setIcon(GlobalInfo.getInternalImageIcon("MBopen.png"));
			jButtonInstallationPackagesDirectorySelect.addActionListener(this);
		}
		return jButtonInstallationPackagesDirectorySelect;
	}

	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object actionFrom = ae.getSource();
		if (actionFrom==this.getJButtonSave()) {
			if (this.isErrorConfiguration()==false) {
				this.setFormData2Global();
			}
			
		} else if (actionFrom==this.getJButtonProjectesDirectroySelect()) {
			// --- Select the projects root directory ---------------			
			File currentDir = this.getCurrentDirectory(this.getJTextFieldProjectsDirectroy().getText().trim());
			File selectedDirectory = this.selectDirectory(Language.translate("Projekt-Basisverzeichnis auswählen"), currentDir);
			if (selectedDirectory==null) return;
			
			String selectedDirectoryPath = selectedDirectory.getAbsolutePath() + File.separator;
			this.getJTextFieldProjectsDirectroy().setText(selectedDirectoryPath);
			this.getJTextFieldProjectsDirectroy().setToolTipText(selectedDirectoryPath);
			
		} else if (actionFrom==this.getJButtonApplicationDirectorySelect()) {
			// --- Select the installed product directory -----------
			File currentDir = this.getCurrentDirectory(this.getJTextFieldInstalledProductDirectory().getText().trim());
			File selectedDirectory = this.selectDirectory(Language.translate("Verzeichnis eines installierten Produkts auswählen"), currentDir);
			if (selectedDirectory==null) return;
			
			String selectedDirectoryPath = selectedDirectory.getAbsolutePath() + File.separator;
			this.getJTextFieldInstalledProductDirectory().setText(selectedDirectoryPath);
			this.getJTextFieldInstalledProductDirectory().setToolTipText(selectedDirectoryPath);
		
		} else if (ae.getSource()==this.getJButtonInstallationPackagesDirectorySelect()) {
			
			File currentDir = this.getCurrentDirectory(this.getJTextFieldInstallationPackagesDirectroy().getText().trim());
			File selectedDirectory = this.selectDirectory(Language.translate("Verzeichnis mit Installtions Paketen auswählen"), currentDir);
			if (selectedDirectory==null) return;
			
			String selectedDirectoryPath = selectedDirectory.getAbsolutePath() + File.separator;
			this.getJTextFieldInstallationPackagesDirectroy().setText(selectedDirectoryPath);
			this.getJTextFieldInstallationPackagesDirectroy().setToolTipText(selectedDirectoryPath);
			
		}	
	}
	
	/**
	 * Returns the current directory as file, starting from the specified initial path setting.
	 * @param initialPathSetting the initial path setting
	 * @return the current directory
	 */
	private File getCurrentDirectory(String initialPathSetting) {

		String currentDirPath = initialPathSetting;
		File currentDir = null;
		if (currentDirPath!=null && currentDirPath.equals("")==false) {
			currentDir = new File(currentDirPath);
		}
		if (currentDir==null || currentDir.exists()==false) {
			currentDir = Application.getGlobalInfo().getLastSelectedFolder();
		}
		return currentDir;
	}
	
	/**
	 * Allows users to select a directory .
	 * @param dialogTitle the dialog title
	 * @param currentDirectory the current directory
	 * @return the selected directory as file
	 */
	private File selectDirectory(String dialogTitle, File currentDirectory) {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(currentDirectory);
		chooser.setMultiSelectionEnabled(false);
		
		int answerChooser = chooser.showDialog(this, Language.translate("Verzeichnis auswählen"));
		if (answerChooser==JFileChooser.CANCEL_OPTION) return null;
		
		Application.getGlobalInfo().setLastSelectedFolder(chooser.getSelectedFile());
		return chooser.getSelectedFile();
	}
	
	/**
	 * Checks if there is an error in the configuration.
	 * @return true, if there is an error 
	 */
	private boolean isErrorConfiguration() {
		
		boolean isError = false;
		
		String title = "";
		String message = "";
		int answer = 0;
		
		// --------------------------------------------------------------------
		// ---- Projects root folder ------------------------------------------
		// --------------------------------------------------------------------
		// --- Check for proper path ending -----------------------------------
		String projectsPathOld = Application.getGlobalInfo().getStringFromPersistedConfiguration(BundleProperties.DEF_PROJECTS_DIRECTORY, null);
		String projectsPathNew = this.getJTextFieldProjectsDirectroy().getText().trim();
		if (projectsPathNew.endsWith(File.separator)==false) {
			projectsPathNew += File.separator;
			this.getJTextFieldProjectsDirectroy().setText(projectsPathNew);
		}
		
		// --- Ensure, that no project is open --------------------------------
		if (projectsPathNew.equals(projectsPathOld)==false && Application.getProjectsLoaded().count()>0) {
			title = "Projekt geöffnet!";
			message  = "Geöffnete Projekte müssen geschlossen werden, um das Projekt-Basisverzeichnis zu ändern.\n"; 
			message += "Sollen die Projekte jetzt geschlossen werden?";
			answer = JOptionPane.showConfirmDialog(this, Language.translate(message), Language.translate(title), JOptionPane.ERROR_MESSAGE);
			if (answer==JOptionPane.YES_OPTION) {
				if (Application.getProjectsLoaded().closeAll(this)==false) {
					return true;
				}
			} else {
				return true;
			}
		}
		
		// --- Check if the project base directory exists ---------------------
		File projectsDir = new File(projectsPathNew);
		if (projectsDir.exists()==false) {
			title = "Projekt-Basisverzeichnis nicht gefunden!";
			message  = "Das angegebene Projekt-Basisverzeichnis konnte nicht gefunden werden.\n"; 
			message += "Möchten Sie das Verzeichnis jetzt erstellen?";
			answer = JOptionPane.showConfirmDialog(this, Language.translate(message), Language.translate(title), JOptionPane.ERROR_MESSAGE);
			if (answer==JOptionPane.YES_OPTION) {
				isError = projectsDir.mkdirs();
			} else {
				return true;
			}
		}

		// --------------------------------------------------------------------
		// ---- Installed product directory -----------------------------------
		// --------------------------------------------------------------------
		String productPathOld = Application.getGlobalInfo().getStringFromPersistedConfiguration(BundleProperties.DEF_PRODUCT_INSTALLATION_DIRECTORY, null);
		
		
		
//			answer = JOptionPane.showConfirmDialog(this.optionDialog, message, title, JOptionPane.ERROR_MESSAGE);
		
		
		String installationPackagesPathOld = Application.getGlobalInfo().getStringFromPersistedConfiguration(BundleProperties.DEF_PRODUCT_DIRECTORY_FOR_INSTALLATION_PACKAGES, null);
		
		
		
		return isError;
	}
	/**
	 * Fills the form with data from the properties file.
	 */
	private void setGlobalData2Form() {
		
		String projectsPath = Application.getGlobalInfo().getPathProjects();
		this.getJTextFieldProjectsDirectroy().setText(projectsPath);
		this.getJTextFieldProjectsDirectroy().setToolTipText(projectsPath);
		
		String productPath = Application.getGlobalInfo().getStringFromPersistedConfiguration(BundleProperties.DEF_PRODUCT_INSTALLATION_DIRECTORY, null);
		this.getJTextFieldInstalledProductDirectory().setText(productPath);
		this.getJTextFieldInstalledProductDirectory().setToolTipText(productPath);
		
		String installationPackagesPath = Application.getGlobalInfo().getStringFromPersistedConfiguration(BundleProperties.DEF_PRODUCT_DIRECTORY_FOR_INSTALLATION_PACKAGES, null);
		this.getJTextFieldInstallationPackagesDirectroy().setText(installationPackagesPath);
		this.getJTextFieldInstallationPackagesDirectroy().setToolTipText(installationPackagesPath);
		
	}
	/**
	 * Writes the form data to the properties file.
	 */
	private void setFormData2Global() {
		
		String projectsPath = this.getJTextFieldProjectsDirectroy().getText().trim();
		Application.getGlobalInfo().setPathProjects(projectsPath);
		Application.getGlobalInfo().putStringToPersistedConfiguration(BundleProperties.DEF_PROJECTS_DIRECTORY, projectsPath);
		
		Application.getGlobalInfo().putStringToPersistedConfiguration(BundleProperties.DEF_PRODUCT_INSTALLATION_DIRECTORY, this.getJTextFieldInstalledProductDirectory().getText().trim());
		
		Application.getGlobalInfo().putStringToPersistedConfiguration(BundleProperties.DEF_PRODUCT_DIRECTORY_FOR_INSTALLATION_PACKAGES, this.getJTextFieldInstallationPackagesDirectroy().getText().trim());
		
		Application.getGlobalInfo().doSavePersistedConfiguration();
		
	}
	
}  
