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
package agentgui.core.project.transfer.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.InstallationPackageFinder;
import agentgui.core.config.InstallationPackageFinder.InstallationPackageDescription;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.transfer.ProjectExportController;
import agentgui.core.project.transfer.ProjectExportSettings;
import agentgui.core.project.transfer.ProjectExportSettingsController;
import de.enflexit.common.swing.fileSelection.DirectoryEvaluator;
import de.enflexit.common.swing.fileSelection.DirectoryEvaluator.FileDescriptor;
import de.enflexit.common.swing.fileSelection.DirectoryEvaluatorListener;
import de.enflexit.common.swing.fileSelection.DirectoryPanel;
import de.enflexit.common.swing.fileSelection.FileTree;
import de.enflexit.common.swing.fileSelection.FileTreeAdapter;

/**
 * The Class ProjectExportDialog.
 */
public class ProjectExportDialog extends JDialog implements ActionListener, DirectoryEvaluatorListener {

	private static final long serialVersionUID = 7642101726572826993L;

	private static final String[] ALWAYS_SELECTED_FILES = {"agentgui.xml", "agentgui.bin", "/setupsEnv/~GeneralGraphSettings~.xml"};
	
	private Project project;
	private ProjectExportSettingsController projectExportSettingsController;
	
	private JLabel jLabelHeader;
	private JCheckBox jCheckBoxIncludeInstallationPackage;
	private JCheckBox jCheckBoxIncludeAllSetups;

	private JComboBox<InstallationPackageDescription> jComboBoxSelectInstallationPackage;
	private DefaultComboBoxModel<InstallationPackageDescription> installationPackagesComboBoxModel;

	private JButton jButtonOk;
	private JButton jButtonCancel;

	private JScrollPane jScrollPaneSetupSelection;
	private DefaultListModel<String> simulationSetupListModel;
	private JList<String> jListSetupSelection;
	private boolean pauseSetupsListSelectionListener;
	private List<String> lastSelectedSetups;
	
	private JLabel jLabelFileExportSelection;
	private DirectoryPanel directoryPanel;
	private FileTree fileTree;

	private boolean canceled = false;

	private FileDescriptor fdSetups;
	private FileDescriptor fdSetupsEnvironment;
	private FileDescriptor fdTemp;
	private FileDescriptor fdSecurity;
	private List<FileDescriptor> fdGitIgnoreList;
	
