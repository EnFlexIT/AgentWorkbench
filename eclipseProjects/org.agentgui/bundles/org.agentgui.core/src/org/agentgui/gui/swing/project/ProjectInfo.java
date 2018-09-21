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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.project.Project;

/**
 * Represents the JPanel/Tab 'Info' for the general project information
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectInfo extends JPanel implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;

	private Project currProject;
	
	private JLabel jLableTitleProject;
	private JTextField jTextFieldProjectName;
	
	private JLabel jLableDescription;
	private JScrollPane jScrollPane;
	private JTextArea jTextAreaProjectDescription;
	private boolean isPauseProjectDescriptionDocumentListener;
	
	private JLabel jLabelStartTab;
	private JTextField jTextFieldStartTab;
	
	private JLabel jLableProjectFolder;
	private JTextField jTextFieldProjectFolder;
	
	private JSeparator jSeparatorHorizontal;
	private JPanel jPanelUpdateOptions;
	
	private Dimension versionPanelDimension = new Dimension(200, 80); 
	private JPanel jPanelVersion;
	private JLabel jLabelVersion;
	private JLabel jLabelVersionInfo;
	private JButton jButtonEditVersion;
	private boolean isVersionEdit;
	private ProjectInfoVersionPanel jPanelVersionEdit;
	
	private JSeparator jSeparatorVersionUpdate;

	private JLabel jLabelUpdateSite;
	private JTextField jTextFieldUpdateSite;
	private JButton jButtonSearchForUpdate;

	private JLabel jLabelLastUpdate;
	private JRadioButton jRadioButtonUpdateAutomated;
	private JRadioButton jRadioButtonUpdateDownloadAndAsk;
	private JRadioButton jRadioButtonUpdateDisabled;
	private JButton jButtonUpdateSiteDefault;
	private JLabel jLabelVersionTag;
	
	
	/**
	 * This is the default constructor.
	 * @param project the project
	 */
	public ProjectInfo(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);		
		this.initialize();
		this.applyProjectViewSettings();
		this.updateLastUpdateCheckDate();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0};
		this.setLayout(gridBagLayout);
		this.setSize(880, 521);
		
		GridBagConstraints gbc_ProjectTitle = new GridBagConstraints();
		gbc_ProjectTitle.gridx = 0;
		gbc_ProjectTitle.insets = new Insets(10, 10, 10, 5);
		gbc_ProjectTitle.anchor = GridBagConstraints.WEST;
		gbc_ProjectTitle.gridy = 0;
		this.add(this.getJLabelProjectTitle(), gbc_ProjectTitle);

		GridBagConstraints gbc_ProjectName = new GridBagConstraints();
		gbc_ProjectName.gridy = 0;
		gbc_ProjectName.fill = GridBagConstraints.HORIZONTAL;
		gbc_ProjectName.gridx = 1;
		gbc_ProjectName.insets = new Insets(10, 0, 10, 10);
		gbc_ProjectName.weightx = 1.0;
		this.add(this.getJTextFieldProjectName(), gbc_ProjectName);
		
		GridBagConstraints gbc_ProjectDescription = new GridBagConstraints();
		gbc_ProjectDescription.gridx = 0;
		gbc_ProjectDescription.anchor = GridBagConstraints.NORTHWEST;
		gbc_ProjectDescription.insets = new Insets(2, 10, 5, 5);
		gbc_ProjectDescription.gridy = 1;
		this.add(this.getJLabelProjectDescription(), gbc_ProjectDescription);

		GridBagConstraints gbc_ProjectDescriptionScrollPane = new GridBagConstraints();
		gbc_ProjectDescriptionScrollPane.weighty = 1.0;
		gbc_ProjectDescriptionScrollPane.fill = GridBagConstraints.BOTH;
		gbc_ProjectDescriptionScrollPane.gridy = 1;
		gbc_ProjectDescriptionScrollPane.insets = new Insets(0, 0, 5, 10);
		gbc_ProjectDescriptionScrollPane.gridx = 1;
		this.add(this.getJScrollPane(), gbc_ProjectDescriptionScrollPane);
		
		GridBagConstraints gbcProjectFolderLable = new GridBagConstraints();
		gbcProjectFolderLable.gridx = 0;
		gbcProjectFolderLable.insets = new Insets(0, 10, 5, 5);
		gbcProjectFolderLable.anchor = GridBagConstraints.WEST;
		gbcProjectFolderLable.gridy = 2;
		this.add(this.getJLabelProjectFolder(), gbcProjectFolderLable);
		
		GridBagConstraints gbc_ProjectFolder = new GridBagConstraints();
		gbc_ProjectFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_ProjectFolder.gridy = 2;
		gbc_ProjectFolder.weightx = 1.0;
		gbc_ProjectFolder.insets = new Insets(0, 0, 5, 10);
		gbc_ProjectFolder.gridx = 1;
		this.add(this.getjTextFieldProjectFolder(), gbc_ProjectFolder);
		
		GridBagConstraints gbc_jLabelStartTab = new GridBagConstraints();
		gbc_jLabelStartTab.anchor = GridBagConstraints.WEST;
		gbc_jLabelStartTab.insets = new Insets(0, 10, 10, 5);
		gbc_jLabelStartTab.gridx = 0;
		gbc_jLabelStartTab.gridy = 3;
		this.add(this.getLabelStartTab(), gbc_jLabelStartTab);
		
		GridBagConstraints gbc_jComboBoxStartTab = new GridBagConstraints();
		gbc_jComboBoxStartTab.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxStartTab.insets = new Insets(0, 0, 10, 10);
		gbc_jComboBoxStartTab.gridx = 1;
		gbc_jComboBoxStartTab.gridy = 3;
		this.add(this.getJTextFieldStartTab(), gbc_jComboBoxStartTab);
		
		GridBagConstraints gbc_jSeparatorHorizontal = new GridBagConstraints();
		gbc_jSeparatorHorizontal.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorHorizontal.gridwidth = 2;
		gbc_jSeparatorHorizontal.insets = new Insets(0, 10, 10, 10);
		gbc_jSeparatorHorizontal.gridx = 0;
		gbc_jSeparatorHorizontal.gridy = 4;
		this.add(this.getJSeparatorHorizontal(), gbc_jSeparatorHorizontal);
		
		GridBagConstraints gbc_jPanelUpdateOptions = new GridBagConstraints();
		gbc_jPanelUpdateOptions.gridwidth = 2;
		gbc_jPanelUpdateOptions.insets = new Insets(0, 10, 10, 10);
		gbc_jPanelUpdateOptions.fill = GridBagConstraints.BOTH;
		gbc_jPanelUpdateOptions.gridx = 0;
		gbc_jPanelUpdateOptions.gridy = 5;
		this.add(this.getJPanelUpdateOptions(), gbc_jPanelUpdateOptions);
	}

	
	private JLabel getJLabelProjectTitle() {
		if (jLableTitleProject==null) {
			jLableTitleProject = new JLabel();
			jLableTitleProject.setText(Language.translate("Projekttitel"));
			jLableTitleProject.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLableTitleProject; 
	}
	private JTextField getJTextFieldProjectName() {
		if (jTextFieldProjectName == null) {
			jTextFieldProjectName = new JTextField();
			jTextFieldProjectName.setText(this.currProject.getProjectName());
			jTextFieldProjectName.setName("ProjectTitel");
			jTextFieldProjectName.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldProjectName.getDocument().addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					currProject.setProjectName(jTextFieldProjectName.getText());
				}
				public void insertUpdate(DocumentEvent e) {
					currProject.setProjectName(jTextFieldProjectName.getText());
				}
				public void changedUpdate(DocumentEvent e) {
					currProject.setProjectName(jTextFieldProjectName.getText());
				}
			});
		}
		return jTextFieldProjectName;
	}

	
	private JLabel getJLabelProjectDescription() {
		if (jLableDescription==null) {
			jLableDescription = new JLabel();
			jLableDescription.setText(Language.translate("Beschreibung"));
			jLableDescription.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLableDescription; 
	}
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(this.getJTextAreaProjectDescription());
			jScrollPane.setPreferredSize(new Dimension(300, 300));
		}
		return jScrollPane;
	}
	private JTextArea getJTextAreaProjectDescription() {
		if (jTextAreaProjectDescription == null) {
			jTextAreaProjectDescription = new JTextArea();
			jTextAreaProjectDescription.setText(this.currProject.getProjectDescription());
			jTextAreaProjectDescription.setColumns(0);
			jTextAreaProjectDescription.setLineWrap(true);
			jTextAreaProjectDescription.setWrapStyleWord(true);
			jTextAreaProjectDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaProjectDescription.setCaretPosition(0);
			jTextAreaProjectDescription.getDocument().addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					this.setProjectDescription();
				}
				public void insertUpdate(DocumentEvent e) {
					this.setProjectDescription();
				}
				public void changedUpdate(DocumentEvent e) {
					this.setProjectDescription();
				}
				private void setProjectDescription() {
					if (isPauseProjectDescriptionDocumentListener==false) {
						ProjectInfo.this.currProject.setProjectDescription(getJTextAreaProjectDescription().getText());
					}
				}
			});
		}
		return jTextAreaProjectDescription;
	}
	
	private JLabel getJLabelProjectFolder() {
		if (jLableProjectFolder==null) {
			jLableProjectFolder = new JLabel();
			jLableProjectFolder.setText("JLabel");
			jLableProjectFolder.setFont(new Font("Dialog", Font.BOLD, 14));
			jLableProjectFolder.setText(Language.translate("Verzeichnis"));
		}
		return jLableProjectFolder; 
	}
	private JTextField getjTextFieldProjectFolder() {
		if (jTextFieldProjectFolder == null) {
			jTextFieldProjectFolder = new JTextField();
			jTextFieldProjectFolder.setText(this.currProject.getProjectFolderFullPath());
			jTextFieldProjectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldProjectFolder.setEditable(false);
		}
		return jTextFieldProjectFolder;
	}
	
	private JLabel getLabelStartTab() {
		if (jLabelStartTab == null) {
			jLabelStartTab = new JLabel();
			jLabelStartTab.setText("Start-Tab");
			jLabelStartTab.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelStartTab;
	}
	private JTextField getJTextFieldStartTab() {
		if (jTextFieldStartTab == null) {
			jTextFieldStartTab = new JTextField();
			jTextFieldStartTab.setEditable(false);
			jTextFieldStartTab.setToolTipText(Language.translate("Verwenden Sie das Kontextmenü im Projektbaum, um einen Start-Tab festzulegen."));
			jTextFieldStartTab.setBounds(new Rectangle(140, 285, 520, 26));
		}
		return jTextFieldStartTab;
	}
	
	
	private JSeparator getJSeparatorHorizontal() {
		if (jSeparatorHorizontal == null) {
			jSeparatorHorizontal = new JSeparator();
		}
		return jSeparatorHorizontal;
	}

	
	private JPanel getJPanelUpdateOptions() {
		if (jPanelUpdateOptions == null) {
			jPanelUpdateOptions = new JPanel();
			GridBagLayout gbl_jPanelUpdateOptions = new GridBagLayout();
			gbl_jPanelUpdateOptions.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
			gbl_jPanelUpdateOptions.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
			gbl_jPanelUpdateOptions.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelUpdateOptions.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelUpdateOptions.setLayout(gbl_jPanelUpdateOptions);
			GridBagConstraints gbc_jPanelVersion = new GridBagConstraints();
			gbc_jPanelVersion.fill = GridBagConstraints.BOTH;
			gbc_jPanelVersion.gridheight = 5;
			gbc_jPanelVersion.gridx = 0;
			gbc_jPanelVersion.gridy = 0;
			jPanelUpdateOptions.add(getJPanelVersion(), gbc_jPanelVersion);
			GridBagConstraints gbc_jSeparatorVersionUpdate = new GridBagConstraints();
			gbc_jSeparatorVersionUpdate.fill = GridBagConstraints.VERTICAL;
			gbc_jSeparatorVersionUpdate.gridheight = 5;
			gbc_jSeparatorVersionUpdate.insets = new Insets(0, 15, 0, 15);
			gbc_jSeparatorVersionUpdate.gridx = 1;
			gbc_jSeparatorVersionUpdate.gridy = 0;
			jPanelUpdateOptions.add(getJSeparatorVersionUpdate(), gbc_jSeparatorVersionUpdate);
			GridBagConstraints gbc_jLabelUpdateSite = new GridBagConstraints();
			gbc_jLabelUpdateSite.anchor = GridBagConstraints.WEST;
			gbc_jLabelUpdateSite.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelUpdateSite.gridx = 2;
			gbc_jLabelUpdateSite.gridy = 0;
			jPanelUpdateOptions.add(getJLabelUpdateSite(), gbc_jLabelUpdateSite);
			GridBagConstraints gbc_jTextFieldUpdateSite = new GridBagConstraints();
			gbc_jTextFieldUpdateSite.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldUpdateSite.insets = new Insets(0, 0, 5, 5);
			gbc_jTextFieldUpdateSite.gridx = 3;
			gbc_jTextFieldUpdateSite.gridy = 0;
			jPanelUpdateOptions.add(getJTextFieldUpdateSite(), gbc_jTextFieldUpdateSite);
			GridBagConstraints gbc_jButtonUpdateSiteDefault = new GridBagConstraints();
			gbc_jButtonUpdateSiteDefault.insets = new Insets(0, 0, 5, 5);
			gbc_jButtonUpdateSiteDefault.gridx = 4;
			gbc_jButtonUpdateSiteDefault.gridy = 0;
			jPanelUpdateOptions.add(getJButtonUpdateSiteDefault(), gbc_jButtonUpdateSiteDefault);
			GridBagConstraints gbc_jButtonSearchForUpdate = new GridBagConstraints();
			gbc_jButtonSearchForUpdate.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonSearchForUpdate.gridx = 5;
			gbc_jButtonSearchForUpdate.gridy = 0;
			jPanelUpdateOptions.add(getJButtonSearchForUpdate(), gbc_jButtonSearchForUpdate);
			GridBagConstraints gbc_jLabelLastUpdate = new GridBagConstraints();
			gbc_jLabelLastUpdate.gridwidth = 4;
			gbc_jLabelLastUpdate.fill = GridBagConstraints.VERTICAL;
			gbc_jLabelLastUpdate.anchor = GridBagConstraints.WEST;
			gbc_jLabelLastUpdate.insets = new Insets(5, 0, 5, 0);
			gbc_jLabelLastUpdate.gridx = 2;
			gbc_jLabelLastUpdate.gridy = 1;
			jPanelUpdateOptions.add(getJLabelLastUpdate(), gbc_jLabelLastUpdate);
			GridBagConstraints gbc_jRadioButtonUpdateAutomated = new GridBagConstraints();
			gbc_jRadioButtonUpdateAutomated.insets = new Insets(0, 0, 5, 5);
			gbc_jRadioButtonUpdateAutomated.gridwidth = 2;
			gbc_jRadioButtonUpdateAutomated.anchor = GridBagConstraints.WEST;
			gbc_jRadioButtonUpdateAutomated.gridx = 2;
			gbc_jRadioButtonUpdateAutomated.gridy = 2;
			jPanelUpdateOptions.add(getJRadioButtonUpdateAutomated(), gbc_jRadioButtonUpdateAutomated);
			GridBagConstraints gbc_jRadioButtonUpdateDownloadAndAsk = new GridBagConstraints();
			gbc_jRadioButtonUpdateDownloadAndAsk.insets = new Insets(0, 0, 5, 5);
			gbc_jRadioButtonUpdateDownloadAndAsk.gridwidth = 2;
			gbc_jRadioButtonUpdateDownloadAndAsk.anchor = GridBagConstraints.WEST;
			gbc_jRadioButtonUpdateDownloadAndAsk.gridx = 2;
			gbc_jRadioButtonUpdateDownloadAndAsk.gridy = 3;
			jPanelUpdateOptions.add(getJRadioButtonUpdateDownloadAndAsk(), gbc_jRadioButtonUpdateDownloadAndAsk);
			GridBagConstraints gbc_jRadioButtonUpdateDisabled = new GridBagConstraints();
			gbc_jRadioButtonUpdateDisabled.insets = new Insets(0, 0, 0, 5);
			gbc_jRadioButtonUpdateDisabled.gridwidth = 2;
			gbc_jRadioButtonUpdateDisabled.anchor = GridBagConstraints.WEST;
			gbc_jRadioButtonUpdateDisabled.gridx = 2;
			gbc_jRadioButtonUpdateDisabled.gridy = 4;
			jPanelUpdateOptions.add(getJRadioButtonUpdateDisabled(), gbc_jRadioButtonUpdateDisabled);
			
			ButtonGroup updateAutoConfig = new ButtonGroup();
			updateAutoConfig.add(this.getJRadioButtonUpdateAutomated());
			updateAutoConfig.add(this.getJRadioButtonUpdateDownloadAndAsk());
			updateAutoConfig.add(this.getJRadioButtonUpdateDisabled());
		}
		return jPanelUpdateOptions;
	}
	
	private ProjectInfoVersionPanel getJPanelVersionEdit() {
		if (jPanelVersionEdit==null) {
			jPanelVersionEdit = new ProjectInfoVersionPanel(this.currProject, this);
			jPanelVersionEdit.setPreferredSize(this.versionPanelDimension);
			jPanelVersionEdit.setMinimumSize(this.versionPanelDimension);
		}
		return jPanelVersionEdit;
	}
	
	private JPanel getJPanelVersion() {
		if (jPanelVersion == null) {
			jPanelVersion = new JPanel();
			GridBagLayout gbl_jPanelVersion = new GridBagLayout();
			gbl_jPanelVersion.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelVersion.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelVersion.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelVersion.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelVersion.setLayout(gbl_jPanelVersion);
			GridBagConstraints gbc_jLabelVersion = new GridBagConstraints();
			gbc_jLabelVersion.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelVersion.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelVersion.gridx = 0;
			gbc_jLabelVersion.gridy = 0;
			jPanelVersion.add(getLabelVersionHeader(), gbc_jLabelVersion);
			GridBagConstraints gbc_jButtonEditVersion = new GridBagConstraints();
			gbc_jButtonEditVersion.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonEditVersion.gridx = 1;
			gbc_jButtonEditVersion.gridy = 0;
			jPanelVersion.add(getJButtonEditVersion(), gbc_jButtonEditVersion);
			GridBagConstraints gbc_jLabelVersionInfo = new GridBagConstraints();
			gbc_jLabelVersionInfo.insets = new Insets(15, 0, 5, 0);
			gbc_jLabelVersionInfo.gridwidth = 2;
			gbc_jLabelVersionInfo.gridx = 0;
			gbc_jLabelVersionInfo.gridy = 1;
			jPanelVersion.add(getLabelVersionInfo(), gbc_jLabelVersionInfo);
			jPanelVersion.setPreferredSize(this.versionPanelDimension);
			jPanelVersion.setMinimumSize(this.versionPanelDimension);
			GridBagConstraints gbc_jLabelVersionTag = new GridBagConstraints();
			gbc_jLabelVersionTag.gridwidth = 2;
			gbc_jLabelVersionTag.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelVersionTag.gridx = 0;
			gbc_jLabelVersionTag.gridy = 2;
			jPanelVersion.add(getJLabelVersionTag(), gbc_jLabelVersionTag);
		}
		return jPanelVersion;
	}
	private JLabel getLabelVersionHeader() {
		if (jLabelVersion == null) {
			jLabelVersion = new JLabel();
			jLabelVersion.setText(Language.translate("Version"));
			jLabelVersion.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelVersion;
	}
	private JButton getJButtonEditVersion() {
		if (jButtonEditVersion == null) {
			jButtonEditVersion = new JButton();
			jButtonEditVersion.setToolTipText(Language.translate("Versionnummer bearbeiten"));
			jButtonEditVersion.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonEditVersion.setPreferredSize(new Dimension(45, 26));
			jButtonEditVersion.addActionListener(this);
		}
		return jButtonEditVersion;
	}
	private JLabel getLabelVersionInfo() {
		if (jLabelVersionInfo == null) {
			jLabelVersionInfo = new JLabel(this.currProject.getVersion().toString());
			jLabelVersionInfo.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelVersionInfo.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelVersionInfo;
	}
	private JLabel getJLabelVersionTag() {
		if (jLabelVersionTag == null) {
			jLabelVersionTag = new JLabel("(" + this.currProject.getVersionTag() + ")");
			jLabelVersionTag.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelVersionTag;
	}
	
	private JSeparator getJSeparatorVersionUpdate() {
		if (jSeparatorVersionUpdate == null) {
			jSeparatorVersionUpdate = new JSeparator();
			jSeparatorVersionUpdate.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparatorVersionUpdate;
	}
	
	
	private JLabel getJLabelUpdateSite() {
		if (jLabelUpdateSite == null) {
			jLabelUpdateSite = new JLabel();
			jLabelUpdateSite.setText("Update-Site");
			jLabelUpdateSite.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelUpdateSite;
	}
	private JTextField getJTextFieldUpdateSite() {
		if (jTextFieldUpdateSite == null) {
			jTextFieldUpdateSite = new JTextField();
			jTextFieldUpdateSite.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldUpdateSite.setText(this.currProject.getUpdateSite());
			jTextFieldUpdateSite.getDocument().addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					this.setUpdateSite();
				}
				public void insertUpdate(DocumentEvent e) {
					this.setUpdateSite();
				}
				public void changedUpdate(DocumentEvent e) {
					this.setUpdateSite();
				}
				private void setUpdateSite() {
					String newSite = jTextFieldUpdateSite.getText();
					if (newSite.equals("")==true) {
						currProject.setUpdateSite(null);
					} else {
						currProject.setUpdateSite(newSite);	
					}
				}
				
			});
		}
		return jTextFieldUpdateSite;
	}
	private JButton getJButtonUpdateSiteDefault() {
		if (jButtonUpdateSiteDefault == null) {
			jButtonUpdateSiteDefault = new JButton();
			jButtonUpdateSiteDefault.setToolTipText(Language.translate("Standard verwenden"));
			jButtonUpdateSiteDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonUpdateSiteDefault.setPreferredSize(new Dimension(45, 26));
			jButtonUpdateSiteDefault.addActionListener(this);
		}
		return jButtonUpdateSiteDefault;
	}
	private JButton getJButtonSearchForUpdate() {
		if (jButtonSearchForUpdate == null) {
			jButtonSearchForUpdate = new JButton();
			jButtonSearchForUpdate.setToolTipText(Language.translate("Nach Update suchen ..."));
			jButtonSearchForUpdate.setIcon(GlobalInfo.getInternalImageIcon("Search.png"));
			jButtonSearchForUpdate.setPreferredSize(new Dimension(45, 26));
			jButtonSearchForUpdate.addActionListener(this);
		}
		return jButtonSearchForUpdate;
	}
	private JLabel getJLabelLastUpdate() {
		if (jLabelLastUpdate == null) {
			jLabelLastUpdate = new JLabel();
			jLabelLastUpdate.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelLastUpdate.setText(Language.translate("Nach Updates suchen") + ":");
		}
		return jLabelLastUpdate;
	}
	private JRadioButton getJRadioButtonUpdateAutomated() {
		if (jRadioButtonUpdateAutomated == null) {
			jRadioButtonUpdateAutomated = new JRadioButton();
			jRadioButtonUpdateAutomated.setFont(new Font("Dialog", Font.PLAIN, 12));
			if (this.currProject.getUpdateAutoConfiguration()==0) {
				jRadioButtonUpdateAutomated.setSelected(true);
			}
			jRadioButtonUpdateAutomated.setText(Language.translate("Updates automatisch installieren"));
			jRadioButtonUpdateAutomated.addActionListener(this);
		}
		return jRadioButtonUpdateAutomated;
	}
	private JRadioButton getJRadioButtonUpdateDownloadAndAsk() {
		if (jRadioButtonUpdateDownloadAndAsk == null) {
			jRadioButtonUpdateDownloadAndAsk = new JRadioButton();
			jRadioButtonUpdateDownloadAndAsk.setFont(new Font("Dialog", Font.PLAIN, 12));
			if (this.currProject.getUpdateAutoConfiguration()==1) {
				jRadioButtonUpdateDownloadAndAsk.setSelected(true);
			}
			jRadioButtonUpdateDownloadAndAsk.setText(Language.translate("Update automatisch herunterladen, Installationszeitpunkt manuell festlegen"));
			jRadioButtonUpdateDownloadAndAsk.addActionListener(this);
		}
		return jRadioButtonUpdateDownloadAndAsk;
	}
	private JRadioButton getJRadioButtonUpdateDisabled() {
		if (jRadioButtonUpdateDisabled == null) {
			jRadioButtonUpdateDisabled = new JRadioButton();
			jRadioButtonUpdateDisabled.setFont(new Font("Dialog", Font.PLAIN, 12));
			if (this.currProject.getUpdateAutoConfiguration()==2) {
				jRadioButtonUpdateDisabled.setSelected(true);
			}
			jRadioButtonUpdateDisabled.setText(Language.translate("Updates nicht automatisch herunterladen oder installieren"));
			jRadioButtonUpdateDisabled.addActionListener(this);
		}
		return jRadioButtonUpdateDisabled;
	}

	/**
	 * Applies the current project view settings.
	 */
	private void applyProjectViewSettings(){

		if (this.currProject.getProjectView().equals(Project.VIEW_Developer)) {
			// --- Show edit button for version edit ------------  
			this.getLabelVersionHeader().setPreferredSize(new Dimension(100, 26));
			this.getLabelVersionHeader().setMinimumSize(new Dimension(100, 26));
			this.getJButtonEditVersion().setVisible(true);
			
		} else if (this.currProject.getProjectView().equals(Project.VIEW_User)) {
			// --- Hide edit button for version edit ------------
			if (this.isVersionEdit) this.switchJPanelVersion();
			this.getLabelVersionHeader().setPreferredSize(new Dimension(145, 26));
			this.getLabelVersionHeader().setMinimumSize(new Dimension(145, 26));
			this.getJButtonEditVersion().setVisible(false);
		}
	}
	
	/**
	 * Switch JPanel for the version of the project between edit and view mode.
	 */
	public void switchJPanelVersion() {
		
		GridBagConstraints gbc_jPanelVersion = new GridBagConstraints();
		gbc_jPanelVersion.fill = GridBagConstraints.BOTH;
		gbc_jPanelVersion.gridheight = 5;
		gbc_jPanelVersion.gridx = 0;
		gbc_jPanelVersion.gridy = 0;

		if (this.isVersionEdit==true) {
			this.getJPanelUpdateOptions().remove(this.getJPanelVersionEdit());
			this.getJPanelUpdateOptions().add(this.getJPanelVersion(), gbc_jPanelVersion);
			this.getLabelVersionInfo().setText(this.currProject.getVersion().toString());
			this.getJLabelVersionTag().setText(this.currProject.getVersionTag());
			this.isVersionEdit = false;
		} else {
			this.getJPanelUpdateOptions().remove(this.getJPanelVersion());
			this.getJPanelUpdateOptions().add(this.getJPanelVersionEdit(), gbc_jPanelVersion);
			this.getJPanelVersionEdit().updateVersionInformation();
			this.isVersionEdit = true;
		}
		this.validate();
		this.repaint();
	}
	
	/**
	 * Updates the date of the last update check.
	 */
	private void updateLastUpdateCheckDate() {
		
		String dateTextChecked = "never checked";
		long timeStampLastChecked = this.currProject.getUpdateDateLastChecked();
		if (timeStampLastChecked!=0) {
			dateTextChecked = "last checked at " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(timeStampLastChecked));
		}
		// --- Define the update option header text -------
		String updateOptionHeader = Language.translate("Nach Updates suchen") + " (" + dateTextChecked + "):";
		this.getJLabelLastUpdate().setText(updateOptionHeader);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		String updateAction = updateObject.toString();
		if (updateAction.equals(Project.VIEW_TabsLoaded) || updateAction.equalsIgnoreCase(Project.CHANGED_ProjectStartTab)) {
			// --- Set the start tab information --------------------
			ProjectWindow projectWindow = (ProjectWindow) this.currProject.getProjectEditorWindow();
			this.getJTextFieldStartTab().setText(projectWindow.getStartTabInformation());
			
		} else if (updateAction.equals(Project.CHANGED_ProjectName)) {
			// --- Display change of the project name ---------------
			if (getJTextFieldProjectName().isFocusOwner()==false) {
				this.getJTextFieldProjectName().setText(this.currProject.getProjectName());				
			}
			
		} else if (updateAction.equals(Project.CHANGED_ProjectDescription) ) {
			// --- Display change of the project description --------
			if (this.getJTextAreaProjectDescription().isFocusOwner()==false ) {
				this.isPauseProjectDescriptionDocumentListener = true;
				this.getJTextAreaProjectDescription().setText(this.currProject.getProjectDescription());
				this.isPauseProjectDescriptionDocumentListener = false;
			}
			
		} else if (updateAction.equals(Project.CHANGED_ProjectView) ) {
			// --- Applies the project view settings ----------------
			this.applyProjectViewSettings();
			
		} else if (updateAction.equals(Project.CHANGED_UpdateDateLastChecked)) {
			// --- The date of the last update-check has changed ----
			this.updateLastUpdateCheckDate();
			
		}
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonEditVersion()) {
			this.switchJPanelVersion();
			
		} else if (ae.getSource()==this.getJButtonUpdateSiteDefault()) {
			this.getJTextFieldUpdateSite().setText(GlobalInfo.DEFAULT_AWB_PROJECT_REPOSITORY);
			this.currProject.setUpdateSite(GlobalInfo.DEFAULT_AWB_PROJECT_REPOSITORY);
			
		} else if (ae.getSource()==this.getJButtonSearchForUpdate()) {
			this.currProject.doProjectUpdate(true);
			
		} else if (ae.getSource()==this.getJRadioButtonUpdateAutomated()) {
			this.currProject.setUpdateAutoConfiguration(0);
		} else if (ae.getSource()==this.getJRadioButtonUpdateDownloadAndAsk()) {
			this.currProject.setUpdateAutoConfiguration(1);
		} else if (ae.getSource()==this.getJRadioButtonUpdateDisabled()) {
			this.currProject.setUpdateAutoConfiguration(2);
		} else {
			System.err.println("Unkknow: action => " + ae.getSource().toString());
		}
	}
	
}  
