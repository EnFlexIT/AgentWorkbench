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
package org.agentgui.gui.swing.project;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.osgi.framework.Version;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.project.Project;
import agentgui.core.update.ProjectRepositoryExport;
import de.enflexit.common.swing.KeyAdapter4Numbers;

/**
 * The Class ProjectInfoVersionPanel can be used to edit the current {@link Version}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectInfoVersionPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -2572793810709664030L;
	
	private ProjectInfo projectInfo;
	private Project currProject;
	
	private JButton jButtonApplyNewVersion;

	private JLabel jLabelMajorVersion;
	private JTextField jTextFieldVersion;
	
	private JLabel jLabelTag;
	private JTextField jTextFieldVersionTag;

	private KeyAdapter4Numbers keyAdapter4Numbers;
	private JLabel jLabelVersion;
	private JButton jButtonUpdateQualifier;
	
	/**
	 * Instantiates a new project info version panel.
	 *
	 * @param project the project
	 * @param projectInfo the tab with the project info
	 */
	public ProjectInfoVersionPanel(Project project, ProjectInfo projectInfo) {
		this.currProject = project;
		this.projectInfo = projectInfo;
		this.updateVersionInformation();
		this.initialize();
	}
	/**
	 * Updates the local version information.
	 */
	public void updateVersionInformation() {
		this.getJTextFieldVersion().setText(this.currProject.getVersion().toString());
		this.getJTextFieldVersionTag().setText(this.currProject.getVersionTag());
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelVersion = new GridBagConstraints();
		gbc_jLabelVersion.anchor = GridBagConstraints.WEST;
		gbc_jLabelVersion.gridwidth = 2;
		gbc_jLabelVersion.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelVersion.gridx = 0;
		gbc_jLabelVersion.gridy = 0;
		add(getJLabelVersion(), gbc_jLabelVersion);
		GridBagConstraints gbc_jButtonUpdateQualifier = new GridBagConstraints();
		gbc_jButtonUpdateQualifier.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonUpdateQualifier.gridx = 2;
		gbc_jButtonUpdateQualifier.gridy = 0;
		add(getJButtonUpdateQualifier(), gbc_jButtonUpdateQualifier);
		GridBagConstraints gbc_jButtonEditVersion = new GridBagConstraints();
		gbc_jButtonEditVersion.anchor = GridBagConstraints.EAST;
		gbc_jButtonEditVersion.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonEditVersion.gridx = 3;
		gbc_jButtonEditVersion.gridy = 0;
		add(getJButtonApplyNewVersion(), gbc_jButtonEditVersion);
		GridBagConstraints gbc_jLabelMajorVersion = new GridBagConstraints();
		gbc_jLabelMajorVersion.insets = new Insets(13, 0, 5, 5);
		gbc_jLabelMajorVersion.anchor = GridBagConstraints.WEST;
		gbc_jLabelMajorVersion.gridx = 0;
		gbc_jLabelMajorVersion.gridy = 1;
		add(getJLabelMajorVersion(), gbc_jLabelMajorVersion);
		GridBagConstraints gbc_jTextFieldMajorVersion = new GridBagConstraints();
		gbc_jTextFieldMajorVersion.insets = new Insets(13, 0, 5, 0);
		gbc_jTextFieldMajorVersion.gridwidth = 3;
		gbc_jTextFieldMajorVersion.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldMajorVersion.gridx = 1;
		gbc_jTextFieldMajorVersion.gridy = 1;
		add(getJTextFieldVersion(), gbc_jTextFieldMajorVersion);
		GridBagConstraints gbc_jLabelTag = new GridBagConstraints();
		gbc_jLabelTag.anchor = GridBagConstraints.WEST;
		gbc_jLabelTag.insets = new Insets(0, 0, 0, 5);
		gbc_jLabelTag.gridx = 0;
		gbc_jLabelTag.gridy = 2;
		add(getJLabelTag(), gbc_jLabelTag);
		GridBagConstraints gbc_jTextFieldTag = new GridBagConstraints();
		gbc_jTextFieldTag.gridwidth = 3;
		gbc_jTextFieldTag.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldTag.gridx = 1;
		gbc_jTextFieldTag.gridy = 2;
		add(getJTextFieldVersionTag(), gbc_jTextFieldTag);
	}

	private JLabel getJLabelVersion() {
		if (jLabelVersion == null) {
			jLabelVersion = new JLabel();
			jLabelVersion.setText(Language.translate("Version"));
			jLabelVersion.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelVersion.setPreferredSize(new Dimension(100, 26));
			jLabelVersion.setMinimumSize(new Dimension(100, 26));
		}
		return jLabelVersion;
	}
	private JButton getJButtonUpdateQualifier() {
		if (jButtonUpdateQualifier == null) {
			jButtonUpdateQualifier = new JButton();
			jButtonUpdateQualifier.setToolTipText("Update qualifier");
			jButtonUpdateQualifier.setIcon(GlobalInfo.getInternalImageIcon("MBclockWhite.png"));
			jButtonUpdateQualifier.setPreferredSize(new Dimension(45, 26));
			jButtonUpdateQualifier.addActionListener(this);
		}
		return jButtonUpdateQualifier;
	}

	private JButton getJButtonApplyNewVersion() {
		if (jButtonApplyNewVersion == null) {
			jButtonApplyNewVersion = new JButton();
			jButtonApplyNewVersion.setToolTipText(Language.translate("Versionnummer speichern"));
			jButtonApplyNewVersion.setIcon(GlobalInfo.getInternalImageIcon("MBcheckGreen.png"));
			jButtonApplyNewVersion.setPreferredSize(new Dimension(45, 26));
			jButtonApplyNewVersion.addActionListener(this);
		}
		return jButtonApplyNewVersion;
	}
	
	private JLabel getJLabelMajorVersion() {
		if (jLabelMajorVersion == null) {
			jLabelMajorVersion = new JLabel("No.");
			jLabelMajorVersion.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMajorVersion;
	}
	private JTextField getJTextFieldVersion() {
		if (jTextFieldVersion == null) {
			jTextFieldVersion = new JTextField();
			jTextFieldVersion.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldVersion.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldVersion.addKeyListener(this.getKeyAdapter4Numbers());
		}
		return jTextFieldVersion;
	}
	
	private JLabel getJLabelTag() {
		if (jLabelTag == null) {
			jLabelTag = new JLabel("Tag");
			jLabelTag.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTag;
	}
	private JTextField getJTextFieldVersionTag() {
		if (jTextFieldVersionTag == null) {
			jTextFieldVersionTag = new JTextField("qualifier");
			jTextFieldVersionTag.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldVersionTag.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return jTextFieldVersionTag;
	}
	
	private KeyAdapter4Numbers getKeyAdapter4Numbers() {
		if (keyAdapter4Numbers==null) {
			keyAdapter4Numbers = new KeyAdapter4Numbers(false) {
				/* (non-Javadoc)
				 * @see de.enflexit.common.swing.KeyAdapter4Numbers#keyTyped(java.awt.event.KeyEvent)
				 */
				@Override
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String singleChar = Character.toString(charackter);
					// --- Allow '-' and '.' ------------------------
					if (singleChar.equals("-") || singleChar.equals(".")) return;
					// --- Do the regular action of the adapter -----
					super.keyTyped(kT);
				}
			};
		}
		return keyAdapter4Numbers;
	}
	
	/**
	 * Returns configuration errors.
	 * @return the configuration error
	 */
	private String getConfigurationError() {
		
		String errorMsg = null;
		if (getJTextFieldVersion().getText()==null || getJTextFieldVersion().getText().equals("")) {
			getJTextFieldVersion().setText("" + currProject.getVersion().toString());
			errorMsg = "The version number is not allowed to be null!\n";
			errorMsg+= "Thus, the version number was reverted to the project settings!";
		}

		if (getJTextFieldVersionTag().getText()==null || getJTextFieldVersionTag().getText().equals("")) {
			getJTextFieldVersionTag().setText(currProject.getVersionTag());
			errorMsg = "The version tag is not allowed to be null!\n";
			errorMsg+= "Thus, the version tag was reverted to the project settings!";
		}
		return errorMsg;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Container pareComp = Application.getMainWindow();
		
		// --- Check for configuration errors -----------------------
		String configError = this.getConfigurationError();
		if (configError!=null) {
			JOptionPane.showMessageDialog(pareComp, configError, "Configuration Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try {
			
			if (ae.getSource()==this.getJButtonApplyNewVersion()) {
				// ------------------------------------------------------
				// --- Save the changes to the project ------------------
				// ------------------------------------------------------
				// --- Check if is newer version --------------------
				Version newVersion = Version.parseVersion(this.getJTextFieldVersion().getText());
				Version oldVersion = this.currProject.getVersion(); 
				if (newVersion.compareTo(oldVersion)<0) {
					String message = "The new version number is smaller that the old one.\n\n";
					message += "Please press 'Yes', if you would like to continue.\n";
					message += "Press 'No', if you want to undo your changes?";
					int answer = JOptionPane.showConfirmDialog(pareComp, message, "Version Check", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (answer==JOptionPane.NO_OPTION) {
						this.getJTextFieldVersion().setText(oldVersion.toString());
						return;
					}
					
				}
				currProject.setVersion(newVersion.toString());
				currProject.setVersionTag(this.getJTextFieldVersionTag().getText());
				projectInfo.switchJPanelVersion();
				
			} else if (ae.getSource()==this.getJButtonUpdateQualifier()) {
				// ------------------------------------------------------
				// --- Update the version qualifier ---------------------
				// ------------------------------------------------------
				Version oldVersion  = Version.parseVersion(this.getJTextFieldVersion().getText());
				String newQualifier = ProjectRepositoryExport.getVersionQualifierForTimeStamp(System.currentTimeMillis());
				Version newVersion = new Version(oldVersion.getMajor(), oldVersion.getMinor(), oldVersion.getMicro(), newQualifier); 
				this.getJTextFieldVersion().setText(newVersion.toString());
			}

		} catch (IllegalArgumentException iaEx) {
			JOptionPane.showMessageDialog(pareComp, iaEx.getLocalizedMessage(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
			System.err.println("[" + this.getClass().getSimpleName() + "] " + iaEx.getLocalizedMessage());
			//iaEx.printStackTrace();
		}
			
	}
	
}