	/**
	 * Instantiates a new project export dialog.
	 * @param project the project
	 */
	public ProjectExportDialog(Project project, ProjectExportController projectExportController) {
		this.project = project;
		this.projectExportSettingsController = new ProjectExportSettingsController(project, projectExportController);
		this.initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {

		this.setTitle(Language.translate("Projekt exportieren"));
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.gridwidth = 2;
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(10, 10, 5, 10);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		getContentPane().add(getJLabelHeader(), gbc_jLabelHeader);

		GridBagConstraints gbc_jCheckBoxIncludeProduct = new GridBagConstraints();
		gbc_jCheckBoxIncludeProduct.insets = new Insets(5, 10, 0, 10);
		gbc_jCheckBoxIncludeProduct.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxIncludeProduct.gridx = 0;
		gbc_jCheckBoxIncludeProduct.gridy = 1;
		getContentPane().add(getJCheckBoxIncludeInstallationPackage(), gbc_jCheckBoxIncludeProduct);
		
		GridBagConstraints gbc_jLabelFileExportSelection = new GridBagConstraints();
		gbc_jLabelFileExportSelection.insets = new Insets(5, 0, 0, 10);
		gbc_jLabelFileExportSelection.anchor = GridBagConstraints.WEST;
		gbc_jLabelFileExportSelection.gridx = 1;
		gbc_jLabelFileExportSelection.gridy = 1;
		getContentPane().add(getJLabelFileExportSelection(), gbc_jLabelFileExportSelection);

		GridBagConstraints gbc_jComboBoxSelectOS = new GridBagConstraints();
		gbc_jComboBoxSelectOS.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSelectOS.insets = new Insets(5, 10, 5, 10);
		gbc_jComboBoxSelectOS.gridx = 0;
		gbc_jComboBoxSelectOS.gridy = 2;
		getContentPane().add(getJComboBoxSelectInstallationPackage(), gbc_jComboBoxSelectOS);
		
		GridBagConstraints gbc_jCheckBoxIncludeAllSetups = new GridBagConstraints();
		gbc_jCheckBoxIncludeAllSetups.insets = new Insets(10, 10, 0, 10);
		gbc_jCheckBoxIncludeAllSetups.anchor = GridBagConstraints.NORTHWEST;
		gbc_jCheckBoxIncludeAllSetups.gridx = 0;
		gbc_jCheckBoxIncludeAllSetups.gridy = 3;
		getContentPane().add(getJCheckBoxIncludeAllSetups(), gbc_jCheckBoxIncludeAllSetups);
		
		GridBagConstraints gbc_jScrollPaneSetupSelection = new GridBagConstraints();
		gbc_jScrollPaneSetupSelection.insets = new Insets(5, 10, 5, 10);
		gbc_jScrollPaneSetupSelection.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneSetupSelection.gridx = 0;
		gbc_jScrollPaneSetupSelection.gridy = 4;
		getContentPane().add(getJScrollPaneSetupSelection(), gbc_jScrollPaneSetupSelection);
		
		GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
		gbc_jButtonOk.insets = new Insets(10, 0, 20, 45);
		gbc_jButtonOk.anchor = GridBagConstraints.EAST;
		gbc_jButtonOk.gridx = 0;
		gbc_jButtonOk.gridy = 5;
		getContentPane().add(getJButtonOk(), gbc_jButtonOk);
		
		GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
		gbc_jButtonCancel.insets = new Insets(10, 35, 20, 0);
		gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
		gbc_jButtonCancel.gridx = 1;
		gbc_jButtonCancel.gridy = 5;
		getContentPane().add(getJButtonCancel(), gbc_jButtonCancel);

		GridBagConstraints gbc_directoryPanel = new GridBagConstraints();
		gbc_directoryPanel.insets = new Insets(5, 0, 5, 10);
		gbc_directoryPanel.gridheight = 3;
		gbc_directoryPanel.fill = GridBagConstraints.BOTH;
		gbc_directoryPanel.gridx = 1;
		gbc_directoryPanel.gridy = 2;
		getContentPane().add(getDirectoryPanel(), gbc_directoryPanel);


		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				canceled = true;
				ProjectExportDialog.this.setVisible(false);
			}
		});
		
		this.setModal(true);
		this.setSize(830, 500);
		this.setLocationRelativeTo(null);
	}

	/**
	 * Gets the j label header.
	 * @return the j label header
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel(Language.translate("Exportiere Projekt") + " '" + project.getProjectName() + "'");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return jLabelHeader;
	}

	/**
	 * Gets the j check box include installation package.
	 * @return the j check box include installation package
	 */
	private JCheckBox getJCheckBoxIncludeInstallationPackage() {
		if (jCheckBoxIncludeInstallationPackage == null) {
			jCheckBoxIncludeInstallationPackage = new JCheckBox(Language.translate("Installationspaket einbeziehen"));
			jCheckBoxIncludeInstallationPackage.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxIncludeInstallationPackage.addActionListener(this);
		}
		return jCheckBoxIncludeInstallationPackage;
	}
	/**
	 * Sets it the installation package configuration is allowed or not.
	 * @param allowConfiguration the new allow installation package configuration
	 */
	public void setAllowInstallationPackageConfiguration(boolean allowConfiguration) {
		this.getJCheckBoxIncludeInstallationPackage().setEnabled(allowConfiguration);
	}
	
	/**
	 * Gets the j combo box select installation package.
	 * @return the j combo box select installation package
	 */
	private JComboBox<InstallationPackageDescription> getJComboBoxSelectInstallationPackage() {
		if (jComboBoxSelectInstallationPackage == null) {
			jComboBoxSelectInstallationPackage = new JComboBox<InstallationPackageDescription>();
			jComboBoxSelectInstallationPackage.setFont(new Font("Dialog", Font.BOLD, 12));
			jComboBoxSelectInstallationPackage.setModel(this.getInstallationPackagesComboBoxModel());
			jComboBoxSelectInstallationPackage.setEnabled(false);
			jComboBoxSelectInstallationPackage.setRenderer(new ListCellRenderer<InstallationPackageDescription>() {
				@Override
				public Component getListCellRendererComponent(JList<? extends InstallationPackageDescription> list, InstallationPackageDescription value, int index, boolean isSelected, boolean cellHasFocus) {
					JLabel visComp = new DefaultListCellRenderer();
					if (value==null) {
						visComp.setText(null);
					} else {
						visComp.setText(value.toString(false));
					}
					return visComp;
				}
			});
			jComboBoxSelectInstallationPackage.addActionListener(this);
		}
		return jComboBoxSelectInstallationPackage;
	}
	/**
	 * Gets the installation packages combo box model.
	 * @return the installation packages combo box model
	 */
	private DefaultComboBoxModel<InstallationPackageDescription> getInstallationPackagesComboBoxModel() {
		if (installationPackagesComboBoxModel == null) {
			// --- Get list of available installation packages --------------
			InstallationPackageFinder ipf = new InstallationPackageFinder();
			installationPackagesComboBoxModel = new DefaultComboBoxModel<>(ipf.getInstallationPackageVector());
		}
		return installationPackagesComboBoxModel;
	}

	/**
	 * Gets the j label file export selection.
	 * @return the j label file export selection
	 */
	private JLabel getJLabelFileExportSelection() {
		if (jLabelFileExportSelection == null) {
			jLabelFileExportSelection = new JLabel(Language.translate("Details zum Dateiexport") + ":");
			jLabelFileExportSelection.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelFileExportSelection;
	}
	
	/**
	 * Returns the non editable files.
	 * @return the non editable files
	 */
	private ArrayList<File> getAlwaysSelectedFiles() {
	
		ArrayList<File> nonEditableFiles = new ArrayList<>();
		String projectBasePath = this.project.getProjectFolderFullPath();
		for (int i = 0; i < ALWAYS_SELECTED_FILES.length; i++) {
			String filePath = projectBasePath + ALWAYS_SELECTED_FILES[i];
			File file = new File(filePath);
			if (file.exists()==true) {
				nonEditableFiles.add(file);
			}
		}
		return nonEditableFiles;
	}
	/**
	 * Gets the directory panel.
	 * @return the directory panel
	 */
	private DirectoryPanel getDirectoryPanel() {
		if (directoryPanel == null) {
			directoryPanel = new DirectoryPanel(new File(this.project.getProjectFolderFullPath()), this.getAlwaysSelectedFiles());
			directoryPanel.addDirectoryEvaluatorListener(this);
			directoryPanel.addFileTreeListener(new FileTreeAdapter() {
				@Override
				public void onFileTreeElementEdited(DefaultMutableTreeNode treeNodeEdited) {
					ProjectExportDialog.this.onFileTreeElementEdited(treeNodeEdited);
				}
			});
		}
		return directoryPanel;
	}
	/**
	 * Returns the file tree that is located in the local {@link DirectoryPanel}.
	 * @return the file tree
	 */
	public FileTree getFileTree() {
		if (fileTree==null) {
			fileTree = this.getDirectoryPanel().getFileTree();
		}
		return fileTree;
	}
	
	
	/**
	 * Gets the JCheckBox include all setups.
	 * @return the JCheckBox include all setups
	 */
	private JCheckBox getJCheckBoxIncludeAllSetups() {
		if (jCheckBoxIncludeAllSetups == null) {
			jCheckBoxIncludeAllSetups = new JCheckBox(Language.translate("Alle Setups exportieren"));
			jCheckBoxIncludeAllSetups.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxIncludeAllSetups.setSelected(true);
			jCheckBoxIncludeAllSetups.addActionListener(this);
		}
		return jCheckBoxIncludeAllSetups;
	}
	/**
	 * Gets the scroll pane.
	 * @return the scroll pane
	 */
	private JScrollPane getJScrollPaneSetupSelection() {
		if (jScrollPaneSetupSelection == null) {
			jScrollPaneSetupSelection = new JScrollPane();
			jScrollPaneSetupSelection.setBorder(BorderFactory.createEtchedBorder());
			jScrollPaneSetupSelection.setMinimumSize(new Dimension(380, 200));
			jScrollPaneSetupSelection.setViewportView(this.getJListSetupSelection());
		}
		return jScrollPaneSetupSelection;
	}
	/**
	 * Gets the simulation setup list model.
	 * @return the simulation setup list model
	 */
	private DefaultListModel<String> getSimulationSetupListModel() {
		if (simulationSetupListModel == null) {
			simulationSetupListModel = new DefaultListModel<>();
			Vector<String> setupsVector = new Vector<String>(this.project.getSimulationSetups().keySet());
			Collections.sort(setupsVector);
			for (int i = 0; i < setupsVector.size(); i++) {
				simulationSetupListModel.addElement(setupsVector.get(i));
			}
		}
		return simulationSetupListModel;
	}
	/**
	 * Gets the {@link JList} for selecting the {@link SimulationSetup}s to export
	 * @return the j list setup selection
	 */
	private JList<String> getJListSetupSelection() {
		if (jListSetupSelection == null) {
			jListSetupSelection = new JList<String>();
			jListSetupSelection.setEnabled(false);
			jListSetupSelection.setModel(this.getSimulationSetupListModel());
			jListSetupSelection.setCellRenderer(new CheckBoxListCellRenderer());
			jListSetupSelection.setSelectionModel(new DefaultListSelectionModel() {
				private static final long serialVersionUID = 8444856337414000172L;
				@Override
				public void setSelectionInterval(int index0, int index1) {
					if (super.isSelectedIndex(index0)) {
						super.removeSelectionInterval(index0, index1);
					} else {
						super.addSelectionInterval(index0, index1);
					}
				}
			});
			jListSetupSelection.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					if (lse.getValueIsAdjusting()==false && pauseSetupsListSelectionListener==false) {
						// --- Find out which indexes are new -----------------
						ArrayList<String> currentSetups = new ArrayList<>(getJListSetupSelection().getSelectedValuesList());
						ArrayList<String> addedSetups   = ProjectExportDialog.this.getAddedSetups(currentSetups);
						ArrayList<String> removedSetups = ProjectExportDialog.this.getRemovedSetups(currentSetups);
						updateLastSelectedSetups();
						// --- Act on added setups  ---------------------------
						for (int i = 0; i < addedSetups.size(); i++) {
							setIncludeSetup(addedSetups.get(i), true);
						}
						// --- Act on removed setups  -------------------------
						for (int i = 0; i < removedSetups.size(); i++) {
							setIncludeSetup(removedSetups.get(i), false);
						}
						
					}
				}
			});
			// --- Initially select all entries ----------------
			this.selectAllSetupsInSetupList();
		}
		return jListSetupSelection;
	}
	private void selectAllSetupsInSetupList() {
		this.getJListSetupSelection().setSelectionInterval(0, (this.getSimulationSetupListModel().size()-1));
	}
	private List<String> getLastSelectedSetups() {
		if (lastSelectedSetups==null) {
			lastSelectedSetups = new ArrayList<>();
		}
		return lastSelectedSetups;
	}
	private void updateLastSelectedSetups() {
		this.lastSelectedSetups = this.getJListSetupSelection().getSelectedValuesList();
	}
	private ArrayList<String> getAddedSetups(ArrayList<String> currSelectedSetups) {
		ArrayList<String> newSetups = new ArrayList<>(currSelectedSetups);
		for (int i = 0; i < this.getLastSelectedSetups().size(); i++) {
			newSetups.remove(this.getLastSelectedSetups().get(i));
		}
		return newSetups;
	}
	private ArrayList<String> getRemovedSetups(ArrayList<String> currSelectedSetups) {
		ArrayList<String> removedSetups = new ArrayList<>(this.getLastSelectedSetups());
		for (int i = 0; i < currSelectedSetups.size(); i++) {
			removedSetups.remove(currSelectedSetups.get(i));
		}
		return removedSetups;
	}
	
	
	
	/**
	 * Gets the ok button.
	 * @return the j button ok
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setMinimumSize(new Dimension(100, 28));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	/**
	 * Gets the cancel button
	 * @return the j button cancel
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setMinimumSize(new Dimension(100, 28));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}


	/**
	 * Sets the export settings according to the form
	 */
	private void getSettingsFromForm() {
		this.getExportSettings().setSimSetups(this.getJListSetupSelection().getSelectedValuesList());
		this.getExportSettings().setIncludeInstallationPackage(this.getJCheckBoxIncludeInstallationPackage().isSelected());
		if (this.getExportSettings().isIncludeInstallationPackage() == true) {
			this.getExportSettings().setInstallationPackage((InstallationPackageDescription) this.getJComboBoxSelectInstallationPackage().getSelectedItem());
		} else {
			this.getExportSettings().setInstallationPackage(null);
		}
		this.projectExportSettingsController.addFilesToExcludeList(this.getDirectoryPanel().getDirectoryEvaluator().getPathList(false));
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.fileSelection.DirectoryEvaluatorListener#onEvaluationWasFinalized()
	 */
	@Override
	public void onEvaluationWasFinalized() {

		// ----------------------------------------------------------
		// --- First, set everything selected -----------------------
		// ----------------------------------------------------------
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.getFileTree().getModel().getRoot();
		
		FileDescriptor fd = (FileDescriptor) rootNode.getUserObject();
		fd.setSelected(true);
		
		this.getFileTree().setChildrenNodesSelected(rootNode, true);
		
		// ----------------------------------------------------------
		// --- Set default excludes ---------------------------------
		// ----------------------------------------------------------
		DirectoryEvaluator de = this.getDirectoryPanel().getDirectoryEvaluator();
		this.fdSetups = de.getFileDescriptorByFileName(Project.DEFAULT_SUB_FOLDER_4_SETUPS, true);
		this.fdSetupsEnvironment = de.getFileDescriptorByFileName(Project.DEFAULT_SUB_FOLDER_ENV_SETUPS, true);
		
		this.fdSecurity = de.getFileDescriptorByFileName(Project.DEFAULT_SUB_FOLDER_SECURITY, true);
		if (this.fdSecurity!=null) {
			this.fdSecurity.setSelected(false);
			this.getFileTree().setChildrenNodesSelected(this.fdSecurity.getTreeNode(), false);
		}

		this.fdTemp = de.getFileDescriptorByFileName(Project.DEFAULT_TEMP_FOLDER,  true);
		if (this.fdTemp!=null) {
			this.fdTemp.setSelected(false);
			this.getFileTree().setChildrenNodesSelected(this.fdTemp.getTreeNode(), false);
		}
		
		this.fdGitIgnoreList = de.getFileDescriptorListByFileName(".gitignore", false);
		for (int i = 0; i < this.fdGitIgnoreList.size(); i++) {
			FileDescriptor fdi = this.fdGitIgnoreList.get(i);
			this.getFileTree().setNodesSelected(fdi.getTreeNode(), false);
		}
		
	}
	
	/**
	 * Returns the setup name.
	 *
	 * @param fileDescriptor the file descriptor
	 * @return the setup
	 */
	private String getSetupName(FileDescriptor fileDescriptor) {
		return this.projectExportSettingsController.getSetupForFile(fileDescriptor.getFile());
	}
	
	
	/**
	 * Will be invoked if a {@link FileTree} element was edited.
	 * @param treeNodeEdited the tree node edited
	 */
	private void onFileTreeElementEdited(DefaultMutableTreeNode treeNodeEdited) {
		
		FileDescriptor fdSelected = (FileDescriptor) treeNodeEdited.getUserObject();
		boolean wasSelected = fdSelected.isSelected();
		
		String setupName = this.getSetupName(fdSelected);
		
		if (fdSelected==this.fdSetupsEnvironment) {
			// --- Select / un-select all setup files ---------------
			if (this.fdSetups.isSelected()!=wasSelected) {
				this.fdSetups.setSelected(wasSelected);
				this.getFileTree().setChildrenNodesSelected(this.fdSetups.getTreeNode(), wasSelected);
				this.setIncludeAllSetups(wasSelected);
			}
			
		} else if (fdSelected==this.fdSetups) {
			// --- Select / un-select all setup files ---------------			
			if (this.fdSetupsEnvironment.isSelected()!=wasSelected) {
				this.fdSetupsEnvironment.setSelected(wasSelected);
				this.getFileTree().setChildrenNodesSelected(this.fdSetupsEnvironment.getTreeNode(), wasSelected);
				this.setIncludeAllSetups(wasSelected);
			}
			
		} else if (setupName!=null) {
			// --- Select / un-select files of setup ---------------- 
			this.setIncludeSetup(setupName, wasSelected);
			
		}
		
	}
	
	/**
	 * Select all elements that correspond to a setup .
	 *
	 * @param setupName the setup name
	 * @param setSelected the set selected
	 */
	private void setIncludeSetup(String setupName, boolean setSelected) {
		
		// --- Check if export all setups is marked ----------------- 
		if (setSelected==false && this.getJCheckBoxIncludeAllSetups().isSelected()==true) {
			this.getJCheckBoxIncludeAllSetups().setSelected(false);
			this.getJListSetupSelection().setEnabled(true);
		}
		
		// --- Select / deselect in the setup list (left) -----------
		ListModel<String> listModel = this.getJListSetupSelection().getModel();
		int oldSelectionIndices[] = this.getJListSetupSelection().getSelectedIndices();
		if (setSelected==true) {
			
			if (this.getJListSetupSelection().getSelectedValuesList().contains(setupName)==false) {
				// --- Add to the list of selected values -----------
				int newSelectionIndices[] = new int[oldSelectionIndices.length+1];
				System.arraycopy(oldSelectionIndices, 0, newSelectionIndices, 0, oldSelectionIndices.length);
				// --- Search for the new setup ---------------------
				for (int i = 0; i < listModel.getSize(); i++) {
					String setup = listModel.getElementAt(i);
					if (setup.equals(setupName)==true) {
						newSelectionIndices[newSelectionIndices.length-1] = i;
						break;
					}
				}
				// --- Set new selections to JList ------------------
				this.pauseSetupsListSelectionListener=true;
				this.getJListSetupSelection().setSelectedIndices(newSelectionIndices);
				this.pauseSetupsListSelectionListener=false;
			}
			
		} else {
			
			if (this.getJListSetupSelection().getSelectedValuesList().contains(setupName)==true) {
				// --- Remove from the list of selected values ------
				int newSelectionSize = oldSelectionIndices.length-1;
				if (newSelectionSize==0) {
					this.getJListSetupSelection().clearSelection();
				} else {
					int newSelectionIndices[] = new int[newSelectionSize];
					// --- Search for the new setup ---------------------
					int k = 0;
					for (int i = 0; i < oldSelectionIndices.length; i++) {
						String setup = listModel.getElementAt(oldSelectionIndices[i]);
						if (setup.equals(setupName)==false) {
							newSelectionIndices[k] = oldSelectionIndices[i];
							k++;
						}
					}
					// --- Set new selections to JList ------------------
					this.pauseSetupsListSelectionListener=true;
					this.getJListSetupSelection().setSelectedIndices(newSelectionIndices);
					this.pauseSetupsListSelectionListener=false;	
				}
			}
			
		}
		this.updateLastSelectedSetups();
		
		// --- Select / deselect the corresponding files (right) ----
		DirectoryEvaluator de = this.getDirectoryPanel().getDirectoryEvaluator();
		List<File> setupFiles = this.projectExportSettingsController.getFilesForSetup(setupName);
		if (setupFiles!=null) {
			for (int i = 0; i < setupFiles.size(); i++) {
				File setupFile = setupFiles.get(i);
				FileDescriptor fd = de.getFileDescriptorByFile(setupFile);
				if (fd!=null && fd.isSelected()!=setSelected) {
					fd.setSelected(setSelected);
					
					// --- Check the selection state of the parent folder, change if necessary --------------
					boolean parentSelectionChanged = false;
					File parentFolder = setupFile.getParentFile();
					FileDescriptor fdParent =  de.getFileDescriptorByFile(parentFolder);
						
					if (fd.isSelected()==true && fdParent.isSelected()==false) {
						// --- If a setup was selected, make sure the parent folder is also selected --------
						fdParent.setSelected(true);
						parentSelectionChanged = true;
					} else if (fd.isSelected()==false && fdParent.isSelected()==true) {
						// --- If a setup was deselected, deselect the parent folder if there are no selected files left
						if (fdParent.hasSelectedChildren()==false) {
							fdParent.setSelected(false);
							parentSelectionChanged = true;
						}
					}
					
					// --- Reload as much of the tree as necessary ------------
					if (parentSelectionChanged==true) {
						de.getFileTreeModel().reload(fdParent.getTreeNode());
					} else {
						de.getFileTreeModel().reload(fd.getTreeNode());
					}
				}
			}	// end for
		}
	}
	
	/**
	 * Sets the visualization to include /exclude all setups.
	 * @param includeAllSetups the new include all setups
	 */
	private void setIncludeAllSetups(boolean includeAllSetups) {
		
		// --- Check the indication check box first -----------------
		if (this.getJCheckBoxIncludeAllSetups().isSelected()!=includeAllSetups) {
			this.getJCheckBoxIncludeAllSetups().setSelected(includeAllSetups);
		}
		
		// --- Set the list of setups -------------------------------
		if (includeAllSetups==true) {
			this.selectAllSetupsInSetupList();
		} else {
			this.getJListSetupSelection().clearSelection();
		}
		
		this.updateLastSelectedSetups();
		this.getJListSetupSelection().setEnabled(!includeAllSetups);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == this.getJCheckBoxIncludeInstallationPackage()) {
			boolean selectionState = this.getJCheckBoxIncludeInstallationPackage().isSelected();
			this.getExportSettings().setIncludeInstallationPackage(selectionState);
			this.getJComboBoxSelectInstallationPackage().setEnabled(selectionState);
			if (selectionState == true) {
				this.getExportSettings().setInstallationPackage((InstallationPackageDescription) this.getJComboBoxSelectInstallationPackage().getSelectedItem());
			} else {
				this.getExportSettings().setInstallationPackage(null);
			}

		} else if (ae.getSource() == this.getJCheckBoxIncludeAllSetups()) {
			this.setIncludeAllSetups(this.getJCheckBoxIncludeAllSetups().isSelected());

		} else if (ae.getSource()==this.getJButtonOk()) {
			this.getSettingsFromForm();
			this.canceled = false;
			this.dispose();

		} else if (ae.getSource()==this.getJButtonCancel()) {
			this.canceled = true;
			this.dispose();

		}
	}

	/**
	 * Gets the export settings.
	 * @return the export settings
	 */
	public ProjectExportSettings getExportSettings() {
		return this.projectExportSettingsController.getProjectExportSettings();
	}

	/**
	 * Checks if is canceled.
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	

	/**
	 * {@link ListCellRenderer} implementation based on {@link JCheckBox}.
	 *
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer<String> {

		private static final long serialVersionUID = 6526933726761540148L;

		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
			this.setText(value);
			this.setSelected(isSelected);
			this.setEnabled(list.isEnabled());
			return this;
		}
	}

}
