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
import java.util.Collections;
import java.util.Vector;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import agentgui.core.application.Language;
import agentgui.core.config.InstallationPackageFinder;
import agentgui.core.config.InstallationPackageFinder.InstallationPackageDescription;
import agentgui.core.project.Project;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.transfer.ProjectExportSettings;
import de.enflexit.common.swing.fileSelection.DirectoryPanel;

/**
 * The Class ProjectExportDialog.
 */
public class ProjectExportDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 7642101726572826993L;

	private Project project;
	private ProjectExportSettings exportSettings;
	
	private JLabel jLabelHeader;
	private JCheckBox jCheckBoxIncludeInstallationPackage;
	private JCheckBox jCheckBoxIncludeAllSetups;

	private JComboBox<InstallationPackageDescription> jComboBoxSelectInstallationPackage;
	private DefaultComboBoxModel<InstallationPackageDescription> installationPackagesComboBoxModel;

	private JPanel jPanelConfirmCancel;

	private JButton jButtonOk;
	private JButton jButtonCancel;

	private JScrollPane jScrollPaneSetupSelection;
	private JList<String> jListSetupSelection;
	private DefaultListModel<String> simulationSetupListModel;

	private boolean canceled = false;
	private DirectoryPanel directoryPanel;
	private JLabel jLabelFileExportSelection;


	/**
	 * Instantiates a new project export dialog.
	 * @param project the project
	 */
	public ProjectExportDialog(Project project) {
		getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
		this.project = project;
		this.initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {

		this.setTitle(Language.translate("Projekt exportieren"));

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
		gbc_jCheckBoxIncludeProduct.insets = new Insets(5, 10, 5, 10);
		gbc_jCheckBoxIncludeProduct.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxIncludeProduct.gridx = 0;
		gbc_jCheckBoxIncludeProduct.gridy = 1;
		getContentPane().add(getJCheckBoxIncludeInstallationPackage(), gbc_jCheckBoxIncludeProduct);
		
		GridBagConstraints gbc_jLabelFileExportSelection = new GridBagConstraints();
		gbc_jLabelFileExportSelection.insets = new Insets(5, 0, 5, 10);
		gbc_jLabelFileExportSelection.anchor = GridBagConstraints.WEST;
		gbc_jLabelFileExportSelection.gridx = 1;
		gbc_jLabelFileExportSelection.gridy = 1;
		getContentPane().add(getLabel_1(), gbc_jLabelFileExportSelection);

		GridBagConstraints gbc_jComboBoxSelectOS = new GridBagConstraints();
		gbc_jComboBoxSelectOS.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxSelectOS.insets = new Insets(5, 10, 5, 10);
		gbc_jComboBoxSelectOS.gridx = 0;
		gbc_jComboBoxSelectOS.gridy = 2;
		getContentPane().add(getJComboBoxSelectInstallationPackage(), gbc_jComboBoxSelectOS);
		
		GridBagConstraints gbc_directoryPanel = new GridBagConstraints();
		gbc_directoryPanel.insets = new Insets(5, 0, 5, 10);
		gbc_directoryPanel.gridheight = 3;
		gbc_directoryPanel.fill = GridBagConstraints.BOTH;
		gbc_directoryPanel.gridx = 1;
		gbc_directoryPanel.gridy = 2;
		getContentPane().add(getDirectoryPanel(), gbc_directoryPanel);

		GridBagConstraints gbc_jCheckBoxIncludeAllSetups = new GridBagConstraints();
		gbc_jCheckBoxIncludeAllSetups.insets = new Insets(10, 10, 5, 10);
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

		GridBagConstraints gbc_jPanelConfirmCancel = new GridBagConstraints();
		gbc_jPanelConfirmCancel.gridwidth = 2;
		gbc_jPanelConfirmCancel.insets = new Insets(5, 10, 10, 10);
		gbc_jPanelConfirmCancel.fill = GridBagConstraints.BOTH;
		gbc_jPanelConfirmCancel.gridx = 0;
		gbc_jPanelConfirmCancel.gridy = 5;
		getContentPane().add(getJPanelConfirmCancel(), gbc_jPanelConfirmCancel);

		
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
			jLabelHeader = new JLabel(Language.translate("Exportiere Projekt") + " " + project.getProjectName());
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
					visComp.setText(value.toString(false));
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

	private JLabel getLabel_1() {
		if (jLabelFileExportSelection == null) {
			jLabelFileExportSelection = new JLabel(Language.translate("Details zum Dateiexport") + ":");
			jLabelFileExportSelection.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelFileExportSelection;
	}
	/**
	 * Gets the directory panel.
	 * @return the directory panel
	 */
	private DirectoryPanel getDirectoryPanel() {
		if (directoryPanel == null) {
			directoryPanel = new DirectoryPanel(new File(this.project.getProjectFolderFullPath()));
		}
		return directoryPanel;
	}

	/**
	 * Gets the j check box include all setups.
	 * @return the j check box include all setups
	 */
	private JCheckBox getJCheckBoxIncludeAllSetups() {
		if (jCheckBoxIncludeAllSetups == null) {
			jCheckBoxIncludeAllSetups = new JCheckBox(Language.translate("Alle Simulations-Setups exportieren"));
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
			jScrollPaneSetupSelection.setViewportView(this.getJListSetupSelection());
			jScrollPaneSetupSelection.setMinimumSize(new Dimension(380, 200));
		}
		return jScrollPaneSetupSelection;
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
			jListSetupSelection.setSelectionModel(new ListSelectionModelForJCheckBox());

			// --- Initially select all entries ----------------
			this.selectAllListEntries(jListSetupSelection);
		}
		return jListSetupSelection;
	}
	/**
	 * Gets the simulation setup list model.
	 * @return the simulation setup list model
	 */
	private DefaultListModel<String> getSimulationSetupListModel() {
		if (simulationSetupListModel == null) {
			simulationSetupListModel = new DefaultListModel<>();
			Vector<String> setupsVector = new Vector<String>(project.getSimulationSetups().keySet());
			Collections.sort(setupsVector, String.CASE_INSENSITIVE_ORDER);
			for (String setup : setupsVector) {
				simulationSetupListModel.addElement(setup);
			}
		}
		return simulationSetupListModel;
	}
	/**
	 * Selects all entries in the given {@link JList}.
	 * @param list the list
	 */
	private void selectAllListEntries(JList<?> list) {
		int[] selectedIndices = new int[list.getModel().getSize()];
		for (int i = 0; i < selectedIndices.length; i++) {
			selectedIndices[i] = i;
		}
		list.setSelectedIndices(selectedIndices);
	}
	
	/**
	 * Gets the {@link JPanel} containing the ok and cancel buttons
	 * @return the j panel confirm cancel
	 */
	private JPanel getJPanelConfirmCancel() {
		if (jPanelConfirmCancel == null) {
			jPanelConfirmCancel = new JPanel();
			GridBagLayout gbl_jPanelConfirmCancel = new GridBagLayout();
			gbl_jPanelConfirmCancel.columnWidths = new int[] { 0, 0, 0 };
			gbl_jPanelConfirmCancel.rowHeights = new int[] { 0, 0 };
			gbl_jPanelConfirmCancel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_jPanelConfirmCancel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			jPanelConfirmCancel.setLayout(gbl_jPanelConfirmCancel);
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.insets = new Insets(5, 0, 10, 15);
			gbc_jButtonOk.ipadx = 45;
			gbc_jButtonOk.weightx = 0.5;
			gbc_jButtonOk.anchor = GridBagConstraints.EAST;
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			jPanelConfirmCancel.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(5, 15, 10, 0);
			gbc_jButtonCancel.fill = GridBagConstraints.VERTICAL;
			gbc_jButtonCancel.ipadx = 45;
			gbc_jButtonCancel.weightx = 0.5;
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelConfirmCancel.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelConfirmCancel;
	}
	/**
	 * Gets the ok button.
	 * @return the j button ok
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setForeground(new Color(0, 153, 0));
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
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
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}


	/**
	 * Sets the export settings according to the form
	 */
	private void getSettingsFromForm() {
		this.getExportSettings().setIncludeAllSetups(this.getJCheckBoxIncludeAllSetups().isSelected());
		if (this.getExportSettings().isIncludeAllSetups() == false) {
			this.getExportSettings().setSimSetups(this.getJListSetupSelection().getSelectedValuesList());
		} else {
			this.getExportSettings().setSimSetups(null);
		}
		this.getExportSettings().setIncludeInstallationPackage(this.getJCheckBoxIncludeInstallationPackage().isSelected());
		if (this.getExportSettings().isIncludeInstallationPackage() == true) {
			this.getExportSettings().setInstallationPackage((InstallationPackageDescription) this.getJComboBoxSelectInstallationPackage().getSelectedItem());
		} else {
			this.getExportSettings().setInstallationPackage(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
			boolean selectionState = this.getJCheckBoxIncludeAllSetups().isSelected();
			this.getExportSettings().setIncludeAllSetups(selectionState);
			this.getJListSetupSelection().setEnabled(!selectionState);
			if (selectionState == true) {
				this.selectAllListEntries(getJListSetupSelection());
			} else {
				this.getJListSetupSelection().clearSelection();
			}

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
		if (exportSettings == null) {
			exportSettings = new ProjectExportSettings();
		}
		return exportSettings;
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

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6526933726761540148L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean,
		 * boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
			this.setText(value);
			this.setSelected(isSelected);
			this.setEnabled(list.isEnabled());
			return this;
		}
	}

	/**
	 * {@link ListSelectionModel} that allows to add/remove items to/from the current selection without pressing Crtl.
	 * Useful e.g. for lists using {@link JCheckBox} as {@link ListCellRenderer}
	 * 
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class ListSelectionModelForJCheckBox extends DefaultListSelectionModel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1067181018028459081L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.DefaultListSelectionModel#setSelectionInterval(int, int)
		 */
		@Override
		public void setSelectionInterval(int index0, int index1) {
			if (super.isSelectedIndex(index0)) {
				super.removeSelectionInterval(index0, index1);
			} else {
				super.addSelectionInterval(index0, index1);
			}
		}
	}

	
}
